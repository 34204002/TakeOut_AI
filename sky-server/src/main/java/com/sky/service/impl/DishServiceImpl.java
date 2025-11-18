package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishService dishService;

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
        if(dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty())
            dishFlavorMapper.addBatchDishFlavor(dishDTO.getFlavors());

    }

    /**
     * 菜品分页查询
     *
     * @param dto
     * @return
     */
    @Override
    public PageResult DishPageQuery(DishPageQueryDTO dto) {
        Page<DishVO> page = PageHelper.startPage(dto.getPage(),dto.getPageSize());

        dishMapper.DishPageQuery(dto);

        List<DishVO> dishList = page.getResult();
        Long total = page.getTotal();

        return new PageResult(total,dishList);
    }

    /**
     * @param ids
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByIds(List<Long> ids) {
        //若启售不删除(status=1)
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //若包含在套餐不删除(setMeal)
        for (Long id : ids) {
            List<Long> setmealDish = setmealDishMapper.getSetmealIdByDishId(id);
            if(setmealDish != null &&!setmealDish.isEmpty()){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }

        //删除dish表
        dishMapper.deleteBatchByIds(ids);
        //删除菜品关联dish_flavor表
        dishFlavorMapper.deleteBatchByDishIds(ids);

    }

    /**
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish Dish = new Dish();
        BeanUtils.copyProperties(dishDTO,Dish);
        dishMapper.update(Dish);

        dishFlavorMapper.deleteBatchByDishIds(new ArrayList<Long>() {{
            add(dishDTO.getId());
        }});
        if(dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty()) {
            dishDTO.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.addBatchDishFlavor(dishDTO.getFlavors());
        }
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish=dishMapper.getById(id);
        List<DishFlavor> dishFlavorsList = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavorsList);

        return dishVO;

    }

    /**
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }


}
