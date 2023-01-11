package org.devrisby.c195.modules.appointment;

import org.devrisby.c195.modules.contact.Contact;
import org.devrisby.c195.modules.customer.Customer;
import org.devrisby.c195.modules.user.User;

import java.time.Instant;

/** Class for Appointment Model */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Instant start;
    private Instant end;
    private Customer customer;
    private Contact contact;
    private User user;

    public Appointment(
            int appointmentID,
            String title,
            String description,
            String location,
            String type,
            Instant start,
            Instant end,
            Customer customer,
            Contact contact,
            User user
    ) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.contact = contact;
        this.user = user;
    }

    public Appointment(
            String title,
            String description,
            String location,
            String type,
            Instant start,
            Instant end,
            Customer customer,
            Contact contact,
            User user
    ) {
        this(-1, title, description, location, type, start, end, customer, contact, user);
    }

    /** Retrieve appointment id */
    public int getAppointmentID() {
        return appointmentID;
    }

    /** Set appointment id */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /** Retrieve title */
    public String getTitle() {
        return title;
    }

    /** Set title */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Get description */
    public String getDescription() {
        return description;
    }

    /** Set description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Get location */
    public String getLocation() {
        return location;
    }

    /** Set location */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Get type */
    public String getType() {
        return type;
    }

    /** Set type */
    public void setType(String type) {
        this.type = type;
    }

    /** Get start */
    public Instant getStart() {
        return start;
    }

    /** Set start */
    public void setStart(Instant start) {
        this.start = start;
    }

    /** Get end */
    public Instant getEnd() {
        return end;
    }

    /** Set end */
    public void setEnd(Instant end) {
        this.end = end;
    }

    /** Get customer */
    public Customer getCustomer() {
        return customer;
    }

    /** Set customer */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /** Get contact */
    public Contact getContact() {
        return contact;
    }

    /** Set contact */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /** Get user */
    public User getUser() {
        return user;
    }

    /** Set user */
    public void setUser(User user) {
        this.user = user;
    }
}
