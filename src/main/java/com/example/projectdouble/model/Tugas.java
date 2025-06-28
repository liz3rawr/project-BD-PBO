package com.example.projectdouble.model;

import java.time.LocalDate;
import java.util.Objects;

public class Tugas {
    private int idTugas;
    private String judul;
    private String deskripsi;
    private LocalDate tanggalDeadline;
    private Integer idMapel; // Foreign Key ke MataPelajaran
    private String namaMapel; // Untuk tampilan
    private Integer idKelas; // Foreign Key ke Kelas
    private String namaKelas; // Untuk tampilan
    private String nipGuru; // Foreign Key ke Guru
    private String namaGuru; // Untuk tampilan

    // Konstruktor untuk mengambil dari database (dengan semua detail join yang ada di DDL terbaru)
    // id_tahun_ajaran dan tahun_ajaran_lengkap tidak lagi di constructor ini
    public Tugas(int idTugas, String judul, String deskripsi, LocalDate tanggalDeadline,
                 Integer idMapel, String namaMapel,
                 Integer idKelas, String namaKelas,
                 String nipGuru, String namaGuru) {
        this.idTugas = idTugas;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalDeadline = tanggalDeadline;
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.nipGuru = nipGuru;
        this.namaGuru = namaGuru;
    }

    // Konstruktor untuk menambahkan tugas baru (ID di-generate DB)
    // Parameter idTahunAjaran dihapus dari sini
    public Tugas(String judul, String deskripsi, LocalDate tanggalDeadline,
                 Integer idMapel, Integer idKelas, String nipGuru) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalDeadline = tanggalDeadline;
        this.idMapel = idMapel;
        this.idKelas = idKelas;
        this.nipGuru = nipGuru;
    }

    // Konstruktor untuk menambahkan tugas baru (dengan ID, tapi ID seringkali auto-generated)
    // Parameter idTahunAjaran dihapus dari sini
    public Tugas(int idTugas, String judul, String deskripsi, LocalDate tanggalDeadline,
                 Integer idMapel, Integer idKelas, String nipGuru) {
        this.idTugas = idTugas;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalDeadline = tanggalDeadline;
        this.idMapel = idMapel;
        this.idKelas = idKelas;
        this.nipGuru = nipGuru;
    }

    // Getters
    public int getIdTugas() {
        return idTugas;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public LocalDate getTanggalDeadline() {
        return tanggalDeadline;
    }

    public Integer getIdMapel() {
        return idMapel;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public Integer getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getNipGuru() {
        return nipGuru;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    // Setters
    public void setIdTugas(int idTugas) {
        this.idTugas = idTugas;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setTanggalDeadline(LocalDate tanggalDeadline) {
        this.tanggalDeadline = tanggalDeadline;
    }

    public void setIdMapel(Integer idMapel) {
        this.idMapel = idMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public void setIdKelas(Integer idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void setNipGuru(String nipGuru) {
        this.nipGuru = nipGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }

    @Override
    public String toString() {
        return judul + " (Deadline: " + (tanggalDeadline != null ? tanggalDeadline : "N/A") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tugas tugas = (Tugas) o;
        return idTugas == tugas.idTugas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTugas);
    }
}
