package com.sky.ai.service;

import com.sky.dto.CustomerServiceKnowledgeDTO;
import com.sky.result.PageResult;

/**
 * 客服知识库管理服务接口
 */
public interface CustomerServiceKnowledgeService {
    
    /**
     * 分页查询知识库
     */
    PageResult pageQuery(Integer page, Integer pageSize, String category);
    
    /**
     * 新增知识库
     */
    Long add(CustomerServiceKnowledgeDTO dto);
    
    /**
     * 更新知识库
     */
    void update(CustomerServiceKnowledgeDTO dto);
    
    /**
     * 删除知识库
     */
    void deleteById(Long id);
    
    /**
     * 重新加载知识库（同步到向量库）
     */
    void reloadKnowledge();
}
