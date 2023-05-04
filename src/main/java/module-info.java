module com.example.proyec {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires java.sql;


    opens com.example.proyec to javafx.fxml;
    exports com.example.proyec;
}