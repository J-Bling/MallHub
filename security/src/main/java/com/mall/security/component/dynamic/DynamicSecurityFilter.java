package com.mall.security.component.dynamic;

import com.mall.security.config.WhitelistUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 动态权限过滤器，用于实现基于路径的动态权限过滤
 */
public class DynamicSecurityFilter extends AbstractSecurityInterceptor implements Filter {
    @Autowired private DynamicSecurityMetadataSource metadataSource;
    @Autowired private WhitelistUrls whitelistUrls;

    @Autowired
    public void setMyAccessDecisionManager(DynamicAccessDecisionManager accessDecisionManager){
        super.setAccessDecisionManager(accessDecisionManager); // 使用改访问策略
    }


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return metadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        FilterInvocation filterInvocation = new FilterInvocation(request,response,chain);
        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.toString())){
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(),filterInvocation.getResponse());
            return;
        }

        PathMatcher pathMatcher =new AntPathMatcher();
        for (String path : whitelistUrls.getUrls()){
            if (pathMatcher.match(path,httpServletRequest.getRequestURI())){
                filterInvocation.getChain().doFilter(filterInvocation.getRequest(),filterInvocation.getResponse());
                return;
            }
        }

        InterceptorStatusToken interceptorStatusToken = super.beforeInvocation(filterInvocation);
        try{
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(),filterInvocation.getResponse());
        }finally {
            super.afterInvocation(interceptorStatusToken,null); //清理方法
        }
    }
}
