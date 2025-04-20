package com.example.demo.util;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @see android.os.FileUtils
 */
@SuppressWarnings("unused")
public final class FileUtil {

    public static boolean rename(@NonNull File file, @NonNull String newName) {
        if (!file.exists()) return false;
        if (newName.isBlank()) return false;
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent(), newName);
        return !newFile.exists() && file.renameTo(newFile);
    }

    public static boolean delete(@NonNull String filePath) {
        return delete(new File(filePath));
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

    /**
     * 删除目录中的旧文件
     *
     * @param minCount Always keep at least this many files.
     * @param minAgeMs Always keep files younger than this age, in milliseconds.
     * @return if any files were deleted.
     */
    public static boolean deleteOlderFiles(File dir, int minCount, long minAgeMs) {
        if (minCount < 0 || minAgeMs < 0) {
            throw new IllegalArgumentException("Constraints must be positive or 0");
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
            if (minAgeMs == 0 || System.currentTimeMillis() - file.lastModified() > minAgeMs) {
                if (file.delete()) {
                    deleted = true;
                }
            }
        }
        return deleted;
    }

    /**
     * @param algorithm MD5
     * @noinspection StatementWithEmptyBody
     */
    @NonNull
    public static byte[] digest(@NonNull File file, @NonNull String algorithm) throws IOException, NoSuchAlgorithmException {
        try (FileInputStream in = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            try (DigestInputStream digestStream = new DigestInputStream(in, digest)) {
                byte[] buffer = new byte[8192];
                while (digestStream.read(buffer) != -1) {
                }
            }
            return digest.digest();
        }
    }

}
