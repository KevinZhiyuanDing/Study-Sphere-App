module cpen.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.json;

    opens cpen.app to javafx.fxml;
    exports cpen.app;
}