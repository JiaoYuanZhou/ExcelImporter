package com.citic.excelimporter.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;


/**
 * @author jiaoyuanzhou
 */
@Data
@ToString
@EqualsAndHashCode()
@NoArgsConstructor
public class R<T> {

    private int code;
    private boolean success;
    private String msg;
    private T data;
    private long timestamp;

    private R(CommonResultCode resultCode){
        this(resultCode , resultCode.getMsg() , null);
    }

    private R(CommonResultCode resultCode , String msg){
        this(resultCode , msg , null);
    }

    private R(CommonResultCode resultCode , T data){
        this(resultCode , resultCode.getMsg() , data);
    }

    public R(Integer code , String msg , T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
        timestamp = System.currentTimeMillis();
        this.success = CommonResultCode.SUCCESS.code == code;
    }

    public R(CommonResultCode resultCode , String msg , T data){
        this.code = resultCode.getCode();
        this.msg = msg;
        this.data = data;
        timestamp = System.currentTimeMillis();
        this.success = CommonResultCode.SUCCESS == resultCode;
    }

    /**
     * 返回成功
     *
     * @param <T>
     *            泛型标记
     * @return Result
     */
    public static <T> R<T> success() {
        return new R<>(CommonResultCode.SUCCESS);
    }

    /**
     * 成功-携带数据
     *
     * @param data
     *            数据
     * @param <T>
     *            泛型标记
     * @return Result
     */
    public static <T> R<T> success(@Nullable T data) {
        return new R<>(CommonResultCode.SUCCESS , data);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status
     *            状态
     * @param msg
     *            异常msg
     * @param <T>
     *            泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status , String msg) {
        return status ? R.success() : R.fail(msg);
    }

    /**
     * 根据状态返回成功或者失败
     *
     * @param status
     *            状态
     * @param rCode
     *            异常code码
     * @param <T>
     *            泛型标记
     * @return Result
     */
    public static <T> R<T> status(boolean status , CommonResultCode rCode) {
        return status ? R.success() : R.fail(String.valueOf(rCode));
    }

    /**
     * 返回失败信息，用于 web
     *
     * @param msg
     *            失败信息
     * @param <T>
     *            泛型标记
     * @return Result
     */
    public static <T> R<T> fail(String msg) {
        return new R<>(CommonResultCode.FAIL , msg);
    }


    public static <T> R<T> fail(Integer code , String msg , T t) {
        return new R<>(code , msg , t);
    }

    /**
     * 返回失败信息
     *
     * @param rCode
     *            异常枚举
     * @param <T>
     *            泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(CommonResultCode rCode) {
        return new R<>(rCode);
    }

    /**
     * 返回失败信息
     *
     * @param rCode
     *            异常枚举
     * @param msg
     *            失败信息
     * @param <T>
     *            泛型标记
     * @return {Result}
     */
    public static <T> R<T> fail(CommonResultCode rCode , String msg) {
        return new R<>(rCode , msg);
    }




}

