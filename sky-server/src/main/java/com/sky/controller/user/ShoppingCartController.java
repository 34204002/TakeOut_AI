package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto){
        log.info("添加购物车:{}",dto);
        shoppingCartService.add(dto);
        return Result.success();
    }
    /**
     * 删除购物车的一个商品
     * @param dto
     * @return
     */
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO dto){
        log.info("删除购物车的一个商品:{}",dto);
        shoppingCartService.sub(dto);
        return Result.success();
    }
    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        log.info("查看购物车");
        return Result.success(shoppingCartService.list());
    }
    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }
}
