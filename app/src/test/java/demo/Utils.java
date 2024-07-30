package demo;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

import timber.log.Timber;


public class Utils {

    public static void initLogger() {
        final Logger logger = Logger.getLogger("UnitTestLogger");
        logger.setLevel(Level.ALL);
        logger.log(Level.CONFIG, "initLogger");
        Timber.plant(new Timber.Tree() {
            @Override
            protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
                if (tag == null || tag.isBlank()) {
                    tag = "";
                } else {
                    tag += ": ";
                }
                switch (priority) {
                    case Log.DEBUG:
                        logger.log(Level.CONFIG, tag + message, t);
                        break;
                    case Log.INFO:
                        logger.log(Level.INFO, tag + message, t);
                        break;
                    case Log.WARN:
                        logger.log(Level.WARNING, tag + message, t);
                        break;
                    case Log.ERROR:
                    case Log.ASSERT:
                        logger.log(Level.SEVERE, tag + message, t);
                        break;
                    case Log.VERBOSE:
                    default:
                        logger.log(Level.ALL, tag + message, t);
                        break;
                }
            }
        });
    }

}
