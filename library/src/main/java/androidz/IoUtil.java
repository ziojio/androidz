package androidz;

import android.os.Build;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;


public class IoUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;


    public static String readString(@NonNull File file) throws IOException {
        return new String(readBytes(file));
    }

    public static byte[] readBytes(@NonNull File file) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Files.readAllBytes(file.toPath());
        } else {
            try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                while (fis.read(buffer, 0, DEFAULT_BUFFER_SIZE) >= 0) {
                    bao.write(buffer, 0, DEFAULT_BUFFER_SIZE);
                }
                return bao.toByteArray();
            }
        }
    }

    public static void write(@NonNull File file, @NonNull String str) throws IOException {
        write(file, str.getBytes());
    }

    public static void write(@NonNull File file, @NonNull byte[] bytes) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.write(file.toPath(), bytes);
        } else {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }
        }
    }

}
