package com.chun.back.config;

import com.chun.back.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/user/profile/*",

                        "/post/list",
                        "/post/*",
                        "/post/*/comments",

                        "/article/list",
                        "/article/latest",
                        "/article/*",

                        "/video/list",
                        "/video/latest",
                        "/video/*",

                        "/admin/login",

                        "/error",
                        "/static/**",
                        "/favicon.ico"
                );
    }
}
