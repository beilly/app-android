package com.vcredit.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vcredit.app.R;
import com.vcredit.app.main.MainActivity;


/**
 * Created by shibenli on 2015/7/17.
 * 简单的TitleBuilder,只包含左侧的文字和中间标题，请使用simple_titlebar.xml布局文件
 */
public class TitleBuilder {

    /**
     * title栏根布局
     */
    protected View titleView;
    protected TextView tvLeft;
    protected ImageView ivLeft;
    protected ImageView tvRight;
    protected TextView tvMiddle;
    protected Activity _activity;
    protected StatusBarLayout llStatusBar;
    protected RelativeLayout rlLayout;

    /**
     * 标题栏布局，一般不建议修改
     */
    protected int titleBarId;

    /**
     * 构造函数
     * @param activity
     */
    public TitleBuilder(Activity activity) {
        this(activity, R.id.title_bar);
    }

    /**
     * 构造函数
     * @param activity
     */
    public TitleBuilder(Activity activity, int titleBarId) {
        this._activity = activity;
        this.titleBarId = titleBarId;
        titleView = _activity.findViewById(getTitleBarId());
        instanceObjects(titleView);
    }

    /**
     * 构造函数
     * @param
     */
    public TitleBuilder(View view) {
        this(view, R.id.title_bar);
    }

    /**
     * 构造函数
     * @param
     */
    public TitleBuilder(View view, int titleBarId) {
        this.titleBarId = titleBarId;
        titleView = view.findViewById(getTitleBarId());
        instanceObjects(titleView);
    }

    protected int getTitleBarId(){
        return titleBarId;
    }

    /**
     * 实例化对象
     */
    protected void instanceObjects(View titleView){
        llStatusBar = (StatusBarLayout) titleView.findViewById(R.id.title_statusBar);
        rlLayout = (RelativeLayout) titleView.findViewById(R.id.title_rlContainer);
        //暂时按照此方法修复部分机型上Titlebar背景在xml中使用默认时，设置无效的问题；
        setBackgroundColor(titleView.getResources().getColor(R.color.bg_titlbar));
        tvLeft = (TextView) titleView.findViewById(R.id.title_left_textview);
        tvMiddle = (TextView) titleView.findViewById(R.id.title_middle_textview);
        ivLeft = (ImageView) titleView.findViewById(R.id.title_left);
        tvRight = (ImageView) titleView.findViewById(R.id.title_right);
    }

    public View getTitleView() {
        return titleView;
    }

    public TitleBuilder withBackIcon(){
        setLeftIcon(R.mipmap.back);
        if (_activity != null){
            setLeftIconListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.onBackPressed();
                }
            });
        }

        return this;
    }

    public TitleBuilder withHomeIcon(){
        setRightIcon(0);
        if (_activity != null){
            setRightIconListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("return_home", true);
                    intent.setClass(_activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                    _activity.startActivity(intent);
                    _activity.finish();//关掉自己
                }
            });
        }

        return this;
    }

    /**
     * 设置文本标题
     * @param text
     * @return
     */
    public TitleBuilder setMiddleTitleText(String text) {
        tvMiddle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvMiddle.setText(text);
        return this;
    }

    /**
     * 设置左侧图标
     * @param resId
     * @return
     */
    public TitleBuilder setLeftIcon(int resId) {
        if (resId > 0){
            ivLeft.setImageResource(resId);
        }
        ivLeft.setVisibility(resId <= 0 ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置右侧图标
     * @param resId
     * @return
     */
    public TitleBuilder setRightIcon(int resId){
        if (resId > 0){
            tvRight.setImageResource(resId);
        }
        tvRight.setVisibility(resId <= 0 ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置左侧图标点击处理函数
     */
    public TitleBuilder setLeftIconListener(View.OnClickListener listener) {
        if(ivLeft.getVisibility() == View.VISIBLE){
            ivLeft.setOnClickListener(listener);
        }

        if (tvLeft.getVisibility() == View.VISIBLE){
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右侧图标点击处理函数
     */
    public TitleBuilder setRightIconListener(View.OnClickListener listener) {
        if (listener != null && tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右侧图标是否显示
     */
    public TitleBuilder setRightIconVisibility(int visibility){
        tvRight.setVisibility(visibility);
        return this;
    }

    /**
     * 设置标题栏（可定制标题栏事件处理类）
     */
    public TitleBuilder setTitlebar(int leftIcon, View.OnClickListener leftCilckListener, String titleName,
                             int rightIcon, View.OnClickListener rightClickListener) {
        if(leftCilckListener == null){
            leftCilckListener = new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    _activity.finish();
                }
            };
        }

        return this.setLeftIcon(leftIcon)
                .setLeftIconListener(leftCilckListener)
                .setMiddleTitleText(titleName)
                .setRightIcon(rightIcon)
                .setRightIconListener(rightClickListener);
    }

    /**
     * 设置标题栏的外观
     * @param width
     * @param height
     * @param backgroundColor
     * @return
     */
    public TitleBuilder setTitlebar(int width, int height, int backgroundColor){
        rlLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        llStatusBar.setBackgroundResource(backgroundColor);
        rlLayout.setBackgroundResource(backgroundColor);
        return this;
    }

    /**
     * 设置标题栏
     */
    public TitleBuilder setTitlebar(String titleName) {
        return setTitlebar(R.mipmap.titlebar_back, null, titleName, 0, null);
    }

    /**
     * 设置标题栏透明
     * @return
     */
    public TitleBuilder isBackgroundTransparent(){
        return setBackgroundColor(android.R.color.transparent);
    }

    /**
     * 设置标题栏的背景
     * @param color
     * @return
     */
    public TitleBuilder setBackgroundColor(int color){
        llStatusBar.setBackgroundColor(color);
        rlLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题栏背景
     * @param resid
     * @return
     */
    public TitleBuilder setBackgroundResource(int resid){
        llStatusBar.setBackgroundResource(resid);
        rlLayout.setBackgroundResource(resid);
        return this;
    }

    /**
     * 设置左侧文字
     * @param text
     * @return
     */
    public TitleBuilder setLeftText(String text){
        if (TextUtils.isEmpty(text))
            tvLeft.setVisibility(View.INVISIBLE);
        else {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(text);
        }
        return this;
    }
}
