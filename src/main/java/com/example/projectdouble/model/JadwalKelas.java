package com.example.projectdouble.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JadwalKelas {
    private int idJadwalKelas;
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private int idKelas;
    private String namaKelas;
    private int idMapel;
    private String namaMapel;
    private String nipGuru;
    private String namaGuru;
    private int idTahunAjaran;
    private String tahunAjaranLengkap;
    private String kelasDanTahunAjaran;

    public JadwalKelas(String hari, LocalTime jamMulai, LocalTime jamSelesai, int idKelas, int idMapel, String nipGuru, int idTahunAjaran) {
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.idKelas = idKelas;
        this.idMapel = idMapel;
        this.nipGuru = nipGuru;
        this.idTahunAjaran = idTahunAjaran;
    }

    public JadwalKelas(int idJadwalKelas, String hari, LocalTime jamMulai, LocalTime jamSelesai,
                       int idKelas, String namaKelas, int idMapel, String namaMapel,
                       String nipGuru, String namaGuru, int idTahunAjaran, String tahunAjaranLengkap) {
        this.idJadwalKelas = idJadwalKelas;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.nipGuru = nipGuru;
        this.namaGuru = namaGuru;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
        this.kelasDanTahunAjaran = namaKelas + " (" + tahunAjaranLengkap + ")";
    }

    // Getters
    public int getIdJadwalKelas() {
        return idJadwalKelas;
    }

    public String getHari() {
        return hari;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public String getNipGuru() {
        return nipGuru;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    public int getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    public String getKelasDanTahunAjaran() {
        return kelasDanTahunAjaran;
    }

    // Setters
    public void setIdJadwalKelas(int idJadwalKelas) {
        this.idJadwalKelas = idJadwalKelas;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
        this.kelasDanTahunAjaran = namaKelas + " (" + this.tahunAjaranLengkap + ")";
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public void setNipGuru(String nipGuru) {
        this.nipGuru = nipGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }

    public void setIdTahunAjaran(int idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
        this.kelasDanTahunAjaran = this.namaKelas + " (" + tahunAjaranLengkap + ")";
    }

    public void setKelasDanTahunAjaran(String kelasDanTahunAjaran) {
        this.kelasDanTahunAjaran = kelasDanTahunAjaran;
    }

    @Override
    public String toString() {
        return namaMapel + " (" + kelasDanTahunAjaran + ") - " + hari + ", " + jamMulai + "-" + jamSelesai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JadwalKelas that = (JadwalKelas) o;
        return idJadwalKelas == that.idJadwalKelas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJadwalKelas);
    }

    public String getTimeRange() {
        if (jamMulai != null && jamSelesai != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return jamMulai.format(formatter) + " - " + jamSelesai.format(formatter);
        }
        return "";
    }
}
