package ru.drobyazko.Components;

import ru.drobyazko.Events.EntryGenerated;

public class BufferManager {
    private final Buffer buffer;

    public BufferManager(Buffer buffer) {
        this.buffer = buffer;
    }

    public void placeEntry(EntryGenerated event) {
        int startPos = buffer.getPos();
        while (!buffer.isElementEmpty()) {
            buffer.next();
            if (buffer.getPos() == startPos) {
                break;
            }
        }
        buffer.insert(event.getEntry(), event.getTime());
        buffer.next();
    }
}
