package com.vcredit.global;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Constants {
    /**
     * 有效秒数
     */
    public static final int EXPIRATION_SECOND = 600 * 1_000;

    /**
     * 有效秒数
     */
    public static final int TIPS_SECOND = 3 * 1_000;

    /**
     * 日期格式（yyyy-MM-dd）
     */
    public static final SimpleDateFormat FORMAT_DATA_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 日期格式（yyyy-MM-dd HH:mm:ss）
     */
    public static final SimpleDateFormat FORMAT_TIME_yyyy_MM_dd_hh_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 保留两位小数
     */
    public static final DecimalFormat FORMAT_NUMBER_N_NN = new DecimalFormat("#,##0.00");

    /**
     * 选中状态
     */
    public static final int STATUS_SELECTED = 1;
    /**
     * 不可用状态
     */
    public static final int STATUS_DISABLED = 2;
    /**
     * 正常状态
     */
    public static final int STATUS_NORMAL = 3;

    /**
     * 圆形的标签
     */
    public static final int WAYBILL_CIRCLER_TAG = 0;
    /**
     * 圆形的背景
     */
    public static final int WAYBILL_CIRCLER_BG = 1;
    /**
     * 背景色
     */
    public static final int WAYBILL_BG = 2;


    /**
     * 直接登录字符串
     **/
    public static final String STRING_TIPS_LOGIN = "直接登录";


    /*登录方式（normal 普通登录 device 设备号登录）*/
    public static final String LOGIN_KIND_NORMAL = "normal";
    public static final String LOGIN_KIND_DEVICE = "device";
    /*登录状态枚举 - 成功*/
    public static final String LOGIN_STATE_SUCCESS = "1";
    /*登录状态枚举 - 失败*/
    public static final String LOGIN_STATE_FAILURE = "0";
    /*登录状态枚举 - 密码过期*/
    public static final String LOGIN_STATE_OUTDATE = "99";
    /*申请状态（1、未申请 2、申请中 3、已被拒 4、已获得额度）*/
    public static final String APPLYSTATUS_NOT = "1";
    public static final String APPLYSTATUS_ING = "2";
    public static final String APPLYSTATUS_REFUSE = "3";
    public static final String APPLYSTATUS_SUCCESS = "4";
    public static final String APPLYSTATUS_ERROR = "5";


    /**认证状态 */
    /**
     * 未认证
     */
    public static final String AUTH_NOT = "1";
    /**
     * 认证中
     */
    public static final String AUTHENTICATING = "2";
    /**
     * 认证成功
     */
    public static final String AUTH_SUCCESS = "3";
    /**
     * 认证拒绝
     */
    public static final String AUTH_REFUSED = "4";
    /**
     * 网络异常
     */
    public static final String AUTH_ERROR = "5";

    /** 额度状态 */
    /**
     * 额度有效
     */
    public static final String CREDITSTATUS_AVALIABLE = "1";
    /**
     * 额度失效
     */
    public static final String CREDITSTATUS_UNAVALIABLE = "2";

    /**
     * 操作类型 1 注册信息校验 2 短信验证码校验
     */
    /**
     * 1 注册信息校验
     */
    public static final String REGISTRATION_CHECK = "1";

    /**
     * 1 认证登录校验
     */
    public static final String AUTHENTICATION_CHECK = "1";

    /**
     * 2 短信验证码校验
     */
    public static final String VERIFICATION_CODE_CHECK = "2";


    /**
     * 借款状态(审核中、已放款、 扣款中)
     */
    /**
     * 审核中
     */
    public static final String LOAN_ING = "1";
    /**
     * 已放款
     */
    public static final String LOAN_OK = "2";
    /**
     * 扣款中
     */
    public static final String LOAN_DEBIT = "3";

    /**
     * 征信步骤（1、征信账号登录页 2、身份验证码录入页 3、征信认证结果页）
     */
    /**
     * 征信账号登录页
     */
    public static final String CREDIT_LOGIN = "1";
    /**
     * 身份验证码录入页
     */
    public static final String CREDIT_VERIFY = "2";
    /**
     * 征信认证结果页
     */
    public static final String CREDIT_RESULT = "3";

    public static final String[] CREDIT_CONTENT = {Constants.CREDIT_VERIFY, Constants.CREDIT_LOGIN};

    /**
     * 获取手机验证码业务类型
     * 1、注册
     * 2、微信用户激活
     * 3、密码找回
     * 4、支付账单
     * 5、提现
     */
    public static final String PHONE_VERIFY_CODE_REGIST = "applyAmtMsg";
    public static final String PHONE_VERIFY_CODE_WECHAT_ACTIVE = "wechatRegister";
    public static final String PHONE_VERIFY_CODE_FIND_PWD = "findPassword";
    public static final String PHONE_VERIFY_CODE_PAY_BILL = "cleanloan";
    public static final String PHONE_VERIFY_CODE_WITHDRWA_CASH = "withdrawMsg";
    public static final int REFRESH_AUTH_STATE = 5000;
    /*验证码倒计时时间*/
    public static final int AUTH_COUTN_DOWN_TIME = 60 * 1000;

    /**
     * 身份证正面拍照（真人展示的requestCode）
     */
    public static final int SHOW_POSITIVE_MODEL_REQUESTCODE = 4001;
    /**
     * 身份证正面拍照返回(请求拍照的requestcode)
     */
    public static final int TAKE_POSITIVE_PHOTO_REQUESTCODE = 4011;


    /**
     * 手持拍照（真人展示的requestCode）
     */
    public static final int SHOW_HOLD_IDCARD_MODEL_REQUESTCODE = 4003;

    /**
     * 手持拍照(请求拍照的requestcode)
     */
    public static final int TAKE_HOLD_IDCARD_PHOTO_REQUESTCODE = 4013;

    /**
     * 征信回答问题requestCode
     */
    public static final int REQUESTCODE_CREDITAUTHQUESTION = 0x4014;

    /**
     * 用户类型
     */
    public static class UserKind {
        /**
         * 未知
         */
        public static String UNKNOEWN = "0";//未知
        /**
         * 学生
         */
        public static String STUDENT = "1";//学生
        /**
         * 白领
         */
        public static String DEGREE = "2";//白领
    }

    /**
     * 提额通道
     */
    public static class LLP {
        /**
         * 公积金/社保
         */
        public static String GJJSB = "1";//公积金/社保
    }

    /**
     * 提示级别
     */
    public static class DisplayLevel {
        /**
         * 强提示
         */
        public static String DAILOG = "1";
        /**
         * 弱提示
         */
        public static String TOAST = "2";
    }
}
