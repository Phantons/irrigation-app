package es.upm.etsit.irrigation.util;


public enum DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int ID;
    public static final int DAYS_OF_WEEK = 7;

    DayOfWeek(int _id) {
        ID = _id;
    }

    public int getID() {
        return ID;
    }
}
