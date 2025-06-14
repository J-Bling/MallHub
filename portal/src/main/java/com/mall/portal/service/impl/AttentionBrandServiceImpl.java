package com.mall.portal.service.impl;

import com.mall.common.api.ResultCode;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.PmsBrandMapper;
import com.mall.mbg.model.PmsBrand;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.domain.model.AttentionBrand;
import com.mall.portal.repository.AttentionBrandRepository;
import com.mall.portal.service.AttentionBrandService;
import com.mall.portal.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class AttentionBrandServiceImpl implements AttentionBrandService {
    @Autowired private ConsumerService consumerService;
    @Autowired private PmsBrandMapper brandMapper;
    @Autowired private AttentionBrandRepository brandRepository;
    @Value("mongodb.insert.SqlEnable") private Boolean sqlEnable;

    private final Logger logger = LoggerFactory.getLogger(AttentionBrandServiceImpl.class);

    @Override
    public void add(AttentionBrand attentionBrand) {
        try {
            if (attentionBrand.getBrandId() == null) {
                Assert.fail("参数错误");
            }
            UmsMember member = consumerService.getCurrentMember();
            attentionBrand.setMemberId(member.getId());
            attentionBrand.setMemberNickname(member.getNickname());
            attentionBrand.setMemberIcon(member.getIcon());
            attentionBrand.setCreateTime(new Date());

            AttentionBrand oldAttentionBrand = brandRepository.findByMemberIdAndBrandId(member.getId(), attentionBrand.getBrandId());
            if (oldAttentionBrand != null) {
                Assert.fail("重复操作");
            }

            if (sqlEnable) {
                PmsBrand brand = brandMapper.selectByPrimaryKey(attentionBrand.getBrandId());
                if (brand == null) {
                    Assert.fail("没有该品牌");
                }
                attentionBrand.setBrandStory(brand.getBrandStory());
                attentionBrand.setBrandLogo(brand.getLogo());
                attentionBrand.setBrandName(brand.getName());
            }
            brandRepository.save(attentionBrand);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("添加品牌关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(long brandId) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            brandRepository.deleteByMemberIdAndBrandId(member.getId(), brandId);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("取消品牌关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AttentionBrand> list(int pageNum, int pageSize) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<AttentionBrand> attentionBrandPage = brandRepository.findByMemberId(member.getId(), pageable);
            return attentionBrandPage.getContent();
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("获取品牌关注列表失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AttentionBrand detail(long brandId) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            return brandRepository.findByMemberIdAndBrandId(member.getId(), brandId);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("获取品牌关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void clean() {
        try {
            UmsMember member = consumerService.getCurrentMember();
            brandRepository.deleteAllByMemberId(member.getId());
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("清空品牌关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}