package androidz.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @see SimpleDateFormat 格式化字符的含义
 */
public final class TimeUtil {
    private static final String[] CHINESE_ZODIAC = new String[]{
            "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"
    };

    private static String sDateTimePattern = "yyyy-MM-dd HH:mm:ss";

    @NonNull
    public static DateFormat getDefaultFormat() {
        return new SimpleDateFormat(sDateTimePattern, Locale.getDefault());
    }

    public static void setDefaultPattern(@NonNull String pattern) {
        sDateTimePattern = pattern;
    }

    public static String now() {
        return getDefaultFormat().format(new Date());
    }

    public static String format(long millis) {
        return getDefaultFormat().format(millis);
    }

    public static String format(@NonNull Date date) {
        return getDefaultFormat().format(date);
    }

    public static String format(@NonNull Date date, @NonNull String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    @Nullable
    public static Date parse(@NonNull String time) {
        try {
            return getDefaultFormat().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Date parse(@NonNull String time, @NonNull String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFriendlyTimeSpanByNow(@NonNull Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0L) {
            return String.format("%tc", millis);
        } else if (span < 1000L) {
            return "刚刚";
        } else if (span < 60000L) {
            return String.format(Locale.getDefault(), "%d秒前", span / 1000L);
        } else if (span < 3600000L) {
            return String.format(Locale.getDefault(), "%d分钟前", span / 60000L);
        } else {
            long wee = getWeeOfToday();
            if (millis >= wee) {
                return String.format("今天%tR", millis);
            } else {
                return millis >= wee - 86400000L ? String.format("昨天%tR", millis) : String.format("%tF", millis);
            }
        }
    }

    public static boolean isToday(@NonNull Date date) {
        return isToday(date.getTime());
    }

    public static boolean isToday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + 86400000L;
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static boolean isLeapYear(@NonNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static String getChineseWeek(@NonNull Date date) {
        return new SimpleDateFormat("E", Locale.CHINA).format(date);
    }

    public static String getChineseZodiac(@NonNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

}
