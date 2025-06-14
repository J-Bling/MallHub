package com.mall.portal.service;

import com.mall.mbg.model.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;

public interface ConsumerService {
    /**
     * 直接从dataDB根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     *从缓存根据会员编号获取会员
     */
    UmsMember getMemberById(Long id);
    /**
     * 根据会员id修改会员积分
     */
    void increaseIntegration(Long id,Integer incrementIntegration);
    /**
     * 获取用户信息 userName -> userId
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 用户注册
     */
    void register(String username, String password, String telephone, String authCode);

    /**
     * 发送验证码
     */
    void sendAuthCode(String telephone);

    /**
     * 修改密码
     */
    void updatePassword(String telephone, String password, String authCode);
    /**
     * 登录后获取token
     */
    String login(String telephone, String password);
    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();
    /**
     * 刷新token
     */
    String refreshToken(String token);

}
