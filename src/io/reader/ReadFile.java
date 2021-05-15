package io.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    public static <T> List<T> readInfo(Class<T> clazz, String fileName, String extension) {
        ObjectMapper objectMapper = new ObjectMapper();
        try(FileReader fileReader = new FileReader(fileName + "." + extension)) {
            CollectionType listFromSave = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(fileReader, listFromSave);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
