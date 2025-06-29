package com.example.projectdouble.DAO;

import com.example.projectdouble.model.Tugas;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TugasDAO {

    public boolean addTugas(Tugas tugas) {
        String sql = "INSERT INTO tugas (judul, deskripsi, tanggal_deadline, id_mapel, id_kelas, nip_guru) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_tugas";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tugas.getJudul());
            stmt.setString(2, tugas.getDeskripsi());
            stmt.setDate(3, Date.valueOf(tugas.getTanggalDeadline()));

            if (tugas.getIdMapel() != null) {
                stmt.setInt(4, tugas.getIdMapel());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (tugas.getIdKelas() != null) {
                stmt.setInt(5, tugas.getIdKelas());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setString(6, tugas.getNipGuru());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tugas.setIdTugas(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan tugas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTugas(Tugas tugas) {
        String sql = "UPDATE tugas SET judul = ?, deskripsi = ?, tanggal_deadline = ?, id_mapel = ?, id_kelas = ?, nip_guru = ? WHERE id_tugas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tugas.getJudul());
            stmt.setString(2, tugas.getDeskripsi());
            stmt.setDate(3, Date.valueOf(tugas.getTanggalDeadline()));
            if (tugas.getIdMapel() != null) {
                stmt.setInt(4, tugas.getIdMapel());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (tugas.getIdKelas() != null) {
                stmt.setInt(5, tugas.getIdKelas());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setString(6, tugas.getNipGuru());
            stmt.setInt(7, tugas.getIdTugas());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui tugas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTugas(int idTugas) {
        String sql = "DELETE FROM tugas WHERE id_tugas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTugas);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus tugas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Tugas getTugasById(int idTugas) {
        String sql = "SELECT t.id_tugas, t.judul, t.deskripsi, t.tanggal_deadline, " +
                "t.id_mapel, mp.nama_mapel, " +
                "t.id_kelas, k.nama_kelas, " +
                "t.nip_guru, g.nama AS nama_guru " +
                // "LEFT JOIN tahun_ajaran ta ON t.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "FROM tugas t " +
                "LEFT JOIN mata_pelajaran mp ON t.id_mapel = mp.id_mapel " +
                "LEFT JOIN kelas k ON t.id_kelas = k.id_kelas " +
                "LEFT JOIN guru g ON t.nip_guru = g.nip " +
                "WHERE t.id_tugas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTugas);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer idMapel = (Integer) rs.getObject("id_mapel");
                String namaMapel = rs.getString("nama_mapel");

                Integer idKelas = (Integer) rs.getObject("id_kelas");
                String namaKelas = rs.getString("nama_kelas");

                return new Tugas(
                        rs.getInt("id_tugas"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_deadline").toLocalDate(),
                        idMapel,
                        namaMapel,
                        idKelas,
                        namaKelas,
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil tugas berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Tugas> getAllTugas() {
        List<Tugas> tugasList = new ArrayList<>();
        String sql = "SELECT t.id_tugas, t.judul, t.deskripsi, t.tanggal_deadline, " +
                "t.id_mapel, mp.nama_mapel, " +
                "t.id_kelas, k.nama_kelas, " +
                "t.nip_guru, g.nama AS nama_guru " +
                // "LEFT JOIN tahun_ajaran ta ON t.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "FROM tugas t " +
                "LEFT JOIN mata_pelajaran mp ON t.id_mapel = mp.id_mapel " +
                "LEFT JOIN kelas k ON t.id_kelas = k.id_kelas " +
                "LEFT JOIN guru g ON t.nip_guru = g.nip " +
                "ORDER BY t.tanggal_deadline DESC, t.judul";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idMapel = (Integer) rs.getObject("id_mapel");
                String namaMapel = rs.getString("nama_mapel");

                Integer idKelas = (Integer) rs.getObject("id_kelas");
                String namaKelas = rs.getString("nama_kelas");

                tugasList.add(new Tugas(
                        rs.getInt("id_tugas"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_deadline").toLocalDate(),
                        idMapel,
                        namaMapel,
                        idKelas,
                        namaKelas,
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua tugas: " + e.getMessage());
            e.printStackTrace();
        }
        return tugasList;
    }

    public List<Tugas> getTugasByGuruNip(String nipGuru) {
        List<Tugas> tugasList = new ArrayList<>();
        String sql = "SELECT t.id_tugas, t.judul, t.deskripsi, t.tanggal_deadline, " +
                "t.id_mapel, mp.nama_mapel, " +
                "t.id_kelas, k.nama_kelas, " +
                "t.nip_guru, g.nama AS nama_guru " +
                // "LEFT JOIN tahun_ajaran ta ON t.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "FROM tugas t " +
                "LEFT JOIN mata_pelajaran mp ON t.id_mapel = mp.id_mapel " +
                "LEFT JOIN kelas k ON t.id_kelas = k.id_kelas " +
                "LEFT JOIN guru g ON t.nip_guru = g.nip " +
                "WHERE t.nip_guru = ? " +
                "ORDER BY t.tanggal_deadline DESC, t.judul";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nipGuru);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idMapel = (Integer) rs.getObject("id_mapel");
                String namaMapel = rs.getString("nama_mapel");

                Integer idKelas = (Integer) rs.getObject("id_kelas");
                String namaKelas = rs.getString("nama_kelas");

                tugasList.add(new Tugas(
                        rs.getInt("id_tugas"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_deadline").toLocalDate(),
                        idMapel,
                        namaMapel,
                        idKelas,
                        namaKelas,
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil tugas berdasarkan NIP guru: " + e.getMessage());
            e.printStackTrace();
        }
        return tugasList;
    }

    public List<Tugas> getTugasByKelasAndTahunAjaran(int idKelas, int idTahunAjaran) {
        List<Tugas> tugasList = new ArrayList<>();
        String sql = "SELECT t.id_tugas, t.judul, t.deskripsi, t.tanggal_deadline, " +
                "t.id_mapel, mp.nama_mapel, " +
                "t.id_kelas, k.nama_kelas, " +
                "t.nip_guru, g.nama AS nama_guru " +
                "FROM tugas t " +
                "LEFT JOIN mata_pelajaran mp ON t.id_mapel = mp.id_mapel " +
                "LEFT JOIN kelas k ON t.id_kelas = k.id_kelas " +
                "LEFT JOIN guru g ON t.nip_guru = g.nip " +
                "WHERE t.id_kelas = ? AND k.id_tahun_ajaran = ? " +
                "ORDER BY t.tanggal_deadline DESC, t.judul";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer idMapel = (Integer) rs.getObject("id_mapel");
                String namaMapel = rs.getString("nama_mapel");
                Integer fetchedIdKelas = (Integer) rs.getObject("id_kelas");
                String namaKelas = rs.getString("nama_kelas");
                tugasList.add(new Tugas(
                        rs.getInt("id_tugas"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_deadline").toLocalDate(),
                        idMapel,
                        namaMapel,
                        fetchedIdKelas,
                        namaKelas,
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil tugas berdasarkan Kelas dan Tahun Ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return tugasList;
    }
}
