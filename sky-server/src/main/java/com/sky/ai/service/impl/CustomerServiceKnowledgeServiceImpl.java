package com.sky.ai.service.impl;

import com.sky.dto.CustomerServiceKnowledgeDTO;
import com.sky.entity.CustomerServiceKnowledge;
import com.sky.ai.mapper.CustomerServiceKnowledgeMapper;
import com.sky.ai.service.CustomerServiceBotService;
import com.sky.ai.service.CustomerServiceKnowledgeService;
import com.sky.result.PageResult;
import com.sky.vo.CustomerServiceKnowledgeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服知识库管理服务实现类
 */
@Service
@Slf4j
public class CustomerServiceKnowledgeServiceImpl implements CustomerServiceKnowledgeService {
    
    @Autowired
    private CustomerServiceKnowledgeMapper knowledgeMapper;
    
    @Autowired
    private CustomerServiceBotService botService;
    
    @Override
    public PageResult pageQuery(Integer page, Integer pageSize, String category) {
        int offset = (page - 1) * pageSize;
        
        List<CustomerServiceKnowledge> list;
        int total;
        
        if (category != null && !category.trim().isEmpty()) {
            // 按分类查询
            list = knowledgeMapper.listByCategory(category);
            total = list.size();
        } else {
            // 分页查询
            list = knowledgeMapper.listByPage(offset, pageSize);
            total = knowledgeMapper.count();
        }
        
        // 转换为 VO
        List<CustomerServiceKnowledgeVO> voList = list.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
        
        return new PageResult(total, voList);
    }
    
    @Override
    @Transactional
    public Long add(CustomerServiceKnowledgeDTO dto) {
        CustomerServiceKnowledge knowledge = new CustomerServiceKnowledge();
        BeanUtils.copyProperties(dto, knowledge);
        knowledge.setCreateTime(LocalDateTime.now());
        
        knowledgeMapper.insert(knowledge);
        
        // 重新加载向量库
        botService.reloadKnowledge();
        
        log.info("新增知识库成功，ID: {}", knowledge.getId());
        return knowledge.getId();
    }
    
    @Override
    @Transactional
    public void update(CustomerServiceKnowledgeDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("更新时ID不能为空");
        }
        
        CustomerServiceKnowledge knowledge = new CustomerServiceKnowledge();
        BeanUtils.copyProperties(dto, knowledge);
        knowledge.setUpdateTime(LocalDateTime.now());
        
        knowledgeMapper.update(knowledge);
        
        // 重新加载向量库
        botService.reloadKnowledge();
        
        log.info("更新知识库成功，ID: {}", dto.getId());
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        knowledgeMapper.deleteById(id);
        
        // 重新加载向量库
        botService.reloadKnowledge();
        
        log.info("删除知识库成功，ID: {}", id);
    }
    
    @Override
    public void reloadKnowledge() {
        botService.reloadKnowledge();
        log.info("重新加载知识库成功");
    }
    
    /**
     * 实体转VO
     */
    private CustomerServiceKnowledgeVO convertToVO(CustomerServiceKnowledge entity) {
        CustomerServiceKnowledgeVO vo = new CustomerServiceKnowledgeVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
