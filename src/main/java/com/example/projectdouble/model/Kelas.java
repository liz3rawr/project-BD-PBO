package com.example.projectdouble.model;

import java.util.Objects;

public class Kelas {
    private int idKelas;
    private String namaKelas;
    private String tingkat;
    private String nipWaliKelas;
    private String namaWaliKelas;
    private int idTahunAjaran;
    private String tahunAjaranLengkap;

    public Kelas(String namaKelas, String tingkat, String nipWaliKelas, int idTahunAjaran) {
        this.namaKelas = namaKelas;
        this.tingkat = tingkat;
        this.nipWaliKelas = nipWaliKelas;
        this.idTahunAjaran = idTahunAjaran;
    }

    public Kelas(int idKelas, String namaKelas, String tingkat, String nipWaliKelas, String namaWaliKelas, int idTahunAjaran, String tahunAjaranLengkap) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.tingkat = tingkat;
        this.nipWaliKelas = nipWaliKelas;
        this.namaWaliKelas = namaWaliKelas;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    // Getters
    public int getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getTingkat() {
        return tingkat;
    }

    public String getNipWaliKelas() {
        return nipWaliKelas;
    }

    public String getNamaWaliKelas() {
        return namaWaliKelas;
    }

    public int getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    // Setters
    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void setTingkat(String tingkat) {
        this.tingkat = tingkat;
    }

    public void setNipWaliKelas(String nipWaliKelas) {
        this.nipWaliKelas = nipWaliKelas;
    }

    public void setNamaWaliKelas(String namaWaliKelas) {
        this.namaWaliKelas = namaWaliKelas;
    }

    public void setIdTahunAjaran(int idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    @Override
    public String toString() {
        return namaKelas + " (TA: " + tahunAjaranLengkap + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kelas kelas = (Kelas) o;
        return idKelas == kelas.idKelas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKelas);
    }
}
