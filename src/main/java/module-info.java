module com.example.javafx_school_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.javafx_school_management_system to javafx.fxml;
    exports com.example.javafx_school_management_system;
    exports Model;
    opens Model to javafx.fxml;
}