package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Presensi;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresensiDAO {

    /**
     * Menambahkan data presensi baru ke database.
     * Sekarang menyimpan id_kelas langsung.
     *
     * @param presensi Objek Presensi yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addPresensi(Presensi presensi) {
        String sql = "INSERT INTO presensi (tanggal, status, nis, id_kelas) VALUES (?, ?, ?, ?) RETURNING id_presensi";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(presensi.getTanggal()));
            stmt.setString(2, presensi.getStatus());
            stmt.setString(3, presensi.getNisSiswa());
            stmt.setObject(4, presensi.getIdKelas(), java.sql.Types.INTEGER); // Menggunakan id_kelas langsung (bisa NULL)

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        presensi.setIdPresensi(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan presensi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui informasi presensi yang sudah ada di database.
     *
     * @param presensi Objek Presensi dengan informasi yang diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updatePresensi(Presensi presensi) {
        String sql = "UPDATE presensi SET tanggal = ?, status = ?, nis = ?, id_kelas = ? WHERE id_presensi = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(presensi.getTanggal()));
            stmt.setString(2, presensi.getStatus());
            stmt.setString(3, presensi.getNisSiswa());
            stmt.setObject(4, presensi.getIdKelas(), java.sql.Types.INTEGER); // Menggunakan id_kelas langsung
            stmt.setInt(5, presensi.getIdPresensi());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui presensi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus presensi dari database berdasarkan ID.
     *
     * @param idPresensi ID presensi yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deletePresensi(int idPresensi) {
        String sql = "DELETE FROM presensi WHERE id_presensi = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPresensi);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus presensi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil presensi dari database berdasarkan ID.
     *
     * @param idPresensi ID presensi yang akan diambil.
     * @return Objek Presensi jika ditemukan, null jika tidak.
     */
    public Presensi getPresensiById(int idPresensi) {
        String sql = "SELECT p.id_presensi, p.tanggal, p.status, p.nis, s.nama AS nama_siswa, " +
                "p.id_kelas, k.nama_kelas, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM presensi p " +
                "JOIN siswa s ON p.nis = s.nis " +
                "LEFT JOIN kelas k ON p.id_kelas = k.id_kelas " + // Gunakan LEFT JOIN karena id_kelas bisa NULL
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE p.id_presensi = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPresensi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer idKelas = rs.getObject("id_kelas", Integer.class);
                String namaKelas = rs.getString("nama_kelas");
                String tahunAjaranLengkap = null;
                if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                return new Presensi(
                        rs.getInt("id_presensi"),
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        idKelas,
                        namaKelas,
                        tahunAjaranLengkap
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil presensi berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil semua data presensi.
     *
     * @return List objek Presensi.
     */
    public List<Presensi> getAllPresensi() {
        List<Presensi> presensiList = new ArrayList<>();
        String sql = "SELECT p.id_presensi, p.tanggal, p.status, p.nis, s.nama AS nama_siswa, " +
                "p.id_kelas, k.nama_kelas, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM presensi p " +
                "JOIN siswa s ON p.nis = s.nis " +
                "LEFT JOIN kelas k ON p.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "ORDER BY p.tanggal DESC, s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idKelas = rs.getObject("id_kelas", Integer.class);
                String namaKelas = rs.getString("nama_kelas");
                String tahunAjaranLengkap = null;
                if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                presensiList.add(new Presensi(
                        rs.getInt("id_presensi"),
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        idKelas,
                        namaKelas,
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua presensi: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }

    /**
     * Mengambil data presensi untuk kelas tertentu dan tahun ajaran tertentu.
     *
     * @param idKelas ID Kelas.
     * @param idTahunAjaran ID Tahun Ajaran.
     * @return List objek Presensi.
     */
    public List<Presensi> getPresensiByKelasAndTahunAjaran(int idKelas, int idTahunAjaran) {
        List<Presensi> presensiList = new ArrayList<>();
        String sql = "SELECT p.id_presensi, p.tanggal, p.status, " +
                "p.nis, s.nama AS nama_siswa, " +
                "p.id_kelas, k.nama_kelas, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM presensi p " +
                "JOIN siswa s ON p.nis = s.nis " +
                "JOIN kelas k ON p.id_kelas = k.id_kelas " + // JOIN ke kelas karena filter by id_kelas
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE p.id_kelas = ? AND k.id_tahun_ajaran = ? " + // Filter berdasarkan id_kelas di presensi dan id_tahun_ajaran di kelas
                "ORDER BY p.tanggal DESC, s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = null;
                if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                presensiList.add(new Presensi(
                        rs.getInt("id_presensi"),
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getInt("id_kelas"), // Mengambil id_kelas
                        rs.getString("nama_kelas"),
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil presensi berdasarkan kelas dan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }

    /**
     * Mengambil data presensi untuk siswa tertentu pada tahun ajaran tertentu.
     *
     * @param nis NIS Siswa.
     * @param idTahunAjaran ID Tahun Ajaran.
     * @return List objek Presensi.
     */
    public List<Presensi> getPresensiByNisAndTahunAjaran(String nis, int idTahunAjaran) {
        List<Presensi> presensiList = new ArrayList<>();
        String sql = "SELECT p.id_presensi, p.tanggal, p.status, " +
                "p.nis, s.nama AS nama_siswa, " +
                "p.id_kelas, k.nama_kelas, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM presensi p " +
                "JOIN siswa s ON p.nis = s.nis " +
                "JOIN kelas k ON p.id_kelas = k.id_kelas " + // JOIN ke kelas untuk mendapatkan tahun_ajaran
                "LEFT JOIN tahun_ajaran ta ON k.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE p.nis = ? AND k.id_tahun_ajaran = ? " + // Filter presensi berdasarkan NIS siswa dan tahun ajaran kelas siswa
                "ORDER BY p.tanggal DESC, s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = null;
                if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                presensiList.add(new Presensi(
                        rs.getInt("id_presensi"),
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getString("status"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getInt("id_kelas"), // Mengambil id_kelas
                        rs.getString("nama_kelas"),
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil presensi berdasarkan NIS dan Tahun Ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }
}
