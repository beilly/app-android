package com.vcredit.global;


import com.vcredit.app.BuildConfig;

/**
 * Created by wangzhengji on 2016/3/22.
 */
public class InterfaceConfig {

    /**
     * 请求超时时间
     */
    public static final int SOCKET_TIMEOUT = 60 * 1000;
    /**
     * 最大重新请求次数
     */
    public static final int MAX_RETRIES = 0;
    /**
     * 重新请求权重
     */
    public static final float BACK_OFF = 1.0f;
    /**
     * 字符编码
     */
    public static final String ENCODEING = "UTF-8";


    /**
     * 请求的域名
     */
    public static final String BASE_URL = BuildConfig.BASE_URL;

    /**
     * 带版本的请求的域名
     */
    public static final String BASE_URL_V =  BASE_URL + BuildConfig.BASE_SERVER;


    /** web接口地址 */
    /**
     * 服务器地址
     */
    public static final String SERVER_URL = BASE_URL_V + "/data/ws/rest/";
    /**
     * 用户登陆
     **/
    public static final String LOGIN = "user/login";
    /**
     * 刷新账单数据
     **/
    public static final String SYSCONIZE_INFO = "user/sysconizeInfo";
    /**
     * 发送验证码
     */
    public static final String SEND_VERIFY_CODE = "common/random/send";
    /**
     * 普通用户注册
     */
    public static final String REGISTER = "user/register";
    /**
     * 重置密码
     */
    public static final String RESETPWD = "user/resetPwd";
    /**
     * 手机号校验
     */
    public static final String CHECKMOBILENO = "user/checkMobileNo";
    /**
     * 初始化申请步骤
     */
    public static final String INITAPPLYSTEP = "appCustomer/InitApplyStep";
    /**
     * 获得资源最新版本
     */
    public static final String GETRESOURCEVER = "version/getResourceVer";
    /**
     * 获得最新安装包信息
     */
    public static final String GETLASTVERSIONS = "version/getLastVersion";
    /**
     * 获得系统枚举
     */
    public static final String GETAPPENUMS = "version/getAppEnums";

}
