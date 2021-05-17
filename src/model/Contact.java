package model;

public class Contact {
    private static int contactId;
    private String contactName;
    private String email;
    // constructor
    public Contact(int contactId, String contactName, String email) {
        setContactId(contactId);
        setContactName(contactName);
        setEmail(email);
    }

    // getters
    public static int getContactId() {
        return contactId;
    }
    public String getContactName() {
        return contactName;
    }
    public String getEmail() { return email; }

    // setters
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public void setEmail(String email) {this.email = email; }
}
