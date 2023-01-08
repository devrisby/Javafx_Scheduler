package org.devrisby.c195.data;

import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Contact;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.User;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentRepository {

    private Connection db;
    private CustomerRepository customerRepository;
    private ContactRepository contactRepository;
    private UserRepository userRepository;

    public AppointmentRepository(Connection db) {
        this.db = db;
        this.customerRepository = new CustomerRepository(this.db);
        this.contactRepository = new ContactRepository(this.db);
        this.userRepository = new UserRepository(this.db);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM appointments";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getLong(1);
        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return 0;
    }

    public void delete(Appointment appointment) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, appointment.getAppointmentID());
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println(appointment.getTitle() + " successfully deleted!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting appointment from database\n" + e.getMessage());
            System.exit(1);
        }
    }

    public void deleteById(int ID) {
        Appointment appointment = findById(ID);
        delete(appointment);
    }

    public void deleteAll() {
        String sql = "DELETE FROM appointments";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Deleted " + rowsAffected + " records from Appointments table!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting appointments from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public boolean existsById(int ID) {
        String sql = "SELECT * FROM appointments where Appointment_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error retrieving appointment from database!\n" + e.getMessage());
            System.exit(1);
        }

        return false;
    }

    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Appointment> appointments = new ArrayList<>();

            while(rs.next()) {
                Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
                Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
                User user = this.userRepository.findById(rs.getInt("User_ID"));


                appointments.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toInstant(),
                        rs.getTimestamp("End").toInstant(),
                        customer,
                        contact,
                        user
                ));
            }

            return appointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Appointment findById(int ID) {
        String sql = "SELECT * FROM appointments WHERE Appointment_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
            Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
            User user = this.userRepository.findById(rs.getInt("User_ID"));


            return new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getTimestamp("Start").toInstant(),
                    rs.getTimestamp("End").toInstant(),
                    customer,
                    contact,
                    user
            );

        } catch (SQLException e) {
            System.out.println("Error retrieving appointment from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public List<Appointment> findByCurrentMonth() {
        String sql = "SELECT *\n" +
                "FROM appointments\n" +
                "WHERE MONTH(start)=MONTH(now());";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Appointment> appointments = new ArrayList<>();

            while(rs.next()) {
                Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
                Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
                User user = this.userRepository.findById(rs.getInt("User_ID"));


                appointments.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toInstant(),
                        rs.getTimestamp("End").toInstant(),
                        customer,
                        contact,
                        user
                ));
            }

            return appointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public List<Appointment> findByCurrentWeek() {
        String sql = "SELECT *\n" +
                "FROM appointments\n" +
                "WHERE WEEK(start)=WEEK(now());";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Appointment> appointments = new ArrayList<>();

            while(rs.next()) {
                Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
                Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
                User user = this.userRepository.findById(rs.getInt("User_ID"));


                appointments.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toInstant(),
                        rs.getTimestamp("End").toInstant(),
                        customer,
                        contact,
                        user
                ));
            }

            return appointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public List<Appointment> findAllByCustomerId(int customerID) {
        String sql = "SELECT *\n" +
                "FROM appointments\n" +
                "WHERE Customer_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, customerID);

            ResultSet rs = ps.executeQuery();
            List<Appointment> appointments = new ArrayList<>();

            while(rs.next()) {
                Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
                Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
                User user = this.userRepository.findById(rs.getInt("User_ID"));


                appointments.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toInstant(),
                        rs.getTimestamp("End").toInstant(),
                        customer,
                        contact,
                        user
                ));
            }

            return appointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public List<Appointment> findAllByUserId(int userID) {
        String sql = "SELECT *\n" +
                "FROM appointments\n" +
                "WHERE User_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            List<Appointment> appointments = new ArrayList<>();

            while(rs.next()) {
                Customer customer = this.customerRepository.findById(rs.getInt("Customer_ID"));
                Contact contact = this.contactRepository.findById(rs.getInt("Contact_ID"));
                User user = this.userRepository.findById(rs.getInt("User_ID"));


                appointments.add(new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toInstant(),
                        rs.getTimestamp("End").toInstant(),
                        customer,
                        contact,
                        user
                ));
            }

            return appointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }


    public Map<String, Integer> countAppointmentsByContacts(){
        String sql = "SELECT contact_name, COUNT(*)\n" +
                "FROM appointments\n" +
                "JOIN contacts\n" +
                "USING(contact_id)\n" +
                "GROUP BY contact_id;";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Map<String, Integer> contactAppointments = new HashMap<>();

            while(rs.next()) {
                String contactName = rs.getString("contact_name");
                int numOfAppointments = rs.getInt("2");


                contactAppointments.put(contactName, numOfAppointments);
            }

            return contactAppointments;

        } catch (SQLException e) {
            System.out.println("Error retrieving contacts and appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public long countAppointmentsByCountry(int countryId){
        String sql = "SELECT COUNT(*) \n" +
                "FROM appointments\n" +
                "JOIN customers\n" +
                "USING(Customer_ID)\n" +
                "JOIN first_level_divisions\n" +
                "USING(Division_ID)\n" +
                "JOIN countries\n" +
                "USING(Country_ID)\n" +
                "WHERE Country_ID=?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getLong(1);

        } catch (SQLException e) {
            System.out.println("Error retrieving contacts and appointments from database!\n" + e.getMessage());
            System.exit(1);
        }

        return 0;
    }

    public Appointment save(Appointment appointment) {
        String sql;

        if(appointment.getAppointmentID() == -1) {
            sql = "INSERT INTO appointments " +
                    "(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE appointments " +
                    "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, User_ID=?, Contact_ID=? " +
                    "WHERE Appointment_ID = ?";
        }

        try {
            PreparedStatement ps = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.from(appointment.getStart()));
            ps.setTimestamp(6, Timestamp.from(appointment.getEnd()));
            ps.setInt(7, appointment.getCustomer().getCustomerID());
            ps.setInt(8, appointment.getUser().getUserID());
            ps.setInt(9, appointment.getContact().getContactID());


            if(appointment.getAppointmentID() != -1) {
                ps.setInt(10, appointment.getAppointmentID());
            }

            ps.executeUpdate();

            if(appointment.getAppointmentID() == -1) {
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    return findById(rs.getInt(1));
                }
            }

            return findById(appointment.getAppointmentID());

        } catch (SQLException e) {
            System.out.println("Error saving or updating appointment to database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
