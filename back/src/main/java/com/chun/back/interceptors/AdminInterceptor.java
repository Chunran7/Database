package com.chun.back.interceptors;

import com.chun.back.pojo.Admin;
import com.chun.back.pojo.Result;
import com.chun.back.utils.JwtUtil;
import com.chun.back.mapper.AdminMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要管理员登录")));
            return false;
        }
        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;

        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Object type = claims.get("type");
            Object adminId = claims.get("adminId");
            if (!"admin".equals(String.valueOf(type)) || adminId == null) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(mapper.writeValueAsString(Result.error("无管理员权限")));
                return false;
            }
            request.setAttribute("adminClaims", claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要管理员登录")));
            return false;
        }
    }
    private void assertRootAdmin(HttpServletRequest request) {
        Map<String,Object> claims = (Map<String,Object>) request.getAttribute("adminClaims");
        Long adminId = Long.valueOf(claims.get("adminId").toString());
        Admin me = AdminMapper.findBasicById(adminId);
        if (me == null || me.getStatus() == 0 || me.getIsRoot() == 0) {
            throw new RuntimeException("只有初始管理员可以审核");
        }
    }


}
