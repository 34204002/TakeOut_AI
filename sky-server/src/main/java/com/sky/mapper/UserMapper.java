package com.sky.mapper;

import com.sky.entity.User;
import com.sky.entity.UserReport;
import com.sky.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {
    User getByOpenid(String openid);

    void insert(User user);
    
    void updateById(User user);

    User getById(Long id);

    List<UserReport> getUserStatistics(LocalDate begin, LocalDate end);

    Long countByCreateTimeBefore(LocalDate time);
}
