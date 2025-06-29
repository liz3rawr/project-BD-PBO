package com.example.projectdouble.DAO;

import com.example.projectdouble.model.AgendaSekolah;
import com.example.projectdouble.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendaSekolahDAO {

    public boolean addAgendaSekolah(AgendaSekolah agenda) {
        String sql = "INSERT INTO agenda_sekolah (judul, deskripsi, tanggal_mulai, tanggal_selesai, id_tahun_ajaran) VALUES (?, ?, ?, ?, ?) RETURNING id_agenda_sekolah";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, agenda.getJudul());
            stmt.setString(2, agenda.getDeskripsi());
            stmt.setDate(3, Date.valueOf(agenda.getTanggalMulai()));
            stmt.setDate(4, Date.valueOf(agenda.getTanggalSelesai()));
            if (agenda.getIdTahunAjaran() != null) {
                stmt.setInt(5, agenda.getIdTahunAjaran());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        agenda.setIdAgendaSekolah(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan agenda sekolah: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<AgendaSekolah> getAllAgendaSekolah() {
        List<AgendaSekolah> agendaList = new ArrayList<>();
        String sql = "SELECT ag.id_agenda_sekolah, ag.judul, ag.deskripsi, ag.tanggal_mulai, ag.tanggal_selesai, ag.id_tahun_ajaran, " +
                "ta.tahun_mulai, ta.tahun_selesai " +
                "FROM agenda_sekolah ag " +
                "LEFT JOIN tahun_ajaran ta ON ag.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "ORDER BY ag.tanggal_mulai DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Integer idTahunAjaran = (Integer) rs.getObject("id_tahun_ajaran");
                String tahunAjaranLengkap = null;
                if (idTahunAjaran != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                agendaList.add(new AgendaSekolah(
                        rs.getInt("id_agenda_sekolah"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_mulai").toLocalDate(),
                        rs.getDate("tanggal_selesai").toLocalDate(),
                        idTahunAjaran,
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua agenda sekolah: " + e.getMessage());
            e.printStackTrace();
        }
        return agendaList;
    }

    public AgendaSekolah getAgendaSekolahById(int idAgendaSekolah) {
        String sql = "SELECT ag.id_agenda_sekolah, ag.judul, ag.deskripsi, ag.tanggal_mulai, ag.tanggal_selesai, ag.id_tahun_ajaran, " +
                "ta.tahun_mulai, ta.tahun_selesai " +
                "FROM agenda_sekolah ag " +
                "LEFT JOIN tahun_ajaran ta ON ag.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE ag.id_agenda_sekolah = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAgendaSekolah);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Integer idTahunAjaran = (Integer) rs.getObject("id_tahun_ajaran");
                String tahunAjaranLengkap = null;
                if (idTahunAjaran != null) {
                    tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
                }
                return new AgendaSekolah(
                        rs.getInt("id_agenda_sekolah"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_mulai").toLocalDate(),
                        rs.getDate("tanggal_selesai").toLocalDate(),
                        idTahunAjaran,
                        tahunAjaranLengkap
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil agenda sekolah berdasarkan ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<AgendaSekolah> getAgendaByTahunAjaran(int idTahunAjaran) {
        List<AgendaSekolah> agendaList = new ArrayList<>();
        String sql = "SELECT ag.id_agenda_sekolah, ag.judul, ag.deskripsi, ag.tanggal_mulai, ag.tanggal_selesai, ag.id_tahun_ajaran, " +
                "ta.tahun_mulai, ta.tahun_selesai " +
                "FROM agenda_sekolah ag " +
                "LEFT JOIN tahun_ajaran ta ON ag.id_tahun_ajaran = ta.id_tahun_ajaran " +
                "WHERE ag.id_tahun_ajaran = ? ORDER BY ag.tanggal_mulai DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTahunAjaran);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");

                agendaList.add(new AgendaSekolah(
                        rs.getInt("id_agenda_sekolah"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        rs.getDate("tanggal_mulai").toLocalDate(),
                        rs.getDate("tanggal_selesai").toLocalDate(),
                        rs.getInt("id_tahun_ajaran"),
                        tahunAjaranLengkap
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil agenda sekolah berdasarkan tahun ajaran: " + e.getMessage());
            e.printStackTrace();
        }
        return agendaList;
    }

    public boolean updateAgendaSekolah(AgendaSekolah agenda) {
        String sql = "UPDATE agenda_sekolah SET judul = ?, deskripsi = ?, tanggal_mulai = ?, tanggal_selesai = ?, id_tahun_ajaran = ? WHERE id_agenda_sekolah = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, agenda.getJudul());
            stmt.setString(2, agenda.getDeskripsi());
            stmt.setDate(3, Date.valueOf(agenda.getTanggalMulai()));
            stmt.setDate(4, Date.valueOf(agenda.getTanggalSelesai()));
            if (agenda.getIdTahunAjaran() != null) {
                stmt.setInt(5, agenda.getIdTahunAjaran());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setInt(6, agenda.getIdAgendaSekolah());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat memperbarui agenda sekolah: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAgendaSekolah(int idAgendaSekolah) {
        String sql = "DELETE FROM agenda_sekolah WHERE id_agenda_sekolah = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAgendaSekolah);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus agenda sekolah: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
