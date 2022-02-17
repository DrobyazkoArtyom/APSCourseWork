package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public class EntryCompleted extends Event {
    private int deviceId;

    public EntryCompleted(Entry entry, int time, int deviceId) {
        super(entry, "Entry " + entry.getSourceId() + " " + entry.getId()
                + " completed, device " + deviceId + " is available.", time, 5);
        this.deviceId = deviceId;
    }

    public int getDeviceId() {
        return deviceId;
    }
}
