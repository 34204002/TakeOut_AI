package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.apache.poi.ss.formula.functions.BaseNumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.catalog.Catalog;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Category service.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 增加分类
     *
     * @param categoryDTO
     */
    public void addCategory(CategoryDTO categoryDTO) {
        Category category=Category.builder()
                .status(1)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .createUser(BaseContext.getCurrentId())
                .updateUser(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.addCategory(category);
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page< Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        categoryMapper.pageQuery(categoryPageQueryDTO);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());

        return pageResult;
    }

    /**
     * 修改分类状态
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 更新分类信息
     *
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> GetByType(Integer type) {

        List<Category> list = categoryMapper.GetByType(type);
        return list;
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }
}
