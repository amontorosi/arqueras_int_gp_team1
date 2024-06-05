package com.gp.arqueras_javafx;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * El controller para la ventana que aparece al elegir una carta
 *
 * @author Manuel Valdepeñas
 * @version 3.0 05/05/2024
 */
public class CardController {
    //elementos de la interfaz
    @FXML
    public Label labelCard;
    @FXML
    public Label labelCardDyn;
    @FXML
    public Button btnMoveKnight;
    @FXML
    public Button btnArchers;
    @FXML
    public Button btnDiscard;
    @FXML
    public VBox containerButtons;
    @FXML
    public Button btnConfirm;
    /**
     * El controller del tablero principal, para poder manipular sus elementos directamente desde esta clase
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

    //funciones de la interfaz propiamente dicha

    /**
     * Función que hace las veces del initialize típico, pero a mi manera, pasándole un parámetro. Actualiza los textos según el idioma, añade los handlers y carga las demás funcionalidades
     *
     * @param i El índice de la carta que se ha escogido
     */
    public void refreshTextsCard(int i) {
        for (ImageView img : mainController.cardsView) {
            img.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
        }

        btnConfirm.getScene().getWindow().setOnCloseRequest(closeEv -> {
            for (ImageView img : mainController.cardsView) {
                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
            }
        });

        switch (Game.getLanguage()) {
            case "EN":
                labelCard.setText("Use card: ");
                labelCardDyn.setText(mainController.cuatroCartasActuales.get(i).getTipo());
                btnMoveKnight.setText("Move a Knight");
                btnArchers.setText("Call the Archers");
                btnDiscard.setText("Discard");
                btnConfirm.setText("Confirm");
                break;
            case "ES":
                labelCard.setText("Usar carta: ");
                labelCardDyn.setText(mainController.cuatroCartasActuales.get(i).getTipo());
                btnMoveKnight.setText("Mover un guerrero");
                btnArchers.setText("Llamar a las arqueras");
                btnDiscard.setText("Descartar");
                btnConfirm.setText("Confirmar");
                break;
        }

        loadButtons(i);
    }

    /**
     * Añade funcionalidades a los tres botones (mover guerrero, llamar a arqueras (con todos los tipos de cartas incluidos), descartar)
     *
     * @param i El índice de la carta que se ha escogido
     */
    public void loadButtons(int i) {
        //Botón "Mover guerrero"
        btnMoveKnight.setOnAction((e) -> {
            ((Stage) (btnDiscard.getScene().getWindow())).close();
            switch (Game.getLanguage()) {
                case "EN":
                    mainController.log.setText(mainController.log.getText() + "\n\nChoose a territory with at least one knight.");
                    break;
                case "ES":
                    mainController.log.setText(mainController.log.getText() + "\n\nSelecciona un territorio donde haya al menos un guerrero.");
                    break;
            }

            mainController.boardHandler = MouseEvent -> {
                Territorio territory = mainController.listaTerritorios.get(mainController.stackTerritoriesList.indexOf(MouseEvent.getSource()));

                if (territory.getCantidadGuerreros() < 1) {
                    switch (Game.getLanguage()) {
                        case "EN":
                            mainController.log.setText(mainController.log.getText() + "\nThere are no knights in that territory. Operation aborted. Please choose a card again.");
                            break;
                        case "ES":
                            mainController.log.setText(mainController.log.getText() + "\nEn ese territorio no hay guerreros. Operación anulada. Vuelve a seleccionar una carta.");
                            break;
                    }
                    for (ImageView img : mainController.cardsView) {
                        img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                    }
                    for (StackPane stack : mainController.stackTerritoriesList) {
                        stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler);
                    }
                } else {
                    switch (Game.getLanguage()) {
                        case "EN":
                            mainController.log.setText(mainController.log.getText() + "\nOne knight from " + territory.getNombre() + " selected. Now choose an adjacent territory to send them to.");
                            break;
                        case "ES":
                            mainController.log.setText(mainController.log.getText() + "\nUn guerrero de " + territory.getNombre() + " seleccionado. Ahora selecciona un territorio adyacente donde enviarlo.");
                            break;
                    }

                    mainController.boardHandler2 = MouseEvent2 -> {
                        Territorio territory2 = mainController.listaTerritorios.get(mainController.stackTerritoriesList.indexOf(MouseEvent2.getSource()));

                        if (territory.getTerritoriosAdyacentes().contains(territory2)) {
                            if (territory2.isDestruido()) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        alert.setTitle("Error");
                                        alert.setHeaderText("Destroyed territory");
                                        alert.setContentText("You can't send knights to a destroyed territory!");
                                        break;
                                    case "ES":
                                        alert.setTitle("Error");
                                        alert.setHeaderText("Territorio destruido");
                                        alert.setContentText("¡No puedes enviar guerreros a un territorio destruido!");
                                        break;
                                }
                                alert.show();
                            } else {
                                territory.setCantidadGuerreros(territory.getCantidadGuerreros() - 1);
                                territory2.setCantidadGuerreros(territory2.getCantidadGuerreros() + 1);
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\nKnight successfully moved to " + territory2.getNombre() + ".");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\nGuerrero movido a " + territory2.getNombre() + " correctamente.");
                                        break;
                                }
                                for (ImageView img : mainController.cardsView) {
                                    img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                                }
                                for (StackPane stack : mainController.stackTerritoriesList) {
                                    stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler);
                                    stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler2);
                                }

                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            }
                        } else {
                            switch (Game.getLanguage()) {
                                case "EN":
                                    mainController.log.setText(mainController.log.getText() + "\nThat is not an adjacent territory. Operation aborted. Please choose a card again.");
                                    break;
                                case "ES":
                                    mainController.log.setText(mainController.log.getText() + "\nEse no es un territorio adyacente. Operación anulada. Vuelve a seleccionar una carta.");
                                    break;
                            }
                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }
                            for (StackPane stack : mainController.stackTerritoriesList) {
                                stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler);
                                stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler2);
                            }
                        }
                    };

                    for (StackPane stack : mainController.stackTerritoriesList) {
                        stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler);
                        stack.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler2);
                    }

                }
            };

            for (StackPane stack : mainController.stackTerritoriesList) {
                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.boardHandler);
            }
        });


        //Botón "Llamar a las arqueras"
        btnArchers.setOnAction((e) -> {
            containerButtons.getChildren().clear();
            btnConfirm.setVisible(true);

            Label labelTxtField;
            TextField txtField;
            HBox containerCoins;
            ImageView coinGreen;
            ImageView coinRed;
            ImageView coinBlue;
            EventHandler<MouseEvent> coinHandler;
            EventHandler<MouseEvent> boardHandler;
            //Esto son referencias atómicas. En las funciones lambda no se pueden modificar variables literales declaradas fuera, sino que hay que crear una referencia de memoria (como si fueran objetos)
            AtomicBoolean esMontanna = new AtomicBoolean(false);
            AtomicBoolean esBosque = new AtomicBoolean(false);
            AtomicBoolean esMar = new AtomicBoolean(false);
            AtomicBoolean coinRedChosen1 = new AtomicBoolean(false);
            AtomicBoolean coinGreenChosen1 = new AtomicBoolean(false);
            AtomicBoolean coinBlueChosen1 = new AtomicBoolean(false);
            AtomicBoolean coinRedChosen2 = new AtomicBoolean(false);
            AtomicBoolean coinGreenChosen2 = new AtomicBoolean(false);
            AtomicBoolean coinBlueChosen2 = new AtomicBoolean(false);

            AtomicInteger mouseEventClicks = new AtomicInteger(0);

            ArrayList<Territorio> territoriosAAtacar;
            ArrayList<Territorio> finalTerritoriosAAtacar;
            switch (mainController.cuatroCartasActuales.get(i).getTipo()) {
                //dispara a los territorios con X cantidad de estandartes
                case "COUNT":
                    territoriosAAtacar = new ArrayList<>();

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Enter a number of banners (1, 2 or 3):");
                            break;
                        case "ES":
                            labelTxtField.setText("Escribe un número de estandartes (1, 2, o 3):");
                            break;
                    }
                    txtField = new TextField();
                    txtField.setMaxWidth(400);
                    txtField.setStyle("-fx-background-color: burlywood; -fx-border-color: grey; -fx-font-family: 'Lemon'; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                    VBox.setMargin(txtField, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, txtField);

                    finalTerritoriosAAtacar = territoriosAAtacar;
                    btnConfirm.setOnAction((event) -> {
                        if (!txtField.getText().trim().equals("1") && !txtField.getText().trim().equals("2") && !txtField.getText().trim().equals("3")) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            switch (Game.getLanguage()) {
                                case "EN":
                                    alert.setTitle("Not a valid number");
                                    alert.setHeaderText("Please enter a valid number of banners");
                                    alert.setContentText("Please enter a valid number between 1, 2 and 3.");
                                    break;
                                case "ES":
                                    alert.setTitle("Número no válido");
                                    alert.setHeaderText("Introduce un número válido de estandartes");
                                    alert.setContentText("Por favor, introduce un número válido entre 1, 2 y 3.");
                                    break;
                            }
                            alert.showAndWait();
                        } else {
                            for (Territorio territorio : mainController.listaTerritorios) {
                                int cuentaEstandartes = 0;
                                if (territorio.isMontanna()) {
                                    cuentaEstandartes++;
                                }
                                if (territorio.isMar()) {
                                    cuentaEstandartes++;
                                }
                                if (territorio.isBosque()) {
                                    cuentaEstandartes++;
                                }
                                if (cuentaEstandartes == Integer.parseInt(txtField.getText().trim())) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }

                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                //una vez decididos los territorios a atacar, se disparan las flechas
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }

                            ((Stage) btnConfirm.getScene().getWindow()).close();
                        }
                    });
                    break;


                //dispara a los territorios que NO tengan X color
                case "NOT":
                    btnConfirm.setVisible(false);
                    territoriosAAtacar = new ArrayList<>();

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Choose a colour:");
                            break;
                        case "ES":
                            labelTxtField.setText("Elige un color:");
                            break;
                    }

                    coinGreen = new ImageView(new Image(new File("src/main/resources/img/coin_green.png").toURI().toString()));
                    coinGreen.setFitHeight(100);
                    coinGreen.setFitWidth(100);
                    coinRed = new ImageView(new Image(new File("src/main/resources/img/coin_red.png").toURI().toString()));
                    coinRed.setFitHeight(100);
                    coinRed.setFitWidth(100);
                    coinBlue = new ImageView(new Image(new File("src/main/resources/img/coin_blue.png").toURI().toString()));
                    coinBlue.setFitHeight(100);
                    coinBlue.setFitWidth(100);

                    containerCoins = new HBox(coinGreen, coinRed, coinBlue);
                    containerCoins.setAlignment(Pos.CENTER);
                    VBox.setMargin(containerCoins, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, containerCoins);

                    finalTerritoriosAAtacar = territoriosAAtacar;

                    coinHandler = MouseEvent -> {
                        if (MouseEvent.getSource().equals(coinGreen)) {
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if (!territorio.isBosque()) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                        } else if (MouseEvent.getSource().equals(coinRed)) {
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if (!territorio.isMontanna()) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                        } else {
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if (!territorio.isMar()) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                        }

                        boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                        for (ImageView img : mainController.cardsView) {
                            img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                        }

                        if (arquerasPuedenDisparar) {
                            //una vez decididos los territorios a atacar, se disparan las flechas
                            for (Territorio territorio : finalTerritoriosAAtacar) {
                                mainController.arqueras.dispararFlechas(territorio, mainController);
                            }
                            mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                            mainController.cuatroCartasActuales.set(i, null);
                            mainController.cardsView.get(i).setVisible(false);
                            MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                        } else {
                            System.out.println("Imposible disparar, no hay flechas suficientes.");
                            switch (Game.getLanguage()) {
                                case "EN":
                                    mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                    break;
                                case "ES":
                                    mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                    break;
                            }
                        }

                        ((Stage) btnConfirm.getScene().getWindow()).close();
                    };

                    coinGreen.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinRed.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinBlue.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    break;


                //dispara a los territorios que tengan al menos UNO de dos colores
                case "OR":
                    btnConfirm.setVisible(false);
                    territoriosAAtacar = new ArrayList<>();

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Choose a colour:");
                            break;
                        case "ES":
                            labelTxtField.setText("Elige un color:");
                            break;
                    }

                    coinGreen = new ImageView(new Image(new File("src/main/resources/img/coin_green.png").toURI().toString()));
                    coinGreen.setFitHeight(100);
                    coinGreen.setFitWidth(100);
                    coinRed = new ImageView(new Image(new File("src/main/resources/img/coin_red.png").toURI().toString()));
                    coinRed.setFitHeight(100);
                    coinRed.setFitWidth(100);
                    coinBlue = new ImageView(new Image(new File("src/main/resources/img/coin_blue.png").toURI().toString()));
                    coinBlue.setFitHeight(100);
                    coinBlue.setFitWidth(100);

                    containerCoins = new HBox(coinGreen, coinRed, coinBlue);
                    containerCoins.setAlignment(Pos.CENTER);
                    VBox.setMargin(containerCoins, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, containerCoins);

                    finalTerritoriosAAtacar = territoriosAAtacar;

                    coinHandler = MouseEvent -> {

                        if (mouseEventClicks.get() == 0) {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen1.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen1.set(true);
                            } else {
                                coinBlueChosen1.set(true);
                            }

                            switch (Game.getLanguage()) {
                                case "EN":
                                    labelTxtField.setText("Now choose a second colour:");
                                    break;
                                case "ES":
                                    labelTxtField.setText("Ahora elige un segundo color:");
                                    break;
                            }

                            mouseEventClicks.getAndIncrement();
                        } else {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen2.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen2.set(true);
                            } else {
                                coinBlueChosen2.set(true);
                            }

                            if (coinRedChosen1.get() || coinRedChosen2.get()) {
                                esMontanna.set(true);
                            }
                            if (coinGreenChosen1.get() || coinGreenChosen2.get()) {
                                esBosque.set(true);
                            }
                            if (coinBlueChosen1.get() || coinBlueChosen2.get()) {
                                esMar.set(true);
                            }
                            //reviso todos los territorios para ver cuáles tienen al menos UN color de los especificados
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if ((esMontanna.get() && esBosque.get()) && (territorio.isMontanna() || territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esMontanna.get() && esMar.get()) && (territorio.isMontanna() || territorio.isMar()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esBosque.get() && esMar.get()) && (territorio.isMar() || territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }

                                //por si el usuario coge las dos veces el mismo color
                                if (coinRedChosen1.get() && coinRedChosen2.get() && territorio.isMontanna() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinGreenChosen1.get() && coinGreenChosen2.get() && territorio.isBosque() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinBlueChosen1.get() && coinBlueChosen2.get() && territorio.isMar() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                //una vez decididos los territorios a atacar, se disparan las flechas
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }

                            ((Stage) btnConfirm.getScene().getWindow()).close();
                        }
                    };

                    coinGreen.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinRed.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinBlue.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    break;


                //dispara a los territorios que tengan DOS colores a la vez
                case "AND":
                    btnConfirm.setVisible(false);
                    territoriosAAtacar = new ArrayList<>();

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Choose a colour:");
                            break;
                        case "ES":
                            labelTxtField.setText("Elige un color:");
                            break;
                    }

                    coinGreen = new ImageView(new Image(new File("src/main/resources/img/coin_green.png").toURI().toString()));
                    coinGreen.setFitHeight(100);
                    coinGreen.setFitWidth(100);
                    coinRed = new ImageView(new Image(new File("src/main/resources/img/coin_red.png").toURI().toString()));
                    coinRed.setFitHeight(100);
                    coinRed.setFitWidth(100);
                    coinBlue = new ImageView(new Image(new File("src/main/resources/img/coin_blue.png").toURI().toString()));
                    coinBlue.setFitHeight(100);
                    coinBlue.setFitWidth(100);

                    containerCoins = new HBox(coinGreen, coinRed, coinBlue);
                    containerCoins.setAlignment(Pos.CENTER);
                    VBox.setMargin(containerCoins, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, containerCoins);

                    finalTerritoriosAAtacar = territoriosAAtacar;

                    coinHandler = MouseEvent -> {

                        if (mouseEventClicks.get() == 0) {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen1.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen1.set(true);
                            } else {
                                coinBlueChosen1.set(true);
                            }

                            switch (Game.getLanguage()) {
                                case "EN":
                                    labelTxtField.setText("Now choose a second colour:");
                                    break;
                                case "ES":
                                    labelTxtField.setText("Ahora elige un segundo color:");
                                    break;
                            }

                            mouseEventClicks.getAndIncrement();
                        } else {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen2.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen2.set(true);
                            } else {
                                coinBlueChosen2.set(true);
                            }

                            if (coinRedChosen1.get() || coinRedChosen2.get()) {
                                esMontanna.set(true);
                            }
                            if (coinGreenChosen1.get() || coinGreenChosen2.get()) {
                                esBosque.set(true);
                            }
                            if (coinBlueChosen1.get() || coinBlueChosen2.get()) {
                                esMar.set(true);
                            }
                            //reviso todos los territorios para ver cuáles tienen al menos UN color de los especificados
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if ((esMontanna.get() && esBosque.get()) && (territorio.isMontanna() && territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esMontanna.get() && esMar.get()) && (territorio.isMontanna() && territorio.isMar()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esBosque.get() && esMar.get()) && (territorio.isMar() && territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }

                                //por si el usuario coge las dos veces el mismo color
                                if (coinRedChosen1.get() && coinRedChosen2.get() && territorio.isMontanna() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinGreenChosen1.get() && coinGreenChosen2.get() && territorio.isBosque() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinBlueChosen1.get() && coinBlueChosen2.get() && territorio.isMar() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                //una vez decididos los territorios a atacar, se disparan las flechas
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }

                            ((Stage) btnConfirm.getScene().getWindow()).close();
                        }
                    };

                    coinGreen.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinRed.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinBlue.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    break;


                //dispara a los territorios cuyo nombre contenga X texto
                case "LIKE":
                    territoriosAAtacar = new ArrayList<>();
                    ArrayList<String> words = new ArrayList<>();
                    words.add("klif");
                    words.add("bek");
                    words.add("ae");
                    words.add("sten");
                    words.add("dal");
                    words.add("nes");
                    words.add("vik");
                    words.add("holm");
                    words.add("sand");

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Enter one of these segments:\nKlif, Bek, Ae, Sten, Dal, Nes, Vik, Holm, Sand");
                            break;
                        case "ES":
                            labelTxtField.setText("Escribe uno de estos segmentos:\nKlif, Bek, Ae, Sten, Dal, Nes, Vik, Holm, Sand");
                            break;
                    }
                    txtField = new TextField();
                    txtField.setMaxWidth(400);
                    txtField.setStyle("-fx-background-color: burlywood; -fx-border-color: grey; -fx-font-family: 'Lemon'; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                    VBox.setMargin(txtField, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, txtField);

                    finalTerritoriosAAtacar = territoriosAAtacar;
                    btnConfirm.setOnAction((event) -> {
                        if (!words.contains(txtField.getText().trim().toLowerCase())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            switch (Game.getLanguage()) {
                                case "EN":
                                    alert.setTitle("Not a valid text segment");
                                    alert.setHeaderText("Please enter a valid text");
                                    alert.setContentText("Please enter a valid text segment.");
                                    break;
                                case "ES":
                                    alert.setTitle("Segmento no válido");
                                    alert.setHeaderText("Introduce un texto válido");
                                    alert.setContentText("Por favor, introduce un segmento de texto válido.");
                                    break;
                            }
                            alert.showAndWait();
                        } else {
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if (territorio.getNombre().toLowerCase().contains(txtField.getText())) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                //una vez decididos los territorios a atacar, se disparan las flechas
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }

                            ((Stage) btnConfirm.getScene().getWindow()).close();
                        }
                    });
                    break;


                //dispara a los territorios que tengan solo UNO de dos colores
                case "XOR":
                    btnConfirm.setVisible(false);
                    territoriosAAtacar = new ArrayList<>();

                    labelTxtField = new Label();
                    labelTxtField.setFont(new Font("Lemon", 15));
                    labelTxtField.setStyle("-fx-font-family: 'Lemon'; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

                    switch (Game.getLanguage()) {
                        case "EN":
                            labelTxtField.setText("Choose a colour:");
                            break;
                        case "ES":
                            labelTxtField.setText("Elige un color:");
                            break;
                    }

                    coinGreen = new ImageView(new Image(new File("src/main/resources/img/coin_green.png").toURI().toString()));
                    coinGreen.setFitHeight(100);
                    coinGreen.setFitWidth(100);
                    coinRed = new ImageView(new Image(new File("src/main/resources/img/coin_red.png").toURI().toString()));
                    coinRed.setFitHeight(100);
                    coinRed.setFitWidth(100);
                    coinBlue = new ImageView(new Image(new File("src/main/resources/img/coin_blue.png").toURI().toString()));
                    coinBlue.setFitHeight(100);
                    coinBlue.setFitWidth(100);

                    containerCoins = new HBox(coinGreen, coinRed, coinBlue);
                    containerCoins.setAlignment(Pos.CENTER);
                    VBox.setMargin(containerCoins, new Insets(10, 0, 0, 0));

                    containerButtons.getChildren().addAll(labelTxtField, containerCoins);

                    finalTerritoriosAAtacar = territoriosAAtacar;

                    coinHandler = MouseEvent -> {

                        if (mouseEventClicks.get() == 0) {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen1.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen1.set(true);
                            } else {
                                coinBlueChosen1.set(true);
                            }

                            switch (Game.getLanguage()) {
                                case "EN":
                                    labelTxtField.setText("Now choose a second colour:");
                                    break;
                                case "ES":
                                    labelTxtField.setText("Ahora elige un segundo color:");
                                    break;
                            }

                            mouseEventClicks.getAndIncrement();
                        } else {
                            if (MouseEvent.getSource().equals(coinRed)) {
                                coinRedChosen2.set(true);
                            } else if (MouseEvent.getSource().equals(coinGreen)) {
                                coinGreenChosen2.set(true);
                            } else {
                                coinBlueChosen2.set(true);
                            }

                            if (coinRedChosen1.get() || coinRedChosen2.get()) {
                                esMontanna.set(true);
                            }
                            if (coinGreenChosen1.get() || coinGreenChosen2.get()) {
                                esBosque.set(true);
                            }
                            if (coinBlueChosen1.get() || coinBlueChosen2.get()) {
                                esMar.set(true);
                            }
                            //reviso todos los territorios para ver cuáles tienen al menos UN color de los especificados
                            for (Territorio territorio : mainController.listaTerritorios) {
                                if ((esMontanna.get() && esBosque.get()) && (territorio.isMontanna() ^ territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esMontanna.get() && esMar.get()) && (territorio.isMontanna() ^ territorio.isMar()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if ((esBosque.get() && esMar.get()) && (territorio.isMar() ^ territorio.isBosque()) && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }

                                //por si el usuario coge las dos veces el mismo color
                                if (coinRedChosen1.get() && coinRedChosen2.get() && territorio.isMontanna() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinGreenChosen1.get() && coinGreenChosen2.get() && territorio.isBosque() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                                if (coinBlueChosen1.get() && coinBlueChosen2.get() && territorio.isMar() && !finalTerritoriosAAtacar.contains(territorio)) {
                                    finalTerritoriosAAtacar.add(territorio);
                                }
                            }
                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                //una vez decididos los territorios a atacar, se disparan las flechas
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }

                            ((Stage) btnConfirm.getScene().getWindow()).close();
                        }
                    };

                    coinGreen.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinRed.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    coinBlue.addEventHandler(MouseEvent.MOUSE_CLICKED, coinHandler);
                    break;


                //dispara a un territorio concreto
                case "Bengala":
                    ((Stage) btnConfirm.getScene().getWindow()).close();

                    switch (Game.getLanguage()) {
                        case "EN":
                            mainController.log.setText(mainController.log.getText() + "\nClick on a territory on the board to order the archers to shoot there...");
                            break;
                        case "ES":
                            mainController.log.setText(mainController.log.getText() + "\nHaz clic sobre el tablero en un territorio para ordenar a las arqueras que disparen allí...");
                            break;
                    }

                    boardHandler = new EventHandler<>() {
                        @Override
                        public void handle(MouseEvent event) {
                            ArrayList<Territorio> finalTerritoriosAAtacar = new ArrayList<>();
                            finalTerritoriosAAtacar.add(mainController.listaTerritorios.get(mainController.stackTerritoriesList.indexOf(event.getSource())));

                            for (StackPane stack : mainController.stackTerritoriesList) {
                                stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                            }

                            boolean arquerasPuedenDisparar = mainController.arqueras.puedenDisparar(finalTerritoriosAAtacar);

                            for (ImageView img : mainController.cardsView) {
                                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
                            }

                            if (arquerasPuedenDisparar) {
                                for (Territorio territorio : finalTerritoriosAAtacar) {
                                    mainController.arqueras.dispararFlechas(territorio, mainController);
                                }
                                mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
                                mainController.cuatroCartasActuales.set(i, null);
                                mainController.cardsView.get(i).setVisible(false);
                                MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
                            } else {
                                System.out.println("Imposible disparar, no hay flechas suficientes.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        mainController.log.setText(mainController.log.getText() + "\n\nNot enough arrows to shoot.");
                                        break;
                                    case "ES":
                                        mainController.log.setText(mainController.log.getText() + "\n\nImposible disparar, no hay flechas suficientes.");
                                        break;
                                }
                            }
                        }
                    };

                    for (StackPane stack : mainController.stackTerritoriesList) {
                        stack.addEventHandler(MouseEvent.MOUSE_CLICKED, boardHandler);
                    }
                    break;
            }
        });


        //Botón "Descartar"
        btnDiscard.setOnAction((e) -> {
            ((Stage) (btnDiscard.getScene().getWindow())).close();
            switch (Game.getLanguage()) {
                case "EN":
                    mainController.log.setText(mainController.log.getText() + "\n\nCard discarded.");
                    break;
                case "ES":
                    mainController.log.setText(mainController.log.getText() + "\n\nCarta descartada.");
                    break;
            }
            for (ImageView img : mainController.cardsView) {
                img.addEventHandler(MouseEvent.MOUSE_CLICKED, mainController.cardHandler);
            }

            System.out.println("Carta descartada");
            mainController.mazoAccionDescartes.push(mainController.cuatroCartasActuales.get(i));
            mainController.cuatroCartasActuales.set(i, null);
            mainController.cardsView.get(i).setVisible(false);
            MainController.cardHandlerClicks.set(MainController.cardHandlerClicks.get() + 1);
        });
    }
}
