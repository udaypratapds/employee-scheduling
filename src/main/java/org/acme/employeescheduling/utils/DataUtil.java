package org.acme.employeescheduling.utils;

import org.eclipse.microprofile.context.ThreadContext;

import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DataUtil {

    public static String getDataFromFile(String filePath) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                return scanner.useDelimiter("\\A").next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        throw new Exception("Input stream in empty or failed while reading");
    }
}
