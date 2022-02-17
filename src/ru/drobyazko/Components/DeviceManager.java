package ru.drobyazko.Components;

import ru.drobyazko.Events.EntryCompleted;
import ru.drobyazko.Events.EntryDispatched;
import ru.drobyazko.Main;

import java.util.ArrayList;
import java.util.List;

public class DeviceManager {
    private final Buffer buffer;
    private final List<Device> deviceList;

    public DeviceManager(Buffer buffer, int amountOfDevices) {
        this.buffer = buffer;
        deviceList = new ArrayList<>();
        for (int i = 0; i < amountOfDevices; ++i) {
            deviceList.add(new Device(i));
        }
    }

    public boolean dispatchEntry(int dispatchTime) {
        for (Device device : deviceList) {
            if (device.isAvailable()) {
                Entry[] entryArray = buffer.getEntryArray();
                int pos = 0;
                int minSourceId = Integer.MAX_VALUE;
                int minEntryNumber = Integer.MAX_VALUE;
                for (int i = 0; i < entryArray.length; ++i) {
                    Entry entry = entryArray[i];
                    if (entry != null && (entry.getSourceId() < minSourceId ||
                            (entry.getSourceId() == minSourceId && entry.getId() < minEntryNumber))) {
                        minSourceId = entry.getSourceId();
                        minEntryNumber = entry.getId();
                        pos = i;
                    }
                }
                if (entryArray[pos] == null) {
                    return false;
                }
                Entry entry = entryArray[pos];
                buffer.removeAtPos(pos);
                entry.setDispatchTime(dispatchTime);
                device.solveEntry(entry);
                Main.eventQueue.add(new EntryDispatched(entry, dispatchTime, device.getId()));
                return true;
            }
        }
        return false;
    }

    public int getAvailableDeviceId() {
        for (Device device : deviceList) {
            if (device.isAvailable()) {
                return device.getId();
            }
        }
        return -1;
    }

    public void free(EntryCompleted event) {
        deviceList.get(event.getDeviceId()).free();
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }
}
