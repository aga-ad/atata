package hahaton;

public enum VisitDay {
    MONDAY("Monday", "5/18/2020"),
    TUESDAY("Tuesday", "5/19/2020"),
    WEDNESDAY("Wednesday", "5/20/2020"),
    THURSDAY("Thursday", "5/21/2020"),
    FRIDAY("Friday", "5/22/2020"),
    SATURDAY("Saturday", "5/23/2020"),
    SUNDAY("Sunday", "5/24/2020"),
    EMPTY("None", "None");

    public final String name;
    public final String date;

    VisitDay(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public static VisitDay parse(String str) {
        if ("".equals(str))
            return EMPTY;
        if ("1".equals(str))
            return MONDAY;
        if ("2".equals(str))
            return TUESDAY;
        if ("3".equals(str))
            return WEDNESDAY;
        if ("4".equals(str))
            return THURSDAY;
        if ("5".equals(str))
            return FRIDAY;
        if ("6".equals(str))
            return SATURDAY;
        if ("7".equals(str))
            return SUNDAY;
        for (VisitDay visitDay : values()) {
            if (visitDay.name().equalsIgnoreCase(str))
                return visitDay;
        }
        return EMPTY;
    }
}
