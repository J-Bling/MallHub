package com.mall.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private static boolean isInvalid(String ipAddress){
        return ipAddress==null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress);
    }

    public static String getRequestIp(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(isInvalid(ipAddress)){
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(isInvalid(ipAddress)){
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalid(ipAddress)){
            ipAddress=request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                try{
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                }catch (UnknownHostException unknownHostException){
                    logger.error("{}",unknownHostException.getMessage(),unknownHostException);
                }
            }
        }
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
