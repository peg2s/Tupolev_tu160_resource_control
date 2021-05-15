package io.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteFile {
    @SneakyThrows
    public static <T> void saveInfo(Class<T> clazz, List<?> file, String extension) {
        ObjectMapper objectMapper = new ObjectMapper();
        try(FileWriter fileWriter = new FileWriter(clazz.getName() + "." + extension, false)) {
            fileWriter.write(objectMapper.writeValueAsString(file));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
