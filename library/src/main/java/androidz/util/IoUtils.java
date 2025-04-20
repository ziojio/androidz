package androidz.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class IoUtils {

    @NonNull
    public static byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        }
    }

    /**
     * @return null if file is empty
     */
    @Nullable
    public static String readText(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(line);
            while ((line = reader.readLine()) != null) {
                builder.append(System.lineSeparator()).append(line);
            }
            return builder.toString();
        }
    }

    /**
     * @return null if file is empty
     */
    @Nullable
    public static String readLine(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }

    @NonNull
    public static List<String> readAllLines(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return result;
        }
    }

    public static void write(File file, @NonNull String str) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(str);
        }
    }

    public static void write(File file, @NonNull byte[] bytes) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            int len = bytes.length;
            int rem = len;
            while (rem > 0) {
                int n = Math.min(rem, 8192);
                out.write(bytes, (len - rem), n);
                rem -= n;
            }
        }
    }
}
