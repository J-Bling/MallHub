package com.mall.admin.service.content.impl;
import com.mall.admin.service.content.CmsPrefrenceAreaService;
import com.mall.mbg.mapper.CmsPrefrenceAreaMapper;
import com.mall.mbg.model.CmsPrefrenceArea;
import com.mall.mbg.model.CmsPrefrenceAreaExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选管理Service实现类
 */
@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
