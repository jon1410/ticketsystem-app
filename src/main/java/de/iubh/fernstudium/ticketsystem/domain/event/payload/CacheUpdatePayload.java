package de.iubh.fernstudium.ticketsystem.domain.event.payload;

public class CacheUpdatePayload {

    private String userName;
    private String newFirstName;
    private String newLastName;

    public CacheUpdatePayload() {
    }

    public CacheUpdatePayload(String newFirstName, String newLastName) {
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }
}
