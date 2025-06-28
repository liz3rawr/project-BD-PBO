module com.example.projectdouble { // Sesuaikan dengan nama modul proyek Anda
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Pastikan ini ada jika Anda menggunakan JDBC
    requires java.desktop; // Mungkin diperlukan untuk FileChooser

    // Buka paket controller ke javafx.fxml agar FXML dapat mengakses controller
    opens com.example.projectdouble.controller to javafx.fxml;

    // --- Tambahkan baris ini untuk Pengumuman model ---
    // Membuka paket model ke javafx.base agar PropertyValueFactory dapat mengakses properti model
    opens com.example.projectdouble.model to javafx.base;

    exports com.example.projectdouble;
    exports com.example.projectdouble.controller;
    exports com.example.projectdouble.model; // Pastikan model juga diekspor
    // Tambahkan exports untuk DAO jika diperlukan oleh modul lain
    exports com.example.projectdouble.DAO;
    exports com.example.projectdouble.util; // Jika ada kelas util yang diakses di luar modul
}