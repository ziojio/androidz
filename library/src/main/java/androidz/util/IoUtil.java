package androidz.util;

import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import androidx.annotation.NonNull;


public final class IoUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

    public static String readString(@NonNull File file) throws IOException {
        return new String(readAllBytes(file));
    }

    public static byte[] readAllBytes(@NonNull File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bao.write(buffer, 0, len);
            }
            return bao.toByteArray();
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
