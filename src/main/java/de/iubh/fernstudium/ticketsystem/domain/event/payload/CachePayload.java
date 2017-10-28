package de.iubh.fernstudium.ticketsystem.domain.event.payload;

public class CachePayload<T> {

    public T payload;

    public CachePayload() {
    }

    public CachePayload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
