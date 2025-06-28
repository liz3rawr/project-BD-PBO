package com.example.projectdouble.DAO;

import com.example.projectdouble.model.MataPelajaran;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MataPelajaranDAO {

    /**
     * Menambahkan mata pelajaran baru ke database.
     * @param mataPelajaran Objek MataPelajaran yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addMataPelajaran(MataPelajaran mataPelajaran) {
        // Menggunakan nama tabel lowercase 'mata_pelajaran'
        String sql = "INSERT INTO mata_pelajaran (nama_mapel, jenjang_kelas) VALUES (?, ?) RETURNING id_mapel";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mataPelajaran.getNamaMapel());
            stmt.setString(2, mataPelajaran.getJenjangKelas());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mataPelajaran.setIdMapel(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan mata pelajaran: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua mata pelajaran dari database.
     * @return List objek MataPelajaran.
     */
    public List<MataPelajaran> getAllMataPelajaran() {
        List<MataPelajaran> mataPelajaranList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'mata_pelajaran'
        String sql = "SELECT id_mapel, nama_mapel, jenjang_kelas FROM mata_pelajaran ORDER BY nama_mapel";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mataPelajaranList.add(new MataPelajaran(
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("jenjang_kelas")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua mata pelajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return mataPelajaranList;
    }

    /**
     * Mengambil mata pelajaran berdasarkan ID.
     * @param idMapel ID mata pelajaran.
     * @return Objek MataPelajaran jika ditemukan, null jika tidak.
     */
    public MataPelajaran getMataPelajaranById(int idMapel) {
        // Menggunakan nama tabel lowercase 'mata_pelajaran'
        String sql = "SELECT id_mapel, nama_mapel, jenjang_kelas FROM mata_pelajaran WHERE id_mapel = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMapel);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MataPelajaran(
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("jenjang_kelas")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil mata pelajaran berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memperbarui data mata pelajaran di database.
     * @param mataPelajaran Objek MataPelajaran dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateMataPelajaran(MataPelajaran mataPelajaran) {
        // Menggunakan nama tabel lowercase 'mata_pelajaran'
        String sql = "UPDATE mata_pelajaran SET nama_mapel = ?, jenjang_kelas = ? WHERE id_mapel = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mataPelajaran.getNamaMapel());
            stmt.setString(2, mataPelajaran.getJenjangKelas());
            stmt.setInt(3, mataPelajaran.getIdMapel());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui mata pelajaran: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus mata pelajaran dari database berdasarkan ID.
     * @param idMapel ID mata pelajaran yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteMataPelajaran(int idMapel) {
        // Menggunakan nama tabel lowercase 'mata_pelajaran'
        String sql = "DELETE FROM mata_pelajaran WHERE id_mapel = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMapel);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus mata pelajaran: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
