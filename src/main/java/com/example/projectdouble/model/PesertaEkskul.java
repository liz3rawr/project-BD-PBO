package com.example.projectdouble.model;

import java.util.Objects;

public class PesertaEkskul {
    private int idPeserta;
    private String nisSiswa; // Foreign Key ke Siswa
    private String namaSiswa; // Untuk tampilan
    private Integer idKelasSiswa; // Untuk filter jika diperlukan
    private String namaKelasSiswa; // Untuk tampilan
    private Integer idTahunAjaranSiswa; // Untuk filter jika diperlukan
    private int idEkstrakurikuler; // Foreign Key ke Ekstrakurikuler
    private String namaEkstrakurikuler; // Untuk tampilan
    private String tingkatEkstrakurikuler; // Untuk tampilan

    // Konstruktor untuk menambahkan peserta ekstrakurikuler baru
    public PesertaEkskul(String nisSiswa, int idEkstrakurikuler) {
        this.nisSiswa = nisSiswa;
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

    // Konstruktor untuk mengambil data peserta ekstrakurikuler dari database
    public PesertaEkskul(int idPeserta, String nisSiswa, String namaSiswa,
                         Integer idKelasSiswa, String namaKelasSiswa, Integer idTahunAjaranSiswa,
                         int idEkstrakurikuler, String namaEkstrakurikuler, String tingkatEkstrakurikuler) {
        this.idPeserta = idPeserta;
        this.nisSiswa = nisSiswa;
        this.namaSiswa = namaSiswa;
        this.idKelasSiswa = idKelasSiswa;
        this.namaKelasSiswa = namaKelasSiswa;
        this.idTahunAjaranSiswa = idTahunAjaranSiswa;
        this.idEkstrakurikuler = idEkstrakurikuler;
        this.namaEkstrakurikuler = namaEkstrakurikuler;
        this.tingkatEkstrakurikuler = tingkatEkstrakurikuler;
        //this.kelasdanTahunAjaran = namaKelasSiswa +"("+
    }

    // Getters
    public int getIdPeserta() {
        return idPeserta;
    }

    public String getNisSiswa() {
        return nisSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public Integer getIdKelasSiswa() {
        return idKelasSiswa;
    }

    public String getNamaKelasSiswa() {
        return namaKelasSiswa;
    }

    public Integer getIdTahunAjaranSiswa() {
        return idTahunAjaranSiswa;
    }

    public int getIdEkstrakurikuler() {
        return idEkstrakurikuler;
    }

    public String getNamaEkstrakurikuler() {
        return namaEkstrakurikuler;
    }

    public String getTingkatEkstrakurikuler() {
        return tingkatEkstrakurikuler;
    }

    // Setters
    public void setIdPeserta(int idPeserta) {
        this.idPeserta = idPeserta;
    }

    public void setNisSiswa(String nisSiswa) {
        this.nisSiswa = nisSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setIdKelasSiswa(Integer idKelasSiswa) {
        this.idKelasSiswa = idKelasSiswa;
    }

    public void setNamaKelasSiswa(String namaKelasSiswa) {
        this.namaKelasSiswa = namaKelasSiswa;
    }

    public void setIdTahunAjaranSiswa(Integer idTahunAjaranSiswa) {
        this.idTahunAjaranSiswa = idTahunAjaranSiswa;
    }

    public void setIdEkstrakurikuler(int idEkstrakurikuler) {
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

    public void setNamaEkstrakurikuler(String namaEkstrakurikuler) {
        this.namaEkstrakurikuler = namaEkstrakurikuler;
    }

    public void setTingkatEkstrakurikuler(String tingkatEkstrakurikuler) {
        this.tingkatEkstrakurikuler = tingkatEkstrakurikuler;
    }

    @Override
    public String toString() {
        return namaSiswa + " - " + namaEkstrakurikuler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PesertaEkskul that = (PesertaEkskul) o;
        return idPeserta == that.idPeserta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPeserta);
    }
}
