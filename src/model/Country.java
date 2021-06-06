package model;

/**
 * Represents a country
 * @author Chelsie Conrad
 */
public class Country {
    private int countryId;
    private String country;

    // constructor
    public Country(int countryId, String country) {
        setCountryId(countryId);
        setCountry(country);
    }

    // getters
    /**
     * Gets the country ID
     * @return Returns the country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Gets the country name
     * @return Returns the country name
     */
    public String getCountry() {
        return country;
    }

    // setters
    /**
     * Sets the country ID
     * @param countryId The value set to the country's ID
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Sets the country name
     * @param country The value set to the country's name
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
