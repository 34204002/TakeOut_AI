package com.sky.controller.user;


import com.sky.result.Result;
import com.sky.service.impl.SetmealServiceImpl;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * The type Setmeal controller.
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealServiceImpl setmealService;
    /**
     * 根据分类id查询套餐
     *
     * @return the result
     */
    @GetMapping("/list")
    public Result<List<SetmealVO>> list(Integer categoryId){
        log.info("根据分类id查询套餐:{}",categoryId);
        List<SetmealVO> list = setmealService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param id the id
     * @return the result
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dish(@PathVariable Long id){
        log.info("根据套餐id查询套餐下的菜品:{}",id);
        List<DishItemVO> list = setmealService.getSetmealDishBySetmealId(id);
        return Result.success(list);
    }
}
