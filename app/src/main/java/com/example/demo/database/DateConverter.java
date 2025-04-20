package com.example.demo.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.room.TypeConverter;

public class DateConverter {
    private final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @TypeConverter
    public Date fromTimestamp(long value) {
        return new Date(value);
    }

    @TypeConverter
    public long toTimestamp(Date date) {
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public String fromDate(Date date) {
        if (date == null) return null;
        return FORMAT.format(date);
    }

    @TypeConverter
    public Date toDate(String date) {
        if (date == null) return null;
        try {
            return FORMAT.parse(date);
        } catch (ParseException ignored) {
            return null;
        }
    }

}
