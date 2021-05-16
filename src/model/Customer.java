package model;
import java.sql.Date;
import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;

    // constructors
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setDivisionId(divisionId);
    }

//    public Customer(int customerId, String customerName) {
//        setCustomerId(customerId);
//        setCustomerName(customerName);
//    }
//    public Customer(int customerId, String customerName, String address, String postalCode, String phone, Timestamp lastUpdate, String lastUpdatedBy) {
//        setCustomerId(customerId);
//        setCustomerName(customerName);
//        setAddress(address);
//        setPostalCode(postalCode);
//        setPhone(phone);
//        setLastUpdate(lastUpdate);
//        setLastUpdatedBy(lastUpdatedBy);
//    }
    // getters
    public int getCustomerId() {
        return customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getAddress() {
        return address;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getPhone() {
        return phone;
    }
    public int getDivisionId() {
        return divisionId;
    }
    // setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
