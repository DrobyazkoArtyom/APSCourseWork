package ru.drobyazko.Components;

import ru.drobyazko.Events.EntryCompleted;
import ru.drobyazko.Main;

public class Device {
    private static final double alpha = 1.0;
    private static final double beta = 3.5;
    private final int id;
    private boolean isAvailable = true;
    private Entry currEntry;
    private int totalWorkTime = 0;
    private int lastEventExitTime = 0;

    public Device(int id) {
        this.id = id;
    }

    public void solveEntry(Entry entry) {
        isAvailable = false;
        int workTime = (int) ((beta - alpha) * Math.random() + alpha);
        totalWorkTime += workTime;
        int exitTime = entry.getDispatchTime() + workTime;
        entry.setExitTime(exitTime);
        currEntry = entry;
        lastEventExitTime = exitTime;
        Main.eventQueue.add(new EntryCompleted(entry, exitTime, id));
    }

    public void free() {
        isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getId() {
        return id;
    }

    public Entry getCurrEntry() {
        return currEntry;
    }

    public int getTotalWorkTime() {
        return totalWorkTime;
    }

    public int getLastEventExitTime() {
        return lastEventExitTime;
    }
}
