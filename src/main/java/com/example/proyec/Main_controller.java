package com.example.proyec;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class Main_controller implements Initializable{

    @FXML
    private Button btn_Nave1;

    @FXML
    private Button btn_Nave2;

    @FXML
    private Button btn_Nave3;

    @FXML
    private Button btn_Naves;

    @FXML
    private Button btn_salir;

    @FXML
    private VBox cajaBtn;
    @FXML
    private Button btn_actualizar;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Bajas;

    @FXML
    private TableColumn<RegistroDiario, String> col_Cliente;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Contrato;

    @FXML
    private TableColumn<RegistroDiario, String> col_Dia;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Entrada;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Nave;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Salida;

    @FXML
    private TableColumn<RegistroDiario, Integer> col_Total;

    @FXML
    public TableView<RegistroDiario> tabla_Generica_Animales;
    public ObservableList<RegistroDiario> registros = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cajaBtn.setVisible(!cajaBtn.isVisible());
        cajaBtn.setManaged(!cajaBtn.isManaged());
            // Crear una consulta SQL para obtener los registros
            String sql = "SELECT rd.*, contratos.numero_contrato, contratos.nombre_cliente, \n" +
                    "   rd.entrada + IFNULL((\n" +
                    "       SELECT SUM(rd2.salida - rd2.defunciones)\n" +
                    "       FROM registros_diarios rd2\n" +
                    "       WHERE rd2.contrato_id = rd.contrato_id\n" +
                    "           AND rd2.nave = rd.nave\n" +
                    "           AND rd2.fecha < rd.fecha\n" +
                    "       GROUP BY rd2.contrato_id, rd2.nave\n" +
                    "   ), 0) AS total_animales\n" +
                    "FROM registros_diarios rd \n" +
                    "JOIN contratos ON rd.contrato_id = contratos.id\n" +
                    "WHERE (rd.nave, rd.fecha) IN (\n" +
                    "   SELECT nave, MAX(fecha) AS ultima_fecha\n" +
                    "   FROM registros_diarios\n" +
                    "   GROUP BY nave\n" +
                    ")\n" +
                    "LIMIT 0, 1000;";


            try {
                Statement stmt = Main.conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    RegistroDiario registro = new RegistroDiario(
                            rs.getInt("numero_contrato"),
                            rs.getDate("fecha"),
                            rs.getInt("entrada"),
                            rs.getInt("salida"),
                            rs.getInt("defunciones"),
                            rs.getInt("nave"),
                            rs.getString("nombre_Cliente"),
                            rs.getInt("total_animales"));
                    registros.add(registro);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            col_Dia.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            col_Contrato.setCellValueFactory(new PropertyValueFactory<>("contrato_id"));
            col_Cliente.setCellValueFactory(new PropertyValueFactory<>("nombre_cliente"));
            col_Nave.setCellValueFactory(new PropertyValueFactory<>("nave"));
            col_Entrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
            col_Salida.setCellValueFactory(new PropertyValueFactory<>("salida"));
            col_Bajas.setCellValueFactory(new PropertyValueFactory<>("defunciones"));
            col_Total.setCellValueFactory(new PropertyValueFactory<>("total"));
            tabla_Generica_Animales.setItems(registros);
        }

    @FXML
    void handler_Nave3(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Contract_view_3.fxml"));
            Parent root = loader.load();
            Contract_controller3 controlador = loader.getController();
            controlador.inicializarDatos3();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void handler_nave2(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Contract_view_2.fxml"));
            Parent root = loader.load();
            Contract_controller2 controlador = loader.getController();
            controlador.inicializarDatos2();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void handler_Nave1(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Contract_view_1.fxml"));
            Parent root = loader.load();
            Contract_controller1 controlador = loader.getController();
            controlador.inicializarDatos1();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handler_desplegar(ActionEvent event) {
        cajaBtn.setVisible(!cajaBtn.isVisible());
        cajaBtn.setManaged(!cajaBtn.isManaged());
    }

    public void Handler_salir(ActionEvent event) {
        System.exit(0);
    }


    public void Handler_actualizar(ActionEvent actionEvent) {
        registros.clear();
        String sql = "SELECT rd.*, contratos.numero_contrato, contratos.nombre_cliente, \n" +
                "   rd.entrada + IFNULL((\n" +
                "       SELECT SUM(rd2.salida - rd2.defunciones)\n" +
                "       FROM registros_diarios rd2\n" +
                "       WHERE rd2.contrato_id = rd.contrato_id\n" +
                "           AND rd2.nave = rd.nave\n" +
                "           AND rd2.fecha < rd.fecha\n" +
                "       GROUP BY rd2.contrato_id, rd2.nave\n" +
                "   ), 0) AS total_animales\n" +
                "FROM registros_diarios rd \n" +
                "JOIN contratos ON rd.contrato_id = contratos.id\n" +
                "WHERE (rd.nave, rd.fecha) IN (\n" +
                "   SELECT nave, MAX(fecha) AS ultima_fecha\n" +
                "   FROM registros_diarios\n" +
                "   GROUP BY nave\n" +
                ")\n" +
                "LIMIT 0, 1000;";


        try {
            Statement stmt = Main.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                RegistroDiario registro = new RegistroDiario(
                        rs.getInt("numero_contrato"),
                        rs.getDate("fecha"),
                        rs.getInt("entrada"),
                        rs.getInt("salida"),
                        rs.getInt("defunciones"),
                        rs.getInt("nave"),
                        rs.getString("nombre_Cliente"),
                        rs.getInt("total_animales"));
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        col_Dia.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_Contrato.setCellValueFactory(new PropertyValueFactory<>("contrato_id"));
        col_Cliente.setCellValueFactory(new PropertyValueFactory<>("nombre_cliente"));
        col_Nave.setCellValueFactory(new PropertyValueFactory<>("nave"));
        col_Entrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
        col_Salida.setCellValueFactory(new PropertyValueFactory<>("salida"));
        col_Bajas.setCellValueFactory(new PropertyValueFactory<>("defunciones"));
        col_Total.setCellValueFactory(new PropertyValueFactory<>("total"));
        tabla_Generica_Animales.setItems(registros);

    }
}


