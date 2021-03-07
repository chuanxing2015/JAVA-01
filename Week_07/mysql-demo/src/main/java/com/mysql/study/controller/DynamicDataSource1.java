package com.mysql.study.controller;


import com.mysql.study.Insert1MillionData;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//@RestController
public class DynamicDataSource1 {

    @Resource
    private DataSource data1Source;

    @Resource
    private DataSource data2Source;


    @GetMapping("insert")
    public void  insert()  {
        Connection connection = null;
        try {
            connection  = data1Source.getConnection();
            String sql = "insert into user values(DEFAULT,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int i = 3;
            preparedStatement.setString(1,"zhangsan" + i);
            preparedStatement.setInt(2,i%80);
            preparedStatement.setInt(3,i%2);
            preparedStatement.setString(4,Insert1MillionData.getPhone(i) );
            preparedStatement.setString(5,Insert1MillionData.getPhone(i)+"@163.com" );
            preparedStatement.setString(6,Insert1MillionData.getCard(i) );
            preparedStatement.setString(7,"qwer"+1 );
            preparedStatement.setLong(8,System.currentTimeMillis() );
            preparedStatement.setLong(9,System.currentTimeMillis() );
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @GetMapping("query")
    public void  query() throws SQLException {
        Connection connection  = data2Source.getConnection();
        String sql = "SELECT *  FROM user";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();
        while (set.next()){
            System.out.println("username : "+set.getString("username"));
            System.out.println("phone : "+set.getString("phone"));
            System.out.println("email : "+set.getString("email"));
            System.out.println("card : "+set.getString("card"));
            System.out.println("-------------------------------");
        }
        set.close();
        preparedStatement.close();
        connection.close();
    }



}
