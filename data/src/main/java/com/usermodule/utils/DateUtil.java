package com.usermodule.utils;


import com.usermodule.common.JalaliCalendar;
import com.usermodule.exception.DataException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class DateUtil {
    public static final int EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE = 10280;
    public static final int EXCEPTION_INPUT_DATES_SHOULD_BE_ON_THE_SAME_YEAR = 10281;
    public static final long DAY_MILLISECONDS = 24 * 60 * 60 * 1000;

    private final static String FORMAT_DATE = "yyyy-MM-dd";
    private final static String FORMAT_FULL_DATE = "yyyy-MM-dd hh:mi:ss";
    public static final long HOUR_MILLISECONDS = 60 * 60 * 1000;
    public static final long MINUTE_MILLISECONDS = 60 * 1000;
    public static final long SECOND_MILLISECONDS = 1000;
    public static final char SYMBOL_TIME_LABEL_DETAIL = 'S';
    public static final char NUMERIC_TIME_LABEL_DETAIL = 'N';
    public static final char ABBRIVIATION_TIME_LABEL_DETAIL = 'A';
    public static final char BREIF_TIME_LABEL_DETAIL = 'B';
    private static final String DEFAULT_TIME_ZONE = "GMT";
    private Date g16DefaultDate = defaultDate();
    private Calendar g16DefaultCalendar = defaultCalendar();

    private DateUtil() {
    }

    private Calendar defaultCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(2007, 0, 1, 0, 0, 0);
        return cal;
    }

    private Date defaultDate() {
        return defaultCalendar().getTime();
    }

    public Date getG16Time(int hour, int minute) {
        Calendar cal = defaultCalendar();
        cal.set(11, hour);
        cal.set(12, minute);
        return cal.getTime();
    }

    public DayOfWeeks getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(7);
        switch (dayOfWeek) {
            case 1:
                return DayOfWeeks.SUN;
            case 2:
                return DayOfWeeks.MON;
            case 3:
                return DayOfWeeks.TUE;
            case 4:
                return DayOfWeeks.WED;
            case 5:
                return DayOfWeeks.THU;
            case 6:
                return DayOfWeeks.FRI;
            case 7:
                return DayOfWeeks.SAT;
            default:
                return DayOfWeeks.UNKNOWN;
        }
    }

    public boolean isSameDay(Date date, DayOfWeeks dayOfWeeks) {
        DayOfWeeks day = getDayOfWeek(date);
        return day.equals(dayOfWeeks);
    }

    public Date addDay(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(6, amount);
        return cal.getTime();
    }

    public List<Date> getDays(Date fromDate, Date toDate, List<DayOfWeeks> dayOfWeekses) {
        List<Date> dates = new ArrayList();
        int daysDuration = getElapsed(fromDate, toDate);

        for (int counter = 0; counter < daysDuration; ++counter) {
            Iterator var6 = dayOfWeekses.iterator();

            while (var6.hasNext()) {
                DayOfWeeks day = (DayOfWeeks) var6.next();
                if (isSameDay(fromDate, day)) {
                    dates.add(fromDate);
                }
            }

            fromDate = addDay(fromDate, 1);
        }

        return dates;
    }

    public List<Date> getDays(Date fromDate, Date toDate) {
        List<Date> dates = new ArrayList();
        int daysDuration = getElapsed(fromDate, toDate);

        for (int counter = 0; counter <= daysDuration; ++counter) {
            dates.add(fromDate);
            fromDate = addDay(fromDate, 1);
        }

        return dates;
    }

    public int getElapsed(Date fromDate, Date toDate) {
        GregorianCalendar fromGregorianCalendar = new GregorianCalendar();
        fromGregorianCalendar.setTime(fromDate);
        GregorianCalendar toGregorianCalendar = new GregorianCalendar();
        toGregorianCalendar.setTime(toDate);
        return getElapsed(fromGregorianCalendar, toGregorianCalendar);
    }

    public int getElapsed(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1;
        GregorianCalendar gc2;
        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        gc1.clear(14);
        gc1.clear(13);
        gc1.clear(12);
        gc1.clear(11);
        gc2.clear(14);
        gc2.clear(13);
        gc2.clear(12);
        gc2.clear(11);

        while (gc1.before(gc2)) {
            gc1.add(5, 1);
            ++elapsed;
        }

        return elapsed;
    }

    public Date getG16Date(String timeZone, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone == null ? "GMT" : timeZone));
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public Date getG16FromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, -1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public Date getG16ToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public Date getG16Date(String timeZone, int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone == null ? "GMT" : timeZone));
        calendar.set(year, month, day, hour, minute, 0);
        return calendar.getTime();
    }

    public int compareTruncateDates(Date firstDate, Date secondDate) {
        firstDate = editTime(firstDate, 12, 0, 0, 0);
        secondDate = editTime(secondDate, 12, 0, 0, 0);
        return firstDate.compareTo(secondDate);
    }

    public Date getG16Date(String timeZone) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone == null ? "GMT" : timeZone));
        return calendar.getTime();
    }

    public Date editTime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, hour);
        calendar.set(12, minute);
        calendar.set(13, second);
        return calendar.getTime();
    }

    public Date editTime(Date date, int hour, int minute, int second, int miliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, hour);
        calendar.set(12, minute);
        calendar.set(13, second);
        calendar.set(14, miliSecond);
        return calendar.getTime();
    }

    public Date getG16DefaultDate() {
        return g16DefaultDate;
    }

    public void setG16DefaultDate(Date g16DefaultDate) {
        this.g16DefaultDate = g16DefaultDate;
    }

    public Calendar getG16DefaultCalendar() {
        return g16DefaultCalendar;
    }

    public void setG16DefaultCalendar(Calendar g16DefaultCalendar) {
        this.g16DefaultCalendar = g16DefaultCalendar;
    }

    public boolean isTwoDateInSameDay(Date firstDate, Date secondDate) {
        firstDate = editTime(firstDate, 12, 0, 0, 0);
        secondDate = editTime(secondDate, 12, 0, 0, 0);
        return firstDate.getTime() == secondDate.getTime();
    }

    public long getDifferenceMilliSeconds(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    public long getDifferenceHours(Date date1, Date date2) {
        return getDifferenceMilliSeconds(date1, date2) / 3600000L;
    }

    public float getDifferenceHoursBaseOnFloat(Date date1, Date date2) {
        float differenceMilliSeconds = (float) getDifferenceMilliSeconds(date1, date2);
        return differenceMilliSeconds / 3600000.0F;
    }

    public Date getG16Year(int year) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(year, 0, 2);
        return calendar.getTime();
    }

    public Date truncateTime(Date date) {
        Date out = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            out = calendar.getTime();
        }

        return out;
    }

    public Date getToday() {
        return truncateTime(new Date());
    }

    public void main(String[] args) {
        Date date1 = editTime(getG16Date((String) null, 2009, 7, 18, 11, 59), 14, 0, 0);
        Date date2 = editTime(getG16Date((String) null), 14, 0, 0);
        System.out.println("date2 = " + date2);
        System.out.println("date1 = " + date1);
        System.out.println("getElapsed(date1, date2) = " + getElapsed(date1, date2));
        System.out.println("isTwoDateInSameDay(date1, date2) = " + isTwoDateInSameDay(date1, date2));
        List<Date> days = getDays(date1, date2);
        Iterator var4 = days.iterator();

        while (var4.hasNext()) {
            Date day = (Date) var4.next();
            System.out.println("day = " + day);
        }

    }

    /**
     * @param solarDate: for example "871114"
     * @return desiredFormatForConvertor: for example "18/06/1387"
     */
    public String SolarYearMonthDayToDateConvertorFormat(String solarDate) {
        DateConvertorUtil cvr = new DateConvertorUtil();
        String todaySolarDate = cvr.convertDate(new Date());

        try {
            Integer.valueOf(solarDate);
        } catch (NumberFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }

        String century = todaySolarDate.substring(0, 2);
        // There is no checking for Year value; if desired embed here
        String year = new StringBuilder().append(century).append(solarDate.substring(0, 2)).toString();

        String month = solarDate.substring(2, 4);
        // Validate Month value
/*
        Integer monthInt = new Integer(month);
        if (monthInt < 1 || monthInt > 12)
*/
        // There is no checking for Year value; if desired embed here
        String day = solarDate.substring(4, 6);

        return day + "/" + month + "/" + year;
    }

    //

    public Integer getDateIntersectionWithPeriodInOneYear(Integer periodStartMonth, Integer periodStartDay,
                                                          Integer periodEndMonth, Integer periodEndDay,
                                                          Date startDate, Date endDate) throws Exception {

        Integer intersection;
        DateConvertorUtil cvr = new DateConvertorUtil();
        if (compareTruncateDates(startDate, endDate) > 0) {
            throw new DataException("THe start date is greater than end date", EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE);
        }

        int duration = getElapsed(truncateTime(startDate), truncateTime(endDate));

        Integer startDateShamsiYear = getSolarYearOfDate(startDate);
        Integer endDateShamsiYear = getSolarYearOfDate(endDate);

        if (startDateShamsiYear.equals(endDateShamsiYear)) {
            Date periodStartDate = (Date) cvr.convert(Date.class,
                    StringUtils.leftPad(String.valueOf(periodStartDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(periodStartMonth), 2, '0') + "/" + endDateShamsiYear);
            Date periodEndDate = (Date) cvr.convert(Date.class,
                    StringUtils.leftPad(String.valueOf(periodEndDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(periodEndMonth), 2, '0') + "/" + endDateShamsiYear);

            if (compareTruncateDates(periodStartDate, periodEndDate) > 0) {
                throw new DataException("THe start date is greater than end date", EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE);
            }

            if (compareTruncateDates(startDate, periodEndDate) >= 0) {
                intersection = 0;
            } else if (compareTruncateDates(endDate, periodStartDate) <= 0) {
                intersection = 0;
            } else {
                Integer otherDays;
                if (compareTruncateDates(startDate, periodStartDate) <= 0) {
                    otherDays = getElapsed(truncateTime(startDate), truncateTime(periodStartDate));
                } else {
                    otherDays = 0;
                }

                if (compareTruncateDates(endDate, periodEndDate) >= 0) {
                    otherDays += getElapsed(truncateTime(periodEndDate), truncateTime(endDate));
                }
                intersection = duration - otherDays;
            }
        } else {
            throw new DataException("INPUT DATES SHOULD BE ON THE SAME YEAR", EXCEPTION_INPUT_DATES_SHOULD_BE_ON_THE_SAME_YEAR);
        }
        return intersection;
    }

    public Integer getDateIntersectionWithPeriod(Integer periodStartMonth, Integer periodStartDay,
                                                 Integer periodEndMonth, Integer periodEndDay,
                                                 Date endDate, Date startDate) throws Exception {
        Integer intersection = 0;
        if (compareTruncateDates(startDate, endDate) > 0) {
            throw new DataException("THe start date is greater than end date", EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE);
        }

        Date checkFrom = startDate;
        Date checkTo = endDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        dateFormat.setCalendar(new JalaliCalendar());
        Integer startShamsiYear = Integer.valueOf(dateFormat.format(startDate));
        Integer endShamsiYear = Integer.valueOf(dateFormat.format(endDate));

        DateConvertorUtil cvr = new DateConvertorUtil();
        Date periodStartDate = (Date) cvr.convert(Date.class,
                StringUtils.leftPad(String.valueOf(periodStartDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(periodStartMonth), 2, '0') + "/" + startShamsiYear);
        Date periodEndDate = (Date) cvr.convert(Date.class,
                StringUtils.leftPad(String.valueOf(periodEndDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(periodEndMonth), 2, '0') + "/" + endShamsiYear);

        if (compareTruncateDates(periodStartDate, periodEndDate) > 0) {
            throw new DataException("THe start date is greater than end date", EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE);
        }

        while (endShamsiYear > startShamsiYear) {
            checkTo = getLastDayOfYear(startShamsiYear);
            intersection +=
                    getDateIntersectionWithPeriodInOneYear(periodStartMonth, periodStartDay, periodEndMonth, periodEndDay,
                            checkFrom, checkTo);
            startShamsiYear++;
            checkFrom = getFirstDayOfYear(startShamsiYear);
            checkTo = endDate;
        }

        intersection +=
                getDateIntersectionWithPeriodInOneYear(periodStartMonth, periodStartDay, periodEndMonth, periodEndDay,
                        checkFrom, checkTo);

        return intersection;
    }

    /**
     * @param fromDate1
     * @param toDate1
     * @param fromDate2
     * @param toDate2
     * @return
     * @throws Exception
     */
    public Map<Integer, Date> getIntersectionBetweenDateRanges(Date fromDate1, Date toDate1, Date fromDate2, Date toDate2) throws Exception {
        Map<Integer, Date> intersectionMap = null;

        if (fromDate1 == null || toDate1 == null || fromDate2 == null || toDate2 == null) {
            throw new Exception("** Invalid Input Parameters to getIntersectionBetweenDateRanges **");
        }

        if (toDate1.compareTo(fromDate2) < 0 || fromDate1.compareTo(toDate2) > 0) {
            log.debug("** The date ranges has no intersection **");

        } else {
            Date startDate;
            Date endDate;

            if (fromDate1.compareTo(fromDate2) >= 0) {
                startDate = fromDate1;
            } else {
                startDate = fromDate2;
            }

            if (toDate1.compareTo(toDate2) <= 0) {
                endDate = toDate1;
            } else {
                endDate = toDate2;
            }

            intersectionMap = new HashMap<Integer, Date>();
            intersectionMap.put(0, startDate);
            intersectionMap.put(1, endDate);
        }

        return intersectionMap;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return a date
     * @throws Exception
     */
    public Date getDateBySolarYearAndMonthAndDay(Integer year, Integer month, Integer day) throws Exception {
        if (year == null || month == null || day == null) {
            throw new Exception("Invalid Input Parameters to build date");
        }

        String yearStr = StringUtils.leftPad(String.valueOf(year), 4, '0');
        String monthStr = StringUtils.leftPad(String.valueOf(month), 2, '0');
        String dayStr = StringUtils.leftPad(String.valueOf(day), 2, '0');

        String dateStr = dayStr + "/" + monthStr + "/" + yearStr;

        DateConvertorUtil cvr = new DateConvertorUtil();
        return (Date) cvr.convert(Date.class, dateStr);
    }

    /**
     * @param year
     * @return
     */
    public Date getFirstDayOfYear(int year) {
        DateConvertorUtil cvr = new DateConvertorUtil();
        String shamsiFirstDayOfYear = "01/01/" + StringUtils.leftPad(String.valueOf(year), 4, "0");
        return (Date) cvr.convert(Date.class, shamsiFirstDayOfYear);
    }

    /**
     * @param year
     * @return
     */
    public Date getLastDayOfYear(int year) {
        Date firstDayOfNextYear = getFirstDayOfYear(year + 1);
        Calendar gregorianCalendar = new GregorianCalendar();

        gregorianCalendar.setTime(firstDayOfNextYear);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);

        return truncateTime(gregorianCalendar.getTime());
    }

    /**
     * Converts the input date to Jalali calendar and then formats it according to the input pattern
     *
     * @param date
     * @param dateFormatPattern (the pattern will be changed to dd/MM/yyyy, if this param is null or empty)
     * @return
     */
    public String getSolarDateByFormat(Date date, String dateFormatPattern) throws Exception {

        if (date == null) {
            throw new Exception("The input date can not be null.");
        }
        if (dateFormatPattern == null || dateFormatPattern.equals("")) {
            dateFormatPattern = "dd/MM/yyyy";
        }

        int firstIndex = StringUtils.upperCase(dateFormatPattern).indexOf("Y");
        int lastIndex = -1;
        if (firstIndex != -1) {
            lastIndex = StringUtils.upperCase(dateFormatPattern).lastIndexOf("Y");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        dateFormat.setCalendar(new JalaliCalendar());
        String dateStr = dateFormat.format(daylightCheck(date));

        //the following lines added for JDK 7 (and later) bug. the jalali calendar returns YYYY instead of YY.
        if (firstIndex != -1 && lastIndex != -1 && lastIndex - firstIndex < 2 && dateStr.length() > dateFormatPattern.length()) {
            char[] dateArr = dateStr.toCharArray();
            dateArr[firstIndex++] = '\0';
            dateArr[firstIndex] = '\0';
            dateStr = "";
            for (char c : dateArr) {
                if (c != '\0') {
                    dateStr += c;
                }
            }
        }
        return dateStr;
    }

    private Date daylightCheck(Date date) {
        TimeZone tz = TimeZone.getDefault();
        if (tz.inDaylightTime(date)) {
            return new Date(date.getTime() + tz.getDSTSavings());
        }
        return date;
    }

    public Integer getDateIntersectionBetween2DateRange(Date startDate1, Date endDate1,
                                                        Date startDate, Date endDate) throws Exception {
        int duration = 0;
        if (compareTruncateDates(startDate, endDate) > 0 || compareTruncateDates(startDate1, endDate1) > 0) {
            throw new DataException("THe start date is greater than end date", EXCEPTION_START_DATE_IS_GREATER_THAN_END_DATE);
        }

        Map<Integer, Date> intersectionBetweenDateRanges = getIntersectionBetweenDateRanges(startDate1, endDate1, startDate, endDate);
        if (intersectionBetweenDateRanges != null) {
            startDate = intersectionBetweenDateRanges.get(0);
            endDate = intersectionBetweenDateRanges.get(1);

            duration = getElapsed(truncateTime(startDate), truncateTime(endDate));
        }

        log.debug("Duration = " + duration);

        return duration;
    }

    /**
     * returns date sets month to one month later , and the year does not increase.
     */
    public Date getDateByOneMonthIncrease(Date date) throws Exception {

        if (date != null) {
            date = truncateTime(date);

            int solarYear = getSolarYearOfDate(date);
            Date nextMonthDate = getSameDayOfNextMonth(date);
            Date lastDayOfYear = getLastDayOfYear(solarYear);

            if (compareTruncateDates(nextMonthDate, lastDayOfYear) == 1) {
                date = lastDayOfYear;
            } else {
                date = nextMonthDate;
            }
        }
        return date;

    }

    public Date getDateByOneDayDecrease(Date date) throws Exception {

        if (date != null) {
            date = truncateTime(date);
        }
        String monthStr = getSolarDateByFormat(date, "MM");
        int solarMonth = Integer.valueOf(monthStr);
        String dayStr = getSolarDateByFormat(date, "dd");
        int solarDay = Integer.valueOf(dayStr);
        String yearStr = getSolarDateByFormat(date, "yyyy");
        int solarYear = Integer.valueOf(yearStr);

        if (solarMonth == 1 && solarDay == 1) {
            solarYear--;
            date = getLastDayOfYear(solarYear);
        } else {
            solarDay--;
            date = getDateBySolarYearAndMonthAndDay(solarYear, solarMonth, solarDay);

        }

        return date;

    }

    public Date getDateByOneYearDecrease(Date date) throws Exception {

        Date lastYearDate = null;
        if (date != null) {
            date = truncateTime(date);

            int solarYear = getSolarYearOfDate(date);
            lastYearDate = getSameDayOfLastYear(date, 2);

        }
        return lastYearDate;

    }

    public Date addYearToDate(Date date) throws Exception {

        Date nextYearDate = null;
        if (date != null) {
            date = truncateTime(date);

            nextYearDate = addYearToDate(date, 1);
        }
        return nextYearDate;

    }

    public Date getSameDayOfLastYear(Date date, int formatMode) {
        Date out = new Date();
        try {
            int solarYear = getSolarYearOfDate(date);
            solarYear--;
            int solarMonth = Integer.valueOf(getSolarDateByFormat(date, "MM"));
            int solarDay = Integer.valueOf(getSolarDateByFormat(date, "dd"));

            DateConvertorUtil cvr = new DateConvertorUtil();

//            if (formatMode == 1) {
//                out = String.valueOf(solarYear) + "/" + towDigitStr(solarMonth) + "/" + towDigitStr(solarDay);
//            } else {
//                out = towDigitStr(solarDay) + "/" + towDigitStr(solarMonth) + "/" + String.valueOf(solarYear);
//            }

            out = (Date) cvr.convert(Date.class,
                    StringUtils.leftPad(String.valueOf(solarDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(solarMonth), 2, '0') + "/" + solarYear);

        } catch (Exception e) {
            log.debug("Date conversion failed. error message = " + e.getMessage());
        }

        return out;
    }

    /**
     * @param date
     * @return
     */
    public Date addYearToDate(Date date, Integer years) {
        Date out = new Date();
        try {
            if (date != null) {
                date = truncateTime(date);

                int solarYear = getSolarYearOfDate(date);

                solarYear += years;
                int solarMonth = Integer.valueOf(getSolarDateByFormat(date, "MM"));
                int solarDay = Integer.valueOf(getSolarDateByFormat(date, "dd"));

                DateConvertorUtil cvr = new DateConvertorUtil();

                out = (Date) cvr.convert(Date.class,
                        StringUtils.leftPad(String.valueOf(solarDay), 2, '0') + "/" + StringUtils.leftPad(String.valueOf(solarMonth), 2, '0') + "/" + solarYear);
            }

        } catch (Exception e) {
            log.debug("Date conversion failed. error message = " + e.getMessage());
        }

        return out;
    }

    public String getDateByTwoMonthIncrease(Date date) {
        date = truncateTime(date);
        Calendar cal = new JalaliCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 2);
        Date next2Month = cal.getTime();
        return getSolarDateByFormat(next2Month);

    }

    /**
     * Converts the input dte to Jalali calendar and returns the formatted string in yyyy/MM/dd format
     *
     * @param date
     * @return
     */
    public String getSolarDateByFormat(Date date) {
        String out = null;
        if (date != null) {
            try {
                out = getSolarDateByFormat(date, "yyyy/MM/dd");
            } catch (Exception e) {
                log.debug("Date conversion failed. error message = " + e.getMessage());
            }
        }
        return out;
    }

    /**
     * @param date
     * @return the Jalali year of the input date
     * @throws Exception
     */

    @SneakyThrows
    public Integer getSolarYearOfDate(Date date) {
        String yearStr = getSolarDateByFormat(date, "yyyy");
        return Integer.valueOf(yearStr);
    }

    /**
     * @param date
     * @return the Jalali Month of the input date
     * @throws Exception
     */

    @SneakyThrows
    public Integer getSolarMonthOfDate(Date date) {
        String monthStr = getSolarDateByFormat(date, "MM");
        return Integer.valueOf(monthStr);
    }

    /**
     * @param date
     * @return the Jalali Day of the input date
     * @throws Exception
     */
    public Integer getSolarDayOfDate(Date date) throws Exception {
        String dayStr = getSolarDateByFormat(date, "dd");
        return Integer.valueOf(dayStr);
    }

    /**
     * Returns the same Day of the next month (in Jalali calendar format)
     *
     * @param date
     * @param formatMode (if mode = 1 then return the date in YYYY/MM/dd format. else in dd/MM/YYYY format)
     * @return
     */
    public String getSameSolarDayOfNextMonth(Date date, int formatMode) {
        String out = "";
        try {
            int solarYear = getSolarYearOfDate(date);
            String monthStr = getSolarDateByFormat(date, "MM");
            int solarMonth = Integer.valueOf(monthStr);
            String dayStr = getSolarDateByFormat(date, "dd");
            int solarDay = Integer.valueOf(dayStr);

            if ((solarMonth == 6) && (solarDay == 31)) {
                //next month is 30 days
                solarDay = 30;
                solarMonth++;
            } else if (solarMonth == 12) {
                //next month will be in next year
                solarMonth = 1;
                solarYear++;
            } else if ((solarMonth == 11) && (solarDay == 30)) {
                //ignore leap year
                //next month is 29 days
                solarDay = 29;
                solarMonth++;
            } else {
                solarMonth++;
            }

            if (formatMode == 1) {
                out = String.valueOf(solarYear) + "/" + towDigitStr(solarMonth) + "/" + towDigitStr(solarDay);
            } else {
                out = towDigitStr(solarDay) + "/" + towDigitStr(solarMonth) + "/" + String.valueOf(solarYear);
            }

        } catch (Exception e) {
            log.debug("Date conversion failed. error message = " + e.getMessage());
        }

        return out;
    }

    /**
     * Returns the Date equal to same Day of the next month (based on Jalali calendar)
     *
     * @param date
     * @return
     */
    public Date getSameDayOfNextMonth(Date date) {
        String nextMonthDay = getSameSolarDayOfNextMonth(date, 2);
        DateConvertorUtil cvr = new DateConvertorUtil();
        return (Date) cvr.convert(Date.class, nextMonthDay);
    }

    /**
     * Convets the current date to Jalali calendar and then formats it according to the input pattern
     *
     * @param dateFormatPattern
     * @return current date Of Jalali calendar in specified format
     */
    public String getCurrentDateByFormat(String dateFormatPattern) throws Exception {
        return getSolarDateByFormat(new Date(), dateFormatPattern);
    }

    /**
     * Returns the current Jalali Year in 4 digit format
     *
     * @return current year
     */
    public String getCurrntYearYYYY() throws Exception {
        return getCurrentDateByFormat("yyyy");
    }

    /**
     * Returns the current Jalali Month
     *
     * @return current year
     */
    public String getCurrntMonth() throws Exception {
        return getCurrentDateByFormat("MM");
    }

    /**
     * Returns the current Jalali Day
     *
     * @return current year
     */
    public String getCurrntDay() throws Exception {
        return getCurrentDateByFormat("dd");
    }

    /**
     * Returns the Jalali Year of input date in 4 digit format
     *
     * @return current year
     */
    public String getDateSolarYearYYYY(Date inputDate) {
        try {
            return getSolarDateByFormat(inputDate, "yyyy");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the Jalali Month of input date in 2 digit format
     *
     * @return current year
     */
    public String getDateSolarMonth(Date inputDate) {
        try {
            return getSolarDateByFormat(inputDate, "MM");
        } catch (Exception e) {
            return null;
        }
    }

    private String towDigitStr(int in) {
        return ((in < 10) ? ("0" + in) : String.valueOf(in));
    }

    /**
     * @param startDate
     * @param endDate
     * @param timeLabelDetail
     * @param addPostfix
     * @return DateUtils.getTwoDateDifferencesPlainEnglish(diffMilis, DateUtils.SYMBOL_TIME_LABEL_DETAIL, false))
     */
    public String getTwoDateDifferencesPlainEnglish(Date startDate, Date endDate, char timeLabelDetail, boolean addPostfix) {
        return getTwoDateDifferencesPlainEnglish(endDate.getTime() - startDate.getTime(), timeLabelDetail, addPostfix);
    }

    /**
     * @param diffMillies
     * @param timeLabelDetail
     * @param addPostfix
     * @return
     */
    public String getTwoDateDifferencesPlainEnglish(long diffMillies, char timeLabelDetail, boolean addPostfix) {
        String postfix = "ago";
        if (diffMillies < 0) {
            postfix = "later";
        }
        diffMillies = Math.abs(diffMillies);
        long days = diffMillies / DAY_MILLISECONDS;
        long hours = (diffMillies - days * DAY_MILLISECONDS) / HOUR_MILLISECONDS;
        long minutes = (diffMillies - days * DAY_MILLISECONDS - hours * HOUR_MILLISECONDS) / MINUTE_MILLISECONDS;
        long seconds = (diffMillies - days * DAY_MILLISECONDS - hours * HOUR_MILLISECONDS - minutes * MINUTE_MILLISECONDS) / SECOND_MILLISECONDS;
        long milliseconds = (diffMillies - days * DAY_MILLISECONDS - hours * HOUR_MILLISECONDS - minutes * MINUTE_MILLISECONDS - seconds * SECOND_MILLISECONDS);
        String resultString = "";

        if (days != 0) {
            resultString = appendTimePart(resultString, days);
            resultString += getTimeLabelDetail('D', timeLabelDetail, days > 1);
        }

        if (hours != 0) {
            resultString = appendTimePart(resultString, hours);
            resultString += getTimeLabelDetail('H', timeLabelDetail, hours > 1);
        }

        if (minutes != 0) {
            resultString = appendTimePart(resultString, minutes);
            resultString += getTimeLabelDetail('M', timeLabelDetail, minutes > 1);
        }

        if (seconds != 0) {
            resultString = appendTimePart(resultString, seconds);
            resultString += getTimeLabelDetail(SYMBOL_TIME_LABEL_DETAIL, timeLabelDetail, seconds > 1);
        }

        resultString = appendTimePart(resultString, milliseconds);
        resultString += getTimeLabelDetail('I', timeLabelDetail, milliseconds > 1);

        if (addPostfix)
            resultString += " " + postfix;

        return resultString;
    }

    /**
     * @param resultString
     * @param timePart
     * @return
     */
    private String appendTimePart(String resultString, long timePart) {
        if (resultString.length() != 0) {
            resultString += ", " + timePart;
        } else
            resultString += (" " + timePart);
        return resultString;
    }

    /**
     * @param whichPart
     * @param timeLabelDetail
     * @param isPlural
     * @return
     */
    private String getTimeLabelDetail(char whichPart, char timeLabelDetail, boolean isPlural) {
        String resultLabel = "";
        switch (Character.toUpperCase(whichPart)) {
            case 'D'://day
                switch (Character.toUpperCase(timeLabelDetail)) {
                    case NUMERIC_TIME_LABEL_DETAIL://Numeric
                        resultLabel = "?";
                        break;
                    case SYMBOL_TIME_LABEL_DETAIL://Symbol
                        resultLabel = "D";
                        break;
                    case ABBRIVIATION_TIME_LABEL_DETAIL://Abbriviation
                        resultLabel = "D";
                        break;
                    case BREIF_TIME_LABEL_DETAIL://Breif
                        resultLabel = "day";
                        break;
                    default:
                        resultLabel = "day";
                        break;
                }
                break;
            case 'H'://hour
                switch (Character.toUpperCase(timeLabelDetail)) {
                    case NUMERIC_TIME_LABEL_DETAIL://Numeric
                        resultLabel = "?";
                        break;
                    case SYMBOL_TIME_LABEL_DETAIL://Symbol
                        resultLabel = "H";
                        break;
                    case ABBRIVIATION_TIME_LABEL_DETAIL://Abbriviation
                        resultLabel = "H";
                        break;
                    case BREIF_TIME_LABEL_DETAIL://Breif
                        resultLabel = "hour";
                        break;
                    default:
                        resultLabel = "hour";
                        break;
                }
                break;
            case 'M'://minute
                switch (Character.toUpperCase(timeLabelDetail)) {
                    case NUMERIC_TIME_LABEL_DETAIL://Numeric
                        resultLabel = "?";
                        break;
                    case SYMBOL_TIME_LABEL_DETAIL://Symbol
                        resultLabel = "'";
                        break;
                    case ABBRIVIATION_TIME_LABEL_DETAIL://Abbriviation
                        resultLabel = "M";
                        break;
                    case BREIF_TIME_LABEL_DETAIL://Breif
                        resultLabel = "Min";
                        break;
                    default:
                        resultLabel = "minute";
                        break;
                }
                break;
            case 'S'://second
                switch (Character.toUpperCase(timeLabelDetail)) {
                    case NUMERIC_TIME_LABEL_DETAIL://Numeric
                        resultLabel = "?";
                        break;
                    case SYMBOL_TIME_LABEL_DETAIL://Symbol
                        resultLabel = "\"";
                        break;
                    case ABBRIVIATION_TIME_LABEL_DETAIL://Abbriviation
                        resultLabel = "S";
                        break;
                    case BREIF_TIME_LABEL_DETAIL://Breif
                        resultLabel = "Sec";
                        break;
                    default:
                        resultLabel = "second";
                        break;
                }
                break;
            case 'I'://milliseconds
                switch (Character.toUpperCase(timeLabelDetail)) {
                    case NUMERIC_TIME_LABEL_DETAIL://Numeric
                        resultLabel = "?";
                        break;
                    case SYMBOL_TIME_LABEL_DETAIL://Symbol
                        resultLabel = "?";
                        break;
                    case ABBRIVIATION_TIME_LABEL_DETAIL://Abbriviation
                        resultLabel = "MS";
                        break;
                    case BREIF_TIME_LABEL_DETAIL://Breif
                        resultLabel = "Milli";
                        break;
                    default:
                        resultLabel = "millisecond";
                        break;
                }
                break;

        }
        if (resultLabel.length() > 1 && isPlural)
            resultLabel += "s";
        return " " + resultLabel;
    }

    /**
     * THE FORMAT SHOULD BE "YYMMDD"
     *
     * @param solarDateStr
     * @param pattern
     * @return Date
     */
    public Date getDateBySolarFormat(String solarDateStr, String pattern) {
        Date result = null;
        if (solarDateStr == null || solarDateStr.equals("") || solarDateStr.length() != solarDateStr.length()) {
            result = null;
        } else {
            try {
                DateConvertorUtil cvr = new DateConvertorUtil();

                if (pattern != null) {
                    pattern = StringUtils.upperCase(pattern);
                    String yearStr = solarDateStr.substring(0, pattern.lastIndexOf('Y') + 1);
                    String monthStr = solarDateStr.substring(pattern.lastIndexOf('Y') + 1, pattern.lastIndexOf("M") + 1);
                    String dayStr = solarDateStr.substring(pattern.lastIndexOf('M') + 1, pattern.lastIndexOf('D') + 1);

                    if (yearStr.length() == 2) {
                        yearStr = "13" + yearStr;
                    }
                    Integer month = Integer.valueOf(monthStr);
                    Integer day = Integer.valueOf(dayStr);

                    if (month < 1 || month > 12) {
                        result = null;
                    } else if (day < 1 || day > 31) {
                        result = null;
                    } else {
                        result = (Date) cvr.convert(Date.class, dayStr + "/" + monthStr + "/" + yearStr);
                    }
                }
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

    public String convertDateToStringStandardSOAP(Date date) {
        String convertedDate = null;
        if (date != null) {
            String DateFormatTemplate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            DateFormat DateFormat = new SimpleDateFormat(DateFormatTemplate);
            convertedDate = DateFormat.format(date);
        }
        return convertedDate;
    }

    public String convertDateToStringStandardSOAPWithMiliSec(Date date) {
        String convertedDate = null;
        if (date != null) {
            String DateFormatTemplate = "yyyy-MM-dd'T'HH:mm:ss'+04:30'";
            DateFormat DateFormat = new SimpleDateFormat(DateFormatTemplate);
            convertedDate = DateFormat.format(date);
        }
        return convertedDate;
    }

    public Date convertStringToDateStandardSOAP(String soapDateStr) throws Exception {
        Date convertedDate = null;
        if (soapDateStr != null && !soapDateStr.equals("")) {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            try {
                convertedDate = dateFormatter.parse(soapDateStr);
            } catch (Exception lEx) {
                throw new Exception("Can't parse " + soapDateStr + " as process date");
            }
        }
        return convertedDate;
    }

    public int findNumOfFridaysBetweeDateRanges(Date fromDate, Date toDate) {
        int friday = 0;
        Date temp = fromDate;
        Calendar cal = Calendar.getInstance();
        while (temp.compareTo(toDate) < 0) {
            cal.setTime(temp);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                friday++;
            }
            temp = addDay(temp, 1);
        }
        return friday;
    }

    public String getSolarDateFullTime(Date inputDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setCalendar(new JalaliCalendar());
        TimeZone tz = TimeZone.getDefault();
        Date data = tz.inDaylightTime(inputDate) ? new Date(inputDate.getTime() + (long) tz.getDSTSavings()) : inputDate;
        return format.format(data);
    }

    @SneakyThrows
    public Date getDate(String date) {
        if (date != null && !date.isEmpty()) {
            return new SimpleDateFormat(FORMAT_DATE).parse(date);
        } else {
            return null;
        }
    }

    @SneakyThrows
    public Date getFullDate(String date) {
        if (date != null && !date.isEmpty()) {
            return new SimpleDateFormat(FORMAT_FULL_DATE).parse(date);
        } else {
            return null;
        }
    }

    @SneakyThrows
    public Date getDateByFormat(String date, String format) {
        if (date != null && !date.isEmpty()) {
            return new SimpleDateFormat(format).parse(date);
        } else {
            return null;
        }
    }

    public enum DayOfWeeks {
        SUN,
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        UNKNOWN;

        private DayOfWeeks() {
        }
    }
}
