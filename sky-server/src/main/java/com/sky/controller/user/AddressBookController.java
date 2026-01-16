package com.sky.controller.user;

import com.sky.annotation.AutoFill;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    public Result<String> add(@RequestBody AddressBook addressBook){
        log.info("新增地址:{}",addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("查询地址");
        return Result.success(addressBookService.list());
    }
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        log.info("查询默认地址");
        return Result.success(addressBookService.getDefault());
    }
    @PutMapping
    public Result<String> updateById(@RequestBody AddressBook addressBook){
        log.info("根据id修改地址:{}",addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }
    @DeleteMapping
    public Result<String> deleteById(@RequestParam Long id){
        log.info("根据id删除地址:{}",id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    @GetMapping()
    public Result<AddressBook> getById(@RequestParam Long id){
        log.info("查询id为: {}的地址信息",id);
        return Result.success(addressBookService.getById(id));
    }
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址:{}",addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
