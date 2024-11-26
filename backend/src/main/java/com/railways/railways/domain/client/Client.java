package com.railways.railways.domain.client;

import java.awt.*;

/**
 * Represents a client with personal details, ticket information, and privilege level.
 * The client also has a timestamp indicating when the client was created.
 */
public class Client {
    // Client privilege level
    private PrivilegeEnum privilege;
    // Unique client identifier
    private final int clientID;
    // Client's first name
    private final String firstName;
    // Client's last name
    private final String lastName;
    // Number of tickets the client wants to buy
    private int ticketsToBuy;
    // Timestamp indicating when the client was created (in milliseconds)
    private final long timestamp;

    /**
     * Constructs a Client object with the provided details.
     *
     * @param clientID the unique identifier for the client
     * @param firstName the client's first name
     * @param lastName the client's last name
     * @param ticketsToBuy the number of tickets the client wants to buy
     * @param privilege the client's privilege level
     */
    public Client(int clientID, String firstName, String lastName, int ticketsToBuy, PrivilegeEnum privilege) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticketsToBuy = ticketsToBuy;
        this.timestamp = System.currentTimeMillis();
        this.privilege = privilege;
    }

    /**
     * Gets the timestamp indicating when the client was created.
     *
     * @return the timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the privilege level of the client.
     *
     * @return the client's privilege enum
     */
    public PrivilegeEnum getPrivilege() {
        return privilege;
    }

    /**
     * Gets the priority of the client's privilege level.
     * The priority is derived from the client's privilege.
     *
     * @return the privilege priority
     */
    public int getPriority() {
        return privilege.getPriority();
    }

    /**
     * Gets the unique client identifier.
     *
     * @return the client ID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Gets the client's first name.
     *
     * @return the first name of the client
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the client's last name.
     *
     * @return the last name of the client
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the full name of the client (first and last name combined).
     *
     * @return the full name of the client
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the number of tickets the client wants to buy.
     *
     * @return the number of tickets
     */
    public int getTicketsToBuy() {
        return ticketsToBuy;
    }

    /**
     * Sets the privilege level for the client.
     *
     * @param privilege the new privilege level
     */
    public void setPrivilege(PrivilegeEnum privilege) {
        this.privilege = privilege;
    }

    /**
     * Sets the number of tickets the client wants to buy.
     *
     * @param ticketsToBuy the new number of tickets
     */
    public void setTicketsToBuy(int ticketsToBuy) {
        this.ticketsToBuy = ticketsToBuy;
    }

}
