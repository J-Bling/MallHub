package com.mall.portal.cache;

import com.mall.common.constant.interfaces.Cache;
import com.mall.mbg.model.UmsMember;

public interface ConsumerCacheService extends Cache {
    long CodeExpired = 300000L;
    String CodeKey = "code-key:";
    String MemberKey = "find-member-key:";
    String MemberLockKey = "member-lock:";
    /**
     * 设置验证码缓存 有效时间5分钟
     */
    void setCode(String phone,String code);
    String getCode(String phone);
    void delCode(String phone);

    void setMember(UmsMember member);
    void setMember(Long id,String NULL);
    UmsMember getMember(Long id);
    void delMember(Long id);

    void unLock(String key);
    boolean tryLock(String key);
}
