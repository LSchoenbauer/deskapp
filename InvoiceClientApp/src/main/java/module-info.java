module com.example.invoiceclientapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.invoiceclientapp to javafx.fxml;
    exports com.example.invoiceclientapp;
    exports com.example.invoiceclientapp.controller;
    opens com.example.invoiceclientapp.controller to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.invoiceclientapp.model to com.fasterxml.jackson.databind;
}