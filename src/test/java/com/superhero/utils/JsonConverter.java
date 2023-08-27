package com.superhero.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.superhero.model.SuperHero;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class JsonConverter {

    public static final String fileAbsolutePath = "src/test/java/com/superhero/resources/json/";

    private JsonConverter(){}


    public static <T> List<T> getListObjectsFromJsonFile(Class<T> clazz, String fileName) throws IOException {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();
        Type objectType = TypeToken.getParameterized(List.class, clazz).getType();
        String jsonContent = loadJsonFromFile(fileName);
        return gson.fromJson(jsonContent, objectType);
    }

    public static String loadJsonFromFile(String fileName) throws IOException {
        String fileLocation = fileAbsolutePath + fileName;
        byte[] fileBytes = Files.readAllBytes(Paths.get(fileLocation));
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

}