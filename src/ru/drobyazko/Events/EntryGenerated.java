package ru.drobyazko.Events;

import ru.drobyazko.Components.Entry;

public class EntryGenerated extends Event {
    public EntryGenerated(Entry entry, int time) {
        super(entry, "Entry " + entry.getSourceId() + " " + entry.getId() + " generated.", time, 4);
    }
}
