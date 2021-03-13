package com.sharding.study;


import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class InsertDataOperation {
    @Resource
    DataSource dataSource;

    @Resource
    JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void insetData() {
        Connection connection = null;
        try {
            System.out.println("---------InsertDataOperation----------" + dataSource);
            connection = dataSource.getConnection();
            String sql = "insert into t_order (user_id,order_id,uid,gid,address,create_time,pay_time values(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            insertContent(preparedStatement, 1600);
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public static void insertContent(PreparedStatement preparedStatement, int count) throws SQLException {
        for (int i = 0; i < count; i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, "1000" + String.valueOf(i));
            preparedStatement.setInt(3, i);
            preparedStatement.setInt(4, i);
            preparedStatement.setString(5,  "北京市长安路");
            preparedStatement.setLong(6, System.currentTimeMillis());
            preparedStatement.setLong(7, System.currentTimeMillis());
            preparedStatement.addBatch();
        }
    }



    @Transactional
    @ShardingTransactionType(TransactionType.XA)  // 支持TransactionType.LOCAL, TransactionType.XA, TransactionType.BASE
    public void insert() {
        jdbcTemplate.execute("insert into t_order (user_id,order_id,uid,gid,address,create_time,pay_time values(?,?,?,?,?,?,?)", (PreparedStatementCallback<Object>) ps -> {
            ps.setInt(1, 10000099);
            ps.setString(2, String.valueOf(99));
            ps.setInt(3, 10000099);
            ps.setInt(4, 10000099);
            ps.setString(5,  "北京市长安路");
            ps.setLong(6, System.currentTimeMillis());
            ps.setLong(7, System.currentTimeMillis());
            ps.executeUpdate();
            return ps;
        });

        jdbcTemplate.execute("insert into t_order (user_id,order_id,uid,gid,address,create_time,pay_time values(?,?,?,?,?,?,?)", (PreparedStatementCallback<Object>) ps -> {
            ps.setInt(1, 10000098);
            ps.setString(2, String.valueOf(98));
            ps.setInt(3, 10000098);
            ps.setInt(4, 10000098);
            ps.setString(5,  "北京市长安路");
            ps.setLong(6, System.currentTimeMillis());
            ps.setLong(7, System.currentTimeMillis());
            ps.executeUpdate();
            return ps;
        });
    }

}
