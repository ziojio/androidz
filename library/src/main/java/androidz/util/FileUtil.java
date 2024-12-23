package androidz.util;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @see android.os.FileUtils
 */
public final class FileUtil {
    private static final int KB = 1024;
    private static final int BUFFER_SIZE = 1024 * 8;

    public static String readString(@NonNull File file) throws IOException {
        return toByteArrayOutputStream(file).toString();
    }

    public static String readString(@NonNull File file, @NonNull Charset charset) throws IOException {
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            return toByteArrayOutputStream(file).toString(charset);
        } else {
            //noinspection CharsetObjectCanBeUsed
            return toByteArrayOutputStream(file).toString(charset.name());
        }
    }

    public static byte[] readAllBytes(@NonNull File file) throws IOException {
        return toByteArrayOutputStream(file).toByteArray();
    }

    private static ByteArrayOutputStream toByteArrayOutputStream(@NonNull File file) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        int available = fin.available();
        ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(KB, available));
        try (BufferedInputStream in = new BufferedInputStream(fin)) {
            byte[] buffer = new byte[Math.max(available / 2, BUFFER_SIZE)];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        return out;
    }

    public static void write(@NonNull File file, @NonNull String str) throws IOException {
        write(file, str.getBytes());
    }

    public static void write(@NonNull File file, @NonNull String str, @NonNull Charset charset) throws IOException {
        write(file, str.getBytes(charset));
    }

    public static void write(@NonNull File file, @NonNull byte[] bytes) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(file);
             BufferedOutputStream out = new BufferedOutputStream(fout)) {
            int len = bytes.length;
            int rem = len;
            while (rem > 0) {
                int n = Math.min(rem, BUFFER_SIZE);
                out.write(bytes, (len - rem), n);
                rem -= n;
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
