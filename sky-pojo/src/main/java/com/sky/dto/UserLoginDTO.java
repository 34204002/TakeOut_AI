package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {

    private String code;
    
    // 添加用户信息字段
    private String name; // 用户昵称
    private String avatar; // 用户头像
    private String sex; // 用户性别
    private String phone; // 用户手机号

}
