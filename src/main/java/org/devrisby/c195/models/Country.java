package org.devrisby.c195.models;

public class Country {
    private final int countryID;
    private final String countryName;

    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return this.getCountryName();
    }
}
