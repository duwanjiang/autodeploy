package com.csnt.deploy.util;

import java.math.BigDecimal;

/**
 * Description:
 * Copyright: © 2018 CSNT. All rights reserved.
 * Company: CSTC
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2018/9/30
 */
public class MathUtil {

    /**
     * 字符串转Number类型
     */
    public static Object asNumber(Object object, Class dstClass) {
        Object retObj = object;

        if (!object.getClass().equals(dstClass)) {
            String valStr = object.toString();
            if (Integer.class.equals(dstClass) || int.class.equals(dstClass)) {
                if ("true".equalsIgnoreCase(valStr)) {
                    retObj = 1;
                } else if ("false".equalsIgnoreCase(valStr)) {
                    retObj = 0;
                } else {
                    retObj = StringUtil.isNotEmpty(valStr) ? Integer.valueOf(valStr) : null;
                }
            } else if (Long.class.equals(dstClass) || long.class.equals(dstClass)) {
                if ("true".equalsIgnoreCase(valStr)) {
                    retObj = 1L;
                } else if ("false".equalsIgnoreCase(valStr)) {
                    retObj = 0L;
                } else {
                    retObj = StringUtil.isNotEmpty(valStr) ? Long.valueOf(valStr) : null;
                }
            } else if (Double.class.equals(dstClass)) {
                retObj = StringUtil.isNotEmpty(valStr) ? Double.valueOf(valStr) : null;
            } else if (Float.class.equals(dstClass)) {
                retObj = StringUtil.isNotEmpty(valStr) ? Float.valueOf(valStr) : null;
            } else if (BigDecimal.class.equals(dstClass)) {
                retObj = StringUtil.isNotEmpty(valStr) ? BigDecimal.valueOf(Double.valueOf(valStr)) : null;
            }
        }
        return retObj;
    }

    /**
     * 字符串转Integer类型
     */
    public static Integer asInteger(Object object) {
        if (object == null) {
            return null;
        }
        return (Integer) asNumber(object, Integer.class);
    }

    /**
     * 字符串转Long类型
     */
    public static Long asLong(Object object) {
        if (object == null) {
            return null;
        }
        return (Long) asNumber(object, Long.class);
    }

    /**
     * Integer[]数组求和
     */
    public static int sumResultCount(Integer[] result) {
        int resultSum = 0;
        for (int temp : result) {
            resultSum += temp;
        }
        return resultSum;
    }

    /**
     * int[]数组求和
     */
    public static int sumResultCount(int[] result) {
        int resultSum = 0;
        for (int temp : result) {
            resultSum += temp;
        }
        return resultSum;
    }
}
