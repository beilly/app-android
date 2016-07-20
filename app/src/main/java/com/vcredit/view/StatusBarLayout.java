package com.vcredit.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class StatusBarLayout extends View {

    private Context mContext;

    public StatusBarLayout(Context context) {
        super(context);
        mContext = context;
    }

    public StatusBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public StatusBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            height = getStatusBarHeight(mContext);
        }
        setMeasuredDimension(mContext.getResources().getDisplayMetrics().widthPixels, height);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
