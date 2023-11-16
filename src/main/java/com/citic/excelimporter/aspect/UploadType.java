package com.citic.excelimporter.aspect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaoyuanzhou
 */

public enum UploadType {
    /**
     * 异步处理枚举
     */
    XLSX(1,"xlsx"),
    XLS(2,"xls"),
    TXT(3,"txt");

    private int code;
    private String desc;
    private static Map<Integer, UploadType> map = new HashMap<>();
    static {
        for (UploadType value : UploadType.values()) {
            map.put(value.code, value);
        }
    }

    UploadType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UploadType getByCode(Integer code) {
        return map.get(code);
    }
}
