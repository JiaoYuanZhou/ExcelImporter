package com.citic.excelimporter.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaoyuanzhou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("person")
public class Person {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String highestEducation;
    private String university;
    private String major;
    private String phone;
    private String email;
    private String residenceAddress;
    private String zipCode;

}

