package demo.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import demo.database.DBConfig;
import demo.database.room.entity.TrackLog;
import demo.database.room.entity.TrackLogDao;
import demo.database.room.entity.UserInfo;
import demo.database.room.entity.UserInfoDao;

@Database(
        version = DBConfig.DB_VERSION,
        entities = {
                UserInfo.class,
                TrackLog.class
        },
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {

    public static AppDB create(Context appContext) {
        return Room.databaseBuilder(appContext, AppDB.class, DBConfig.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract UserInfoDao userDao();

    public abstract TrackLogDao trackLogDao();

}
