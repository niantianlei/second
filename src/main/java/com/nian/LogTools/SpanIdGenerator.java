package com.nian.LogTools;
/**
* @author created by NianTianlei
* @createDate on 2018年7月15日 下午6:44:59
*/
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SpanIdGenerator {
    private static final long ipFactor = getIpFactor();
    private static AtomicLong counter = new AtomicLong();

    static {
        counter.set(0L);
    }

    public SpanIdGenerator() {
    }

    private static long ip2long(String ip) {
        int ret = 0;
        String[] ipGroups = ip.split("\\.");
        Integer[] ipInt = new Integer[4];

        for(int i = 0; i < ipGroups.length; ++i) {
            ipInt[i] = Integer.valueOf(ipGroups[i]);
        }

        ret = ipInt[0] << 24 | ipInt[1] << 16 | ipInt[2] << 8 | ipInt[3];
        return (long)ret;
    }

    private static long timeFactor() {
        Date data = new Date();
        long msec = data.getTime();
        return msec;
    }

    private static long timeFactorInSecond() {
        return timeFactor() / 1000L;
    }

    private static long randFactor() {
        long ret = (long)(Math.random() * Math.pow(2.0D, 32.0D));
        return ret;
    }

    private static List<String> getLocalIpList() {
        ArrayList ipList = new ArrayList();

        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
                Enumeration inetAddresses = networkInterface.getInetAddresses();

                while(inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException var6) {
            ;
        }

        return ipList;
    }

    private static long getIpFactor() {
        List<String> ips = getLocalIpList();
        String ipAddr = "127.0.0.1";
        if (ips != null && ips.size() != 0) {
            Iterator var3 = ips.iterator();

            while(var3.hasNext()) {
                String iptmp = (String)var3.next();
                if (!iptmp.equals("127.0.0.1")) {
                    ipAddr = iptmp;
                    break;
                }
            }

            return ip2long(ipAddr);
        } else {
            return ip2long(ipAddr);
        }
    }

    static String getSpanId() {
        long timeFactor = timeFactor();
        long randFactor = randFactor();
        long spanId = (ipFactor & timeFactor) << 32 | randFactor & -1L;
        return handleFormat(Long.toHexString(spanId), 16);
    }

    static String getTraceId() {
        long timeFactor = timeFactorInSecond();
        long randFactor1 = randFactor();
        long randFactor2 = counter.incrementAndGet();
        long traceIdpart1 = ipFactor << 32 | timeFactor & -1L;
        long traceIdpart2 = randFactor1 << 32 | (randFactor2 & 16777215L) << 8 | 134L;
        return handleFormat(Long.toHexString(traceIdpart1), 16) + handleFormat(Long.toHexString(traceIdpart2), 16);
    }

    private static String handleFormat(String value, int digit) {
        if (value == null) {
            return "00000000";
        } else {
            while(value.length() < digit) {
                value = "0" + value;
            }

            return value;
        }
    }
}
