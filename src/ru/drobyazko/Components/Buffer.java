package ru.drobyazko.Components;

import ru.drobyazko.Events.EntryCancelled;
import ru.drobyazko.Events.EntryPlaced;
import ru.drobyazko.Main;

public class Buffer {
    private int pos = 0;
    private int size = 0;
    private int capacity;
    private Entry[] entryArray;

    public Buffer(int capacity) {
        this.capacity = capacity;
        entryArray = new Entry[capacity];
    }

    public Entry next() {
        int currPos = pos;
        pos = (pos + 1) % capacity;
        return entryArray[currPos];
    }

    public boolean isElementEmpty() {
        return entryArray[pos] == null;
    }

    public boolean isBufferEmpty() {
        return size == 0;
    }

    public void insert(Entry entry, int time) {
        if (entryArray[pos] == null) {
            ++size;
            Main.eventQueue.add(new EntryPlaced(entry, time));
        } else {
            entryArray[pos].setCancelled(true);
            entryArray[pos].setDispatchTime(time);
            entryArray[pos].setExitTime(time);
            Main.eventQueue.add(new EntryCancelled(entryArray[pos], entry, time));
        }
        entryArray[pos] = entry;
    }

    public void removeAtPos(int pos) {
        if (entryArray[pos] != null) {
            --size;
        }
        entryArray[pos] = null;
    }

    public int getPos() {
        return pos;
    }

    public Entry[] getEntryArray() {
        return entryArray;
    }
}
