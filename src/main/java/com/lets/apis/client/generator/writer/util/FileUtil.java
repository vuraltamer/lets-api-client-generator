package com.lets.apis.client.generator.writer.util;

import com.lets.apis.client.generator.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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

    public static String read(String directoryPath, String fileName) {
        try (InputStream inputStream = Template.class.getResourceAsStream(directoryPath + fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("FileUtil::read::exception", e);
        }
    }
}