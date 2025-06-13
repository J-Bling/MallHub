package com.mall.portal.service.impl;

import com.mall.mbg.mapper.UmsMemberReceiveAddressMapper;
import com.mall.mbg.model.UmsMember;
import com.mall.mbg.model.UmsMemberReceiveAddress;
import com.mall.mbg.model.UmsMemberReceiveAddressExample;
import com.mall.portal.service.AddressService;
import com.mall.portal.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired private UmsMemberReceiveAddressMapper receiveAddressMapper;
    @Autowired private ConsumerService consumerService;

    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember member = consumerService.getCurrentMember();
        address.setMemberId(member.getId());
        return receiveAddressMapper.insert(address);
    }

    @Override
    public int delete(Long id) {
        UmsMember member = consumerService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(member.getId()).andIdEqualTo(id);
        return receiveAddressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        address.setId(id);
        UmsMember member = consumerService.getCurrentMember();
        if (address.getDefaultStatus()==null){
            address.setDefaultStatus(0);
        }
        if (address.getDefaultStatus()==1){
            UmsMemberReceiveAddress receiveAddress = new UmsMemberReceiveAddress();
            receiveAddress.setDefaultStatus(0);
            UmsMemberReceiveAddressExample updateExample = new UmsMemberReceiveAddressExample();
            updateExample.createCriteria().andDefaultStatusEqualTo(1).andMemberIdEqualTo(member.getId());
            receiveAddressMapper.updateByExampleSelective(receiveAddress,updateExample);
        }
        return receiveAddressMapper.updateByPrimaryKeySelective(address);
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember member = consumerService.getCurrentMember();
        UmsMemberReceiveAddressExample example =new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        return receiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        return receiveAddressMapper.selectByPrimaryKey(id);
    }
}
