package com.oneinstep.light.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 */
@UtilityClass
@Slf4j
public class DateTimeUtil {

    /**
     * 日期格式 yyyyMMdd
     */
    public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault());

    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    /**
     * 获取当前时间
     *
     * @return yyyyMMdd
     */
    public static int today() {
        String currentDateStr = ZonedDateTime.now(ZoneId.systemDefault()).format(YYYY_MM_DD);
        return Integer.parseInt(currentDateStr);
    }

    /**
     * 获取明天时间
     *
     * @return 明天时间 yyyyMMdd
     */
    public static int tomorrow() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        String tomorrowDateStr = now.plusDays(1).format(YYYY_MM_DD);
        return Integer.parseInt(tomorrowDateStr);
    }

    /**
     * 格式化时间 ZonedDateTime 为 {yyyyMMdd} （int）
     *
     * @return yyyyMMdd
     */
    public static String formatDateTime(ZonedDateTime dateTime) {
        return dateTime.format(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将字符串转换为 ZonedDateTime，字符串格式需要为 yyyy-MM-dd HH:mm:ss
     *
     * @param timeStr 时间字符串
     * @return ZonedDateTime
     */
    public static ZonedDateTime convertStrToZonedDateTime(@NotNull String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        try {
            return ZonedDateTime.parse(timeStr, YYYY_MM_DD_HH_MM_SS);
        } catch (Exception e) {
            log.error("Convert timeStr to ZonedDateTime error. Maybe the format of timeStr is invalid. Make sure it's yyyy-MM-dd HH:mm:ss.", e);
        }
        return null;
    }

    /**
     * 获取某个日期（int）加上{days}天的日期
     *
     * @return yyyyMMdd
     */
    public static Integer plusDays(Integer date, int days) {
        LocalDate dateTime = LocalDate.parse(String.valueOf(date), YYYY_MM_DD);
        String tomorrowDateStr = dateTime.plusDays(days).format(YYYY_MM_DD);
        return Integer.parseInt(tomorrowDateStr);
    }

    /**
     * 获取某个日期（int）减去{days}天的日期
     *
     * @return yyyyMMdd
     */
    public static Integer minusDays(Integer date, int days) {
        LocalDate dateTime = LocalDate.parse(String.valueOf(date), YYYY_MM_DD);
        String tomorrowDateStr = dateTime.minusDays(days).format(YYYY_MM_DD);
        return Integer.parseInt(tomorrowDateStr);
    }

}
