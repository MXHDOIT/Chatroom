package com.xpu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpu.exception.ChatroomException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class JSONUtil {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    //读取输入流的json数据，反序列化为对象
    //泛型的操作：<T>方法上定义泛型类型，返回值和输入参数可以使用泛型
    public static <T> T read(InputStream is, Class<T> claszz){
        try {
            return MAPPER.readValue(is,claszz);
        } catch (IOException e) {
            throw new ChatroomException("反序列化异常");
        }
    }

    //序列化
    public static String write(Object o){
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ChatroomException("json序列化异常");
        }
    }
}
