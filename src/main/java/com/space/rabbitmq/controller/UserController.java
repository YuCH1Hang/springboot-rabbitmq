package com.space.rabbitmq.controller;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.space.rabbitmq.model.Result;
import com.space.rabbitmq.model.User;
import com.space.rabbitmq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param user 参数封装
     * @return Result
     */
    @PostMapping(value = "/regist")
    public Result regist(User user){
        return userService.regist(user);
    }

    /**
     * 登录
     * @param user 参数封装
     * @return Result
     */
    @PostMapping(value = "/login")
    public Result login(@ModelAttribute("user") User user,HttpSession session){
	Result result = new Result();
	result.setSuccess(false);
	result.setDetail(null);
	try {
              Long userId = userService.login(user);
	          session.setAttribute("username", user.getUsername());
	          result.setSuccess(true);
		  //user.setId(userId);
		  //result.setDetail(user);
	          result.setMsg("login ok.");
	} catch (Exception e) {
	       result.setMsg(e.getMessage());
	       e.printStackTrace();
	}				              
        return result;
    }

    
}

