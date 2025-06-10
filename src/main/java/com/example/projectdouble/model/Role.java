package com.example.projectdouble.model;

public class Role {
    private int idRole;
    private String namaRole;

    public Role(int idRole, String namaRole) {
        this.idRole = idRole;
        this.namaRole = namaRole;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return namaRole; // Penting untuk ComboBox di JavaFX
    }
}
