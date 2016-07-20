package com.vcredit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.vcredit.app.R;
import com.vcredit.utils.CommonUtils;

/**
 * 自定义一键删除EditText
 * Created by zhuofeng on 2015/7/14.
 */
public class EditTextWithDel extends EditText implements View.OnFocusChangeListener{

    private Drawable drawable;
    private Context mContext;
    private EditText tmpEditText;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithDel);
        drawable = attributes.getDrawable(R.styleable.EditTextWithDel_delSrc);
        attributes.recycle();
        init();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    private void init(){

        if (drawable == null)
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.edt_delete_all);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    private void setDrawable(){
        if (length() <= 0){
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }else {
            drawable.setBounds(0, 0, CommonUtils.Dp2Px(mContext, 15), CommonUtils.Dp2Px(mContext, 15));
//            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawable != null && event.getAction() == MotionEvent.ACTION_UP){
            int x = (int) event.getX() ;
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) && (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = drawable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) /2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if(isInnerWidth && isInnerHeight) {
                setText("");
                if (tmpEditText != null)
                    tmpEditText.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    public void setTmpEditText(EditText tmpEditText) {
        this.tmpEditText = tmpEditText;
    }
}
