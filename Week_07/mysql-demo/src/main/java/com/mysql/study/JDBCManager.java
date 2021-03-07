package com.mysql.study;

import org.apache.commons.dbcp.BasicDataSource;


public class JDBCManager {


    private static BasicDataSource dataSource;
    private static String dbName = "study";
    private static final String driverClassName = "com.mysql.jdbc.Driver";
    //private static final String url = "jdbc:mysql://localhost/" + dbName; //+一个具体的数据库名称
    public static final String url = "jdbc:mysql://localhost/" + dbName+"?rewriteBatchedStatements=true"; //+一个具体的数据库名称
    public static final String userName = "root";
    public static final String password = "qwer1234";

    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
    }


    private static JDBCManager jdbcManager = new JDBCManager();

    private JDBCManager(){}

    public static JDBCManager getInstance(){
        return jdbcManager;
    }

    public BasicDataSource getBasicDataSource(){
        return dataSource;
    }
}
