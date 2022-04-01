module com.example.ijaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens ija.ijaproject to javafx.fxml;
    exports ija.ijaproject;
}