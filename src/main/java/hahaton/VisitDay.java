package hahaton;

public enum VisitDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    EMPTY;

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
