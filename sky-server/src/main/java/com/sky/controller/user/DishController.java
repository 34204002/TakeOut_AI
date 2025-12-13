package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The type Dish controller.
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     *
     * @param categoryId the category id
     * @return the result
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);

        //构造redis中的key
        String key = "dish_" + categoryId;
        //查询Redis中缓存的菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        //存在直接返回
        if(list != null && list.size() > 0){
            return Result.success(list);
        }
        //不存在
        else {
            list = dishService.getByCategoryId(categoryId);
            //将数据库查询到的数据加入Redis中
            redisTemplate.opsForValue().set(key,list,60*60*24,TimeUnit.SECONDS);
            return Result.success(list);
        }
    }

}
