package com.gp.arqueras_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller para la ventana de selección de dificultad
 *
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2024
 */
public class DiffController implements Initializable {
    //elementos del fxml
    @FXML
    public Label labelDiff;
    @FXML
    public RadioButton radioEasy;
    @FXML
    public ToggleGroup diffGroup;
    @FXML
    public RadioButton radioNormal;
    @FXML
    public RadioButton radioHard;
    @FXML
    public Label diffDesc;
    @FXML
    public Button btnStart;

    /**
     * Recarga todos los textos iniciales en función del idioma elegido. Se llama nada más lanzar la nueva ventana
     */
    @FXML
    public void refreshTexts() {
        switch (Game.getLanguage()) {
            case "EN":
                labelDiff.setText("Choose a Difficulty Level");
                radioEasy.setText("Easy");
                radioNormal.setText("Normal");
                radioHard.setText("Hard");
                diffDesc.setText("");
                btnStart.setText("Let's Go!");
                break;
            case "ES":
                labelDiff.setText("Elige una dificultad");
                radioEasy.setText("Fácil");
                radioNormal.setText("Normal");
                radioHard.setText("Difícil");
                diffDesc.setText("");
                btnStart.setText("¡Vamos!");
                break;
        }
    }

    /**
     * Carga los radio buttons, estableciendo los listeners
     */
    @FXML
    public void loadRadio() {
        diffGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (diffGroup.getToggles().indexOf(newValue) == 0) {
                switch (Game.getLanguage()) {
                    case "EN":
                        diffDesc.setText("• 10 rounds\n• 50 arrows\n• 1 arrow lost on retreat\n\nIdeal for greenhorn knights who want to get to know the game better.");
                        break;
                    case "ES":
                        diffDesc.setText("• 10 rondas\n• 50 flechas\n• 1 flecha perdida por huida\n\nIdeal para guerreros principiantes que quieren conocer mejor el juego.");
                        break;
                }
            } else if (diffGroup.getToggles().indexOf(newValue) == 1) {
                switch (Game.getLanguage()) {
                    case "EN":
                        diffDesc.setText("• 12 rounds\n• 48 arrows\n• 2 arrows lost on retreat\n\nThe most balanced approach overall.");
                        break;
                    case "ES":
                        diffDesc.setText("• 12 rondas\n• 48 flechas\n• 2 flechas perdidas por huida\n\nLa experiencia más equilibrada.");
                        break;
                }
            } else if (diffGroup.getToggles().indexOf(newValue) == 2) {
                switch (Game.getLanguage()) {
                    case "EN":
                        diffDesc.setText("• 15 rounds\n• 45 arrows\n• 3 arrows lost on retreat\n\nGood luck holding Nand! You'll need it.");
                        break;
                    case "ES":
                        diffDesc.setText("• 15 rondas\n• 45 flechas\n• 3 flechas perdidas por huida\n\n¡Buena suerte defendiendo Nand! La necesitarás.");
                        break;
                }
            }
        });
    }

    /**
     * Lanza la ventana principal del juego y crea un objeto Game en función de la dificultad elegida
     *
     * @param window La ventana desde donde se ha llamado a la nueva ventana (para poder cerrarla luego)
     */
    @FXML
    public void launchGame(Stage window) {
        int radioIndex = diffGroup.getToggles().indexOf(diffGroup.getSelectedToggle());

        Game partida = null;

        if (radioIndex == 0) {
            partida = new Game("easy");
            System.out.println("Dificultad elegida: fácil");
        } else if (radioIndex == 1) {
            partida = new Game("normal");
            System.out.println("Dificultad elegida: normal");
        } else if (radioIndex == 2) {
            partida = new Game("hard");
            System.out.println("Dificultad elegida: difícil");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            switch (Game.getLanguage()) {
                case "EN":
                    alert.setTitle("Can't start game");
                    alert.setHeaderText("Not a valid difficulty setting");
                    alert.setContentText("Please choose a valid difficulty setting in order to play the game!");
                    break;
                case "ES":
                    alert.setTitle("Imposible iniciar partida");
                    alert.setHeaderText("Dificultad no válida");
                    alert.setContentText("¡Elige una dificultad válida para jugar al juego!");
                    break;
            }
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlloader = new FXMLLoader(MainApp.class.getResource("main_interface.fxml"));
            final Stage newWindow = new Stage();
            Scene scene = new Scene(fxmlloader.load(), 1366, 565);
            switch (Game.getLanguage()) {
                case "EN":
                    newWindow.setTitle("Archers of Nand");
                    break;
                case "ES":
                    newWindow.setTitle("Arqueras de Nand");
                    break;
            }
            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();
            newWindow.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
            window.close();
            ((Stage) btnStart.getScene().getWindow()).close();

            MainController controller = fxmlloader.getController();

            controller.newGame(partida);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTexts();
    }
}