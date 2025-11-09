package com.sky.mapper;

import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface Employee mapper.
 */
@Mapper
public interface EmployeeMapper {


    @Select("select * from sky_take_out.employee where username = #{username}")
    Employee getByUsername(String username);


    void addEmployee(Employee employee);


    List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    void update(Employee employee);


    @Select("select * from sky_take_out.employee where id = #{id}")
    Employee getById(Long id);
}
