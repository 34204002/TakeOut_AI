package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkSpaceService {
    BusinessDataVO getBusinessData();

    OrderOverViewVO getOverviewOrders();
    
    DishOverViewVO getOverviewDishes();
    
    SetmealOverViewVO getOverviewSetmeals();
}
