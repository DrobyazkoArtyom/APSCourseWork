package ru.drobyazko;

import ru.drobyazko.Components.*;
import ru.drobyazko.Events.EntryCompleted;
import ru.drobyazko.Events.EntryGenerated;
import ru.drobyazko.Events.EntryPlaced;
import ru.drobyazko.Events.Event;

import java.util.*;

public class Main {

    private static final int amountOfSources = 5;
    private static final int amountOfEntries = 500; // how many entries each source should generate
    private static final int amountOfDevices = 4;
    private static final int bufferCapacity = 15;
    private static final boolean stepMode = true;
    public static Queue<Event> eventQueue =
            new PriorityQueue<>(amountOfSources * amountOfEntries,
                    Comparator.comparing(Event::getTime).thenComparing(Event::getPriority));

    public static void main(String[] args) {

        List<Source> sourceList = new ArrayList<>();
        for (int i = 0; i < amountOfSources; ++i) {
            sourceList.add(new Source(i));
        }

        for (Source source : sourceList) {
            for (int i = 0; i < amountOfEntries; ++i) {
                eventQueue.add(source.generateEntry());
            }
        }

        Buffer buffer = new Buffer(bufferCapacity);
        BufferManager bufferManager = new BufferManager(buffer);
        DeviceManager deviceManager = new DeviceManager(buffer, amountOfDevices);

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();

            if (stepMode) {
                System.out.println("Time: " + event.getTime());
                System.out.println(event.getMessage());

                for (Source source : sourceList) {
                    if (event instanceof EntryGenerated && source.getId() == event.getEntry().getSourceId()) {
                        Entry entry = event.getEntry();
                        System.out.println("Source " + source.getId() + " generated entry "
                                + entry.getSourceId() + " " + entry.getId() + ".");
                    } else {
                        System.out.println("Source " + source.getId() + " is inactive.");
                    }
                }

                for (int i = 0; i < bufferCapacity; ++i) {
                    Entry[] entryArray = buffer.getEntryArray();
                    if (entryArray[i] == null) {
                        System.out.println("Buffer cell " + i + " is empty.");
                    } else {
                        System.out.println("Buffer cell " + i + " is occupied by entry "
                                + entryArray[i].getSourceId() + " " + entryArray[i].getId() + ".");
                    }
                }

                for (Device device : deviceManager.getDeviceList()) {
                    if (device.isAvailable()) {
                        System.out.println("Device " + device.getId() + " is available.");
                    } else if (event instanceof EntryCompleted && device.getId() == ((EntryCompleted) event).getDeviceId()) {
                        System.out.println("Device " + device.getId() + " finished serving entry "
                                + device.getCurrEntry().getSourceId() + " " + device.getCurrEntry().getId() + ".");
                    } else {
                        System.out.println("Device " + device.getId() + " is serving entry "
                                + device.getCurrEntry().getSourceId() + " " + device.getCurrEntry().getId() + ".");
                    }
                }
                System.out.println();

            }

            if (event instanceof EntryGenerated) {
                bufferManager.placeEntry((EntryGenerated) event);
            } else if (event instanceof EntryCompleted) {
                deviceManager.free((EntryCompleted) event);
                if (!buffer.isBufferEmpty()) {
                    deviceManager.dispatchEntry(event.getTime());
                }
            } else if (event instanceof EntryPlaced && !buffer.isBufferEmpty() && deviceManager.getAvailableDeviceId() != -1) {
                deviceManager.dispatchEntry(event.getTime());
            }
        }

        int totalCancelledEntriesAmount = 0;
        for (int i = 0; i < amountOfSources; ++i) {
            System.out.println("Source " + i);
            List<Entry> sourceEntryList = sourceList.get(i).getEntryList();
            System.out.println("Generated entries amount: " + sourceEntryList.size());

            int cancelledEntriesAmount = 0;
            double averageInSystemTime = 0;
            double averageWaitTime = 0;
            double averageServiceTime = 0;
            for (int j = 0; j < sourceEntryList.size(); ++j) {
                if (sourceEntryList.get(j).isCancelled()) {
                    ++cancelledEntriesAmount;
                } else {
                    averageServiceTime += sourceEntryList.get(j).getExitTime() - sourceEntryList.get(j).getDispatchTime();
                }
                averageInSystemTime += sourceEntryList.get(j).getExitTime() - sourceEntryList.get(j).getEnterTime();
                averageWaitTime += sourceEntryList.get(j).getDispatchTime() - sourceEntryList.get(j).getEnterTime();
            }
            averageInSystemTime = averageInSystemTime / (sourceEntryList.size());
            averageWaitTime = averageWaitTime / (sourceEntryList.size());
            averageServiceTime = averageServiceTime / (sourceEntryList.size() - cancelledEntriesAmount);

            double inSystemTimeDispersion = 0;
            for (int j = 0; j < sourceEntryList.size(); ++j) {
                inSystemTimeDispersion +=
                        (sourceEntryList.get(j).getExitTime() - sourceEntryList.get(j).getEnterTime() - averageInSystemTime) *
                        (sourceEntryList.get(j).getExitTime() - sourceEntryList.get(j).getEnterTime() - averageInSystemTime);
            }
            inSystemTimeDispersion = inSystemTimeDispersion / (sourceEntryList.size() - 1);

            double waitTimeDispersion = 0;
            for (int j = 0; j < sourceEntryList.size(); ++j) {
                waitTimeDispersion +=
                        (sourceEntryList.get(j).getDispatchTime() - sourceEntryList.get(j).getEnterTime() - averageWaitTime) *
                        (sourceEntryList.get(j).getDispatchTime() - sourceEntryList.get(j).getEnterTime() - averageWaitTime);
            }
            waitTimeDispersion = waitTimeDispersion / (sourceEntryList.size() - 1);

            System.out.println("Cancelled entries amount: " + cancelledEntriesAmount);
            System.out.println("Cancelled entry possibility: " + (double) cancelledEntriesAmount / sourceEntryList.size());
            System.out.println("Average in system time: " + averageInSystemTime);
            System.out.println("Average wait time: " + averageWaitTime);
            System.out.println("Average service time: " + averageServiceTime);
            System.out.println("System time dispersion: " + inSystemTimeDispersion);
            System.out.println("Wait time dispersion: " + waitTimeDispersion);
            System.out.println();

            totalCancelledEntriesAmount += cancelledEntriesAmount;
        }

        System.out.println("Total cancelled entry probability: " + (double)totalCancelledEntriesAmount / (amountOfEntries * amountOfSources));
        System.out.println();

        double totalDeviceEfficiency = 0;
        for (Device device : deviceManager.getDeviceList()) {
            System.out.println("Device " + device.getId());
            totalDeviceEfficiency += (double) device.getTotalWorkTime() / device.getLastEventExitTime();
            System.out.println("Efficiency: " + (double) device.getTotalWorkTime() / device.getLastEventExitTime());
        }
        System.out.println();
        System.out.println("Total device efficiency: " + totalDeviceEfficiency / deviceManager.getDeviceList().size());
    }

}
