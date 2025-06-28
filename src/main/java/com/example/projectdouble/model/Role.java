package com.example.projectdouble.model;

import java.util.Objects;

public class Role {
    private int idRole;
    private String namaRole;

    public Role(int idRole, String namaRole) {
        this.idRole = idRole;
        this.namaRole = namaRole;
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

    @Override
    public String toString() {
        return namaRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return idRole == role.idRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole);
    }
}
