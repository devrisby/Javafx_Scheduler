package org.devrisby.c195.data;

import org.devrisby.c195.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<User> {

    private Connection db;

    public UserRepository(Connection db) {
        this.db = db;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM users";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            System.out.println("Error retrieving users from database!\n" + e.getMessage());
            System.exit(1);
        }

        return 0;
    }

    // TODO: handle appointments foreign key constraint
    @Override
    public void delete (User user) {
        String sql = "DELETE FROM users WHERE User_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, user.getUserID());
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println(user.getUserName() + " successfully deleted!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting users from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void deleteById (int ID) {
        String sql = "SELECT * FROM users WHERE User_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password"));
            delete(user);
        } catch (SQLException e) {
            System.out.println("Error deleting user from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Deleted " + rowsAffected + " records from Users table!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting users from database!\n" + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean existsById(int ID){
        String sql = "SELECT * FROM users WHERE User_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error retrieving user from database!\n" + e.getMessage());
            System.exit(1);
        }

        return false;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<>();

            while(rs.next()) {
                users.add(new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password")));
            }

            return users;
        } catch (SQLException e) {
            System.out.println("Error retrieving users from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    @Override
    public User findById(int ID) {
        String sql = "SELECT * FROM users WHERE User_ID = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password"));
        } catch (SQLException e) {
            System.out.println("Error retrieving user from database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public Optional<User> findByUserName(String userName) {
        String sql = "SELECT * FROM users WHERE User_Name = ?";

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return Optional.of(new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password")));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.out.println("Error finding user by username\n" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        String sql;

        if(user.getUserID() == -1) {
            sql = "INSERT INTO users (User_Name, Password) VALUES(?, ?)";
        } else {
            sql = "UPDATE users SET User_Name=?, Password=? WHERE User_Id=?";
        }

        try {
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());

            if(user.getUserID() != -1) {
                ps.setInt(3, user.getUserID());
            }

            ps.executeUpdate();

            return findByUserName(user.getUserName()).orElse(null);

        } catch (SQLException e) {
            System.out.println("Error saving or updating user to database!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
