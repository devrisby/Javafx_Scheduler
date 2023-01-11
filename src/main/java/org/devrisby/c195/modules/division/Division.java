package org.devrisby.c195.modules.division;

import org.devrisby.c195.modules.country.Country;

/** Class for Division Model */
public class Division {
    private final int divisionID;
    private final String divisionName;
    private final Country country;

    public Division(int divisionID, String divisionName, Country country) {
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

    @Override
    public String toString() {
        return this.divisionName;
    }
}
