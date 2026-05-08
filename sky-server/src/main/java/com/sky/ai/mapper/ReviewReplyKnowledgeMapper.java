package com.sky.ai.mapper;

import com.sky.entity.ReviewReplyKnowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 差评回复知识库Mapper
 */
@Mapper
public interface ReviewReplyKnowledgeMapper {
    
    /**
     * 根据分类查询启用的知识库
     *
     * @param category 问题分类
     * @return 知识库列表
     */
    @Select("SELECT * FROM sky_take_out.review_reply_knowledge WHERE category = #{category} AND status = 1 ORDER BY effectiveness_score DESC")
    List<ReviewReplyKnowledge> listByCategory(String category);
    
    /**
     * 查询所有启用的知识库
     *
     * @return 知识库列表
     */
    @Select("SELECT * FROM sky_take_out.review_reply_knowledge WHERE status = 1 ORDER BY effectiveness_score DESC")
    List<ReviewReplyKnowledge> listAll();
    
    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 知识库
     */
    @Select("SELECT * FROM sky_take_out.review_reply_knowledge WHERE id = #{id}")
    ReviewReplyKnowledge getById(Long id);
    
    /**
     * 增加使用次数
     *
     * @param id 主键ID
     */
    void incrementUseCount(Long id);
}
