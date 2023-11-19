package com.citic.excelimporter.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;

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

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Digits(integer = 3, fraction = 0, message = "年龄必须为整数")
    private Integer age;

    @NotBlank(message = "最高学历不能为空")
    private String highestEducation;

    @NotBlank(message = "最高学历院校不能为空")
    private String university;

    @NotBlank(message = "最高学历专业不能为空")
    private String major;

    @NotBlank(message = "电话不能为空")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    private String residenceAddress;
    private String zipCode;

}

