module ija.ijaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.xml;
    requires json.simple;

    opens ija.ijaproject to javafx.fxml;
    exports ija.ijaproject;
}