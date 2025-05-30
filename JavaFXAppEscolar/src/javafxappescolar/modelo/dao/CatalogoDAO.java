/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.dao.pojo.Alumno;
import javafxappescolar.modelo.dao.pojo.Carrera;
import javafxappescolar.modelo.dao.pojo.Facultad;

/**
 *
 * @author reino
 */
public class CatalogoDAO {
    public static ArrayList<Facultad> obtenerFacultades()throws SQLException{
    ArrayList facultades= new ArrayList<>();
    Connection conexionBD= ConexionBD.abrirConexion();
    if (conexionBD != null){
        String consulta= "SELECT idFacultad, nombre AS 'facultad' from facultad";
        PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
        ResultSet resultado= sentencia.executeQuery();
        while(resultado.next()){
            facultades.add(convertirRegistroFacultad(resultado));
        }
        sentencia.close();
        resultado.close();
        conexionBD.close();
    }else{
        throw new SQLException("Sin conexion con la base de datos");
    }
    
    return facultades;
    }
     private static Facultad convertirRegistroFacultad(ResultSet resultado) throws SQLException{
        Facultad facultad= new Facultad();
        facultad.setIdFacultad(resultado.getInt("idFacultad"));
        facultad.setNombre(resultado.getString("facultad"));
        return facultad;
    }
    
    public static ArrayList<Carrera> obtenerCarrerasPorFacultad(int idFacultad) throws SQLException{
        ArrayList carreras= new ArrayList<>();
        Connection conexionBD= ConexionBD.abrirConexion();
    if (conexionBD != null){
        String consulta = "SELECT c.idCarrera, c.nombre AS 'carrera', c.codigo, f.idFacultad, f.nombre AS 'Facultad' FROM carrera c JOIN facultad f ON c.idFacultad = f.idFacultad WHERE f.idFacultad = ?";
        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        sentencia.setInt(1, idFacultad);
        ResultSet resultado= sentencia.executeQuery();
        while(resultado.next()){
            carreras.add(convertirRegistroCarrera(resultado));
        }
        sentencia.close();
        resultado.close();
        conexionBD.close();
    }else{
        throw new SQLException("Sin conexion con la base de datos");
    }
    
    return carreras;
    }
        private static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException{
         Carrera carrera= new Carrera();
        carrera.setIdCarrera(resultado.getInt("idCarrera"));
        carrera.setNombre(resultado.getString("carrera"));
        carrera.setCodigo(resultado.getString("codigo"));
        return carrera;
    }
}
