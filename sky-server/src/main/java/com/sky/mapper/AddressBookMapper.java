package com.sky.mapper;

import com.sky.annotation.AutoFill;

import com.sky.entity.AddressBook;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    @AutoFill(value = OperationType.INSERT)
    void insert(AddressBook addressBook);

    List<AddressBook> list(AddressBook addressBook);

    AddressBook getDefault(AddressBook addressBook);

    @AutoFill(value = OperationType.UPDATE)
    void updateById(AddressBook addressBook);

    void deleteById(Long id);

    AddressBook getById(Long id);
    @AutoFill(value = OperationType.UPDATE)
    void updateAllToNonDefault(Long userId);
}
