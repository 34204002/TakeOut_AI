package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    void addDish(DishDTO dishDTO);

    PageResult DishPageQuery(DishPageQueryDTO dto);

    void deleteByIds(List<Long> ids);

    void update(DishDTO dishDTO);

    DishVO getById(Long id);


    void startOrStop(Integer status, Long id);
}
