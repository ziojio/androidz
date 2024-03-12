package androidz.util;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FileUtil {

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /**
     * 根据文件路径获取文件
     */
    @Nullable
    public static File getFileByPath(String filePath) {
        return isBlank(filePath) ? null : new File(filePath);
    }

    public static boolean isFileExists(File file) {
        if (file == null) return false;
        if (file.exists()) {
            return true;
        }
        return isFileExists(file.getAbsolutePath());
    }

    public static boolean isFileExists(String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) return false;
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(filePath);
    }

    private static boolean isFileExistsApi29(String filePath) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = Androidz.getApp().getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) return false;
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static String getFileName(String filePath) {
        if (isBlank(filePath)) return "";
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    public static String getDirName(String filePath) {
        if (isBlank(filePath)) return "";
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
    }

    public static String getFileExtension(File file) {
        if (file == null) return "";
        return getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if (isBlank(filePath)) return "";
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    public static boolean createFile(String filepath) {
        return createFile(getFileByPath(filepath));
    }

    public static boolean createFile(File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        // file exists and unsuccessfully delete then return false
        if (file.exists() && !file.delete()) return false;
        if (!createDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDirExists(String dirPath) {
        return isDirExists(getFileByPath(dirPath));
    }

    public static boolean isDirExists(File dir) {
        return dir != null && dir.exists() && dir.isDirectory();
    }

    public static boolean createDir(String dirPath) {
        return createDir(getFileByPath(dirPath));
    }

    public static boolean createDir(File file) {
        if (file == null) return false;
        return file.exists() ? file.isDirectory() : file.mkdirs();
    }

    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    /**
     * Rename the file.
     *
     * @param file    The file.
     * @param newName The new name of file.
     */
    public static boolean rename(File file, String newName) {
        // file is null then return false
        if (file == null) return false;
        // file doesn't exist then return false
        if (!file.exists()) return false;
        // the new name is space then return false
        if (isBlank(newName)) return false;
        // the new name equals old name then return true
        if (newName.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + newName);
        // the new name of file exists then return false
        return !newFile.exists() && file.renameTo(newFile);
    }

    public static boolean delete(String filePath) {
        return delete(getFileByPath(filePath));
    }

    /**
     * Delete the file.
     */
    public static boolean delete(File file) {
        if (file == null) return false;
        if (file.isDirectory()) {
            return deleteDir(file);
        }
        return deleteFile(file);
    }

    private static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    private static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static boolean deleteAllInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, pathname -> true);
    }

    public static boolean deleteFilesInDir(final File dir) {
        return deleteFilesInDirWithFilter(dir, File::isFile);
    }

    /**
     * Delete all files that satisfy the filter in directory.
     */
    public static boolean deleteFilesInDirWithFilter(File dir, FileFilter filter) {
        if (dir == null || filter == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (filter.accept(file)) {
                    if (file.isFile()) {
                        if (!file.delete()) return false;
                    } else if (file.isDirectory()) {
                        if (!deleteDir(file)) return false;
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    public static byte[] getFileMD5(@NonNull String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) {
            return null;
        }
        return getFileMD5(file);
    }

    @Nullable
    public static byte[] getFileMD5(@NonNull File file) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md)) {
            byte[] buffer = new byte[10240];
            while (true) {
                if (!(dis.read(buffer) > 0)) {
                    break;
                }
            }
            return dis.getMessageDigest().digest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
