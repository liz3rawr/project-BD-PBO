package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Guru;
import com.example.projectdouble.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuruDAO {

    private UserDAO userDao = new UserDAO(); // Digunakan untuk operasi terkait user

    /**
     * Menambahkan data guru baru ke database.
     * @param guru Objek Guru yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addGuru(Guru guru) {
        // Menggunakan nama tabel lowercase 'guru'
        String sql = "INSERT INTO guru (nip, nama, jenis_kelamin, email, no_hp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guru.getNip());
            stmt.setString(2, guru.getNama());
            stmt.setString(3, guru.getJenisKelamin());
            stmt.setString(4, guru.getEmail());
            stmt.setString(5, guru.getNoHp());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan data guru: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data guru di database.
     * @param guru Objek Guru dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateGuru(Guru guru) {
        // Menggunakan nama tabel lowercase 'guru'
        String sql = "UPDATE guru SET nama = ?, jenis_kelamin = ?, email = ?, no_hp = ? WHERE nip = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guru.getNama());
            stmt.setString(2, guru.getJenisKelamin());
            stmt.setString(3, guru.getEmail());
            stmt.setString(4, guru.getNoHp());
            stmt.setString(5, guru.getNip());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui data guru: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Guru mapResultSetToGuru(ResultSet rs) throws SQLException {
        Guru guru = new Guru(
                rs.getString("nip"),
                rs.getString("nama"),
                rs.getString("jenis_kelamin"),
                rs.getString("email"),
                rs.getString("no_hp")
        );
        guru.setIdUser((Integer) rs.getObject("id_user"));
        guru.setUsernameUser(rs.getString("username"));
        guru.setPasswordUser(rs.getString("password"));
        return guru;
    }

    /**
     * Mengambil data guru berdasarkan NIP, termasuk detail user jika ada.
     *
     * @param nip NIP guru.
     * @return Objek Guru jika ditemukan, null jika tidak.
     */
    public Guru getGuruByNip(String nip) {
        String sql = "SELECT g.nip, g.nama, g.jenis_kelamin, g.email, g.no_hp, " +
                "u.id_user, u.username, u.password " +
                //", r.id_role, r.nama_role " +
                "FROM guru g " +
                "LEFT JOIN users u ON TRIM(UPPER(g.nip)) = TRIM(UPPER(u.username)) " + // Asumsi username user adalah NIP guru
                //"LEFT JOIN role r ON u.role = r.id_role " +
                "WHERE TRIM(UPPER(g.nip)) = TRIM(UPPER(?))";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nip);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGuru(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil guru berdasarkan NIP: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Mengambil semua data guru.
     *
     * @return List objek Guru.
     */
    public List<Guru> getAllGuru() {
        List<Guru> guruList = new ArrayList<>();
        String sql = "SELECT g.nip, g.nama, g.jenis_kelamin, g.email, g.no_hp, " +
                "u.id_user, u.username, u.password " +
                //", r.id_role, r.nama_role " +
                "FROM guru g " +
                "LEFT JOIN users u ON TRIM(UPPER(g.nip)) = TRIM(UPPER(u.username)) " + // Asumsi username user adalah NIP guru
                //"LEFT JOIN role r ON u.role = r.id_role " +
                "ORDER BY g.nip ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                guruList.add(mapResultSetToGuru(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua guru: " + e.getMessage());
            e.printStackTrace();
        }
        return guruList;
    }

    /**
     * Mencari guru berdasarkan keyword di nama atau NIP.
     * @param keyword Kata kunci pencarian.
     * @return List objek Guru yang cocok.
     */
    public List<Guru> searchGuru(String keyword) {
        List<Guru> guruList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'guru' dan 'users'
        String sql = "SELECT g.nip, g.nama, g.jenis_kelamin, g.email, g.no_hp, u.id_user, u.username, u.password " +
                "FROM guru g LEFT JOIN users u ON TRIM(UPPER(g.nip)) = TRIM(UPPER(u.username)) " +
                "WHERE g.nama ILIKE ? OR g.nip ILIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                guruList.add(mapResultSetToGuru(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari guru: " + e.getMessage());
            e.printStackTrace();
        }
        return guruList;
    }

    /**
     * Menghapus data guru dari database.
     * Ini juga akan menghapus user terkait jika ada.
     * @param nip NIP guru yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteGuru(String nip) {
        Guru guruToDelete = getGuruByNip(nip); // Ambil data guru lengkap
        if (guruToDelete == null) {
            System.err.println("Guru dengan NIP " + nip + " tidak ditemukan untuk dihapus.");
            return false;
        }

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            String sqlUpdateKelas = "UPDATE kelas SET nip_guru = NULL WHERE nip_guru = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sqlUpdateKelas)) {
                stmt.setString(1, nip);
                stmt.executeUpdate();
            }

            String sqlDeletePembina = "DELETE FROM pembina WHERE nip = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sqlDeletePembina)) {
                stmt.setString(1, nip);
                stmt.executeUpdate();
            }

            String sqlDeleteTugas = "UPDATE tugas SET nip_guru = NULL WHERE nip_guru = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sqlDeleteTugas)) {
                stmt.setString(1, nip);
                stmt.executeUpdate();
            }

            String sqlDeleteJadwal = "DELETE FROM jadwal_kelas WHERE nip_guru = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sqlDeleteJadwal)) {
                stmt.setString(1, nip);
                stmt.executeUpdate();
            }

            String sqlDeleteGuru = "DELETE FROM guru WHERE nip = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteGuru)) {
                stmt.setString(1, nip);
                stmt.executeUpdate();
            }

            if (guruToDelete.getIdUser() != null && guruToDelete.getIdUser() != 0) {
                userDao.deleteUser(guruToDelete.getIdUser());
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus guru: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
