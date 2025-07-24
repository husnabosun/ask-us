module com.example.karakamyonculars {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.karakamyonculars to javafx.fxml;
    exports com.example.karakamyonculars;
}