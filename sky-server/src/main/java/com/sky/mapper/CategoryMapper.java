package com.sky.mapper;


import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    void addCategory(Category category);

    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(Category category);

    List<Category> GetByType(Integer type);

    void deleteById(Long id);
}
