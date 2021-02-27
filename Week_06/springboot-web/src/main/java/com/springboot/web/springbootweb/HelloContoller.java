package com.springboot.web.springbootweb;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloContoller {

    @RequestMapping("/")
    @ResponseBody
    public String hello(){
        return "hello";
    }
}
