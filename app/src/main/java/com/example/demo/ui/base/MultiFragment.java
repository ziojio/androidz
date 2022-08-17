package com.example.demo.ui.base;

import com.example.demo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * 多个 Fragment 像 Activity 一样显示和关闭
 */
public class MultiFragment extends BaseFragment {

    public void pop() {
        pop(null, 0);
    }

    public void pop(@Nullable String name, int flags) {
        if (getParentFragmentManager().getBackStackEntryCount() == 0) {
            requireActivity().finish();
        } else {
            getParentFragmentManager().popBackStack(name, flags);
        }
    }

    public void start(@NonNull Fragment fragment) {
        start(fragment, null, null);
    }

    /**
     * @param name An optional name for this back stack state, or null.
     */
    public void start(@NonNull Fragment fragment, @Nullable String tag, @Nullable String name) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(getId(), fragment, tag);
        transaction.addToBackStack(name);
        transaction.commit();
    }
}
