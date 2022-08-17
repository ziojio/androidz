package androidz.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    public static void setBoundsFromRect(@NonNull View view, @NonNull Rect rect) {
        view.setLeft(rect.left);
        view.setTop(rect.top);
        view.setRight(rect.right);
        view.setBottom(rect.bottom);
    }

    @NonNull
    public static Rect calculateRectFromBounds(@NonNull View view) {
        return calculateRectFromBounds(view, 0);
    }

    @NonNull
    public static Rect calculateRectFromBounds(@NonNull View view, int offsetY) {
        return new Rect(
                view.getLeft(), view.getTop() + offsetY, view.getRight(), view.getBottom() + offsetY);
    }

    @NonNull
    public static Rect calculateOffsetRectFromBounds(@NonNull View view, @NonNull View offsetView) {
        int[] offsetViewAbsolutePosition = new int[2];
        offsetView.getLocationOnScreen(offsetViewAbsolutePosition);
        int offsetViewAbsoluteLeft = offsetViewAbsolutePosition[0];
        int offsetViewAbsoluteTop = offsetViewAbsolutePosition[1];

        int[] viewAbsolutePosition = new int[2];
        view.getLocationOnScreen(viewAbsolutePosition);
        int viewAbsoluteLeft = viewAbsolutePosition[0];
        int viewAbsoluteTop = viewAbsolutePosition[1];

        int fromLeft = offsetViewAbsoluteLeft - viewAbsoluteLeft;
        int fromTop = offsetViewAbsoluteTop - viewAbsoluteTop;
        int fromRight = fromLeft + offsetView.getWidth();
        int fromBottom = fromTop + offsetView.getHeight();

        return new Rect(fromLeft, fromTop, fromRight, fromBottom);
    }

    @NonNull
    public static List<View> getChildren(@Nullable View view) {
        List<View> children = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                children.add(viewGroup.getChildAt(i));
            }
        }
        return children;
    }

    public static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }

    public static boolean isLayoutRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    /**
     * Returns the content view that is the parent of the provided view.
     */
    @Nullable
    public static ViewGroup getContentView(@Nullable View view) {
        if (view == null) {
            return null;
        }

        View rootView = view.getRootView();
        ViewGroup contentView = rootView.findViewById(android.R.id.content);
        if (contentView != null) {
            return contentView;
        }

        // Account for edge cases: Parent's parent can be null without ever having found
        // android.R.id.content (e.g. if view is in an overlay during a transition).
        // Additionally, sometimes parent's parent is neither a ViewGroup nor a View (e.g. if view
        // is in a PopupWindow).
        if (rootView != view && rootView instanceof ViewGroup) {
            return (ViewGroup) rootView;
        }

        return null;
    }

    public static void addOnGlobalLayoutListener(
            @Nullable View view, @NonNull ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(victim);
        }
    }

    public static void removeOnGlobalLayoutListener(
            @Nullable View view, @NonNull ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (view != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        }
    }

    /**
     * Returns the provided view's background color, if it has ColorDrawable as its background, or
     * {@code null} if the background has a different drawable type.
     */
    @Nullable
    public static Integer getBackgroundColor(@NonNull View view) {
        return view.getBackground() instanceof ColorDrawable
                ? ((ColorDrawable) view.getBackground()).getColor()
                : null;
    }
}