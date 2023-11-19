package com.citic.excelimporter.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.citic.excelimporter.common.R;
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
    public R<Object> getById(Long id) {
        Person person = personMapper.selectById(id);
        if (person == null) {
            return R.fail("查找失败，ID不存在");
        }
        return R.success(person);
    }

    @Override
    public R<Object> removeById(Long id) {
        Person person = personMapper.selectById(id);
        if (person == null) {
            return R.fail("删除失败，传入的ID不存在");
        }
        personMapper.deleteById(id);
        return R.success();
    }

    @Override
    public void savePerson(Person person) {
        personMapper.insert(person);
    }

    @Override
    public IPage<Person> getPeopleWithPagination(long currentPage, long pageSize) {
        Page<Person> page = new Page<>(currentPage, pageSize);
        return personMapper.selectPage(page, null);
    }

    @Override
    public R<Object> updatePerson(Person person) {
        if (person.getId() == null) {
            return R.fail("ID不能为空");
        }
        Person person1 = personMapper.selectById(person.getId());
        if (person1 == null) {
            return R.fail("ID为" + person.getId() + "的用户不存在");
        }
        personMapper.updateById(person);
        return R.success();
    }


}

