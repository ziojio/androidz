package com.example.demo.database;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @TypeConverter
    public Date fromTimestamp(long value) {
        return new Date(value);
    }

    @TypeConverter
    public long toTimestamp(Date date) {
        if (date == null)
            return 0;
        return date.getTime();
    }

    @TypeConverter
    public String fromDate(Date date) {
        if (date == null)
            return null;
        return dateFormat.format(date);
    }

    @TypeConverter
    public Date toDate(String date) {
        if (date == null)
            return null;
        try {
            return dateFormat.parse(date);
        } catch (ParseException ignored) {
            return null;
        }
    }

}
