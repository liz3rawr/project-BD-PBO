package com.example.projectdouble.model;

public class Guru {
    private String nip;
    private int idUser;
    private String nama;
    private String jenisKelamin;
    private String email;
    private String noHp;
    private String username; // Untuk memudahkan data terkait user

    public Guru(String nip, int idUser, String nama, String jenisKelamin, String email, String noHp) {
        this.nip = nip;
        this.idUser = idUser;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
        this.noHp = noHp;
    }

    public Guru(String nip, int idUser, String nama, String jenisKelamin, String email, String noHp, String username) {
        this.nip = nip;
        this.idUser = idUser;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
        this.noHp = noHp;
        this.username = username;
    }

    // Getters and Setters
    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return nama + " (" + nip + ")"; // Berguna untuk ComboBox di FXML
    }
}
