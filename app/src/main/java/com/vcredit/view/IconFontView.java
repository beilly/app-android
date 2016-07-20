package com.vcredit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.vcredit.app.R;
import com.vcredit.global.AppConfig;
import com.vcredit.utils.CommonUtils;

/**
 * Created by Family on 2016/3/7.
 */
public class IconFontView extends TextView {

    private Context context;
    private boolean isFirst = true;
    private String text;
    private int textColor;
    private String clicked_text;
    private int clicked_textColor;

    public IconFontView(Context context) {
        this(context, null);
    }

    public IconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    /** 初始化 */
    private void init(AttributeSet attrs){
        // 设置字体类型
        setTypeface(Typeface.createFromAsset(context.getAssets(), AppConfig.ICONFONT_PATH));

        // 获取配置信息（text && textColor）
        getAttrs(attrs);
    }

    /** 获得配置信息 */
    private void getAttrs(AttributeSet attrs) {
        TypedArray taCustom = context.obtainStyledAttributes(attrs, R.styleable.IconFontViewAttr);
        clicked_text = taCustom.getString(R.styleable.IconFontViewAttr_clicked_text);
        clicked_textColor = taCustom.getColor(R.styleable.IconFontViewAttr_clicked_textColor, 0);
        taCustom.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            text = getText().toString();
            textColor = getCurrentTextColor();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        if (isDuplicateParentStateEnabled()) {
            CommonUtils.LOG_D(IconFontView.class, "dispatchSetPressed" + pressed);
            if (pressed){
                if(clicked_text != null)
                    this.setText(clicked_text);
                if(clicked_textColor != 0)
                    this.setTextColor(clicked_textColor);
            }else {
                this.setText(text);
                this.setTextColor(textColor);
            }
        }
    }
}
