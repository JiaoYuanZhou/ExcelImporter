package com.citic.excelimporter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.citic.excelimporter.pojo.Person;

import java.util.List;

/**
 * @author jiaoyuanzhou
 */
public interface PersonService  {

    /**
     * 批量导入人员数据
     * @param people
     */
    public void importData(List<Person> people);


    Person getById(Long id);

    void removeById(Long id);


    void saveOrUpdatePerson(Person person);

    IPage<Person> getPeopleWithPagination(long currentPage, long pageSize);
}
