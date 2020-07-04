package io.github.encryptorcode.implementation.storage.file;

import java.io.*;
import java.util.Optional;

public class FileUtils {
    public static <T> Optional<T> readFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return Optional.empty();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            //noinspection unchecked
            return Optional.of((T) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static <T> void writeToFile(String filePath, T object) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
