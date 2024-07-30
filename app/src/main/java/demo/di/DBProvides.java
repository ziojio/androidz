package demo.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import demo.UIApp;
import demo.database.room.AppDB;
import demo.database.room.entity.TrackLogDao;


@Module
@InstallIn(SingletonComponent.class)
public interface DBProvides {

    @Provides
    static AppDB DB() {
        return UIApp.getDB();
    }

    @Provides
    static TrackLogDao trackLogDao() {
        return UIApp.getDB().trackLogDao();
    }
}
