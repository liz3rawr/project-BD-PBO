package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Ekstrakurikuler;
import com.example.projectdouble.model.Siswa;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EkstrakurikulerDAO {

    /**
     * Menambahkan ekstrakurikuler baru ke database.
     * @param ekstrakurikuler Objek Ekstrakurikuler yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addEkstrakurikuler(Ekstrakurikuler ekstrakurikuler) {
        // Menggunakan nama tabel lowercase 'ekstrakurikuler' sesuai konvensi PostgreSQL untuk unquoted identifiers
        String sql = "INSERT INTO ekstrakurikuler (nama, tingkat) VALUES (?, ?) RETURNING id_ekstrakurikuler";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ekstrakurikuler.getNama());
            stmt.setString(2, ekstrakurikuler.getTingkat());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ekstrakurikuler.setIdEkstrakurikuler(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua ekstrakurikuler dari database.
     * @return List objek Ekstrakurikuler.
     */
    public List<Ekstrakurikuler> getAllEkstrakurikuler() {
        List<Ekstrakurikuler> ekstrakurikulerList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'ekstrakurikuler'
        String sql = "SELECT id_ekstrakurikuler, nama, tingkat FROM ekstrakurikuler ORDER BY nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ekstrakurikulerList.add(new Ekstrakurikuler(
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama"),
                        rs.getString("tingkat")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
        }
        return ekstrakurikulerList;
    }

    /**
     * Mengambil semua ekstrakurikuler dari database beserta nama-nama pembinanya.
     * Nama-nama pembina digabungkan menjadi satu string.
     * @return List objek Ekstrakurikuler dengan nama-nama pembina.
     */
    public List<Ekstrakurikuler> getAllEkstrakurikulerWithMentors() {
        List<Ekstrakurikuler> ekstrakurikulerList = new ArrayList<>();
        // Menggunakan nama tabel lowercase 'ekstrakurikuler', 'pembina', 'guru'
        String sql = "SELECT e.id_ekstrakurikuler, e.nama, e.tingkat, STRING_AGG(g.nama, ', ') AS mentor_names " +
                "FROM ekstrakurikuler e " +
                "LEFT JOIN pembina p ON e.id_ekstrakurikuler = p.id_ekstrakurikuler " +
                "LEFT JOIN guru g ON p.nip = g.nip " +
                "GROUP BY e.id_ekstrakurikuler, e.nama, e.tingkat " +
                "ORDER BY e.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String mentorNames = rs.getString("mentor_names");
                Ekstrakurikuler ekskul = new Ekstrakurikuler(
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama"),
                        rs.getString("tingkat"),
                        (mentorNames != null ? mentorNames : "Belum Ada Pembina") // Pastikan tidak null jika tidak ada mentor
                );
                ekstrakurikulerList.add(ekskul);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil ekstrakurikuler dengan pembina: " + e.getMessage());
            e.printStackTrace();
        }
        return ekstrakurikulerList;
    }

    /**
     * Mengambil ekstrakurikuler berdasarkan ID.
     * @param idEkstrakurikuler ID ekstrakurikuler.
     * @return Objek Ekstrakurikuler jika ditemukan, null jika tidak.
     */
    public Ekstrakurikuler getEkstrakurikulerById(int idEkstrakurikuler) {
        // Menggunakan nama tabel lowercase 'ekstrakurikuler'
        //String sql = "SELECT id_ekstrakurikuler, nama, tingkat FROM ekstrakurikuler WHERE id_ekstrakurikuler = ?";
        String sql = "SELECT e.id_ekstrakurikuler, e.nama, e.tingkat, " +
                "STRING_AGG(g.nama, ', ') AS mentor_names " + // Gunakan STRING_AGG untuk mendapatkan semua nama mentor
                "FROM ekstrakurikuler e " +
                "LEFT JOIN pembina p ON e.id_ekstrakurikuler = p.id_ekstrakurikuler " +
                "LEFT JOIN guru g ON p.nip = g.nip " +
                "WHERE e.id_ekstrakurikuler = ? " +
                "GROUP BY e.id_ekstrakurikuler, e.nama, e.tingkat"; // Penting untuk GROUP BY
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ekstrakurikuler(
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama"),
                        rs.getString("tingkat"),
                        rs.getString("mentor_names") // Ambil hasil STRING_AGG
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil ekstrakurikuler berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Memperbarui data ekstrakurikuler di database.
     * @param ekstrakurikuler Objek Ekstrakurikuler dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateEkstrakurikuler(Ekstrakurikuler ekstrakurikuler) {
        // Menggunakan nama tabel lowercase 'ekstrakurikuler'
        String sql = "UPDATE ekstrakurikuler SET nama = ?, tingkat = ? WHERE id_ekstrakurikuler = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ekstrakurikuler.getNama());
            stmt.setString(2, ekstrakurikuler.getTingkat());
            stmt.setInt(3, ekstrakurikuler.getIdEkstrakurikuler());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEkstrakurikuler(int idEkstrakurikuler) {
        String sqlDeletePembina = "DELETE FROM pembina WHERE id_ekstrakurikuler = ?";
        String sqlDeletePeserta = "DELETE FROM peserta_ekskul WHERE id_ekstrakurikuler = ?";
        String sqlDeleteEkskul = "DELETE FROM ekstrakurikuler WHERE id_ekstrakurikuler = ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPembina = conn.prepareStatement(sqlDeletePembina)) {
                stmtPembina.setInt(1, idEkstrakurikuler);
                stmtPembina.executeUpdate();
            }

            try (PreparedStatement stmtPeserta = conn.prepareStatement(sqlDeletePeserta)) {
                stmtPeserta.setInt(1, idEkstrakurikuler);
                stmtPeserta.executeUpdate();
            }

            try (PreparedStatement stmtEkskul = conn.prepareStatement(sqlDeleteEkskul)) {
                stmtEkskul.setInt(1, idEkstrakurikuler);
                int rowsAffected = stmtEkskul.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            try (Connection conn = DBConnect.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error saat rollback transaksi: " + rollbackEx.getMessage());
            }
            return false;
        }


    }

    public List<Siswa> getStudentsByExtracurricularAndTahunAjaran(int idEkstrakurikuler, int idTahunAjaran) {
        List<Siswa> studentList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM peserta_ekskul pe " +
                "JOIN siswa s ON pe.nis = s.nis " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE pe.id_ekstrakurikuler = ? AND s.id_tahun_ajaran = ? " +
                "ORDER BY s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentList.add(new Siswa(
                        rs.getString("nis"),
                        rs.getString("nama"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("tempat_lahir"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        rs.getString("alamat"),
                        (Integer) rs.getObject("id_kelas"),
                        rs.getString("nama_kelas"),
                        (Integer) rs.getObject("id_tahun_ajaran"),
                        rs.getString("tahun_ajaran_lengkap"),
                        (Integer) rs.getObject("id_user"),
                        rs.getString("username_user"),
                        rs.getString("password_user")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil siswa berdasarkan ekstrakurikuler dan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return studentList;
    }
}
