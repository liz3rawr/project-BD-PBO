package com.example.projectdouble.model;

import java.util.Objects;

public class Ekstrakurikuler {
    private int idEkstrakurikuler;
    private String nama;
    private String tingkat;
    private String mentorNames;

    public Ekstrakurikuler(String nama, String tingkat) {
        this.nama = nama;
        this.tingkat = tingkat;
    }

    public Ekstrakurikuler(int idEkstrakurikuler, String nama, String tingkat) {
        this.idEkstrakurikuler = idEkstrakurikuler;
        this.nama = nama;
        this.tingkat = tingkat;
    }

    public Ekstrakurikuler(int idEkstrakurikuler, String nama, String tingkat, String namaPembina) {
        this.idEkstrakurikuler = idEkstrakurikuler;
        this.nama = nama;
        this.tingkat = tingkat;
        this.mentorNames = namaPembina;
    }


    // Getters
    public int getIdEkstrakurikuler() {
        return idEkstrakurikuler;
    }

    public String getNama() {
        return nama;
    }

    public String getTingkat() {
        return tingkat;
    }

    public String getMentorNames() {
        return mentorNames;
    }

    // Setters
    public void setIdEkstrakurikuler(int idEkstrakurikuler) {
        this.idEkstrakurikuler = idEkstrakurikuler;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTingkat(String tingkat) {
        this.tingkat = tingkat;
    }

    public void setMentorNames(String namaPembina) {
        this.mentorNames = namaPembina;
    }

    @Override
    public String toString() {
        return nama + (tingkat != null && !tingkat.isEmpty() ? " (" + tingkat + ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ekstrakurikuler that = (Ekstrakurikuler) o;
        return idEkstrakurikuler == that.idEkstrakurikuler;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEkstrakurikuler);
    }
}
