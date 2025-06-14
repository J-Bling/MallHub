package com.mall.portal.service.impl;

import com.mall.common.api.ResultCode;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.domain.model.AttentionProduct;
import com.mall.portal.repository.AttentionProductRepository;
import com.mall.portal.service.AttentionProductService;
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
public class AttentionProductServiceImpl implements AttentionProductService {
    @Value("mongodb.insert.SqlEnable") private Boolean sqlEnable;
    @Autowired private ConsumerService consumerService;
    @Autowired private AttentionProductRepository productRepository;
    @Autowired private PmsProductMapper productMapper;

    private final Logger logger = LoggerFactory.getLogger(AttentionProductServiceImpl.class);

    @Override
    public void add(AttentionProduct attentionProduct) {
        try {
            if (attentionProduct.getProductId() == null) {
                Assert.fail("参数错误");
            }
            UmsMember member = consumerService.getCurrentMember();
            attentionProduct.setMemberId(member.getId());
            attentionProduct.setMemberNickname(member.getNickname());
            attentionProduct.setMemberIcon(member.getIcon());
            attentionProduct.setCreateTime(new Date());

            AttentionProduct oldAttentionProduct = productRepository.findByMemberIdAndProductId(member.getId(), attentionProduct.getProductId());
            if (oldAttentionProduct != null) {
                Assert.fail("错误操作");
            }

            if (sqlEnable) {
                PmsProduct product = productMapper.selectByPrimaryKey(attentionProduct.getProductId());
                if (product == null || product.getDeleteStatus() == 1) {
                    Assert.fail("该商品不存在");
                }
                attentionProduct.setProductPic(product.getPic());
                attentionProduct.setProductName(product.getName());
                attentionProduct.setProductPrice("" + product.getPrice());
                attentionProduct.setProductSubTitle(product.getSubTitle());
            }

            productRepository.save(attentionProduct);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("添加商品关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(long productId) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            productRepository.deleteByMemberIdAndProductId(member.getId(), productId);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("删除商品关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AttentionProduct> list(int pageNum, int pageSize) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<AttentionProduct> productPage = productRepository.findByMemberId(member.getId(), pageable);
            return productPage.getContent();
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("获取商品关注列表失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AttentionProduct detail(long productId) {
        try {
            UmsMember member = consumerService.getCurrentMember();
            return productRepository.findByMemberIdAndProductId(member.getId(), productId);
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("获取商品关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void clean() {
        try {
            UmsMember member = consumerService.getCurrentMember();
            productRepository.deleteAllByMemberId(member.getId());
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("清空商品关注失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
