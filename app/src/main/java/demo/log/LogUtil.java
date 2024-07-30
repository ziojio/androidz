package demo.log;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import demo.UIApp;
import demo.database.room.entity.TrackLog;
import demo.util.AsyncTask;

public class LogUtil {

    public static File getLogDir(@NonNull Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            file = context.getCacheDir();
        }
        return new File(file, "log");
    }

    public static String getLogFileName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "timber_" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(date) + ".log";
    }

    public static void saveLog(@NonNull TrackLog trackLog) {
        AsyncTask.doAction(() -> UIApp.getDB().trackLogDao().insert(trackLog));
    }

}
