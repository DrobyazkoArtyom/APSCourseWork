package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public abstract class Event {
    protected Entry entry;
    protected String message;
    protected int time;
    protected int priority;

    public Event(Entry entry, String message, int time, int priority) {
        this.entry = entry;
        this.message = message;
        this.time = time;
        this.priority = priority;
    }

    public int getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public Entry getEntry() {
        return entry;
    }

    public int getPriority() {
        return priority;
    }
}
