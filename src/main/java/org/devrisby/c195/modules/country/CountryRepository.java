package org.devrisby.c195.modules.country;

import org.devrisby.c195.data.ReadRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Class for Country DB operations */
public class CountryRepository implements ReadRepository<Country> {

    private Connection db;

    public CountryRepository(Connection db) {
        this.db = db;
    }

    public List<Country> findAll(){
        String sql = "SELECT * FROM countries";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Country> countries = new ArrayList<>();

            while(rs.next()) {
                countries.add(new Country(rs.getInt("Country_ID"), rs.getString("Country")));
            }

            return countries;
        } catch (SQLException e) {
            System.out.println("Error retrieving countries from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Country findById(int ID){
        String sql = "SELECT * FROM countries WHERE Country_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new Country(rs.getInt("Country_ID"), rs.getString("Country"));
        } catch (SQLException e) {
            System.out.println("Error retrieving country from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Country findByField(String field) {
        String sql = "SELECT * FROM countries WHERE Country=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, field);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new Country(rs.getInt("Country_ID"), rs.getString("Country"));
        } catch (SQLException e) {
            System.out.println("Error retrieving country from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
