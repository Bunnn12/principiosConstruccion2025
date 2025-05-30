/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.inicioSesionDAO;
import javafxappescolar.modelo.dao.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }    

    @FXML
    private void btnClickVerificarSesion(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        
        if (validarCampos(username, password)){
            validarCredenciales(username, password);
        }
    }
    
    private boolean validarCampos(String username, String password){
        lbErrorUsuario.setText(""); //limpian los campos 
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if(username.isEmpty()){
            lbErrorUsuario.setText("Usuario obligatorio");
            camposValidos= false;
        }
        if(password.isEmpty()){
           lbErrorPassword.setText("Contraseña obligatoria");
           camposValidos= false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales(String username, String password){
        try{
        Usuario usuarioSesion = inicioSesionDAO.verificarCredenciales(username, password);
        if (usuarioSesion != null){
          Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido(a) "+usuarioSesion.toString()+ " al sistema.");
            irPantallaPrincipal(usuarioSesion);
        }
            else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectos", "Usuario y/o contraseñas incorrectas, por favor verifica tu información");
            }
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexión", e.getMessage());
        }
    }
    
    private void irPantallaPrincipal(Usuario usuarioSesion){
        try{
        Stage escenarioBase = (Stage) tfPassword.getScene().getWindow(); //se usa el casting
        //Parent vista= FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
        FXMLLoader cargador= new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
        Parent vista= cargador.load();
        FXMLPrincipalController controlador= cargador.getController();
        controlador.inicializarInformacion(usuarioSesion);
        Scene escenaPrincipal= new Scene(vista);
        escenarioBase.setScene(escenaPrincipal);
        escenarioBase.setTitle("Home");
        escenarioBase.centerOnScreen();
        escenarioBase.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
