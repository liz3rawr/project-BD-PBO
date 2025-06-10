package com.example.projectdouble.model;

import java.time.LocalDate;

public class Siswa {
    private String nis;
    private int idUser;
    private String nama;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir; // Menggunakan LocalDate untuk tanggal
    private String alamat;
    private String username; // Untuk memudahkan data terkait user

    public Siswa(String nis, int idUser, String nama, String jenisKelamin, String tempatLahir, LocalDate tanggalLahir, String alamat) {
        this.nis = nis;
        this.idUser = idUser;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
    }

    public Siswa(String nis, int idUser, String nama, String jenisKelamin, String tempatLahir, LocalDate tanggalLahir, String alamat, String username) {
        this.nis = nis;
        this.idUser = idUser;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.username = username;
    }

    // Getters and Setters
    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
