package com.example.projectdouble.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class NilaiUjian {
    private int idNilaiUjian;
    private String jenisUjian;
    private BigDecimal nilai;
    private int idMapel;
    private String nis;
    private Integer idTahunAjaran;

    private String namaMapel;
    private String namaSiswa;
    private String tahunAjaranLengkap;

    public NilaiUjian(int idNilaiUjian, String jenisUjian, BigDecimal nilai, int idMapel, String nis, Integer idTahunAjaran) {
        this.idNilaiUjian = idNilaiUjian;
        this.jenisUjian = jenisUjian;
        this.nilai = nilai;
        this.idMapel = idMapel;
        this.nis = nis;
        this.idTahunAjaran = idTahunAjaran;
    }

    public NilaiUjian(int idNilaiUjian, String jenisUjian, BigDecimal nilai,
                      int idMapel, String namaMapel, String nis, String namaSiswa,
                      Integer idTahunAjaran, String tahunAjaranLengkap) {
        this.idNilaiUjian = idNilaiUjian;
        this.jenisUjian = jenisUjian;
        this.nilai = nilai;
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.nis = nis;
        this.namaSiswa = namaSiswa;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    public NilaiUjian(int idNilaiUjian, String jenisUjian, BigDecimal nilai, Integer idMapel, String namaMapel, String nis, String namaSiswa, Integer idTahunAjaran, String tahunAjaranLengkap) {
        this.idNilaiUjian = idNilaiUjian;
        this.jenisUjian = jenisUjian;
        this.nilai = nilai;
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.nis = nis;
        this.namaSiswa = namaSiswa;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    // Getters and Setters
    public int getIdNilaiUjian() {
        return idNilaiUjian;
    }

    public void setIdNilaiUjian(int idNilaiUjian) {
        this.idNilaiUjian = idNilaiUjian;
    }

    public String getJenisUjian() {
        return jenisUjian;
    }

    public void setJenisUjian(String jenisUjian) {
        this.jenisUjian = jenisUjian;
    }

    public BigDecimal getNilai() {
        return nilai;
    }

    public void setNilai(BigDecimal nilai) {
        this.nilai = nilai;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public Integer getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public void setIdTahunAjaran(Integer idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NilaiUjian that = (NilaiUjian) o;
        return idNilaiUjian == that.idNilaiUjian;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNilaiUjian);
    }

    public static NilaiUjian fromResultSet(ResultSet rs) throws SQLException {
        String tahunAjaranLengkap = null;
        Integer idTahunAjaranFromRs = rs.getObject("id_tahun_ajaran", Integer.class);
        if (rs.getObject("tahun_mulai") != null && rs.getObject("tahun_selesai") != null) {
            tahunAjaranLengkap = rs.getInt("tahun_mulai") + "/" + rs.getInt("tahun_selesai");
        }
        return new NilaiUjian(
                rs.getInt("id_nilai_ujian"),
                rs.getString("jenis_ujian"),
                rs.getBigDecimal("nilai"),
                rs.getObject("id_mapel", Integer.class),
                rs.getString("nama_mapel"),
                rs.getString("nis"),
                rs.getString("nama_siswa"),
                idTahunAjaranFromRs,
                tahunAjaranLengkap
        );
    }
}
