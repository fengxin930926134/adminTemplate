package com.fengx.template.utils.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 日期工具类
 * 使用： LocalDateTime
 */
public class DateUtils {

    public static final String DEF_FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DEF_FMT_DATE = "yyyy-MM-dd";
    private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();

    //================================== 计时 单位统一毫秒 =====================================

    /**
     * 开始计时
     */
    public static void startTime() {
        TIMER.set(nowLog());
    }

    /**
     * 结束计时
     */
    public static long endTime() {
        if (TIMER.get() != null) {
            long time = nowLog() - TIMER.get();
            TIMER.remove();
            return time;
        }
        return 0;
    }

    //================================== 获取时间 =========================================

    /**
     * 获取现在时间, 时间戳
     */
    public static long nowLog() {
        return System.currentTimeMillis();
    }

    /**
     * 获取短格式yyyy-MM-dd的String时间
     * @return String
     */
    public static String nowShortStr() {
        return format(LocalDateTime.now(), DEF_FMT_DATE);
    }

    /**
     * 获取短时间String
     * yyyy-MM-dd
     * @param dateTime 时间
     * @return String
     */
    public static String shortStr(LocalDateTime dateTime) {
        return format(dateTime, DEF_FMT_DATE);
    }

    /**
     * 短时间获取LocalDateTime
     * @param strDate String yyyy-MM-dd
     * @return LocalDateTime
     */
    public static LocalDateTime shortDate(String strDate) {
        return format(strDate, DEF_FMT_DATE);
    }

    //================================== 格式化 =========================================

    /**
     * 格式转换
     * @param dateTime 时间
     * @param pattern 格式
     * @return String
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(dateTime);
    }

    /**
     * 格式转换
     * @param strDate 时间
     * @param pattern 格式
     * @return String
     */
    public static LocalDateTime format(String strDate, String pattern) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(pattern).parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1).parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter();
        return LocalDateTime.parse(strDate, formatter);
    }
}