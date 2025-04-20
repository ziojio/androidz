package androidz.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

public final class AndroidzInitializer implements Initializer<AndroidzInitializer> {
    @NonNull
    @Override
    public AndroidzInitializer create(@NonNull Context context) {
        UtilApp.initialize(context);
        return this;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
