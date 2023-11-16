package com.citic.excelimporter.common;


import lombok.Getter;


/**
 * @author jiaoyuanzhou
 */

@Getter
public enum CommonResultCode {

    /**
     * 成功
     */
    SUCCESS(200,"请求成功。"),
    FAIL(490,"请求异常。"),
    ;

    public int code;
    public String msg;

    CommonResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResultCode getByCode(int code){

        CommonResultCode[] values = values();

        for (CommonResultCode value : values) {

            if (code == value.code){
                return value;
            }
        }

        return FAIL;
    }

}

