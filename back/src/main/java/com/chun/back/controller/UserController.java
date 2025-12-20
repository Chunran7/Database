package com.chun.back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chun.back.pojo.Result;
import com.chun.back.service.UserService;
import com.chun.back.utils.JwtUtil;
import com.chun.back.utils.Md5Util;

import jakarta.validation.constraints.Pattern;

import com.chun.back.pojo.User;

/**
 * 用户控制器类 - 处理用户相关的HTTP请求
 * 
 * Spring Boot创建Controller的一般过程：
 * 1. 创建一个类并使用@RestController或@Controller注解标记
 * 2. 使用@RequestMapping定义类级别的URL映射
 * 3. 在方法上使用具体的HTTP请求映射注解，如@GetMapping、@PostMapping等
 * 4. 使用@Autowired注入需要的服务层对象
 * 5. 编写处理具体业务逻辑的方法并返回相应结果
 * 
 * 常用注解说明：
 * - @RestController: 组合注解，相当于@Controller和@ResponseBody，用于构建RESTful Web服务，
 *   表示该类是一个控制器，且每个方法的返回值都会直接写入HTTP响应体中
 * - @RequestMapping: 用于映射Web请求到具体的处理类或方法，可以指定URL路径和HTTP方法类型
 *   在类上使用表示该类下所有方法的公共路径前缀
 * - @PostMapping: 组合注解，相当于@RequestMapping(method = RequestMethod.POST)，
 *   明确指定处理POST请求的方法，用于创建资源
 * - @Autowired: Spring框架提供的注解，用于自动装配依赖对象，
 *   默认按类型进行装配，会自动从Spring容器中查找匹配的对象并注入
 * - @Pattern: Bean Validation规范中的注解，用于校验字符串是否符合指定的正则表达式，
 *   这里用于校验用户名和密码格式
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username,@Pattern(regexp = "^\\S{5,16}$")String password){
        // 查询用户
            User u=userService.findByUserName(username);
        if(u==null){
            //没有占用
            userService.register(username,password);
            return Result.success();
        }
        else{
            //已经被占用
            return Result.error("用户名已被占用");
        }
    }


    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$")String username,@Pattern(regexp = "^\\S{5,16}$")String password){
        User u=userService.findByUserName(username);
        if(u!=null){
            //用户名存在
            if(Md5Util.getMD5String(password).equals(u.getPassword())){
                //密码正确
                Map<String,Object> claims=new HashMap<>();
                claims.put("id",u.getId());
                claims.put("username",u.getUsername());
                String token=JwtUtil.genToken(claims);
                return Result.success(token);
            }else{
                //密码错误
                return Result.error("密码错误");
            }
        }
        else{
            //用户名不存在
            return Result.error("用户名不存在");
        }
    }
}