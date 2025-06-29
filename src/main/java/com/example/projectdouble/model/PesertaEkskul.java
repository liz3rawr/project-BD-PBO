package com.example.projectdouble.model;

import java.util.Objects;

public class PesertaEkskul {
    private int idPeserta;
    private String nisSiswa;
    private String namaSiswa;
    private Integer idKelasSiswa;
    private String namaKelasSiswa;
    private Integer idTahunAjaranSiswa;
    private int idEkstrakurikuler;
    private String namaEkstrakurikuler;
    private String tingkatEkstrakurikuler;

    public PesertaEkskul(String nisSiswa, int idEkstrakurikuler) {
        this.nisSiswa = nisSiswa;
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

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
