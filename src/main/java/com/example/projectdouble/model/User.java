package com.example.projectdouble.model;

public class User {
    private int idUser;
    private String username;
    private String password; // Seharusnya di-hash di aplikasi nyata, tapi untuk PBO ini bisa langsung
    private int idRole; // Kunci asing ke Role
    private String namaRole; // Untuk memudahkan tampilan

    public User(int idUser, String username, String password, int idRole) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.idRole = idRole;
    }

    public User(int idUser, String username, String password, int idRole, String namaRole) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.idRole = idRole;
        this.namaRole = namaRole;
    }

    // Getters and Setters
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getNamaRole() {
        return namaRole;
    }

    public void setNamaRole(String namaRole) {
        this.namaRole = namaRole;
    }
}
