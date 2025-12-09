package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Category controller.
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 根据type查询菜品分类
     *
     * @param type the type
     * @return the result
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("查询所有分类:{}",type);
        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }
}
