package com.mysql.study;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Insert1MillionData {

    /*
        CREATE TABLE `user` (
          `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
          `username` varchar(30) NOT NULL COMMENT '用户名称',
          `age` tinyint unsigned NOT NULL COMMENT '用户年龄',
          `sex` tinyint NOT NULL COMMENT '性别 0表示男，1表示女',
          `phone` char(11) NOT NULL COMMENT '用户电话',
          `email` varchar(50) NOT NULL COMMENT '邮箱',
          `card` char(18) DEFAULT NULL COMMENT '身份证号',
          `password` varchar(32) NOT NULL COMMENT '密码',
          `register_time` bigint NOT NULL COMMENT '注册时间',
          `change_time` bigint NOT NULL COMMENT '修改时间',
          PRIMARY KEY (`id`),
          UNIQUE KEY `ph_pass` (`phone`,`password`)
        ) ENGINE=InnoDB AUTO_INCREMENT=4000001 DEFAULT CHARSET=utf8
     */

    //直接通过jdbc插入一百条数据
    //1. 没有开批量写入,花了接近10分钟
    //2. 关闭自动提交，手动提交花了3分钟多
    //3. 关闭自动提交 并配置url中开启rewriteBatchedStatements=true 1分钟
    //4. 保留主键索引，去掉唯一索引，差不多也需要1分钟
    //5. 字段越少，插入的数据越快
    public static void main(String[] args) throws SQLException {
        insertUserA();
    }


    public static void insertUserA(){
        long start = System.currentTimeMillis();
        DataSource dataSource = JDBCManager.getInstance().getBasicDataSource();
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            //注意这里在sql最后不要写；号，否则批量执行的时候回报错
            String sql = "insert into user values(DEFAULT,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            insertContent(preparedStatement);
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("cost time : "+ (System.currentTimeMillis() - start));
    }

    public static void insertUserB(){
        long start = System.currentTimeMillis();
        DataSource dataSource = JDBCManager.getInstance().getBasicDataSource();
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            //注意这里在sql最后不要写；号，否则批量执行的时候回报错
            String sql = "insert into test values(DEFAULT,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            Random random =new Random();
            for(int i = 0; i<1000000 ; i ++){
                preparedStatement.setInt(1,random.nextInt(1000000) );
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("cost time : "+ (System.currentTimeMillis() - start));
    }


    private static void insertContent(PreparedStatement preparedStatement) throws SQLException {
        insertContent(preparedStatement,1000000);
    }

    public static void insertContent(PreparedStatement preparedStatement, int count) throws SQLException {
        for(int i = 0; i<count ; i ++){
            preparedStatement.setString(1,"zhangsan" + i);
            preparedStatement.setInt(2,i%80);
            preparedStatement.setInt(3,i%2);
            preparedStatement.setString(4,getPhone(i) );
            preparedStatement.setString(5,getPhone(i)+"@163.com" );
            preparedStatement.setString(6,getCard(i) );
            preparedStatement.setString(7,"qwer"+1 );
            preparedStatement.setLong(8,System.currentTimeMillis() );
            preparedStatement.setLong(9,System.currentTimeMillis() );
            preparedStatement.addBatch();
        }
    }



    public static String getPhone(int i){
        return String.valueOf(13983147719L-i);
    }

    public static String getCard(int i){
        return String.valueOf(300227208905116614L-i);
    }
}
