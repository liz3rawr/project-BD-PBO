CREATE TABLE role (
    id_role SERIAL PRIMARY KEY,
    nama_role VARCHAR(20) NOT NULL
);

INSERT INTO role (id_role, nama_role) VALUES
(1,'admin'), (2,'guru'), (3, 'siswa');

CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role INT REFERENCES role(id_role)
);
select * from users;

INSERT INTO users (username, password, role) VALUES 
('admin1', 'pes1', 1),
('admin2', 'pes2', 1),
('admin3', 'pes3', 1);


CREATE TABLE siswa (
    nis VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100),
    jenis_kelamin VARCHAR(10),
    tempat_lahir VARCHAR(50),
    tanggal_lahir DATE,
    alamat TEXT
);

CREATE TABLE guru (
    nip VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100),
    jenis_kelamin VARCHAR(10),
    email VARCHAR(100),
    no_hp VARCHAR(15)
);

INSERT INTO guru (nip, nama, jenis_kelamin, email, no_hp) VALUES
('G001', 'Budi Santoso', 'Laki-laki', 'budi@guru.com', '081234567890'),
('G002', 'Evi Ayu', 'Perempuan', 'evi@guru.com', '082240009921'),
('G003', 'Bayu Putra', 'Laki-laki', 'bayu@guru.com', '081310002020'),
('G004', 'Dina Lestari', 'Perempuan', 'dina@guru.com', '081310002021'),
('G005', 'Eko Prasetyo', 'Laki-laki', 'eko@guru.com', '081310002022'),
('G006', 'Fitriani Rahma', 'Perempuan', 'fitri@guru.com', '081310002023'),
('G007', 'Gilang Permana', 'Laki-laki', 'gilang@guru.com', '081310002024'),
('G008', 'Hesti Ayu', 'Perempuan', 'hesti@guru.com', '081310002025'),
('G009', 'Irwan Maulana', 'Laki-laki', 'irwan@guru.com', '081310002026'),
('G010', 'Joko Supriyadi', 'Laki-laki', 'joko@guru.com', '081310002027'),
('G011', 'Kartika Melati', 'Perempuan', 'kartika@guru.com', '081310002028'),
('G012', 'Lukman Hakim', 'Laki-laki', 'lukman@guru.com', '081310002029'),
('G013', 'Maria Yuliana', 'Perempuan', 'maria@guru.com', '081310002030'),
('G014', 'Nugroho Pranoto', 'Laki-laki', 'nugroho@guru.com', '081310002031'),
('G015', 'Olivia Wulandari', 'Perempuan', 'olivia@guru.com', '081310002032'),
('G016', 'Putra Ramadhan', 'Laki-laki', 'putra@guru.com', '081310002033'),
('G017', 'Qori Azzahra', 'Perempuan', 'qori@guru.com', '081310002034'),
('G018', 'Rian Saputra', 'Laki-laki', 'rian@guru.com', '081310002035'),
('G019', 'Sari Utami', 'Perempuan', 'sari@guru.com', '081310002036'),
('G020', 'Teguh Wibowo', 'Laki-laki', 'teguh@guru.com', '081310002037');

INSERT INTO users (username, password, role) VALUES
('G001', 'guru1', 2),
('G002', 'guru2', 2),
('G003', 'guru3', 2),
('G004', 'guru4', 2),
('G005', 'guru5', 2),
('G006', 'guru6', 2),
('G007', 'guru7', 2),
('G008', 'guru8', 2),
('G009', 'guru9', 2),
('G010', 'guru10', 2),
('G011', 'guru11', 2),
('G012', 'guru12', 2),
('G013', 'guru13', 2),
('G014', 'guru14', 2),
('G015', 'guru15', 2),
('G016', 'guru16', 2),
('G017', 'guru17', 2),
('G018', 'guru18', 2),
('G019', 'guru19', 2),
('G020', 'guru20', 2);

select * from users;
select * from guru;

CREATE TABLE kelas (
    id_kelas SERIAL PRIMARY KEY,
    nama_kelas VARCHAR(50),
    tingkat VARCHAR(20)
);

CREATE TABLE tahun_ajaran (
    id_tahun_ajaran SERIAL PRIMARY KEY,
    tahun_mulai INT,
    tahun_selesai INT,
    tahun_ganjil_genap VARCHAR(10)
);

CREATE TABLE mata_pelajaran (
    id_mapel SERIAL PRIMARY KEY,
    nama_mapel VARCHAR(100),
    jenjang_kelas VARCHAR(20)
);

CREATE TABLE jadwal_kelas (
    id_jadwal_kelas SERIAL PRIMARY KEY,
    hari VARCHAR(10),
    jam_mulai TIME,
    jam_selesai TIME,
    id_kelas INT REFERENCES kelas(id_kelas),
    id_mapel INT REFERENCES mata_pelajaran(id_mapel),
    nip_guru VARCHAR(20) REFERENCES guru(nip)
);

CREATE TABLE rapor (
    id_rapor SERIAL PRIMARY KEY,
    nis VARCHAR(20) REFERENCES siswa(nis),
    semester VARCHAR(10),
    tanggal_cetak DATE
);

CREATE TABLE nilai_ujian (
    id_nilai_ujian SERIAL PRIMARY KEY,
    jenis_ujian VARCHAR(20),
    nilai NUMERIC(5,2),
    semester VARCHAR(10),
    id_mapel INT REFERENCES mata_pelajaran(id_mapel),
    nis VARCHAR(20) REFERENCES siswa(nis)
);

CREATE TABLE tugas (
    id_tugas SERIAL PRIMARY KEY,
    judul VARCHAR(100),
    deskripsi TEXT,
    tanggal_deadline DATE
);

CREATE TABLE nilai_tugas (
    id_nilai_tugas SERIAL PRIMARY KEY,
    nilai NUMERIC(5,2),
    id_tugas INT REFERENCES tugas(id_tugas),
    nis VARCHAR(20) REFERENCES siswa(nis)
);

CREATE TABLE presensi (
    id_presensi SERIAL PRIMARY KEY,
    tanggal DATE,
    status VARCHAR(10),
    nis VARCHAR(20) REFERENCES siswa(nis),
    id_jadwal_kelas INT REFERENCES jadwal_kelas(id_jadwal_kelas)
);

CREATE TABLE ekstrakurikuler (
    id_ekstrakurikuler SERIAL PRIMARY KEY,
    nama VARCHAR(100),
    tingkat VARCHAR(20)
);

CREATE TABLE pembina (
    id_pembina SERIAL PRIMARY KEY,
    nip VARCHAR(20) REFERENCES guru(nip),
    id_ekstrakurikuler INT REFERENCES ekstrakurikuler(id_ekstrakurikuler)
);

CREATE TABLE peserta_ekskul (
    id_peserta SERIAL PRIMARY KEY,
    nis VARCHAR(20) REFERENCES siswa(nis),
    id_ekstrakurikuler INT REFERENCES ekstrakurikuler(id_ekstrakurikuler)
);

CREATE TABLE pengumuman (
    id_pengumuman SERIAL PRIMARY KEY,
    judul VARCHAR(100),
    deskripsi TEXT,
    tanggal DATE,
    lampiran TEXT
);

CREATE TABLE agenda_sekolah (
    id_agenda_sekolah SERIAL PRIMARY KEY,
    judul VARCHAR(100),
    deskripsi TEXT,
    tanggal DATE
);
