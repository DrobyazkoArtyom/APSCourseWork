package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public class EntryPlaced extends Event {
    public EntryPlaced(Entry entry, int time) {
        super(entry, "Entry " + entry.getSourceId() + " " + entry.getId() + " placed in the buffer.", time, 3);
    }
}
