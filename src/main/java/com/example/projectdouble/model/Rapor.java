package com.example.projectdouble.model;

import java.time.LocalDate;
import java.util.Objects;

public class Rapor {
    private int idRapor;
    private String nisSiswa; // Foreign Key ke Siswa
    private String namaSiswa; // Untuk tampilan
    private Integer idTahunAjaran; // Foreign Key ke TahunAjaran
    private String tahunAjaranLengkap; // Untuk tampilan
    private LocalDate tanggalCetak;

    // Konstruktor untuk membuat entri rapor baru
    public Rapor(String nisSiswa, Integer idTahunAjaran, LocalDate tanggalCetak) {
        this.nisSiswa = nisSiswa;
        this.idTahunAjaran = idTahunAjaran;
        this.tanggalCetak = tanggalCetak;
    }

    // Konstruktor untuk mengambil data rapor dari database
    public Rapor(int idRapor, String nisSiswa, String namaSiswa, Integer idTahunAjaran, String tahunAjaranLengkap, LocalDate tanggalCetak) {
        this.idRapor = idRapor;
        this.nisSiswa = nisSiswa;
        this.namaSiswa = namaSiswa;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
        this.tanggalCetak = tanggalCetak;
    }

    // Getters
    public int getIdRapor() {
        return idRapor;
    }

    public String getNisSiswa() {
        return nisSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public Integer getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    public LocalDate getTanggalCetak() {
        return tanggalCetak;
    }

    // Setters
    public void setIdRapor(int idRapor) {
        this.idRapor = idRapor;
    }

    public void setNisSiswa(String nisSiswa) {
        this.nisSiswa = nisSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setIdTahunAjaran(Integer idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    public void setTanggalCetak(LocalDate tanggalCetak) {
        this.tanggalCetak = tanggalCetak;
    }

    @Override
    public String toString() {
        return "Rapor{" +
                "idRapor=" + idRapor +
                ", nisSiswa='" + nisSiswa + '\'' +
                ", tahunAjaranLengkap='" + tahunAjaranLengkap + '\'' +
                ", tanggalCetak=" + tanggalCetak +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rapor rapor = (Rapor) o;
        return idRapor == rapor.idRapor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRapor);
    }
}
