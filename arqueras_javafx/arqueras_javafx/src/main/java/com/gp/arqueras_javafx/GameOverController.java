package com.gp.arqueras_javafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;

/**
 * El controller para la ventana que aparece al finalizar la partida
 *
 * @author Manuel Valdepeñas
 * @version 3.0 22/05/2024
 */
public class GameOverController {
    @FXML
    public Label labelPoints;
    @FXML
    public Label labelPointsDyn;
    @FXML
    public Label labelGameDesc;
    @FXML
    public Label labelGameOver;
    @FXML
    public Button btnExit;
    @FXML
    public ImageView imgIcon;
    @FXML
    public HBox HBoxUsername;
    @FXML
    public Label labelUsername;
    @FXML
    public TextField tfUsername;
    /**
     * El controller del tablero principal, para poder acceder a sus elementos directamente desde esta clase
     */
    private MainController mainController;

    /**
     * Setter del controller principal, ya que es privado
     *
     * @param mainController El controller principal, para poder modificar elementos del tablero principal
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Método que acaba la partida con derrota
     */
    public void gameOverLose(){
        HBoxUsername.setVisible(false);
        HBoxUsername.setManaged(false);
        System.out.println("Partida perdida");
        switch (Game.getLanguage()) {
            case "EN":
                labelGameDesc.setText("Four or more teritories have fallen to the orcs. The Valley of Nand has succumbed to their power. You've lost.");
                btnExit.setText("Exit");
                labelGameOver.setText("GAME OVER");
                break;
            case "ES":
                labelGameDesc.setText("Cuatro o más territorios han caído a manos de los orcos. El valle de Nand ha sucumbido a su poder. Has perdido.");
                btnExit.setText("Salir");
                labelGameOver.setText("FIN DE LA PARTIDA");
                break;
        }
        labelPoints.setVisible(false);
        labelPointsDyn.setVisible(false);
        imgIcon.setImage(new Image(new File("src/main/resources/img/defeat.png").toURI().toString()));
        btnExit.setOnAction(e -> Platform.exit());

        //si se cierra la ventana, también cuenta como que se sale del juego
        btnExit.getScene().getWindow().setOnCloseRequest(closeEv -> Platform.exit());
    }

    /**
     * Método que acaba la partida con victoria
     */
    public void gameOverWin(Game currentGame){
        System.out.println("Partida ganada");
        //contar los puntos totales ganados
        int totalPoints = 0;
        if (currentGame.getDifficulty().equalsIgnoreCase("easy")) {
            totalPoints += 15;
        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal")) {
            totalPoints += 25;
        } else if (currentGame.getDifficulty().equalsIgnoreCase("hard")) {
            totalPoints += 40;
        }

        for (Territorio terr : mainController.listaTerritorios) {
            if (terr.isDestruido()) {
                totalPoints -= 5;
            } else {
                totalPoints -= terr.getCantidadOrcos();
            }
            totalPoints += terr.getCantidadGuerreros();
        }

        totalPoints += mainController.arqueras.getCantidadFlechas();

        switch (Game.getLanguage()) {
            case "EN":
                labelGameDesc.setText("Congratulations, you have managed to keep the orcs at bay and the Valley of Nand is safe! You've won!");
                btnExit.setText("Exit");
                labelGameOver.setText("GAME OVER");
                labelPoints.setText("Points: ");
                labelUsername.setText("Enter your name: ");
                break;
            case "ES":
                labelGameDesc.setText("¡Enhorabuena, has conseguido mantener a los orcos a raya y el valle de Nand se ha salvado! ¡Has ganado!");
                btnExit.setText("Salir");
                labelGameOver.setText("FIN DE LA PARTIDA");
                labelPoints.setText("Puntos: ");
                labelUsername.setText("Introduce tu nombre: ");
                break;
        }
        labelPointsDyn.setText(String.valueOf(totalPoints));
        imgIcon.setImage(new Image(new File("src/main/resources/img/victory.png").toURI().toString()));
        btnExit.setOnAction(e -> {
            if (tfUsername.getText().isEmpty() || tfUsername.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                switch (Game.getLanguage()) {
                    case "EN":
                        alert.setTitle("Error");
                        alert.setHeaderText("Name required");
                        alert.setContentText("You must enter a name in order to save your score!");
                        break;
                    case "ES":
                        alert.setTitle("Error");
                        alert.setHeaderText("Nombre necesario");
                        alert.setContentText("¡Debes introducir un nombre para registrar tu puntuación!");
                        break;
                }
                alert.show();
            } else {
                Platform.exit();
            }
        });

        //si se cierra la ventana, también cuenta como que se sale del juego
        btnExit.getScene().getWindow().setOnCloseRequest(closeEv -> {
            if (tfUsername.getText().isEmpty() || tfUsername.getText().isBlank()) {
                closeEv.consume();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                switch (Game.getLanguage()) {
                    case "EN":
                        alert.setTitle("Error");
                        alert.setHeaderText("Name required");
                        alert.setContentText("You must enter a name in order to save your score!");
                        break;
                    case "ES":
                        alert.setTitle("Error");
                        alert.setHeaderText("Nombre necesario");
                        alert.setContentText("¡Debes introducir un nombre para registrar tu puntuación!");
                        break;
                }
                alert.show();
            } else {
                Platform.exit();
            }
        });
    }
}
