package androidz.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import androidx.annotation.NonNull;


public class IoUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;


    public static String readString(@NonNull File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            while (fis.read(buffer, 0, DEFAULT_BUFFER_SIZE) >= 0) {
                bao.write(buffer, 0, DEFAULT_BUFFER_SIZE);
            }
            return bao.toString();
        }
    }

    public static byte[] readBytes(@NonNull File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            while (fis.read(buffer, 0, DEFAULT_BUFFER_SIZE) >= 0) {
                bao.write(buffer, 0, DEFAULT_BUFFER_SIZE);
            }
            return bao.toByteArray();
        }
    }

    public static void writeString(@NonNull File file, @NonNull String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    public static void writeBytes(@NonNull File file, @NonNull byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
    }

}
