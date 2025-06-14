package com.mall.portal.domain.model;

import com.mall.mbg.model.UmsMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ConsumerDetail implements UserDetails {
    private UmsMember member;
    private String role = "consumer";

    public ConsumerDetail(){}
    public ConsumerDetail(UmsMember member){
        this.member=member;
    }
    public ConsumerDetail(UmsMember member ,String role){
        this.role=role;
        this.member=member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    public String getSalt(){
        return this.member.getSalt();
    }

    @Override
    public String getUsername() {
        return this.member.getId().toString();
    }

    public String getRealUsername(){
        return this.member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.member.getStatus()==1;
    }

    public UmsMember getMember() {
        return member;
    }

    public void setMember(UmsMember member) {
        this.member = member;
    }
}
