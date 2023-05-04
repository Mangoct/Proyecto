package com.example.proyec;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Contract_controller1 {

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

    public void inicializarDatos1() {
        // Crear una consulta SQL para obtener los registros de la nave 1
        String sql = "SELECT * FROM contratos WHERE nave = 1";

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
        // Crear un cuadro de diálogo para ingresar el número del contrato
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Añadir contrato");
        dialog.setHeaderText(null);
        dialog.setContentText("Por favor ingrese el número del nuevo contrato:");

        // Obtener el resultado del cuadro de diálogo
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String numeroContrato = result.get();

            // Validar que el número del contrato no esté vacío y no exista ya en la lista
            if (!numeroContrato.isEmpty() && contratos.stream().noneMatch(c -> String.valueOf(c.getNumeroContrato()).equals(numeroContrato))) {
                try {
                    // Crear un cuadro de diálogo para elegir el cliente asociado al contrato

                    ChoiceDialog<String> clienteDialog = new ChoiceDialog<>("", new Clientes().getClientes().stream().map(Cliente::getNombre).collect(Collectors.toList()));
                    clienteDialog.setTitle("Añadir contrato");
                    clienteDialog.setHeaderText(null);
                    clienteDialog.setContentText("Por favor elija al cliente asociado al contrato:");

                    // Obtener el resultado del cuadro de diálogo de clientes
                    Optional<String> clienteResult = clienteDialog.showAndWait();
                    if (clienteResult.isPresent()) {
                        String nombreCliente = clienteResult.get();

                        // Insertar el nuevo contrato en la base de datos
                        String sqlInsertContrato = "INSERT INTO contratos (numero_contrato, nave, nombre_cliente) VALUES (?, ?, ?)";
                        PreparedStatement psInsertContrato = Main.conn.prepareStatement(sqlInsertContrato, Statement.RETURN_GENERATED_KEYS);
                        psInsertContrato.setString(1, numeroContrato);
                        psInsertContrato.setInt(2, 1);
                        psInsertContrato.setString(3, nombreCliente);
                        psInsertContrato.executeUpdate();

                        // Almacenar el número del contrato que se acaba de insertar
                        int nuevoContratoId;
                        try (ResultSet generatedKeys = psInsertContrato.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                nuevoContratoId = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Creating user failed, no ID obtained.");
                            }
                        }
                        // Agregar el nuevo contrato a la lista
                        Contrato contrato = new Contrato(Integer.parseInt(numeroContrato));
                        contratos.add(contrato);
                        tabla_Contratos.refresh();

                        // Insertar un registro diario asociado al nuevo contrato
                        String sqlInsertRegistro = "INSERT INTO registros_diarios (contrato_id, fecha, entrada, salida, defunciones, nave, animales_restantes) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement psInsertRegistro = Main.conn.prepareStatement(sqlInsertRegistro);
                        psInsertRegistro.setInt(1, nuevoContratoId);
                        psInsertRegistro.setDate(2, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
                        psInsertRegistro.setInt(3, 0);
                        psInsertRegistro.setInt(4, 0);
                        psInsertRegistro.setInt(5, 0);
                        psInsertRegistro.setInt(6, 1);
                        psInsertRegistro.setInt(7, 0);
                        psInsertRegistro.executeUpdate();
                        cargarRegistrosDiarios();
                    } else {
                        // Mostrar un mensaje de error si no se eligió ningún cliente
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Debe elegir un cliente para asociar al contrato.");
                        alert.showAndWait();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Mostrar un mensaje de error si el número del contrato está vacío o ya existe
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("El número del contrato no puede estar vacío o ya existir en la lista.");
                alert.showAndWait();
            }
        }
    }

    private void cargarRegistrosDiarios() {
        contratos.clear();
        // Crear una consulta SQL para obtener los registros de la nave 1
        String sql = "SELECT * FROM contratos WHERE nave = 1";

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
