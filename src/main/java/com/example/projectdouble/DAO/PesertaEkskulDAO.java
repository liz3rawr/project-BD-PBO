package com.example.projectdouble.DAO;

import com.example.projectdouble.model.PesertaEkskul;
import com.example.projectdouble.model.Siswa;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PesertaEkskulDAO {

    public boolean addPesertaEkskul(PesertaEkskul peserta) {
        String sql = "INSERT INTO peserta_ekskul (nis, id_ekstrakurikuler) VALUES (?, ?) RETURNING id_peserta";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, peserta.getNisSiswa());
            stmt.setInt(2, peserta.getIdEkstrakurikuler());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        peserta.setIdPeserta(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan peserta ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePesertaEkskul(PesertaEkskul peserta) {
        String sql = "UPDATE peserta_ekskul SET nis = ?, id_ekstrakurikuler = ? WHERE id_peserta = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, peserta.getNisSiswa());
            stmt.setInt(2, peserta.getIdEkstrakurikuler());
            stmt.setInt(3, peserta.getIdPeserta());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui peserta ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePesertaEkskul(int idPeserta) {
        String sql = "DELETE FROM peserta_ekskul WHERE id_peserta = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPeserta);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus peserta ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public PesertaEkskul getPesertaEkskulById(int idPeserta) {
        String sql = "SELECT pe.id_peserta, pe.nis, s.nama AS nama_siswa, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, " +
                "pe.id_ekstrakurikuler, e.nama AS nama_ekstrakurikuler, e.tingkat " +
                "FROM peserta_ekskul pe " +
                "JOIN siswa s ON pe.nis = s.nis " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN ekstrakurikuler e ON pe.id_ekstrakurikuler = e.id_ekstrakurikuler " +
                "WHERE pe.id_peserta = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPeserta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer idKelasSiswa = rs.getObject("id_kelas", Integer.class);
                String namaKelasSiswa = rs.getString("nama_kelas");
                Integer idTahunAjaranSiswa = rs.getObject("id_tahun_ajaran", Integer.class);
                return new PesertaEkskul(
                        rs.getInt("id_peserta"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        idKelasSiswa,
                        namaKelasSiswa,
                        idTahunAjaranSiswa,
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama_ekstrakurikuler"),
                        rs.getString("tingkat")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil peserta ekstrakurikuler berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<PesertaEkskul> getAllPesertaEkskul() {
        List<PesertaEkskul> pesertaList = new ArrayList<>();
        String sql = "SELECT pe.id_peserta, pe.nis, s.nama AS nama_siswa, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, " +
                "pe.id_ekstrakurikuler, e.nama AS nama_ekstrakurikuler, e.tingkat " +
                "FROM peserta_ekskul pe " +
                "JOIN siswa s ON pe.nis = s.nis " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN ekstrakurikuler e ON pe.id_ekstrakurikuler = e.id_ekstrakurikuler " +
                "ORDER BY s.nama, e.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idKelasSiswa = rs.getObject("id_kelas", Integer.class);
                String namaKelasSiswa = rs.getString("nama_kelas");
                Integer idTahunAjaranSiswa = rs.getObject("id_tahun_ajaran", Integer.class);
                pesertaList.add(new PesertaEkskul(
                        rs.getInt("id_peserta"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        idKelasSiswa,
                        namaKelasSiswa,
                        idTahunAjaranSiswa,
                        rs.getInt("id_ekstrakurikuler"),
                        rs.getString("nama_ekstrakurikuler"),
                        rs.getString("tingkat")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua peserta ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
        }
        return pesertaList;
    }

    public List<Siswa> getStudentsByExtracurricularAndTahunAjaran(int idEkstrakurikuler, int idTahunAjaran) {
        List<Siswa> siswaList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai, " +
                "s.id_user, u.username, u.password, r.id_role, r.nama_role " +
                "FROM peserta_ekskul pe " +
                "JOIN siswa s ON pe.nis = s.nis " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN role r ON u.role = r.id_role " +
                "WHERE pe.id_ekstrakurikuler = ? AND s.id_tahun_ajaran = ? " +
                "ORDER BY s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idKelas = rs.getObject("id_kelas", Integer.class);
                String namaKelas = rs.getString("nama_kelas");
                String tahunAjaranLengkap = null;
                if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                Integer idUser = rs.getObject("id_user", Integer.class);
                String usernameUser = rs.getString("username");
                String passwordUser = rs.getString("password");

                siswaList.add(new Siswa(
                        rs.getString("nis"),
                        rs.getString("nama"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("tempat_lahir"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        rs.getString("alamat"),
                        idKelas,
                        namaKelas,
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap,
                        idUser,
                        usernameUser,
                        passwordUser
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil siswa di ekstrakurikuler berdasarkan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return siswaList;
    }

    public List<Siswa> getStudentsByExtracurricular(int idEkstrakurikuler) {
        List<Siswa> studentList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM peserta_ekskul pe " +
                "JOIN siswa s ON pe.nis = s.nis " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE pe.id_ekstrakurikuler = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEkstrakurikuler);
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
            System.err.println("Error saat mengambil siswa berdasarkan ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
        }
        return studentList;
    }

    public boolean deletePesertaEkskulByNisAndEkskulId(String nis, int idEkstrakurikuler) {
        String sql = "DELETE FROM peserta_ekskul WHERE nis = ? AND id_ekstrakurikuler = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            stmt.setInt(2, idEkstrakurikuler);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus peserta ekstrakurikuler berdasarkan NIS dan ID Ekstrakurikuler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
