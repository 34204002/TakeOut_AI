package com.sky.ai.controller.admin;

import com.sky.dto.CustomerServiceKnowledgeDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.ai.service.CustomerServiceKnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端客服知识库管理控制器
 */
@RestController("adminCustomerServiceKnowledgeController")
@RequestMapping("/admin/customer-service/knowledge")
@Slf4j
@Tag(name = "客服知识库管理")
public class CustomerServiceKnowledgeController {
    
    @Autowired
    private CustomerServiceKnowledgeService knowledgeService;
    
    /**
     * 分页查询知识库
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param category 分类（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询知识库")
    public Result<PageResult> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category
    ) {
        log.info("分页查询知识库，page: {}, pageSize: {}, category: {}", page, pageSize, category);
        
        PageResult result = knowledgeService.pageQuery(page, pageSize, category);
        return Result.success(result);
    }
    
    /**
     * 新增知识库
     *
     * @param dto 知识库信息
     * @return 新增的ID
     */
    @PostMapping
    @Operation(summary = "新增知识库")
    public Result<Long> add(@RequestBody CustomerServiceKnowledgeDTO dto) {
        log.info("新增知识库，category: {}, question: {}", dto.getCategory(), dto.getQuestion());
        
        Long id = knowledgeService.add(dto);
        return Result.success(id);
    }
    
    /**
     * 更新知识库
     *
     * @param dto 知识库信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "更新知识库")
    public Result update(@RequestBody CustomerServiceKnowledgeDTO dto) {
        log.info("更新知识库，ID: {}", dto.getId());
        
        knowledgeService.update(dto);
        return Result.success();
    }
    
    /**
     * 删除知识库
     *
     * @param id 知识库ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除知识库")
    public Result delete(@PathVariable Long id) {
        log.info("删除知识库，ID: {}", id);
        
        knowledgeService.deleteById(id);
        return Result.success();
    }
    
    /**
     * 重新加载知识库（同步到向量库）
     *
     * @return 操作结果
     */
    @PostMapping("/reload")
    @Operation(summary = "重新加载知识库")
    public Result reload() {
        log.info("重新加载知识库");
        
        knowledgeService.reloadKnowledge();
        return Result.success();
    }
}
