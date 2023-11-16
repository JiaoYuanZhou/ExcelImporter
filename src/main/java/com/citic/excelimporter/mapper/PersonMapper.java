package com.citic.excelimporter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citic.excelimporter.pojo.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jiaoyuanzhou
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
    /**
     * 批量导入人员数据
     * @param list
     */
    void insertBatch(@Param("list") List<Person> list);


}
