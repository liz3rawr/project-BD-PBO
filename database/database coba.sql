CREATE TABLE role (
    id_role SERIAL PRIMARY KEY,
    nama_role VARCHAR(50) UNIQUE NOT NULL
);

-- Tabel USER
CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_role INT NOT NULL,
    FOREIGN KEY (id_role) REFERENCES role (id_role) ON DELETE CASCADE
);

-- Tabel SISWA
CREATE TABLE siswa (
    nis VARCHAR(20) PRIMARY KEY, -- Nomor Induk Siswa (Primary Key)
    id_user INT UNIQUE, -- Kunci asing ke tabel USER
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(10),
    tempat_lahir VARCHAR(100),
    tanggal_lahir DATE,
    alamat TEXT,
    FOREIGN KEY (id_user) REFERENCES users (id_user) ON DELETE CASCADE
);

-- Tabel GURU
CREATE TABLE guru (
    nip VARCHAR(20) PRIMARY KEY, -- Nomor Induk Pegawai (Primary Key)
    id_user INT UNIQUE, -- Kunci asing ke tabel USER
    nama VARCHAR(255) NOT NULL,
    jenis_kelamin VARCHAR(10),
    email VARCHAR(255),
    no_hp VARCHAR(20),
    FOREIGN KEY (id_user) REFERENCES users (id_user) ON DELETE CASCADE
);

-- Tabel TAHUN_AJARAN
CREATE TABLE tahun_ajaran (
    id_tahun_ajaran SERIAL PRIMARY KEY,
    tahun_mulai INT NOT NULL,
    tahun_selesai INT NOT NULL,
    tahun_ganjil_genap VARCHAR(10) NOT NULL -- Contoh: 'Ganjil', 'Genap'
);

-- Tabel KELAS
CREATE TABLE KELAS (
    id_kelas SERIAL PRIMARY KEY,
    nama_kelas VARCHAR(100) NOT NULL,
    tingkat VARCHAR(50) NOT NULL,
    nip_wali_kelas VARCHAR(20), -- Wali kelas (FK ke GURU)
    id_tahun_ajaran INT NOT NULL, -- Tahun ajaran (FK ke TAHUN_AJARAN)
    FOREIGN KEY (nip_wali_kelas) REFERENCES guru (nip) ON DELETE SET NULL,
    FOREIGN KEY (id_tahun_ajaran) REFERENCES tahun_ajaran (id_tahun_ajaran) ON DELETE CASCADE
);

-- Tabel MATA_PELAJARAN
CREATE TABLE MATA_PELAJARAN (
    id_mapel SERIAL PRIMARY KEY,
    nama_mapel VARCHAR(100) UNIQUE NOT NULL,
    jenjang_kelas VARCHAR(50) NOT NULL
);

-- Tabel JADWAL_KELAS
CREATE TABLE JADWAL_KELAS (
    id_jadwal_kelas SERIAL PRIMARY KEY,
    id_kelas INT NOT NULL,
    id_mapel INT NOT NULL,
    nip_guru VARCHAR(20) NOT NULL,
    hari VARCHAR(20) NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    FOREIGN KEY (id_kelas) REFERENCES KELAS (id_kelas) ON DELETE CASCADE,
    FOREIGN KEY (id_mapel) REFERENCES MATA_PELAJARAN (id_mapel) ON DELETE CASCADE,
    FOREIGN KEY (nip_guru) REFERENCES GURU (nip) ON DELETE CASCADE
);

-- Tabel PRESENSI (Siswa)
CREATE TABLE PRESENSI (
    id_presensi SERIAL PRIMARY KEY,
    nis VARCHAR(20) NOT NULL,
    id_jadwal_kelas INT NOT NULL,
    tanggal DATE NOT NULL,
    status VARCHAR(20) NOT NULL, -- Contoh: 'Hadir', 'Absen', 'Izin', 'Sakit'
    FOREIGN KEY (nis) REFERENCES SISWA (nis) ON DELETE CASCADE,
    FOREIGN KEY (id_jadwal_kelas) REFERENCES JADWAL_KELAS (id_jadwal_kelas) ON DELETE CASCADE
);

-- Tabel TUGAS
CREATE TABLE TUGAS (
    id_tugas SERIAL PRIMARY KEY,
    id_mapel INT NOT NULL,
    nip_guru VARCHAR(20) NOT NULL,
    judul VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    tanggal_deadline DATE,
    FOREIGN KEY (id_mapel) REFERENCES MATA_PELAJARAN (id_mapel) ON DELETE CASCADE,
    FOREIGN KEY (nip_guru) REFERENCES GURU (nip) ON DELETE CASCADE
);

-- Tabel NILAI_TUGAS
CREATE TABLE NILAI_TUGAS (
    id_nilai_tugas SERIAL PRIMARY KEY,
    id_tugas INT NOT NULL,
    nis VARCHAR(20) NOT NULL,
    nilai NUMERIC(5,2), -- Nilai bisa desimal
    FOREIGN KEY (id_tugas) REFERENCES TUGAS (id_tugas) ON DELETE CASCADE,
    FOREIGN KEY (nis) REFERENCES SISWA (nis) ON DELETE CASCADE
);

-- Tabel NILAI_UJIAN
CREATE TABLE NILAI_UJIAN (
    id_nilai_ujian SERIAL PRIMARY KEY,
    nis VARCHAR(20) NOT NULL,
    id_mapel INT NOT NULL,
    jenis_ujian VARCHAR(50) NOT NULL, -- Contoh: 'UTS', 'UAS', 'Ulangan Harian'
    nilai NUMERIC(5,2),
    semester VARCHAR(20),
    id_tahun_ajaran INT NOT NULL,
    FOREIGN KEY (nis) REFERENCES SISWA (nis) ON DELETE CASCADE,
    FOREIGN KEY (id_mapel) REFERENCES MATA_PELAJARAN (id_mapel) ON DELETE CASCADE,
    FOREIGN KEY (id_tahun_ajaran) REFERENCES TAHUN_AJARAN (id_tahun_ajaran) ON DELETE CASCADE
);

-- Tabel RAPOR
CREATE TABLE RAPOR (
    id_rapor SERIAL PRIMARY KEY,
    nis VARCHAR(20) NOT NULL,
    id_tahun_ajaran INT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    tanggal_cetak DATE,
    FOREIGN KEY (nis) REFERENCES SISWA (nis) ON DELETE CASCADE,
    FOREIGN KEY (id_tahun_ajaran) REFERENCES TAHUN_AJARAN (id_tahun_ajaran) ON DELETE CASCADE
);

-- Tabel PENGUMUMAN
CREATE TABLE PENGUMUMAN (
    id_pengumuman SERIAL PRIMARY KEY,
    judul VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    tanggal DATE NOT NULL,
    lampiran VARCHAR(255) -- Path atau nama file lampiran
);

-- Tabel AGENDA_SEKOLAH
CREATE TABLE AGENDA_SEKOLAH (
    id_agenda_sekolah SERIAL PRIMARY KEY,
    judul VARCHAR(255) NOT NULL,
    deskripsi TEXT,
    tanggal DATE NOT NULL
);

-- Tabel EKSTRAKURIKULER
CREATE TABLE EKSTRAKURIKULER (
    id_ekstrakurikuler SERIAL PRIMARY KEY,
    nama VARCHAR(100) UNIQUE NOT NULL,
    tingkat VARCHAR(50) -- Contoh: 'SD', 'SMP', 'SMA', 'Umum'
);

-- Tabel PEMBINA (Many-to-Many antara GURU dan EKSTRAKURIKULER)
CREATE TABLE PEMBINA (
    id_pembina SERIAL PRIMARY KEY,
    nip_guru VARCHAR(20) NOT NULL,
    id_ekstrakurikuler INT NOT NULL,
    FOREIGN KEY (nip_guru) REFERENCES GURU (nip) ON DELETE CASCADE,
    FOREIGN KEY (id_ekstrakurikuler) REFERENCES EKSTRAKURIKULER (id_ekstrakurikuler) ON DELETE CASCADE
);

-- Tabel PESERTA_EKSKUL (Many-to-Many antara SISWA dan EKSTRAKURIKULER)
CREATE TABLE PESERTA_EKSKUL (
    id_peserta SERIAL PRIMARY KEY,
    nis VARCHAR(20) NOT NULL,
    id_ekstrakurikuler INT NOT NULL,
    FOREIGN KEY (nis) REFERENCES SISWA (nis) ON DELETE CASCADE,
    FOREIGN KEY (id_ekstrakurikuler) REFERENCES EKSTRAKURIKULER (id_ekstrakurikuler) ON DELETE CASCADE
);

-- Menambahkan data role awal
INSERT INTO ROLE (nama_role) VALUES ('Admin'), ('Guru'), ('Siswa');
