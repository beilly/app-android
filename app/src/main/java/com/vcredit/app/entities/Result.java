package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shibenli on 2016/6/16.
 */
@Setter
@Getter
@ToString(callSuper = true)
public class Result extends ResultInfo implements Serializable {

    @Expose
    protected String operationStatus;
}
