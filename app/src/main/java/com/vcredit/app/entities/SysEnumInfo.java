package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统枚举
 * Created by zhuofeng on 2015/8/24.
 */
@Setter
@Getter
@ToString
public class SysEnumInfo implements Serializable {
    /*
        versionCodeEnum	枚举最新版本号（初始版本 1）
        "eSchool（学校枚举）"
        "eSchoolRoll （学籍层次）"
        "eEducationCargo（学历类别）"
        "eAgeLimit（年限）"
        "eMarriage（银行）"
        "eBank（婚姻状况字典）"
        "eEducation（学历字典）"
        eDegree	object	学历贷学历字典
*/
    @Expose
    private int versionCodeEnum;
    @Expose
    private List<KeyValueEntity> eSchool;
    @Expose
    private List<KeyValueEntity> eSchoolRoll;
    @Expose
    private List<KeyValueEntity> eEducationCargo;
    @Expose
    private List<KeyValueEntity> eAgeLimit;
    @Expose
    private List<KeyValueEntity> eBank;
    @Expose
    private List<KeyValueEntity> eMarriage;
    @Expose
    private List<KeyValueEntity> eDegree;


}
