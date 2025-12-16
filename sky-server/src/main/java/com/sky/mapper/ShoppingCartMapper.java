package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    @AutoFill(value = OperationType.INSERT)
    void add(ShoppingCart shoppingCart);

    void sub(ShoppingCart shoppingCart);
    
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    
    void updateNumberById(ShoppingCart shoppingCart);

    void deleteByUserId(Long currentId);
}
