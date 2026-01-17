package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;


    /**
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list(AddressBook.builder().userId(BaseContext.getCurrentId()).build());
    }

    /**
     * @return
     */
    @Override
    public AddressBook getDefault() {
       return addressBookMapper.getDefault(AddressBook.builder().userId(BaseContext.getCurrentId()).build());
    }

    /**
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateById(addressBook);
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
       return addressBookMapper.getById(id);
    }

    /**
     * @param addressBook
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        addressBookMapper.updateAllToNonDefault(BaseContext.getCurrentId());
        addressBookMapper.updateById(addressBook);
    }
}
