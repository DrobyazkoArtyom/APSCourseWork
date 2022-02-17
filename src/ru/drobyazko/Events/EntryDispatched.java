package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public class EntryDispatched extends Event {
    public EntryDispatched(Entry entry, int time, int deviceId) {
        super(entry, "Entry " + entry.getSourceId() + " "
                + entry.getId() + " dispatched to device " + deviceId + ".", time, 1);
    }
}