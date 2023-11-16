package com.citic.excelimporter.utils;

import java.io.*;

/**
 * @author jiaoyuanzhou
 */
public class FileUtil {

    public static void mergeFiles(File outFile, File... files) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(outFile, true)) {
            for (File file : files) {
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }
}
