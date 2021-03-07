package com.mysql.study.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.study.mapper.User;
import com.mysql.study.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
    @Override
    public List<User> getUsers() {
        return baseMapper.selectList(Wrappers.<User>lambdaQuery());
    }

    @Override
    public boolean save(User user) {
        return super.save(user);
    }
}
