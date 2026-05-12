package com.sky.ai.mapper;

import com.sky.entity.CustomerServiceKnowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerServiceKnowledgeMapper {
    
    /**
     * 查询所有启用的知识库
     */
    List<CustomerServiceKnowledge> listAll();
    
    /**
     * 根据分类查询
     */
    List<CustomerServiceKnowledge> listByCategory(String category);
    
    /**
     * 根据ID查询
     */
    CustomerServiceKnowledge getById(Long id);
    
    /**
     * 插入
     */
    void insert(CustomerServiceKnowledge knowledge);
    
    /**
     * 更新
     */
    void update(CustomerServiceKnowledge knowledge);
    
    /**
     * 删除
     */
    void deleteById(Long id);
    
    /**
     * 分页查询
     */
    List<CustomerServiceKnowledge> listByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    
    /**
     * 总数
     */
    int count();
}
