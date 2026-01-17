package com.sky.mapper;

import com.sky.entity.User;
import com.sky.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getByOpenid(String openid);

    void insert(User user);
    
    void updateById(User user);

    User getById(Long id);
}
