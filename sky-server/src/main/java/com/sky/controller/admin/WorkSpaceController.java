package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
@RequestMapping ("/admin/workspace")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;
    @RequestMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData(){
        log.info("获取今日营业数据");
        return Result.success(workSpaceService.getBusinessData());
    }
    @RequestMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders(){
        log.info("查询今日订单管理数据");
        return Result.success(workSpaceService.getOverviewOrders());
    }
    @RequestMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        log.info("查询当前菜品数据");
        return Result.success(workSpaceService.getOverviewDishes());
    }
    @RequestMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        log.info("查询当前套餐数据");
        return Result.success(workSpaceService.getOverviewSetmeals());
    }
}
