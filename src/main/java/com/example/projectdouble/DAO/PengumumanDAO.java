package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Pengumuman;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengumumanDAO {
    /**
     * Menambahkan pengumuman baru ke database.
     * @param pengumuman Objek Pengumuman yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addPengumuman(Pengumuman pengumuman) {
        // Menggunakan nama tabel lowercase 'pengumuman'
        String sql = "INSERT INTO pengumuman (judul, deskripsi, tanggal, lampiran) VALUES (?, ?, ?, ?) RETURNING id_pengumuman";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pengumuman.getJudul());
            stmt.setString(2, pengumuman.getDeskripsi());
            stmt.setTimestamp(3, Timestamp.valueOf(pengumuman.getTanggal())); // Konversi LocalDateTime ke Timestamp
            stmt.setString(4, pengumuman.getLampiran());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pengumuman.setIdPengumuman(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan pengumuman: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua pengumuman dari database.
     * @return List objek Pengumuman.
     */
    public List<Pengumuman> getAllPengumuman() {
        List<Pengumuman> pengumumanList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'pengumuman'
        String sql = "SELECT id_pengumuman, judul, deskripsi, tanggal, lampiran FROM pengumuman ORDER BY tanggal DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pengumumanList.add(new Pengumuman(
                        rs.getInt("id_pengumuman"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getTimestamp("tanggal").toLocalDateTime(), // Konversi Timestamp ke LocalDateTime
                        rs.getString("lampiran")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return pengumumanList;
    }

    /**
     * Mengambil data pengumuman berdasarkan ID.
     * @param idPengumuman ID pengumuman yang dicari.
     * @return Objek Pengumuman jika ditemukan, null jika tidak.
     */
    public Pengumuman getPengumumanById(int idPengumuman) {
        // Menggunakan nama tabel lowercase 'pengumuman'
        String sql = "SELECT id_pengumuman, judul, deskripsi, tanggal, lampiran FROM pengumuman WHERE id_pengumuman = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPengumuman);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pengumuman(
                        rs.getInt("id_pengumuman"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getTimestamp("tanggal").toLocalDateTime(), // Konversi Timestamp ke LocalDateTime
                        rs.getString("lampiran")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil pengumuman berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memperbarui data pengumuman di database.
     * @param pengumuman Objek Pengumuman dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updatePengumuman(Pengumuman pengumuman) {
        // Menggunakan nama tabel lowercase 'pengumuman'
        String sql = "UPDATE pengumuman SET judul = ?, deskripsi = ?, tanggal = ?, lampiran = ? WHERE id_pengumuman = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pengumuman.getJudul());
            stmt.setString(2, pengumuman.getDeskripsi());
            stmt.setTimestamp(3, Timestamp.valueOf(pengumuman.getTanggal()));
            stmt.setString(4, pengumuman.getLampiran());
            stmt.setInt(5, pengumuman.getIdPengumuman());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui pengumuman: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus pengumuman dari database berdasarkan ID.
     * @param idPengumuman ID pengumuman yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deletePengumuman(int idPengumuman) {
        // Menggunakan nama tabel lowercase 'pengumuman'
        String sql = "DELETE FROM pengumuman WHERE id_pengumuman = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPengumuman);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus pengumuman: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
