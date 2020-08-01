package com.xpu.dao;

import com.xpu.model.User;
import org.junit.Test;

public class UserDaoTest {
    UserDao userDao = new UserDao();

    //新增
    @Test
    public void f1(){
        User user = new User();
        user.setName("蔡自桂");
        user.setPassword("123456");
        user.setNikeName("蔡徐坤");
        userDao.insert(user);
    }


    @Test
    public void f2(){
        User user = userDao.queryById(1);
        System.out.println(user);
    }

    @Test
    public void f3(){
        userDao.updateTime(1);
    }
}
