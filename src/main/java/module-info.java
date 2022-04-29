module ija.ijaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.xml;
    requires json.simple;

    exports ija.ijaproject.cls to com.google.gson;

    opens ija.ijaproject to javafx.fxml;

    exports ija.ijaproject;
}