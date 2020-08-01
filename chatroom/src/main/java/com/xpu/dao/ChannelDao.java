package com.xpu.dao;

import com.xpu.exception.ChatroomException;
import com.xpu.model.Channel;
import com.xpu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//使用ChannelDao操作channel表
public class ChannelDao {
    //1，新增频道
    public void insert(Channel channel){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "insert into channel values(null,?)";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,channel.getChannelName());
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ChatroomException("新增频道异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("新增频道异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //2.查询所有频道
    public List<Channel> query(){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from channel";
        PreparedStatement p = null;
        ResultSet r = null;
        List<Channel> res = new ArrayList<>();

        try {
            p = c.prepareStatement(sql);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            while (r.next()){
                Channel channel = new Channel();
                channel.setChannelId(r.getInt("channelId"));
                channel.setChannelName(r.getString("channelName"));
                res.add(channel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("查询频道异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return res;
    }

    //3.删除频道
    public void delete(int channelId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "delete from channel where channelId = ?";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setInt(1,channelId);
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ChatroomException("删除频道异常");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("删除频道异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //4.查询频道通过频道名
    public Channel query(String channelName){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "select * from channel where channelName = ?";
        PreparedStatement p = null;
        ResultSet r = null;

        try {
            p = c.prepareStatement(sql);
            p.setString(1,channelName);
            //3.执行SQL
            r = p.executeQuery();
            //4.处理结果集
            while (r.next()){
                Channel channel = new Channel();
                channel.setChannelId(r.getInt("channelId"));
                channel.setChannelName(r.getString("channelName"));
                return channel;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("查询频道异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }
        return null;
    }
}
