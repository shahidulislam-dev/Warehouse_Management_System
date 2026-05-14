package com.warehouse_management.requests;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class EventsRequest {

    private String eventName;
    private LocalDate eventDate;
    private boolean active;

    public EventsRequest() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}