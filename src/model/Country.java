package model;
import java.sql.Date;
import java.sql.Timestamp;

public class Country {
    private int countryId;
    private String country;

    // constructor
    public Country(int countryId, String country) {
        setCountryId(countryId);
        setCountry(country);
    }

    // getters
    public int getCountryId() {
        return countryId;
    }
    public String getCountry() {
        return country;
    }

    // setters
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
