package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Kelas;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO {

    public boolean addKelas(Kelas kelas) {
        String sql = "INSERT INTO kelas (nama_kelas, tingkat, nip_guru, id_tahun_ajaran) VALUES (?, ?, ?, ?) RETURNING id_kelas";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kelas.getNamaKelas());
            stmt.setString(2, kelas.getTingkat());
            stmt.setString(3, kelas.getNipWaliKelas());
            stmt.setInt(4, kelas.getIdTahunAjaran());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        kelas.setIdKelas(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan kelas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Kelas> getAllKelas() {
        List<Kelas> kelasList = new ArrayList<>();
        String sql = "SELECT k.id_kelas, k.nama_kelas, k.tingkat, k.nip_guru, g.nama AS nama_wali_kelas, " +
                "k.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM kelas k " +
                "LEFT JOIN guru g ON k.nip_guru = g.nip " +
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "ORDER BY k.nama_kelas, ta.tahun_mulai DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                kelasList.add(new Kelas(
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getString("tingkat"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_wali_kelas"),
                        rs.getInt("id_tahun_ajaran"),
                        rs.getString("tahun_ajaran_lengkap")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua kelas: " + e.getMessage());
            e.printStackTrace();
        }
        return kelasList;
    }

    public Kelas getKelasById(int idKelas) {
        String sql = "SELECT k.id_kelas, k.nama_kelas, k.tingkat, k.nip_guru, g.nama AS nama_wali_kelas, " +
                "k.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM kelas k " +
                "LEFT JOIN guru g ON k.nip_guru = g.nip " +
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE k.id_kelas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Kelas(
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getString("tingkat"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_wali_kelas"),
                        rs.getInt("id_tahun_ajaran"),
                        rs.getString("tahun_ajaran_lengkap")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil kelas berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateKelas(Kelas kelas) {
        String sql = "UPDATE kelas SET nama_kelas = ?, tingkat = ?, nip_guru = ?, id_tahun_ajaran = ? WHERE id_kelas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kelas.getNamaKelas());
            stmt.setString(2, kelas.getTingkat());
            stmt.setString(3, kelas.getNipWaliKelas());
            stmt.setInt(4, kelas.getIdTahunAjaran());
            stmt.setInt(5, kelas.getIdKelas());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui kelas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKelas(int idKelas) {
        String sqlUpdateSiswa = "UPDATE siswa SET id_kelas = NULL, id_tahun_ajaran = NULL, semester = NULL WHERE id_kelas = ?";
        String sqlDeleteJadwalKelas = "DELETE FROM jadwal_kelas WHERE id_kelas = ?";
        String sqlDeleteKelas = "DELETE FROM kelas WHERE id_kelas = ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUpdateSiswa = conn.prepareStatement(sqlUpdateSiswa)) {
                stmtUpdateSiswa.setInt(1, idKelas);
                stmtUpdateSiswa.executeUpdate();
            }

            try (PreparedStatement stmtJadwalKelas = conn.prepareStatement(sqlDeleteJadwalKelas)) {
                stmtJadwalKelas.setInt(1, idKelas);
                stmtJadwalKelas.executeUpdate();
            }

            try (PreparedStatement stmtKelas = conn.prepareStatement(sqlDeleteKelas)) {
                stmtKelas.setInt(1, idKelas);
                int rowsAffected = stmtKelas.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus kelas: " + e.getMessage());
            e.printStackTrace();
            try (Connection conn = DBConnect.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error saat rollback transaksi: " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    public boolean isClassNameExistsForYear(String namaKelas, int idTahunAjaran) {
        String sql = "SELECT COUNT(*) FROM kelas WHERE nama_kelas = ? AND id_tahun_ajaran = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaKelas);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengecek duplikasi nama kelas: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isClassNameExistsForYearExcludingSelf(String namaKelas, int idTahunAjaran, int excludeIdKelas) {
        String sql = "SELECT COUNT(*) FROM kelas WHERE nama_kelas = ? AND id_tahun_ajaran = ? AND id_kelas != ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaKelas);
            stmt.setInt(2, idTahunAjaran);
            stmt.setInt(3, excludeIdKelas);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengecek duplikasi nama kelas (eksklusif): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Kelas getKelasByNipAndTahunAjaran(String nipGuru, int idTahunAjaran) {
        String sql = "SELECT k.id_kelas, k.nama_kelas, k.tingkat, k.nip_guru, g.nama AS nama_wali_kelas, " +
                "k.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM kelas k " +
                "LEFT JOIN guru g ON k.nip_guru = g.nip " +
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE k.nip_guru = ? AND k.id_tahun_ajaran = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nipGuru);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Kelas(
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getString("tingkat"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_wali_kelas"),
                        rs.getInt("id_tahun_ajaran"),
                        rs.getString("tahun_ajaran_lengkap")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil kelas berdasarkan NIP guru dan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
