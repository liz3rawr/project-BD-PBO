package com.example.projectdouble.DAO;

import com.example.projectdouble.model.JadwalKelas;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JadwalKelasDAO {

    /**
     * Menambahkan jadwal kelas baru ke database.
     *
     * @param jadwal Objek JadwalKelas yang akan ditambahkan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean addJadwalKelas(JadwalKelas jadwal) {
        String sql = "INSERT INTO jadwal_kelas (hari, jam_mulai, jam_selesai, id_kelas, id_mapel, nip_guru, id_tahun_ajaran) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_jadwal_kelas";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, jadwal.getHari());
            stmt.setTime(2, Time.valueOf(jadwal.getJamMulai()));
            stmt.setTime(3, Time.valueOf(jadwal.getJamSelesai()));
            stmt.setInt(4, jadwal.getIdKelas());
            stmt.setInt(5, jadwal.getIdMapel());
            stmt.setString(6, jadwal.getNipGuru());
            stmt.setInt(7, jadwal.getIdTahunAjaran());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        jadwal.setIdJadwalKelas(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan jadwal kelas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui informasi jadwal kelas yang sudah ada.
     *
     * @param jadwal Objek JadwalKelas dengan informasi yang diperbarui.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean updateJadwalKelas(JadwalKelas jadwal) {
        String sql = "UPDATE jadwal_kelas SET hari = ?, jam_mulai = ?, jam_selesai = ?, id_kelas = ?, id_mapel = ?, nip_guru = ?, id_tahun_ajaran = ? WHERE id_jadwal_kelas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jadwal.getHari());
            stmt.setTime(2, Time.valueOf(jadwal.getJamMulai()));
            stmt.setTime(3, Time.valueOf(jadwal.getJamSelesai()));
            stmt.setInt(4, jadwal.getIdKelas());
            stmt.setInt(5, jadwal.getIdMapel());
            stmt.setString(6, jadwal.getNipGuru());
            stmt.setInt(7, jadwal.getIdTahunAjaran());
            stmt.setInt(8, jadwal.getIdJadwalKelas());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui jadwal kelas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus jadwal kelas dari database.
     *
     * @param idJadwalKelas ID jadwal kelas yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean deleteJadwalKelas(int idJadwalKelas) {
        String sql = "DELETE FROM jadwal_kelas WHERE id_jadwal_kelas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idJadwalKelas);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus jadwal kelas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil data jadwal kelas berdasarkan ID, termasuk detail kelas, mapel, guru, dan tahun ajaran.
     *
     * @param idJadwalKelas ID jadwal kelas.
     * @return Objek JadwalKelas jika ditemukan, null jika tidak.
     */
    public JadwalKelas getJadwalKelasById(int idJadwalKelas) {
        String sql = "SELECT jk.id_jadwal_kelas, jk.hari, jk.jam_mulai, jk.jam_selesai, " +
                "jk.id_kelas, k.nama_kelas, " +
                "jk.id_mapel, mp.nama_mapel, " +
                "jk.nip_guru, g.nama AS nama_guru, " +
                "jk.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM jadwal_kelas jk " +
                "JOIN kelas k ON jk.id_kelas = k.id_kelas " +
                "JOIN mata_pelajaran mp ON jk.id_mapel = mp.id_mapel " +
                "JOIN guru g ON jk.nip_guru = g.nip " +
                "JOIN tahun_ajaran ta ON jk.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE jk.id_jadwal_kelas = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idJadwalKelas);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                return new JadwalKelas(
                        rs.getInt("id_jadwal_kelas"),
                        rs.getString("hari"),
                        rs.getTime("jam_mulai").toLocalTime(),
                        rs.getTime("jam_selesai").toLocalTime(),
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil jadwal kelas berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengambil semua data jadwal kelas dengan detail lengkap.
     *
     * @return List objek JadwalKelas.
     */
    public List<JadwalKelas> getAllJadwalKelasDetails() {
        List<JadwalKelas> jadwalList = new ArrayList<>();
        String sql = "SELECT jk.id_jadwal_kelas, jk.hari, jk.jam_mulai, jk.jam_selesai, " +
                "jk.id_kelas, jk.id_mapel, jk.nip_guru, jk.id_tahun_ajaran, " + // Use k.id_tahun_ajaran
                "mp.nama_mapel, " +
                "k.nama_kelas, ta.tahun_mulai, ta.tahun_selesai, " +
                "g.nama AS nama_guru " +
                "FROM jadwal_kelas jk " +
                "JOIN mata_pelajaran mp ON jk.id_mapel = mp.id_mapel " +
                "JOIN kelas k ON jk.id_kelas = k.id_kelas " +
                "JOIN guru g ON jk.nip_guru = g.nip " +
                "LEFT JOIN tahun_ajaran ta ON jk.id_tahun_ajaran = ta.id_tahun_ajaran " + // Join on k.id_tahun_ajaran
                "ORDER BY jk.hari, jk.jam_mulai";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                jadwalList.add(new JadwalKelas(
                        rs.getInt("id_jadwal_kelas"),
                        rs.getString("hari"),
                        rs.getTime("jam_mulai").toLocalTime(),
                        rs.getTime("jam_selesai").toLocalTime(),
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua jadwal kelas: " + e.getMessage());
            e.printStackTrace();
        }
        return jadwalList;
    }


    /**
     * Mengambil jadwal kelas berdasarkan ID kelas dan hari.
     * Ini bisa digunakan untuk menemukan jadwal tertentu di kelas pada hari tertentu.
     *
     * @param idKelas ID Kelas.
     * @param hari    Nama hari.
     * @return List objek JadwalKelas.
     */
    public List<JadwalKelas> getJadwalKelasByKelasIdAndHari(int idKelas, String hari) {
        List<JadwalKelas> jadwalList = new ArrayList<>();
        String sql = "SELECT jk.id_jadwal_kelas, jk.hari, jk.jam_mulai, jk.jam_selesai, " +
                "jk.id_kelas, k.nama_kelas, " +
                "jk.id_mapel, mp.nama_mapel, " +
                "jk.nip_guru, g.nama AS nama_guru, " +
                "jk.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai " +
                "FROM jadwal_kelas jk " +
                "JOIN kelas k ON jk.id_kelas = k.id_kelas " +
                "JOIN mata_pelajaran mp ON jk.id_mapel = mp.id_mapel " +
                "JOIN guru g ON jk.nip_guru = g.nip " +
                "JOIN tahun_ajaran ta ON jk.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE jk.id_kelas = ? AND jk.hari = ? " +
                "ORDER BY jk.jam_mulai";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKelas);
            stmt.setString(2, hari);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                jadwalList.add(new JadwalKelas(
                        rs.getInt("id_jadwal_kelas"),
                        rs.getString("hari"),
                        rs.getTime("jam_mulai").toLocalTime(),
                        rs.getTime("jam_selesai").toLocalTime(),
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil jadwal kelas berdasarkan ID kelas dan hari: " + e.getMessage());
            e.printStackTrace();
        }
        return jadwalList;
    }

    /**
     * Mengambil daftar jadwal pelajaran untuk kelas dan tahun ajaran tertentu.
     * Metode ini melakukan JOIN dengan tabel mata_pelajaran untuk mendapatkan nama mapel.
     *
     * @param idKelas ID dari kelas yang jadwalnya ingin diambil.
     * @param idTahunAjaran ID dari tahun ajaran yang relevan.
     * @return List dari objek JadwalKelas.
     */
    public List<JadwalKelas> getJadwalByKelasAndTahunAjaran(int idKelas, int idTahunAjaran) {
        // Siapkan list untuk menampung hasil query
        List<JadwalKelas> jadwalList = new ArrayList<>();

        // Query SQL untuk mengambil data jadwal dengan JOIN ke tabel mata pelajaran
        // 'jk' adalah alias untuk jadwal_kelas, dan 'mp' untuk mata_pelajaran
        String sql = "SELECT jk.id_jadwal_kelas, jk.hari, jk.jam_mulai, jk.jam_selesai, jk.id_kelas, jk.id_mapel, jk.id_tahun_ajaran, ta.tahun_mulai, ta.tahun_selesai, mp.nama_mapel " +
                "FROM jadwal_kelas jk " +
                "JOIN mata_pelajaran mp ON jk.id_mapel = mp.id_mapel " +
                "JOIN tahun_ajaran ta ON jk.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE jk.id_kelas = ? AND jk.id_tahun_ajaran = ? " +
                "ORDER BY jk.hari, jk.jam_mulai"; // Urutkan berdasarkan hari dan jam

        // Menggunakan try-with-resources untuk memastikan koneksi dan statement ditutup secara otomatis
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameter untuk PreparedStatement (mencegah SQL Injection)
            pstmt.setInt(1, idKelas);
            pstmt.setInt(2, idTahunAjaran);
            ResultSet rs = pstmt.executeQuery();

            // Eksekusi query dan dapatkan hasilnya di ResultSet
            //try (ResultSet rs = pstmt.executeQuery()) {
            // Looping selama masih ada baris data di ResultSet
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                jadwalList.add(new JadwalKelas(
                        rs.getInt("id_jadwal_kelas"),
                        rs.getString("hari"),
                        rs.getTime("jam_mulai").toLocalTime(),
                        rs.getTime("jam_selesai").toLocalTime(),
                        rs.getInt("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("id_mapel"),
                        rs.getString("nama_mapel"),
                        rs.getString("nip_guru"),
                        rs.getString("nama_guru"),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap
                ));

//                    // Buat objek JadwalKelas baru untuk setiap baris data
//                    JadwalKelas jadwal = new JadwalKelas();
//
//                    // Set properti objek JadwalKelas dari data di ResultSet
//                    jadwal.setIdJadwal(rs.getInt("id_jadwal"));
//                    jadwal.setHari(rs.getString("hari"));
//                    // Mengambil waktu dan mengubahnya menjadi LocalTime jika tipe data di model adalah LocalTime
//                    jadwal.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
//                    jadwal.setJamSelesai(rs.getTime("jam_selesai").toLocalTime());
//                    jadwal.setIdKelas(rs.getInt("id_kelas"));
//                    jadwal.setIdMapel(rs.getInt("id_mapel"));
//                    jadwal.setIdTahunAjaran(rs.getInt("id_tahun_ajaran"));
//                    // Properti 'namaMapel' didapat dari tabel mata_pelajaran hasil JOIN
//                    jadwal.setNamaMapel(rs.getString("nama_mapel"));
//
//                    // Tambahkan objek yang sudah diisi ke dalam list
//                    jadwalList.add(new JadwalKelas());
            }
            //}
        } catch (SQLException e) {
            // Tangani error SQL, misalnya dengan mencetak stack trace atau logging
            System.err.println("Error saat mengambil data jadwal: " + e.getMessage());
            e.printStackTrace();
        }

        // Kembalikan list yang berisi data jadwal (bisa kosong jika tidak ada data)
        return jadwalList;
    }
}
