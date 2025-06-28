package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Rapor;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaporDAO {
    /**
     * Menambahkan rapor baru ke database.
     * @param rapor Objek Rapor yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addRapor(Rapor rapor) {
        String sql = "INSERT INTO rapor (nis, id_tahun_ajaran, tanggal_cetak) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, rapor.getNisSiswa());
            stmt.setInt(2, rapor.getIdTahunAjaran());
            stmt.setDate(3, Date.valueOf(rapor.getTanggalCetak()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rapor.setIdRapor(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan rapor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data rapor di database.
     * @param rapor Objek Rapor dengan data yang diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateRapor(Rapor rapor) {
        String sql = "UPDATE rapor SET nis = ?, id_tahun_ajaran = ?, tanggal_cetak = ? WHERE id_rapor = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rapor.getNisSiswa());
            if (rapor.getIdTahunAjaran() != null) {
                stmt.setInt(2, rapor.getIdTahunAjaran());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setDate(3, Date.valueOf(rapor.getTanggalCetak()));
            stmt.setInt(4, rapor.getIdRapor());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui rapor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus rapor dari database.
     * @param idRapor ID rapor yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteRapor(int idRapor) {
        String sql = "DELETE FROM rapor WHERE id_rapor = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRapor);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus rapor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua data rapor dari database.
     * @return List objek Rapor.
     */
    public List<Rapor> getAllRapor() {
        List<Rapor> raporList = new ArrayList<>();
        String sql = "SELECT r.id_rapor, r.nis, s.nama AS nama_siswa, " +
                "r.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai, r.tanggal_cetak " +
                "FROM rapor r " +
                "JOIN siswa s ON r.nis = s.nis " +
                "JOIN tahun_ajaran ta ON r.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "ORDER BY r.tanggal_cetak DESC, s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                raporList.add(new Rapor(
                        rs.getInt("id_rapor"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap,
                        rs.getDate("tanggal_cetak").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua rapor: " + e.getMessage());
            e.printStackTrace();
        }
        return raporList;
    }

    /**
     * Mengambil data rapor berdasarkan ID.
     * @param idRapor ID rapor yang dicari.
     * @return Objek Rapor jika ditemukan, null jika tidak.
     */
    public Rapor getRaporById(int idRapor) {
        String sql = "SELECT r.id_rapor, r.nis, s.nama AS nama_siswa, " +
                "r.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai, r.tanggal_cetak " +
                "FROM rapor r " +
                "JOIN siswa s ON r.nis = s.nis " +
                "JOIN tahun_ajaran ta ON r.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE r.id_rapor = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRapor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                return new Rapor(
                        rs.getInt("id_rapor"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap,
                        rs.getDate("tanggal_cetak").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil rapor berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil daftar rapor untuk siswa tertentu.
     * @param nis NIS siswa.
     * @return List objek Rapor.
     */
    public List<Rapor> getRaporByNis(String nis) {
        List<Rapor> raporList = new ArrayList<>();
        String sql = "SELECT r.id_rapor, r.nis, r.id_tahun_ajaran, r.tanggal_cetak, " +
                "s.nama AS nama_siswa, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM rapor r " +
                "JOIN siswa s ON r.nis = s.nis " +
                "LEFT JOIN tahun_ajaran ta ON r.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE r.nis = ? ORDER BY r.tanggal_cetak DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idTahunAjaran = (Integer) rs.getObject("id_tahun_ajaran");
                String tahunAjaranLengkap = null;
                if (idTahunAjaran != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                raporList.add(new Rapor(
                        rs.getInt("id_rapor"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        idTahunAjaran,
                        tahunAjaranLengkap,
                        rs.getDate("tanggal_cetak").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil rapor berdasarkan NIS: " + e.getMessage());
            e.printStackTrace();
        }
        return raporList;
    }

    /**
     * Mengambil daftar catatan rapor untuk siswa tertentu pada tahun ajaran tertentu.
     * Berguna untuk mengecek apakah rapor sudah pernah dicetak.
     *
     * @param nis           NIS siswa.
     * @param idTahunAjaran ID Tahun Ajaran.
     * @return List objek Rapor.
     */
    public List<Rapor> getRaporByNisAndTahunAjaran(String nis, int idTahunAjaran) {
        List<Rapor> raporList = new ArrayList<>();
        String sql = "SELECT r.id_rapor, r.nis, s.nama AS nama_siswa, " +
                "r.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai, r.tanggal_cetak " +
                "FROM rapor r " +
                "JOIN siswa s ON r.nis = s.nis " +
                "JOIN tahun_ajaran ta ON r.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE r.nis = ? AND r.id_tahun_ajaran = ? " +
                "ORDER BY r.tanggal_cetak DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                raporList.add(new Rapor(
                        rs.getInt("id_rapor"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap,
                        rs.getDate("tanggal_cetak").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil rapor berdasarkan NIS dan Tahun Ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return raporList;
    }

    // Contoh di RaporDAO.java
    public boolean isRaporPrinted(String nisSiswa, int idTahunAjaran) {
        String sql = "SELECT COUNT(*) FROM rapor WHERE nis = ? AND id_tahun_ajaran = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nisSiswa);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if rapor is printed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
