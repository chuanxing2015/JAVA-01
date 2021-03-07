package com.mysql.study.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
public class User extends Model<User> {
    public int id;
    public String username;
    public int age;
    public int sex;
    public String phone;
    public String email;
    public String card;
    public String password;
    public long register_time;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", card='" + card + '\'' +
                ", password='" + password + '\'' +
                ", register_time=" + register_time +
                ", change_time=" + change_time +
                '}';
    }

    private long change_time;

}
