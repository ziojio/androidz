package com.example.demo.database;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TrackLog {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int priority;
    public String tag;
    public String message;
    public String timestamp;

    public static TrackLog error(String tag, String message) {
        TrackLog log = new TrackLog();
        log.priority = Log.ERROR;
        log.tag = tag;
        log.message = message;
        log.timestamp = FORMAT.format(new Date());
        return log;
    }

    public static TrackLog debug(String tag, String message) {
        TrackLog log = new TrackLog();
        log.priority = Log.DEBUG;
        log.tag = tag;
        log.message = message;
        log.timestamp = FORMAT.format(new Date());
        return log;
    }
}
