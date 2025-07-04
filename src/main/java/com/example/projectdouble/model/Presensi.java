package com.example.projectdouble.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Presensi {
    private int idPresensi;
    private LocalDate tanggal;
    private String status;
    private String nisSiswa;
    private String namaSiswa;
    private Integer idKelas;
    private String namaKelas;
    private String tahunAjaranLengkap;

    public Presensi(int idPresensi, LocalDate tanggal, String status, String nisSiswa, Integer idKelas) {
        this.idPresensi = idPresensi;
        this.tanggal = tanggal;
        this.status = status;
        this.nisSiswa = nisSiswa;
        this.idKelas = idKelas;
    }

    public Presensi(int idPresensi, LocalDate tanggal, String status, String nisSiswa, String namaSiswa,
                    Integer idKelas, String namaKelas, String tahunAjaranLengkap) {
        this.idPresensi = idPresensi;
        this.tanggal = tanggal;
        this.status = status;
        this.nisSiswa = nisSiswa;
        this.namaSiswa = namaSiswa;
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    // Getters
    public int getIdPresensi() {
        return idPresensi;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    public String getNisSiswa() {
        return nisSiswa;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public Integer getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    // Setters
    public void setIdPresensi(int idPresensi) {
        this.idPresensi = idPresensi;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNisSiswa(String nisSiswa) {
        this.nisSiswa = nisSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public void setIdKelas(Integer idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Presensi presensi = (Presensi) o;
        return idPresensi == presensi.idPresensi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPresensi);
    }
}
