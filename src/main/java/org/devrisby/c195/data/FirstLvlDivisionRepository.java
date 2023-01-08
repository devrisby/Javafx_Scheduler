package org.devrisby.c195.data;

import org.devrisby.c195.models.Country;
import org.devrisby.c195.models.FirstLvlDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FirstLvlDivisionRepository implements ReadRepository<FirstLvlDivision> {

    private Connection db;

    public FirstLvlDivisionRepository(Connection db) {
        this.db = db;
    }

    public List<FirstLvlDivision> findAll() {
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<FirstLvlDivision> firstLvlDivisions = new ArrayList<>();

            while(rs.next()) {
                firstLvlDivisions.add(new FirstLvlDivision(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country")))
                );
            }

            return firstLvlDivisions;
        } catch (SQLException e) {
            System.out.println("Error retrieving first level divisions from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public FirstLvlDivision findById(int ID){
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)" +
                "WHERE Division_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Country country = new Country(rs.getInt("Country_ID"), rs.getString("Country"));

            return new FirstLvlDivision(
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    country
            );

        } catch (SQLException e) {
            System.out.println("Error retrieving first level division from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public FirstLvlDivision findByField(String field) {
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)" +
                "WHERE Division=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, field);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Country country = new Country(rs.getInt("Country_ID"), rs.getString("Country"));

            return new FirstLvlDivision(
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    country
            );

        } catch (SQLException e) {
            System.out.println("Error retrieving first level division from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public List<FirstLvlDivision> findByCountryId(int countryId) {
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)" +
                "WHERE Country_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();

            List<FirstLvlDivision> firstLvlDivisions = new ArrayList<>();

            while(rs.next()) {
                firstLvlDivisions.add(new FirstLvlDivision (
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country"))
                ));
            }

            return firstLvlDivisions;

        } catch (SQLException e) {
            System.out.println("Error retrieving first level division from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
