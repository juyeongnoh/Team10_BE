package bdbe.bdbd._core.errors.utils;

import bdbe.bdbd.optime.DayType;

import java.time.DayOfWeek;

public class DateUtils {

    public static DayType getDayType(DayOfWeek dayOfWeek) {
        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return DayType.WEEKEND;
        } else {
            return DayType.WEEKDAY;
        }
    }
}
