package com.xpu.servlet;

import com.xpu.dao.UserDao;
import com.xpu.model.Result;
import com.xpu.model.User;
import com.xpu.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取数据
        User user = JSONUtil.read(request.getInputStream(), User.class);
        //查询数据库
        UserDao userDao = new UserDao();
        User query = userDao.query(user.getName());
        Result result = new Result();
        //进行数据处理(判断是否成功)
        if(query!=null && query.getPassword().equals(user.getPassword())){
            result.ok = 1;
            result.setUserId(query.getUserId());
            result.setName(query.getName());
            result.setNikeName(query.getNikeName());
            //登录成功设置session
            HttpSession session = request.getSession();
            session.setAttribute("user",query);
        }else {
            result.ok = 0;
            result.reason = "账号密码错误";
        }

        //返回数据给前端
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.write(result));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取session
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        //返回数据
        Result result = new Result();
        if (user != null){
            result.ok = 1;
            result.setUserId(user.getUserId());
            result.setName(user.getName());
            result.setNikeName(user.getNikeName());
        }else {
            result.ok = 0;
            result.reason = "尚未登录";
        }
        //返回数据给前端
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.write(result));
    }
}
