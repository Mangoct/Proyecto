package com.example.proyec;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contract_controller2 {

    @FXML
    private Button btn_Añadir;

    @FXML
    private Button btn_Borrar;

    @FXML
    private Button btn_EnviarPDF;

    @FXML
    private Button btn_GenerarPDF;

    @FXML
    private Button btn_volver;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_nContrato;
    @FXML
    private TableView<Contrato> tabla_Contratos;

    public ObservableList<Contrato> contratos = FXCollections.observableArrayList();

    public void inicializarDatos2() {
        // Crear una consulta SQL para obtener los registros de la nave 1
        String sql = "SELECT * FROM contratos WHERE nave = 2";

        try {
            Statement stmt = Main.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Contrato contrato = new Contrato(rs.getInt("numero_contrato"));
                contratos.add(contrato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_nContrato.setCellValueFactory(new PropertyValueFactory<>("numeroContrato"));

        tabla_Contratos.setItems(contratos);
    }

    @FXML
    void handler_AñadirContrato(ActionEvent event) {

    }

    @FXML
    void handler_BorrarContrato(ActionEvent event) {
        // Obtener el contrato seleccionado en la tabla
        Contrato contrato = tabla_Contratos.getSelectionModel().getSelectedItem();
        if (contrato != null) {
            // Preguntar al usuario si realmente quiere borrar el contrato
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro que desea borrar el contrato " + contrato.getNumeroContrato() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                try {
                    Main.conn.setAutoCommit(false);
                    // Obtener el contrato_id correspondiente al contrato seleccionado
                    String sqlSelectContratoID = "SELECT id FROM contratos WHERE numero_contrato = ?";
                    PreparedStatement psSelectContratoID = Main.conn.prepareStatement(sqlSelectContratoID);
                    psSelectContratoID.setInt(1, contrato.getNumeroContrato());
                    ResultSet rsContratoID = psSelectContratoID.executeQuery();

                    if (rsContratoID.next()) {
                        int contrato_id = rsContratoID.getInt("id");
                        String sqlDeleteRegistros = "DELETE FROM registros_diarios WHERE contrato_id = ?";
                        PreparedStatement psDeleteRegistros = Main.conn.prepareStatement(sqlDeleteRegistros);
                        psDeleteRegistros.setInt(1, contrato_id);
                        psDeleteRegistros.executeUpdate();


                        String sqlDeleteContrato = "DELETE FROM contratos WHERE id = ?";
                        PreparedStatement psDeleteContrato = Main.conn.prepareStatement(sqlDeleteContrato);
                        psDeleteContrato.setInt(1, contrato_id);
                        psDeleteContrato.executeUpdate();
                        Main.conn.commit();

                        contratos.remove(contrato);
                        tabla_Contratos.refresh();
                        Main.conn.setAutoCommit(true);
                    } else {
                        // Manejar el caso en el que no se encuentran filas
                    }
                    rsContratoID.close();
                    psSelectContratoID.close();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void handler_EnviarPDF(ActionEvent event) {

    }

    @FXML
    void handler_GenerarPDF(ActionEvent event) {

    }

    public void handler_Volver(ActionEvent event) {
        Stage stage = (Stage) btn_volver.getScene().getWindow();
        stage.close();
    }
}
