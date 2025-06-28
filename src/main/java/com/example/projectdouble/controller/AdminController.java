package com.example.projectdouble.controller;

import com.example.projectdouble.DAO.*;
import com.example.projectdouble.model.*;
import com.example.projectdouble.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminController implements Initializable {
    @FXML
    private Button logout;
    @FXML
    private Label welcome;

    // Tab Pane (for switching tabs)
    @FXML
    private TabPane mainTabPane; // Tambahkan ini jika belum ada di FXML Anda
    @FXML
    private Tab entryStudentDataTab; // Tambahkan ini jika belum ada di FXML Anda

    // --- Components for Announcements Tab ---
    @FXML
    private TextField announcementTitleField;
    @FXML
    private TextArea announcementContentArea;
    @FXML
    private TextField announcementAttachmentField;
    @FXML
    private Button publishAnnouncementButton;
    @FXML
    private Button updateAnnouncementButton;
    @FXML
    private Button deleteAnnouncementButton;
    @FXML
    private TableView<Pengumuman> announcementTable;
    @FXML
    private TableColumn<Pengumuman, String> colAnnouncementTitle;
    @FXML
    private TableColumn<Pengumuman, String> colAnnouncementDate;
    @FXML
    private TableColumn<Pengumuman, String> colAnnouncementContent;
    @FXML
    private TableColumn<Pengumuman, String> colAnnouncementAttachment;
    @FXML
    private Button chooseFileButton;

    private PengumumanDAO pengumumanDAO;
    private ObservableList<Pengumuman> announcementList;

    // --- Components for Entry Student Data Tab ---
    @FXML
    private TextField nisInputField;
    @FXML
    private TextField studentNameInputField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField placeOfBirthInputField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextArea addressTextArea;
    @FXML
    private Button addStudentDataButton;

    @FXML
    private TextField studentUsernameInputField;
    @FXML
    private TextField studentPasswordInputField;
    @FXML
    private Button addStudentUserButton;

    @FXML
    private ComboBox<Siswa> selectNisEditComboBox;
    @FXML
    private TextField nisEditField;
    @FXML
    private TextField nameEditField;
    @FXML
    private ComboBox<String> genderEditComboBox;
    @FXML
    private TextField placeOfBirthEditField;
    @FXML
    private DatePicker dateOfBirthEditPicker;
    @FXML
    private TextArea addressEditArea;
    @FXML
    private Label usernameEditLabel;
    @FXML
    private Label passwordEditLabel;
    @FXML
    private Button updateStudentDataButton;
    @FXML
    private Button deleteStudentDataButton;

    private SiswaDAO siswaDao;
    private UserDAO userDao;


    // --- Components for View Students Tab ---
    @FXML
    private TextField searchStudentField;
    @FXML
    private TableView<Siswa> studentTable;
    @FXML
    private TableColumn<Siswa, String> colStudentNis;
    @FXML
    private TableColumn<Siswa, String> colStudentName;
    @FXML
    private TableColumn<Siswa, String> colStudentGender;
    @FXML
    private TableColumn<Siswa, String> colStudentPlaceOfBirth;
    @FXML
    private TableColumn<Siswa, LocalDate> colStudentDateOfBirth;
    @FXML
    private TableColumn<Siswa, String> colStudentAddress;
    @FXML
    private TableColumn<Siswa, String> colStudentClass; // New column for class
    @FXML
    private TableColumn<Siswa, String> colStudentSchoolYear; // New column for school year and semester
    private ObservableList<Siswa> studentList;


    // --- Components for View Teachers Tab ---
    @FXML
    private TextField searchTeacherField;
    @FXML
    private TableView<Guru> teacherTable;
    @FXML
    private TableColumn<Guru, String> colTeacherNip;
    @FXML
    private TableColumn<Guru, String> colTeacherPassword;
    @FXML
    private TableColumn<Guru, String> colTeacherName;
    @FXML
    private TableColumn<Guru, String> colTeacherGender;
    @FXML
    private TableColumn<Guru, String> colTeacherEmail;
    @FXML
    private TableColumn<Guru, String> colTeacherPhone;
    @FXML
    private Button deleteTeacherButton;
    private GuruDAO guruDao;
    private ObservableList<Guru> teacherList;


    // --- Components for Manage Schedules Tab ---
    @FXML
    private ChoiceBox<String> dayChoiceBox;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private ChoiceBox<MataPelajaran> subjectChoiceBox;
    @FXML
    private ChoiceBox<Kelas> classChoiceBox;
    @FXML
    private ChoiceBox<Guru> teacherChoiceBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearScheduleComboBox;
    @FXML
    private ComboBox<String> semesterScheduleComboBox;
    @FXML
    private Button addScheduleButton;

    // NEW FXML for schedule table
    @FXML
    private TableView<JadwalKelas> scheduleTable;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleDay;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleTime;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleSubject;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleClass;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleTeacher;
    @FXML
    private TableColumn<JadwalKelas, String> colScheduleSemester;
    // NEW: Delete Schedule Button
    @FXML private Button deleteScheduleButton;

    private ObservableList<JadwalKelas> scheduleList;


    private JadwalKelasDAO jadwalKelasDao;
    private MataPelajaranDAO mataPelajaranDao;
    private KelasDAO kelasDao;
    private TahunAjaranDAO tahunAjaranDao;


    // --- Components for Manage Classes Tab ---
    // CREATE NEW CLASS
    @FXML
    private TextField classNameInputField;
    @FXML
    private ChoiceBox<Guru> homeroomTeacherChoiceBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearCreateClassComboBox;
    @FXML
    private ComboBox<String> semesterCreateClassComboBox;
    @FXML
    private Button createClassButton;

    // EDIT/DELETE CLASS
    @FXML
    private ComboBox<Kelas> classNameEditComboBox;
    @FXML
    private ComboBox<Guru> homeroomTeacherEditComboBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearEditClassComboBox;
    @FXML
    private ComboBox<String> semesterEditClassComboBox;
    @FXML
    private Button updateClassButton;
    @FXML
    private Button deleteClassButton;

    // ASSIGN STUDENT TO CLASS
    @FXML
    private TextField searchStudentAssignClassField;
    @FXML
    private ComboBox<Kelas> classAssignComboBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearAssignComboBox;
    @FXML
    private ComboBox<String> semesterAssignComboBox;
    @FXML
    private TableView<Siswa> studentsToAssignTable;
    @FXML
    private TableColumn<Siswa, String> colAssignNis;
    @FXML
    private TableColumn<Siswa, String> colAssignName;
    @FXML
    private Button assignStudentButton;

    // STUDENT IN SELECTED CLASS
    @FXML
    private ComboBox<Kelas> classStudentsInSelectedClassComboBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearStudentsInSelectedClassComboBox;
    @FXML
    private ComboBox<String> semesterStudentsInSelectedClassComboBox;
    @FXML
    private TableView<Siswa> studentsInSelectedClassTable;
    @FXML
    private TableColumn<Siswa, String> colInClassNis;
    @FXML
    private TableColumn<Siswa, String> colInClassName;
    @FXML
    private Button editSelectedStudentInClassButton;
    @FXML
    private Button removeStudentFromClassButton;


    // --- Components for Manage School Agenda Tab ---
    @FXML
    private TextField agendaContentField;
    @FXML
    private DatePicker agendaStartDatePicker;
    @FXML
    private DatePicker agendaEndDatePicker;
    @FXML
    private ComboBox<TahunAjaran> schoolYearAgendaAddComboBox;
    @FXML
    private ComboBox<String> semesterAgendaAddComboBox;
    @FXML
    private Button addAgendaButton;

    @FXML
    private ComboBox<TahunAjaran> schoolYearAgendaViewComboBox;
    @FXML
    private ComboBox<String> semesterAgendaViewComboBox;
    @FXML
    private TableView<AgendaSekolah> agendaTable;
    @FXML
    private TableColumn<AgendaSekolah, String> colAgendaContent;
    @FXML
    private TableColumn<AgendaSekolah, LocalDate> colAgendaStart;
    @FXML
    private TableColumn<AgendaSekolah, LocalDate> colAgendaEnd;
    @FXML
    private Button deleteAgendaButton;
    private AgendaSekolahDAO agendaSekolahDao;
    private ObservableList<AgendaSekolah> agendaList;


    // --- Components for Extracurricular Tab ---
    // INPUT MENTOR
    @FXML
    private ComboBox<Ekstrakurikuler> extracurricularInputMentorComboBox;
    @FXML
    private ComboBox<Guru> mentor1ComboBox;
    @FXML
    private ComboBox<Guru> mentor2ComboBox;
    @FXML
    private ComboBox<Guru> mentor3ComboBox;
    @FXML
    private ComboBox<Guru> mentor4ComboBox;
    @FXML
    private ComboBox<String> levelInputMentorComboBox;
    @FXML
    private Button addMentorButton;
    @FXML
    private Button updateMentorButton;

    // EXTRACURRICULAR Table
    @FXML
    private TableView<Ekstrakurikuler> extracurricularTable;
    @FXML
    private TableColumn<Ekstrakurikuler, String> colExtracurricularName;
    @FXML
    private TableColumn<Ekstrakurikuler, String> colExtracurricularLevel;
    @FXML
    private TableColumn<Ekstrakurikuler, String> colExtracurricularMentor;
    @FXML
    private Button deleteExtracurricularButton;


    // INPUT SISWA to Extracurricular
    @FXML
    private ComboBox<Kelas> classInputSiswaEkskulComboBox;
    @FXML
    private ComboBox<TahunAjaran> schoolYearInputSiswaEkskulComboBox;
    @FXML
    private ComboBox<String> semesterInputSiswaEkskulComboBox;
    @FXML
    private ComboBox<Ekstrakurikuler> extracurricularInputSiswaEkskulComboBox;
    @FXML
    private ComboBox<String> levelInputSiswaEkskulComboBox;
    @FXML
    private ComboBox<Siswa> studentInputSiswaEkskulComboBox;
    @FXML
    private Button addStudentToExtracurricularButton;

    @FXML
    private TableView<Siswa> studentsInExtracurricularTable; // Renamed for clarity
    @FXML
    private TableColumn<Siswa, String> colExtracurricularStudentNis;
    @FXML
    private TableColumn<Siswa, String> colExtracurricularStudentName;
    @FXML
    private TableColumn<Siswa, String> colExtracurricularStudentClass;
    @FXML
    private Button removeStudentFromExtracurricularButton;
    private ObservableList<Siswa> studentsInSelectedExtracurricularList;
    private EkstrakurikulerDAO ekstrakurikulerDao;
    private PembinaDAO pembinaDao;
    private PesertaEkskulDAO pesertaEkskulDao;


    public void setUsername(String user){
        welcome.setText("Welcome, " + user + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize DAO
        pengumumanDAO = new PengumumanDAO();
        siswaDao = new SiswaDAO();
        userDao = new UserDAO();
        guruDao = new GuruDAO();
        jadwalKelasDao = new JadwalKelasDAO();
        mataPelajaranDao = new MataPelajaranDAO();
        kelasDao = new KelasDAO();
        tahunAjaranDao = new TahunAjaranDAO();
        agendaSekolahDao = new AgendaSekolahDAO();
        ekstrakurikulerDao = new EkstrakurikulerDAO();
        pembinaDao = new PembinaDAO();
        pesertaEkskulDao = new PesertaEkskulDAO();


        // Set welcome message
        User loggedInUser = SessionManager.getLoggedInUser();
        if (loggedInUser != null) {
            welcome.setText("Welcome, " + loggedInUser.getDisplayName() + "!");
        } else {
            welcome.setText("Welcome, Guest!");
        }

        // Initialize UI components for each tab
        setupAnnouncementTab();
        setupStudentEntryTab();
        setupViewStudentsTab();
        setupViewTeachersTab();
        setupManageSchedulesTab();
        setupManageClassesTab();
        setupManageSchoolAgendaTab();
        setupExtracurricularTab();

        // Load initial data
        loadAnnouncements();
        loadAllStudents();
        loadAllTeachers();
        loadTeachersIntoComboBoxes(); // For Kelas, Jadwal, Pembina
        loadTahunAjaranIntoComboBoxes(); // For Kelas, Jadwal, Agenda
        loadKelasIntoComboBoxes(); // For Jadwal, Assign Student, Siswa Ekskul
        loadMataPelajaranIntoChoiceBoxes(); // For Jadwal
        loadEkstrakurikulerIntoComboBoxes(); // For Pembina and Peserta Ekskul
        loadStudentsForExtracurricularAssignment(); // Load students initially for extracurricular assignment
        loadSchedules(); // Call to load schedules on initialization

        // Listeners for changes in selection to populate edit forms/tables
        setupListeners();
    }

    // --- Logout Action ---
    @FXML
    public void actionLogout(ActionEvent actionEvent) {
        try {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectdouble/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
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
                        setGraphic(null); // Important to clear graphic if any
                    } else {
                        setText(item.toString());
                        setGraphic(null); // Important to clear graphic if any
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
                    text.wrappingWidthProperty().bind(column.widthProperty().subtract(5)); // Subtract a bit for padding
                    setGraphic(text);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setAlignment(Pos.CENTER_LEFT); // Default left alignment if only wrapping
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


    // --- Helper Methods to setup Tabs ---

    private void setupAnnouncementTab() {
        colAnnouncementTitle.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colAnnouncementDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanggal().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        colAnnouncementContent.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colAnnouncementAttachment.setCellValueFactory(new PropertyValueFactory<>("lampiran"));

        // Listener for selecting announcements and populating the edit form
        announcementTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateAnnouncementFields(newSelection);
            } else {
                clearAnnouncementFields();
            }
        });

        wrapTextColumn(colAnnouncementTitle);
        wrapTextColumn(colAnnouncementContent); // Apply wrapping to content
        wrapTextColumn(colAnnouncementAttachment); // Apply wrapping to attachment
        centerAlignColumn(colAnnouncementDate); // Keep date centered
    }

    private void setupStudentEntryTab() {
        genderComboBox.setItems(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        genderEditComboBox.setItems(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        // Listener for selectNisEditComboBox
        selectNisEditComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateEditStudentFields(newSelection);
            } else {
                clearEditStudentFields();
            }
        });
    }

    private void setupViewStudentsTab() {
        colStudentNis.setCellValueFactory(new PropertyValueFactory<>("nis"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colStudentGender.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colStudentPlaceOfBirth.setCellValueFactory(new PropertyValueFactory<>("tempatLahir"));
        colStudentDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colStudentAddress.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colStudentClass.setCellValueFactory(new PropertyValueFactory<>("namaKelas")); // From Siswa model's namaKelas
        colStudentSchoolYear.setCellValueFactory(cellData -> {
            Siswa siswa = cellData.getValue();
            String tahunAjaran = siswa.getTahunAjaranLengkap();
            // Removed semester
            if (tahunAjaran != null) {
                return new SimpleStringProperty(tahunAjaran);
            }
            return new SimpleStringProperty("");
        });

        // Format date of birth for display
        colStudentDateOfBirth.setCellFactory(column -> new TableCell<Siswa, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Apply center alignment to desired student columns
        centerAlignColumn(colStudentNis);
        centerAlignColumn(colStudentGender);
        centerAlignColumn(colStudentPlaceOfBirth);
        centerAlignColumn(colStudentDateOfBirth); // Already handled in cell factory above
        centerAlignColumn(colStudentClass);
        centerAlignColumn(colStudentSchoolYear);
        wrapTextColumn(colStudentAddress);
        wrapTextColumn(colStudentName);
    }

    private void setupViewTeachersTab() {
        colTeacherNip.setCellValueFactory(new PropertyValueFactory<>("nip"));
        colTeacherPassword.setCellValueFactory(new PropertyValueFactory<>("passwordUser")); // Display password (for demo, not for production)
        colTeacherName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colTeacherGender.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colTeacherEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTeacherPhone.setCellValueFactory(new PropertyValueFactory<>("noHp"));

        centerAlignColumn(colTeacherNip);
        centerAlignColumn(colTeacherPassword);
        centerAlignColumn(colTeacherEmail);
        centerAlignColumn(colTeacherGender);
        centerAlignColumn(colTeacherPhone);
    }

    private void setupManageSchedulesTab() {
        dayChoiceBox.setItems(FXCollections.observableArrayList("Senin", "Selasa", "Rabu", "Kamis", "Jumat"));

        // Setup Schedule Table Columns
        colScheduleDay.setCellValueFactory(new PropertyValueFactory<>("hari"));
        colScheduleTime.setCellValueFactory(new PropertyValueFactory<>("timeRange")); // Uses computed property
        colScheduleSubject.setCellValueFactory(new PropertyValueFactory<>("namaMapel"));
        colScheduleClass.setCellValueFactory(new PropertyValueFactory<>("namaKelas")); // Uses computed property
        colScheduleTeacher.setCellValueFactory(new PropertyValueFactory<>("namaGuru"));
        colScheduleSemester.setCellValueFactory(new PropertyValueFactory<>("tahunAjaranLengkap")); // Removed

        // Apply center alignment to all schedule columns
        centerAlignColumn(colScheduleDay);
        centerAlignColumn(colScheduleTime);
        centerAlignColumn(colScheduleSubject);
        centerAlignColumn(colScheduleClass);
        centerAlignColumn(colScheduleTeacher);
    }

    private void setupManageClassesTab() {
        // Assign Student to Class
        colAssignNis.setCellValueFactory(new PropertyValueFactory<>("nis"));
        colAssignName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        // Student in Selected Class
        colInClassNis.setCellValueFactory(new PropertyValueFactory<>("nis"));
        colInClassName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        centerAlignColumn(colAssignNis);
        centerAlignColumn(colAssignName);
        centerAlignColumn(colInClassName);
        centerAlignColumn(colInClassNis);

        // Listeners for classes and school years combo boxes to filter student lists
        classStudentsInSelectedClassComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadStudentsInSelectedClass());
        schoolYearStudentsInSelectedClassComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadStudentsInSelectedClass());

        classNameEditComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateEditClassFields(newSelection);
            } else {
                clearEditClassFields();
            }
        });
    }

    private void setupManageSchoolAgendaTab() {
        colAgendaContent.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colAgendaStart.setCellValueFactory(new PropertyValueFactory<>("tanggalMulai"));
        colAgendaEnd.setCellValueFactory(new PropertyValueFactory<>("tanggalSelesai"));

        centerAlignColumn(colAgendaContent);

        colAgendaStart.setCellFactory(column -> new TableCell<AgendaSekolah, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        colAgendaEnd.setCellFactory(column -> new TableCell<AgendaSekolah, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        centerAlignColumn(colAgendaStart);
        centerAlignColumn(colAgendaEnd);

        // semesterAgendaAddComboBox.setItems(FXCollections.observableArrayList("Ganjil", "Genap")); // Removed
        // semesterAgendaViewComboBox.setItems(FXCollections.observableArrayList("Ganjil", "Genap")); // Removed

        agendaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Not implemented: no edit agenda in FXML, only add/delete/view
            // If editing was desired, you'd populate fields here
        });

        // Add listeners for filtering agenda table
        schoolYearAgendaViewComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadSchoolAgenda());
    }


    private void setupExtracurricularTab() {
        levelInputMentorComboBox.setItems(FXCollections.observableArrayList("Siaga", "Penggalang")); // Example levels
        levelInputSiswaEkskulComboBox.setItems(FXCollections.observableArrayList("Siaga", "Penggalang")); // Example levels

        colExtracurricularName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colExtracurricularLevel.setCellValueFactory(new PropertyValueFactory<>("tingkat"));
        colExtracurricularMentor.setCellValueFactory(new PropertyValueFactory<>("mentorNames")); // Bind to the derived property

        colExtracurricularStudentNis.setCellValueFactory(new PropertyValueFactory<>("nis"));
        colExtracurricularStudentName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colExtracurricularStudentClass.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));

        centerAlignColumn(colExtracurricularName);
        centerAlignColumn(colExtracurricularLevel);
        centerAlignColumn(colExtracurricularStudentNis);
        centerAlignColumn(colExtracurricularStudentName);
        centerAlignColumn(colExtracurricularStudentClass);
        wrapTextColumn(colExtracurricularMentor);

        extracurricularTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // No edit fields for extracurricular in FXML, only delete.
            // If editing was desired, you'd populate fields here.
        });

        // Listener for extracurricularInputMentorComboBox to select existing ekskul for adding mentor
        extracurricularInputMentorComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Populate levelInputMentorComboBox based on selected extracurricular's level
                levelInputMentorComboBox.getSelectionModel().select(newVal.getTingkat());
            } else {
                levelInputMentorComboBox.getSelectionModel().clearSelection();
            }
        });

        // Listener for schoolYearInputSiswaEkskulComboBox to trigger student list load
        schoolYearInputSiswaEkskulComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadStudentsForExtracurricularAssignment();
        });

        // Listeners for classInputSiswaEkskulComboBox
        classInputSiswaEkskulComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadStudentsForExtracurricularAssignment());

        // Listener for extracurricularInputSiswaEkskulComboBox to load students in its table
        extracurricularInputSiswaEkskulComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadStudentsInExtracurricularTable(); // Load students when extracurricular is selected
        });

        studentsInExtracurricularTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // No edit fields for extracurricular in FXML, only delete.
            // If editing was desired, you'd populate fields here.
        });
    }

    private void setupListeners() {
        // Listener for searchStudentField on View Students tab
        searchStudentField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchStudent(null));
        // Listener for searchTeacherField on View Teachers tab
        searchTeacherField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchTeacher(null));
        // Listener for searchStudentAssignClassField on Manage Classes tab
        searchStudentAssignClassField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchStudentToAssign());
    }


    // --- Load Data Methods ---

    private void loadAnnouncements() {
        announcementList = FXCollections.observableArrayList(pengumumanDAO.getAllPengumuman());
        announcementTable.setItems(announcementList);
    }

    private void loadAllStudents() {
        studentList = FXCollections.observableArrayList(siswaDao.getAllSiswa());
        studentTable.setItems(studentList);
        selectNisEditComboBox.setItems(studentList); // Populate NIS for editing
        studentsToAssignTable.setItems(studentList); // Populate table for assigning
    }

    private void loadAllTeachers() {
        teacherList = FXCollections.observableArrayList(guruDao.getAllGuru());
        teacherTable.setItems(teacherList);
    }

    private void loadTeachersIntoComboBoxes() {
        ObservableList<Guru> teachers = FXCollections.observableArrayList(guruDao.getAllGuru());
        homeroomTeacherChoiceBox.setItems(teachers);
        homeroomTeacherEditComboBox.setItems(teachers);
        teacherChoiceBox.setItems(teachers);
        mentor1ComboBox.setItems(teachers);
        mentor2ComboBox.setItems(teachers);
        mentor3ComboBox.setItems(teachers);
        mentor4ComboBox.setItems(teachers);
    }

    private void loadTahunAjaranIntoComboBoxes() {
        ObservableList<TahunAjaran> tahunAjaranList = FXCollections.observableArrayList(tahunAjaranDao.getAllTahunAjaran());
        schoolYearScheduleComboBox.setItems(tahunAjaranList);
        schoolYearCreateClassComboBox.setItems(tahunAjaranList);
        schoolYearEditClassComboBox.setItems(tahunAjaranList);
        schoolYearAssignComboBox.setItems(tahunAjaranList);
        schoolYearStudentsInSelectedClassComboBox.setItems(tahunAjaranList);
        schoolYearAgendaAddComboBox.setItems(tahunAjaranList);
        schoolYearAgendaViewComboBox.setItems(tahunAjaranList);
        schoolYearInputSiswaEkskulComboBox.setItems(tahunAjaranList);
    }

    private void loadMataPelajaranIntoChoiceBoxes() {
        ObservableList<MataPelajaran> mapelList = FXCollections.observableArrayList(mataPelajaranDao.getAllMataPelajaran());
        subjectChoiceBox.setItems(mapelList);
    }

    private void loadKelasIntoComboBoxes() {
        ObservableList<Kelas> kelasList = FXCollections.observableArrayList(kelasDao.getAllKelas());
        classChoiceBox.setItems(kelasList);
        classNameEditComboBox.setItems(kelasList);
        classAssignComboBox.setItems(kelasList);
        classStudentsInSelectedClassComboBox.setItems(kelasList);
        classInputSiswaEkskulComboBox.setItems(kelasList);
    }

    private void loadEkstrakurikulerIntoComboBoxes() {
        ObservableList<Ekstrakurikuler> ekskulList = FXCollections.observableArrayList(ekstrakurikulerDao.getAllEkstrakurikulerWithMentors());
        extracurricularTable.setItems(ekskulList);
        extracurricularInputMentorComboBox.setItems(ekskulList);
        extracurricularInputSiswaEkskulComboBox.setItems(ekskulList);
    }

    private void loadStudentsForExtracurricularAssignment() {
        Kelas selectedClass = classInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();

        if (selectedClass != null && selectedTahunAjaran != null) {
            // Call DAO to get students based on class and academic year (no semester)
            ObservableList<Siswa> filteredStudents = FXCollections.observableArrayList(
                    siswaDao.getStudentsInClass(selectedClass.getIdKelas(), selectedTahunAjaran.getIdTahunAjaran()));
            studentInputSiswaEkskulComboBox.setItems(filteredStudents);
        } else {
            // Clear ComboBox if filters are not complete
            studentInputSiswaEkskulComboBox.setItems(FXCollections.observableArrayList());
        }
    }

    private void loadSchoolAgenda() {
        TahunAjaran selectedTahunAjaran = schoolYearAgendaViewComboBox.getSelectionModel().getSelectedItem();

        if (selectedTahunAjaran != null) {
            agendaList = FXCollections.observableArrayList(agendaSekolahDao.getAgendaByTahunAjaran(selectedTahunAjaran.getIdTahunAjaran()));
        } else {
            // Load all if no filter or handle error
            agendaList = FXCollections.observableArrayList(agendaSekolahDao.getAllAgendaSekolah());
        }
        agendaTable.setItems(agendaList);
    }

    private void loadStudentsInSelectedClass() {
        Kelas selectedKelas = classStudentsInSelectedClassComboBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearStudentsInSelectedClassComboBox.getSelectionModel().getSelectedItem();

        if (selectedKelas != null && selectedTahunAjaran != null) {
            ObservableList<Siswa> students = FXCollections.observableArrayList(
                    siswaDao.getStudentsInClass(selectedKelas.getIdKelas(), selectedTahunAjaran.getIdTahunAjaran()));
            studentsInSelectedClassTable.setItems(students);
        } else {
            studentsInSelectedClassTable.setItems(FXCollections.observableArrayList());
        }
    }

    // NEW: Load students enrolled in extracurriculars into the table
    private void loadStudentsInExtracurricularTable() {
        Ekstrakurikuler selectedEkskul = extracurricularInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();
        if (selectedEkskul != null) {
            // Assume PesertaEkskulDao has a method to get Siswa by extracurricular ID
            ObservableList<Siswa> students = FXCollections.observableArrayList(pesertaEkskulDao.getStudentsByExtracurricular(selectedEkskul.getIdEkstrakurikuler()));
            studentsInExtracurricularTable.setItems(students);
        } else {
            studentsInExtracurricularTable.setItems(FXCollections.observableArrayList()); // Clear table if no ekskul selected
        }
    }

    // NEW: Load all schedules into the table
    private void loadSchedules() {
        scheduleList = FXCollections.observableArrayList(jadwalKelasDao.getAllJadwalKelasDetails());
        scheduleTable.setItems(scheduleList);
    }


    // --- Populate / Clear Fields Methods ---

    private void populateAnnouncementFields(Pengumuman p) {
        announcementTitleField.setText(p.getJudul());
        announcementContentArea.setText(p.getDeskripsi());
        announcementAttachmentField.setText(p.getLampiran());
    }

    private void clearAnnouncementFields() {
        announcementTitleField.clear();
        announcementContentArea.clear();
        announcementAttachmentField.clear();
        announcementTable.getSelectionModel().clearSelection();
    }

    private void populateEditStudentFields(Siswa siswa) {
        nisEditField.setText(siswa.getNis());
        nameEditField.setText(siswa.getNama());
        genderEditComboBox.getSelectionModel().select(siswa.getJenisKelamin());
        placeOfBirthEditField.setText(siswa.getTempatLahir());
        dateOfBirthEditPicker.setValue(siswa.getTanggalLahir());
        addressEditArea.setText(siswa.getAlamat());
        usernameEditLabel.setText(siswa.getUsernameUser() != null ? siswa.getUsernameUser() : "N/A");
        passwordEditLabel.setText(siswa.getPasswordUser() != null ? siswa.getPasswordUser() : "N/A"); // For demo only
    }

    private void clearAddStudentFields() {
        nisInputField.clear();
        studentNameInputField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        placeOfBirthInputField.clear();
        dateOfBirthPicker.setValue(null);
        addressTextArea.clear();
    }

    private void clearAddStudentUserFields() {
        studentUsernameInputField.clear();
        studentPasswordInputField.clear();
    }

    private void clearEditStudentFields() {
        selectNisEditComboBox.getSelectionModel().clearSelection();
        nisEditField.clear();
        nameEditField.clear();
        genderEditComboBox.getSelectionModel().clearSelection();
        placeOfBirthEditField.clear();
        dateOfBirthEditPicker.setValue(null);
        addressEditArea.clear();
        usernameEditLabel.setText("");
        passwordEditLabel.setText("");
    }

    private void populateEditClassFields(Kelas kelas) {
        // Find the Kelas object in the combo box items by its ID to ensure selection works correctly
        classNameEditComboBox.getSelectionModel().select(kelas);
        homeroomTeacherEditComboBox.getSelectionModel().select(kelas.getNipWaliKelas() != null ? guruDao.getGuruByNip(kelas.getNipWaliKelas()) : null);

        // FIX: Handle potential null for idTahunAjaran when retrieving from model and passing to DAO
        if (kelas.getIdTahunAjaran() != 0) {
            schoolYearEditClassComboBox.getSelectionModel().select(tahunAjaranDao.getTahunAjaranById(kelas.getIdTahunAjaran()));
        } else {
            schoolYearEditClassComboBox.getSelectionModel().clearSelection();
        }
    }

    private void clearCreateClassFields() {
        classNameInputField.clear();
        homeroomTeacherChoiceBox.getSelectionModel().clearSelection();
        schoolYearCreateClassComboBox.getSelectionModel().clearSelection();
    }

    private void clearEditClassFields() {
        classNameEditComboBox.getSelectionModel().clearSelection();
        homeroomTeacherEditComboBox.getSelectionModel().clearSelection();
        schoolYearEditClassComboBox.getSelectionModel().clearSelection();
    }

    private void clearAddScheduleFields() {
        dayChoiceBox.getSelectionModel().clearSelection();
        startTimeField.clear();
        endTimeField.clear();
        subjectChoiceBox.getSelectionModel().clearSelection();
        classChoiceBox.getSelectionModel().clearSelection();
        teacherChoiceBox.getSelectionModel().clearSelection();
        schoolYearScheduleComboBox.getSelectionModel().clearSelection();
    }

    private void clearAddSchoolAgendaFields() {
        agendaContentField.clear();
        agendaStartDatePicker.setValue(null);
        agendaEndDatePicker.setValue(null);
        schoolYearAgendaAddComboBox.getSelectionModel().clearSelection();
    }

    private void clearAddMentorFields() {
        extracurricularInputMentorComboBox.getSelectionModel().clearSelection();
        mentor1ComboBox.getSelectionModel().clearSelection();
        mentor2ComboBox.getSelectionModel().clearSelection();
        mentor3ComboBox.getSelectionModel().clearSelection();
        mentor4ComboBox.getSelectionModel().clearSelection();
        levelInputMentorComboBox.getSelectionModel().clearSelection();
    }

    // NEW: Clear fields for "Add Student to Extracurricular" form
    private void clearAddStudentToExtracurricularFields() {
        classInputSiswaEkskulComboBox.getSelectionModel().clearSelection();
        schoolYearInputSiswaEkskulComboBox.getSelectionModel().clearSelection();
        // semesterInputSiswaEkskulComboBox.getSelectionModel().clearSelection(); // Removed
        extracurricularInputSiswaEkskulComboBox.getSelectionModel().clearSelection();
        levelInputSiswaEkskulComboBox.getSelectionModel().clearSelection();
        studentInputSiswaEkskulComboBox.getSelectionModel().clearSelection();
        studentInputSiswaEkskulComboBox.setItems(FXCollections.observableArrayList()); // Clear filtered students in the student combo box
    }

    // --- Action Event Handlers ---

    @FXML
    private void handleChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Lampiran");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            announcementAttachmentField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handlePublishAnnouncement(ActionEvent event) {
        String judul = announcementTitleField.getText();
        String deskripsi = announcementContentArea.getText();
        String lampiran = announcementAttachmentField.getText();
        LocalDateTime tanggal = LocalDateTime.now(); // Current date and time

        if (judul.isEmpty() || deskripsi.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Judul dan konten pengumuman tidak boleh kosong.");
            return;
        }

        Pengumuman newPengumuman = new Pengumuman(0, judul, deskripsi, tanggal, lampiran); // ID 0 because auto-generated
        if (pengumumanDAO.addPengumuman(newPengumuman)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pengumuman berhasil diterbitkan!");
            loadAnnouncements();
            clearAnnouncementFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menerbitkan pengumuman.");
        }
    }

    @FXML
    private void handleUpdateSelectedAnnouncement(ActionEvent event) {
        Pengumuman selectedPengumuman = announcementTable.getSelectionModel().getSelectedItem();
        if (selectedPengumuman == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih pengumuman yang akan diperbarui.");
            return;
        }

        String judul = announcementTitleField.getText();
        String deskripsi = announcementContentArea.getText();
        String lampiran = announcementAttachmentField.getText();
        LocalDateTime tanggal = LocalDateTime.now(); // Update date to current time if desired

        if (judul.isEmpty() || deskripsi.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Judul dan konten pengumuman tidak boleh kosong.");
            return;
        }

        selectedPengumuman.setJudul(judul);
        selectedPengumuman.setDeskripsi(deskripsi);
        selectedPengumuman.setLampiran(lampiran);
        selectedPengumuman.setTanggal(tanggal); // Update date

        if (pengumumanDAO.updatePengumuman(selectedPengumuman)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pengumuman berhasil diperbarui!");
            loadAnnouncements();
            clearAnnouncementFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui pengumuman.");
        }
    }

    @FXML
    private void handleDeleteSelectedAnnouncement(ActionEvent event) {
        Pengumuman selectedPengumuman = announcementTable.getSelectionModel().getSelectedItem();
        if (selectedPengumuman == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih pengumuman yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus pengumuman ini?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (pengumumanDAO.deletePengumuman(selectedPengumuman.getIdPengumuman())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pengumuman berhasil dihapus!");
                loadAnnouncements();
                clearAnnouncementFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus pengumuman.");
            }
        }
    }

    @FXML
    private void handleAddStudentData(ActionEvent event) {
        String nis = nisInputField.getText();
        String nama = studentNameInputField.getText();
        String jenisKelamin = genderComboBox.getSelectionModel().getSelectedItem();
        String tempatLahir = placeOfBirthInputField.getText();
        LocalDate tanggalLahir = dateOfBirthPicker.getValue();
        String alamat = addressTextArea.getText();

        if (nis.isEmpty() || nama.isEmpty() || jenisKelamin == null || tempatLahir.isEmpty() || tanggalLahir == null || alamat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field data siswa harus diisi.");
            return;
        }

        Siswa newSiswa = new Siswa(nis, nama, jenisKelamin, tempatLahir, tanggalLahir, alamat);
        if (siswaDao.addSiswa(newSiswa)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data siswa berhasil ditambahkan!");
            clearAddStudentFields();
            loadAllStudents(); // Refresh student list

            // NEW: Auto-fill INPUT STUDENT USER fields
            studentUsernameInputField.setText(nis);
            if (tanggalLahir != null) {
                // Format date of birth to DD/MM/YYYY
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                studentPasswordInputField.setText(tanggalLahir.format(formatter));
            } else {
                studentPasswordInputField.clear();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan data siswa. NIS mungkin sudah ada.");
        }
    }

    @FXML
    private void handleAddStudentUser(ActionEvent event) {
        String username = studentUsernameInputField.getText();
        String password = studentPasswordInputField.getText(); // In a real app, hash this!
        int roleId = 3; // Role ID for Siswa (assuming 3 is siswa)

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Username dan password tidak boleh kosong.");
            return;
        }

        // Check if student NIS exists in the database before creating user
        Siswa existingSiswa = siswaDao.getSiswaByNis(username); // Username is considered NIS
        if (existingSiswa == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Siswa dengan NIS tersebut tidak ditemukan.");
            return;
        }
        if (existingSiswa.getIdUser() != null && existingSiswa.getIdUser() != 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Siswa ini sudah memiliki akun pengguna.");
            return;
        }

        int newUserId = userDao.createNewUser(username, password, roleId);
        if (newUserId != -1) {
            // Update student with new id_user
            existingSiswa.setIdUser(newUserId);
            if (siswaDao.updateSiswaUser(existingSiswa)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Akun pengguna siswa berhasil ditambahkan!");
                clearAddStudentUserFields();
                loadAllStudents(); // Refresh student list to show user info
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengaitkan akun pengguna ke siswa.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuat akun pengguna. Username mungkin sudah ada.");
        }
    }

    @FXML
    private void handleUpdateStudentData(ActionEvent event) {
        Siswa selectedSiswa = selectNisEditComboBox.getSelectionModel().getSelectedItem();
        if (selectedSiswa == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih siswa yang akan diperbarui.");
            return;
        }

        String nis = nisEditField.getText(); // NIS should not change
        String nama = nameEditField.getText();
        String jenisKelamin = genderEditComboBox.getSelectionModel().getSelectedItem();
        String tempatLahir = placeOfBirthEditField.getText();
        LocalDate tanggalLahir = dateOfBirthEditPicker.getValue();
        String alamat = addressEditArea.getText();

        if (nama.isEmpty() || jenisKelamin == null || tempatLahir.isEmpty() || tanggalLahir == null || alamat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field data siswa harus diisi.");
            return;
        }

        selectedSiswa.setNama(nama);
        selectedSiswa.setJenisKelamin(jenisKelamin);
        selectedSiswa.setTempatLahir(tempatLahir);
        selectedSiswa.setTanggalLahir(tanggalLahir);
        selectedSiswa.setAlamat(alamat);

        if (siswaDao.updateSiswa(selectedSiswa)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data siswa berhasil diperbarui!");
            loadAllStudents();
            clearEditStudentFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data siswa.");
        }
    }

    @FXML
    private void handleDeleteStudentData(ActionEvent event) {
        Siswa selectedSiswa = selectNisEditComboBox.getSelectionModel().getSelectedItem();
        if (selectedSiswa == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih siswa yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus data siswa ini?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (siswaDao.deleteSiswa(selectedSiswa.getNis())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data siswa berhasil dihapus!");
                loadAllStudents();
                clearEditStudentFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data siswa.");
            }
        }
    }

    @FXML
    private void handleSearchStudent(ActionEvent event) {
        String keyword = searchStudentField.getText();
        if (keyword.isEmpty()) {
            loadAllStudents();
        } else {
            studentList.setAll(siswaDao.searchSiswa(keyword));
        }
    }

    @FXML
    private void handleSearchTeacher(ActionEvent event) {
        String keyword = searchTeacherField.getText();
        if (keyword.isEmpty()) {
            loadAllTeachers();
        } else {
            teacherList.setAll(guruDao.searchGuru(keyword));
        }
    }

    @FXML
    private void handleDeleteSelectedTeacher(ActionEvent event) {
        Guru selectedGuru = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedGuru == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih guru yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus data guru ini? Ini juga akan menghapus akun pengguna terkait.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (guruDao.deleteGuru(selectedGuru.getNip())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data guru berhasil dihapus!");
                loadAllTeachers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data guru.");
            }
        }
    }

    // NEW: Handle Delete Schedule
    @FXML
    private void handleDeleteSchedule(ActionEvent event) {
        JadwalKelas selectedJadwal = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedJadwal == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih jadwal yang akan dihapus dari tabel.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus jadwal ini?\n" +
                "Hari: " + selectedJadwal.getHari() + "\n" +
                "Waktu: " + selectedJadwal.getTimeRange() + "\n" +
                "Mata Pelajaran: " + selectedJadwal.getNamaMapel() + "\n" +
                "Kelas: " + selectedJadwal.getKelasDanTahunAjaran());
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (jadwalKelasDao.deleteJadwalKelas(selectedJadwal.getIdJadwalKelas())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil dihapus!");
                loadSchedules(); // Refresh schedule table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus jadwal.");
            }
        }
    }

    @FXML
    private void handleAddSchedule(ActionEvent event) {
        String hari = dayChoiceBox.getSelectionModel().getSelectedItem();
        String startTimeStr = startTimeField.getText();
        String endTimeStr = endTimeField.getText();
        MataPelajaran selectedMapel = subjectChoiceBox.getSelectionModel().getSelectedItem();
        Kelas selectedKelas = classChoiceBox.getSelectionModel().getSelectedItem();
        Guru selectedGuru = teacherChoiceBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearScheduleComboBox.getSelectionModel().getSelectedItem();

        if (hari == null || startTimeStr.isEmpty() || endTimeStr.isEmpty() || selectedMapel == null ||
                selectedKelas == null || selectedGuru == null || selectedTahunAjaran == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field jadwal harus diisi.");
            return;
        }

        try {
            LocalTime jamMulai = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime jamSelesai = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));

            // Check if end time is after start time
            if (jamSelesai.isBefore(jamMulai) || jamSelesai.equals(jamMulai)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Jam selesai harus setelah jam mulai.");
                return;
            }

            // Updated JadwalKelas constructor call (removed semester)
            JadwalKelas newJadwal = new JadwalKelas(hari, jamMulai, jamSelesai, selectedKelas.getIdKelas(), selectedMapel.getIdMapel(), selectedGuru.getNip(), selectedTahunAjaran.getIdTahunAjaran());

            if (jadwalKelasDao.addJadwalKelas(newJadwal)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil ditambahkan!");
                clearAddScheduleFields();
                loadSchedules(); // Refresh schedule table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan jadwal.");
            }

        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Format Waktu Salah", "Format waktu harus HH:MM (contoh: 08:00).");
        }
    }

    @FXML
    private void handleCreateClass(ActionEvent event) {
        String namaKelas = classNameInputField.getText();
        Guru selectedWaliKelas = homeroomTeacherChoiceBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearCreateClassComboBox.getSelectionModel().getSelectedItem();

        if (namaKelas.isEmpty() || selectedWaliKelas == null || selectedTahunAjaran == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field kelas baru harus diisi.");
            return;
        }

        String tingkat = ""; // You might want to derive tingkat from namaKelas, e.g., "1A" -> "1"
        if (namaKelas.matches("^\\d+[A-Za-z]?$")) {
            tingkat = namaKelas.replaceAll("[^\\d]", "");
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Format nama kelas tidak valid. Harap masukkan angka diikuti opsional huruf (misal: 2A, 5B).");
            return;
        }


        // Kelas model does not have semester directly
        Kelas newKelas = new Kelas(namaKelas, tingkat, selectedWaliKelas.getNip(), selectedTahunAjaran.getIdTahunAjaran()); // "General" for tingkat, adjust if needed
        if (kelasDao.addKelas(newKelas)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kelas berhasil dibuat!");
            clearCreateClassFields();
            loadKelasIntoComboBoxes(); // Refresh class comboboxes
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuat kelas. Nama kelas mungkin sudah ada untuk tahun ajaran ini.");
        }
    }

    @FXML
    private void handleUpdateClass(ActionEvent event) {
        Kelas selectedKelas = classNameEditComboBox.getSelectionModel().getSelectedItem();
        if (selectedKelas == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih kelas yang akan diperbarui.");
            return;
        }

        Guru selectedWaliKelas = homeroomTeacherEditComboBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearEditClassComboBox.getSelectionModel().getSelectedItem();

        if (selectedWaliKelas == null || selectedTahunAjaran == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Wali kelas dan tahun ajaran harus diisi.");
            return;
        }

        selectedKelas.setNipWaliKelas(selectedWaliKelas.getNip());
        selectedKelas.setIdTahunAjaran(selectedTahunAjaran.getIdTahunAjaran());

        if (kelasDao.updateKelas(selectedKelas)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kelas berhasil diperbarui!");
            clearEditClassFields();
            loadKelasIntoComboBoxes();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui kelas.");
        }
    }

    @FXML
    private void handleDeleteClass(ActionEvent event) {
        Kelas selectedKelas = classNameEditComboBox.getSelectionModel().getSelectedItem();
        if (selectedKelas == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih kelas yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus kelas ini? Ini akan memutuskan kaitan siswa yang ditugaskan ke kelas ini.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (kelasDao.deleteKelas(selectedKelas.getIdKelas())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kelas berhasil dihapus!");
                clearEditClassFields();
                loadKelasIntoComboBoxes();
                loadAllStudents(); // Refresh student list because their class might be null now
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus kelas.");
            }
        }
    }

    @FXML
    private void handleSearchStudentToAssign() {
        String keyword = searchStudentAssignClassField.getText();
        if (keyword.isEmpty()) {
            studentsToAssignTable.setItems(studentList); // Show all students if search is empty
        } else {
            ObservableList<Siswa> filteredStudents = FXCollections.observableArrayList();
            for (Siswa siswa : studentList) {
                if (siswa.getNis().toLowerCase().contains(keyword.toLowerCase()) ||
                        siswa.getNama().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredStudents.add(siswa);
                }
            }
            studentsToAssignTable.setItems(filteredStudents);
        }
    }


    @FXML
    private void handleAssignStudent(ActionEvent event) {
        Siswa selectedStudent = studentsToAssignTable.getSelectionModel().getSelectedItem();
        Kelas selectedClass = classAssignComboBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearAssignComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent == null || selectedClass == null || selectedTahunAjaran == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Pilih siswa, kelas, dan tahun ajaran.");
            return;
        }

        // Check if student is already in the same class for the same academic year
        Siswa studentInDb = siswaDao.getSiswaByNis(selectedStudent.getNis());
        if (studentInDb != null && studentInDb.getIdKelas() != null &&
                studentInDb.getIdKelas().equals(selectedClass.getIdKelas()) &&
                studentInDb.getIdTahunAjaran() != null && studentInDb.getIdTahunAjaran().equals(selectedTahunAjaran.getIdTahunAjaran())) {
            showAlert(Alert.AlertType.WARNING, "Duplikat Data", "Siswa ini sudah terdaftar di kelas yang sama untuk tahun ajaran yang dipilih.");
            return;
        }


        if (siswaDao.assignStudentToClass(selectedStudent.getNis(), selectedClass.getIdKelas(), selectedTahunAjaran.getIdTahunAjaran())) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Siswa berhasil ditugaskan ke kelas!");
            loadAllStudents(); // Refresh all student data
            loadStudentsInSelectedClass(); // Refresh students in selected class if any is chosen
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menugaskan siswa ke kelas.");
        }
    }

    @FXML
    private void handleEditSelectedStudentInClass(ActionEvent event) {
        Siswa selectedStudent = studentsInSelectedClassTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih siswa yang akan diedit.");
            return;
        }
        // This is a placeholder for actual edit functionality.
        // You would typically open the "Entry Student Data" tab and populate its "Edit Student Data" fields.
        //showAlert(Alert.AlertType.INFORMATION, "Fitur Belum Tersedia", "Fungsi edit siswa dari tabel ini belum diimplementasikan. Silakan gunakan tab 'Entry Student Data' untuk mengedit siswa.");

        // Optional: Programmatically switch tab and select the student in the edit combo box
        mainTabPane.getSelectionModel().select(entryStudentDataTab);
        selectNisEditComboBox.getSelectionModel().select(selectedStudent);
    }

    @FXML
    private void handleRemoveStudentFromClass(ActionEvent event) {
        Siswa selectedStudent = studentsInSelectedClassTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih siswa yang akan dihapus dari kelas.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus siswa ini dari kelas?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (siswaDao.removeClassInfoFromStudent(selectedStudent.getNis())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Siswa berhasil dihapus dari kelas!");
                loadAllStudents(); // Refresh all student data
                loadStudentsInSelectedClass(); // Refresh current class view
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus siswa dari kelas.");
            }
        }
    }


    @FXML
    private void handleAddSchoolAgenda(ActionEvent event) {
        String judul = agendaContentField.getText();
        LocalDate tanggalMulai = agendaStartDatePicker.getValue();
        LocalDate tanggalSelesai = agendaEndDatePicker.getValue();
        TahunAjaran selectedTahunAjaran = schoolYearAgendaAddComboBox.getSelectionModel().getSelectedItem();

        if (judul.isEmpty() || tanggalMulai == null || tanggalSelesai == null || selectedTahunAjaran == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field agenda sekolah harus diisi.");
            return;
        }
        if (tanggalSelesai.isBefore(tanggalMulai)) {
            showAlert(Alert.AlertType.ERROR, "Tanggal Salah", "Tanggal selesai harus setelah tanggal mulai.");
            return;
        }

        // Removed semester from AgendaSekolah constructor
        AgendaSekolah newAgenda = new AgendaSekolah(judul, "", tanggalMulai, tanggalSelesai, selectedTahunAjaran.getIdTahunAjaran()); // deskripsi can be empty or taken from title
        if (agendaSekolahDao.addAgendaSekolah(newAgenda)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Agenda sekolah berhasil ditambahkan!");
            clearAddSchoolAgendaFields();
            loadSchoolAgenda(); // Reload agenda table
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan agenda sekolah.");
        }
    }

    @FXML
    private void handleDeleteSchoolAgenda(ActionEvent event) {
        AgendaSekolah selectedAgenda = agendaTable.getSelectionModel().getSelectedItem();
        if (selectedAgenda == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih agenda yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus agenda ini?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (agendaSekolahDao.deleteAgendaSekolah(selectedAgenda.getIdAgendaSekolah())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Agenda sekolah berhasil dihapus!");
                loadSchoolAgenda(); // Reload agenda table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus agenda sekolah.");
            }
        }
    }

    @FXML
    private void handleAddMentor(ActionEvent event) {
        Ekstrakurikuler selectedEkskul = extracurricularInputMentorComboBox.getSelectionModel().getSelectedItem();
        Guru mentor1 = mentor1ComboBox.getSelectionModel().getSelectedItem();
        Guru mentor2 = mentor2ComboBox.getSelectionModel().getSelectedItem();
        Guru mentor3 = mentor3ComboBox.getSelectionModel().getSelectedItem();
        Guru mentor4 = mentor4ComboBox.getSelectionModel().getSelectedItem();

        if (selectedEkskul == null || (mentor1 == null && mentor2 == null && mentor3 == null && mentor4 == null)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Pilih ekstrakurikuler dan setidaknya satu mentor.");
            return;
        }

        boolean success = true;
        // Collect all selected mentors
        List<Guru> selectedMentors = new java.util.ArrayList<>();
        if (mentor1 != null) selectedMentors.add(mentor1);
        if (mentor2 != null && !selectedMentors.contains(mentor2)) selectedMentors.add(mentor2);
        if (mentor3 != null && !selectedMentors.contains(mentor3)) selectedMentors.add(mentor3);
        if (mentor4 != null && !selectedMentors.contains(mentor4)) selectedMentors.add(mentor4);

        for (Guru mentor : selectedMentors) {
            Pembina newPembina = new Pembina(mentor.getNip(), selectedEkskul.getIdEkstrakurikuler());
            if (!pembinaDao.addPembina(newPembina)) {
                success = false;
                System.err.println("Gagal menambahkan pembina " + mentor.getNama() + " untuk " + selectedEkskul.getNama());
            }
        }

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Mentor berhasil ditambahkan ke ekstrakurikuler!");
            clearAddMentorFields();
            loadEkstrakurikulerIntoComboBoxes(); // Refresh extracurricular table
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Beberapa mentor mungkin gagal ditambahkan atau sudah menjadi pembina untuk ekskul ini.");
        }
    }

    @FXML
    private void handleUpdateMentor(ActionEvent event) {
        Ekstrakurikuler selectedEkskul = extracurricularInputMentorComboBox.getSelectionModel().getSelectedItem();
        if (selectedEkskul == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih ekstrakurikuler yang mentornya akan diperbarui.");
            return;
        }

        List<Guru> newSelectedMentors = new java.util.ArrayList<>();
        if (mentor1ComboBox.getSelectionModel().getSelectedItem() != null) newSelectedMentors.add(mentor1ComboBox.getSelectionModel().getSelectedItem());
        if (mentor2ComboBox.getSelectionModel().getSelectedItem() != null && !newSelectedMentors.contains(mentor2ComboBox.getSelectionModel().getSelectedItem())) newSelectedMentors.add(mentor2ComboBox.getSelectionModel().getSelectedItem());
        if (mentor3ComboBox.getSelectionModel().getSelectedItem() != null && !newSelectedMentors.contains(mentor3ComboBox.getSelectionModel().getSelectedItem())) newSelectedMentors.add(mentor3ComboBox.getSelectionModel().getSelectedItem());
        if (mentor4ComboBox.getSelectionModel().getSelectedItem() != null && !newSelectedMentors.contains(mentor4ComboBox.getSelectionModel().getSelectedItem())) newSelectedMentors.add(mentor4ComboBox.getSelectionModel().getSelectedItem());

        // Konfirmasi sebelum melakukan update
        Optional<ButtonType> result = showConfirmation("Konfirmasi Update Mentor", "Anda yakin ingin memperbarui mentor untuk ekstrakurikuler " + selectedEkskul.getNama() + "?\n\nMentor baru: " + newSelectedMentors.stream().map(Guru::getNama).collect(Collectors.joining(", ")));
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = true;
            // 1. Hapus semua pembina yang ada untuk ekstrakurikuler ini
            // PENTING: Anda perlu mengimplementasikan metode ini di PembinaDAO
            // Contoh: public boolean deletePembinaByEkstrakurikulerId(int idEkstrakurikuler)
            if (!pembinaDao.deletePembinaByEkstrakurikulerId(selectedEkskul.getIdEkstrakurikuler())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus mentor lama.");
                return; // Berhenti jika gagal menghapus
            }

            // 2. Tambahkan mentor yang baru dipilih
            for (Guru mentor : newSelectedMentors) {
                //Pembina newPembina = new Pembina(0, mentor.getNip(), mentor.getNama(), selectedEkskul.getIdEkstrakurikuler(), selectedEkskul.getNama());
                Pembina newPembina = new Pembina(mentor.getNip(), selectedEkskul.getIdEkstrakurikuler());
                if (!pembinaDao.addPembina(newPembina)) {
                    success = false;
                    System.err.println("Gagal menambahkan pembina " + mentor.getNama() + " untuk " + selectedEkskul.getNama());
                }
            }

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Mentor ekstrakurikuler berhasil diperbarui!");
                clearAddMentorFields();
                loadEkstrakurikulerIntoComboBoxes(); // Refresh tabel ekstrakurikuler
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Beberapa mentor mungkin gagal ditambahkan.");
            }
        }
    }

    @FXML
    private void handleDeleteExtracurricular(ActionEvent event) {
        Ekstrakurikuler selectedEkskul = extracurricularTable.getSelectionModel().getSelectedItem();
        if (selectedEkskul == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih ekstrakurikuler yang akan dihapus.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus ekstrakurikuler ini dan semua pembina serta pesertanya?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (ekstrakurikulerDao.deleteEkstrakurikuler(selectedEkskul.getIdEkstrakurikuler())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Ekstrakurikuler berhasil dihapus!");
                loadEkstrakurikulerIntoComboBoxes(); // Refresh extracurricular table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus ekstrakurikuler.");
            }
        }
    }


    @FXML
    private void handleAddStudentToExtracurricular(ActionEvent event) {
        Siswa selectedSiswa = studentInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();
        Ekstrakurikuler selectedEkskul = extracurricularInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();
        Kelas selectedKelas = classInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();
        TahunAjaran selectedTahunAjaran = schoolYearInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();


        if (selectedSiswa == null || selectedEkskul == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Pilih siswa dan ekstrakurikuler.");
            return;
        }

        PesertaEkskul newPeserta = new PesertaEkskul(selectedSiswa.getNis(), selectedEkskul.getIdEkstrakurikuler()); // ID 0 for auto-generated
        if (pesertaEkskulDao.addPesertaEkskul(newPeserta)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Siswa berhasil ditambahkan ke ekstrakurikuler!");
            clearAddStudentToExtracurricularFields(); // Clear inputs
            loadStudentsInExtracurricularTable(); // Refresh table for the selected extracurricular
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan siswa ke ekstrakurikuler. Siswa mungkin sudah terdaftar.");
        }
    }

    @FXML
    private void handleRemoveStudentFromExtracurricular(ActionEvent event) {
        Siswa selectedStudent = studentsInExtracurricularTable.getSelectionModel().getSelectedItem();
        Ekstrakurikuler selectedEkskul = extracurricularInputSiswaEkskulComboBox.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih siswa dari tabel yang akan dihapus dari ekstrakurikuler.");
            return;
        }
        if (selectedEkskul == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Ekstrakurikuler belum dipilih.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Konfirmasi Hapus", "Anda yakin ingin menghapus siswa '" + selectedStudent.getNama() + "' dari ekstrakurikuler '" + selectedEkskul.getNama() + "'?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Call DAO to delete extracurricular participant
            if (pesertaEkskulDao.deletePesertaEkskulByNisAndEkskulId(selectedStudent.getNis(), selectedEkskul.getIdEkstrakurikuler())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Siswa berhasil dihapus dari ekstrakurikuler!");
                loadStudentsInExtracurricularTable(); // Refresh the table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus siswa dari ekstrakurikuler.");
            }
        }
    }


    // --- Generic Alert Helper ---
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
