package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //微信服务接口地址
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties wxProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserLoginVO userLogin(UserLoginDTO userLoginDTO) {

        String openid = getByName("openid", userLoginDTO.getCode());

        //3.判断openid是否为空
        if (openid == null) {//登陆失败
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        } else {
            User user = userMapper.getByOpenid(openid);
            if (user == null) {//新用户，注册
                user = User.builder()
                        .openid(openid)
                        .createTime(LocalDateTime.now())
                        .build();
                userMapper.insert(user);
            }
            //生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getUserSecretKey(),
                    jwtProperties.getUserTtl(),
                    claims);
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getId())
                    .openid(user.getOpenid())
                    .token(token)
                    .build();

            return userLoginVO;
        }
    }
    private String getByName(String name, String code){
        //1.调用微信接口服务，获取微信用户信息
        Map<String, String> map = new HashMap<>();
        map.put("appid", wxProperties.getAppid());
        map.put("secret", wxProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);

        //2.解析微信接口返回数据
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString(name);

        return openid;
    }
}
