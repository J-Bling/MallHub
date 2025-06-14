package com.mall.portal.dao;

import org.apache.ibatis.annotations.Update;

public interface ConsumerDao {
    @Update("update ums_member set integration = integration + #{incrementIntegration} where id = #{consumerId}")
    void increaseIntegration(long consumerId , int incrementIntegration);
}
