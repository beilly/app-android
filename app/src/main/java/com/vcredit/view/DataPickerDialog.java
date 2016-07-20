package com.vcredit.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lany.picker.numberpicker.NumberPicker;
import com.vcredit.app.R;
import com.vcredit.utils.CommonUtils;

/**
 * Created by shibenli on 2016/3/31.
 */
public class DataPickerDialog extends AlertDialog implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private final TextView mTitleView;

    public interface OnDataSetListener {
        void onDataSet(NumberPicker view, int index);
    }

    private OnDataSetListener mCallback;

    protected NumberPicker mDataPicker;

    protected int index;

    public DataPickerDialog(Context context, OnDataSetListener callBack) {
        this(context,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? com.lany.picker.R.style.Theme_Dialog_Alert
                        : 0, callBack);
    }

    protected DataPickerDialog(Context context, int theme, OnDataSetListener callback) {
        super(context, theme);

        Context themeContext = getContext();
        mCallback = callback;
        LayoutInflater inflater = (LayoutInflater) themeContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.data_picker_dialog, null);
        setView(view);
        mDataPicker = (NumberPicker) view.findViewById(R.id.data_picker);
        mTitleView = (TextView) view.findViewById(R.id.dialog_title);
        mDataPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        final TextView tvCancle = (TextView) view.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_cancle);
        final TextView tvSure = (TextView) view.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_sure);
        tvCancle.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        mDataPicker.setOnValueChangedListener(this);
        updateTitle();
    }

    public DataPickerDialog setTag(Object tag){
        mDataPicker.setTag(tag);
        return this;
    }

    public DataPickerDialog setTag(int key, Object tag){
        mDataPicker.setTag(key, tag);
        return this;
    }

    public Object getTag() {
        return mDataPicker.getTag();
    }

    public Object getTag(int key) {
        return mDataPicker.getTag(key);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = CommonUtils.Dp2Px(getContext(), 280);
        getWindow().setAttributes(params);
    }

    private void updateTitle() {
        Object[] displayedValues = mDataPicker.getDisplayedValues();
        String title = displayedValues == null || displayedValues.length <= index ? "请选择" : displayedValues[index].toString();
        mTitleView.setText(TextUtils.isEmpty(titleName) ? title : titleName);
    }


    protected String titleName;

    /**
     * 设置显示的标题，如果没有设置标题就会根据当前选项更新
     * @param title
     * @return
     */
    public DataPickerDialog setTitle(String title){
        titleName = title;
        return this;
    }

    /**
     * 设置选择项标题
     * @param displayedValues
     * @return
     */
    public DataPickerDialog setDisplayedValues(String[] displayedValues) {
        return setDisplayedValues(displayedValues, displayedValues == null || displayedValues.length <= 0 ? 0 : displayedValues.length - 1);
    }

    /**
     * 设置选择项标题
     * @param displayedValues
     * @param max
     * @return
     */
    public DataPickerDialog setDisplayedValues(Object[] displayedValues, int max) {
        index = mDataPicker.getValue();
        mDataPicker.setValue(0);
        mDataPicker.setMaxValue(max);
        mDataPicker.setMinValue(0);
        mDataPicker.setDisplayedValues(displayedValues);
        mDataPicker.setWrapSelectorWheel(true);
        mDataPicker.setValue(index);
        updateTitle();
        return this;
    }

    /**
     * 设置普通分割线
     * @param divider
     * @return
     */
    public DataPickerDialog setDividerDrawable(Drawable divider) {
        mDataPicker.setDividerDrawable(divider);
        return this;
    }

    /**
     * 设置选中项的分割线
     * @param selectionDivider
     * @return
     */
    public DataPickerDialog setSelectionDivider(Drawable selectionDivider) {
        this.mDataPicker.setSelectionDivider(selectionDivider);
        return this;
    }

    /**
     * 设置分割线高度
     * @param selectionDividerHeight
     * @return
     */
    public DataPickerDialog setSelectionDividerHeight(int selectionDividerHeight) {
        this.mDataPicker.setSelectionDividerHeight(selectionDividerHeight);
        return this;
    }

    public DataPickerDialog upData(int index) {
//        String[] displayedValues = mDataPicker.getDisplayedValues();
//        if (displayedValues != null && 0 < index && index < displayedValues.length){
//            mDataPicker.setWrapSelectorWheel(true);
//        }else {
//            mDataPicker.setWrapSelectorWheel(false);
//        }
        mDataPicker.setValue(index);
        updateTitle();
        return this;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        index = newVal;
        updateTitle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_withdraw_cash_dynamic_dialog_sure:
                tryNotifyDataSet();
            case R.id.tv_withdraw_cash_dynamic_dialog_cancle:
                dismiss();
                break;
        }
    }

    private void tryNotifyDataSet() {
        if (mCallback != null) {
            mDataPicker.clearFocus();
            mCallback.onDataSet(mDataPicker, mDataPicker.getValue());
        }
    }
}