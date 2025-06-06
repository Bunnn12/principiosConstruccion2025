/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.utilidades;

import java.awt.Component;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author reino
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido){
         Alert alerta = new Alert(tipo);
               alerta.setTitle(titulo);
               alerta.setHeaderText(null);
               alerta.setContentText(contenido);
               alerta.showAndWait();
    }
    public static Stage obtenerEscenarioComponente(Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion= new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion= alertaConfirmacion.showAndWait();
        return seleccion.get() == ButtonType.OK;
        //return (alertaConfirmacion.showAndWait().get())== ButtonType.ok SE PUEDE HACER EN UNA SOLA LINEA
    }
}
