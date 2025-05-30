/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.dominio;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.pojo.ResultadoOperacion;

/**
 *
 * @author reino
 */
public class AlumnoDM {
    public static ResultadoOperacion verificarEstatusEstadoMatricula(String matricula){
        ResultadoOperacion resultado= new ResultadoOperacion();
        if(matricula.startsWith("s")){
            try {
                boolean existe= AlumnoDAO.verificarExistenciaMatricula(matricula);
                resultado.setError(existe);
                resultado.setMensaje("La matricula ya existe en los registros");
            } catch (SQLException ex) {
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se puede validar la matricula intentelo más tarde");
            }
        }else{
            resultado.setError(true);
            resultado.setMensaje("La matricula no tiene el formato correcto");
        }
        return resultado;
    }
}
