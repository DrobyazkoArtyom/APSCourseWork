package ru.drobyazko.Components;

public class Entry {
    private final int sourceId;
    private final int id;
    private final int enterTime;
    private boolean isCancelled = false;
    private int exitTime;
    private int dispatchTime;

    public Entry(int time, int id, int sourceId) {
        this.enterTime = time;
        this.id = id;
        this.sourceId = sourceId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getId() {
        return id;
    }

    public int getEnterTime() {
        return enterTime;
    }

    public int getExitTime() {
        return exitTime;
    }

    public int getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(int dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
