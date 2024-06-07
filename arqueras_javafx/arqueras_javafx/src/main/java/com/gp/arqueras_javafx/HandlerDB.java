package com.gp.arqueras_javafx;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;

/**
 * Clase que ejerce de conexión entre el juego y la base de datos asociada
 */
public class HandlerDB {
    //ATRIBUTOS
    /**
     * La dirección de la base de datos
     */
    private String url;
    /**
     * El usuario con el que va a iniciar sesión en la base de datos
     */
    private String usuario;
    /**
     * La contraseña del usuario
     */
    private String password;

    //CONSTRUCTOR
    public HandlerDB(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    //GETTERS
    public String getUrl() {
        return url;
    }

    //SETTERS
    public void setUrl(String url) {
        this.url = url;
    }


    //MÉTODOS

    /**
     * Ejecuta el script de creación de la base de datos y la tabla de puntuaciones
     */
    public void runDBCreationScript() throws Exception {
        Connection conn = (Connection) DriverManager.getConnection(url, usuario, password);
        ScriptRunner sr = new ScriptRunner(conn);
        Reader reader = new BufferedReader(new FileReader("../../arqueras_db/arqueras_schema.sql"));
        sr.runScript(reader);
        reader.close();
        conn.close();
    }

    /**
     * Inserta en la tabla de puntuaciones los datos del jugador que acaba de ganar
     * @param playerName El nombre que haya introducido el usuario
     * @param difficulty La dificultad de la partida al iniciar el juego
     * @param score La puntuación final tras haber ganado
     */
    public void insertScore(String playerName, String difficulty, int score) throws SQLException {
        Connection conexion = (Connection) DriverManager.getConnection(url, usuario, password);

        PreparedStatement pst = conexion.prepareStatement("insert into scores values (?,?, now(), ?)");
        pst.setString(1, playerName);
        pst.setString(2, difficulty);
        pst.setInt(3, score);
        pst.executeUpdate();

        conexion.close();
    }

    /**
     * Permite eliminar puntuaciones que no queramos almacenar más. Las puntuaciones a eliminar deben identificarse mediante el nombre del jugador y la fecha de registro de la puntuación (son una clave primaria, no puede haber dos puntuaciones del mismo jugador a la misma hora)
     * @param playerName El nombre del jugador cuya puntuación quedó almacenada
     * @param date La fecha (en formato "YYYY-MM-DD HH:MM:SS") en la que se registró la puntuación de dicho jugador
     * @throws SQLException
     */
    public void deleteScore(String playerName, String date) throws SQLException {
        Connection conexion = (Connection) DriverManager.getConnection(url, usuario, password);
        Statement stmt = conexion.createStatement();
        String sql = "delete from scores where name='" + playerName + "' and date='" + date + "'";
        stmt.executeUpdate(sql);
        conexion.close();
    }
}