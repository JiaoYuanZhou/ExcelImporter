package com.citic.excelimporter.aspect;


import com.citic.excelimporter.common.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 在切面中使用线程池异步处理数据量较大的数据，避免接口超时
 * @author jiaoyuanzhou
 */
@Component
@Aspect
@Slf4j
public class UploadAspect {
    public static ExecutorService uploadExecuteService = new ThreadPoolExecutor(10, 20, 300L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), new ThreadPoolExecutor.AbortPolicy());

    @Pointcut("@annotation(com.citic.excelimporter.aspect.Upload)")
    public void uploadPoint() {
    }

    @Around(value = "uploadPoint()")
    public R<Object> uploadControl(ProceedingJoinPoint pjp) {
        // 获取方法上的注解，进而获取uploadType
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Upload annotation = signature.getMethod().getAnnotation(Upload.class);
        UploadType type = annotation == null ? UploadType.XLSX : annotation.type();
        // 获取batchNo
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        // 初始化一条上传的日志，记录开始时间
        log.info("文件上传类型" + type);

        // 线程池启动异步线程，开始执行上传的逻辑
        uploadExecuteService.submit(() -> {
            try {
                Object proceed = pjp.proceed();
                log.info("成功写入数据库" + batchNo);
            } catch (Throwable e) {
                log.error("导入失败：", e);
                // 失败，抛了异常，需要记录
                log.info(e.toString() + batchNo);
            }
        });
        return R.success();
    }
}
