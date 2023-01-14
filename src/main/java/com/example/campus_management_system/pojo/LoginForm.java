package com.example.campus_management_system.pojo;

import lombok.Data;

@Data
public class LoginForm {

    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
