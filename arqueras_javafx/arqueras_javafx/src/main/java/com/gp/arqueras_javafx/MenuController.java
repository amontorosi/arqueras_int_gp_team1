package com.gp.arqueras_javafx;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * El Controller del menú de inicio, la primera ventana que aparece
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2024
 */
public class MenuController implements Initializable {
    @FXML
    public ChoiceBox<String> selectLang;
    @FXML
    public Label labelLang;
    @FXML
    public Button btnExit;
    @FXML
    public Button btnNewGame;

    /**
     * Lo que se ejecutará nada más arrancar el programa, las inicializaciones y los listeners predeterminados
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectLang.getItems().add("English");
        selectLang.getItems().add("Español");
        selectLang.getSelectionModel().select("Español");
        Game.setLanguage("ES");

        selectLang.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            switch (newSelection) {
                case "English":
                    Game.setLanguage("EN");
                    break;
                case "Español":
                    Game.setLanguage("ES");
                    break;
            }
            refreshTexts();
        });

        btnExit.setOnAction((e) -> exit(e));
    }

    /**
     * Actualiza todos los textos de la interfaz en función del idioma elegido
     */
    @FXML
    public void refreshTexts(){
        switch (Game.getLanguage()) {
            case "EN":
                labelLang.setText("Language: ");
                btnExit.setText("Quit");
                btnNewGame.setText("New Game");
                ((Stage) btnExit.getScene().getWindow()).setTitle("Welcome to Archers of Nand!");
                break;
            case "ES":
                labelLang.setText("Idioma: ");
                btnExit.setText("Salir");
                btnNewGame.setText("Nueva partida");
                ((Stage) btnExit.getScene().getWindow()).setTitle("¡Te damos la bienvenida a Arqueras de Nand!");
                break;
        }
    }

    /**
     * Abre la ventana de selección de dificultad
     */
    @FXML
    public void startGame(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("difficulty_interface.fxml"));
            final Stage dialog = new Stage();
            switch (Game.getLanguage()) {
                case "EN":
                    dialog.setTitle("Choose a Difficulty Level");
                    break;
                case "ES":
                    dialog.setTitle("Elige una dificultad");
                    break;
            }
            Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));

            dialog.show();

            Stage window = (Stage) btnExit.getScene().getWindow();

            DiffController controller = fxmlLoader.getController();
            controller.refreshTexts();
            controller.loadRadio();
            controller.btnStart.setOnAction((e) -> {
                controller.launchGame(window);
            });
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    /**
     * Salir del juego, cerrando la ventana
     * @param e El evento que genera el botón que se ha pulsado
     */
    @FXML
    public void exit(Event e){
        Button pressedBtn = (Button) e.getSource();
        Stage window = (Stage) pressedBtn.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        switch (Game.getLanguage()) {
            case "EN":
                alert.setTitle("Quit to desktop");
                alert.setHeaderText("Are you sure you want to exit the game?");
                alert.setContentText("The Valley of Nand needs your help!");
                break;
            case "ES":
                alert.setTitle("Salir del juego");
                alert.setHeaderText("¿Seguro que quieres salir del juego?");
                alert.setContentText("¡El valle de Nand necesita tu ayuda!");
                break;
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            window.close();
        }
    }
}
