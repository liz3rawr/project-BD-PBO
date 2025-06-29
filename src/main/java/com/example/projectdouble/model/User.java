package com.example.projectdouble.model;

import java.util.Objects;

public class User {
    private int idUser;
    private String username;
    private String password;
    private Role role;
    private String displayName; //nama guru/siswa

    public User(int idUser, String username, String password, Role role) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Setters
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", role=" + (role != null ? role.getNamaRole() : "N/A") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idUser == user.idUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser);
    }
}
