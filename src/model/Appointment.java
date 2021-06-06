package model;

/**
 * Represents an appointment
 * @author Chelsie Conrad
 */
public class Appointment {
    private int customerId;
    private int userId;
    private int contactId;
    private String contact;
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;

    // constructors
    public Appointment(int customerId, int userId, int contactId, String contact, int appointmentId, String title, String description, String location, String type, String start, String end, String startTime, String endTime, String startDate, String endDate) {
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
        setStartTime(startTime);
        setEndTime(endTime);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public Appointment(int customerId, int contactId, int appointmentId, String title, String description, String type, String start, String end, String startTime, String endTime, String startDate, String endDate) {
        setCustomerId(customerId);
        setContactId(contactId);
        setAppointmentId(appointmentId);
        setTitle(title);
        setDescription(description);
        setType(type);
        setStart(start);
        setEnd(end);
        setStartTime(startTime);
        setEndTime(endTime);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    // getters

    /**
     * Gets the user ID
     * @return Returns the user's ID
     */
    public int getUserId() { return userId; }

    /**
     * Gets the contact ID
     * @return Returns the contact's ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Gets the contact name
     * @return Returns the contact name
     */
    public String getContact() { return contact; }

    /**
     * Gets the customer ID
     * @return Returns the customer's ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets the appointment ID
     * @return Returns the appointment's ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Gets the appointment title
     * @return Returns the appointment's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the appointment description
     * @return Returns the appointment's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the appointment location
     * @return Returns the appointment's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the appointment type
     * @return Returns the appointment's type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the appointment start
     * @return Returns the appointment's start
     */
    public String getStart() { return start; }

    /**
     * Gets the appointment end
     * @return Returns the appointment's end
     */
    public String getEnd() { return end; }

    /**
     * Gets the appointment start time
     * @return Returns the appointment's start time
     */
    public String getStartTime() { return startTime; }

    /**
     * Gets the appointment end time
     * @return Returns the appointment's end time
     */
    public String getEndTime() { return endTime; }

    /**
     * Gets the appointment start date
     * @return Returns the appointment's start date
     */
    public String getStartDate() { return startDate; }

    /**
     * Gets the appointment end date
     * @return Returns the appointment's end date
     */
    public String getEndDate() { return endDate; }

    // setters

    /**
     * Sets the user ID
     * @param userId The value set to the user's ID
     */
    private void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the contact ID
     * @param contactId The value set to the contact's ID
     */
    private void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Sets the contact name
     * @param contact The value set for the contact's name
     */
    public void setContact(String contact) { this.contact = contact; }

    /**
     * Sets the customer ID
     * @param customerId The value set to the customer's ID
     */
    private void setCustomerId(int customerId) { this.customerId = customerId; }

    /**
     * Sets the appointment ID
     * @param appointmentId The value set to the appointment's ID
     */
    private void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    /**
     * Sets the appointment title
     * @param title The value set to the appointment's title
     */
    private void setTitle(String title) { this.title = title; }

    /**
     * Sets the appointment description
     * @param description The value set to the appointment's description
     */
    private void setDescription(String description) { this.description = description; }

    /**
     * Sets the appointment location
     * @param location The value set to the appointment's location
     */
    private void setLocation(String location) { this.location = location; }

    /**
     * Sets the appointment type
     * @param type The value set to the appointment's type
     */
    private void setType(String type) { this.type = type; }

    /**
     * Sets the appointment start
     * @param start The value set to the appointment's start
     */
    private void setStart(String start) { this.start = start; }

    /**
     * Sets the appointment end
     * @param end The value set to the appointment's end
     */
    private void setEnd(String end) { this.end = end; }

    /**
     * Sets the appointment's start time
     * @param startTime The appointment's start time
     */
    private void setStartTime(String startTime) { this.startTime = startTime; }

    /**
     * Sets the appointment's end time
     * @param endTime The appointment's end time
     */
    private void setEndTime(String endTime) { this.endTime = endTime; }

    /**
     * Sets the appointment's start date
     * @param startDate The appointment's start date
     */
    private void setStartDate(String startDate) { this.startDate = startDate; }

    /**
     * Sets the appointment's end date
     * @param endDate The appointment's end date
     */
    private void setEndDate(String endDate) { this.endDate = endDate; }
}
