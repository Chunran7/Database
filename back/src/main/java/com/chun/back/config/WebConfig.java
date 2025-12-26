package com.chun.back.config;

import com.chun.back.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

                        // 本地上传头像等静态资源
                        "/uploads/**",

                        "/error",
                        "/static/**",
                        "/favicon.ico"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/** 映射到项目运行目录下的 uploads/ 目录
        // 示例：/uploads/avatars/xxx.png -> {user.dir}/uploads/avatars/xxx.png
        String userDir = System.getProperty("user.dir");
        String location = "file:" + userDir + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
