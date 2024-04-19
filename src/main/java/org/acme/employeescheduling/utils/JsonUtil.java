package org.acme.employeescheduling.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    private JsonUtil() {
    }

    public static <T> T deserialize(Reader reader, Class<T> classType) {
        return gson.fromJson(reader, classType);
    }
    public static <T> T deserialize(String reader, Class<T> classType) {
        return gson.fromJson(reader, classType);
    }

    public static <T> String serialize(T object) {
        return gson.toJson(object);
    }
}

