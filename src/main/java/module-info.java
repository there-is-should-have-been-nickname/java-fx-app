module com.example.testfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;


    opens com.example.testfx to javafx.fxml;
    exports com.example.testfx;
}