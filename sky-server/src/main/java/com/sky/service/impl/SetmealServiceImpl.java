package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
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

import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;


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

    /**
     * @param ids
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatchByIds(List<Long> ids) {
        if(ids != null && !ids.isEmpty())
            ids.forEach(id -> {
                SetmealVO setmealVO = setmealMapper.getById(id);
                if(setmealVO.getStatus() == 1)
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            });
        setmealMapper.deleteBatchByIds(ids);
        setmealDishMapper.deleteBatchBySetmealIds(ids);

    }

    /**
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
       SetmealVO setmealVO = setmealMapper.getById(id);
       setmealVO.setSetmealDishes(setmealDishMapper.getSetmealDishBySetmealId(id));
       setmealVO.setCategoryName(categoryMapper.getById(setmealVO.getCategoryId()).getName());
       return setmealVO;
    }

    /**
     * @param setmealDTO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBatchBySetmealIds(Collections.singletonList(setmealDTO.getId()));
        if (setmealDTO.getSetmealDishes() != null && !setmealDTO.getSetmealDishes().isEmpty()){

            List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

            setmealDishes.forEach(dish -> dish.setSetmealId(setmeal.getId()));

            setmealDishMapper.addBatchSetmealDish(setmealDishes);
        }
    }

    /**
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        setmealMapper.update(Setmeal.builder().status(status).id(id).build());
    }
}
