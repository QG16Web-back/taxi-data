package com.qg.taxi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Create by ming on 18-8-3 下午12:07
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public class FileUtil {

    /**
     * 获取文件中的内容
     *
     * @param file 文件
     * @return 文件内容
     */
    public static String getContent(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
