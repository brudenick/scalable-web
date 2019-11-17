package com.waes;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class StaticAppender extends AppenderBase<ILoggingEvent> {
    static List<ILoggingEvent> events = new ArrayList<>();

    public static List<ILoggingEvent> getEvents() {
        return events;
    }

    public static void clearEvents() {
        events.clear();
    }

    @Override
    public void append(ILoggingEvent e) {
        events.add(e);
    }
}