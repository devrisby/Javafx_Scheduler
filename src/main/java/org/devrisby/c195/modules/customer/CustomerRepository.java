package org.devrisby.c195.modules.customer;

import org.devrisby.c195.data.CrudRepository;
import org.devrisby.c195.modules.country.Country;
import org.devrisby.c195.modules.division.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Class for Customer DB operations */
public class CustomerRepository implements CrudRepository<Customer> {

    private Connection db;

    public CustomerRepository(Connection db) {
        this.db = db;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM customers";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            System.out.println("Error retrieving customers from database!\n" + e.getMessage());
            System.exit(1);
        }

        return 0;
    }

    public void delete (Customer customer) {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, customer.getCustomerID());
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println(customer.getCustomerName() + " successfully deleted!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customers from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public void deleteById (int ID) {
        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, f.Division, f.Division_ID, c.Postal_Code, co.Country_ID, co.Country, c.Phone \n" +
                "FROM customers c\n" +
                "JOIN first_level_divisions f\n" +
                "USING (Division_ID)\n" +
                "JOIN countries co\n" +
                "USING(Country_ID)\n" +
                "WHERE Customer_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Division division = new Division(
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    new Country(rs.getInt("Country_ID"), rs.getString("Country"))
            );

            Customer customer = new Customer(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    division
            );

            delete(customer);
        } catch (SQLException e) {
            System.out.println("Error deleting customer from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM customers";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Deleted " + rowsAffected + " records from Customers table!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customers from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public boolean existsById(int ID){
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error retrieving customer from database!\n" + e.getMessage());
            System.exit(1);
        }

        return false;
    }

    public List<Customer> findAll() {
        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, f.Division, f.Division_ID, c.Postal_Code, co.Country_ID, co.Country, c.Phone \n" +
                "FROM customers c\n" +
                "JOIN first_level_divisions f\n" +
                "USING (Division_ID)\n" +
                "JOIN countries co\n" +
                "USING(Country_ID)";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Customer> customers = new ArrayList<>();

            while(rs.next()) {

                Division division = new Division(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country"))
                );

                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        division
                );

                customers.add(customer);
            }

            return customers;
        } catch (SQLException e) {
            System.out.println("Error retrieving customers from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Customer findById(int ID) {
        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, f.Division, f.Division_ID, c.Postal_Code, co.Country_ID, co.Country, c.Phone \n" +
                "FROM customers c\n" +
                "JOIN first_level_divisions f\n" +
                "USING (Division_ID)\n" +
                "JOIN countries co\n" +
                "USING(Country_ID)" +
                "WHERE Customer_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Division division = new Division(
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    new Country(rs.getInt("Country_ID"), rs.getString("Country"))
            );

            return new Customer(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    division
            );

        } catch (SQLException e) {
            System.out.println("Error retrieving user from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Optional<Customer> findByCustomerName(String customerName) {
        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, f.Division, f.Division_ID, c.Postal_Code, co.Country_ID, co.Country, c.Phone \n" +
                "FROM customers c\n" +
                "JOIN first_level_divisions f\n" +
                "USING (Division_ID)\n" +
                "JOIN countries co\n" +
                "USING(Country_ID)" +
                "WHERE Customer_Name=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Division division = new Division(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        new Country(rs.getInt("Country_ID"), rs.getString("Country"))
                );

                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        division
                );

                return Optional.of(customer);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.out.println("Error finding customer by customer name\n" + e.getMessage());
            return Optional.empty();
        }
    }

    public Customer save(Customer customer) {
        String sql;

        if(customer.getCustomerID() == -1) {
            sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? WHERE Customer_Id=?";
        }

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, customer.getCustomerName());
            ps.setString(2,  customer.getAddress());
            ps.setString(3,  customer.getPostalCode());
            ps.setString(4,  customer.getPhone());
            ps.setInt(5, customer.getDivision().getDivisionID());


            if(customer.getCustomerID() != -1) {
                ps.setInt(6, customer.getCustomerID());
            }

            ps.executeUpdate();
            return findByCustomerName(customer.getCustomerName()).orElse(null);

        } catch (SQLException e) {
            System.out.println("Error saving or updating customer to database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
