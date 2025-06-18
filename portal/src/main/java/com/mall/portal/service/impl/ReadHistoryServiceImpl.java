package com.mall.portal.service.impl;

import com.mall.common.api.ResultCode;
import com.mall.common.exception.ApiException;
import com.mall.common.exception.Assert;
import com.mall.mbg.mapper.PmsProductMapper;
import com.mall.mbg.model.PmsProduct;
import com.mall.mbg.model.UmsMember;
import com.mall.portal.domain.model.ReadHistory;
import com.mall.portal.repository.ReadHistoryRepository;
import com.mall.portal.service.ConsumerService;
import com.mall.portal.service.ReadHistoryService;
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
public class ReadHistoryServiceImpl implements ReadHistoryService {
    @Value("mongodb.insert.SqlEnable") private Boolean sqlEnable;
    @Autowired private ReadHistoryRepository readHistoryRepository;
    @Autowired private ConsumerService consumerService;
    @Autowired private PmsProductMapper productMapper;

    private final Logger logger = LoggerFactory.getLogger(ReadHistoryServiceImpl.class);


    @Override
    public void add(ReadHistory readHistory) {
        try {
            if (readHistory.getProductId()==null){
                Assert.fail("参数错误");
            }
            UmsMember member = consumerService.getCurrentMember();
            readHistory.setMemberIcon(member.getIcon());
            readHistory.setMemberId(member.getId());
            readHistory.setMemberNickname(member.getNickname());
            readHistory.setCreateTime(new Date());

            if (sqlEnable){
                PmsProduct product = productMapper.selectByPrimaryKey(readHistory.getProductId());
                if (product==null || product.getDeleteStatus()==1){
                    Assert.fail("该商品不存在");
                }
                readHistory.setProductPic(product.getPic());
                readHistory.setProductName(product.getName());
                readHistory.setProductPrice(""+product.getPrice());
                readHistory.setProductSubTitle(product.getSubTitle());
            }

            readHistoryRepository.save(readHistory);

        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("商品浏览记录失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(List<String> ids) {
        try{
            if (ids ==null || ids.isEmpty()){
                Assert.fail("参数错误");
            }
            UmsMember member =consumerService.getCurrentMember();
            readHistoryRepository.deleteByMemberIdAndIdIn(member.getId(),ids);

        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("删除商品浏览记录失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ReadHistory> list(int pageNum, int pageSize) {
        try{
            UmsMember member = consumerService.getCurrentMember();
            Pageable pageable = PageRequest.of(pageNum,pageSize);
            Page<ReadHistory> historyPage = readHistoryRepository.findByMemberId(member.getId(),pageable);
            return historyPage.getContent();
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("获取商品浏览记录列表失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void clean() {
        try{
            UmsMember member = consumerService.getCurrentMember();
            readHistoryRepository.deleteAllByMemberId(member.getId());
        }catch (ApiException apiException){
            throw apiException;
        } catch (Exception e) {
            logger.error("清空商品浏览记录失败 : {}",e.getMessage());
            throw new ApiException("服务器错误", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
