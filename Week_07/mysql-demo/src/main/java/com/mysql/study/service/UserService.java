package com.mysql.study.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysql.study.mapper.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService extends IService<User>{

    //@Select("select * from user")
    List<User> getUsers();

    //@Insert("insert into user values(DEFAULT,#{user.username},#{user.age},#{user.sex},#{user.phone},#{user.email},#{user.card},#{user.password},#{user.register_time},#{user.change_time})")
    boolean save(@Param("user") User user);
}
