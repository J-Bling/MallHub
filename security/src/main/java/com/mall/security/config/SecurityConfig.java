package com.mall.security.config;

import com.mall.security.component.JwtAuthenticationTokenFilter;
import com.mall.security.component.RestAuthenticationEntryPoint;
import com.mall.security.component.RestfulAccessDeniedHandler;
import com.mall.security.component.dynamic.DynamicSecurityFilter;
import com.mall.security.component.dynamic.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired private WhitelistUrls whitelistUrls;
    @Autowired private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired(required = false) private DynamicSecurityService dynamicSecurityService;
    @Autowired(required = false) private DynamicSecurityFilter dynamicSecurityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                httpSecurity.authorizeRequests();

        //允许白名单访问
        for (String path : whitelistUrls.getUrls()){
            registry.antMatchers(path).permitAll();
        }
//        允许跨域请求的OPTIONS请求
//        registry.antMatchers(HttpMethod.OPTIONS)
//                .permitAll();

        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                //跨站请求防护及不使用session
                .and()
//                .csrf()
//                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //自定义权限拒绝处理类
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler) //处理403
                .authenticationEntryPoint(restAuthenticationEntryPoint) //验证入口点
                //自定义权限拦截器JWT过滤器
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //有动态权限配置时添加动态权限校验过滤器
        if(dynamicSecurityService!=null){
            registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);
        }

        return httpSecurity.build();
    }
}
