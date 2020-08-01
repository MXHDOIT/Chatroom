package com.xpu.webSocket;

import com.google.gson.Gson;
import com.xpu.dao.MessageDao;
import com.xpu.dao.UserDao;
import com.xpu.model.Message;
import com.xpu.model.User;
import com.xpu.util.JSONUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

// 用来实现 websocket 接口
// 此处 userId 是一个变量. 客户端连接的时候, 具体提交的 userId 可能都有差异.
// 服务器需要获取到具体的 userId 是啥.
@ServerEndpoint(value = "/message/{userId}")
public class MessageAPI {

    private int userId;
    @OnOpen
    public void onOpen(@PathParam("userId") String userIdStr, Session session) throws IOException {
        //获取userId
        this.userId = Integer.parseInt(userIdStr);
        System.out.println("建立连接："+userId);
        //添加用户
        MessageCenter.getInstance().addOnlineUser(userId,session);
        //获取不在线的消息列表
        //获取下线时间
        UserDao userDao = new UserDao();
        User user = userDao.queryById(userId);

        MessageDao messageDao = new MessageDao();
        List<Message> messages = messageDao.selectByTimeStamp(user.getLastLogout(), new Timestamp(System.currentTimeMillis()));
        //发送消息给前端
        for (Message message :
                messages) {
            session.getBasicRemote().sendText(JSONUtil.write(message));
        }
    }

    @OnClose
    public void onClose(){
        System.out.println("断开连接："+userId);
        //把用户从在线列表中删掉
        MessageCenter.getInstance().delOnlineUser(userId);
        //更新下线时间
        UserDao userDao = new UserDao();
        userDao.updateTime(userId);
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("连接出现错误："+userId);
        error.printStackTrace();
        //把用户从在线列表中删掉
        MessageCenter.getInstance().delOnlineUser(userId);
        //更新下线时间
        UserDao userDao = new UserDao();
        userDao.updateTime(userId);
    }

    @OnMessage
    public void onMessage(String request, Session session) throws InterruptedException {
        System.out.println("收到消息："+userId);
        //获取消息
        Message message = new Gson().fromJson(request, Message.class);
        message.setSendTime(new Timestamp(System.currentTimeMillis()));
        //存入发送者
        UserDao userDao = new UserDao();
        User user = userDao.queryById(userId);
        message.setNickName(user.getNikeName());
        //添加消息
        MessageCenter.getInstance().addMessage(message);
        //写入数据库
        MessageDao messageDao = new MessageDao();
        messageDao.insert(message);
    }
}
