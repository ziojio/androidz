package hummerx.adapter;

import android.content.Context;
import android.content.Intent;

import com.didi.hummer.adapter.navigator.NavPage;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.adapter.navigator.impl.IntentCreator;

import hummerx.ui.HummerActivity;

public class MyIntentCreator implements IntentCreator {

    public void appendBaseIntentParams(Intent intent, NavPage page) {
        intent.putExtra(DefaultNavigatorAdapter.EXTRA_PAGE_ID, page.id);
        intent.putExtra(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL, page);
    }

    @Override
    public Intent createHummerIntent(Context context, NavPage page) {
        Intent intent = new Intent(context, HummerActivity.class);
        appendBaseIntentParams(intent, page);
        return intent;
    }

    @Override
    public Intent createWebIntent(Context context, NavPage page) {
        return null;
    }

    @Override
    public Intent createCustomIntent(Context context, NavPage page) {
        return null;
    }
}