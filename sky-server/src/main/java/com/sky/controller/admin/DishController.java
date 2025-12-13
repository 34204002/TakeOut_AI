package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品 管理
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    /**
     * The Dish service.
     */
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品.
     *
     * @param dishDTO the dish dto
     * @return the result
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询.
     *
     * @param dto the dto
     * @return the result
     */
    @GetMapping("/page")
    public Result<PageResult> DishPageQuery(DishPageQueryDTO dto){
        log.info("分页查询菜品:{}",dto);
        PageResult pageResult = dishService.DishPageQuery(dto);
        return Result.success(pageResult);

    }
    /**
     * 删除菜品
     *
     *
     * @return the result
     */
    @DeleteMapping
    public Result<String> deleteByIds (@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteByIds(ids);
        return Result.success();
    }
    /**
     * 修改菜品
     * @return the result
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishService.update(dishDTO);

        cleanCache("dish_*");

        return Result.success();
    }
    /**
     * 根据id查询菜品
     * @return the result
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }
    /**
     * 根据id起售停售菜品
     * @return the result
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status,Long id){
        log.info("起售停售菜品:{}",id);
        dishService.startOrStop(status,id);

        cleanCache("dish_*");

        return Result.success();
    }
    /**
     * 根据categoryId查询菜品
     * @return the result
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据categoryId查询菜品:{}",categoryId);
        List<DishVO> list = dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    private void cleanCache(String pattern){
        log.info("清理缓存:{}",pattern);
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }private void cleanCache(Integer categoryId){
        String key="dish_"+categoryId;
        log.info("清理缓存:{}",key);
        redisTemplate.delete(key);
    }
}
