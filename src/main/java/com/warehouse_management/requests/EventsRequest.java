package com.warehouse_management.requests;

import java.time.LocalDateTime;

public class EventsRequest {

    private String eventName;
    private LocalDateTime eventDate;
    private boolean active;

    public EventsRequest() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}