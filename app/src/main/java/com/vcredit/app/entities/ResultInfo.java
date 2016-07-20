package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zhuofeng on 2015/8/18.
 */
@Setter
@Getter
@ToString
public class ResultInfo implements Serializable{

    /**
     * displayInfo : 最强王者
     * operationResult : true
     * displayLevel :"2" 	提示级别(1、强提示 2、弱提示)
     */
    @Expose
    protected String displayInfo;
    @Expose
    protected String displayLevel;
    @Expose
    protected boolean operationResult;
}
