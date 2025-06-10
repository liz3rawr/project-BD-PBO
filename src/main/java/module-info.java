module com.example.projectdouble {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.projectdouble.controller to javafx.fxml;
    opens com.example.projectdouble.model      to javafx.fxml;

    opens com.example.projectdouble to javafx.fxml;
    exports com.example.projectdouble;
}