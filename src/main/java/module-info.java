module com.tempims.tempims {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.tempims.tempims to javafx.fxml;
    exports com.tempims.tempims;
}