package demo;

import android.app.Application;

public class DemoApp extends Application {
    static DemoApp demoApp;

    public static DemoApp getApp() {
        return demoApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        demoApp = this;

    }

}
