package com.gp.arqueras_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * El "public static void main" de la aplicación, el punto de partida
 *
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2023
 */
public class MainApp extends Application {
    /**
     * Atributo estático que almacena esta propia clase de tipo Application, útil para poder cargar el manuel en PDF en el botón de ayuda después
     */
    public static MainApp applicationInstance;

    @Override
    public void start(Stage stage) throws IOException {
        MainApp.applicationInstance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("menu_interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 413);
        stage.setTitle("¡Bienvenido a Arqueras of Nand!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}