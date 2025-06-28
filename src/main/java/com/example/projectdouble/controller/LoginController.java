package com.example.projectdouble.controller;

import com.example.projectdouble.DAO.UserDAO;
import com.example.projectdouble.model.Role;
import com.example.projectdouble.model.User;
import com.example.projectdouble.util.DBConnect;
import com.example.projectdouble.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField username; // Mengubah dari usernameField ke username
    @FXML
    private PasswordField password; // Mengubah dari passwordField ke password
    @FXML
    private ComboBox<Role> role; // Mengubah dari roleComboBox ke role

    private UserDAO userDao; // Objek DAO untuk operasi user

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDao = new UserDAO(); // Inisialisasi UserDao
        loadRoles(); // Memuat role ke ComboBox saat inisialisasi

    }

    /**
     * Memuat daftar role dari database ke ComboBox.
     */
    private void loadRoles() {
        ObservableList<Role> roles = FXCollections.observableArrayList(userDao.getAllRoles());
        role.setItems(roles); // Menggunakan fx:id baru "role"
    }

    /**
     * Menangani aksi klik tombol Login.
     * @param event Objek ActionEvent.
     */
    @FXML
    private void actionLogin(ActionEvent event) { // Mengubah dari handleLogin ke actionLogin
        String inputUsername = username.getText(); // Mengambil username dari input
        String inputPassword = password.getText(); // Mengambil password dari input
        Role selectedRole = role.getSelectionModel().getSelectedItem(); // Mengambil role yang dipilih

        // Validasi input
        if (inputUsername.isEmpty() || inputPassword.isEmpty() || selectedRole == null) {
            // errorMessageLabel.setText("Username, password, dan role harus diisi.");
            DBConnect.showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username, password, dan role harus diisi.");
            return;
        }

        // Autentikasi pengguna
        User loggedInUser = userDao.authenticateUser(inputUsername, inputPassword, selectedRole.getNamaRole());


        if (loggedInUser != null) {
            // Set nama asli untuk welcome message (jika ada)
            String welcomeName = loggedInUser.getUsername();
            if ("guru".equals(loggedInUser.getRole().getNamaRole())) {
                String guruName = userDao.getGuruNameByNip(loggedInUser.getUsername());
                if (guruName != null) {
                    welcomeName = guruName;
                }
            } else if ("siswa".equals(loggedInUser.getRole().getNamaRole())) {
                String siswaName = userDao.getSiswaNameByNis(loggedInUser.getUsername());
                if (siswaName != null) {
                    welcomeName = siswaName;
                }
            }
            loggedInUser.setDisplayName(welcomeName); // Set username di objek User menjadi nama asli

            SessionManager.setLoggedInUser(loggedInUser); // Simpan user yang login di SessionManager

            try {
                // Mendapatkan Stage saat ini dari event
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Menentukan FXML dashboard berdasarkan role
                String fxmlPath;
                String dashboardTitle;

                switch (loggedInUser.getRole().getNamaRole()) {
                    case "admin":
                        fxmlPath = "/com/example/projectdouble/admin-dashboard.fxml";
                        dashboardTitle = "Dashboard Admin";
                        break;
                    case "guru":
                        fxmlPath = "/com/example/projectdouble/guru-dashboard.fxml";
                        dashboardTitle = "Dashboard Guru";
                        break;
                    case "siswa":
                        fxmlPath = "/com/example/projectdouble/siswa-dashboard.fxml";
                        dashboardTitle = "Dashboard Siswa";
                        break;
                    default:
                        DBConnect.showAlert(Alert.AlertType.ERROR, "Login Gagal", "Role tidak dikenal.");
                        return;
                }

                // Memuat FXML dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();



                // Membuat Scene baru dan menetapkannya ke Stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(dashboardTitle); // Mengatur judul jendela
                stage.show(); // Menampilkan Stage

            } catch (IOException e) {
                System.err.println("Gagal memuat dashboard: " + e.getMessage());
                e.printStackTrace();
                DBConnect.showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat memuat tampilan.");
            }
        } else {
            // errorMessageLabel.setText("Username, password, atau role salah."); // Pesan error jika login gagal
            DBConnect.showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username, password, atau role salah.");
        }
    }
}
