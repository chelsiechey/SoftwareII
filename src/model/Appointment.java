package model;
import java.sql.Date;
import java.sql.Timestamp;

public class Appointment {
    private int customerId; // customer has many appointments
    private int userId; // user has many appointments
    private int contactId; // contact has many appointments
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    // constructor
    // TO DO
    public Appointment(int customerId, int userId, int contactId, int appointmentId, String title, String description, String location, String type, Timestamp start, Timestamp end) {
        setCustomerId(customerId);
        setUserId(userId);
        setContactId(contactId);
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
    public Timestamp getStart() { return start; }
    public Timestamp getEnd() { return end; }

    // setters
    private void setUserId(int userId) {
        this.userId = userId;
    }
    private void setContactId(int contactId) {
        this.contactId = contactId;
    }
    private void setCustomerId(int customerId) { this.customerId = customerId; }
    private void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    private void setTitle(String title) { this.title = title; }
    private void setDescription(String description) { this.description = description; }
    private void setLocation(String location) { this.location = location; }
    private void setType(String type) { this.type = type; }
    private void setStart(Timestamp start) {this.start = start; }
    private void setEnd(Timestamp end) {this.end = end; }
}
