package androidz.util;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOUtil {

    public static String readString(@NonNull String filePath) {
        File file = FileUtil.getFileByPath(filePath);
        if (file != null) {
            try {
                return readString(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String readString(@NonNull File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String str = reader.readLine();
            if (str != null) {
                builder.append(str);
            }
            while ((str = reader.readLine()) != null) {
                builder.append('\n').append(str);
            }
        } finally {
            closeQuiet(reader);
        }
        return builder.toString();
    }

    public static boolean writeFile(@NonNull File file, @NonNull String content) {
        if (!FileUtil.createFile(file))
            return false;

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuiet(writer);
        }
        return false;
    }

    public static void closeQuiet(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException ignored) {
            }
        }
    }

}
