package com.springboot.study;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class JDBCTest {
    @Resource
    private DataSource dataSource;

    @Resource
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void init () throws SQLException {
        //默认数据源
        System.out.println(dataSource.getClass());
        //获得连接
        Connection connection =   dataSource.getConnection();
        System.out.println(connection);

        //关闭连接
        connection.close();
    }


    @PostConstruct
    public void query () throws SQLException {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from test");
        System.out.println(list);
    }
}
