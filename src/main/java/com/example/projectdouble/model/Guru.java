package com.example.projectdouble.model;

import java.util.Objects;

public class Guru {
    private String nip;
    private Integer idUser;
    private String nama;
    private String jenisKelamin;
    private String email;
    private String noHp;
    private String usernameUser;
    private String passwordUser;

    public Guru(String nip, Integer idUser, String nama, String jenisKelamin, String email, String noHp, String usernameUser, String passwordUser) {
        this.nip = nip;
        this.idUser = idUser;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
        this.noHp = noHp;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
    }

    public Guru(String nip, String nama, String jenisKelamin, String email, String noHp) {
        this.nip = nip;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
        this.noHp = noHp;
        this.idUser = null;
        this.usernameUser = null;
        this.passwordUser = null;
    }

    // Getters
    public String getNip() {
        return nip;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getNama() {
        return nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getEmail() {
        return email;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    // Setters
    public void setNip(String nip) {
        this.nip = nip;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    @Override
    public String toString() {
        return nama + " (" + nip + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guru guru = (Guru) o;
        return Objects.equals(nip, guru.nip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nip);
    }
}
