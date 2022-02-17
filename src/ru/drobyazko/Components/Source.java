package ru.drobyazko.Components;

import ru.drobyazko.Events.EntryGenerated;

import java.util.ArrayList;
import java.util.List;

public class Source {
    private static final double lambda = 0.4;
    private final int id;
    private int currTime = 0;
    private int currEntryId = 0;
    private List<Entry> entryList = new ArrayList<>();

    public Source(int id) {
        this.id = id;
    }

    public EntryGenerated generateEntry() {
        currTime += (int) (-1 / lambda * Math.log(Math.random()));
        Entry entry = new Entry(currTime, currEntryId++, id);
        entryList.add(entry);
        return new EntryGenerated(entry, currTime);
    }

    public int getId() {
        return id;
    }

    public List<Entry> getEntryList() {
        return entryList;
    }
}
