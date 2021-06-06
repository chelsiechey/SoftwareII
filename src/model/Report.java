package model;

/**
 * Represents a report
 * @author Chelsie Conrad
 */
public class Report {
    private String type;
    private String count;

    // constructor
    public Report(String type, String count) {
        setType(type);
        setCount(count);
    }

    // getters

    /**
     * Gets the report type
     * @return Returns the report type
     */
    public String getType() { return type; }

    /**
     * Gets the report count
     * @return Returns the report count
     */
    public String getCount() { return count; }

    // setters

    /**
     * Sets the report type
     * @param type The value set to the report's type
     */
    public void setType(String type) { this.type = type; }

    /**
     * Sets the report count
     * @param count The value set to the report's count
     */
    public void setCount(String count) { this.count = count; }
}
