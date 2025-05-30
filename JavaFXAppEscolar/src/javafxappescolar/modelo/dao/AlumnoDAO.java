/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.pojo.Alumno;
import javafxappescolar.modelo.dao.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;

/**
 * Este es un mensaje de prueba para ver si funcionan los commit
 * @author reino
 */
public class AlumnoDAO {
    public static ArrayList<Alumno> obtenerAlumnos() throws SQLException{
    ArrayList alumnos= new ArrayList<>();
    Connection conexionBD= ConexionBD.abrirConexion();
    if (conexionBD != null){
        String consulta= "SELECT idAlumno, a.nombre, apellidoPaterno, apellidoMaterno, matricula, email, a.idCarrera, fechaNacimiento, c.nombre AS 'carrera', c.idFacultad, f.nombre AS 'Facultad' FROM alumno a JOIN carrera c ON c.idCarrera= a.idCarrera JOIN facultad f ON f.idFacultad= c.idFacultad";
        PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
        ResultSet resultado= sentencia.executeQuery();
        while(resultado.next()){
            alumnos.add(convertirRegistroAlumno(resultado));
        }
        sentencia.close();
        resultado.close();
        conexionBD.close();
    }else{
        throw new SQLException("Sin conexion con la base de datos");
    }
    
    return alumnos;
}
    public static ResultadoOperacion registrarAlumno(Alumno alumno) throws SQLException{
        ResultadoOperacion resultado= new ResultadoOperacion();
        Connection conexionBD= ConexionBD.abrirConexion();
        if(conexionBD!=null){
            String consulta= " INSERT INTO alumno (nombre, apellidoPaterno, apellidoMaterno, matricula, email, idCarrera, fechaNacimiento, foto) VALUES(?, ?, ?, ?, ?, ?,?, ?)";
            PreparedStatement prepararSentencia= conexionBD.prepareStatement(consulta);
            prepararSentencia.setString(1, alumno.getNombre());
            prepararSentencia.setString(2, alumno.getApellidoPaterno());
            prepararSentencia.setString(3, alumno.getApellidoMaterno());
            prepararSentencia.setString(4, alumno.getMatricula());
            prepararSentencia.setString(5, alumno.getEmail());
            prepararSentencia.setInt(6, alumno.getIdCarrera());
            prepararSentencia.setString(7, alumno.getFechaNacimiento());
            prepararSentencia.setBytes(8, alumno.getFoto());
            int filasAfctadas= prepararSentencia.executeUpdate();
            if (filasAfctadas== 1){
                resultado.setError(false);
                resultado.setMensaje("Alumno(a), registrado(a) correctamente");
            }
            else{
                resultado.setError(true);
                resultado.setMensaje("Lo sentimos :( por el momento no se puede registrar la información del alumno");
            }
            prepararSentencia.close();
            conexionBD.close();
        } else{
            throw new SQLException("Sin conexion con la base de datos");
        }
        return resultado;
    }
    private static Alumno convertirRegistroAlumno(ResultSet resultado) throws SQLException{
        Alumno alumno= new Alumno();
        alumno.setIdAlumno(resultado.getInt("idAlumno"));
        alumno.setNombre(resultado.getString("nombre"));
        alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        alumno.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setEmail(resultado.getString("email"));
        alumno.setIdCarrera(resultado.getInt("idCarrera"));
        alumno.setCarrera(resultado.getString("carrera"));
        alumno.setIdFacultad(resultado.getInt("idFacultad"));
        alumno.setFacultad(resultado.getString("facultad"));
        alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
        return alumno;
    }
    public static byte[] obtenerFotoAlumno(int idAlumno)throws SQLException{
        
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
        if (resultado.next()) {
            byte[] fotoBytes = resultado.getBytes("foto");
            if (fotoBytes != null) {
                foto = new byte[fotoBytes.length];
                for (int i = 0; i < fotoBytes.length; i++) {
                    foto[i] = fotoBytes[i];
                }
            }
        }
        resultado.close();
        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }
    return foto;
    }
    public static boolean verificarExistenciaMatricula(String matricula)throws SQLException{
        boolean existe = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
        String consulta = "SELECT COUNT(*) AS total FROM alumno WHERE matricula = ?";
        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setString(1, matricula);
        ResultSet resultado = sentencia.executeQuery();
        if (resultado.next()) {
            existe = resultado.getInt("total") > 0;
        }
        resultado.close();
        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }
    return existe;
    }
    public static ResultadoOperacion editarAlumno(Alumno alumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
        String consulta = "UPDATE alumno SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, email = ?, idCarrera = ?, fechaNacimiento = ?, foto = ? WHERE idAlumno = ?";
        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setString(1, alumno.getNombre());
        sentencia.setString(2, alumno.getApellidoPaterno());
        sentencia.setString(3, alumno.getApellidoMaterno());
        sentencia.setString(4, alumno.getEmail());
        sentencia.setInt(5, alumno.getIdCarrera());
        sentencia.setString(6, alumno.getFechaNacimiento());
        sentencia.setBytes(7, alumno.getFoto());
        sentencia.setInt(8, alumno.getIdAlumno());

        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas == 1) {
            resultado.setError(false);
            resultado.setMensaje("Alumno(a) actualizado(a) correctamente");
        } else {
            resultado.setError(true);
            resultado.setMensaje("No se pudo actualizar la información del alumno");
        }

        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }
    return resultado;
    }
    
    public static ResultadoOperacion eliminarAlumno(int idAlumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
        String consulta = "DELETE FROM alumno WHERE idAlumno = ?";
        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setInt(1, idAlumno);

        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas == 1) {
            resultado.setError(false);
            resultado.setMensaje("Alumno(a) eliminado(a) correctamente");
        } else {
            resultado.setError(true);
            resultado.setMensaje("No se pudo eliminar al alumno");
        }

        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }
    return resultado;
    }
}
