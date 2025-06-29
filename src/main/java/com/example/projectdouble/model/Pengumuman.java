package com.example.projectdouble.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pengumuman {
    private int idPengumuman;
    private String judul;
    private String deskripsi;
    private LocalDateTime tanggal;
    private String lampiran;

    public Pengumuman(String judul, String deskripsi, LocalDateTime tanggal, String lampiran) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.lampiran = lampiran;
    }

    public Pengumuman(int idPengumuman, String judul, String deskripsi, LocalDateTime tanggal, String lampiran) {
        this.idPengumuman = idPengumuman;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.lampiran = lampiran;
    }

    // Getters
    public int getIdPengumuman() {
        return idPengumuman;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public String getLampiran() {
        return lampiran;
    }

    // Setters
    public void setIdPengumuman(int idPengumuman) {
        this.idPengumuman = idPengumuman;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public void setLampiran(String lampiran) {
        this.lampiran = lampiran;
    }

    @Override
    public String toString() {
        return judul + " (Tanggal: " + tanggal + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pengumuman that = (Pengumuman) o;
        return idPengumuman == that.idPengumuman;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPengumuman);
    }
}
