package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    public void addCategory(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void startOrStop(Integer status, Long id);

    void update(CategoryDTO categoryDTO);

    List<Category> GetByType(Integer type);

    void deleteById(Long id);
}
