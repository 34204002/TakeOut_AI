package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * @param setmealDTO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        SetmealDish setmealDish = new SetmealDish();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.addSetmeal(setmeal);
        setmealDTO.getSetmealDishes().forEach(dish -> {
            dish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.addBatchSetmealDish(setmealDTO.getSetmealDishes());
    }

    /**
     * 套餐分页查询
     *
     * @param dto
     * @return
     */
    @Override
    public PageResult SetmealPageQuery(SetmealPageQueryDTO dto) {
        Page<SetmealVO> page= PageHelper.startPage(dto.getPage(),dto.getPageSize());
        setmealMapper.SetmealPageQuery(dto);
        List<SetmealVO> list = page.getResult();
        Long total=page.getTotal();
        return new PageResult(total,list);

    }
}
