package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shibenli on 2016/7/15.
 * 信用信息
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CreditInfo implements Serializable {
    /**
     * {
     "limit": 611,
     "score": 20000,
     "evaluation": "信用良好",
     "status": "1"
     }
     */

    @Expose
    protected int limit;

    @Expose
    protected int score;

    @Expose
    protected String evaluation;

    @Expose
    protected String status;
}
