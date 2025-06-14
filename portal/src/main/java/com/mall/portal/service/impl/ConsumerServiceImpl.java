package com.mall.portal.service.impl;

import com.mall.common.api.ResultCode;
import com.mall.common.exception.ApiException;
import com.mall.common.util.EncryptionUtil;
import com.mall.common.util.SmsUtil;
import com.mall.mbg.mapper.UmsMemberLevelMapper;
import com.mall.mbg.mapper.UmsMemberMapper;
import com.mall.mbg.model.UmsMember;
import com.mall.mbg.model.UmsMemberExample;
import com.mall.mbg.model.UmsMemberLevel;
import com.mall.mbg.model.UmsMemberLevelExample;
import com.mall.portal.cache.ConsumerCacheService;
import com.mall.portal.dao.ConsumerDao;
import com.mall.portal.domain.model.ConsumerDetail;
import com.mall.portal.service.ConsumerService;
import com.mall.security.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private SmsUtil smsUtil;
    @Autowired private UmsMemberMapper memberMapper;
    @Autowired private ConsumerDao consumerDao;
    @Autowired private UmsMemberLevelMapper levelMapper;
    @Autowired private ConsumerCacheService consumerCacheService;

    private final Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public UmsMember getMemberById(Long id) {
        try {
            return this.getMemberByIdWithRetry(id, 0);
        }catch (Exception e){
            logger.error("获取用户信息失败 : {}",e.getMessage());
            return null;
        }
    }

    private UmsMember getMemberByIdWithRetry(Long id,int retryCount){
        UmsMember member = consumerCacheService.getMember(id);
        if (member!=null) {
            return member.getId()==null ? null : member;
        }
        String lockKey =ConsumerCacheService.MemberLockKey+id;
        boolean isLock = consumerCacheService.tryLock(lockKey);
        if (isLock){
            try{
                member = getById(id);
                if (member==null){
                    consumerCacheService.setMember(id,ConsumerCacheService.defaultNULL);
                }else{
                    consumerCacheService.setMember(member);
                }
                return member;
            }finally {
                consumerCacheService.unLock(lockKey);
            }
        }
        if (retryCount < ConsumerCacheService.retryCount) {
            try {
                Thread.sleep(ConsumerCacheService.defaultLockTime);
                return this.getMemberByIdWithRetry(id, retryCount + 1);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void increaseIntegration(Long id, Integer incrementIntegration) {
        consumerDao.increaseIntegration(id,incrementIntegration);
        consumerCacheService.delMember(id); //删除缓存防止脏读
    }

    /**
     * username实际上是 Id.toString()
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsMember member = this.getMemberById(Long.parseLong(username));
        if (member!=null) {
            return new ConsumerDetail(member);
        }
        return null;
    }

    @Override
    public void register(String username, String password, String telephone, String authCode) {
        try {
            UmsMemberExample example = new UmsMemberExample();
            example.createCriteria().andPhoneEqualTo(telephone);
            long count = memberMapper.countByExample(example);
            if (count > 0) {
                throw new ApiException("该账号已经注册", ResultCode.BAD_REQUEST);
            }
            if (!isValidCode(authCode, telephone)) {
                throw new ApiException("验证码无效", ResultCode.BAD_REQUEST);
            }
            String salt = EncryptionUtil.generateSalt();
            String pass = EncryptionUtil.encryption(salt, password);

            UmsMember member = new UmsMember();
            member.setUsername(username);
            member.setPassword(pass);
            member.setPhone(telephone);
            member.setSalt(salt);
            member.setCreateTime(new Date());
            member.setStatus(1);
            UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
            levelExample.createCriteria().andDefaultStatusEqualTo(1);
            List<UmsMemberLevel> memberLevels = levelMapper.selectByExample(levelExample);
            if (memberLevels != null && !memberLevels.isEmpty()) {
                member.setMemberLevelId(memberLevels.get(0).getId());
            }
            memberMapper.insert(member);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e){
            logger.error("注册用户发送错误 : {}",e.getMessage());
            throw new ApiException("注册失败",ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async("taskExecutor")
    public void sendAuthCode(String telephone) {
        try {
            String code = smsUtil.generateCode();
            smsUtil.send(telephone, code);
            consumerCacheService.setCode(telephone,code);
        }catch (Exception e){
            logger.error("发送验证码发送错误 : {}",e.getMessage());
            throw e;
        }
    }


    @Override
    public void updatePassword(String telephone, String password, String authCode) {
        try {
            UmsMemberExample example = new UmsMemberExample();
            example.createCriteria().andPhoneEqualTo(telephone);
            List<UmsMember> members = memberMapper.selectByExample(example);
            if (members == null || members.isEmpty()) {
                throw new ApiException("没有该用户", ResultCode.BAD_REQUEST);
            }

            if (!isValidCode(authCode, telephone)) {
                throw new ApiException("验证码无效", ResultCode.BAD_REQUEST);
            }

            UmsMember member = members.get(0);
            String salt = member.getSalt();
            String newPass = EncryptionUtil.encryption(salt, password);
            UmsMember member1 = new UmsMember();
            member1.setId(member.getId());
            member1.setPassword(newPass);
            memberMapper.updateByPrimaryKeySelective(member1);
            consumerCacheService.setMember(member);
        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception e){
            logger.error("修改密码失败 : {}",e.getMessage());
            throw new ApiException("修改密码失败",ResultCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public String login(String telephone, String password) {
        UmsMemberExample example=  new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> members = memberMapper.selectByExample(example);
        if (members==null || members.isEmpty()) {
            throw new ApiException("该用户没有注册", ResultCode.BAD_REQUEST);
        }

        UmsMember member = members.get(0);
        String salt = member.getSalt();
        String originPass = member.getPassword();
        boolean isValid = EncryptionUtil.isValid(salt,originPass,password);

        if (!isValid){
            throw new ApiException("密码错误",ResultCode.BAD_REQUEST);
        }

        ConsumerDetail consumerDetail =new ConsumerDetail(member);
        return jwtUtil.generateToken(consumerDetail);
    }

    @Override
    public UmsMember getCurrentMember() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        ConsumerDetail consumerDetail = (ConsumerDetail) authentication.getPrincipal();
        return consumerDetail.getMember();
    }

    @Override
    public String refreshToken(String token) {
        return jwtUtil.refreshHeaderToken(token);
    }

    /**
     * 验证 验证码有效性
     */
    private boolean isValidCode(String code,String phone){
        if (code!=null && !code.isEmpty()){
            boolean valid=  code.equals(consumerCacheService.getCode(phone));
            if (valid){
                consumerCacheService.delCode(phone);
            }
            return valid;
        }
        return false;
    }
}
