package com.xpu.dao;

import com.xpu.model.Channel;
import org.junit.Test;

import java.util.List;

public class ChannelDaoTest {
    ChannelDao channelDao = new ChannelDao();
    //1.新增频道测试
    @Test
    public void f(){
        Channel channel = new Channel();
        channel.setChannelName("篮球俱乐部");
        channelDao.insert(channel);
    }

    //2.查询所有频道
    @Test
    public void f1(){
        List<Channel> query = channelDao.query();
        for (Channel c :
                query) {
            System.out.println(c);
        }
    }
    //3.删除频道
    @Test
    public void f2(){
        channelDao.delete(1);
    }
}
