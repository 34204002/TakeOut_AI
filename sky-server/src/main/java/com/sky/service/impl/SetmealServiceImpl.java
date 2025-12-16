package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private DishMapper dishMapper;


    /**
     * 新增套餐
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
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteBatchByIds(List<Long> ids) {
        if(ids != null && !ids.isEmpty())
            ids.forEach(id -> {
                Setmeal setmeal = setmealMapper.getById(id);
                if(setmeal.getStatus() == 1)
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            });
        setmealMapper.deleteBatchByIds(ids);
        setmealDishMapper.deleteBatchBySetmealIds(ids);

    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
       Setmeal setmeal = setmealMapper.getById(id);
       SetmealVO setmealVO = new SetmealVO();
       BeanUtils.copyProperties(setmeal, setmealVO);
       setmealVO.setSetmealDishes(setmealDishMapper.getSetmealDishBySetmealId(id));
       setmealVO.setCategoryName(categoryMapper.getById(setmeal.getCategoryId()).getName());
       return setmealVO;
    }

    /**
     * 修改套餐
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
     * 启售/停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        setmealMapper.update(Setmeal.builder().status(status).id(id).build());
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public List<SetmealVO> getByCategoryId(Integer categoryId) {
        List<SetmealVO> list = setmealMapper.getByCategoryId(categoryId);
        if (list != null) {
            list.forEach(setmealVO -> {
                if (setmealVO != null) {
                    if (categoryMapper.getById(setmealVO.getCategoryId()) != null) {
                        setmealVO.setCategoryName(categoryMapper.getById(setmealVO.getCategoryId()).getName());
                    }
                    setmealVO.setSetmealDishes(setmealDishMapper.getSetmealDishBySetmealId(setmealVO.getId()));
                }
            });
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getSetmealDishBySetmealId(Long id) {

        SetmealVO setmealVO = this.getById(id);

        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        List<DishItemVO> list=new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            Dish dish = dishMapper.getById(setmealDish.getDishId());
            DishItemVO dishItemVO = new DishItemVO();
            BeanUtils.copyProperties(dish,dishItemVO);
            dishItemVO.setCopies(setmealDish.getCopies());
            list.add(dishItemVO);
        }
        return list;
    }
}
