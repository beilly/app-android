package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by chenlei on 2016/3/25.
 */
@Setter
@Getter
@Accessors(chain = true)
public class KeyValueEntity implements Serializable {

    @Expose
    private String value;
    @Expose
    private String key;

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValueEntity that = (KeyValueEntity) o;
        return key.equals(that.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}

