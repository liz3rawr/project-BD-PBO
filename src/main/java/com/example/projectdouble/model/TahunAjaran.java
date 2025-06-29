package com.example.projectdouble.model;

import java.util.Objects;

public class TahunAjaran {
    private int idTahunAjaran;
    private int tahunMulai;
    private int tahunSelesai;
    private String tahunAjaranLengkap; //"2023/2024"

    public TahunAjaran(int idTahunAjaran, int tahunMulai, int tahunSelesai) {
        this.idTahunAjaran = idTahunAjaran;
        this.tahunMulai = tahunMulai;
        this.tahunSelesai = tahunSelesai;
        this.tahunAjaranLengkap = tahunMulai + "/" + tahunSelesai;
    }

    // Getters
    public int getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public int getTahunMulai() {
        return tahunMulai;
    }

    public int getTahunSelesai() {
        return tahunSelesai;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    // Setters
    public void setIdTahunAjaran(int idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunMulai(int tahunMulai) {
        this.tahunMulai = tahunMulai;
        this.tahunAjaranLengkap = tahunMulai + "/" + this.tahunSelesai;
    }

    public void setTahunSelesai(int tahunSelesai) {
        this.tahunSelesai = tahunSelesai;
        this.tahunAjaranLengkap = this.tahunMulai + "/" + tahunSelesai;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    @Override
    public String toString() {
        return tahunAjaranLengkap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TahunAjaran that = (TahunAjaran) o;
        return idTahunAjaran == that.idTahunAjaran;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTahunAjaran);
    }
}
