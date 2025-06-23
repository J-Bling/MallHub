package com.mall.admin.service.orders.impl;

import com.mall.admin.service.orders.OmsCompanyAddressService;
import com.mall.mbg.mapper.OmsCompanyAddressMapper;
import com.mall.mbg.model.OmsCompanyAddress;
import com.mall.mbg.model.OmsCompanyAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
