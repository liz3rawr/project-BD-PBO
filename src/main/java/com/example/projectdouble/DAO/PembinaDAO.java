package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Guru;
import com.example.projectdouble.model.Pembina;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembinaDAO {

    /**
     * Menambahkan pembina baru ke database.
     * @param pembina Objek Pembina yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addPembina(Pembina pembina) {
        // Menggunakan nama tabel lowercase 'pembina'
        String sql = "INSERT INTO pembina (nip, id_ekstrakurikuler) VALUES (?, ?) RETURNING id_pembina";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pembina.getNipGuru()); // DDL Anda menggunakan 'nip' bukan 'nip_guru' di tabel pembina
            stmt.setInt(2, pembina.getIdEkstrakurikuler());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pembina.setIdPembina(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan pembina: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua pembina dari database.
     * @return List objek Pembina.
     */
    public List<Pembina> getAllPembina() {
        List<Pembina> pembinaList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'pembina', 'guru', 'ekstrakurikuler'
        String sql = "SELECT p.id_pembina, p.nip AS nip_guru, g.nama AS nama_guru, p.id_ekstrakurikuler, e.nama AS nama_ekstrakurikuler " +
                "FROM pembina p " +
                "JOIN guru g ON p.nip = g.nip " + // DDL Anda menggunakan 'nip' sebagai FK
                "JOIN ekstrakurikuler e ON p.id_ekstrakurikuler = e.id_ekstrakurikuler ORDER BY g.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pembinaList.add(new Pembina(
                        rs.getInt("id_pembina"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama_ekstrakurikuler")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua pembina: " + e.getMessage());
            e.printStackTrace();
        }
        return pembinaList;
    }

    /**
     * Mengambil pembina berdasarkan ID.
     * @param idPembina ID pembina.
     * @return Objek Pembina jika ditemukan, null jika tidak.
     */
    public Pembina getPembinaById(int idPembina) {
        // Menggunakan nama tabel lowercase 'pembina', 'guru', 'ekstrakurikuler'
        String sql = "SELECT p.id_pembina, p.nip AS nip_guru, g.nama AS nama_guru, p.id_ekstrakurikuler, e.nama AS nama_ekstrakurikuler " +
                "FROM pembina p " +
                "JOIN guru g ON p.nip = g.nip " +
                "JOIN ekstrakurikuler e ON p.id_ekstrakurikuler = e.id_ekstrakurikuler " +
                "WHERE p.id_pembina = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPembina);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pembina(
                        rs.getInt("id_pembina"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama_ekstrakurikuler")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil pembina berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memperbarui data pembina di database.
     * @param pembina Objek Pembina dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updatePembina(Pembina pembina) {
        // Menggunakan nama tabel lowercase 'pembina'
        String sql = "UPDATE pembina SET nip = ?, id_ekstrakurikuler = ? WHERE id_pembina = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pembina.getNipGuru()); // DDL Anda menggunakan 'nip'
            stmt.setInt(2, pembina.getIdEkstrakurikuler());
            stmt.setInt(3, pembina.getIdPembina());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui pembina: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus pembina dari database berdasarkan ID.
     * @param idPembina ID pembina yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deletePembina(int idPembina) {
        // Menggunakan nama tabel lowercase 'pembina'
        String sql = "DELETE FROM pembina WHERE id_pembina = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPembina);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus pembina: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Di dalam class PembinaDAO { ... }

    /**
     * Mengambil daftar guru yang merupakan pembina untuk ekstrakurikuler tertentu.
     * @param idEkstrakurikuler ID ekstrakurikuler.
     * @return List objek Guru.
     */
    public List<Guru> getMentorsByEkstrakurikuler(int idEkstrakurikuler) {
        List<Guru> mentorList = new ArrayList<>();
        String sql = "SELECT g.nip, g.nama, g.jenis_kelamin, g.email, g.no_hp, u.id_user, u.username, u.password " +
                "FROM pembina p " +
                "JOIN guru g ON p.nip = g.nip " +
                "LEFT JOIN users u ON g.nip = u.username " + // Join ke tabel users untuk ambil info user
                "WHERE p.id_ekstrakurikuler = ? ORDER BY g.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mentorList.add(new Guru(
                        rs.getString("nip"),
                        //(Integer) rs.getObject("id_user"),
                        rs.getString("nama"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("email"),
                        rs.getString("no_hp")
//                        ,
//                        rs.getString("username"),
//                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil mentor berdasarkan ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
        }
        return mentorList;
    }

    /**
     * Menghapus semua entri pembina untuk ekstrakurikuler tertentu.
     * @param idEkstrakurikuler ID ekstrakurikuler.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deletePembinaByEkstrakurikulerId(int idEkstrakurikuler) {
        String sql = "DELETE FROM pembina WHERE id_ekstrakurikuler = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus pembina berdasarkan ID ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
