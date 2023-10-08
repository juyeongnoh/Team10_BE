package bdbe.bdbd.optime;

import lombok.Getter;

@Getter
public enum DayType {
    WEEKDAY("평일"),
    WEEKEND("주말"),
    HOLIDAY("휴일");

    private final String dayName;

    DayType(String dayName) {
        this.dayName = dayName;
    }
}