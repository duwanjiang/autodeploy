package com.csnt.deploy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * SimpleDateFormat 以及 Calendar 存在线程不安全问题。
 * 以后追求高性能,可以把这个地方改为threadlocal,线程独占形式
 *
 * @author Marshall
 */
public class UtilDate {

    /*时间日期相关*/
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyyMMddHH = "yyyyMMddHH";
    public static final String yyyy_MM_dd_path = "yyyy/MM/dd";
    public static final String yyyy_MM_ddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static final String yyyy_MM_ddTHHmmss = "yyyy-MM-dd'T'HH:mm:ss";

    private static SimpleDateFormat PARSE_yyyy_MM = new SimpleDateFormat(yyyy_MM);
    private static SimpleDateFormat FORMAT_yyyy_MM = new SimpleDateFormat(yyyy_MM);
    private static SimpleDateFormat FORMAT_yyyy_MM_dd = new SimpleDateFormat(yyyy_MM_dd);
    private static SimpleDateFormat PARSE_yyyy_MM_dd = new SimpleDateFormat(yyyy_MM_dd);
    private static SimpleDateFormat FORMAT_yyyyMMdd = new SimpleDateFormat(yyyyMMdd);
    private static SimpleDateFormat FORMAT_yyyyMMddHH = new SimpleDateFormat(yyyyMMddHH);
    private static SimpleDateFormat PARSE_yyyyMMdd = new SimpleDateFormat(yyyyMMdd);
    private static SimpleDateFormat PARSE_yyyyMMddHH = new SimpleDateFormat(yyyyMMddHH);
    private static SimpleDateFormat FORMAT_yyyy_MM_dd_path = new SimpleDateFormat(yyyy_MM_dd_path);
    private static SimpleDateFormat PARSE_DEFAULT = new SimpleDateFormat(yyyy_MM_ddHHmmss);
    private static SimpleDateFormat FORMAT_DEFAULT = new SimpleDateFormat(yyyy_MM_ddHHmmss);

    private static SimpleDateFormat FORMAT_yyyyMMddHHmmss = new SimpleDateFormat(yyyyMMddHHmmss);
    private static SimpleDateFormat PARSE_yyyyMMddHHmmss = new SimpleDateFormat(yyyyMMddHHmmss);

    private static SimpleDateFormat FORMAT_yyyy_MM_ddTHHmmss = new SimpleDateFormat(yyyy_MM_ddTHHmmss);
    private static SimpleDateFormat PARSE_yyyy_MM_ddTHHmmss = new SimpleDateFormat(yyyy_MM_ddTHHmmss);
    private static SimpleDateFormat FORMAT_yyyyMMddHHmmssSSS = new SimpleDateFormat(yyyyMMddHHmmssSSS);
    private static SimpleDateFormat PARSE_yyyyMMddHHmmssSSS = new SimpleDateFormat(yyyyMMddHHmmssSSS);

    /*
     * public static ThreadLocal<DateFormat> threadLocal_yyyy_MM = new
     * ThreadLocal<DateFormat>() {
     *
     * @Override protected synchronized DateFormat initialValue() { return new
     * SimpleDateFormat("yyyy-MM"); } };
     *
     * public static ThreadLocal<DateFormat> threadLocal_Default = new
     * ThreadLocal<DateFormat>() {
     *
     * @Override protected synchronized DateFormat initialValue() { return new
     * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); } };
     */


    /**
     * default 默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public synchronized static String format_Default(Date date) {
        return FORMAT_DEFAULT.format(date);
    }

    public synchronized static String format_yyyy_MM_ddTHHmmss(Date date) {
        return FORMAT_yyyy_MM_ddTHHmmss.format(date);
    }

    public synchronized static String format_yyyyMMddHHmmssSSS(Date date) {
        return FORMAT_yyyyMMddHHmmssSSS.format(date);
    }

    public synchronized static String format_yyyyMMdd(Date date) {
        return FORMAT_yyyyMMdd.format(date);
    }

    public synchronized static String format_yyyyMMddHH(Date date) {
        return FORMAT_yyyyMMddHH.format(date);
    }

    public synchronized static String format_yyyy_MM(Date date) {
        return FORMAT_yyyy_MM.format(date);
    }

    public synchronized static String format_yyyy_MM_dd(Date date) {
        return FORMAT_yyyy_MM_dd.format(date);
    }

    public synchronized static String format_yyyy_MM_dd_path(Date date) {
        return FORMAT_yyyy_MM_dd_path.format(date);
    }

    public synchronized static String format_yyyyMMddHHmmss(Date date) {
        return FORMAT_yyyyMMddHHmmss.format(date);
    }


    /**
     * default 默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public synchronized static Date parse_Default(String str) throws ParseException {
        return PARSE_DEFAULT.parse(str);
    }

    public synchronized static Date parse_yyyy_MM(String str)
            throws ParseException {
        return PARSE_yyyy_MM.parse(str);
    }

    public synchronized static Date parse_yyyy_MM_dd(String date) throws ParseException {
        return PARSE_yyyy_MM_dd.parse(date);
    }

    public synchronized static Date parse_yyyyMMdd(String date) throws ParseException {
        return PARSE_yyyyMMdd.parse(date);
    }

    public synchronized static Date parse_yyyyMMddHH(String date) throws ParseException {
        return PARSE_yyyyMMddHH.parse(date);
    }

    public synchronized static Date parse_yyyyMMddHHmmss(String date) throws ParseException {
        return PARSE_yyyyMMddHHmmss.parse(date);
    }

    public synchronized static Date parse_yyyyMMddHHmmssSSS(String date) throws ParseException {
        return PARSE_yyyyMMddHHmmssSSS.parse(date);
    }

    public synchronized static Date parse_yyyy_MM_ddTHHmmss(String date) throws ParseException {
        return PARSE_yyyy_MM_ddTHHmmss.parse(date);
    }


    public synchronized static String getCurrent_yyyyMMddHHmmss() {
        return format_yyyyMMddHHmmss(new Date());
    }

    public synchronized static String getCurrent_yyyyMMddHHmmssSSS() {
        return format_yyyyMMddHHmmssSSS(new Date());
    }

    public synchronized static String getCurrent_yyyy_MM() {
        return format_yyyy_MM(new Date());
    }

    public synchronized static String getLastMonth_yyyy_MM() {
        return format_yyyy_MM(getLastMonth());
    }

    /**
     * 由于Calendar 不是线程安全的 有可能导致得到时间错误 加上 线程安全锁 获取系统时间的上月
     */
    public synchronized static Date getLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取时间差
     *
     * @param startTimeStamp 开始时间戳
     * @param endTimeStamp   结束时间戳
     * @return
     */
    public static String diffTime(long startTimeStamp, long endTimeStamp) {
        String result = "";
        long diff = endTimeStamp - startTimeStamp;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = diff / (1000 * 60 * 60) - days * 24;
        long mins = diff / (1000 * 60) - hours * 60;
        long seconds = diff / (1000) - mins * 60;
        if (days > 0) {
            result += days + "天";
        }
        if (hours > 0) {
            result += hours + "小时";
        }
        if (mins > 0) {
            result += mins + "分";
        }
        if (seconds > 0) {
            result += seconds + "秒";
        }
        if (StringUtil.isEmpty(result)) {
            result = diff + "毫秒";
        }
        return result;
    }

    /**
     * 根据不同的时间格式获取日期
     *
     * @param str
     * @return
     */
    public static Date checkAndGetDate(String str) {
        Date re;
        try {
            if ((re = parse_yyyy_MM_ddTHHmmss(str)) != null) {
                return re;
            }
            if ((re = parse_yyyy_MM_dd(str)) != null) {
                return re;
            }
            if ((re = parse_Default(str)) != null) {
                return re;
            }
            if ((re = parse_yyyyMMdd(str)) != null) {
                return re;
            }
            if ((re = parse_yyyyMMddHHmmssSSS(str)) != null) {
                return re;
            }
        } catch (ParseException e) {
            return null;
        }
        return null;
    }

    /**
     * 格式化日期对象
     *
     * @param date   日期对象
     * @param format 格式化字符串
     * @return
     */
    public static Date parseDate(String date, String format) {
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null && !("".equals(date))) {
            try {
                result = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 格式化日期对象
     *
     * @param date   日期对象
     * @param format 格式化字符串
     * @return
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            return null;
//            result = sdf.format(new Date())
        } else {
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * 增加几天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

}
