package com.xpu.dao;

import com.xpu.exception.ChatroomException;
import com.xpu.model.Message;
import com.xpu.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    //1.新增消息
    public void insert(Message message){
        //1.获取链接
        Connection c = DBUtil.getConnection();
        //2.拼接SQL
        String sql = "insert into message values(null,?,?,?,?)";
        PreparedStatement p = null;

        try {
            p = c.prepareStatement(sql);
            p.setInt(1,message.getUserId());
            p.setInt(2,message.getChannelId());
            p.setString(3,message.getContent());
            p.setTimestamp(4,message.getSendTime());
            //3.执行SQL
            int res = p.executeUpdate();
            //4.处理结果集
            if (res != 1){
                throw new ChatroomException("新增消息异常");
            }
            System.out.println("插入成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ChatroomException("新增消息异常");
        }finally {
            //5.关闭连接
            DBUtil.close(c,p);
        }
    }

    //2.查询消息按时间段
    public List<Message> selectByTimeStamp(Timestamp from, Timestamp to) {
        List<Message> messages = new ArrayList<>();
        // 1. 获取到连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        //    MySQL 中的 datetime 类型是可以直接比较大小的.
        String sql = "select * from message where sendTime >= ? and sendTime <= ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            System.out.println("selectByTimeStamp: " + statement);
            // 3. 执行 SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合
            while (resultSet.next()) {
                Message message = new Message();
                message.setMessageId(resultSet.getInt("messageId"));
                message.setUserId(resultSet.getInt("userId"));
                message.setChannelId(resultSet.getInt("channelId"));
                message.setContent(resultSet.getString("content"));
                message.setSendTime(resultSet.getTimestamp("sendTime"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }
}
