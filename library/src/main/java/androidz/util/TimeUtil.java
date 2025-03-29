package androidz.util;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @see SimpleDateFormat 格式化字符的含义
 */
public final class TimeUtil {
    private static final String[] CHINESE_ZODIAC = new String[]{
            "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"
    };

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
