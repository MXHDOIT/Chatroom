package com.xpu.servlet;

import com.xpu.dao.UserDao;
import com.xpu.exception.ChatroomException;
import com.xpu.model.Result;
import com.xpu.model.User;
import com.xpu.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取数据将其反序列化为User对象
        request.setCharacterEncoding("utf-8");
        User user = JSONUtil.read(request.getInputStream(), User.class);
        //查询数据库看是否存在
        UserDao userDao = new UserDao();
        User queryUser = userDao.query(user.getName());
        Result result = new Result();   //返回数据
        System.out.println(queryUser);
        try {
            if (queryUser != null){
                throw new ChatroomException("用户存在");
            }else {
                userDao.insert(user);
                result.ok = 1;
            }
        } catch (ChatroomException e) {
            e.printStackTrace();
            result.ok = 0;
            result.reason = "已注册";
        }finally {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.write(result));
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
