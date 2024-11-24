package com.railways.railways.domain.client;

import java.awt.*;

public class Client {
    private PrivilegeEnum privilege;
    private final int clientID;
    private final String firstName;
    private final String lastName;
    private int ticketsToBuy;
    private final long timestamp; // New field to store insertion time

    public Client(int clientID, String firstName, String lastName, int ticketsToBuy, PrivilegeEnum privilege) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticketsToBuy = ticketsToBuy;
        this.timestamp = System.currentTimeMillis();
        this.privilege = privilege;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public PrivilegeEnum getPrivilege() {
        return privilege;
    }

    public int getPriority() {
        return privilege.getPriority();
    }

    public int getClientID() {
        return clientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getTicketsToBuy() {
        return ticketsToBuy;
    }

    public void setPrivilege(PrivilegeEnum privilege) {
        this.privilege = privilege;
    }

    public void setTicketsToBuy(int ticketsToBuy) {
        this.ticketsToBuy = ticketsToBuy;
    }

}
