package com.example.couchpotatosplan.myday;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class MyDayEventList {
    public static ArrayList<MyDayEvent> eventsList = new ArrayList<>();

    public static ArrayList<MyDayEvent> eventsForDate(String date)
    {
        ArrayList<MyDayEvent> events = new ArrayList<>();

        for(MyDayEvent event : eventsList)
        {
            if(event != null) {
                if (event.getDate().equals(date))
                    events.add(event);
            }
        }

        return events;
    }
}
