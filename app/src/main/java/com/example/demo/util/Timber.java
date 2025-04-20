package com.example.demo.util;

import android.os.Build;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logging for lazy people.
 */
@SuppressWarnings({"unused"}) // Public API.
public final class Timber {

    public static void v(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.VERBOSE, null, message, args);
        }
    }

    public static void v(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.VERBOSE, tr, null);
        }
    }

    public static void d(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.DEBUG, null, message, args);
        }
    }

    public static void d(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.DEBUG, tr, null);
        }
    }

    public static void i(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.INFO, null, message, args);
        }
    }

    public static void i(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.INFO, tr, null);
        }
    }

    public static void w(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.WARN, null, message, args);
        }
    }

    public static void w(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.WARN, tr, null);
        }
    }

    public static void e(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.ERROR, null, message, args);
        }
    }

    public static void e(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.ERROR, tr, null);
        }
    }

    public static void wtf(String message, Object... args) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.ASSERT, null, message, args);
        }
    }

    public static void wtf(Throwable tr) {
        Tree[] forest = forestAsArray;
        for (Tree tree : forest) {
            println(tree, Log.ASSERT, tr, null);
        }
    }

    /**
     * Add a new logging tree.
     */
    @SuppressWarnings("ConstantConditions") // Validating public API contract.
    public static void plant(@NotNull Tree tree) {
        if (tree == null) {
            throw new NullPointerException("tree == null");
        }
        synchronized (FOREST) {
            FOREST.add(tree);
            forestAsArray = FOREST.toArray(new Tree[FOREST.size()]);
        }
    }

    /**
     * Remove a planted tree.
     */
    public static void uproot(@NotNull Tree tree) {
        synchronized (FOREST) {
            if (!FOREST.remove(tree)) {
                throw new IllegalArgumentException("Cannot uproot tree which is not planted: " + tree);
            }
            forestAsArray = FOREST.toArray(new Tree[FOREST.size()]);
        }
    }

    /**
     * Remove all planted trees.
     */
    public static void uprootAll() {
        synchronized (FOREST) {
            FOREST.clear();
            forestAsArray = TREE_ARRAY_EMPTY;
        }
    }

    public static int treeCount() {
        synchronized (FOREST) {
            return FOREST.size();
        }
    }

    private static final Tree[] TREE_ARRAY_EMPTY = new Tree[0];
    // Both fields guarded by 'FOREST'.
    private static final ArrayList<Tree> FOREST = new ArrayList<>();
    private static volatile Tree[] forestAsArray = TREE_ARRAY_EMPTY;

    private Timber() {
        throw new AssertionError("No instances.");
    }

    private static final int MAX_TAG_LENGTH = 23;
    private static final int CALL_STACK_INDEX = 3;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    /**
     * Extract the tag which should be used for the message from the {@code element}. By default
     * this will use the class name without any anonymous class suffixes (e.g., {@code Foo$1}
     * becomes {@code Foo}).
     */
    private static String createStackElementTag() {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        StackTraceElement element = stackTrace[CALL_STACK_INDEX];
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        // Tag length limit was removed in API 24.
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }

    private static void println(Tree tree, int priority, Throwable tr, String message, Object... args) {
        String tag = createStackElementTag();
        if (!tree.isLoggable(tag, priority)) {
            return;
        }
        if (message == null || message.isEmpty()) {
            if (tr == null) {
                return; // Swallow message if it's null and there's no throwable.
            }
            message = getStackTraceString(tr);
        } else {
            if (args != null && args.length > 0) {
                message = String.format(message, args);
            }
            if (tr != null) {
                message += "\n" + getStackTraceString(tr);
            }
        }
        tree.log(priority, tag, message, tr);
    }

    private static String getStackTraceString(Throwable tr) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * A facade for handling logging calls. Install instances via {@link #plant Timber.plant()}.
     */
    public static abstract class Tree {
        /**
         * Return whether a message at {@code priority} or {@code tag} should be logged.
         */
        protected boolean isLoggable(@Nullable String tag, int priority) {
            return true;
        }

        /**
         * Write a log message to its destination. Called for all level-specific methods by default.
         *
         * @param priority Log level. See {@link Log} for constants.
         * @param tag      Explicit or inferred tag. May be {@code null}.
         * @param message  Formatted log message. May be {@code null}, but then {@code t} will not be.
         * @param t        Accompanying exceptions. May be {@code null}, but then {@code message} will not be.
         */
        protected abstract void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t);
    }

    /**
     * A {@link Tree Tree} for debug builds.
     */
    public static class DebugTree extends Tree {
        private static final int MAX_LOG_LENGTH = 4000;

        /**
         * Break up {@code message} into maximum-length chunks (if needed) and send to either
         * {@link Log#println(int, String, String) Log.println()} or
         * {@link Log#wtf(String, String) Log.wtf()} for logging.
         */
        @Override
        protected void log(int priority, String tag, @NotNull String message, Throwable t) {
            if (message.length() < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message);
                } else {
                    Log.println(priority, tag, message);
                }
                return;
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part);
                    } else {
                        Log.println(priority, tag, part);
                    }
                    i = end;
                } while (i < newline);
            }
        }
    }
}
