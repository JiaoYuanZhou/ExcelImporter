package com.citic.excelimporter.pojo;

import lombok.Data;

/**
 *  用来显示表格中哪一行哪一列出错
 * @author jiaoyuanzhou
 */
@Data
public class ExcelTable {
    private Integer row;
    private Integer column;
}
