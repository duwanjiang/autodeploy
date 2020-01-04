package com.csnt.deploy.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by duwanjiang on 2017/12/21.
 */
public class StringUtil {

    public static Charset CHARSET_UTF8 = Charset.forName("utf-8");
    public static Charset CHARSET_GBK = Charset.forName("gbk");
    /**
     * 空对象判断
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null || "".equals(obj.toString().trim())) {
            return true;
        }
        return false;
    }

    /**
     * 非空对象判断
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断是否包含元素
     *
     * @param array
     * @param str
     * @return
     */
    public static boolean isContained(String[] array, String str) {
        if (isEmpty(str)) {
            return false;
        }
        if (array == null) {
            return false;
        }
        for (String temp : array) {
            if (temp.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串转码指定类型
     *
     * @param str
     * @param encodeType 编码类型
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodingStr(String str, String encodeType) throws UnsupportedEncodingException {
        return new String(str.getBytes(), Charset.forName(encodeType));
    }

    /**
     * 将字符串转码为utf-8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodingUTF8(String str) throws UnsupportedEncodingException {
        return encodingStr(str, "utf-8");
    }

    /**
     * 将字符串转码为utf-8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodingGBK(String str) throws UnsupportedEncodingException {
        return encodingStr(str, "gbk");
    }

    /**
     * 根据分隔符合并List到字符串
     */
    public static String joinForSqlIn(List lstInput, String delim) {
        if (lstInput == null || lstInput.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (Iterator it = lstInput.iterator(); it.hasNext(); ) {
            sb.append("'").append(replaceNull(it.next())).append("'");

            if (it.hasNext()) {
                sb.append(delim);
            }
        }

        return sb.toString();
    }

    /**
     * 重构字符串，如果为null，返回空串
     */
    public static String replaceNull(Object obj) {
        if (isEmpty(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 判断文件路径的斜杠是否是Java的分隔符
     *
     * @param filePath
     * @return
     */
    public static boolean isBackslash(String filePath) {
        if (StringUtil.isNotEmpty(filePath) && filePath.lastIndexOf("\\") > -1) {
            return true;
        }
        return false;
    }


    /**
     * 生成uuid
     *
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 正则表达式匹配两个指定字符串中间的内容
     */
    public static List<String> analizeResMsg(String soap, String rgex) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    /**
     * 通过包名取时间字符串
     *
     * @param packageName
     * @return
     */
    public static String getDateByPackageName(String packageName) {
        if (isEmpty(packageName)) {
            return "";
        }
        String[] packageNameArr = packageName.split("_");
        return packageNameArr[packageNameArr.length - 1]
                .replace(".json", "")
                .replace(".zip", "");
    }

    /**
     * 通过json文件名获取id(120301_130301_1_201810130915.json -> 1)
     * @param jsonFileName
     * @return
     */
    public static Long getIdByJsonFileName(String jsonFileName) {
        if (isEmpty(jsonFileName)) {
            return null;
        }
        String[] fileArr = jsonFileName.split("_");
        return Long.valueOf(fileArr[2]);
    }
}
