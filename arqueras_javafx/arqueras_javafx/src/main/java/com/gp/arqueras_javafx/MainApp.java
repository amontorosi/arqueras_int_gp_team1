package com.gp.arqueras_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * El punto de la partida de la aplicación, la clase que lanza la ventana del menú principal
 *
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2023
 */
public class MainApp extends Application {
    /**
     * Atributo estático que almacena esta propia clase de tipo Application, útil para poder cargar el manuel en PDF en el botón de ayuda después
     */
    public static MainApp applicationInstance;

    /**
     * Método que lanza el menú principal
     *
     * @param stage La ventana donde se va a insertar la escena cargada del FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        MainApp.applicationInstance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("menu_interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 413);
        stage.setTitle("¡Bienvenido a Arqueras of Nand!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/archers_of_nand_icon.png")));
        stage.show();
    }
}