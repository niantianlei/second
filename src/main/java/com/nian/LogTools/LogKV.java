package com.nian.LogTools;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
* @author created by NianTianlei
* @createDate on 2018年7月15日 下午6:41:01
*/
public class LogKV {
    private String key;
    private String value;

    public LogKV(String key, Object value) {
        this.key = key;
        if (value == null) {
            value = "";
        }

        if (!(value instanceof List) && !(value instanceof Map)) {
            this.value = value.toString();
        } else {
            this.value = JSON.toJSONString(value);
        }

    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
