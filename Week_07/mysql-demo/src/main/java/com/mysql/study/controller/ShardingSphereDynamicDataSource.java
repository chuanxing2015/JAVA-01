package com.mysql.study.controller;


import com.mysql.study.mapper.User;
import com.mysql.study.mapper.UserMapper;
import com.mysql.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ShardingSphereDynamicDataSource {

    @Autowired
    private UserService userService;

    @GetMapping("list-user")
    public String getUser(){
        List<User> users =  userService.getUsers();
        System.out.println(users);
        return "ok";
    }


    @GetMapping("insert-user")
    public String insertUser(){
        User user = new User();
        user.setUsername("lisi2");
        user.setAge(20);
        user.setPhone("14983252319");
        user.setEmail("14983252319@163.com");
        user.setRegister_time(System.currentTimeMillis());
        user.setChange_time(System.currentTimeMillis());
        user.setCard("12433234523232");
        user.setSex(1);
        user.setPassword("dfsd");
        userService.save(user);
        return "ok";
    }

}
