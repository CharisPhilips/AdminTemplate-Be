package com.kilcote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * DateTime Converter
 *
 * @author Shun fu
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     *	 Format the time, the format is yyyyMMddHHmmss 
     *
     * @param localDateTime LocalDateTime
     * @return Formatted string
     */
    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    /**
     *	 Format the time according to the format passed in
     *
     * @param localDateTime LocalDateTime
     * @param format
     * @return Formatted string
     */
    public static String formatFullTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 	Format the time according to the format passed in
     *
     * @param date   Date
     * @param format
     * @return Formatted string
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 	Format CST type time string
     *
     * @param date   CST type time string
     * @param format format
     * @return Formatted string
     * @throws ParseException abnormal
     */
    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return getDateFormat(usDate, format);
    }

    /**
     *	 format Instant
     *
     * @param instant Instant
     * @param format  format
     * @return Formatted string
     */
    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * @param dateToConvert
     * @return
     */
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    	if (dateToConvert == null) return null;
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    }
    
    /**
     * @param dateToConvert
     * @return
     */
    public static LocalTime convertToLocalTimeViaInstant(Date dateToConvert) {
    	if (dateToConvert == null) return null;
    	return dateToConvert.toInstant()
    			.atZone(ZoneId.systemDefault())
    			.toLocalTime();
    }
    
    /**
     * @param dateToConvert
     * @return
     */
    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
    	if (dateToConvert == null) return null;
    	return dateToConvert.toInstant()
    			.atZone(ZoneId.systemDefault())
    			.toLocalDateTime();
    }
    
    /**
     *	 Determine whether the current time is within the specified time range
     *
     * @param from start time
     * @param to   end time
     * @return result
     */
    public static boolean between(LocalTime from, LocalTime to) {
        LocalTime now = LocalTime.now();
        return now.isAfter(from) && now.isBefore(to);
    }
    
	public static int getDiffDays(Date dtStart, Date dtEnd) {
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		start.setTime(dtStart);
		end.setTime(dtEnd);
		
		Date startDate = start.getTime();
		Date endDate = end.getTime();
		
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return (int) diffDays;
	}
}
