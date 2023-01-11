package org.devrisby.c195.modules.division;

import org.devrisby.c195.data.ReadRepository;
import org.devrisby.c195.modules.country.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Class for Division DB operations */
public class DivisionRepository implements ReadRepository<Division> {

    private final Connection db;

    public DivisionRepository(Connection db) {
        this.db = db;
    }

    public List<Division> findAll() {
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Division> divisions = new ArrayList<>();

            while(rs.next()) {
                divisions.add(new Division(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country")))
                );
            }

            return divisions;
        } catch (SQLException e) {
            System.out.println("Error retrieving first level divisions from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Division findById(int ID){
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

            return new Division(
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

    public Division findByField(String field) {
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

            return new Division(
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

    public List<Division> findByCountryId(int countryId) {
        String sql = "SELECT Division_ID, Division, c.Country_ID, c.Country\n" +
                "FROM first_level_divisions f\n" +
                "JOIN countries c\n" +
                "USING(Country_ID)" +
                "WHERE Country_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();

            List<Division> divisions = new ArrayList<>();

            while(rs.next()) {
                divisions.add(new Division(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country"))
                ));
            }

            return divisions;

        } catch (SQLException e) {
            System.out.println("Error retrieving first level division from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
