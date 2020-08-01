package com.xpu.webSocket;

import com.xpu.model.Message;
import com.xpu.util.JSONUtil;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

//管理消息和用户列表，实现消息转发
//这个类作为一个单例即可
public class MessageCenter {
    private volatile static MessageCenter instance;

    public static MessageCenter getInstance(){
        if (instance == null){
            synchronized (MessageCenter.class){
                if (instance == null) {
                    instance = new MessageCenter();
                }
            }
        }
        return instance;
    }

    //里面包含两个重要的数据结构
    //1.保存消息的队列
    private BlockingDeque<Message> messages = new LinkedBlockingDeque<>();
    //2.保存在线用户列表
    private ConcurrentHashMap<Integer, Session> onlineUsers =new ConcurrentHashMap<>();

    //用户上线
    public void addOnlineUser(int userId, Session session) {
        onlineUsers.put(userId, session);
    }
    // 2. 用户下线
    public void delOnlineUser(int userId) {
        onlineUsers.remove(userId);
    }
    // 3. 新增消息
    public void addMessage(Message message) throws InterruptedException {
        messages.put(message);
    }

    //创建一个线程，来一直扫描的队列，吧里面的消息进行转发给在线用户
    private MessageCenter(){
        Thread t = new Thread(){
            @Override
            public void run(){
                while (true){
                    try {
                        // 1. 从队列中尝试取消息
                        //    如果队列为空, 此时 take 就会阻塞
                        Message message = messages.take();
                        // 2. 把消息转成 json 字符串
                        String s = JSONUtil.write(message);
                        for (ConcurrentHashMap.Entry<Integer, Session> entry : onlineUsers.entrySet()) {
                            Session session = entry.getValue();
                            session.getBasicRemote().sendText(s);
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}
