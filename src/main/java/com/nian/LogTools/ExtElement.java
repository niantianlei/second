package com.nian.LogTools;
/**
* @author created by NianTianlei
* @createDate on 2018年7月15日 下午6:43:28
*/
public class ExtElement {
    private String traceId = "";
    private String spanId = "";

    public ExtElement() {
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        if (this.spanId == null || this.spanId.equals("")) {
            this.spanId = SpanIdGenerator.getSpanId();
        }

        return this.spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }
}
