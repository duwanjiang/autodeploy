package com.csnt.deploy.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cloud on 2017/12/12.
 */
public class VersionUtil {

    /**
     * 增量版本格式
     */
    public static final String versionIncreFormat = "yyyyMMddHHmm";

    /**
     * 全量版本格式
     */
    public static final String versionFullFormat = "yyyyMMdd";
    /**
     * 小时版本格式
     */
    public static final String versionHourFormat = "yyyyMMddHH";
    /**
     * 全量版本格式
     */
    public static final String versionDateFormat = "yyyyMMddHHmmss";


    //分钟类型
    public static final Integer minuteTypeTwo = 20;
    public static final Integer minuteTypeOne = 15;


    /**
     * 获取当前的版本号
     *
     * @return
     */
    public static String getCurVersion(String versionType, Integer minuteType) {
        if (StringUtil.isEmpty(minuteType)) {
            minuteType = 0;
        }
        if (versionType.equals(versionFullFormat)) {
            if (minuteType == 0) {
                return getCurFullVersion();
            } else {
                //修改minuteType 在日版本下修改minuteType代表版本的偏移量
                return getCurFullVersion(minuteType);
            }
        } else if (versionType.equals(versionHourFormat)) {
            return getHourVersion(new Date());
        } else if (versionType.equals(versionIncreFormat) && minuteType.equals(minuteTypeTwo)) {
            return getIncreVersionTwo(new Date());
        } else {
            return getIncreVersion(new Date());
        }
    }

    /**
     * 获取指定时间的不同版本
     *
     * @return
     */
    public static String getVersionByType(Date date, String versionType, Integer minuteType) {
        if (StringUtil.isEmpty(minuteType)) {
            minuteType = 0;
        }
        if (versionType.equals(versionFullFormat)) {
            return getFullVersion(date);
        } else if (versionType.equals(versionHourFormat)) {
            return getHourVersion(date);
        } else if (versionType.equals(versionIncreFormat) && minuteType.equals(minuteTypeTwo)) {
            return getIncreVersionTwo(date);
        } else {
            return getIncreVersion(date);
        }
    }

    /**
     * 获取下一个的版本号
     *
     * @return
     */
    public static String preVersion(String version, String versionType, Integer minuteType) {
        if (StringUtil.isEmpty(minuteType)) {
            minuteType = 0;
        }
        if (versionFullFormat.equals(versionType)) {
            return preFullVersion(version);
        } else if (versionHourFormat.equals(versionType)) {
            return preHourVersion(version);
        } else if (versionType.equals(versionIncreFormat) && minuteType.equals(minuteTypeTwo)) {
            return preIncreVersionTwo(version);
        } else {
            return preIncreVersion(version);
        }
    }

    /**
     * 获取下一个的版本号
     *
     * @return
     */
    public static String nextVersion(String version, String versionType, Integer minuteType) {
        if (StringUtil.isEmpty(minuteType)) {
            minuteType = 0;
        }
        if (versionType.equals(versionFullFormat)) {
            return nextFullVersion(version);
        } else if (versionType.equals(versionHourFormat)) {
            return nextHourVersion(version);
        } else if (versionType.equals(versionIncreFormat) && minuteType.equals(minuteTypeTwo)) {
            return nextIncreVersionTwo(version);
        } else {
            return nextIncreVersion(version);
        }
    }

    /**
     * 获取指定版本的下一个全量版本号
     *
     * @param version 版本号
     * @return
     */
    private static String nextFullVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionFullFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return UtilDate.formatDate(calendar.getTime(), versionFullFormat);
    }

    /**
     * 获取指定版本的下一个小时版本号
     *
     * @param version 版本号
     * @return
     */
    private static String nextHourVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionHourFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return UtilDate.formatDate(calendar.getTime(), versionHourFormat);
    }

    /**
     * 获取当前的增量版本号
     *
     * @return
     */
    public static String getCurIncreVersion() {
        return getIncreVersion(new Date());
    }

    /**
     * 将增量版本号转化为全量版本号
     *
     * @return
     */
    public static String changeIncre2FullVersion(String increVersion) {
        return increVersion.substring(0, 8);
    }

    /**
     * 获取指定版本的下一个增量版本号(间隔15分钟)
     *
     * @param version 版本号
     * @return
     */
    public static String nextIncreVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionIncreFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.MINUTE, 5);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * 获取指定版本的下一个增量版本号(间隔20分钟)
     *
     * @param version 版本号
     * @return
     */
    public static String nextIncreVersionTwo(String version) {
        Date versionDate = UtilDate.parseDate(version, versionIncreFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.MINUTE, 20);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * 获取指定版本的上一个增量版本号(间隔15分钟)
     *
     * @param version 版本号
     * @return
     */
    public static String preIncreVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionIncreFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.MINUTE, -15);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * 获取指定版本的上一个增量版本号(间隔20分钟)
     *
     * @param version 版本号
     * @return
     */
    public static String preIncreVersionTwo(String version) {
        Date versionDate = UtilDate.parseDate(version, versionIncreFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.MINUTE, -20);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * 获取指定版本的前一个全量版本号
     *
     * @param version 版本号
     * @return
     */
    public static String preFullVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionFullFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return UtilDate.formatDate(calendar.getTime(), versionFullFormat);
    }

    /**
     * 获取指定版本的前一个小时版本号
     *
     * @param version 版本号
     * @return
     */
    public static String preHourVersion(String version) {
        Date versionDate = UtilDate.parseDate(version, versionHourFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(versionDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        return UtilDate.formatDate(calendar.getTime(), versionHourFormat);
    }

    /**
     * 获取当天第一个版本
     *
     * @return
     */
    public static String getTodayFirstVersion() {
        return getFirstVersion(getCurFullVersion());
    }

    /**
     * 获取指定版本当天的第一个版本
     *
     * @return
     */
    public static String getFirstVersion(String version) {
        return changeIncre2FullVersion(version) + "0015";
    }

    /**
     * 获取指定版本当天的第一个版本
     *
     * @return
     */
    public static String getFirstVersion(String version, String versionType, Integer minuteType) {
        if (StringUtil.isEmpty(minuteType)) {
            minuteType = 0;
        }
        if (versionFullFormat.equals(versionType)) {
            return changeIncre2FullVersion(version);
        } else if (versionHourFormat.equals(versionType)) {
            return changeIncre2FullVersion(version) + "00";
        } else if (versionType.equals(versionIncreFormat) && minuteType.equals(minuteTypeTwo)) {
            return changeIncre2FullVersion(version) + "0020";
        } else {
            return changeIncre2FullVersion(version) + "0005";
        }
    }

    /**
     * 获取增量的版本的最后4位
     *
     * @return
     */
    public static String getIncreVersionEnd4str(String version) {
        return version.substring(8, version.length());
    }

    /**
     * 获取指定时间的增量版本号(间隔5分钟)
     *
     * @return
     */
    /**
     * 获取指定时间的增量版本号
     *
     * @return
     */
    public static String getIncreVersion(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //分钟
        int minute = getIncreaseMinute(calendar.get(Calendar.MINUTE));

        calendar.set(Calendar.MINUTE, minute);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * kyq：交公路函2019-387号文调整，根据时间或者获取增量版本号时间，5分钟一个增量版本
     *
     * @datetime 2019-07-22
     */
    private static int getIncreaseMinute(int minute) {
        int divide = minute / 5;
        return divide * 5;
    }

    /**
     * 获取指定时间的增量版本号(间隔20分钟)
     *
     * @return
     */
    public static String getIncreVersionTwo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        if (minute >= 0 && minute < 20) {
            minute = 0;
        } else if (minute >= 20 && minute < 40) {
            minute = 20;
        } else if (minute >= 40 && minute <= 59) {
            minute = 40;
        }
        calendar.set(Calendar.MINUTE, minute);
        return UtilDate.formatDate(calendar.getTime(), versionIncreFormat);
    }

    /**
     * 获取指定时间的小时版本号
     *
     * @return
     */
    public static String getHourVersion(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //小时
        int hour = calendar.get(Calendar.HOUR);
        calendar.set(Calendar.HOUR, hour);
        return UtilDate.formatDate(calendar.getTime(), versionHourFormat);
    }

    /**
     * 获取当前的全量版本号
     *
     * @return
     */
    public static String getCurFullVersion() {
        return getFullVersion(new Date());
    }

    /**
     * 获取当前的全量版本号
     *
     * @return minuteType 偏移天数
     */
    public static String getCurFullVersion(Integer minuteType) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, minuteType);
        return getFullVersion(c.getTime());
    }

    /**
     * 获取指定时间的全量版本
     *
     * @param date
     * @return
     */
    public static String getFullVersion(Date date) {
        return UtilDate.formatDate(date, versionFullFormat);
    }


    /**
     * 获取两个增量版本间的相差增量个数
     *
     * @param startVersion
     * @param endVersion
     * @return
     */
    public static long getDiffIncreVersion(String startVersion, String endVersion) {
        Long startVersionLong = UtilDate.parseDate(startVersion, versionIncreFormat).getTime();
        Long endVersionLong = UtilDate.parseDate(endVersion, versionIncreFormat).getTime();
        Long diff = endVersionLong - startVersionLong;
        long count = diff / (1000 * 60 * 15);
        return count;
    }

    /**
     * 将版本号不全位数为监控日期
     *
     * @param version
     * @return
     */
    public static String changeVersionToMonitorDate(String version) {
        for (int i = version.length(); i < 17; i++) {
            version += "0";
        }
        return version;
    }


}
