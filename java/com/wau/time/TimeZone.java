package com.wau.time;


public enum TimeZone {

    EST(0, "EASTERN STANDARD TIME", "GMT-0500"),

    AST(1, "ATLANTIC STANDARD TIME", "GMT-0400"),

    IST(2, "INDIAN STANDARD TIME", "GMT+0530"),

    NONE(3, "NO TIME", "GMT");

    private final int timeZoneId;
    private final String timeZoneName;
    private final String offSet;

    public int getId() {
        return this.timeZoneId;
    }

    public String getOffSet() {
        return this.offSet;
    }

    public String getTimeZoneName() {
        return this.timeZoneName;
    }

    TimeZone(int timeZoneId, String timeZoneName, String offSet) {
        this.timeZoneId = timeZoneId;
        this.timeZoneName = timeZoneName;
        this.offSet = offSet;
    }
}


