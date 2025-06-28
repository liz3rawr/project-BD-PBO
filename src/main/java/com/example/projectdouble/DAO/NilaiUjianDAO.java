package com.example.projectdouble.DAO;

import com.example.projectdouble.model.NilaiUjian;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NilaiUjianDAO {

    /**
     * Menambahkan nilai ujian baru ke database.
     * Sekarang menyimpan id_tahun_ajaran secara langsung.
     *
     * @param nilaiUjian Objek NilaiUjian yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addNilaiUjian(NilaiUjian nilaiUjian) {
        String sql = "INSERT INTO nilai_ujian (jenis_ujian, nilai, id_mapel, nis, id_tahun_ajaran) VALUES (?, ?, ?, ?, ?) RETURNING id_nilai_ujian";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nilaiUjian.getJenisUjian());
            stmt.setBigDecimal(2, nilaiUjian.getNilai());
            stmt.setInt(3, nilaiUjian.getIdMapel());
            stmt.setString(4, nilaiUjian.getNis());
            stmt.setInt(5, nilaiUjian.getIdTahunAjaran()); // Menggunakan id_tahun_ajaran

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nilaiUjian.setIdNilaiUjian(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan nilai ujian: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui nilai ujian yang sudah ada di database.
     * Sekarang memperbarui id_tahun_ajaran secara langsung.
     *
     * @param nilaiUjian Objek NilaiUjian dengan informasi yang diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateNilaiUjian(NilaiUjian nilaiUjian) {
        String sql = "UPDATE nilai_ujian SET jenis_ujian = ?, nilai = ?, id_mapel = ?, nis = ?, id_tahun_ajaran = ? WHERE id_nilai_ujian = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nilaiUjian.getJenisUjian());
            stmt.setBigDecimal(2, nilaiUjian.getNilai());
            stmt.setInt(3, nilaiUjian.getIdMapel());
            stmt.setString(4, nilaiUjian.getNis());
            stmt.setInt(5, nilaiUjian.getIdTahunAjaran()); // Memperbarui id_tahun_ajaran
            stmt.setInt(6, nilaiUjian.getIdNilaiUjian());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui nilai ujian: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus nilai ujian dari database berdasarkan ID.
     *
     * @param idNilaiUjian ID nilai ujian yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteNilaiUjian(int idNilaiUjian) {
        String sql = "DELETE FROM nilai_ujian WHERE id_nilai_ujian = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idNilaiUjian);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus nilai ujian: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil nilai ujian dari database berdasarkan ID.
     * Mengambil tahun_ajaran_lengkap dari tabel tahun_ajaran.
     *
     * @param idNilaiUjian ID nilai ujian yang akan diambil.
     * @return Objek NilaiUjian jika ditemukan, null jika tidak.
     */
    public NilaiUjian getNilaiUjianById(int idNilaiUjian) {
        String sql = "SELECT nu.id_nilai_ujian, nu.jenis_ujian, nu.nilai, " +
                "nu.id_mapel, mp.nama_mapel, nu.nis, s.nama AS nama_siswa, " +
                "nu.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM nilai_ujian nu " +
                "JOIN mata_pelajaran mp ON nu.id_mapel = mp.id_mapel " +
                "JOIN siswa s ON nu.nis = s.nis " +
                "JOIN tahun_ajaran ta ON nu.id_tahun_ajaran = ta.id_tahun_ajaran " + // Join langsung ke tahun_ajaran
                "WHERE nu.id_nilai_ujian = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idNilaiUjian);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Menggunakan static factory method dari model NilaiUjian
                return NilaiUjian.fromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nilai ujian berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil semua data nilai ujian.
     *
     * @return List objek NilaiUjian.
     */
    public List<NilaiUjian> getAllNilaiUjian() {
        List<NilaiUjian> nilaiList = new ArrayList<>();
        String sql = "SELECT nu.id_nilai_ujian, nu.jenis_ujian, nu.nilai, " +
                "nu.id_mapel, mp.nama_mapel, nu.nis, s.nama AS nama_siswa, " +
                "nu.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM nilai_ujian nu " +
                "JOIN mata_pelajaran mp ON nu.id_mapel = mp.id_mapel " +
                "JOIN siswa s ON nu.nis = s.nis " +
                "JOIN tahun_ajaran ta ON nu.id_tahun_ajaran = ta.id_tahun_ajaran " + // Join langsung ke tahun_ajaran
                "ORDER BY s.nama, mp.nama_mapel, nu.jenis_ujian";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Menggunakan static factory method dari model NilaiUjian
                nilaiList.add(NilaiUjian.fromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua nilai ujian: " + e.getMessage());
            e.printStackTrace();
        }
        return nilaiList;
    }

    /**
     * Mengambil nilai ujian yang difilter berdasarkan kriteria tertentu.
     * Memfilter berdasarkan NIS, ID Mata Pelajaran, Jenis Ujian, dan ID Tahun Ajaran.
     *
     * @param nis          NIS Siswa (bisa null jika tidak ingin filter siswa).
     * @param idMapel      ID Mata Pelajaran (bisa null jika tidak ingin filter mapel).
     * @param jenisUjian   Jenis Ujian (bisa null jika tidak ingin filter jenis ujian).
     * @param idTahunAjaran ID Tahun Ajaran (bisa null jika tidak ingin filter tahun ajaran).
     * @return List objek NilaiUjian.
     */
    public List<NilaiUjian> getNilaiUjianFiltered(String nis, Integer idMapel, String jenisUjian, Integer idTahunAjaran) {
        List<NilaiUjian> nilaiList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT nu.id_nilai_ujian, nu.jenis_ujian, nu.nilai, " +
                        "nu.id_mapel, mp.nama_mapel, nu.nis, s.nama AS nama_siswa, " +
                        "nu.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                        "FROM nilai_ujian nu " +
                        "JOIN mata_pelajaran mp ON nu.id_mapel = mp.id_mapel " +
                        "JOIN siswa s ON nu.nis = s.nis " +
                        "JOIN tahun_ajaran ta ON nu.id_tahun_ajaran = ta.id_tahun_ajaran " + // Join langsung ke tahun_ajaran
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (nis != null && !nis.isEmpty()) {
            sql.append(" AND nu.nis = ?");
            params.add(nis);
        }
        if (idMapel != null && idMapel != 0) {
            sql.append(" AND nu.id_mapel = ?");
            params.add(idMapel);
        }
        if (jenisUjian != null && !jenisUjian.isEmpty()) {
            sql.append(" AND nu.jenis_ujian = ?");
            params.add(jenisUjian);
        }
        if (idTahunAjaran != null && idTahunAjaran != 0) { // Filter berdasarkan id_tahun_ajaran
            sql.append(" AND nu.id_tahun_ajaran = ?");
            params.add(idTahunAjaran);
        }

        sql.append(" ORDER BY s.nama, mp.nama_mapel, nu.jenis_ujian");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Menggunakan static factory method dari model NilaiUjian
                nilaiList.add(NilaiUjian.fromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nilai ujian dengan filter: " + e.getMessage());
            e.printStackTrace();
        }
        return nilaiList;
    }

    /**
     * Mengambil daftar nilai ujian untuk siswa tertentu pada tahun ajaran tertentu.
     *
     * @param nis           NIS Siswa.
     * @param idTahunAjaran ID Tahun Ajaran.
     * @return List objek NilaiUjian.
     */
    public List<NilaiUjian> getNilaiUjianByNisAndTahunAjaran(String nis, int idTahunAjaran) {
        List<NilaiUjian> nilaiList = new ArrayList<>();
        String sql = "SELECT nu.id_nilai_ujian, nu.jenis_ujian, nu.nilai, " +
                "nu.id_mapel, mp.nama_mapel, nu.nis, s.nama AS nama_siswa, " +
                "nu.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM nilai_ujian nu " +
                "JOIN mata_pelajaran mp ON nu.id_mapel = mp.id_mapel " +
                "JOIN siswa s ON nu.nis = s.nis " +
                "JOIN tahun_ajaran ta ON nu.id_tahun_ajaran = ta.id_tahun_ajaran " + // Join langsung ke tahun_ajaran
                "WHERE nu.nis = ? AND nu.id_tahun_ajaran = ? " +
                "ORDER BY mp.nama_mapel, nu.jenis_ujian";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nis);
            stmt.setInt(2, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Menggunakan static factory method dari model NilaiUjian
                nilaiList.add(NilaiUjian.fromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nilai ujian berdasarkan NIS dan Tahun Ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return nilaiList;
    }
}
