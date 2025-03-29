package androidz.util;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @see android.os.FileUtils
 */
public final class FileUtil {
    private static final int BUFFER_SIZE = 1024 * 8;

    public static String readText(@NonNull File file) throws IOException {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line = reader.readLine();
                if (line != null) {
                    builder.append(line);
                    String lineSeparator = System.lineSeparator();
                    while ((line = reader.readLine()) != null) {
                        builder.append(lineSeparator);
                        builder.append(line);
                    }
                }
            }
            return builder.toString();
        } else {
            return new String(readAllBytes(file), StandardCharsets.UTF_8);
        }
    }

    public static byte[] readAllBytes(@NonNull File file) throws IOException {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            return Files.readAllBytes(file.toPath());
        } else {
            try (FileInputStream fis = new FileInputStream(file);
                 ByteArrayOutputStream bao = new ByteArrayOutputStream(fis.available())) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = fis.read(buffer)) >= 0) {
                    bao.write(buffer, 0, len);
                }
                return bao.toByteArray();
            }
        }
    }

    public static void write(@NonNull File file, @NonNull String str) throws IOException {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                writer.write(str);
            }
        } else {
            CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
            try (OutputStream f = new FileOutputStream(file, false);
                 OutputStream out = Channels.newOutputStream(Channels.newChannel(f));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
                writer.write(str);
            }
        }
    }

    public static void write(@NonNull File file, @NonNull byte[] bytes) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.write(file.toPath(), bytes);
        } else {
            try (FileOutputStream f = new FileOutputStream(file, false);
                 OutputStream o = Channels.newOutputStream(Channels.newChannel(f));
                 BufferedOutputStream out = new BufferedOutputStream(o)) {
                int len = bytes.length;
                int rem = len;
                while (rem > 0) {
                    int n = Math.min(rem, BUFFER_SIZE);
                    out.write(bytes, (len - rem), n);
                    rem -= n;
                }
            }
        }
    }

    @Nullable
    public static File getFileByPath(@Nullable String filePath) {
        return filePath == null || filePath.isBlank() ? null : new File(filePath);
    }

    public static boolean rename(@NonNull File file, @NonNull String newName) {
        if (!file.exists()) return false;
        if (newName.isBlank()) return false;
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent(), newName);
        return !newFile.exists() && file.renameTo(newFile);
    }

    public static boolean delete(@NonNull String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) return false;
        return delete(file);
    }

    public static boolean delete(@NonNull File file) {
        if (file.isDirectory()) {
            return deleteDir(file);
        }
        return deleteFile(file);
    }

    public static boolean deleteFile(@NonNull File file) {
        return !file.exists() || (file.isFile() && file.delete());
    }

    public static boolean deleteDir(@NonNull File dir) {
        if (deleteContents(dir)) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 删除目录中的所有文件
     */
    public static boolean deleteContents(@NonNull File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    success = false;
                }
            }
        }
        return success;
    }

    public static boolean deleteContents(@NonNull File dir, @NonNull FileFilter filter) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (filter.accept(file)) {
                    if (file.isDirectory()) {
                        success &= deleteContents(file);
                    }
                    if (!file.delete()) {
                        success = false;
                    }
                }
            }
        }
        return success;
    }

    /**
     * @param minCount 至少保留多少个文件
     * @param minAgeMs 距离上次修改多久后才删除
     * @return 是否有文件被删除
     */
    public static boolean deleteOlderFiles(@NonNull File dir, int minCount, long minAgeMs) {
        if (minCount < 0 || minAgeMs < 0) {
            throw new IllegalArgumentException("Arguments must be positive or 0");
        }

        final File[] files = dir.listFiles();
        if (files == null) return false;

        // Sort with newest files first
        Arrays.sort(files, (lhs, rhs) -> Long.compare(rhs.lastModified(), lhs.lastModified()));

        // Keep at least minCount files
        boolean deleted = false;
        for (int i = minCount; i < files.length; i++) {
            final File file = files[i];
            // Keep files newer than minAgeMs
            final long age = System.currentTimeMillis() - file.lastModified();
            if (age > minAgeMs) {
                if (file.delete()) {
                    deleted = true;
                }
            }
        }
        return deleted;
    }


    @Nullable
    public static byte[] getFileMD5(@NonNull File file) {
        try {
            return digest(file, "MD5");
        } catch (IOException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * @param algorithm MD5
     */
    @NonNull
    public static byte[] digest(@NonNull File file, @NonNull String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (FileInputStream in = new FileInputStream(file)) {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            try (DigestInputStream digestStream = new DigestInputStream(in, digest)) {
                final byte[] buffer = new byte[1024 * 8];
                while (digestStream.read(buffer) != -1) {
                }
            }
            return digest.digest();
        }
    }

}
