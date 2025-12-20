package com.chun.back.interceptors;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chun.back.utils.JwtUtil;
import com.chun.back.pojo.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 从请求头中获取token（支持 Bearer 前缀）
        String token = request.getHeader("Authorization");
        response.setContentType("application/json;charset=UTF-8");
        if (token == null || token.isEmpty()) {
            // 未提供token
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要登录")));
            return false;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 将解析后的用户信息保存到request中，控制器可以通过request.getAttribute("claims")获取
            request.setAttribute("claims", claims);
            return true; // 解析成功，放行请求
        } catch (Exception e) {
            // 解析失败
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要登录")));
            return false;
        }
    }

}
