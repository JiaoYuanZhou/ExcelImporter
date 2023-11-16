package com.citic.excelimporter.aspect;

import com.citic.excelimporter.decryption.DecryptionContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author jiaoyuanzhou
 */
@Aspect
@Component
public class DecryptionAspect {


    @Around("@annotation(com.citic.excelimporter.decryption.DecryptData)")
    public Object decryptFile(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取方法参数
            Object[] args = joinPoint.getArgs();

            // 判断方法参数中是否包含 MultipartFile 类型的参数
            MultipartFile encryptedFile = findEncryptedFile(args);
            if (encryptedFile == null) {
                throw new IllegalArgumentException("方法里没有 MultipartFile 参数");
            }

            // 获取加密后的Excel文件的字节数组
            byte[] encryptedData = encryptedFile.getBytes();

            // 获取IV和加密数据
            byte[] iv = new byte[16];
            byte[] encryptedBuffer = new byte[encryptedData.length - 16];
            System.arraycopy(encryptedData, 0, iv, 0, 16);
            System.arraycopy(encryptedData, 16, encryptedBuffer, 0, encryptedBuffer.length);

            // 在这里使用相同的密钥解密文件
            String secretKey = "super_secret_key";
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(128, iv));
            byte[] decryptedData = cipher.doFinal(encryptedBuffer);

            // 将解密后的数据存储在 ThreadLocal 中
            DecryptionContextHolder.setDecryptedData(decryptedData);

            // 继续执行原方法
            Object result = joinPoint.proceed();

            // 清除解密后的数据
            DecryptionContextHolder.clear();

            return result;
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            // 处理异常，例如记录日志或抛出自定义异常
            throw e;
        }
    }

    private MultipartFile findEncryptedFile(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                return (MultipartFile) arg;
            }
        }
        return null;
    }

}

