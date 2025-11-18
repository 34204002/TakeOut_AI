package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void addBatchDishFlavor(List<DishFlavor> list);
    void deleteBatchByDishIds(List<Long> dishId);

    List<DishFlavor> getByDishId(Long id);
}
