package com.example.projectdouble.controller;

import com.example.projectdouble.DAO.UserDAO;
import com.example.projectdouble.model.User;
import com.example.projectdouble.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private Button logout;
    @FXML
    private Label welcome;

    private UserDAO userDao;

    public void setUsername(String user){
        welcome.setText("Welcome, " + user + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDao = new UserDAO();
//        siswaDao = new SiswaDao(); // Initialize
//        tahunAjaranDao = new TahunAjaranDao(); // Initialize
//        kelasDao = new KelasDao(); // Initialize
//        guruDao = new GuruDao(); // Initialize
//        mataPelajaranDao = new MataPelajaranDao(); // Initialize
//        jadwalKelasDao = new JadwalKelasDao(); // Initialize
//        pengumumanDao = new PengumumanDao(); // Initialize
//        agendaSekolahDao = new AgendaSekolahDao(); // Initialize
//        ekstrakurikulerDao = new EkstrakurikulerDao(); // Initialize
//        pembinaDao = new PembinaDao(); // Initialize
//        pesertaEkskulDao = new PesertaEkskulDao(); // Initialize

        User loggedInUser = SessionManager.getLoggedInUser();

        if (loggedInUser != null) {
            welcome.setText("Welcome, " + loggedInUser.getUsername() + "!");
        } else {
            welcome.setText("Welcome, Guest!");
            // Optionally, redirect to login if no user is logged in
            // try {
            //     handleLogout(null);
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
        }
    }


    @FXML
    public void actionLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectdouble/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
