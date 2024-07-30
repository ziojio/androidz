package demo;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import timber.log.Timber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void onStart() {
        Utils.initLogger();
        Timber.d("start");
    }

    @Test
    public void test() {
        doFun();
    }

    @After
    public void onEnd() {
        Timber.d("end");
    }

    private void doFun() {
        String str = "A";
        string_info(str);
        str = "&";
        string_info(str);
        str = "℃";
        string_info(str);
        str = "℉";
        string_info(str);
        str = "好";
        string_info(str);
        str = "个";
        string_info(str);
        str = "鞞";
        string_info(str);
        str = "👌";
        string_info(str);
        str = "🀄";
        string_info(str);
    }

    private void string_info(String str) {
        Timber.d("------------------------------------------");
        Timber.d("str " + str);
        Timber.d("str.length " + str.length());
        Timber.d("str.bytes " + str.getBytes().length);
        Timber.d("str.codePoints " + str.codePoints().findFirst().getAsInt());
        Timber.d("str.codePoints hex " + Integer.toHexString(str.codePoints().findFirst().getAsInt()));
        Timber.d("str.CharArray " + Arrays.toString(str.toCharArray()));
        Timber.d("str.CharArray " + str.toCharArray().length);
    }
}