package com.example.slacker.weekly;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils
{
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy. MM");
        return date.format(formatter);
    }

    public static String monthDayFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM. dd EE");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }
}