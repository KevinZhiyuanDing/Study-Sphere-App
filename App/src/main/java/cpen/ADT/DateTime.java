package cpen.adt;

import cpen.ADT.DateTimeFormat;

public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hour;

    /**
     * Constructs a DateTime with the specified year, month, day and hour.
     *
     * @param year the year, must not be negative.
     * @param month the month value, from 1 (Jan) - 12 (Dec).
     * @param day the day of the month,
     *            from 1 - 31 for Jan, Mar, May, July, Aug, Oct, Dec
     *            from 1 - 30 for Apr, Jun, Sep, Nov
     *            from 1 - 29 for Feb on non-leap years
     *            from 1 - 28 for Feb on leap years
     * @param hour the hour of the day in military time, from 0 to 23.
     * @throws IllegalArgumentException if any of the parameters are out of their valid range.
     */
    public DateTime(int year, int month, int day, int hour) {
        if (year < 0 || month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Returns the string representation of this date using the specified format.
     *
     * @param f the date format: DateTimeFormat.YYYY_MM_DD or DateTimeFormat.MM_DD_YYYY.
     * @return the formatted date string.
     */
    public String getDateString(DateTimeFormat f) {
        if (f == DateTimeFormat.YYYY_MM_DD) {
            return getYearString() + "-" + getMonthString() + "-" + getDayString();
        } else {
            return getMonthString() + "-" + getDayString() + "-" + getYearString();
        }
    }

    /**
     * Returns the string representation of the year in four digits.
     * Requires the year be representable by 4 decimal digits
     * @return the year as a four-digit string.
     */
    public String getYearString() {
        if (year < 10) {
            return "000" + year;
        } else if (year < 100) {
            return "00" + year;
        } else if (year < 1000) {
            return "0" + year;
        }
        return String.valueOf(year);
    }

    /**
     * Returns the string representation of the month in two digits with padding leading zeros.
     *
     * @return the month as a string.
     */
    public String getMonthString() {
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    /**
     * Returns the string representation of the day in two digits with padding leading zero.
     *
     * @return the day as a string.
     */
    public String getDayString() {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    /**
     * Returns the string representation of the hour in two digits with padding leading zero
     *
     * @return the hour as a string.
     */
    public String getHourString() {
        if (hour < 10) {
            return "0" + hour;
        }
        return String.valueOf(hour);
    }

    /**
     * Returns the string representation of this date and time using the specified format.
     *
     * @param f the date format: DateTimeFormat.YYYY_MM_DD or DateTimeFormat.MM_DD_YYYY
     * @return the formatted date and time string.
     */
    public String getDateTimeString(DateTimeFormat f) {
        return getDateString(f) + getHourString() + ":00";
    }
}