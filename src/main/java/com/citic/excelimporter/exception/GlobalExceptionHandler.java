package com.citic.excelimporter.exception;

import com.citic.excelimporter.common.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * 全局处理异常
 * @author jiaoyuanzhou
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // 处理全局异常，并返回适当的 HTTP 状态码和错误信息
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleGlobalValidException(MethodArgumentNotValidException ex, WebRequest request) {
        // 处理全局异常，并返回适当的 HTTP 状态码和错误信息
        List<FieldError> fieldErrors = ex.getFieldErrors();
        StringBuffer stringBuffer = new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            stringBuffer.append(fieldError.getDefaultMessage()).append(" ");
        }
        return new ResponseEntity<>(R.fail(String.valueOf(stringBuffer)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

