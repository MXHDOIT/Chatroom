package com.xpu.servlet;

import com.xpu.dao.ChannelDao;
import com.xpu.exception.ChatroomException;
import com.xpu.model.Channel;
import com.xpu.model.Result;
import com.xpu.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/channel")
public class ChannelServlet extends HttpServlet {
    //新增频道
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取数据进行封装
        Channel channel = JSONUtil.read(request.getInputStream(), Channel.class);
        //进行数据库操作
        ChannelDao channelDao = new ChannelDao();
        Channel query = channelDao.query(channel.getChannelName());
        //返回数据
        Result result = new Result();
        if (query!=null){
            result.ok = 0;
            result.reason = "频道已存在";
        }else {
            channelDao.insert(channel);
            result.ok = 1;
        }
        //返回数据
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.write(result));
    }

    //获取频道列表
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //查询数据库
        ChannelDao channelDao = new ChannelDao();
        List<Channel> query = channelDao.query();
        //返回数据
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.write(query));
    }

    //删除频道
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //获取数据
        String channelId = req.getParameter("channelId");
        //返回数据
        Result result = new Result();
        //操作数据库
        try {
            ChannelDao channelDao = new ChannelDao();
            channelDao.delete(Integer.parseInt(channelId));
            result.ok = 1;
        } catch (ChatroomException e) {
            e.printStackTrace();
            result.ok = 0;
            result.reason = "删除异常";
        }finally {
            //返回给前端
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(JSONUtil.write(result));
        }

    }
}
