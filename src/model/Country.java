package model;

public class Country {
    private int id;
    private String name;

    // constructor
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
