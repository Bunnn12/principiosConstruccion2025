/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.pojo.Alumno;
import javafxappescolar.modelo.dao.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLAdminAlumnoController implements Initializable, INotificacion {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colApePaterno;
    @FXML
    private TableColumn colApeMaterno;
    @FXML
    private TableColumn colFacultad;
    @FXML
    private TableColumn colCarrera;
    @FXML
    private TableView<Alumno> tvAlumnos;
    @FXML
    private TableColumn colNombre;
    private ObservableList<Alumno> alumnos;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    
    
    private void configurarTabla(){
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApePaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApeMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colFacultad.setCellValueFactory(new PropertyValueFactory("facultad"));
        colCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
        
    }
    private void cargarInformacionTabla(){
        try{
        alumnos= FXCollections.observableArrayList();
        ArrayList<Alumno> alumnosDAO = AlumnoDAO.obtenerAlumnos();
        alumnos.addAll(alumnosDAO);
        tvAlumnos.setItems(alumnos);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los alumnos, por favor intentélo más tarde");
            cerrarVentana();
        }
            
    }
    private void cerrarVentana(){
        ((Stage) tfBuscar.getScene().getWindow()).close();
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioAlumno(false, null);
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
        Alumno alumno =tvAlumnos.getSelectionModel().getSelectedItem();
        if(alumno !=null){
            irFormularioAlumno(true, alumno);
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
                    "Selecciona un alumno", "Para modificar la información del alumno debe seleccionarlo primero de la tabla");
        }
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) throws SQLException {
        //int pos = tvAlumnos.getSelectionModel().getSelectedIndex()
        //En el if seria if(pos>= 0) significa que si seleccioono ya que regresa -1 si es null la seleccion
        Alumno alumno = tvAlumnos.getSelectionModel().getSelectedItem();
        String confirmar= String.format("¿Estás seguro de que quieres eliminar al alumno(a) %s %s?\n Una vez eliminado el registro no podrá ser recuperado", alumno.getNombre(), alumno.getApellidoPaterno());
        if(alumno!= null){
            if(Utilidad.mostrarAlertaConfirmacion("Eliminar Alumno", confirmar)){
                eliminarAlumno(alumno.getIdAlumno());
                //Con el otro metodo seria Alumno alumno= alumnos.get(pos);
                //eliminarAlumno(alumno.getIdAlumno())
            }
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un alumno", "Para eliminar el registro de un alumno debe seleccionarlo primero de la tabla");
        }
    }
    
    private void eliminarAlumno(int idAlumno) {
        try {
            ResultadoOperacion resultado= AlumnoDAO.eliminarAlumno(idAlumno);
            if(!resultado.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno(a) eliminado", "El registro del alumno fue eliminado correctamente");
                cargarInformacionTabla();
            } else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al eliminar", resultado.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas al eliminar", "Lo sentimos :(, el alumno no puede ser eliminado en este momento, inténtelo de nuevo más tarde");
            ex.printStackTrace();
        }
    }
    private void irFormularioAlumno(boolean esEdicion, Alumno alumnoEdicion){
    try {
        Stage escenarioFormulario = new Stage();
        FXMLLoader loader = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLFormularioAlumno.fxml"));
        Parent vista = loader.load(); 
        FXMLFormularioAlumnoController controlador= loader.getController();
        controlador.InicializarInformacion(esEdicion, alumnoEdicion, this);
        Scene escena = new Scene(vista);
        escenarioFormulario.setTitle("Formulario de alumno");
        escenarioFormulario.setScene(escena);
        escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
        escenarioFormulario.showAndWait();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
        
    }

    @Override
    public void operacionExitosa(String tipo, String nombreAlumno) {
        System.out.println("operacion exitosa" + tipo+ " ,con el alumno "+nombreAlumno);
        cargarInformacionTabla();
    }
}
