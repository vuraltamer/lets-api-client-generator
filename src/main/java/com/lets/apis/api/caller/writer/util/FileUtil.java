package com.lets.apis.api.caller.writer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void write(String directoryPath, String content, String fileName) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Dosya yazılamadı: " + e.getMessage());
        }
    }
}