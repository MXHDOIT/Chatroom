package com.xpu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.xpu.exception.ChatroomException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private final static String URL = "jdbc:mysql://localhost:3306/chatroom";
    private final static String USER = "root";
    private final static String PASSWORD = "root";

    private volatile static DataSource dataSource;


    //初始化数据库连接池
    private static DataSource getDataSource(){
        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null){
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource)dataSource).setURL(URL);
                    ((MysqlDataSource)dataSource).setUser(USER);
                    ((MysqlDataSource)dataSource).setPassword(PASSWORD);
                }
            }
        }

        return dataSource;
    }

    //获取连接
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new ChatroomException("获取数据连接异常");
        }
    }

    //关闭连接
    public static void close(Connection c, PreparedStatement p, ResultSet r){
        try {
            if (r != null){
                r.close();
            }

            if (p != null){
                p.close();
            }

            if (c != null){
                c.close();
            }
        } catch (SQLException throwables) {
            throw new ChatroomException("关闭数据连接异常");
        }
    }


    public static void close(Connection c, PreparedStatement p){
        close(c,p,null);
    }
}
