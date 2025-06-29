package com.example.projectdouble.controller;

import com.example.projectdouble.DAO.*;
import com.example.projectdouble.model.*;
import com.example.projectdouble.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SiswaController implements Initializable {

    private SiswaDAO siswaDAO;
    private PengumumanDAO pengumumanDAO;
    private TugasDAO tugasDAO;
    private EkstrakurikulerDAO ekstrakurikulerDAO;
    private PesertaEkskulDAO pesertaEkskulDAO;
    private AgendaSekolahDAO agendaSekolahDAO;
    private JadwalKelasDAO jadwalKelasDAO;
    private NilaiUjianDAO nilaiUjianDAO;
    private MataPelajaranDAO mataPelajaranDAO;
    private PresensiDAO presensiDAO;
    private TahunAjaranDAO tahunAjaranDAO;
    private KelasDAO kelasDAO;
    private RaporDAO raporDAO;

    @FXML
    private Label welcome;
    @FXML private Button logout;
    @FXML private TextField classHeaderField;
    @FXML private TextField homeroomTeacherHeaderField;
    @FXML private ComboBox<TahunAjaran> schoolYearHeaderCombo;
    @FXML private TextField biodataNisField, biodataNameField, biodataPobField, biodataDobField;
    @FXML private ComboBox<String> biodataGenderCombo;
    @FXML private TextArea biodataAddressArea;
    @FXML private TextField editNisField, editNameField, editPobField, editDobField;
    @FXML private ComboBox<String> editGenderCombo;
    @FXML private TextArea editAddressArea;
    @FXML private Button updateBiodataButton;
    @FXML private TableView<Pengumuman> announcementTable;
    @FXML private TableColumn<Pengumuman, String> announcementTitleCol, announcementDateCol, announcementContentCol, announcementAttachmentCol;
    @FXML private ComboBox<MataPelajaran> assignmentSubjectCombo;
    @FXML private TableView<Tugas> assignmentTable;
    @FXML private TableColumn<Tugas, String> assignmentTitleCol, assignmentDescCol, assignmentDeadlineCol;

    // Extracurricular tables
    @FXML private TableView<Ekstrakurikuler> extracurricularTable1;
    @FXML private TableColumn<Ekstrakurikuler, String> extracurricularNameCol1, extracurricularLevelCol1, extracurricularMentorCol1;
    @FXML private TableView<Ekstrakurikuler> extracurricularTable;
    @FXML private TableColumn<Ekstrakurikuler, String> extracurricularNameCol, extracurricularLevelCol, extracurricularMentorCol;


    @FXML private ComboBox<TahunAjaran> agendaSchoolYearCombo;
    @FXML private TableView<AgendaSekolah> agendaTable;
    @FXML private TableColumn<AgendaSekolah, String> agendaContentCol, agendaStartCol, agendaEndCol;
    @FXML private TableView<JadwalKelas> scheduleTable;
    @FXML private TableColumn<JadwalKelas, String> scheduleDayCol, scheduleStartCol, scheduleEndCol, scheduleSubjectCol;
    @FXML private ComboBox<MataPelajaran> gradeSubjectCombo;
    @FXML private TableView<NilaiUjian> gradeTable;
    @FXML private TableColumn<NilaiUjian, String> gradeExamCol;
    @FXML private TableColumn<NilaiUjian, BigDecimal> gradeGradeCol;
    @FXML private TableColumn<NilaiUjian, String> gradeSemesterCol;
    @FXML private Tab reportTab;
    @FXML private TextField reportClassField;
    @FXML private TableView<NilaiUjian> reportTable;
    @FXML private TableColumn<NilaiUjian, String> reportSubjectCol, reportExamCol;
    @FXML private TableColumn<NilaiUjian, BigDecimal> reportGradeCol;
    @FXML private TextField attendanceClassField;
    @FXML private TableView<Presensi> attendanceTable;
    @FXML private TableColumn<Presensi, String> attendanceDateCol, attendanceStatusCol;

    private Siswa currentSiswa;
    private final ObservableList<Tugas> masterTugasList = FXCollections.observableArrayList();
    private final ObservableList<NilaiUjian> masterReportList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        siswaDAO = new SiswaDAO();
        pengumumanDAO = new PengumumanDAO();
        tugasDAO = new TugasDAO();
        ekstrakurikulerDAO = new EkstrakurikulerDAO();
        pesertaEkskulDAO = new PesertaEkskulDAO();
        agendaSekolahDAO = new AgendaSekolahDAO();
        jadwalKelasDAO = new JadwalKelasDAO();
        nilaiUjianDAO = new NilaiUjianDAO();
        mataPelajaranDAO = new MataPelajaranDAO();
        presensiDAO = new PresensiDAO();
        tahunAjaranDAO = new TahunAjaranDAO();
        kelasDAO = new KelasDAO();
        raporDAO = new RaporDAO();

        setupAllUIComponents();
        loadInitialData();

        assignmentSubjectCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterAssignments());
        schoolYearHeaderCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateHeaderAndRaporStatusBasedOnSelectedYear());
        gradeSubjectCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterGrades());
        agendaSchoolYearCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterSchoolAgenda());

        User loggedInUser = SessionManager.getLoggedInUser();
        if (loggedInUser != null) {
            loadDataForSiswa(loggedInUser.getUsername());
        } else {
            welcome.setText("Welcome, Guest!");
            showAlert(Alert.AlertType.WARNING, "Sesi Tidak Ditemukan", "Tidak dapat menemukan data user yang login.");
        }
    }

    // Helper method for center alignment of columns
    private <S, T> void centerAlignColumn(TableColumn<S, T> column) {
        column.setCellFactory(col -> {
            return new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            };
        });
    }

    // Helper method for text wrapping only (left aligned)
    private <S, T> void wrapTextColumn(TableColumn<S, T> column) {
        column.setCellFactory(col -> {
            return new TableCell<S, T>() {
                private final javafx.scene.text.Text text;

                {
                    text = new Text();
                    text.wrappingWidthProperty().bind(column.widthProperty().subtract(5));
                    setGraphic(text);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setAlignment(Pos.CENTER_LEFT);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                }

                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        text.setText(null);
                        setGraphic(null);
                    } else {
                        text.setText(item.toString());
                        setGraphic(text);
                    }
                }
            };
        });
    }

    private void setupAllUIComponents() {
        announcementTitleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        announcementDateCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggal().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        announcementContentCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        announcementAttachmentCol.setCellValueFactory(new PropertyValueFactory<>("lampiran"));
        assignmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        assignmentDescCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        assignmentDeadlineCol.setCellValueFactory(new PropertyValueFactory<>("tanggalDeadline"));

        // Setup for extracurricularTable1 (My Extracurricular)
        extracurricularNameCol1.setCellValueFactory(new PropertyValueFactory<>("nama"));
        extracurricularLevelCol1.setCellValueFactory(new PropertyValueFactory<>("tingkat"));
        extracurricularMentorCol1.setCellValueFactory(new PropertyValueFactory<>("mentorNames"));

        // Setup for extracurricularTable (All Extracurriculars)
        extracurricularNameCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        extracurricularLevelCol.setCellValueFactory(new PropertyValueFactory<>("tingkat"));
        extracurricularMentorCol.setCellValueFactory(new PropertyValueFactory<>("mentorNames"));

        agendaContentCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        agendaStartCol.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));
        agendaEndCol.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));
        scheduleDayCol.setCellValueFactory(new PropertyValueFactory<>("hari"));
        scheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        scheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        scheduleSubjectCol.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        gradeExamCol.setCellValueFactory(new PropertyValueFactory<>("jenisUjian"));
        gradeGradeCol.setCellValueFactory(new PropertyValueFactory<>("nilai"));
        gradeSemesterCol.setCellValueFactory(cellData -> {
            String jenisUjian = cellData.getValue().getJenisUjian();
            String semester;
            switch (jenisUjian) {
                case "UTS 1":
                case "UAS 1":
                case "PH 1":
                case "PH 2":
                    semester = "Ganjil";
                    break;
                case "UTS 2":
                case "UAS 2":
                case "PH 3":
                case "PH 4":
                    semester = "Genap";
                    break;
                default:
                    semester = "-";
                    break;
            }
            return new javafx.beans.property.SimpleStringProperty(semester);
        });
        reportSubjectCol.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        reportExamCol.setCellValueFactory(new PropertyValueFactory<>("jenisUjian"));
        reportGradeCol.setCellValueFactory(new PropertyValueFactory<>("nilai"));
        attendanceDateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        attendanceStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        wrapTextColumn(announcementTitleCol);
        wrapTextColumn(announcementContentCol);
        wrapTextColumn(announcementAttachmentCol);
        centerAlignColumn(announcementDateCol);

        wrapTextColumn(assignmentTitleCol);
        wrapTextColumn(assignmentDescCol);
        centerAlignColumn(assignmentDeadlineCol);

        centerAlignColumn(extracurricularLevelCol);
        centerAlignColumn(extracurricularLevelCol1);
        centerAlignColumn(extracurricularNameCol);
        centerAlignColumn(extracurricularNameCol1);
        wrapTextColumn(extracurricularMentorCol);
        wrapTextColumn(extracurricularMentorCol1);

        centerAlignColumn(agendaContentCol);
        centerAlignColumn(agendaStartCol);
        centerAlignColumn(agendaEndCol);

        centerAlignColumn(scheduleDayCol);
        centerAlignColumn(scheduleEndCol);
        centerAlignColumn(scheduleStartCol);
        centerAlignColumn(scheduleSubjectCol);

        centerAlignColumn(gradeExamCol);
        centerAlignColumn(gradeGradeCol);
        centerAlignColumn(gradeSemesterCol);

        centerAlignColumn(reportExamCol);
        centerAlignColumn(reportGradeCol);
        centerAlignColumn(reportSubjectCol);

        centerAlignColumn(attendanceDateCol);
        centerAlignColumn(attendanceStatusCol);
    }

    private void loadInitialData() {
        announcementTable.setItems(FXCollections.observableArrayList(pengumumanDAO.getAllPengumuman()));
        agendaTable.setItems(FXCollections.observableArrayList(agendaSekolahDAO.getAllAgendaSekolah()));
        ObservableList<MataPelajaran> allSubjects = FXCollections.observableArrayList(mataPelajaranDAO.getAllMataPelajaran());
        assignmentSubjectCombo.setItems(allSubjects);
        gradeSubjectCombo.setItems(allSubjects);
        ObservableList<TahunAjaran> allSchoolYears = FXCollections.observableArrayList(tahunAjaranDAO.getAllTahunAjaran());
        schoolYearHeaderCombo.setItems(allSchoolYears);
        agendaSchoolYearCombo.setItems(allSchoolYears);

        schoolYearHeaderCombo.setConverter(new StringConverter<TahunAjaran>() {
            @Override
            public String toString(TahunAjaran object) {
                return object != null ? object.getTahunAjaranLengkap() : "";
            }

            @Override
            public TahunAjaran fromString(String string) {
                return null;
            }
        });
    }

    private void loadDataForSiswa(String nis) {
        this.currentSiswa = siswaDAO.getSiswaByNis(nis);
        if (this.currentSiswa != null) {
            welcome.setText("Welcome, " + this.currentSiswa.getNama() + "!");
            populateMyBiodata();
            loadAllTablesForSiswa();
        } else {
            showAlert(Alert.AlertType.ERROR, "Data Error", "Data siswa tidak ditemukan.");
        }
    }

    private void updateHeaderAndRaporStatusBasedOnSelectedYear() {
        if (currentSiswa == null) return;
        TahunAjaran selectedYear = schoolYearHeaderCombo.getValue();

        // Update header fields
        if (selectedYear != null && Objects.equals(selectedYear.getIdTahunAjaran(), currentSiswa.getIdTahunAjaran())) {
            String currentClassName = currentSiswa.getNamaKelas();
            classHeaderField.setText(currentClassName != null ? currentClassName : "Belum ada kelas");

            if (currentSiswa.getIdKelas() != null) {
                Kelas currentKelas = kelasDAO.getKelasById(currentSiswa.getIdKelas());
                if (currentKelas != null && currentKelas.getNamaWaliKelas() != null) {
                    homeroomTeacherHeaderField.setText(currentKelas.getNamaWaliKelas());
                } else {
                    homeroomTeacherHeaderField.setText("N/A");
                }
            } else {
                homeroomTeacherHeaderField.setText("N/A");
            }
        } else {
            classHeaderField.clear();
            homeroomTeacherHeaderField.clear();
        }

        // Handle Report Tab visibility and content
        if (selectedYear != null && raporDAO.isRaporPrinted(currentSiswa.getNis(), selectedYear.getIdTahunAjaran())) {
            reportTab.setDisable(false);
            reportTable.setItems(FXCollections.observableArrayList(
                    nilaiUjianDAO.getNilaiUjianByNisAndTahunAjaran(currentSiswa.getNis(), selectedYear.getIdTahunAjaran())
            ));
            reportTable.setPlaceholder(new Label(""));
        } else {
            reportTab.setDisable(true);
            reportTable.getItems().clear();
            reportTable.setPlaceholder(new Label("Rapor untuk tahun ajaran ini belum tersedia atau belum dicetak oleh wali kelas."));
        }


        if (selectedYear != null) {
            // Update Grades Table
            masterReportList.setAll(nilaiUjianDAO.getNilaiUjianByNisAndTahunAjaran(currentSiswa.getNis(), selectedYear.getIdTahunAjaran()));
            gradeTable.setItems(masterReportList);
            filterGrades();

            // Update Assignments Table
            masterTugasList.setAll(tugasDAO.getTugasByKelasAndTahunAjaran(
                    currentSiswa.getIdKelas() != null ? currentSiswa.getIdKelas() : 0,
                    selectedYear.getIdTahunAjaran()
            ));
            assignmentTable.setItems(masterTugasList);
            filterAssignments();

            // Update Attendance Table
            attendanceTable.setItems(FXCollections.observableArrayList(
                    presensiDAO.getPresensiByNisAndTahunAjaran(currentSiswa.getNis(), selectedYear.getIdTahunAjaran())
            ));
        } else {
            gradeTable.getItems().clear();
            assignmentTable.getItems().clear();
            attendanceTable.getItems().clear();
        }
    }

    private void loadAllTablesForSiswa() {
        if(currentSiswa == null) return;

        if (currentSiswa.getIdTahunAjaran() != null) {
            TahunAjaran initialYear = tahunAjaranDAO.getTahunAjaranById(currentSiswa.getIdTahunAjaran());
            if (initialYear != null) {
                schoolYearHeaderCombo.getSelectionModel().select(initialYear);
            }
        }

        updateHeaderAndRaporStatusBasedOnSelectedYear();

        if(currentSiswa.getIdKelas() != null) {
            scheduleTable.setItems(FXCollections.observableArrayList(
                    jadwalKelasDAO.getAllJadwalKelasDetails().stream()
                            .filter(j -> Objects.equals(j.getIdKelas(), currentSiswa.getIdKelas()))
                            .collect(Collectors.toList()))
            );
        }

        masterReportList.setAll(nilaiUjianDAO.getNilaiUjianByNisAndTahunAjaran(currentSiswa.getNis(), currentSiswa.getIdTahunAjaran()));
        gradeTable.setItems(masterReportList);


        attendanceClassField.setText(currentSiswa.getNamaKelas() != null ? currentSiswa.getNamaKelas() : "N/A");
        reportClassField.setText(currentSiswa.getNamaKelas() != null ? currentSiswa.getNamaKelas() : "N/A");

        List<Ekstrakurikuler> allEkskulWithMentors = ekstrakurikulerDAO.getAllEkstrakurikulerWithMentors();
        List<PesertaEkskul> myParticipation = pesertaEkskulDAO.getAllPesertaEkskul().stream().filter(p -> p.getNisSiswa().equals(currentSiswa.getNis())).collect(Collectors.toList());

        List<Ekstrakurikuler> myEkskul = new ArrayList<>();
        for (PesertaEkskul p : myParticipation) {
            allEkskulWithMentors.stream()
                    .filter(e -> e.getIdEkstrakurikuler() == p.getIdEkstrakurikuler())
                    .findFirst().ifPresent(myEkskul::add);
        }
        extracurricularTable1.setItems(FXCollections.observableArrayList(myEkskul));

        extracurricularTable.setItems(FXCollections.observableArrayList(allEkskulWithMentors));



    }

    private void filterAssignments() {
        MataPelajaran selectedSubject = assignmentSubjectCombo.getValue();
        TahunAjaran selectedSchoolYear = schoolYearHeaderCombo.getValue();

        if (currentSiswa == null || selectedSchoolYear == null) {
            assignmentTable.getItems().clear();
            return;
        }

        List<Tugas> tasksForClassAndYear = tugasDAO.getTugasByKelasAndTahunAjaran(
                currentSiswa.getIdKelas() != null ? currentSiswa.getIdKelas() : 0,
                selectedSchoolYear.getIdTahunAjaran()
        );

        if (selectedSubject == null) {
            assignmentTable.setItems(FXCollections.observableArrayList(tasksForClassAndYear));
        } else {
            FilteredList<Tugas> filteredData = new FilteredList<>(FXCollections.observableArrayList(tasksForClassAndYear), t -> {
                return t.getIdMapel() != null && t.getIdMapel().equals(selectedSubject.getIdMapel());
            });
            assignmentTable.setItems(filteredData);
        }
    }

    private void filterGrades() {
        MataPelajaran selectedSubject = gradeSubjectCombo.getValue();
        TahunAjaran selectedSchoolYear = schoolYearHeaderCombo.getValue();

        if (currentSiswa == null || selectedSchoolYear == null) {
            gradeTable.getItems().clear();
            return;
        }

        List<NilaiUjian> scoresForStudentAndYear = nilaiUjianDAO.getNilaiUjianByNisAndTahunAjaran(
                currentSiswa.getNis(), selectedSchoolYear.getIdTahunAjaran()
        );

        if (selectedSubject == null) {
            gradeTable.setItems(FXCollections.observableArrayList(scoresForStudentAndYear));
        } else {
            FilteredList<NilaiUjian> filteredData = new FilteredList<>(FXCollections.observableArrayList(scoresForStudentAndYear), nilai -> {
                return Objects.equals(nilai.getIdMapel(), selectedSubject.getIdMapel());
            });
            gradeTable.setItems(filteredData);
        }
    }

    private void filterSchoolAgenda() {
        TahunAjaran ta = agendaSchoolYearCombo.getValue();

        if(ta != null) {
            agendaTable.setItems(FXCollections.observableArrayList(agendaSekolahDAO.getAgendaByTahunAjaran(ta.getIdTahunAjaran())));
        } else {
            agendaTable.setItems(FXCollections.observableArrayList(agendaSekolahDAO.getAllAgendaSekolah()));
        }
    }

    private void populateMyBiodata() {
        biodataNisField.setText(currentSiswa.getNis());
        biodataNameField.setText(currentSiswa.getNama());
        biodataGenderCombo.setValue(currentSiswa.getJenisKelamin());
        biodataPobField.setText(currentSiswa.getTempatLahir());
        biodataDobField.setText(currentSiswa.getTanggalLahir().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        biodataAddressArea.setText(currentSiswa.getAlamat());
        editNisField.setText(currentSiswa.getNis());
        editNameField.setText(currentSiswa.getNama());
        editGenderCombo.setValue(currentSiswa.getJenisKelamin());
        editPobField.setText(currentSiswa.getTempatLahir());
        editDobField.setText(currentSiswa.getTanggalLahir().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        editAddressArea.setText(currentSiswa.getAlamat());
    }

    @FXML
    void actionLogout(ActionEvent event) {
        try {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectdouble/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateBiodataButtonAction(ActionEvent event) {
        String nama = editNameField.getText();
        String tempatLahir = editPobField.getText();
        String tanggalLahirStr = editDobField.getText();
        String alamat = editAddressArea.getText();
        String jenisKelamin = editGenderCombo.getValue();
        if (nama.isEmpty() || tempatLahir.isEmpty() || tanggalLahirStr.isEmpty() || alamat.isEmpty() || jenisKelamin == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Semua field pada Edit Biodata harus diisi.");
            return;
        }
        LocalDate tanggalLahir;
        try {
            tanggalLahir = LocalDate.parse(tanggalLahirStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Format Salah", "Format Tanggal Lahir harus dd/MM/yyyy.");
            return;
        }
        currentSiswa.setNama(nama);
        currentSiswa.setJenisKelamin(jenisKelamin);
        currentSiswa.setTempatLahir(tempatLahir);
        currentSiswa.setTanggalLahir(tanggalLahir);
        currentSiswa.setAlamat(alamat);
        if (siswaDAO.updateSiswa(currentSiswa)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data biodata berhasil diperbarui.");
            loadDataForSiswa(currentSiswa.getNis());
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui data biodata di database.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
