package com.space.rabbitmq.controller;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HomeController {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
    @RequestMapping("/")
    public void myhome(HttpServletResponse response){
	try {
            response.sendRedirect("/login.html");
	}catch (Exception e){
	    e.printStackTrace();
	}

    }
}
