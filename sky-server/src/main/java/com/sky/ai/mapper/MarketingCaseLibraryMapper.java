package com.sky.ai.mapper;

import com.sky.entity.MarketingCaseLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 营销案例库Mapper
 */
@Mapper
public interface MarketingCaseLibraryMapper {
    
    /**
     * 根据条件查询营销案例
     *
     * @param dishCategory 菜品分类
     * @param activityType 活动类型
     * @param channel 投放渠道
     * @return 案例列表
     */
    @Select("SELECT * FROM sky_take_out.marketing_case_library WHERE dish_category = #{dishCategory} AND activity_type = #{activityType} AND channel = #{channel} AND status = 1 ORDER BY performance_score DESC LIMIT 5")
    List<MarketingCaseLibrary> listByCondition(String dishCategory, String activityType, String channel);
    
    /**
     * 根据菜品分类查询
     *
     * @param dishCategory 菜品分类
     * @return 案例列表
     */
    @Select("SELECT * FROM sky_take_out.marketing_case_library WHERE dish_category = #{dishCategory} AND status = 1 ORDER BY performance_score DESC")
    List<MarketingCaseLibrary> listByDishCategory(String dishCategory);
    
    /**
     * 查询所有启用的案例
     *
     * @return 案例列表
     */
    @Select("SELECT * FROM sky_take_out.marketing_case_library WHERE status = 1 ORDER BY create_time DESC")
    List<MarketingCaseLibrary> listAll();
    
    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 营销案例
     */
    @Select("SELECT * FROM sky_take_out.marketing_case_library WHERE id = #{id}")
    MarketingCaseLibrary getById(Long id);
    
    /**
     * 插入营销案例
     *
     * @param caseLibrary 营销案例
     */
    void insert(MarketingCaseLibrary caseLibrary);
    
    /**
     * 增加使用次数
     *
     * @param id 主键ID
     */
    void incrementUsageCount(Long id);
    
    /**
     * 更新营销案例
     *
     * @param caseLibrary 营销案例
     */
    void update(MarketingCaseLibrary caseLibrary);
}
