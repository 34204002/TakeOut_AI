package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.service.impl.EmployeeServiceImpl;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@RestController("adminEmployeeController")
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    /**
     * 登录
     *
     * @param employeeLoginDTO the employee login dto
     * @return result
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return result
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO the employee dto
     * @return result
     */
    @PostMapping()
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工:{}",employeeDTO);
        employeeServiceImpl.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO the employee page query dto
     * @return result
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询:{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);


        return Result.success(pageResult);
    }

    /**
     * 修改员工状态
     *
     * @param status the status
     * @param id     the id
     * @return result
     */
    @PostMapping("/status/{status}")
    public  Result<String> startOrStop(@PathVariable Integer status,Long id){
        log.info("修改id: {}员工状态为: {}",id,status);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工
     *
     * @param id the id
     * @return the result
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("查询id为: {}的员工信息",id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工信息
     *
     * @param employeeDTO the employee dto
     * @return the result
     */
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息修改: {}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
