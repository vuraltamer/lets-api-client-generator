package com.lets.apis.client.generator.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ApiBasePackageUtil {

    private static String projectBasePackage;

    public static String get() {
        if (projectBasePackage == null) {
            return projectBasePackage = getProjectBasePackage();
        }
        return projectBasePackage;
    }

    private static String getProjectBasePackage() {
        final List<Class<?>> allClasses = getAllClasses();
        return findProjectBasePackage(allClasses);
    }

    public static List<Class<?>> getAllClasses() {
        Path root = Paths.get("build/classes/java/main");
        if (!Files.exists(root)) {
            System.err.println("Directory not found: " + root.toAbsolutePath());
            return Collections.emptyList();
        }

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{root.toUri().toURL()},
                Thread.currentThread().getContextClassLoader());
             Stream<Path> paths = Files.walk(root)) {

            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".class"))
                    .map(p -> toClassName(root, p))
                    .map(name -> loadClassSafe(name, loader))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static String toClassName(Path root, Path classFile) {
        Path relative = root.relativize(classFile);
        String path = relative.toString()
                .replace(File.separatorChar, '.')
                .replaceAll("\\.class$", "");
        return path;
    }

    private static Class<?> loadClassSafe(String className, ClassLoader loader) {
        try {
            return Class.forName(className, false, loader);
        } catch (Exception exception) {
            return null;
        }
    }

    private static String findProjectBasePackage(List<Class<?>> classes) {
        if (classes == null || classes.isEmpty()) {
            return "";
        }
        List<String[]> packageParts = classes.stream()
                .map(c -> c.getPackageName().split("\\."))
                .collect(Collectors.toList());

        int minLength = packageParts.stream()
                .mapToInt(arr -> arr.length)
                .min()
                .orElse(0);

        List<String> commonSegments = IntStream.range(0, minLength)
                .takeWhile(i -> isRootedPath(i, packageParts))
                .mapToObj(i -> packageParts.get(0)[i])
                .collect(Collectors.toList());

        return String.join(".", commonSegments);
    }

    private static boolean isRootedPath(int i, List<String[]> packageParts) {
        return packageParts.stream()
                .map(parts -> parts[i])
                .distinct()
                .count() == 1;
    }
}