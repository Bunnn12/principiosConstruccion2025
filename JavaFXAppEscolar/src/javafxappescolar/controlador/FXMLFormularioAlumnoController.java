/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxappescolar.dominio.AlumnoDM;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.CatalogoDAO;
import javafxappescolar.modelo.dao.pojo.Alumno;
import javafxappescolar.modelo.dao.pojo.Carrera;
import javafxappescolar.modelo.dao.pojo.Facultad;
import javafxappescolar.modelo.dao.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;
import javax.imageio.ImageIO;
import static sun.security.krb5.Confounder.bytes;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private ImageView ivFoto;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ComboBox<Facultad> cbFacultad;
    @FXML
    private ComboBox<Carrera> cbCarrera;
    
    ObservableList<Facultad> facultades;
    ObservableList<Carrera> carreras;
    File archivoFoto;
    @FXML
    private TextField tfApellidoMaterno;
    INotificacion observador;
    Alumno alumnoEdicion;
    boolean esEdicion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFacultades();
        seleccionarFacultad();
    }
    private void cargarFacultades(){
        try {
            facultades= FXCollections.observableArrayList();
            List <Facultad> facultadesDAO = CatalogoDAO.obtenerFacultades();
            facultades.addAll(facultadesDAO);
            cbFacultad.setItems(facultades);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            ex.printStackTrace();
        }
    }
private void seleccionarFacultad() {
    cbFacultad.valueProperty().addListener(new ChangeListener<Facultad>() {
        @Override
        public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
            if (newValue != null) {
                cargarCarreras(newValue.getIdFacultad());
            }
        }
    });
}

    private void cargarCarreras(int idFacultad){
            try {
            carreras= FXCollections.observableArrayList();
            List <Carrera> carrerasDAO = CatalogoDAO.obtenerCarrerasPorFacultad(idFacultad);
            carreras.addAll(carrerasDAO);
            cbCarrera.setItems(carreras);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            ex.printStackTrace();
        }
}    


    @FXML
    private void clicGuardar(ActionEvent event) {
        if(validarCampos()){
            try {
                if(!esEdicion){
                    ResultadoOperacion resultado= AlumnoDM.verificarEstatusEstadoMatricula(tfMatricula.getText());
                    if(!resultado.isError()){
                        Alumno alumno = obtenerAlumnoNuevo();
                        guardarAlumno(alumno);
                    }
                    else{
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Verificar datos", "Verifique los datos ingresados");
                    }
                }else{
                     Alumno alumno = obtenerAlumnoEdicion();
                     modificarAlumno(alumnoEdicion);
                }
            } catch (IOException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en foto", "Lo sentimos :(, la foto seleccionada no puede ser guardada");
            }
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Utilidad.obtenerEscenarioComponente(tfNombre).close();
    }

    @FXML
    private void clicSeleccionarFoto(ActionEvent event) {
        mostrarDialogoSeleccionFoto();
    }
    
    private void mostrarDialogoSeleccionFoto(){
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImg= new FileChooser.ExtensionFilter("Archivos JPG (.jpg)","*.jpg");
        dialogoSeleccion.getExtensionFilters().add(filtroImg);
        archivoFoto= dialogoSeleccion.showOpenDialog(Utilidad.obtenerEscenarioComponente(tfNombre));
        if(archivoFoto != null){
            mostrarFotoPerfil(archivoFoto);
        }
    }
    private void mostrarFotoPerfil(File archivoFoto){
        try {
            BufferedImage bufferImg= ImageIO.read(archivoFoto);
            Image imagen= SwingFXUtils.toFXImage(bufferImg, null);
            ivFoto.setImage(imagen);
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            ex.printStackTrace();
        }
    }
    
    private boolean validarCampos(){
        String nombre = tfNombre.getText().trim();
        String apellidoPaterno = tfApellidoPaterno.getText().trim();
        String apellidoMaterno = tfApellidoMaterno.getText().trim();
        String matricula = tfMatricula.getText().trim();
        String email = tfEmail.getText().trim();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        Facultad facultad = cbFacultad.getSelectionModel().getSelectedItem();
        Carrera carrera = cbCarrera.getSelectionModel().getSelectedItem();

    if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || 
        matricula.isEmpty() || email.isEmpty()) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, completa todos los campos obligatorios.");
        return false;
    }

    if (fechaNacimiento == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fecha faltante", "Selecciona una fecha de nacimiento.");
        return false;
    }

    if (facultad == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Facultad faltante", "Selecciona una facultad.");
        return false;
    }

    if (carrera == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Carrera faltante", "Selecciona una carrera.");
        return false;
    }

    if (archivoFoto == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Foto faltante", "Selecciona una foto para el perfil.");
        return false;
    }

    // Verificar existencia de matrícula en base de datos (solo si no es edición)
    if (!esEdicion) {
        try {
            if (AlumnoDAO.verificarExistenciaMatricula(matricula)) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Matrícula existente", "Ya existe un alumno con esta matrícula.");
                return false;
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No se pudo validar la matrícula.");
            return false;
        }
    }

    return true;
    }
    private Alumno obtenerAlumnoNuevo() throws IOException{
        Alumno alumno= new Alumno();
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApellidoPaterno.getText());
        alumno.setApellidoMaterno(tfApellidoMaterno.getText());
        alumno.setEmail(tfEmail.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        Carrera carrera = cbCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carrera.getIdCarrera());
        byte[] foto = Files.readAllBytes(archivoFoto.toPath());
        alumno.setFoto(foto);
        return alumno;
    }
    private void guardarAlumno(Alumno alumno){   
        try {
            ResultadoOperacion resultadoInsertar= AlumnoDAO.registrarAlumno(alumno);
            if (!resultadoInsertar.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno registrado", "El alumno(a) "+alumno.getNombre()+" ha sido registrado correctamente");
                Utilidad.obtenerEscenarioComponente(tfNombre).close();
                observador.operacionExitosa("Insertar", alumno.getNombre());
            }
            else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultadoInsertar.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    private void modificarAlumno(Alumno alumnoEdicion){
        try {
            ResultadoOperacion resultadoInsertar= AlumnoDAO.editarAlumno(alumnoEdicion);
            if (!resultadoInsertar.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno modificado: "+alumnoEdicion.getNombre(),"ha sido registrado correctamente");
                Utilidad.obtenerEscenarioComponente(tfNombre).close();
                observador.operacionExitosa("Insertar", alumnoEdicion.getNombre());
            }
            else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar", resultadoInsertar.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
        }
    }
    public void InicializarInformacion(boolean esEdicion, Alumno alumnoEdicion, INotificacion observador){
        this.esEdicion= esEdicion;
        this.alumnoEdicion= alumnoEdicion;
        this.observador= observador;
        if(esEdicion== true){
            cargarInformacionEdicion();
        }
    }
    private void cargarInformacionEdicion(){
        tfNombre.setText(alumnoEdicion.getNombre());
        tfApellidoPaterno.setText(alumnoEdicion.getApellidoPaterno());
        tfApellidoMaterno.setText(alumnoEdicion.getApellidoMaterno());
        tfMatricula.setText(alumnoEdicion.getMatricula());
        tfEmail.setText(alumnoEdicion.getEmail());
        dpFechaNacimiento.setValue(LocalDate.parse(alumnoEdicion.getFechaNacimiento()));
        tfMatricula.setEditable(false);
        if(alumnoEdicion.getFechaNacimiento() != null){
            LocalDate.parse(alumnoEdicion.getFechaNacimiento());
        }
        int indice = obtenerPosicionFacultad(alumnoEdicion.getIdFacultad());
        cbFacultad.getSelectionModel().select(indice);
        int indiceCarrera= obtenerPosicionCarrera(alumnoEdicion.getIdCarrera());
        cbCarrera.getSelectionModel().select(indiceCarrera);
        try {
            byte[] foto= AlumnoDAO.obtenerFotoAlumno(alumnoEdicion.getIdAlumno());
            ByteArrayInputStream input = new ByteArrayInputStream(foto);
            Image image = new Image(input);
            ivFoto.setImage(image);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    
    private int obtenerPosicionFacultad(int idFacultad){
        for(int i=0; i< facultades.size(); i++){
            if (facultades.get(i).getIdFacultad()== idFacultad){
                return i;
            }
        }
        
        return 0;
    }
    
        private int obtenerPosicionCarrera(int idCarrera){
        for(int i=0; i< carreras.size(); i++){
            if (carreras.get(i).getIdCarrera()== idCarrera){
                return i;
            }
        }
        
        return 0;
    }
        private Alumno obtenerAlumnoEdicion() throws IOException{
        Alumno alumno= new Alumno();
        alumno.setIdAlumno(alumnoEdicion.getIdAlumno());
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApellidoPaterno.getText());
        alumno.setApellidoMaterno(tfApellidoMaterno.getText());
        alumno.setEmail(tfEmail.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setFechaNacimiento(dpFechaNacimiento.getValue().toString());
        Carrera carrera = cbCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carrera.getIdCarrera());
        if(archivoFoto!= null){
            byte[] foto = Files.readAllBytes(archivoFoto.toPath());
             alumno.setFoto(foto);
        } else{
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        return alumno;
    }
}
