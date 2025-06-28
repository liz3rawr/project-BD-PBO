package com.example.projectdouble.model;

import java.util.Objects;

public class MataPelajaran {
    private int idMapel;
    private String namaMapel;
    private String jenjangKelas;

    public MataPelajaran(int idMapel, String namaMapel, String jenjangKelas) {
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.jenjangKelas = jenjangKelas;
    }

    // Getters
    public int getIdMapel() {
        return idMapel;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public String getJenjangKelas() {
        return jenjangKelas;
    }

    // Setters
    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public void setJenjangKelas(String jenjangKelas) {
        this.jenjangKelas = jenjangKelas;
    }

    @Override
    public String toString() {
        return namaMapel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MataPelajaran that = (MataPelajaran) o;
        return idMapel == that.idMapel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMapel);
    }
}
