package com.citic.excelimporter.decryption;

/**
 * 使用ThreadLocal用来存放解密后的数据
 * @author jiaoyuanzhou
 */
public class DecryptionContextHolder {

    private static final ThreadLocal<byte[]> decryptedDataHolder = new ThreadLocal<>();

    public static byte[] getDecryptedData() {
        return decryptedDataHolder.get();
    }

    public static void setDecryptedData(byte[] decryptedData) {
        decryptedDataHolder.set(decryptedData);
    }

    public static void clear() {
        decryptedDataHolder.remove();
    }
}

