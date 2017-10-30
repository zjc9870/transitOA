package com.expect.admin.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeUtil {

	public final static String shortFormat = "yyyyMMdd";
	public final static String longFormat = "yyyyMMddHHmmss";
	public final static String webFormat = "yyyy-MM-dd";
	public final static String timeFormat = "HHmmss";
	public final static String monthFormat = "yyyyMM";
	public final static String chineseDtFormat = "yyyy年MM月dd日";
	public final static String fullFormat = "yyyy-MM-dd HH:mm:ss";
	public final static String newFormat = "yyyy-MM-dd";
	public final static String zbFormat = "yyyy/MM/dd";
	public final static String tsfxFormat = "yyyy.MM.dd";
	public final static String noSecondFormat = "yyyy-MM-dd HH:mm";

	public final static long ONE_DAY_MILL_SECONDS = 86400000;

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            时间的格式
	 */
	public static String format(DateTime dateTime, String format) {
		return dateTime.toString(format);
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            时间的格式
	 */
	public static String format(String format) {
		DateTime dateTime = new DateTime();
		return dateTime.toString(format);
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            时间的格式
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 */
	public static String format(int year, int month, int day, int hour, int minute, int second, String format) {
		DateTime dateTime = new DateTime(year, month, day, hour, minute, second);
		return dateTime.toString(format);
	}

	/**
	 * 解析时间
	 * 
	 * @param dateTimeStr
	 *            时间字符串
	 * @param format
	 *            时间的格式
	 */
	public static DateTime parse(String dateTimeStr, String format) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
		DateTime dateTime = DateTime.parse(dateTimeStr, dateTimeFormatter);
		return dateTime;
	}

	/**
	 * 获取时间
	 * 
	 * @return int[] 0:年，1:月，2:日 ，3:时，4:分，5:秒
	 */
	public static int[] getDateTime() {
		DateTime dateTime = new DateTime();
		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int day = dateTime.getDayOfMonth();
		int hour = dateTime.getHourOfDay();
		int minute = dateTime.getMinuteOfDay();
		int second = dateTime.getSecondOfDay();
		return new int[] { year, month, day, hour, minute, second };
	}

	/**
	 * 获取前一天的时间
	 */
	public static DateTime getYesterdayDateTime() {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.minusDays(1);
		return day;
	}

	/**
	 * 获取前一天的时间
	 */
	public static String getYesterdayDateTime(String format) {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.minusDays(1);
		String dateTimeStr = format(day, format);
		return dateTimeStr;
	}

	/**
	 * 获取之前几天的时间
	 */
	public static String getPreDateTime(String format, int minusDay) {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.minusDays(minusDay);
		String dateTimeStr = format(day, format);
		return dateTimeStr;
	}

	/**
	 * 获取后一天的时间
	 */
	public static DateTime getTomorrowDateTime() {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.plusDays(1);
		return day;
	}

	/**
	 * 获取后一天的时间
	 */
	public static String getTomorrowDateTime(String format) {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.plusDays(1);
		String dateTimeStr = format(day, format);
		return dateTimeStr;
	}

	/**
	 * 获取未来之后几天的时间
	 */
	public static String getNextDateTime(String format, int plusDay) {
		DateTime dateTime = new DateTime();
		DateTime day = dateTime.plusDays(plusDay);
		String dateTimeStr = format(day, format);
		return dateTimeStr;
	}

	/**
	 * 相差的天数
	 */
	public static int diffDay(String startTime, String endTime, String format) {
		DateTime startDateTime = parse(startTime, format);
		DateTime endDateTime = parse(endTime, format);
		Days days = Days.daysBetween(startDateTime, endDateTime);
		return days.getDays();
	}

	/**
	 * 相差的天数
	 */
	public static int diffDay(DateTime startDateTime, DateTime endDateTime) {
		Days days = Days.daysBetween(startDateTime, endDateTime);
		return days.getDays();
	}

	/**
	 * 得到未来某一天的日期
	 */
	public static String getNextDayByDateTime(String dateTimeStr, int plusDays, String format) {
		DateTime dateTime = parse(dateTimeStr, format);
		DateTime day = dateTime.plusDays(plusDays);
		String newDateTimeStr = format(day, format);
		return newDateTimeStr;
	}

	/**
	 * 得到未来某一天的日期
	 */
	public static String getNextDayByDateTime(DateTime dateTime, int plusDays, String format) {
		DateTime day = dateTime.plusDays(plusDays);
		String newDateTimeStr = format(day, format);
		return newDateTimeStr;
	}

	/**
	 * 得到未来某一天的日期
	 */
	public static DateTime getNextDayByDateTime(DateTime dateTime, int plusDays) {
		DateTime result = dateTime.plusDays(plusDays);
		return result;
	}

	/**
	 * 根据毫秒时间，格式化时间
	 */
	public static String format(long timeMill, String format) {
		DateTime dateTime = new DateTime();
		dateTime = dateTime.withMillis(timeMill);
		return format(dateTime, format);
	}

}
