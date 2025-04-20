package androidz.util.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 把OnActivityResult方式改为Callback
 */
@SuppressWarnings({"unused", "deprecation"})
public class ActivityLauncher {
    private static final String TAG = "ActivityLauncher";
    private final Context mContext;
    private RouterFragment mRouterFragment;
    private RouterFragmentX mRouterFragmentX;

    public static ActivityLauncher with(@NonNull Context context) {
        return new ActivityLauncher(context);
    }

    public static ActivityLauncher with(@NonNull Fragment fragment) {
        return with(fragment.requireActivity());
    }

    private ActivityLauncher(@NonNull Context context) {
        mContext = context;
        if (context instanceof FragmentActivity) {
            mRouterFragmentX = getRouterFragmentX((FragmentActivity) context);
        } else if (context instanceof Activity) {
            mRouterFragment = getRouterFragment((Activity) context);
        }
    }

    private RouterFragmentX getRouterFragmentX(FragmentActivity activity) {
        RouterFragmentX routerFragment = findRouterFragmentX(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragmentX.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragmentX findRouterFragmentX(FragmentActivity activity) {
        return (RouterFragmentX) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    private RouterFragment getRouterFragment(Activity activity) {
        RouterFragment routerFragment = findRouterFragment(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragment findRouterFragment(Activity activity) {
        return (RouterFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, Callback callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, Callback callback) {
        if (mRouterFragmentX != null) {
            mRouterFragmentX.startActivityForResult(intent, callback);
        } else if (mRouterFragment != null) {
            mRouterFragment.startActivityForResult(intent, callback);
        } else {
            mContext.startActivity(intent);
        }
    }

    public interface Callback {
        void onActivityResult(int resultCode, Intent data);
    }
}
