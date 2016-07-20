package com.vcredit.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.vcredit.app.R;

/**
 * An EditText implementing the material design guidelines for password input:
 * https://www.google.com/design/spec/components/text-fields.html#text-fields-password-input
 * <p>
 * It uses the right drawable for the visibility indicator.  If you try to use it yourself, you
 * will have a bad time.
 */
public class PasswordView extends EditText {

    private Drawable eye;
    private Drawable eyeWithStrike;
    private boolean visible = false;

    public PasswordView(Context context) {
        super(context);
        init(null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setSingleLine();
        eye = ContextCompat.getDrawable(getContext(), R.mipmap.eye_on).mutate();
        eyeWithStrike = ContextCompat.getDrawable(getContext(), R.mipmap.eye_off).mutate();
        setup();
    }

    protected void setup() {
        if (visible) {
            // 显示密码
            setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance()); //数字
        } else {
            //setTransformationMethod 则可以支持将输入的字符转换，包括清除换行符、转换为掩码
            setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        }
        Drawable drawable = visible ? eye : eyeWithStrike;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = getCompoundDrawables()[2];
            int x = (int) event.getX() ;
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > getWidth() - getTotalPaddingRight() -deviation) && (x < (getWidth() - getPaddingRight() + deviation));
            //获取图标的边界，返回一个Rect对象
            Rect rect = drawable.getBounds();
            //获取图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) /2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中图标
            boolean isInnerHeight = (y > distance - deviation) && ((y < (distance + height) + deviation));
            if(isInnerWidth && isInnerHeight) {
                visible = !visible;
                setup();
            }
        }

        return super.onTouchEvent(event);
    }

    protected int deviation = 5;

    /**
     * 误差
     * @param deviation
     */
    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }
}
