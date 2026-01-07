package com.chun.back.config;

import com.chun.back.interceptors.AdminInterceptor;
import com.chun.back.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Autowired
        private LoginInterceptor loginInterceptor;

        @Autowired
        private AdminInterceptor adminInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {

                // ✅ 1) 管理员拦截器：只拦 /admin/** API接口，放行前端路由和登录/注册/申请
                registry.addInterceptor(adminInterceptor)
                                .addPathPatterns("/api/admin/**")
                                .excludePathPatterns(
                                                "/admin", // 放行前端路由（不带/api前缀）
                                                "/api/admin/login",
                                                "/api/admin/register",
                                                "/api/admin/apply");

                // ✅ 2) 用户拦截器：拦所有，但必须排除 /admin/**（否则你就会“登录后立刻被踢”）
                registry.addInterceptor(loginInterceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns(
                                                // 用户自身放行
                                                "/api/user/login",
                                                "/api/user/register",
                                                // 忘记密码相关接口放行（允许未登录访问）
                                                "/api/user/forgot/**",
                                                "/api/user/profile/*",

                                                // 公共资源放行（按你项目实际补）
                                                "/",
                                                "/index.html",
                                                "/assets/**", // 【关键】放行 Vue 打包出的所有 CSS 和 JS
                                                "/uploads/**",
                                                "/api/uploads/**", // 兼容带/api前缀的资源请求
                                                "/static/**",
                                                "/favicon.ico",
                                                "/error",
                                                "/*.html", // 明确放行根目录下的所有 HTML
                                                "/*.txt", // 如果有 robots.txt
                                                "/*.ico", // 确保各种浏览器图标放行

                                                // Vue History模式前端路由路径放行
                                                "/home",
                                                "/login",
                                                "/article",
                                                "/article/*",
                                                "/video",
                                                "/video/*",
                                                "/forum",
                                                "/post/*",

                                                // ✅ 关键：排除所有管理员接口
                                                "/api/admin/**",
                                                "/admin/**");
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 映射 /uploads/** 和 /api/uploads/** 到磁盘上的 uploads 目录。
                // 兼容两种常见的运行目录：
                // - 在项目根目录运行（uploads 与 back 同级） -> file:../uploads/
                // - 在 back 模块目录运行（uploads 在 back/uploads） -> file:uploads/
                registry.addResourceHandler("/uploads/**", "/api/uploads/**")
                                .addResourceLocations("file:uploads/", "file:../uploads/", "classpath:/static/uploads/")
                                .setCachePeriod(3600);

                // 兼容直接请求 /videos/** 或 /api/videos/**，从 uploads/videos 目录读取
                registry.addResourceHandler("/videos/**", "/api/videos/**")
                                .addResourceLocations("file:uploads/videos/", "file:../uploads/videos/",
                                                "classpath:/static/uploads/videos/")
                                .setCachePeriod(3600);

                // 保持静态资源默认映射
                registry.addResourceHandler("/static/**")
                                .addResourceLocations("classpath:/static/");

                // 添加对Vue打包后assets目录的映射
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/")
                                .setCachePeriod(3600);
        }

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                // 匹配不包含 "." 的所有路径，并转发到 index.html
                // 这样 /admin, /home 等路径都会由前端 Vue 路由接管
                registry.addViewController("/{path:[^\\.]*}")
                                .setViewName("forward:/index.html");
        }

        @Override
        public void configurePathMatch(PathMatchConfigurer configurer) {
                // 为所有 @RestController 注解的类自动添加 /api 前缀
                configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(RestController.class));
        }
}
