package com.usermodule.utils;


import com.usermodule.common.JalaliCalendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateConvertorUtil implements Serializable {
    public DateConvertorUtil() {
    }

    public Object convert(Class inClazz, Object inObject) {
        return Date.class.equals(inClazz) ? this.object2Date(inObject) : inObject;
    }

    private Date object2Date(Object inObject) {
        if (inObject == null) {
            return null;
        } else if (inObject instanceof Date) {
            return (Date) inObject;
        } else if (!(inObject instanceof String)) {
            return null;
        } else {
            String strDate = (String) inObject;
            if (strDate.trim().length() == 0) {
                return null;
            } else {
                Calendar c = new JalaliCalendar(Integer.valueOf(strDate.substring(6, 10)), Integer.valueOf(strDate.substring(3, 5)) - 1, Integer.valueOf(strDate.substring(0, 2)));
                return c.getTime();
            }
        }
    }

    public String convertDate(Date temp) {
        String date = "";
        if (temp != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setCalendar(new JalaliCalendar());
            date = dateFormat.format(this.daylightCheck(temp));
        }

        return date;
    }

    private Date daylightCheck(Date date) {
        TimeZone tz = TimeZone.getDefault();
        return tz.inDaylightTime(date) ? new Date(date.getTime() + (long) tz.getDSTSavings()) : date;
    }
}
