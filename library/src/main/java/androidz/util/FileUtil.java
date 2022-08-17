package androidz.util;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @see android.os.FileUtils
 */
public final class FileUtil {
    private static final String TAG = "FileUtil";

    /**
     * 根据文件路径获取文件
     */
    @Nullable
    public static File getFileByPath(String filePath) {
        return filePath == null || filePath.isBlank() ? null : new File(filePath);
    }

    public static boolean isFileExists(File file) {
        if (file == null) return false;
        return file.exists();
    }

    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    public static boolean isDirExists(File dir) {
        return dir != null && dir.exists() && dir.isDirectory();
    }

    public static boolean isDirExists(String dirPath) {
        return isDirExists(getFileByPath(dirPath));
    }

    public static boolean createDir(String dirPath) {
        return createDir(getFileByPath(dirPath));
    }

    public static boolean createDir(File file) {
        if (file == null) return false;
        return file.exists() ? file.isDirectory() : file.mkdirs();
    }

    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(File file, String newName) {
        if (file == null) return false;
        if (!file.exists()) return false;
        if (newName == null || newName.isBlank()) return false;
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent(), newName);
        return !newFile.exists() && file.renameTo(newFile);
    }

    public static boolean delete(String filePath) {
        return delete(getFileByPath(filePath));
    }

    /**
     * Delete the File or Directory
     */
    public static boolean delete(File file) {
        if (file == null) return false;
        if (file.isDirectory()) {
            return deleteDir(file);
        }
        return deleteFile(file);
    }

    public static boolean deleteFile(@NonNull File file) {
        return !file.exists() || (file.isFile() && file.delete());
    }

    /**
     * Delete directory.
     */
    public static boolean deleteDir(@NonNull File dir) {
        if (deleteContents(dir)) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * Delete only files in directory.
     */
    public static boolean deleteFiles(@NonNull File dir) {
        return deleteContents(dir, File::isFile);
    }

    /**
     * Delete all files in directory.
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
     * Delete all files that satisfy the filter in directory.
     */
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
     * Delete older files in a directory until only those matching the given constraints remain.
     *
     * @param minCount Always keep at least this many files.
     * @param minAgeMs Always keep files younger than this age, in milliseconds.
     * @return if any files were deleted.
     */
    public static boolean deleteOlderFiles(@NonNull File dir, @IntRange(from = 0) int minCount, long minAgeMs) {
        if (minCount < 0 || minAgeMs < 0) {
            throw new IllegalArgumentException("Constraints must be positive or 0");
        }

        final File[] files = dir.listFiles();
        if (files == null) return false;

        // Sort with newest files first
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.compare(rhs.lastModified(), lhs.lastModified());
            }
        });

        // Keep at least minCount files
        boolean deleted = false;
        for (int i = minCount; i < files.length; i++) {
            final File file = files[i];

            // Keep files newer than minAgeMs
            final long age = System.currentTimeMillis() - file.lastModified();
            if (age > minAgeMs) {
                if (file.delete()) {
                    Log.d(TAG, "Deleted old file " + file);
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
     * Compute the digest of the given file using the requested algorithm.
     *
     * @param algorithm Any valid algorithm accepted by {@link MessageDigest#getInstance(String)}.
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
