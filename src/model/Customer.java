package model;
import utils.DBConnection;

import java.sql.*;

public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String formattedAddress;
    private String postalCode;
    private String phone;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String country;
    private String state;

    // constructors
    public Customer() {

    }
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setDivisionId(divisionId);
        setCountry(divisionId);
        setState(divisionId);
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
    public String getCountry() {
        return country;
    }
    public String getState() {
        return state;
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
//    public void setLastUpdate(Timestamp lastUpdate) {
//        this.lastUpdate = lastUpdate;
//    }
//    public void setLastUpdatedBy(String lastUpdatedBy) {
//        this.lastUpdatedBy = lastUpdatedBy;
//    }
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setCountry(int divisionId) {
        try {
            String sql = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID=" + divisionId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if (rs.getInt("COUNTRY_ID") == 1) {
                    this.country = "U.S";
                }
                else if (rs.getInt("COUNTRY_ID") == 2) {
                    this.country = "UK";
                }
                else if (rs.getInt("COUNTRY_ID") == 3) {
                    this.country = "Canada";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void setState(int divisionId) {
        try {
            String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID=" + divisionId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                this.state = rs.getString("Division");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
