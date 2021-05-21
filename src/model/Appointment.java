package model;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int customerId; // customer has many appointments
    private int userId; // user has many appointments
    private int contactId; // contact has many appointments
    private String contact;
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;

    // constructor
    // TO DO
    public Appointment(int customerId, int userId, int contactId, String contact, int appointmentId, String title, String description, String location, String type, String start, String end) {
        setCustomerId(customerId);
        setUserId(userId);
        setContactId(contactId);
        setContact(contact);
        setAppointmentId(appointmentId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
    }

    // getters
    public int getUserId() { return userId; }
    public int getContactId() {
        return contactId;
    }
    public String getContact() { return contact; }
    public int getCustomerId() {
        return customerId;
    }
    public int getAppointmentId() {
        return appointmentId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getType() {
        return type;
    }
    public String getStart() { return start; }
    public String getEnd() { return end; }

    // setters
    private void setUserId(int userId) {
        this.userId = userId;
    }
    private void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public void setContact(String contact) { this.contact = contact; }
    private void setCustomerId(int customerId) { this.customerId = customerId; }
    private void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    private void setTitle(String title) { this.title = title; }
    private void setDescription(String description) { this.description = description; }
    private void setLocation(String location) { this.location = location; }
    private void setType(String type) { this.type = type; }
    private void setStart(String start) { this.start = start; }
    private void setEnd(String end) { this.end = end; }
}
