package com.example.projectdouble.controller;

import com.example.projectdouble.DAO.*;
import com.example.projectdouble.model.*;
import com.example.projectdouble.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GuruController implements Initializable {

    private final GuruDAO guruDAO = new GuruDAO();
    private final UserDAO userDAO = new UserDAO();
    private final PengumumanDAO pengumumanDAO = new PengumumanDAO();
    private final JadwalKelasDAO jadwalKelasDAO = new JadwalKelasDAO();
    private final AgendaSekolahDAO agendaSekolahDAO = new AgendaSekolahDAO();
    private final KelasDAO kelasDAO = new KelasDAO();
    private final MataPelajaranDAO mataPelajaranDAO = new MataPelajaranDAO();
    private final SiswaDAO siswaDAO = new SiswaDAO();
    private final NilaiUjianDAO nilaiUjianDAO = new NilaiUjianDAO();
    private final TugasDAO tugasDAO = new TugasDAO();
    private final TahunAjaranDAO tahunAjaranDAO = new TahunAjaranDAO();
    private final PembinaDAO pembinaDAO = new PembinaDAO();
    private final PesertaEkskulDAO pesertaEkskulDAO = new PesertaEkskulDAO();
    private final PresensiDAO presensiDAO = new PresensiDAO();
    private final EkstrakurikulerDAO ekstrakurikulerDAO = new EkstrakurikulerDAO();
    private final RaporDAO raporDao = new RaporDAO();

    @FXML
    private Label welcome;
    @FXML private Button logout;
    @FXML private TabPane tabPane;

    // Announcements Tab
    @FXML private TableView<Pengumuman> announcementsTable;
    @FXML private TableColumn<Pengumuman, String> announcementsTitleCol;
    @FXML private TableColumn<Pengumuman, String> announcementsDateCol;
    @FXML private TableColumn<Pengumuman, String> announcementsContentCol;
    @FXML private TableColumn<Pengumuman, String> announcementsAttachmentCol;

    // Biodata Tab
    @FXML private TextField biodataNipField, biodataNameField, biodataEmailField, biodataPhoneField;
    @FXML private TextField editBiodataNipField, editBiodataNameField, editBiodataEmailField, editBiodataPhoneField;
    @FXML private ComboBox<String> biodataGenderCombo, editBiodataGenderCombo;
    @FXML private TextField editPasswordUsernameField;
    @FXML private TextField editPasswordOldPassField, editPasswordNewPassField;
    @FXML private Button updateBiodataButton, changePasswordButton;

    // Class Schedule Tab
    @FXML private TableView<JadwalKelas> scheduleTable;
    @FXML private TableColumn<JadwalKelas, String> scheduleDayCol, scheduleSubjectCol, scheduleClassCol;
    @FXML private TableColumn<JadwalKelas, LocalTime> scheduleStartCol, scheduleEndCol;
    @FXML private Button refreshScheduleButton;

    // Input Exam Score Tab
    @FXML private ComboBox<Kelas> inputScoreClassCombo;
    @FXML private ComboBox<MataPelajaran> inputScoreSubjectCombo;
    @FXML private ComboBox<String> inputScoreExamTypeCombo;
    @FXML private ComboBox<Siswa> inputScoreStudentCombo;
    @FXML private ComboBox<TahunAjaran> inputScoreSchoolYearCombo;
    @FXML private TextField inputScoreField, editScoreField;
    @FXML private Button inputScoreSubmitButton, updateScoreButton;
    @FXML private TableView<NilaiUjian> existingScoreTable;
    @FXML private TableColumn<NilaiUjian, String> existingScoreStudentCol, existingScoreExamTypeCol;
    @FXML private TableColumn<NilaiUjian, BigDecimal> existingScoreCol;

    // Manage Assignments Tab
    @FXML private ComboBox<Kelas> assignmentClassCombo;
    @FXML private ComboBox<MataPelajaran> assignmentSubjectCombo;
    @FXML private ComboBox<TahunAjaran> assignmentSchoolYearCombo;
    @FXML private TextField assignmentTitleField;
    @FXML private TextArea assignmentDescriptionArea;
    @FXML private DatePicker assignmentDeadlinePicker;
    @FXML private Button addAssignmentButton;
    @FXML private TableView<Tugas> assignmentTable;
    @FXML private TableColumn<Tugas, String> assignmentTitleCol, assignmentDescCol;
    @FXML private TableColumn<Tugas, LocalDate> assignmentDeadlineCol;
    @FXML private TableColumn<Tugas, String> assignmentSubjectCol;
    @FXML private TableColumn<Tugas, String> assignmentClassCol;

    // Homeroom Teacher Tab
    @FXML private Tab homeroomTab;
    @FXML private ComboBox<TahunAjaran> homeroomSchoolYearCombo;
    @FXML private TextField homeroomClassField;
    @FXML private TableView<Siswa> homeroomStudentsTable;
    @FXML private TableColumn<Siswa, String> homeroomNisCol, homeroomStudentNameCol, homeroomGenderCol;
    @FXML private ComboBox<Siswa> homeroomAttendanceStudentCombo, homeroomRaporStudentCombo;
    @FXML private DatePicker homeroomAttendanceDatePicker;
    @FXML private ComboBox<String> homeroomAttendanceStatusCombo;
    @FXML private Button recordAttendanceButton;
    @FXML private TableView<Presensi> homeroomAttendanceTable;
    @FXML private TableColumn<Presensi, String> homeroomAttendanceDateCol, homeroomAttendanceStudentCol, homeroomAttendanceStatusCol, homeroomAttendanceClassCol;
    @FXML private Button homeroomPrintRaporButton;

    // School Agenda Tab
    @FXML private ComboBox<TahunAjaran> agendaSchoolYearCombo;
    @FXML private TableView<AgendaSekolah> agendaTable;
    @FXML private TableColumn<AgendaSekolah, String> agendaContentCol;
    @FXML private TableColumn<AgendaSekolah, LocalDate> agendaStartCol, agendaEndCol;

    // Extracurricular Mentor Tab
    @FXML private Tab extracurricularTab;
    @FXML private TableView<Ekstrakurikuler> mentorExtracurricularTable;
    @FXML private TableColumn<Ekstrakurikuler, String> mentorExtraNameCol, mentorExtraLevelCol;
    @FXML private ComboBox<Ekstrakurikuler> mentorExtraSelectCombo;
    @FXML private Label mentorLevelLabel;
    @FXML private ComboBox<String> mentorLevelSelectCombo;
    @FXML private ComboBox<TahunAjaran> mentorSchoolYearCombo;
    @FXML private TableView<Siswa> mentorStudentTable;
    @FXML private TableColumn<Siswa, String> mentorStudentNisCol, mentorStudentNameCol, mentorStudentGenderCol, mentorStudentClassCol;

    private Guru currentTeacher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAllUIComponents();
        loadInitialData();

        User loggedInUser = SessionManager.getLoggedInUser();
        if (loggedInUser != null) {
            String username = loggedInUser.getUsername();
            this.currentTeacher = guruDAO.getGuruByNip(username);

            if (this.currentTeacher != null) {
                welcome.setText("Welcome, " + currentTeacher.getNama() + "!");
                loadDataForGuru();
            } else {
                showAlert(Alert.AlertType.ERROR, "Data Error", "Detail data untuk guru dengan NIP '" + username + "' tidak ditemukan.");
            }
        } else {
            welcome.setText("Welcome, Guest!");
            showAlert(Alert.AlertType.WARNING, "Sesi Tidak Ditemukan", "Tidak dapat menemukan data user yang login.");
        }
    }

    private void loadDataForGuru() {
        if (currentTeacher == null) return;

        // Populate Biodata Tab
        biodataNipField.setText(currentTeacher.getNip());
        biodataNameField.setText(currentTeacher.getNama());
        biodataGenderCombo.setValue(currentTeacher.getJenisKelamin());
        biodataEmailField.setText(currentTeacher.getEmail());
        biodataPhoneField.setText(currentTeacher.getNoHp());

        editBiodataNipField.setText(currentTeacher.getNip());
        editBiodataNameField.setText(currentTeacher.getNama());
        editBiodataGenderCombo.setValue(currentTeacher.getJenisKelamin());
        editBiodataEmailField.setText(currentTeacher.getEmail());
        editBiodataPhoneField.setText(currentTeacher.getNoHp());

        editPasswordUsernameField.setText(currentTeacher.getUsernameUser());
        editPasswordOldPassField.setDisable(false);
        editPasswordNewPassField.setDisable(false);

        // Load Class Schedule for the current teacher
        scheduleTable.setItems(FXCollections.observableArrayList(
                jadwalKelasDAO.getAllJadwalKelasDetails().stream()
                        .filter(j -> Objects.equals(j.getNipGuru(), currentTeacher.getNip()))
                        .collect(Collectors.toList())
        ));



        // Load Assignments (now general tasks)
        assignmentTable.setItems(FXCollections.observableArrayList(tugasDAO.getTugasByGuruNip(currentTeacher.getNip())));

        checkAndSetupConditionalTabs();
    }

    private void setupAllUIComponents() {
        setupTableColumns();
        populateStaticComboBoxes();
        addListeners();
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

    private void setupTableColumns() {
        // Announcements Table
        announcementsTitleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        announcementsDateCol.setCellValueFactory(cellData -> new javafx.beans.property
                .SimpleStringProperty(cellData.getValue().getTanggal().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        announcementsContentCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        announcementsAttachmentCol.setCellValueFactory(new PropertyValueFactory<>("lampiran"));

        // Schedule Table
        scheduleDayCol.setCellValueFactory(new PropertyValueFactory<>("hari"));
        scheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("jamMulai"));
        scheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("jamSelesai"));
        scheduleSubjectCol.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        scheduleClassCol.setCellValueFactory(new PropertyValueFactory<>("kelasDanTahunAjaran"));

        // Existing Score Table
        existingScoreStudentCol.setCellValueFactory(new PropertyValueFactory<>("namaSiswa"));
        existingScoreExamTypeCol.setCellValueFactory(new PropertyValueFactory<>("jenisUjian"));
        existingScoreCol.setCellValueFactory(new PropertyValueFactory<>("nilai"));

        // Assignment Table
        assignmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        assignmentDescCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        assignmentDeadlineCol.setCellValueFactory(new PropertyValueFactory<>("tanggalDeadline"));
        assignmentSubjectCol.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        assignmentClassCol.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));

        // Homeroom Students Table
        homeroomNisCol.setCellValueFactory(new PropertyValueFactory<>("nis"));
        homeroomStudentNameCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        homeroomGenderCol.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));

        // Homeroom Attendance Table
        homeroomAttendanceDateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        homeroomAttendanceStudentCol.setCellValueFactory(new PropertyValueFactory<>("namaSiswa"));
        homeroomAttendanceStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        homeroomAttendanceClassCol.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));

        // Agenda Table
        agendaContentCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        agendaStartCol.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));
        agendaEndCol.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));

        // Mentor Extracurricular Table
        mentorExtraNameCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        mentorExtraLevelCol.setCellValueFactory(new PropertyValueFactory<>("tingkat"));

        // Mentor Student Table
        mentorStudentNisCol.setCellValueFactory(new PropertyValueFactory<>("nis"));
        mentorStudentNameCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        mentorStudentGenderCol.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        mentorStudentClassCol.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));

        wrapTextColumn(announcementsTitleCol);
        wrapTextColumn(announcementsContentCol);
        wrapTextColumn(announcementsAttachmentCol);
        centerAlignColumn(announcementsDateCol);

        centerAlignColumn(scheduleClassCol);
        centerAlignColumn(scheduleDayCol);
        centerAlignColumn(scheduleSubjectCol);
        centerAlignColumn(scheduleStartCol);
        centerAlignColumn(scheduleEndCol);

        centerAlignColumn(existingScoreStudentCol);
        centerAlignColumn(existingScoreExamTypeCol);
        centerAlignColumn(existingScoreCol);

        wrapTextColumn(assignmentDescCol);
        wrapTextColumn(assignmentTitleCol);
        wrapTextColumn(assignmentClassCol);
        wrapTextColumn(assignmentDeadlineCol);
        wrapTextColumn(assignmentSubjectCol);
        centerAlignColumn(assignmentClassCol);
        centerAlignColumn(assignmentDeadlineCol);
        centerAlignColumn(assignmentSubjectCol);

        centerAlignColumn(homeroomNisCol);
        centerAlignColumn(homeroomGenderCol);
        centerAlignColumn(homeroomAttendanceClassCol);
        centerAlignColumn(homeroomAttendanceDateCol);
        centerAlignColumn(homeroomAttendanceStatusCol);

        centerAlignColumn(agendaContentCol);
        centerAlignColumn(agendaStartCol);
        centerAlignColumn(agendaEndCol);

        centerAlignColumn(mentorExtraLevelCol);
        centerAlignColumn(mentorExtraNameCol);
        centerAlignColumn(mentorStudentNisCol);
        centerAlignColumn(mentorStudentGenderCol);
        centerAlignColumn(mentorStudentClassCol);
    }

    private void populateStaticComboBoxes() {
        biodataGenderCombo.getItems().addAll("Laki-laki", "Perempuan");
        editBiodataGenderCombo.getItems().addAll("Laki-laki", "Perempuan");
        inputScoreExamTypeCombo.getItems().addAll("UTS 1", "UTS 2", "UAS 1", "UAS 2", "PH 1", "PH 2","PH 3", "PH 4");
        homeroomAttendanceStatusCombo.getItems().addAll("Hadir", "Izin", "Sakit", "Alpa");
        mentorLevelSelectCombo.getItems().addAll("Siaga", "Penggalang");
        mentorLevelSelectCombo.setPromptText("Semua Level");
    }

    private void addListeners() {
        // untuk Input Score Tab: filter siswa berdasarkan kelas dan tahun ajaran
        inputScoreClassCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && inputScoreSchoolYearCombo.getValue() != null) {
                ObservableList<Siswa> studentsInClass = FXCollections.observableArrayList(
                        siswaDAO.getStudentsInClassByTahunAjaran(newVal.getIdKelas(), inputScoreSchoolYearCombo.getValue().getIdTahunAjaran())
                );
                inputScoreStudentCombo.setItems(studentsInClass);
            } else {
                inputScoreStudentCombo.setItems(FXCollections.observableArrayList());
            }
            filterExistingScores();
        });

        // untuk Input Score Tab: filter siswa berdasarkan kelas dan tahun ajaran
        inputScoreSchoolYearCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                List<Kelas> filteredKelas = kelasDAO.getAllKelas().stream()
                        .filter(k -> Objects.equals(k.getIdTahunAjaran(), newVal.getIdTahunAjaran()))
                        .collect(Collectors.toList());
                inputScoreClassCombo.setItems(FXCollections.observableArrayList(filteredKelas));
                inputScoreClassCombo.getSelectionModel().clearSelection();
            } else {
                inputScoreClassCombo.getItems().clear();
            }
            filterExistingScores();
        });

        assignmentClassCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newKelas) -> {
            if (newKelas != null) {
                TahunAjaran associatedTahunAjaran = assignmentSchoolYearCombo.getItems().stream()
                        .filter(ta -> Objects.equals(ta.getIdTahunAjaran(), newKelas.getIdTahunAjaran()))
                        .findFirst()
                        .orElse(null);

                assignmentSchoolYearCombo.getSelectionModel().select(associatedTahunAjaran);
            } else {
                assignmentSchoolYearCombo.getSelectionModel().clearSelection();
            }
        });


        inputScoreSubjectCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterExistingScores());
        inputScoreStudentCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterExistingScores());
        inputScoreExamTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterExistingScores());


        agendaSchoolYearCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterSchoolAgenda());

        mentorExtraSelectCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            boolean isPramuka = n != null && n.getNama().equalsIgnoreCase("Pramuka");
            mentorLevelLabel.setVisible(isPramuka);
            mentorLevelSelectCombo.setVisible(isPramuka);
            mentorLevelSelectCombo.setDisable(!isPramuka);
            if (!isPramuka) {
                mentorLevelSelectCombo.getSelectionModel().clearSelection();
            }
            filterMentorStudents();
        });
        mentorLevelSelectCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> filterMentorStudents());
        mentorSchoolYearCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterMentorStudents());


        // untuk Homeroom Tab:
        homeroomSchoolYearCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (currentTeacher != null && newVal != null) {
                Kelas homeroomClass = kelasDAO.getKelasByNipAndTahunAjaran(currentTeacher.getNip(), newVal.getIdTahunAjaran());
                if (homeroomClass != null) {
                    homeroomClassField.setText(homeroomClass.getNamaKelas() + " (" + homeroomClass.getTahunAjaranLengkap() + ")");
                    ObservableList<Siswa> students = FXCollections.observableArrayList(
                            siswaDAO.getStudentsInClassByTahunAjaran(homeroomClass.getIdKelas(), homeroomClass.getIdTahunAjaran())
                    );
                    homeroomStudentsTable.setItems(students);
                    homeroomAttendanceStudentCombo.setItems(students);
                    homeroomRaporStudentCombo.setItems(students);
                } else {
                    homeroomClassField.clear();
                    homeroomStudentsTable.setItems(FXCollections.observableArrayList());
                    homeroomAttendanceStudentCombo.setItems(FXCollections.observableArrayList());
                    homeroomRaporStudentCombo.setItems(FXCollections.observableArrayList());
                    homeroomAttendanceTable.setItems(FXCollections.observableArrayList());
                }
            } else {
                homeroomClassField.clear();
                homeroomStudentsTable.setItems(FXCollections.observableArrayList());
                homeroomAttendanceStudentCombo.setItems(FXCollections.observableArrayList());
                homeroomRaporStudentCombo.setItems(FXCollections.observableArrayList());
                homeroomAttendanceTable.setItems(FXCollections.observableArrayList());
            }
            filterHomeroomAttendanceByStudent();
        });

        // untuk memfilter tabel absensi setelah siswa dipilih
        homeroomAttendanceStudentCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filterHomeroomAttendanceByStudent();
        });
        // untuk memfilter tabel absensi setelah tanggal/status absensi diubah (jika relevan untuk filtering)
        homeroomAttendanceDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterHomeroomAttendanceByStudent());
        homeroomAttendanceStatusCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterHomeroomAttendanceByStudent());


        // input skor secara real-time
        addNumericOnlyListener(inputScoreField);
        addNumericOnlyListener(editScoreField);
    }

    private void loadInitialData() {
        announcementsTable.setItems(FXCollections.observableArrayList(pengumumanDAO.getAllPengumuman()));
        agendaTable.setItems(FXCollections.observableArrayList(agendaSekolahDAO.getAllAgendaSekolah()));

        ObservableList<TahunAjaran> allSchoolYears = FXCollections.observableArrayList(tahunAjaranDAO.getAllTahunAjaran());
        inputScoreSchoolYearCombo.setItems(allSchoolYears);
        assignmentSchoolYearCombo.setItems(allSchoolYears);
        agendaSchoolYearCombo.setItems(allSchoolYears);
        homeroomSchoolYearCombo.setItems(allSchoolYears);
        mentorSchoolYearCombo.setItems(allSchoolYears);

        // input score and assignment
        inputScoreClassCombo.setItems(FXCollections.observableArrayList(kelasDAO.getAllKelas()));
        inputScoreSubjectCombo.setItems(FXCollections.observableArrayList(mataPelajaranDAO.getAllMataPelajaran()));
        assignmentClassCombo.setItems(FXCollections.observableArrayList(kelasDAO.getAllKelas()));
        assignmentSubjectCombo.setItems(FXCollections.observableArrayList(mataPelajaranDAO.getAllMataPelajaran()));

        // mentor tab
        mentorExtracurricularTable.setItems(FXCollections.observableArrayList(ekstrakurikulerDAO.getAllEkstrakurikulerWithMentors()));
        mentorExtraSelectCombo.setItems(FXCollections.observableArrayList(ekstrakurikulerDAO.getAllEkstrakurikuler()));
    }

    private void checkAndSetupConditionalTabs() {
        boolean isHomeroomTeacherForAnyYear = kelasDAO.getAllKelas().stream()
                .anyMatch(k -> Objects.equals(k.getNipWaliKelas(), currentTeacher.getNip()));
        homeroomTab.setDisable(!isHomeroomTeacherForAnyYear);

        TahunAjaran selectedTahunAjaran = homeroomSchoolYearCombo.getValue();
        if (selectedTahunAjaran == null) {
            homeroomClassField.clear();
            homeroomStudentsTable.getItems().clear();
            homeroomAttendanceStudentCombo.getItems().clear();
            homeroomRaporStudentCombo.getItems().clear();
        } else {
            final int finalIdTahunAjaran = selectedTahunAjaran.getIdTahunAjaran();
            Kelas homeroomClass = kelasDAO.getAllKelas().stream()
                    .filter(k -> Objects.equals(k.getNipWaliKelas(), currentTeacher.getNip()) && k.getIdTahunAjaran() == finalIdTahunAjaran)
                    .findFirst()
                    .orElse(null);

            if (homeroomClass != null) {
                homeroomClassField.setText(homeroomClass.toString());
                List<Siswa> students = siswaDAO.getStudentsInClass(homeroomClass.getIdKelas(), homeroomClass.getIdTahunAjaran());
                ObservableList<Siswa> studentList = FXCollections.observableArrayList(students);
                homeroomStudentsTable.setItems(studentList);
                homeroomAttendanceStudentCombo.setItems(studentList);
                homeroomRaporStudentCombo.setItems(studentList);
            } else {
                homeroomClassField.clear();
                homeroomStudentsTable.getItems().clear();
                homeroomAttendanceStudentCombo.getItems().clear();
                homeroomRaporStudentCombo.getItems().clear();
            }
        }

        List<Ekstrakurikuler> mentoredEkskul = pembinaDAO.getAllPembina().stream()
                .filter(p -> Objects.equals(p.getNipGuru(), currentTeacher.getNip()))
                .map(p -> ekstrakurikulerDAO.getEkstrakurikulerById(p.getIdEkstrakurikuler()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        extracurricularTab.setDisable(mentoredEkskul.isEmpty());
        if (!mentoredEkskul.isEmpty()) {
            mentorExtracurricularTable.setItems(FXCollections.observableArrayList(mentoredEkskul));
            mentorExtraSelectCombo.setItems(FXCollections.observableArrayList(mentoredEkskul));
            filterMentorStudents();
        }
        mentorLevelLabel.setVisible(false);
        mentorLevelSelectCombo.setVisible(false);
        mentorLevelSelectCombo.setDisable(true);
    }

    private void filterMentorStudents() {
        Ekstrakurikuler selectedEkskul = mentorExtraSelectCombo.getValue();
        String selectedPramukaLevel = mentorLevelSelectCombo.getValue();
        TahunAjaran selectedSchoolYear = mentorSchoolYearCombo.getSelectionModel().getSelectedItem();

        if (selectedEkskul == null || selectedSchoolYear == null) {
            mentorStudentTable.getItems().clear();
            return;
        }

        List<Siswa> studentsInEkskul = pesertaEkskulDAO.getStudentsByExtracurricularAndTahunAjaran(
                selectedEkskul.getIdEkstrakurikuler(), selectedSchoolYear.getIdTahunAjaran()
        );

        if (selectedEkskul.getNama().equalsIgnoreCase("Pramuka") && selectedPramukaLevel != null && !selectedPramukaLevel.isEmpty()) {
            final List<String> siagaGrades = Arrays.asList("1", "2", "3", "4");
            final List<String> penggalangGrades = Arrays.asList("5", "6");

            List<Siswa> filteredStudents = studentsInEkskul.stream()
                    .filter(siswa -> {
                        if (siswa.getIdKelas() == null) return false;
                        Kelas kelasSiswa = kelasDAO.getKelasById(siswa.getIdKelas());
                        if (kelasSiswa == null || kelasSiswa.getTingkat() == null) return false;
                        String siswaTingkat = kelasSiswa.getTingkat();

                        switch (selectedPramukaLevel.toLowerCase()) {
                            case "siaga":
                                return siagaGrades.contains(siswaTingkat);
                            case "penggalang":
                                return penggalangGrades.contains(siswaTingkat);
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
            mentorStudentTable.setItems(FXCollections.observableArrayList(filteredStudents));
        } else {
            mentorStudentTable.setItems(FXCollections.observableArrayList(studentsInEkskul));
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

    private void filterExistingScores() {
        Kelas selectedKelas = inputScoreClassCombo.getSelectionModel().getSelectedItem();
        MataPelajaran selectedMapel = inputScoreSubjectCombo.getSelectionModel().getSelectedItem();
        Siswa selectedSiswa = inputScoreStudentCombo.getSelectionModel().getSelectedItem();
        String selectedExamType = inputScoreExamTypeCombo.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = inputScoreSchoolYearCombo.getSelectionModel().getSelectedItem();

        if (currentTeacher == null || selectedMapel == null || selectedKelas == null || selectedTahunAjaran == null) {
            existingScoreTable.setItems(FXCollections.observableArrayList());
            return;
        }

        boolean isHomeroomTeacher = Objects.equals(currentTeacher.getNip(), selectedKelas.getNipWaliKelas());

        boolean guruMengajarDiJadwal = jadwalKelasDAO.getAllJadwalKelasDetails().stream()
                .anyMatch(j -> Objects.equals(j.getNipGuru(), currentTeacher.getNip()) &&
                        Objects.equals(j.getIdMapel(), selectedMapel.getIdMapel()) &&
                        Objects.equals(j.getIdKelas(), selectedKelas.getIdKelas()) &&
                        Objects.equals(j.getIdTahunAjaran(), selectedTahunAjaran.getIdTahunAjaran()));


        if (!guruMengajarDiJadwal) {
            existingScoreTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<NilaiUjian> filteredScores = nilaiUjianDAO.getNilaiUjianFiltered(
                selectedSiswa != null ? selectedSiswa.getNis() : null,
                selectedMapel.getIdMapel(),
                selectedExamType,
                selectedTahunAjaran.getIdTahunAjaran()
        );
        existingScoreTable.setItems(FXCollections.observableArrayList(filteredScores));
    }


    private void loadHomeroomAttendance(int idKelas, int idTahunAjaran) {
        ObservableList<Presensi> attendanceList = FXCollections.observableArrayList(
                presensiDAO.getPresensiByKelasAndTahunAjaran(idKelas, idTahunAjaran)
        );
        homeroomAttendanceTable.setItems(attendanceList);
    }

    private void filterHomeroomAttendanceByStudent() {
        Siswa selectedStudent = homeroomAttendanceStudentCombo.getSelectionModel().getSelectedItem();
        TahunAjaran selectedSchoolYear = homeroomSchoolYearCombo.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = homeroomAttendanceDatePicker.getValue();
        String selectedStatus = homeroomAttendanceStatusCombo.getSelectionModel().getSelectedItem();

        if (currentTeacher == null || selectedSchoolYear == null) {
            homeroomAttendanceTable.setItems(FXCollections.observableArrayList());
            return;
        }

        Kelas homeroomClass = kelasDAO.getKelasByNipAndTahunAjaran(currentTeacher.getNip(), selectedSchoolYear.getIdTahunAjaran());
        if (homeroomClass == null) {
            homeroomAttendanceTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Presensi> allAttendancesForClassAndYear = presensiDAO.getPresensiByKelasAndTahunAjaran(homeroomClass.getIdKelas(), homeroomClass.getIdTahunAjaran());

        List<Presensi> filteredList = allAttendancesForClassAndYear.stream()
                .filter(presensi -> {
                    boolean matchesStudent = (selectedStudent == null || presensi.getNisSiswa().equals(selectedStudent.getNis()));
                    boolean matchesDate = (selectedDate == null || presensi.getTanggal().isEqual(selectedDate));
                    boolean matchesStatus = (selectedStatus == null || selectedStatus.isEmpty() || presensi.getStatus().equalsIgnoreCase(selectedStatus));
                    return matchesStudent && matchesDate && matchesStatus;
                })
                .collect(Collectors.toList());

        homeroomAttendanceTable.setItems(FXCollections.observableArrayList(filteredList));
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addNumericOnlyListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    @FXML
    void actionLogout(ActionEvent event) {
        try {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectdouble/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman login.");
        }
    }

    @FXML
    void handleUpdateBiodataButtonAction(ActionEvent event) {
        String nama = editBiodataNameField.getText();
        String noHp = editBiodataPhoneField.getText();
        String email = editBiodataEmailField.getText();
        String jenisKelamin = editBiodataGenderCombo.getValue();

        if (nama.isEmpty() || jenisKelamin == null || email.isEmpty() || noHp.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Semua field pada Edit Biodata harus diisi.");
            return;
        }
        if (!nama.matches("[a-zA-Z\\s']+")) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nama hanya boleh mengandung huruf dan spasi.");
            return;
        }
        if (!noHp.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nomor telepon hanya boleh mengandung angka.");
            return;
        }

        if (!email.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Format email tidak valid.");
            return;
        }

        Guru updatedGuru = new Guru(
                currentTeacher.getNip(),
                currentTeacher.getIdUser(),
                nama,
                jenisKelamin,
                email,
                noHp
                ,
                currentTeacher.getUsernameUser(),
                currentTeacher.getPasswordUser()
        );

        if (guruDAO.updateGuru(updatedGuru)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data biodata berhasil diperbarui.");
            currentTeacher = guruDAO.getGuruByNip(currentTeacher.getNip());
            loadDataForGuru();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui data biodata di database.");
        }
    }

    @FXML
    void handleChangePasswordButtonAction(ActionEvent event) {
        String oldPassword = editPasswordOldPassField.getText();
        String newPassword = editPasswordNewPassField.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Password lama dan password baru harus diisi.");
            return;
        }

        if (!currentTeacher.getPasswordUser().equals(oldPassword)) {
            showAlert(Alert.AlertType.ERROR, "Autentikasi Gagal", "Password lama tidak cocok.");
            return;
        }

        if (userDAO.updatePassword(currentTeacher.getIdUser(), newPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil diubah.");

            currentTeacher = guruDAO.getGuruByNip(currentTeacher.getNip());
            editPasswordOldPassField.clear();
            editPasswordNewPassField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mengubah password.");
        }
    }

    @FXML
    void handleAddAssignmentButtonAction(ActionEvent event) {
        String title = assignmentTitleField.getText();
        String description = assignmentDescriptionArea.getText();
        LocalDate deadline = assignmentDeadlinePicker.getValue();

        MataPelajaran selectedMapel = assignmentSubjectCombo.getValue();
        Kelas selectedKelas = assignmentClassCombo.getValue();

        if (title.isEmpty() || description.isEmpty() || deadline == null ||
                selectedMapel == null || selectedKelas == null /*|| selectedTahunAjaran == null*/) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Judul, Deskripsi, Deadline, Mata Pelajaran, Kelas, dan Tahun Ajaran harus diisi.");
            return;
        }

        Tugas newTugas = new Tugas(
                0,
                title,
                description,
                deadline,
                Integer.valueOf(selectedMapel.getIdMapel()),
                Integer.valueOf(selectedKelas.getIdKelas()),
                currentTeacher.getNip()
        );

        if (tugasDAO.addTugas(newTugas)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Tugas baru berhasil ditambahkan.");

            assignmentTable.setItems(FXCollections.observableArrayList(tugasDAO.getTugasByGuruNip(currentTeacher.getNip())));
            assignmentTitleField.clear();
            assignmentDescriptionArea.clear();
            assignmentDeadlinePicker.setValue(null);
            assignmentClassCombo.getSelectionModel().clearSelection();
            assignmentSubjectCombo.getSelectionModel().clearSelection();
            assignmentSchoolYearCombo.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menambahkan tugas ke database.");
        }
    }

    @FXML
    void handleRecordAttendanceButtonAction(ActionEvent event) {
        Siswa selectedStudent = homeroomAttendanceStudentCombo.getSelectionModel().getSelectedItem();
        LocalDate attendanceDate = homeroomAttendanceDatePicker.getValue();
        String status = homeroomAttendanceStatusCombo.getSelectionModel().getSelectedItem();
        TahunAjaran selectedSchoolYear = homeroomSchoolYearCombo.getSelectionModel().getSelectedItem();

        if (selectedStudent == null || attendanceDate == null || status == null || selectedSchoolYear == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Semua field absensi harus diisi.");
            return;
        }

        Kelas homeroomClass = kelasDAO.getKelasByNipAndTahunAjaran(currentTeacher.getNip(), selectedSchoolYear.getIdTahunAjaran());

        if (homeroomClass == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Guru ini bukan wali kelas untuk tahun ajaran yang dipilih, atau kelas tidak ditemukan.");
            return;
        }

        if (selectedStudent.getIdKelas() == null || !selectedStudent.getIdKelas().equals(homeroomClass.getIdKelas()) ||
                selectedStudent.getIdTahunAjaran() == null || !selectedStudent.getIdTahunAjaran().equals(homeroomClass.getIdTahunAjaran())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Siswa yang dipilih tidak terdaftar di kelas homeroom Anda untuk tahun ajaran ini.");
            return;
        }


        List<Presensi> existingAttendances = presensiDAO.getPresensiByNisAndTahunAjaran(selectedStudent.getNis(), selectedSchoolYear.getIdTahunAjaran()).stream()
                .filter(p -> p.getTanggal().isEqual(attendanceDate) && Objects.equals(p.getIdKelas(), homeroomClass.getIdKelas()))
                .collect(Collectors.toList());

        if (!existingAttendances.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Absensi Sudah Ada", "Absensi untuk siswa ini pada tanggal " + attendanceDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " sudah dicatat.");
            return;
        }

        Presensi newPresensi = new Presensi(
                0,
                attendanceDate,
                status,
                selectedStudent.getNis(),
                homeroomClass.getIdKelas()
        );

        if (presensiDAO.addPresensi(newPresensi)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Absensi berhasil dicatat.");
            filterHomeroomAttendanceByStudent();
            homeroomAttendanceStudentCombo.getSelectionModel().clearSelection();
            homeroomAttendanceDatePicker.setValue(null);
            homeroomAttendanceStatusCombo.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal mencatat absensi.");
        }

    }

    @FXML
    void handleSubmitScoreButtonAction(ActionEvent event) {
        String scoreText = inputScoreField.getText();
        if (scoreText.isEmpty() || !scoreText.matches("\\d*\\.?\\d*")) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nilai harus berupa angka yang valid.");
            return;
        }
        try {
            Kelas kelas = inputScoreClassCombo.getValue();
            MataPelajaran mapel = inputScoreSubjectCombo.getValue();
            Siswa siswa = inputScoreStudentCombo.getValue();
            String examType = inputScoreExamTypeCombo.getValue();
            TahunAjaran tahunAjaran = inputScoreSchoolYearCombo.getValue();

            if (kelas == null || mapel == null || siswa == null || examType == null || tahunAjaran == null) {
                showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Pastikan semua field terisi dengan benar (Kelas, Mata Pelajaran, Siswa, Tipe Ujian, Tahun Ajaran).");
                return;
            }

            boolean isHomeroomTeacher = Objects.equals(currentTeacher.getNip(), kelas.getNipWaliKelas());

            boolean guruMengajarDiJadwal = jadwalKelasDAO.getAllJadwalKelasDetails().stream()
                    .anyMatch(j -> Objects.equals(j.getNipGuru(), currentTeacher.getNip()) &&
                            Objects.equals(j.getIdMapel(), mapel.getIdMapel()) &&
                            Objects.equals(j.getIdKelas(), kelas.getIdKelas()) &&
                            Objects.equals(j.getIdTahunAjaran(), tahunAjaran.getIdTahunAjaran()));

            if (!guruMengajarDiJadwal) {
                showAlert(Alert.AlertType.INFORMATION, "Informasi", "Anda tidak mengajar mata pelajaran ini di kelas ini untuk tahun ajaran yang dipilih.");
                return;
            }

            BigDecimal score = new BigDecimal(scoreText);
            if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
                showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nilai harus di antara 0 dan 100.");
                return;
            }

            NilaiUjian newNilai = new NilaiUjian(0, examType, score, mapel.getIdMapel(), siswa.getNis(), tahunAjaran.getIdTahunAjaran());

            if(nilaiUjianDAO.addNilaiUjian(newNilai)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Nilai berhasil ditambahkan.");
                filterExistingScores();
                inputScoreField.clear();
                inputScoreExamTypeCombo.getSelectionModel().clearSelection();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menambahkan nilai. Nilai mungkin sudah ada untuk siswa ini pada mata pelajaran dan jenis ujian yang sama.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nilai yang dimasukkan bukan angka yang valid.");
        }
    }

    @FXML
    void handleUpdateScoreButtonAction(ActionEvent event) {
        NilaiUjian selectedNilai = existingScoreTable.getSelectionModel().getSelectedItem();
        if (selectedNilai == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih nilai dari tabel yang ingin diubah.");
            return;
        }
        String scoreText = editScoreField.getText();
        if (scoreText.isEmpty() || !scoreText.matches("\\d*\\.?\\d*")) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nilai harus berupa angka yang valid.");
            return;
        }
        try {
            BigDecimal newScore = new BigDecimal(scoreText);
            if (newScore.compareTo(BigDecimal.ZERO) < 0 || newScore.compareTo(new BigDecimal("100")) > 0) {
                showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nilai harus di antara 0 dan 100.");
                return;
            }
            selectedNilai.setNilai(newScore);
            if (nilaiUjianDAO.updateNilaiUjian(selectedNilai)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Nilai berhasil diperbarui.");
                filterExistingScores();
                editScoreField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui nilai.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Nilai yang dimasukkan bukan angka yang valid.");
        }
    }

    @FXML
    void handleRefreshScheduleButtonAction(ActionEvent event) {
        if (currentTeacher != null) {
            scheduleTable.setItems(FXCollections.observableArrayList(
                    jadwalKelasDAO.getAllJadwalKelasDetails().stream()
                            .filter(j -> Objects.equals(j.getNipGuru(), currentTeacher.getNip()))
                            .collect(Collectors.toList())
            ));
            showAlert(Alert.AlertType.INFORMATION, "Berhasil Refresh", "Jadwal telah dimuat ulang.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Data guru tidak ditemukan untuk refresh jadwal.");
        }
    }

    @FXML
    void handlePrintRaporButtonAction(ActionEvent event) {
        Siswa selectedStudent = homeroomRaporStudentCombo.getValue();
        TahunAjaran selectedSchoolYear = homeroomSchoolYearCombo.getValue();

        if(selectedStudent == null || selectedSchoolYear == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih siswa dan tahun ajaran untuk mencetak rapor.");
            return;
        }

        boolean isAlreadyPrinted = raporDao.isRaporPrinted(selectedStudent.getNis(), selectedSchoolYear.getIdTahunAjaran());
        if (isAlreadyPrinted) {
            showAlert(Alert.AlertType.INFORMATION, "Informasi", "Rapor untuk siswa ini di tahun ajaran ini sudah dicetak sebelumnya.");
        }

        if (!isAlreadyPrinted) {
            Rapor newRaporEntry = new Rapor(selectedStudent.getNis(), selectedSchoolYear.getIdTahunAjaran(), LocalDate.now());
            raporDao.addRapor(newRaporEntry);
        }

        // 1. Ambil data siswa dan kelasnya
        Siswa studentDetails = siswaDAO.getSiswaByNis(selectedStudent.getNis());
        Kelas studentClass = null;
        if (studentDetails != null && studentDetails.getIdKelas() != null) {
            studentClass = kelasDAO.getKelasById(studentDetails.getIdKelas());
        }

        // 2. Ambil nama wali kelas
        String waliKelasName = "N/A";
        if (studentClass != null && studentClass.getNipWaliKelas() != null) {
            Guru waliKelas = guruDAO.getGuruByNip(studentClass.getNipWaliKelas());
            if (waliKelas != null) {
                waliKelasName = waliKelas.getNama();
            }
        }


        // 3. Ambil daftar nilai ujian siswa untuk tahun ajaran tersebut
        List<NilaiUjian> examScores = nilaiUjianDAO.getNilaiUjianByNisAndTahunAjaran(selectedStudent.getNis(), selectedSchoolYear.getIdTahunAjaran());

        // 4. Ambil daftar presensi siswa untuk tahun ajaran tersebut
        List<Presensi> attendances = presensiDAO.getPresensiByNisAndTahunAjaran(selectedStudent.getNis(), selectedSchoolYear.getIdTahunAjaran());

        // 5. Konten rapor
        StringBuilder raporContent = new StringBuilder();
        raporContent.append("=============== RAPOR SISWA ===============\n");
        raporContent.append("Nama Siswa    : ").append(studentDetails != null ? studentDetails.getNama() : "N/A").append("\n");
        raporContent.append("NIS           : ").append(selectedStudent.getNis()).append("\n");
        raporContent.append("Kelas         : ").append(studentClass != null ? studentClass.getNamaKelas() : "N/A").append("\n");
        raporContent.append("Tahun Ajaran  : ").append(selectedSchoolYear.getTahunAjaranLengkap()).append("\n");
        raporContent.append("-------------------------------------------\n");
        raporContent.append("NILAI UJIAN:\n");
        if (examScores.isEmpty()) {
            raporContent.append("Tidak ada data nilai ujian.\n");
        } else {
            for (NilaiUjian nilai : examScores) {
                raporContent.append("- ").append(nilai.getNamaMapel()).append(" (").append(nilai.getJenisUjian()).append("): ").append(nilai.getNilai()).append("\n");
            }
        }
        raporContent.append("-------------------------------------------\n");
        raporContent.append("REKAPITULASI PRESENSI:\n");
        if (attendances.isEmpty()) {
            raporContent.append("Tidak ada data presensi.\n");
        } else {
            long totalHadir = attendances.stream().filter(p -> "Hadir".equalsIgnoreCase(p.getStatus())).count();
            long totalIzin = attendances.stream().filter(p -> "Izin".equalsIgnoreCase(p.getStatus())).count();
            long totalSakit = attendances.stream().filter(p -> "Sakit".equalsIgnoreCase(p.getStatus())).count();
            long totalAlpa = attendances.stream().filter(p -> "Alpa".equalsIgnoreCase(p.getStatus())).count();
            long totalPresensi = attendances.size();

            raporContent.append("Total Hari Presensi : ").append(totalPresensi).append(" catatan\n");
            raporContent.append("Jumlah Hadir        : ").append(totalHadir).append(" catatan\n");
            raporContent.append("Jumlah Izin         : ").append(totalIzin).append(" catatan\n");
            raporContent.append("Jumlah Sakit        : ").append(totalSakit).append(" catatan\n");
            raporContent.append("Jumlah Alpa         : ").append(totalAlpa).append(" catatan\n");
        }
        raporContent.append("-------------------------------------------\n");
        raporContent.append("Nama Wali Kelas : ").append(waliKelasName).append("\n");
        raporContent.append("Tanggal Cetak   : ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append("\n");
        raporContent.append("===========================================\n");


        // 6. Simpan file TXT ke folder Downloads
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Rapor");
        String sanitizedFileName = selectedStudent.getNama().replaceAll("[^a-zA-Z0-9.-]", "_") + " - Rapor " + selectedSchoolYear.getTahunAjaranLengkap().replace("/", "-") + ".txt";
        fileChooser.setInitialFileName(sanitizedFileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));

        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        if (downloadsDir.exists() && downloadsDir.isDirectory()) {
            fileChooser.setInitialDirectory(downloadsDir);
        } else {
            fileChooser.setInitialDirectory(new File(userHome));
        }


        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                java.nio.file.Files.write(file.toPath(), raporContent.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Rapor berhasil disimpan ke:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan rapor:\n" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
