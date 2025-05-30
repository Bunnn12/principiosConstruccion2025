/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.dao.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLPrincipalController implements Initializable {

    private Usuario usuarioSesion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUsuario;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion= usuarioSesion;
        cargarInformacionUsuario();
    }
    private void cargarInformacionUsuario(){
        if(usuarioSesion != null){
            lbNombre.setText(usuarioSesion.toString());
            lbUsuario.setText(usuarioSesion.getUsername());
        }
    }

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        try{
            Stage escenarioBase= (Stage) lbNombre.getScene().getWindow();
            FXMLLoader cargador= new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vista= cargador.load();
            Scene escenaInicio = new Scene(vista);
            escenarioBase.setScene(escenaInicio);
            escenarioBase.setTitle("InicioSesion");
            escenarioBase.centerOnScreen();
        }catch(IOException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    @FXML
    private void clickBtnAdminAlumno(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLAdminAlumno.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Administrador de alumnos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }
}
