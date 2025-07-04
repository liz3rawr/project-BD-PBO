package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Role;
import com.example.projectdouble.model.User;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int createNewUser(String username, String password, int roleId) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, roleId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat membuat user baru: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedId;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id_user = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id_user = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui password user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT u.id_user, u.username, u.password, r.id_role, r.nama_role " +
                "FROM users u JOIN role r ON u.role = r.id_role " +
                "WHERE u.username = ? AND u.password = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role userRole = new Role(rs.getInt("id_role"), rs.getString("nama_role"));
                return new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), userRole);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        String sql = "SELECT u.id_user, u.username, u.password, r.id_role, r.nama_role " +
                "FROM users u JOIN role r ON u.role = r.id_role " +
                "WHERE u.id_user = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role userRole = new Role(rs.getInt("id_role"), rs.getString("nama_role"));
                return new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), userRole);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil user berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id_role, nama_role FROM role";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id_role"), rs.getString("nama_role")));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua role: " + e.getMessage());
            e.printStackTrace();
        }
        return roles;
    }

    public boolean updateUsername(int userId, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id_user = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui username user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String password, String roleName) {
        String sql = "SELECT u.id_user, u.username, u.password, r.id_role, r.nama_role " +
                "FROM users u JOIN role r ON u.role = r.id_role " +
                "WHERE TRIM(UPPER(u.username)) = TRIM(UPPER(?)) AND u.password = ? AND r.nama_role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, roleName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role userRole = new Role(rs.getInt("id_role"), rs.getString("nama_role"));
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        userRole
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat autentikasi pengguna: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getGuruNameByNip(String nip) {
        String sql = "SELECT nama FROM guru WHERE TRIM(UPPER(nip)) = TRIM(UPPER(?))";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nip);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nama");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama guru berdasarkan NIP: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getSiswaNameByNis(String nis) {
        String sql = "SELECT nama FROM siswa WHERE TRIM(UPPER(nis)) = TRIM(UPPER(?))";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nama");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama siswa berdasarkan NIS: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
