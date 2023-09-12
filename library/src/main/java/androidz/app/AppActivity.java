package androidz.app;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidz.action.HandlerAction;

public class AppActivity extends AppCompatActivity implements HandlerAction {

    /**
     * @see #setContentView
     */
    protected View getContentView() {
        ViewGroup contentParent = findViewById(Window.ID_ANDROID_CONTENT);
        return contentParent.getChildAt(0);
    }

    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(ApplicationInstance.getInstance()).get(modelClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeCallbacks();
    }

}
