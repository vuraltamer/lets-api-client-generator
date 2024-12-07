package com.lets.apis.client.generator.writer;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ControllerDetail;
import com.lets.apis.client.generator.model.ControllerModelDetail;
import com.lets.apis.client.generator.model.GradleDetail;
import com.lets.apis.client.generator.writer.util.FileUtil;

public class FileWriter {

    public static void write(ApiDetail apiDetail) {
        apiDetail.getControllerDetails().stream()
                .forEach(controllerDetail -> write(apiDetail.getDirectoryPath(), controllerDetail));

        apiDetail.getModelDetails().stream()
                .forEach(modelDetail -> write(apiDetail.getDirectoryPath(), modelDetail));

        write(apiDetail.getDirectoryPath(), apiDetail.getGradleDetail());
    }

    private static void write(String directoryPath, ControllerModelDetail detail) {
        String generatePath = directoryPath + "src/main/java/" + detail.getPackageName().replace(".", "/");
        String className = detail.getClassName() + ".java";
        FileUtil.write(generatePath, detail.getContent(), className);
    }

    private static void write(String directoryPath, ControllerDetail detail) {
        String generatePath = directoryPath + "src/main/java/" + detail.getPackageName().replace(".", "/");
        String className = detail.getClassName() + ".java";
        FileUtil.write(generatePath, detail.getContent(), className);
    }

    public static void write(String directoryPath, GradleDetail gradleDetail){
        GradleDetail.GradleModel buildGradle = gradleDetail.getBuildGradle();
        GradleDetail.GradleModel settingsGradle = gradleDetail.getSettingsGradle();
        FileUtil.write(directoryPath, buildGradle.getContent(), buildGradle.getName());
        FileUtil.write(directoryPath, settingsGradle.getContent(), settingsGradle.getName());
    }
}