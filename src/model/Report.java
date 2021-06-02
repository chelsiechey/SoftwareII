package model;

public class Report {
    private String type;
    private String count;
    public Report(String type, String count) {
        setType(type);
        setCount(count);
    }
    // getters
    public String getType() {
        return type;
    }
    public String getCount() { return count; }

    // setters
    public void setType(String type) { this.type = type; }
    public void setCount(String count) { this.count = count; }
}
