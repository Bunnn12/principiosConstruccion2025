/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import javafxappescolar.modelo.dao.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxappescolar.modelo.ConexionBD;
/**
 *
 * @author reino
 */
public class inicioSesionDAO {
    public static Usuario verificarCredenciales(String username, String password) throws SQLException{
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null){
            String consulta= "select idUsuario, nombre, apellidoPaterno, apellidoMaterno, username from usuario where username= ? AND password = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery(); //executeQuery es sincrono
            if (resultado.next()){
                usuarioSesion= convertirRegistroUsuario(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }else {
            throw new SQLException("Error: Sin conexión a la base de datos");
        }
        return usuarioSesion;
    }
    
    private static Usuario convertirRegistroUsuario (ResultSet resultado) throws SQLException{
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("idUsuario"));
        usuario.setNombre(resultado.getString("nombre"));
        usuario.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        usuario.setApellidoMaterno(resultado.getString("apellidoMaterno")!=null ? resultado.getString("apellidoMaterno") : " ");
        usuario.setUsername(resultado.getString("username"));
        return usuario;
    }
}
