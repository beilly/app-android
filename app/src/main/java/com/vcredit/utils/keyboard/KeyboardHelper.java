package com.vcredit.utils.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.vcredit.app.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 键盘助手类
 */
public class KeyboardHelper implements View.OnClickListener, View.OnFocusChangeListener, OnKeyboardActionListener {
    private final static int TAG_ET = R.id.keyboard_view;
    private final static int TAG_TOLAST = TAG_ET + 1;

    protected Keyboard keyMain;// 主键盘

    private static PopupWindow keyboardWindow;
    private View keyboardLayout;
    protected KeyboardView keyboardView;

    public KeyboardHelper(Fragment fragment) {
        this(fragment.getActivity(), null);
    }

    public KeyboardHelper(Fragment fragment, EditText editText) {
        this(fragment, editText, false);
    }

    public KeyboardHelper(Fragment fragment, EditText editText, boolean toLast) {
        this(fragment.getActivity(), editText, toLast);
    }

    public KeyboardHelper(Context mActivity) {
        this(mActivity, null);
    }

    public KeyboardHelper(Context mActivity, EditText editText) {
        this(mActivity, editText, false);
    }

    public KeyboardHelper(Context mActivity, EditText editText, boolean toLast) {
        keyboardWindow = createKeyboardWindow(mActivity.getApplicationContext());
        keyboardView = (KeyboardView) keyboardWindow.getContentView().findViewById(R.id.keyboard_view);
        keyboardLayout = keyboardWindow.getContentView().findViewById(R.id.keyboard_view_layout);
        initKeyboar(mActivity);
        addEditText(editText, toLast);
    }

    /**
     * 初始化键盘
     * @param mActivity
     * @return
     */
    protected void initKeyboar(Context mActivity){
        keyMain = new Keyboard(mActivity.getApplicationContext(), R.xml.keyboard_symbols);
        keyboardView.setKeyboard(keyMain);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);
    }

    /**
     * 添加键盘布局
     * @param editText
     * @return
     */
    public KeyboardHelper addEditText(EditText editText) {
        return addEditText(editText, false);
    }
    /**
     * 添加键盘布局
     * @param editText
     * @param toLast
     * @return
     */
    public KeyboardHelper addEditText(EditText editText, boolean toLast) {
        if (editText == null)
            return this;

        editText.setTag(TAG_TOLAST, toLast);
        editText.setCursorVisible(true);
        editText.setSingleLine(false);
        editText.setOnClickListener(this);
        editText.setOnFocusChangeListener(this);

        int inputType = editText.getInputType();
        if (isPasswordInputType(inputType)) {
            changeSafeDisplay(editText, false);
        }
        return this;
    }

    /**
     * 检测当前绑定的密码输入框是否是密码输入
     *
     * @param showPwd
     * @return
     */
    public KeyboardHelper changeSafeDisplay(boolean showPwd) {
        EditText editText = getEditTextByTag();
        if (editText == null) return this;

        return changeSafeDisplay(editText, showPwd);
    }

    /**
     * 检测指定输入框是否是密码输入
     *
     * @param editText
     * @param showPwd
     * @return
     */
    public KeyboardHelper changeSafeDisplay(EditText editText, boolean showPwd) {

        if (showPwd) {
            // 显示密码
            editText.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance()); //数字
        } else {
            //setTransformationMethod 则可以支持将输入的字符转换，包括清除换行符、转换为掩码
            editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        }

        return this;
    }

    @Override
    public void onFocusChange(View arg0, boolean arg1) {
        if (!(arg0 instanceof EditText)) {
            return;
        }

        EditText editText = (EditText) arg0;
        if (arg1) {
            //修改光标的位置
            updateSelection(editText);
            //键盘操作
            if (android.os.Build.VERSION.SDK_INT <= 10) {// 3.0以下
                editText.setInputType(InputType.TYPE_NULL);
            } else {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                try {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        setShowSoftInputOnFocus = cls.getMethod(
                                "setShowSoftInputOnFocus",
                                boolean.class);
                    } else {
                        setShowSoftInputOnFocus = cls.getMethod(
                                "setSoftInputShownOnFocus",
                                boolean.class);
                    }
                    setShowSoftInputOnFocus.setAccessible(true);
                    setShowSoftInputOnFocus.invoke(editText, false);
                } catch (Exception e) {

                }
            }
            InputMethodManager imm = (InputMethodManager) editText.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            showKeyboard(editText);
        } else {
            hideKeyboard(editText);
        }
    }


    @Override
    public void onClick(View arg0) {
        if (!(arg0 instanceof EditText)) {
            return;
        }

        EditText editText = (EditText) arg0;
        updateSelection(editText);
        this.showKeyboard(editText);
    }


    @Override
    public void swipeUp() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        EditText editText = getEditTextByTag();
        if (editText == null) return;

        Editable editable = editText.getText();
        int start = editText.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
            hideKeyboard(editText);
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (!TextUtils.isEmpty(editable)) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else {
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    @Nullable
    protected EditText getEditTextByTag() {
        Object arg0 = keyboardView.getTag(TAG_ET);
        if (arg0 == null || !(arg0 instanceof EditText)) {
            return null;
        }
        return (EditText) arg0;
    }


    /**
     * 随机键值标志位，默认false
     */
    protected boolean shouldRandom = false;

    /**
     * 是否生成随机键值
     *
     * @param shouldRandom
     */
    public KeyboardHelper setShouldRandom(boolean shouldRandom) {
        this.shouldRandom = shouldRandom;
        return this;
    }

    public void showKeyboard(@NonNull EditText editText) {
        keyboardView.setTag(TAG_ET, editText);

        int visibility = keyboardLayout.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardLayout.setVisibility(View.VISIBLE);
        }
        if (shouldRandom)
            randomKey();

        if (editText.getWindowToken() != null) {
//            Resources resources = editText.getContext().getApplicationContext().getResources();
//            DisplayMetrics dm = resources.getDisplayMetrics();
//            int width3 = dm.widthPixels;
//            int height3 = dm.heightPixels;
//            keyboardWindow.showAtLocation(editText.getRootView(), Gravity.BOTTOM, 0, -height3);
            keyboardWindow.showAtLocation(editText.getRootView(), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 修改光标的位置
     * @param editText
     */
    protected void updateSelection(@NonNull EditText editText) {
        //修改光标的位置
        boolean toLast = (boolean) editText.getTag(TAG_TOLAST);
        if (toLast) {
            CharSequence text = editText.getText();
            editText.setSelection(text.length());
        }
    }

    public void hideKeyboard(EditText editText) {
        keyboardView.setTag(TAG_ET, null);
        int visibility = keyboardLayout.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            hideKeyboard();
        }
    }

    /**
     * 强制隐藏键盘
     */
    public static synchronized void hideKeyboard(){
        try {
            if (keyboardWindow != null)
            keyboardWindow.dismiss();
        } catch (Exception e) {
        }
    }

    protected boolean isNumber(String str) {
        String wordstr = "0123456789";
        if (wordstr.indexOf(str) > -1) {
            return true;
        }
        return false;
    }

    protected boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

    protected void randomKey() {
        List<Key> keyList = keyMain.getKeys();
        // 查找出0-9的数字键
        List<Key> newkeyList = new ArrayList<Key>();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).label != null
                    && isNumber(keyList.get(i).label.toString())) {
                newkeyList.add(keyList.get(i));
            }
        }
        // 数组长度
        int count = newkeyList.size();
        // 结果集
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        // 用一个LinkedList作为中介
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        // 初始化temp
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        // 取数
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num)
                    .getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i).getCode();
        }
        keyboardView.setKeyboard(keyMain);
    }

    protected static PopupWindow createKeyboardWindow(Context context) {
        if (keyboardWindow != null)
            return keyboardWindow;

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.keyboard_layout, null);

        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width3 = dm.widthPixels;
        int height3 = dm.heightPixels;

        PopupWindow popupWindow = new PopupWindow(contentView,
                height3, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
//
//		popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				return false;
//				// 这里如果返回true的话，touch事件将被拦截
//				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//			}
//		});

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        popupWindow.setAnimationStyle(R.style.keyboard_anim_style);

        return popupWindow;
    }

    private static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }
}
