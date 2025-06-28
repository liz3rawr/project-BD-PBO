package com.example.projectdouble.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AgendaSekolah {
    private int idAgendaSekolah;
    private String judul;
    private String deskripsi;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    private Integer idTahunAjaran; // Foreign Key ke TahunAjaran (ON DELETE SET NULL)
    private String tahunAjaranLengkap; // Untuk tampilan

    // Konstruktor untuk menambahkan agenda baru
    public AgendaSekolah(String judul, String deskripsi, LocalDate tanggalMulai, LocalDate tanggalSelesai, Integer idTahunAjaran) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.idTahunAjaran = idTahunAjaran;
    }

    // Konstruktor untuk mengambil agenda dari database
    public AgendaSekolah(int idAgendaSekolah, String judul, String deskripsi, LocalDate tanggalMulai, LocalDate tanggalSelesai, Integer idTahunAjaran, String tahunAjaranLengkap) {
        this.idAgendaSekolah = idAgendaSekolah;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    // Getters
    public int getIdAgendaSekolah() {
        return idAgendaSekolah;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public Integer getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    // Setters
    public void setIdAgendaSekolah(int idAgendaSekolah) {
        this.idAgendaSekolah = idAgendaSekolah;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public void setIdTahunAjaran(Integer idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    @Override
    public String toString() {
        // Format tampilan agenda, misalnya "Judul: [Tanggal Mulai] - [Tanggal Selesai]"
        return judul + " (" + tanggalMulai.format(DateTimeFormatter.ofPattern("dd MMM")) + " - " + tanggalSelesai.format(DateTimeFormatter.ofPattern("dd MMM")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendaSekolah that = (AgendaSekolah) o;
        return idAgendaSekolah == that.idAgendaSekolah;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAgendaSekolah);
    }
}
