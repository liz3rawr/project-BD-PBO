package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Role;
import com.example.projectdouble.model.User;
import com.example.projectdouble.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Memvalidasi kredensial pengguna dan mengembalikan objek User jika valid.
     * Menggunakan tabel 'users' dan 'role'.
     * @param username Username yang dimasukkan.
     * @param password Password yang dimasukkan.
     * @param roleName Nama role yang dipilih.
     * @return Objek User jika kredensial cocok dan role sesuai, null jika tidak.
     */
    public User authenticateUser(String username, String password, String roleName) {
        String sql = "SELECT u.id_user, u.username, u.password, r.id_role, r.nama_role " +
                "FROM users u JOIN role r ON u.role = r.id_role " + // Menggunakan 'users' dan 'role' (lowercase)
                "WHERE u.username = ? AND u.password = ? AND r.nama_role = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, roleName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_role"), // Menggunakan 'id_role' dari tabel role
                        rs.getString("nama_role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat autentikasi pengguna: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil semua role dari database.
     * Menggunakan tabel 'role'.
     * @return List objek Role.
     */
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id_role, nama_role FROM role"; // Menggunakan 'role' (lowercase)
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(new Role(rs.getInt("id_role"), rs.getString("nama_role")));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil role: " + e.getMessage());
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * Mendapatkan nama siswa berdasarkan NIS.
     * Menggunakan tabel 'siswa'.
     * @param nis NIS siswa.
     * @return Nama siswa jika ditemukan, null jika tidak.
     */
    public String getSiswaNameByNis(String nis) {
        String sql = "SELECT nama FROM siswa WHERE nis = ?"; // Menggunakan 'siswa' (lowercase)
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nama");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama siswa: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan nama guru berdasarkan NIP.
     * Menggunakan tabel 'guru'.
     * @param nip NIP guru.
     * @return Nama guru jika ditemukan, null jika tidak.
     */
    public String getGuruNameByNip(String nip) {
        String sql = "SELECT nama FROM guru WHERE nip = ?"; // Menggunakan 'guru' (lowercase)
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nip);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nama");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama guru: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metode untuk membuat user baru (digunakan oleh Admin saat input siswa/guru baru).
     * Menggunakan tabel 'users'.
     * @param username Username baru.
     * @param password Password baru (belum di-hash di sini).
     * @param roleId ID role (1=admin, 2=guru, 3=siswa).
     * @return ID user yang baru dibuat, atau -1 jika gagal.
     */
    public int createNewUser(String username, String password, int roleId) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?) RETURNING id_user"; // Menggunakan 'users' (lowercase)
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_user");
            }
        } catch (SQLException e) {
            System.err.println("Error saat membuat user baru: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}
