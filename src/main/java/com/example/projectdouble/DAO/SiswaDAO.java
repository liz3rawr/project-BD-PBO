package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Siswa;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaDAO {

    private UserDAO userDao = new UserDAO();

    public boolean addSiswa(Siswa siswa) {
        String sql = "INSERT INTO siswa (nis, nama, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siswa.getNis());
            stmt.setString(2, siswa.getNama());
            stmt.setString(3, siswa.getJenisKelamin());
            stmt.setString(4, siswa.getTempatLahir());
            stmt.setDate(5, Date.valueOf(siswa.getTanggalLahir()));
            stmt.setString(6, siswa.getAlamat());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan data siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSiswa(Siswa siswa) {
        String sql = "UPDATE siswa SET nama = ?, jenis_kelamin = ?, tempat_lahir = ?, tanggal_lahir = ?, alamat = ? WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, siswa.getNama());
            stmt.setString(2, siswa.getJenisKelamin());
            stmt.setString(3, siswa.getTempatLahir());
            stmt.setDate(4, Date.valueOf(siswa.getTanggalLahir()));
            stmt.setString(5, siswa.getAlamat());
            stmt.setString(6, siswa.getNis());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui data siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSiswaUser(Siswa siswa) {
        String sql = "UPDATE siswa SET id_user = ? WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (siswa.getIdUser() != null) {
                stmt.setInt(1, siswa.getIdUser());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, siswa.getNis());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat mengaitkan user ke siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeClassInfoFromStudent(String nis) {
        String sql = "UPDATE siswa SET id_kelas = NULL, id_tahun_ajaran = NULL WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus informasi kelas dari siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Siswa getSiswaByNis(String nis) {
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM siswa s " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE s.nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Siswa(
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
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil siswa berdasarkan NIS: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Siswa> getAllSiswa() {
        List<Siswa> siswaList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM siswa s " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "ORDER BY s.nis ASC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                siswaList.add(new Siswa(
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
            System.err.println("Error saat mengambil semua data siswa: " + e.getMessage());
            e.printStackTrace();
        }
        return siswaList;
    }

    public List<Siswa> searchSiswa(String keyword) {
        List<Siswa> siswaList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM siswa s " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE s.nama ILIKE ? OR s.nis ILIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                siswaList.add(new Siswa(
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
            System.err.println("Error saat mencari siswa: " + e.getMessage());
            e.printStackTrace();
        }
        return siswaList;
    }

    public boolean deleteSiswa(String nis) {
        Siswa siswaToDelete = getSiswaByNis(nis);
        if (siswaToDelete == null) {
            System.err.println("Siswa dengan NIS " + nis + " tidak ditemukan untuk dihapus.");
            return false;
        }

        String sqlDeletePesertaEkskul = "DELETE FROM peserta_ekskul WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmtPesertaEkskul = conn.prepareStatement(sqlDeletePesertaEkskul)) {
            stmtPesertaEkskul.setString(1, nis);
            stmtPesertaEkskul.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saat menghapus entri peserta_ekskul untuk siswa dengan NIS " + nis + ": " + e.getMessage());
            e.printStackTrace();
        }

        String sqlSiswa = "DELETE FROM siswa WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmtSiswa = conn.prepareStatement(sqlSiswa)) {
            stmtSiswa.setString(1, nis);
            int rowsAffectedSiswa = stmtSiswa.executeUpdate();

            if (rowsAffectedSiswa > 0) {
                if (siswaToDelete.getIdUser() != null && siswaToDelete.getIdUser() != 0) {
                    userDao.deleteUser(siswaToDelete.getIdUser());
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean assignStudentToClass(String nis, Integer idKelas, Integer idTahunAjaran) {
        String sql = "UPDATE siswa SET id_kelas = ?, id_tahun_ajaran = ? WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (idKelas != null) {
                stmt.setInt(1, idKelas);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            if (idTahunAjaran != null) {
                stmt.setInt(2, idTahunAjaran);
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, nis);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning student to class: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Siswa> getStudentsInClass(int idKelas, int idTahunAjaran) {
        List<Siswa> siswaList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM siswa s " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE s.id_kelas = ? AND s.id_tahun_ajaran = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                siswaList.add(new Siswa(
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
            System.err.println("Error saat mengambil siswa dalam kelas: " + e.getMessage());
            e.printStackTrace();
        }
        return siswaList;
    }

    public boolean updateSiswaKelasTahunAjaran(Siswa siswa) {
        String sql = "UPDATE siswa SET id_kelas = ?, id_tahun_ajaran = ? WHERE nis = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (siswa.getIdKelas() != null) {
                stmt.setInt(1, siswa.getIdKelas());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }

            if (siswa.getIdTahunAjaran() != null) {
                stmt.setInt(2, siswa.getIdTahunAjaran());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setString(3, siswa.getNis());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui kelas dan tahun ajaran siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Siswa> getStudentsInClassByTahunAjaran(int idKelas, int idTahunAjaran) {
        List<Siswa> siswaList = new ArrayList<>();
        String sql = "SELECT s.nis, s.nama, s.jenis_kelamin, s.tempat_lahir, s.tanggal_lahir, s.alamat, " +
                "u.id_user, u.username AS username_user, u.password AS password_user, " +
                "s.id_kelas, k.nama_kelas, s.id_tahun_ajaran, (ta.tahun_mulai || '/' || ta.tahun_selesai) AS tahun_ajaran_lengkap " +
                "FROM siswa s " +
                "LEFT JOIN users u ON s.id_user = u.id_user " +
                "LEFT JOIN kelas k ON s.id_kelas = k.id_kelas " +
                "LEFT JOIN tahun_ajaran ta ON s.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE s.id_kelas = ? AND s.id_tahun_ajaran = ? " +
                "ORDER BY s.nama";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                siswaList.add(new Siswa(
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
            System.err.println("Error saat mengambil siswa dalam kelas berdasarkan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return siswaList;
    }

    public String getLastNis() {
        String sql = "SELECT nis FROM siswa ORDER BY nis DESC LIMIT 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nis");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mendapatkan NIS terakhir: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

