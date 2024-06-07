package com.gp.arqueras_javafx;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.ibatis.jdbc.ScriptRunner;

public class HandlerDB {
    //ATRIBUTOS
    private String url;
    private String usuario;
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
    public void runDBCreationScript() throws Exception {
        Connection conn = (Connection) DriverManager.getConnection(url, usuario, password);
        ScriptRunner sr = new ScriptRunner(conn);
        Reader reader = new BufferedReader(new FileReader("../../arqueras_db/arqueras_schema.sql"));
        sr.runScript(reader);
        reader.close();
        conn.close();
    }

    public void insertScore(String playerName, String difficulty, int score) throws SQLException {
        Connection conexion = (Connection) DriverManager.getConnection(url, usuario, password);

        PreparedStatement pst = conexion.prepareStatement("insert into scores values (?,?, now(), ?)");
        pst.setString(1, playerName);
        pst.setString(2, difficulty);
        pst.setInt(3, score);
        pst.executeUpdate();

        conexion.close();
    }

    public void deleteScore(String playerName, String date) throws SQLException {
        Connection conexion = (Connection) DriverManager.getConnection(url, usuario, password);
        Statement stmt = conexion.createStatement();
        String sql = "delete from scores where name='" + playerName + "' and date='" + date + "'";
        stmt.executeUpdate(sql);
        conexion.close();
    }
}