package org.devrisby.c195.models;

public class FirstLvlDivision {
    private int divisionID;
    private String divisionName;
    private Country country;

    public FirstLvlDivision(int divisionID, String divisionName, Country country) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.country = country;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public Country getCountry() {
        return country;
    }
}
