package com.citic.excelimporter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.citic.excelimporter.common.R;
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


    R<Object> getById(Long id);

    R<Object> removeById(Long id);


    void savePerson(Person person);

    IPage<Person> getPeopleWithPagination(long currentPage, long pageSize);

    R<Object> updatePerson(Person person);
}
