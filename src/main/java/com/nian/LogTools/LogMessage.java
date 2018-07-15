package com.nian.LogTools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* @author created by NianTianlei
* @createDate on 2018年7月15日 下午6:40:01
*/

public class LogMessage {
    public static String HEADER_TRACEID = "niantianlei-header-rid";
    public static String HEADER_SPANID = "niantianlei-header-spanid";
    private static String DEFAULT_DLTAG = "_undef";
    private static ThreadLocal<ExtElement> extElements = new InheritableThreadLocal<ExtElement>() {
        protected ExtElement initialValue() {
            return new ExtElement();
        }
    };
    private String dltag = "";
    private String cspanId = "";
    private List<LogKV> logElements = new ArrayList();

    public LogMessage() {
    }

    public static String getTraceId() {
        return ((ExtElement)extElements.get()).getTraceId();
    }

    public static String getSpanId() {
        return ((ExtElement)extElements.get()).getSpanId();
    }

    public static void setTraceId(String traceId) {
        ((ExtElement)extElements.get()).setTraceId(traceId);
    }

    public static void setSpanId(String spanId) {
        ((ExtElement)extElements.get()).setSpanId(spanId);
    }

    public static String generatorNewSpanid() {
        String newSpanid = SpanIdGenerator.getSpanId();
        return newSpanid;
    }

    public static String genertorNewTraceid() {
        String traceid = SpanIdGenerator.getTraceId();
        return traceid;
    }

    public LogMessage setCspanId(String cspanId) {
        this.cspanId = cspanId;
        return this;
    }

    public LogMessage setDltag(String dltag) {
        this.dltag = dltag;
        return this;
    }

    public LogMessage add(String key, Object value) {
        LogKV logKV = new LogKV(key, value);
        this.logElements.add(logKV);
        return this;
    }

    public List<LogKV> getLogElements() {
        return this.logElements;
    }

    public void setLogElements(List<LogKV> logElements) {
        this.logElements = logElements;
    }

    public String getDltag() {
        return this.dltag != null && !this.dltag.equals("") ? this.dltag : DEFAULT_DLTAG;
    }

    public String getCspanId() {
        return this.cspanId;
    }

    public static void remove() {
        extElements.remove();
    }

    public String toString() {
        ExtElement extElement = (ExtElement)extElements.get();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getDltag());
        sb.append("||traceid=");
        sb.append(extElement.getTraceId());
        sb.append("||spanid=");
        sb.append(extElement.getSpanId());
        sb.append("||cspanid=");
        sb.append(this.getCspanId());
        if (this.logElements.size() > 0) {
            Iterator var4 = this.logElements.iterator();

            while(var4.hasNext()) {
                LogKV param = (LogKV)var4.next();
                sb.append("||");
                sb.append(param.getKey());
                sb.append("=");
                sb.append(param.getValue());
            }
        }

        return sb.toString();
    }
}
