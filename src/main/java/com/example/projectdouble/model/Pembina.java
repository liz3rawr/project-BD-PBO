package com.example.projectdouble.model;

import java.util.Objects;

public class Pembina {
    private int idPembina;
    private String nipGuru;
    private String namaGuru;
    private int idEkstrakurikuler;
    private String namaEkstrakurikuler;

    public Pembina(String nipGuru, int idEkstrakurikuler) {
        this.nipGuru = nipGuru;
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

    public Pembina(int idPembina, String nipGuru, String namaGuru, int idEkstrakurikuler, String namaEkstrakurikuler) {
        this.idPembina = idPembina;
        this.nipGuru = nipGuru;
        this.namaGuru = namaGuru;
        this.idEkstrakurikuler = idEkstrakurikuler;
        this.namaEkstrakurikuler = namaEkstrakurikuler;
    }

    // Getters
    public int getIdPembina() {
        return idPembina;
    }

    public String getNipGuru() {
        return nipGuru;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    public int getIdEkstrakurikuler() {
        return idEkstrakurikuler;
    }

    public String getNamaEkstrakurikuler() {
        return namaEkstrakurikuler;
    }

    // Setters
    public void setIdPembina(int idPembina) {
        this.idPembina = idPembina;
    }

    public void setNipGuru(String nipGuru) {
        this.nipGuru = nipGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }

    public void setIdEkstrakurikuler(int idEkstrakurikuler) {
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

    public void setNamaEkstrakurikuler(String namaEkstrakurikuler) {
        this.namaEkstrakurikuler = namaEkstrakurikuler;
    }

    @Override
    public String toString() {
        return namaGuru + " - " + namaEkstrakurikuler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pembina pembina = (Pembina) o;
        return idPembina == pembina.idPembina;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPembina);
    }
}
