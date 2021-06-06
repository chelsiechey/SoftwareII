package model;

/**
 * Represents a contact
 * @author Chelsie Conrad
 */
public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    // constructor
    public Contact(int contactId, String contactName, String email) {
        setContactId(contactId);
        setContactName(contactName);
        setEmail(email);
    }

    // getters

    /**
     * Gets the contact ID
     * @return Returns the contact's ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Gets the contact name
     * @return Returns the contact's name
     */
    public String getContactName() { return contactName; }

    /**
     * Gets the contact email
     * @return Returns the contact's email
     */
    public String getEmail() { return email; }

    // setters

    /**
     * Sets the contact ID
     * @param contactId The value set to the contact ID
     */
    public void setContactId(int contactId) { this.contactId = contactId; }

    /**
     * Sets the contact name
     * @param contactName The value set to the contact name
     */
    public void setContactName(String contactName) { this.contactName = contactName; }

    /**
     * Sets the contact email
     * @param email The value set to the contact email
     */
    public void setEmail(String email) {this.email = email; }
}
