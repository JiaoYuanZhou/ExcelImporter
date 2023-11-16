package com.citic.excelimporter.utils;

import com.citic.excelimporter.exception.DataValidationException;
import com.citic.excelimporter.pojo.Person;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 处理Excel表格，将其转换为pojo对象
 * @author jiaoyuanzhou
 */
public class ExcelUtils {
    public static List<Person> readExcel(InputStream decryptedStream) throws IOException, DataValidationException {
        List<Person> people = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(decryptedStream)) {
            // 假设数据在第一个sheet中
            Sheet sheet = workbook.getSheetAt(0);

            int totalRows = sheet.getPhysicalNumberOfRows();


            Iterator<Row> rowIterator = sheet.iterator();

            // 跳过表头
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Person person = createPersonFromRow(row, totalRows);
                people.add(person);
            }
        }
        return people;
    }

    private static Person createPersonFromRow(Row row, Integer totalRows) throws DataValidationException {
        Person person = new Person();
        DecimalFormat decimalFormat = new DecimalFormat("0");


        int missingColumn = findMissingColumn(row);
        if (missingColumn != -1) {
            throw new DataValidationException("数据不能为空，在行： " + (row.getRowNum() + 1) + "列：" + (missingColumn+1));
        }


        if (row.getCell(2).getCellType() != CellType.NUMERIC) {
            throw new DataValidationException("年龄必须为数字类型，在行： " + (row.getRowNum() + 1) + ", 列： 3" );
        }

        if (row.getRowNum() >= 50) {
            throw new DataValidationException("超过行数导入限制。最大允许行数为50行。该文件行数为" + totalRows);
        }


        person.setId(Double.valueOf(row.getCell(0).getNumericCellValue()).longValue() );
        person.setName(row.getCell(1).getStringCellValue());
        person.setAge((int) row.getCell(2).getNumericCellValue());
        person.setHighestEducation(row.getCell(3).getStringCellValue());
        person.setUniversity(row.getCell(4).getStringCellValue());
        person.setMajor(row.getCell(5).getStringCellValue());

        person.setPhone(decimalFormat.format(row.getCell(6).getNumericCellValue()));
        person.setEmail(row.getCell(7).getStringCellValue());
        person.setResidenceAddress(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : null);
        person.setZipCode(String.valueOf(row.getCell(9) != null ? row.getCell(9).getNumericCellValue() : null));
        person.setZipCode(decimalFormat.format(row.getCell(9) != null ? row.getCell(9).getNumericCellValue() : null));

        return person;
    }

    private static int findMissingColumn(Row row) {
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            if (cell == null || cell.getCellType() == CellType.BLANK || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                return columnIndex;
            }
        }
        return -1;
    }


}

