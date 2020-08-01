package com.xpu.dao;

import com.xpu.exception.ChatroomException;
import com.xpu.model.User;
import com.xpu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//利用UserDao操作user表
public class UserDao {
    //1.新增用户（注册）
    public void insert(User user){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "insert into user values(null,?,?,?,now())";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,user.getName());
            p.setString(2,user.getPassword());
            p.setString(3,user.getNikeName());
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ChatroomException("新增用户异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("新增用户异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //2.根据用户名查询用户（登录）
    public User query(String userName){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from user where name = ?";
        PreparedStatement p = null;
        ResultSet r = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,userName);
            //3.执行SQL
            r = p.executeQuery();

            //4.处理结果集
            if (r.next()){
                User user = new User();
                user.setUserId(r.getInt("userId"));
                user.setName(r.getString("name"));
                user.setPassword(r.getString("password"));
                user.setNikeName(r.getString("nikeName"));
                user.setLastLogout(r.getTimestamp("lastLogout"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("根据用户名查询用户异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return null;
    }

    //3.根据用户id查询用户（将id转换为昵称）
    public User queryById(int userId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from user where userId = ?";
        PreparedStatement p = null;
        ResultSet r = null;

        try {
            p = c.prepareStatement(sql);
            p.setInt(1,userId);
            //3.执行SQL
            r = p.executeQuery();

            //4.处理结果集
            if (r.next()){
                User user = new User();
                user.setUserId(r.getInt("userId"));
                user.setName(r.getString("name"));
                user.setPassword(r.getString("password"));
                user.setNikeName(r.getString("nikeName"));
                user.setLastLogout(r.getTimestamp("lastLogout"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("根据用户id查询用户异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return null;
    }

    //4.更新用户的lastLogout时间(用户下线时)
    public void updateTime(int userId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "update user set lastLogout = now() where userId = ?";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setInt(1,userId);
            //执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ChatroomException("更新用户的lastLogout时间异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("更新用户的lastLogout时间异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }
}
