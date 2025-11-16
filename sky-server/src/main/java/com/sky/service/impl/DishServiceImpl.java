package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    /**
     *新增菜品
     *
     * @param dishDTO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.addDish(dish);

        Long dishId = dish.getId();

        dishDTO.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dishId));
        dishFlavorMapper.addBatchDishFlavor(dishDTO.getFlavors());

    }
}
