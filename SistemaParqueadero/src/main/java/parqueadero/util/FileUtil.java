package parqueadero.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class FileUtil {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    public static <T> T read(String path, Class<T> classOfT) {
        try {
            String json = Files.readString(Paths.get(path));
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T read(String path, java.lang.reflect.Type typeOfT) {
        try {
            String json = Files.readString(Paths.get(path));
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> void write(String path, T object) {
        try {
            Files.createDirectories(Paths.get(path).getParent());
            Files.writeString(Paths.get(path), gson.toJson(object), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}