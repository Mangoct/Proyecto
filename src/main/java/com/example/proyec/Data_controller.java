package com.example.proyec;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Data_controller {

    @FXML
    private Button btn_Añadir;

    @FXML
    private Button btn_Eliminar;

    @FXML
    private TableColumn<?, ?> col_Bajas;

    @FXML
    private TableColumn<?, ?> col_Dia;

    @FXML
    private TableColumn<?, ?> col_Entrada;

    @FXML
    private TableColumn<?, ?> col_Salida;

    @FXML
    private TableColumn<?, ?> col_Total;

    @FXML
    private TableView<?> tabla_DatosNave;

    @FXML
    void handler_AñadirDato(ActionEvent event) {

    }

    @FXML
    void handler_BorrarDato(ActionEvent event) {

    }

}
