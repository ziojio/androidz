package com.example.demo.database.room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.room.TypeConverter;

public class Converters {
    private final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @TypeConverter
    public String fromDate(Date date) {
        if (date == null) return "";
        return FORMAT.format(date);
    }

    @TypeConverter
    public Date toDate(String date) {
        try {
            return FORMAT.parse(date);
        } catch (ParseException ignored) {
        }
        return null;
    }

}
