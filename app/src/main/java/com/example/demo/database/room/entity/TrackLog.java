package com.example.demo.database.room.entity;

import com.example.demo.database.room.Converters;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class TrackLog {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String tag;
    public String msg;
    @TypeConverters(Converters.class)
    public Date time;

    public TrackLog() {
    }

    @Ignore
    public TrackLog(String tag, String msg) {
        this.tag = tag;
        this.msg = msg;
        this.time = new Date();
    }

    @NonNull
    @Override
    public String toString() {
        return "TrackLog{" +
                "tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
