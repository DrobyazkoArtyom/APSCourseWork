package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public class EntryCancelled extends Event {
    public EntryCancelled(Entry entry, Entry newEntry, int time) {
        super(entry, "Entry " + entry.getSourceId() + " " + entry.getId()
                + " cancelled and replaced by entry " + newEntry.getSourceId() + " " + newEntry.getId() + ".", time, 2);
    }
}
