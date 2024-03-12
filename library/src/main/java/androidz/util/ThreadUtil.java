package androidz.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtil {
    private static final String TAG = "ThreadUtil";

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static volatile ThreadPoolExecutorUtil sExecutor;

    public static Handler getMainHandler() {
        return sHandler;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean isBackgroundThread() {
        return !isMainThread();
    }

    public static void runOnUiThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            sHandler.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        sHandler.postDelayed(runnable, delayMillis);
    }

    public static void execute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    public static ThreadPoolExecutor getExecutor() {
        if (sExecutor == null) {
            synchronized (ThreadUtil.class) {
                if (sExecutor == null) {
                    int processors = Runtime.getRuntime().availableProcessors();
                    int core = Math.max(2, processors / 2);
                    int max = processors + core;
                    sExecutor = new ThreadPoolExecutorUtil(core, max, 30, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(processors), new ThreadFactoryImpl(),
                            new RejectedExecutionHandler() {
                                @Override
                                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                    Log.e(TAG, "rejectedExecution " + r.toString());
                                }
                            });
                    sExecutor.allowCoreThreadTimeOut(true);
                }
            }
        }
        return sExecutor;
    }

    private static final class ThreadPoolExecutorUtil extends ThreadPoolExecutor {

        ThreadPoolExecutorUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                               BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                               RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public void execute(Runnable command) {
            if (command == null) return;
            if (isShutdown()) return;
            try {
                super.execute(command);
            } catch (Exception e) {
                Log.e(TAG, "execute " + command, e);
            }
        }
    }

    private static final class ThreadFactoryImpl implements ThreadFactory {
        private final AtomicInteger count = new AtomicInteger(1);

        public Thread newThread(Runnable run) {
            Thread thread = new Thread(run, TAG + "#" + count.getAndIncrement()) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable t) {
                        Log.e(TAG, getName(), t);
                    }
                }
            };
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Log.e(TAG, "uncaughtExceptionHandler for " + t.getName(), e);
                }
            });
            return thread;
        }
    }
}