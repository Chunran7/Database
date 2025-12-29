package com.chun.back.config;

import com.chun.back.interceptors.AdminInterceptor;
import com.chun.back.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // ✅ 1) 管理员拦截器：只拦 /admin/**，但放行登录/注册/申请
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/login",
                        "/admin/register",
                        "/admin/apply"
                );

        // ✅ 2) 用户拦截器：拦所有，但必须排除 /admin/**（否则你就会“登录后立刻被踢”）
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 用户自身放行
                        "/user/login",
                        "/user/register",
                        "/user/profile/*",

                        // 公共资源放行（按你项目实际补）
                        "/uploads/**",
                        "/static/**",
                        "/favicon.ico",
                        "/error",

                        // ✅ 关键：排除所有管理员接口
                        "/admin/**"
                );
    }
}
