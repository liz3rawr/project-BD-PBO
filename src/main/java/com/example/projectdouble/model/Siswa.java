package com.example.projectdouble.model;

import com.example.projectdouble.DAO.UserDAO;

import java.time.LocalDate;
import java.util.Objects;

public class Siswa {
    private String nis; // Primary Key
    private String nama;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String alamat;
    private Integer idKelas;
    private String namaKelas;
    private Integer idTahunAjaran;
    private String tahunAjaranLengkap;
    private Integer idUser;
    private String usernameUser;
    private String passwordUser;

    public Siswa(String nis, String nama, String jenisKelamin, String tempatLahir, LocalDate tanggalLahir, String alamat,
                 Integer idKelas, String namaKelas, Integer idTahunAjaran, String tahunAjaranLengkap, Integer idUser, String usernameUser, String passwordUser) {
        this.nis = nis;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.idTahunAjaran = idTahunAjaran;
        this.tahunAjaranLengkap = tahunAjaranLengkap;
        this.idUser = idUser;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
    }

    public Siswa(String nis, String nama, String jenisKelamin, String tempatLahir, LocalDate tanggalLahir, String alamat) {
        this.nis = nis;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.idKelas = null;
        this.namaKelas = null;
        this.idTahunAjaran = null;
        this.tahunAjaranLengkap = null;
        this.idUser = null;
        this.usernameUser = null;
        this.passwordUser = null;
    }

    public Siswa(String nis, String nama, String jenisKelamin, String tempatLahir, LocalDate tanggalLahir, String alamat,
                 Integer idKelas, Integer idTahunAjaran) {
        this.nis = nis;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tempatLahir = tempatLahir;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.idKelas = idKelas;
        this.idTahunAjaran = idTahunAjaran;
        this.idUser = null;
        this.usernameUser = null;
        this.passwordUser = null;
    }


    // Getters
    public String getNis() {
        return nis;
    }

    public String getNama() {
        return nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public Integer getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public Integer getIdTahunAjaran() {
        return idTahunAjaran;
    }

    public String getTahunAjaranLengkap() {
        return tahunAjaranLengkap;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    // Setters
    public void setNis(String nis) {
        this.nis = nis;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setIdKelas(Integer idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void setIdTahunAjaran(Integer idTahunAjaran) {
        this.idTahunAjaran = idTahunAjaran;
    }

    public void setTahunAjaranLengkap(String tahunAjaranLengkap) {
        this.tahunAjaranLengkap = tahunAjaranLengkap;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    @Override
    public String toString() {
        return nama + " (" + nis + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Siswa siswa = (Siswa) o;
        return Objects.equals(nis, siswa.nis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nis);
    }
}
