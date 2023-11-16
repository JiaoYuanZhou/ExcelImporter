package com.citic.excelimporter.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.citic.excelimporter.mapper.PersonMapper;
import com.citic.excelimporter.pojo.Person;
import com.citic.excelimporter.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jiaoyuanzhou
 */

@Service
public class PersonServiceImpl implements PersonService{

    @Resource
    private PersonMapper personMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(List<Person> people) {
        // 批量插入数据
        personMapper.insertBatch(people);
    }



    @Override
    public Person getById(Long id) {
        return personMapper.selectById(id);
    }

    @Override
    public void removeById(Long id) {
        personMapper.deleteById(id);
    }

    @Override
    public void saveOrUpdatePerson(Person person) {
        if (person.getId() != null && personMapper.selectById(person.getId()) != null) {
            personMapper.updateById(person);
        }else {
            personMapper.insert(person);

        }
    }

    @Override
    public IPage<Person> getPeopleWithPagination(long currentPage, long pageSize) {
        Page<Person> page = new Page<>(currentPage, pageSize);
        return personMapper.selectPage(page, null);
    }


}

