package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper {
    @AutoFill(value = OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);


    List<Setmeal> SetmealPageQuery(SetmealPageQueryDTO dto);
}
