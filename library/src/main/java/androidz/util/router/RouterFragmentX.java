package androidz.util.router;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;

import java.util.Random;

/**
 * 把OnActivityResult方式转换为Callback方式的空Fragment（兼容包）
 */
@SuppressWarnings("deprecation")
public class RouterFragmentX extends Fragment {

    private final SparseArray<ActivityLauncher.Callback> mCallbacks = new SparseArray<>();

    public RouterFragmentX() {
        // Required empty public constructor
    }

    public static RouterFragmentX newInstance() {
        return new RouterFragmentX();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startActivityForResult(Intent intent, ActivityLauncher.Callback callback) {
        int requestCode = makeRequestCode();
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 随机生成唯一的requestCode，最多尝试10次
     */
    private int makeRequestCode() {
        final Random mCodeGenerator = new Random();
        int requestCode;
        int tryCount = 0;
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (mCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10);
        return requestCode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityLauncher.Callback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(resultCode, data);
        }
    }
}
