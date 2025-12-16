package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * @param dto
     */
    @Override
    public void add(ShoppingCartDTO dto) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(dto, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 先查询购物车中是否已存在相同商品
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            // 如果已存在，则增加数量
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 如果不存在，则添加新的购物车记录
            Dish dish = null;
            Setmeal setmeal = null;
            List<DishFlavor> dishFlavors = new ArrayList<>();
            
            if (dto.getDishId() != null) {
                dish = dishMapper.getById(dto.getDishId());
                dishFlavors = dishFlavorMapper.getByDishId(dto.getDishId());
            }
            if (dto.getSetmealId() != null) {
                setmeal = setmealMapper.getById(dto.getSetmealId());
            }
            
            if (dish != null) {
                BeanUtils.copyProperties(dish, shoppingCart);
                shoppingCart.setAmount(dish.getPrice()); // 手动设置价格
            }
            if (dishFlavors != null && !dishFlavors.isEmpty()) {
                StringBuilder dishFlavorNames = new StringBuilder();
                for (DishFlavor dishFlavor : dishFlavors) {
                    dishFlavorNames.append(dishFlavor.getName()).append(",");
                }
                // 删除最后一个多余的逗号
                if (!dishFlavorNames.isEmpty()) {
                    dishFlavorNames.deleteCharAt(dishFlavorNames.length() - 1);
                }
                shoppingCart.setDishFlavor(dishFlavorNames.toString());
            }
            if (setmeal != null) {
                BeanUtils.copyProperties(setmeal, shoppingCart);
                shoppingCart.setAmount(setmeal.getPrice()); // 手动设置价格
            }
            
            shoppingCart.setNumber(1);
            shoppingCartMapper.add(shoppingCart);
        }
    }

    /**
     * @param dto
     */
    @Override
    public void sub(ShoppingCartDTO dto) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(dto, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        
        // 查询要操作的购物车商品
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            ShoppingCart cart = shoppingCartList.get(0);
            // 数量大于1时减少数量，等于1时直接删除
            if (cart.getNumber() > 1) {
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(cart);
            } else {
                shoppingCartMapper.sub(cart); // 修改这里，传入完整cart对象以确保准确删除
            }
        }
    }

    /**
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
    }

    /**
     *
     */
    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
