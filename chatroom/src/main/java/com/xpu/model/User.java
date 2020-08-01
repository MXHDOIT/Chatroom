package com.xpu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class User {

    private int userId;
    private String name;
    private String password;
    private String nikeName;
    private Timestamp lastLogout;
}
