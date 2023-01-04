package org.devrisby.c195.data;

import org.devrisby.c195.models.Contact;
import org.devrisby.c195.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository implements ReadRepository<Contact>{

    private Connection db;

    public ContactRepository(Connection db) {
        this.db = db;
    }

    public List<Contact> findAll() {
        String sql = "SELECT * FROM contacts";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Contact> contacts = new ArrayList<>();

            while(rs.next()) {
                contacts.add(
                        new Contact(
                                rs.getInt("Contact_ID"),
                                rs.getString("Contact_Name"),
                                rs.getString("Email")
                        ));
            }

            return contacts;
        } catch (SQLException e) {
            System.out.println("Error retrieving contacts from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Contact findById(int ID){
        String sql = "SELECT * FROM contacts WHERE Contact_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new Contact(
                    rs.getInt("Contact_ID"),
                    rs.getString("Contact_Name"),
                    rs.getString("Email")
            );
        } catch (SQLException e) {
            System.out.println("Error retrieving contact from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Contact findByField(String field) {
        String sql = "SELECT * FROM contacts WHERE Contact_Name=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, field);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new Contact(
                    rs.getInt("Contact_ID"),
                    rs.getString("Contact_Name"),
                    rs.getString("Email")
            );
        } catch (SQLException e) {
            System.out.println("Error retrieving contact from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
