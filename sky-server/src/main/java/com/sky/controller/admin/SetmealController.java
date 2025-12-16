package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.service.impl.SetmealServiceImpl;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("添加套餐:{}",setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }
    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO dto){
        log.info("分页查询:{}",dto);
        PageResult pageResult = setmealService.SetmealPageQuery(dto);
        return Result.success(pageResult);
    }
    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteBatchByIds(@RequestParam List<Long> ids){
        log.info("批量删除:{}",ids);
        setmealService.deleteBatchByIds(ids);
        return Result.success();
    }
    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("查询:{}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }
    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }
    /**
     * 停售/起售套餐
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> startOrStop(@PathVariable Integer status,Long id){
        log.info("起售/停售套餐:{},{}",status,id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
