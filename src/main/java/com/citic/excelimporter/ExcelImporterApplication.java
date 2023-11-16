package com.citic.excelimporter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.citic.excelimporter.mapper")
@SpringBootApplication
public class ExcelImporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelImporterApplication.class, args);
    }

}
