package model;
import utils.DBDivision;

/**
 * Represents a customer
 * @author Chelsie Conrad
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private String country;
    private String division;
    private String state;

    // constructors
    public Customer() {

    }
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String division) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setDivisionId(divisionId);
        setCountry(divisionId);
        setState(divisionId);
        setDivision(division);
    }

    // getters
    /**
     * Gets the customer ID
     * @return Returns the customer's ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Gets the customer name
     * @return Returns the customer's name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the customer address
     * @return Returns the customer's address
     */
    public String getAddress() { return address; }

    /**
     * Gets the customer postal code
     * @return Returns the customer's postal code
     */
    public String getPostalCode() { return postalCode; }

    /**
     * Gets the customer phone
     * @return Returns the customer's phone number
     */
    public String getPhone() { return phone; }

    /**
     * Gets the customer division ID
     * @return Returns the customer's division ID
     */
    public int getDivisionId() { return divisionId; }

    /**
     * Gets the customer country
     * @return Returns the customer's country
     */
    public String getCountry() { return country; }

    /**
     * Gets the customer state/province
     * @return Returns the customer's state/province
     */
    public String getState() { return state; }

    /**
     * Gets the customer division
     * @return Returns the customer's division
     */
    public String getDivision() {return division; }

    // setters

    /**
     * Sets the customer ID
     * @param customerId The value set to the customer's ID
     */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /**
     * Sets the customer name
     * @param customerName The value set to the customer's name
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * Sets the customer address
     * @param address The value set to the customer's address
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the customer phone
     * @param phone The value set to the customer's phone number
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Sets the customer postal code
     * @param postalCode The value set to the customer's postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     * Sets the customer division
     * @param division The value set to the customer's division
     */
    public void setDivision(String division) {this.division = division; }

    /**
     * Sets the customer division ID
     * @param divisionId The value set to the customer's division ID
     */
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }

    /**
     * Sets the customer country based on their division ID
     * @param divisionId The customer's division ID
     */
    public void setCountry(int divisionId) { this.country = DBDivision.getCountryOfDivisionId(divisionId); }

    /**
     * Sets the customer state based on their division ID
     * @param divisionId The customer's division ID
     */
    public void setState(int divisionId) { this.state = DBDivision.getStateOfDivisionId(divisionId); }
}
