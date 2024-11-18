package com.railways.railways.simulator.src.hall;

import java.awt.*;

public class Client {
    private PrivilegeEnum privilege;
    private final int clientID;
    private final String firstName;
    private final String lastName;
    private int ticketsToBuy;
    private Point position;

    public Client(int clientID, String firstName, String lastName, int ticketsToBuy, Point position) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticketsToBuy = ticketsToBuy;
        this.position = position;
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

    public Point getPosition() {
        return position;
    }

}
