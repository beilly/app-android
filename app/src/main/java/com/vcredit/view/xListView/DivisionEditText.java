package com.vcredit.view.xListView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * 分割输入框
 * Created by wangzhengji on 2015/9/6.
 */
public class DivisionEditText extends EditText {
    /* 每组的长度 */
    private Integer length = 3;
    /* 限制字符长度 */
    private Integer maxLength = 5;
    /* 分隔符 */
    private String delimiter = ",";
    /* 输入文本 */
    private String text = "";
    private EditText editText;
    private boolean isChange = false;
    private int commaNumBeforeModified; //改变前逗号个数
    private int commaNumAfterModified;  //改变后逗号个数
    public DivisionEditText(Context context) {
        super(context);
        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    /** 得到每组个数 */
    public Integer getLength() {
        return length;
    }

    /** 设置每组个数 */
    public void setLength(Integer length) {
        this.length = length;
    }

    /** 设置最大字符录入长度 */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /** 得到间隔符 */
    public String getDelimiter() {
        return delimiter;
    }

    /** 设置间隔符 */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getInputText() {
        return super.getText().toString().replace(getDelimiter(), "");
    }

    /**
     * 初始化
     */
    public void init() {

        // 内容变化监听
        this.addTextChangedListener(new DivisionTextWatcher());
        // 获取焦点监听
        this.setOnFocusChangeListener(new DivisionFocusChangeListener());

        editText = this;
    }

    /**
     * 文本监听
     *
     * @author Administrator
     *
     */
    private class DivisionTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            commaNumBeforeModified = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ',') {
                    commaNumBeforeModified++;
                }
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            //获取当前的光标位置
            int location = getSelectionEnd();
            // 统计个数
            int len = s.length();

            // 输入首字母为0之后则不显示
            if (len > 1) {
                if (s.toString().charAt(0) == '0') {
                    editText.setText("0");
                    setSelection(1);
                    return;
                }
            }

//            if (len < getLength())// 长度小于要求的数
//                return;
//            if (count > 1) {
//                return;
//            }
            if(isChange){
                return;
            }
            commaNumAfterModified= 0;
            if (len > 6) {//限制输入的长度
                String str = editText.getText().toString();
                // 截取新字符串
                String newStr = str.substring(0, 6);
                text = inversionString(formatSymbol(inversionString(newStr)));
//                maxListener.afterLengthMax();//自定义接口，实现监听回调
            } else {
                //先倒置，运算之后再倒置回来
                text = inversionString(formatSymbol(inversionString(s.toString())));//关键点
            }
            isChange = true;
            setText(text);
            isChange = false;

            //用来判断是增加还是删减
            if(commaNumAfterModified>commaNumBeforeModified){
                //有一种特殊情况也会认为是增加
                //(1)如果0000 0000 0000 0000
                //           |(光标位置)
                //然后按下删除键空格被删掉了这时候 before的空格数是2 after是3
                location += commaNumAfterModified -commaNumBeforeModified;
            }
            //用来判断是增加还是删减
            if(location>text.length()){
                location = text.length();
            }

            if((6 ==text.length())&&(3 == location)||(5 == text.length()&&(2 == location))){
                //这回收当前光标都在逗号的后面，如果删除的话，光标就跳到逗号前面
                location --;
            }
            setSelection(location);
        }
    }
    /**
     * 若有，先去除，进行计算之后再添加
     */
    private String formatSymbol(String str) {
        char[] chars = str.replace(getDelimiter(), "").toCharArray();
        StringBuffer sb = new StringBuffer();


        for (int i = 0; i < chars.length; i++) {
            if (i % getLength() == 0 && i != 0)// 每次遍历到4的倍数，就添加一个空格
            {
                sb.append(getDelimiter());
                sb.append(chars[i]);// 添加字符
                commaNumAfterModified++;
            } else {
                sb.append(chars[i]);// 添加字符
            }
        }
        return sb.toString();
    }

    /**
     * 字符串逆序*
     * @param str
     * @return
     */
    private String inversionString(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i= 0; i < chars.length; i++) {
            sb.append(chars[chars.length - i - 1]);
        }
        return sb.toString();
    }

    /**
     * 获取焦点监听
     *
     * @author Administrator
     *
     */
    private class DivisionFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // 设置焦点
                setSelection(getText().toString().length());
            }
        }
    }

    /** EditText 长度最大化监听 */
    public interface OnChangeLengthMaxListener {
        public void afterLengthMax();
    }

    public void setOnChangeLengthMaxListener(OnChangeLengthMaxListener maxListener) {
        this.maxListener = maxListener;
    }

    private OnChangeLengthMaxListener maxListener;
}