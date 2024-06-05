package com.gp.arqueras_javafx;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Clase principal con todas las instancias de las clases y métodos principales del transcurso del juego. Controla el tablero principal del juego.
 *
 * @author Manuel Valdepeñas
 * @version 2.2 24/03/2024
 */
public class MainController implements Initializable {
    //ATRIBUTOS

    //elementos del fxml
    @FXML
    public ImageView btnExit;
    @FXML
    public ImageView btnHelp;
    @FXML
    public Label txtArrows;
    @FXML
    public Label txtDiff;
    @FXML
    public Label txtRound;
    @FXML
    public Label txtArrowsDyn;
    @FXML
    public Label txtRemainingCards;
    @FXML
    public Label txtRemainingCardsDyn;
    @FXML
    public Label txtDiffDyn;
    @FXML
    public Label txtRoundDyn;
    @FXML
    public ImageView imgCoinRed;
    @FXML
    public ImageView imgCoinGreen;
    @FXML
    public ImageView imgCoinBlue;
    @FXML
    public ImageView imgCard1;
    @FXML
    public ImageView imgCard2;
    @FXML
    public ImageView imgCard3;
    @FXML
    public ImageView imgCard4;
    @FXML
    public ImageView imgHorde;
    @FXML
    public Text log;
    @FXML
    public VBox logContainer;
    @FXML
    public ScrollPane logScroll;
    @FXML
    public ImageView imgArchers;
    @FXML
    public StackPane stackKlifdalholm;
    @FXML
    public ImageView imgKlifdalholm;
    @FXML
    public StackPane stackKlifstenvik;
    @FXML
    public ImageView imgKlifstenvik;
    @FXML
    public StackPane stackBeknesvik;
    @FXML
    public ImageView imgBeknesvik;
    @FXML
    public StackPane stackBekstenholm;
    @FXML
    public ImageView imgBekstenholm;
    @FXML
    public StackPane stackBekdalsand;
    @FXML
    public ImageView imgBekdalsand;
    @FXML
    public StackPane stackAestensand;
    @FXML
    public ImageView imgAestensand;
    @FXML
    public StackPane stackAenesholm;
    @FXML
    public ImageView imgAenesholm;
    @FXML
    public ImageView imgHordeBlank;
    @FXML
    public ImageView imgCardBlank;
    @FXML
    public HBox containerCoins;
    @FXML
    public Label txtKlifdalholmOrcs;
    @FXML
    public Label txtKlifdalholmKnights;
    @FXML
    public Label txtBekdalsandOrcs;
    @FXML
    public Label txtBekdalsandKnights;
    @FXML
    public Label txtKlifstenvikOrcs;
    @FXML
    public Label txtKlifstenvikKnights;
    @FXML
    public Label txtBeknesvikOrcs;
    @FXML
    public Label txtBeknesvikKnights;
    @FXML
    public Label txtBekstenholmOrcs;
    @FXML
    public Label txtBekstenholmKnights;
    @FXML
    public Label txtAestensandOrcs;
    @FXML
    public Label txtAestensandKnights;
    @FXML
    public Label txtAenesholmOrcs;
    @FXML
    public Label txtAenesholmKnights;

    //variables que no son del fxml, pero afectan a la interfaz
    public ArrayList<ImageView> cardsView;
    public ImageView[] coinsView = new ImageView[3];
    EventHandler<MouseEvent> cardHandler = null;
    EventHandler<MouseEvent> hordeHandler = null;
    EventHandler<MouseEvent> boardHandler = null;
    EventHandler<MouseEvent> boardHandler2 = null;
    public static int countHordePressed;
    public static int vecesNingunaCara;
    public ArrayList<StackPane> stackTerritoriesList;


    //instancias del juego
    private Territorio klifstenvik = null;
    private Territorio klifdalholm = null;
    private Territorio beknesvik = null;
    private Territorio aenesholm = null;
    private Territorio aestensand = null;
    private Territorio bekdalsand = null;
    private Territorio bekstenholm = null;
    private final Moneda monedaBosque = new Moneda("Bosque");
    private final Moneda monedaMontanna = new Moneda("Montaña");
    private final Moneda monedaMar = new Moneda("Mar");
    private final Moneda[] setMonedas = {monedaBosque, monedaMontanna, monedaMar};
    //JavaFX no tiene stacks observables, he implementado esta clase para ello: https://gist.github.com/smac89/7bc52fd5749247cfa2e9
    public ObservableStack<CartaAccion> mazoAccion = new ObservableStack<>();
    //public Stack<CartaAccion> mazoAccion = new Stack<>();
    public IntegerBinding mazoAccionSizeProperty = Bindings.size(mazoAccion);
    public Stack<CartaAccion> mazoAccionDescartes = new Stack<>();
    public ObservableList<CartaAccion> cuatroCartasActuales = FXCollections.observableArrayList();
    public Stack<CartaHorda> mazoHordas = new Stack<>();
    public CartaArquera arqueras;
    public ArrayList<Territorio> listaTerritorios;
    private Game currentGame = null;
    static IntegerProperty cardHandlerClicks = new SimpleIntegerProperty();
    private boolean fullDeck;
    private static IntegerProperty destroyedTerritories = new SimpleIntegerProperty();


    //MÉTODOS

    /**
     * Inicializa las listas principales y algunos listeners, actualiza los textos según el idioma
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshTextsMain();

        cardsView = new ArrayList<>();
        cardsView.add(imgCard1);
        cardsView.add(imgCard2);
        cardsView.add(imgCard3);
        cardsView.add(imgCard4);

        coinsView[0] = imgCoinGreen;
        coinsView[1] = imgCoinRed;
        coinsView[2] = imgCoinBlue;

        //listener para que, cuando se añada texto al log, el scroll se vaya automáticamente hacia abajo
        logContainer.heightProperty().addListener((ChangeListener) (observableValue, o, t1) -> logScroll.setVvalue((Double) t1));

        //listener para que, cuando el mazo de acción se quede vacío, se rellene con el mazo de descartes y se mezcle
        mazoAccion.addListener((ListChangeListener<CartaAccion>) change -> {
            if (change.getList().isEmpty()) {

                switch (Game.getLanguage()) {
                    case "EN":
                        log.setText(log.getText() + "\n\nThe action cards stack has been replenished.");
                        break;
                    case "ES":
                        log.setText(log.getText() + "\n\nSe ha repuesto el mazo de cartas de acción.");
                        break;
                }

                for (CartaAccion descarte : mazoAccionDescartes) {
                    mazoAccion.push(descarte);
                }
                mazoAccionDescartes.clear();
                Collections.shuffle(mazoAccion);
            }
        });

        //listener para detectar cuándo se tiene una mano de 4 cartas
        cuatroCartasActuales.addListener((ListChangeListener<CartaAccion>) change -> {
            int nullCounter = 0;
            if (change.getList().size() == 4 && !fullDeck) {
                //no cuenta como una mano completa si alguna de las cartas es nula (es decir, se ha usado y no se ha reemplazado aún)
                for (CartaAccion carta : cuatroCartasActuales) {
                    if (carta == null) {
                        nullCounter++;
                        break;
                    }
                }
                if (nullCounter == 0) {
                    fullDeck = true;
                    imgCardBlank.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardHandler);
                    PauseTransition pause5 = new PauseTransition(Duration.seconds(1));
                    pause5.setOnFinished((e5) -> {

                        //se coloca un caballero donde se quiera
                        enviarCaballeros();
                    });
                    pause5.play();
                }
            }
        });

        //listener para comprobar la cantidad de territorios destruidos
        destroyedTerritories.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 4) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("gameover_interface.fxml"));
                    final Stage dialog = new Stage();
                    switch (Game.getLanguage()) {
                        case "EN":
                            dialog.setTitle("Game Over");
                            break;
                        case "ES":
                            dialog.setTitle("Fin de la partida");
                            break;
                    }
                    Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
                    dialog.setScene(dialogScene);
                    dialog.setResizable(false);
                    dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
                    dialog.show();

                    GameOverController controller = fxmlLoader.getController();

                    controller.setMainController(this);
                    controller.gameOverLose();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

        //listener para comprobar cuántas cartas de acción se han usado
        cardHandlerClicks.addListener((observable, oldValue, newValue) -> {
            //si se han sumado dos clicks en las cartas de acción, es que se han usado dos cartas
            if (newValue.intValue() == 2) {
                switch (Game.getLanguage()) {
                    case "EN":
                        log.setText(log.getText() + "\n\nYou have used your 2 cards. Let the melee battle commence!");
                        break;
                    case "ES":
                        log.setText(log.getText() + "\n\nHas usado tus 2 cartas. ¡Que comience la batalla cuerpo a cuerpo!");
                        break;
                }

                for (ImageView img : cardsView) {
                    img.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardHandler);
                }

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished((e) -> {
                    for (Territorio terr : listaTerritorios) {
                        if (terr.getCantidadOrcos() > 0 && terr.getCantidadGuerreros() > 0) {
                            terr.pelea(this);
                        }
                    }

                    switch (Game.getLanguage()) {
                        case "EN":
                            log.setText(log.getText() + "\n\nBattle complete. Round finished.");
                            break;
                        case "ES":
                            log.setText(log.getText() + "\n\nBatalla terminada. Ronda finalizada.");
                            break;
                    }

                    System.out.println("Fin de ronda");

                    PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
                    pause2.setOnFinished(e2 -> {
                        if (currentGame.getDifficulty().equalsIgnoreCase("easy") && currentGame.getCurrentRound() == 10) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("gameover_interface.fxml"));
                                final Stage dialog = new Stage();
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        dialog.setTitle("Game Over");
                                        break;
                                    case "ES":
                                        dialog.setTitle("Fin de la partida");
                                        break;
                                }
                                Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
                                dialog.setScene(dialogScene);
                                dialog.setResizable(false);
                                dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
                                dialog.show();

                                GameOverController controller = fxmlLoader.getController();

                                controller.setMainController(this);
                                controller.gameOverWin();
                            } catch (IOException exc) {
                                System.out.println(exc);
                            }
                        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal") && currentGame.getCurrentRound() == 12) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("gameover_interface.fxml"));
                                final Stage dialog = new Stage();
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        dialog.setTitle("Game Over");
                                        break;
                                    case "ES":
                                        dialog.setTitle("Fin de la partida");
                                        break;
                                }
                                Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
                                dialog.setScene(dialogScene);
                                dialog.setResizable(false);
                                dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
                                dialog.show();

                                GameOverController controller = fxmlLoader.getController();

                                controller.setMainController(this);
                                controller.gameOverWin();
                            } catch (IOException exc) {
                                System.out.println(exc);
                            }
                        } else if (currentGame.getDifficulty().equalsIgnoreCase("hard") && currentGame.getCurrentRound() == 13) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("gameover_interface.fxml"));
                                final Stage dialog = new Stage();
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        dialog.setTitle("Game Over");
                                        break;
                                    case "ES":
                                        dialog.setTitle("Fin de la partida");
                                        break;
                                }
                                Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
                                dialog.setScene(dialogScene);
                                dialog.setResizable(false);
                                dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
                                dialog.show();

                                GameOverController controller = fxmlLoader.getController();

                                controller.setMainController(this);
                                controller.gameOverWin();
                            } catch (IOException exc) {
                                System.out.println(exc);
                            }
                        } else {
                            currentGame.setCurrentRound(currentGame.getCurrentRound() + 1);
                            startRound();
                        }
                    });
                    pause2.play();
                });
                pause.play();
            }
        });
    }

    /**
     * Método que actualiza los textos iniciales de la pantalla principal del juego en función del idioma escogido
     */
    @FXML
    public void refreshTextsMain() {
        switch (Game.getLanguage()) {
            case "EN":
                txtDiff.setText("Difficulty: ");
                txtRound.setText("Round: ");
                break;
            case "ES":
                txtDiff.setText("Dificultad: ");
                txtRound.setText("Ronda: ");
                break;
        }
    }

    /**
     * Crea una nueva partida y desata todo el flujo de ejecución
     *
     * @param partida La partida que se creó en la ventana de dificultad, para poder almacenarla aquí
     * @author Manuel Valdepeñas
     */
    @FXML
    public void newGame(Game partida) {
        currentGame = partida;

        switch (Game.getLanguage()) {
            case "EN":
                log.setText("Welcome to Archers of Nand! " +
                        "You can get help by clicking the upper left button. " +
                        "We hope you enjoy the game! The game will begin in 5 seconds. Get ready!");
                break;
            case "ES":
                log.setText("¡Te damos la bienvenida a Arqueras de Nand! " +
                        "Puedes consultar la ayuda en el botón de la esquina superior izquierda. " +
                        "¡Esperamos que disfrutes del juego! La partida comenzará en 5 segundos. ¡Prepárate!");
                break;
        }

        //instanciar las arqueras en función de la dificultad
        if (currentGame.getDifficulty().equalsIgnoreCase("easy")) {
            arqueras = new CartaArquera(50, 1);
            switch (Game.getLanguage()) {
                case "EN":
                    txtDiffDyn.setText("Easy");
                    break;
                case "ES":
                    txtDiffDyn.setText("Fácil");
                    break;
            }
        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal")) {
            arqueras = new CartaArquera(48, 2);
            switch (Game.getLanguage()) {
                case "EN":
                    txtDiffDyn.setText("Normal");
                    break;
                case "ES":
                    txtDiffDyn.setText("Normal");
                    break;
            }
        } else if (currentGame.getDifficulty().equalsIgnoreCase("hard")) {
            arqueras = new CartaArquera(45, 3);
            switch (Game.getLanguage()) {
                case "EN":
                    txtDiffDyn.setText("Hard");
                    break;
                case "ES":
                    txtDiffDyn.setText("Difícil");
                    break;
            }
        }

        txtArrowsDyn.textProperty().bind(arqueras.flechasProperty().asString());
        txtRemainingCardsDyn.textProperty().bind(mazoAccionSizeProperty.asString());

        //rellenar el mazo de hordas en función de la dificultad
        if (currentGame.getDifficulty().equalsIgnoreCase("easy")) {
            for (int i = 0; i < 5; i++) {
                CartaHorda carta = new CartaHorda(3);
                mazoHordas.push(carta);
            }
            for (int i = 0; i < 5; i++) {
                CartaHorda carta = new CartaHorda(4);
                mazoHordas.push(carta);
            }
        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal")) {
            for (int i = 0; i < 4; i++) {
                CartaHorda carta = new CartaHorda(3);
                mazoHordas.push(carta);
            }
            for (int i = 0; i < 4; i++) {
                CartaHorda carta = new CartaHorda(4);
                mazoHordas.push(carta);
            }
            for (int i = 0; i < 4; i++) {
                CartaHorda carta = new CartaHorda(5);
                mazoHordas.push(carta);
            }
        } else if (currentGame.getDifficulty().equalsIgnoreCase("hard")) {
            for (int i = 0; i < 5; i++) {
                CartaHorda carta = new CartaHorda(3);
                mazoHordas.push(carta);
            }
            for (int i = 0; i < 5; i++) {
                CartaHorda carta = new CartaHorda(4);
                mazoHordas.push(carta);
            }
            for (int i = 0; i < 5; i++) {
                CartaHorda carta = new CartaHorda(5);
                mazoHordas.push(carta);
            }
        }

        if (currentGame.getDifficulty().equalsIgnoreCase("easy")) {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/10");
        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal")) {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/12");
        } else {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/15");
        }

        klifdalholm = new Territorio("Klifdalholm", true, true, false, new ArrayList<>());
        klifstenvik = new Territorio("Klifstenvik", false, true, false, new ArrayList<>());
        beknesvik = new Territorio("Beknesvik", false, true, true, new ArrayList<>());
        aenesholm = new Territorio("Aenesholm", false, false, true, new ArrayList<>());
        aestensand = new Territorio("Aestensand", true, false, true, new ArrayList<>());
        bekdalsand = new Territorio("Bekdalsand", true, false, false, new ArrayList<>());
        bekstenholm = new Territorio("Bekstenholm", true, true, true, new ArrayList<>());

        //relleno los territorios adyacentes de cada territorio
        klifdalholm.getTerritoriosAdyacentes().add(klifstenvik);
        klifdalholm.getTerritoriosAdyacentes().add(bekstenholm);
        klifdalholm.getTerritoriosAdyacentes().add(bekdalsand);
        klifstenvik.getTerritoriosAdyacentes().add(klifdalholm);
        klifstenvik.getTerritoriosAdyacentes().add(beknesvik);
        klifstenvik.getTerritoriosAdyacentes().add(bekstenholm);
        beknesvik.getTerritoriosAdyacentes().add(klifstenvik);
        beknesvik.getTerritoriosAdyacentes().add(bekstenholm);
        beknesvik.getTerritoriosAdyacentes().add(aenesholm);
        aenesholm.getTerritoriosAdyacentes().add(beknesvik);
        aenesholm.getTerritoriosAdyacentes().add(bekstenholm);
        aenesholm.getTerritoriosAdyacentes().add(aestensand);
        aestensand.getTerritoriosAdyacentes().add(aenesholm);
        aestensand.getTerritoriosAdyacentes().add(bekstenholm);
        aestensand.getTerritoriosAdyacentes().add(bekdalsand);
        bekdalsand.getTerritoriosAdyacentes().add(klifdalholm);
        bekdalsand.getTerritoriosAdyacentes().add(bekstenholm);
        bekdalsand.getTerritoriosAdyacentes().add(aestensand);
        bekstenholm.getTerritoriosAdyacentes().add(klifstenvik);
        bekstenholm.getTerritoriosAdyacentes().add(bekdalsand);
        bekstenholm.getTerritoriosAdyacentes().add(klifdalholm);
        bekstenholm.getTerritoriosAdyacentes().add(beknesvik);
        bekstenholm.getTerritoriosAdyacentes().add(aenesholm);
        bekstenholm.getTerritoriosAdyacentes().add(aestensand);

        listaTerritorios = new ArrayList<>();
        listaTerritorios.add(klifdalholm);
        listaTerritorios.add(klifstenvik);
        listaTerritorios.add(beknesvik);
        listaTerritorios.add(aenesholm);
        listaTerritorios.add(aestensand);
        listaTerritorios.add(bekdalsand);
        listaTerritorios.add(bekstenholm);

        stackTerritoriesList = new ArrayList<>();
        stackTerritoriesList.add(stackKlifdalholm);
        stackTerritoriesList.add(stackKlifstenvik);
        stackTerritoriesList.add(stackBeknesvik);
        stackTerritoriesList.add(stackAenesholm);
        stackTerritoriesList.add(stackAestensand);
        stackTerritoriesList.add(stackBekdalsand);
        stackTerritoriesList.add(stackBekstenholm);

        txtKlifdalholmOrcs.textProperty().bind(klifdalholm.cantidadOrcosProperty().asString());
        txtKlifdalholmKnights.textProperty().bind(klifdalholm.cantidadGuerrerosProperty().asString());
        txtBekdalsandOrcs.textProperty().bind(bekdalsand.cantidadOrcosProperty().asString());
        txtBekdalsandKnights.textProperty().bind(bekdalsand.cantidadGuerrerosProperty().asString());
        txtKlifstenvikOrcs.textProperty().bind(klifstenvik.cantidadOrcosProperty().asString());
        txtKlifstenvikKnights.textProperty().bind(klifstenvik.cantidadGuerrerosProperty().asString());
        txtBeknesvikOrcs.textProperty().bind(beknesvik.cantidadOrcosProperty().asString());
        txtBeknesvikKnights.textProperty().bind(beknesvik.cantidadGuerrerosProperty().asString());
        txtBekstenholmOrcs.textProperty().bind(bekstenholm.cantidadOrcosProperty().asString());
        txtBekstenholmKnights.textProperty().bind(bekstenholm.cantidadGuerrerosProperty().asString());
        txtAestensandOrcs.textProperty().bind(aestensand.cantidadOrcosProperty().asString());
        txtAestensandKnights.textProperty().bind(aestensand.cantidadGuerrerosProperty().asString());
        txtAenesholmOrcs.textProperty().bind(aenesholm.cantidadOrcosProperty().asString());
        txtAenesholmKnights.textProperty().bind(aenesholm.cantidadGuerrerosProperty().asString());

        for (Territorio terr : listaTerritorios) {
            //si el territorio tiene 3 o más orcos, es destruido y no puede recuperarse
            terr.cantidadOrcosProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() >= 3) {
                    if (!terr.isDestruido()) {
                        terr.setDestruido(true);
                        destroyedTerritories.setValue(destroyedTerritories.getValue()+1);
                        System.out.println("¡" + terr.getNombre() + " ha sido destruido!");
                        switch (Game.getLanguage()) {
                            case "EN":
                                log.setText(log.getText() + "\n" + terr.getNombre() + " has been destroyed!");
                                break;
                            case "ES":
                                log.setText(log.getText() + "\n¡" + terr.getNombre() + " ha sido destruido!");
                                break;
                        }
                        switch (terr.getNombre()) {
                            case "Klifdalholm":
                                imgKlifdalholm.setImage(new Image(new File("src/main/resources/img/klifdalholm_dest.png").toURI().toString()));
                                break;
                            case "Klifstenvik":
                                imgKlifstenvik.setImage(new Image(new File("src/main/resources/img/klifstenvik_dest.png").toURI().toString()));
                                break;
                            case "Beknesvik":
                                imgBeknesvik.setImage(new Image(new File("src/main/resources/img/beknesvik_dest.png").toURI().toString()));
                                break;
                            case "Aenesholm":
                                imgAenesholm.setImage(new Image(new File("src/main/resources/img/aenesholm_dest.png").toURI().toString()));
                                break;
                            case "Aestensand":
                                imgAestensand.setImage(new Image(new File("src/main/resources/img/aestensand_dest.png").toURI().toString()));
                                break;
                            case "Bekdalsand":
                                imgBekdalsand.setImage(new Image(new File("src/main/resources/img/bekdalsand_dest.png").toURI().toString()));
                                break;
                            case "Bekstenholm":
                                imgBekstenholm.setImage(new Image(new File("src/main/resources/img/bekstenholm_dest.png").toURI().toString()));
                                break;
                        }
                    }
                }
            });
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished((e1) -> {
            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\n\nOrganising and shuffling the decks...");
                    break;
                case "ES":
                    log.setText(log.getText() + "\n\nOrganizando y barajando los mazos...");
                    break;
            }

            PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
            pause2.setOnFinished((e2) -> {
                //rellenar el mazo de cartas de acción
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("OR"));
                }
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("AND"));
                }
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("LIKE"));
                }
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("XOR"));
                }
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("COUNT"));
                }
                for (int j = 0; j < 5; j++) {
                    mazoAccion.push(new CartaAccion("NOT"));
                }
                for (int j = 0; j < 3; j++) {
                    mazoAccion.push(new CartaAccion("Bengala"));
                }

                //se barajan ambos mazos
                Collections.shuffle(mazoHordas);
                Collections.shuffle(mazoAccion);

                //COMIENZA LA PARTIDA
                //estado inicial
                System.out.println("Estado inicial de la partida (" + currentGame.getDifficulty() + "):\nTerritorios:");
                for (Territorio territorio : listaTerritorios) {
                    System.out.println(territorio);
                }
                System.out.println("Hordas restantes: " + mazoHordas.size());
                System.out.println("Flechas restantes: " + arqueras.getCantidadFlechas());

                startRound();
            });
            pause2.play();

        });
        pause.play();
    }

    /**
     * Inicio de ronda
     */
    public void startRound() {
        if (currentGame.getDifficulty().equalsIgnoreCase("easy")) {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/10");
        } else if (currentGame.getDifficulty().equalsIgnoreCase("normal")) {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/12");
        } else {
            txtRoundDyn.setText(currentGame.getCurrentRound() + "/15");
        }

        imgArchers.setImage(new Image(new File("src/main/resources/img/archers.png").toURI().toString()));
        fullDeck = false;
        switch (Game.getLanguage()) {
            case "EN":
                log.setText(log.getText() + "\n\nYou need 4 action cards in your hand. Click the action stack to take a card.");
                break;
            case "ES":
                log.setText(log.getText() + "\n\nNecesitas 4 cartas de acción en tu mano. Pulsa el mazo de acción para sacar una carta.");
                break;
        }

        cardHandler = event -> {
            if (MouseEvent.MOUSE_CLICKED.equals(event.getEventType())) {
                //se sacan cartas de acción hasta que haya 4 en la "mesa" (se ha hecho shuffle antes, así que ya son aleatorias)
                //que haya 4 se controla mediante un listener, javafx no permite cambiar elementos de la interfaz y hacer transiciones dentro de bucles
                CartaAccion cartaSacada = mazoAccion.pop();
                if (cuatroCartasActuales.size() < 4) {
                    cuatroCartasActuales.add(cartaSacada);
                    cardsView.get(cuatroCartasActuales.size() - 1).setVisible(true);

                    switch (cartaSacada.getTipo()) {
                        case "AND":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_and.png").toURI().toString()));
                            break;
                        case "OR":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_or.png").toURI().toString()));
                            break;
                        case "XOR":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_xor.png").toURI().toString()));
                            break;
                        case "COUNT":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_count.png").toURI().toString()));
                            break;
                        case "Bengala":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_flare.png").toURI().toString()));
                            break;
                        case "LIKE":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_like.png").toURI().toString()));
                            break;
                        case "NOT":
                            cardsView.get(cuatroCartasActuales.size() - 1).setImage(new Image(new File("src/main/resources/img/card_not.png").toURI().toString()));
                            break;
                    }
                } else {
                    //si ya hay cartas, recorro la mano para ver cuales se han usado y reemplazarlas por cartas nuevas
                    for (ImageView img : cardsView) {
                        if (!img.isVisible()) {
                            cuatroCartasActuales.set(cardsView.indexOf(img), cartaSacada);
                            cardsView.get(cardsView.indexOf(img)).setVisible(true);

                            switch (cartaSacada.getTipo()) {
                                case "AND":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_and.png").toURI().toString()));
                                    break;
                                case "OR":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_or.png").toURI().toString()));
                                    break;
                                case "XOR":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_xor.png").toURI().toString()));
                                    break;
                                case "COUNT":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_count.png").toURI().toString()));
                                    break;
                                case "Bengala":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_flare.png").toURI().toString()));
                                    break;
                                case "LIKE":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_like.png").toURI().toString()));
                                    break;
                                case "NOT":
                                    cardsView.get(cardsView.indexOf(img)).setImage(new Image(new File("src/main/resources/img/card_not.png").toURI().toString()));
                                    break;
                            }
                            break;
                        }
                    }
                }
                System.out.println("Carta de acción sacada: " + cartaSacada.getTipo());
                log.setText(log.getText() + "\n" + cartaSacada.getTipo());
            }
        };
        imgCardBlank.addEventHandler(MouseEvent.MOUSE_CLICKED, cardHandler);
    }

    /**
     * Método que se ejecutará al principio de cada ronda. El jugador podrá elegir a qué territorio del mapa enviar una ficha de guerrero
     *
     * @param territorio El territorio elegido por el jugador para enviar un guerrero
     * @author Manuel Valdepeñas
     */
    public void colocarGuerreros(Territorio territorio) {
        if (territorio.isDestruido()) {
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
            territorio.setCantidadGuerreros(territorio.getCantidadGuerreros() + 1);
            System.out.println("Guerrero enviado a " + territorio.getNombre() + ". Guerreros en " + territorio.getNombre() + ": " + territorio.getCantidadGuerreros());
            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\nOne knight sent to " + territorio.getNombre() + ".");
                    break;
                case "ES":
                    log.setText(log.getText() + "\nUn guerrero enviado a " + territorio.getNombre() + ".");
                    break;
            }

            for (StackPane stack : stackTerritoriesList) {
                stack.removeEventHandler(MouseEvent.MOUSE_CLICKED, boardHandler);
            }

            sacarCartaHorda();
        }
    }

    /**
     * Método que saca una carta de horda y actualiza la imagen
     */
    public void sacarCartaHorda() {
        //se saca una carta de horda y se realiza el sorteo (véase el método colocarOrcos más abajo)
        switch (Game.getLanguage()) {
            case "EN":
                log.setText(log.getText() + "\n\nTaking a horde card...");
                break;
            case "ES":
                log.setText(log.getText() + "\n\nSacando carta de horda...");
                break;
        }
        PauseTransition pause6 = new PauseTransition(Duration.seconds(2));
        pause6.setOnFinished((e6) -> {
            CartaHorda hordaSacada = mazoHordas.pop();
            if (mazoHordas.isEmpty()) {
                imgHordeBlank.setVisible(false);
            }
            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\nHorde: " + hordaSacada.getNumeroOrcos());
                    break;
                case "ES":
                    log.setText(log.getText() + "\nHorda: " + hordaSacada.getNumeroOrcos());
                    break;
            }

            switch (hordaSacada.getNumeroOrcos()) {
                case 3:
                    imgHorde.setImage(new Image(new File("src/main/resources/img/card_horde_3.png").toURI().toString()));
                    break;
                case 4:
                    imgHorde.setImage(new Image(new File("src/main/resources/img/card_horde_4.png").toURI().toString()));
                    break;
                case 5:
                    imgHorde.setImage(new Image(new File("src/main/resources/img/card_horde_5.png").toURI().toString()));
                    break;
            }
            imgHorde.setVisible(true);

            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\n\nEach orc from the card has to go to a territory. " +
                            "Click on the horde card you have taken to flip the coins for each orc.");
                    break;
                case "ES":
                    log.setText(log.getText() + "\n\nCada orco de la carta debe dirigirse a un territorio. " +
                            "Pulsa la carta de horda que ha salido para lanzar las monedas para cada orco.");
                    break;
            }

            colocarOrcos(hordaSacada);
        });
        pause6.play();
    }

    /**
     * Método que se ejecutará al principio de cada ronda. Por cada orco de la carta que haya salido, se lanzarán las tres monedas y, en función de lo que salga, se enviará cada orco a un territorio determinado
     *
     * @param carta La carta que se ha cogido del mazo (se da por hecho que esa carta se descarta en cuanto se usa)
     * @author Manuel Valdepeñas
     */
    public void colocarOrcos(CartaHorda carta) {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished((e) -> {
            //por cada terreno que tenga cuatro o más orcos, se añade un orco más a la carta
            for (Territorio territorio : listaTerritorios) {
                if (territorio.getCantidadOrcos() >= 4) {
                    switch (Game.getLanguage()) {
                        case "EN":
                            log.setText(log.getText() + "\nThe territory " + territorio.getNombre() + " has 4 or more orcs. One more orc has been added to the horde.");
                            break;
                        case "ES":
                            log.setText(log.getText() + "\nEl territorio " + territorio.getNombre() + " tiene 4 o más orcos. Se ha añadido un orco más a la horda.");
                            break;
                    }
                    carta.setNumeroOrcos(carta.getNumeroOrcos() + 1);
                }
            }

            vecesNingunaCara = 0;
            countHordePressed = 0;

            hordeHandler = mouseEvent -> {
                boolean moneda1 = setMonedas[0].lanzarMoneda();
                boolean moneda2 = setMonedas[1].lanzarMoneda();
                boolean moneda3 = setMonedas[2].lanzarMoneda();

                if (moneda1) {
                    coinsView[0].setImage(new Image(new File("src/main/resources/img/coin_green.png").toURI().toString()));
                } else {
                    coinsView[0].setImage(new Image(new File("src/main/resources/img/coin_reverse.png").toURI().toString()));
                }

                if (moneda2) {
                    coinsView[1].setImage(new Image(new File("src/main/resources/img/coin_red.png").toURI().toString()));
                } else {
                    coinsView[1].setImage(new Image(new File("src/main/resources/img/coin_reverse.png").toURI().toString()));
                }

                if (moneda3) {
                    coinsView[2].setImage(new Image(new File("src/main/resources/img/coin_blue.png").toURI().toString()));
                } else {
                    coinsView[2].setImage(new Image(new File("src/main/resources/img/coin_reverse.png").toURI().toString()));
                }

                if (setMonedas[0].isCaraOCruz() && !setMonedas[1].isCaraOCruz() && !setMonedas[2].isCaraOCruz()) {
                    //0 true
                    carta.horda(bekdalsand, this);
                } else if (setMonedas[0].isCaraOCruz() && setMonedas[1].isCaraOCruz() && !setMonedas[2].isCaraOCruz()) {
                    //0, 1 true
                    carta.horda(klifdalholm, this);
                } else if (setMonedas[0].isCaraOCruz() && !setMonedas[1].isCaraOCruz() && setMonedas[2].isCaraOCruz()) {
                    //0, 2 true
                    carta.horda(aestensand, this);
                } else if (!setMonedas[0].isCaraOCruz() && setMonedas[1].isCaraOCruz() && !setMonedas[2].isCaraOCruz()) {
                    //1 true
                    carta.horda(klifstenvik, this);
                } else if (!setMonedas[0].isCaraOCruz() && setMonedas[1].isCaraOCruz() && setMonedas[2].isCaraOCruz()) {
                    //1, 2 true
                    carta.horda(beknesvik, this);
                } else if (!setMonedas[0].isCaraOCruz() && !setMonedas[1].isCaraOCruz() && setMonedas[2].isCaraOCruz()) {
                    //2 true
                    carta.horda(aenesholm, this);
                } else if (setMonedas[0].isCaraOCruz() && setMonedas[1].isCaraOCruz() && setMonedas[2].isCaraOCruz()) {
                    //0, 1, 2 true
                    carta.horda(bekstenholm, this);
                } else {
                    imgArchers.setImage(new Image(new File("src/main/resources/img/archers_reverse.png").toURI().toString()));
                    //ninguno true, las arqueras huyen y pierden flechas (solo la primera vez que pasa)
                    if (vecesNingunaCara == 0) {
                        if (arqueras.getCantidadFlechas() <= 0) {
                            System.out.println("Los orcos han atacado a las arqueras. Las arqueras no tienen flechas que perder en su huida.");
                            switch (Game.getLanguage()) {
                                case "EN":
                                    log.setText(log.getText() + "\nThe orcs have attacked the archers. The archers don't have any arrows to lose on their retreat.");
                                    break;
                                case "ES":
                                    log.setText(log.getText() + "\nLos orcos han atacado a las arqueras. Las arqueras no tienen flechas que perder en su huida.");
                                    break;
                            }
                        } else {
                            arqueras.setCantidadFlechas(arqueras.getCantidadFlechas() - arqueras.getCosteHuida());
                            if (arqueras.getCosteHuida() == 1) {
                                System.out.println("Los orcos han atacado a las arqueras. Las arqueras pierden " + arqueras.getCosteHuida() + " flecha en su huida.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        log.setText(log.getText() + "\nThe orcs have attacked the archers. The archers lose " + arqueras.getCosteHuida() + " arrow on their retreat.");
                                        break;
                                    case "ES":
                                        log.setText(log.getText() + "\nLos orcos han atacado a las arqueras. Las arqueras pierden " + arqueras.getCosteHuida() + " flecha en su huida.");
                                        break;
                                }
                            } else {
                                System.out.println("Los orcos han atacado a las arqueras. Las arqueras pierden " + arqueras.getCosteHuida() + " flechas en su huida.");
                                switch (Game.getLanguage()) {
                                    case "EN":
                                        log.setText(log.getText() + "\nThe orcs have attacked the archers. The archers lose " + arqueras.getCosteHuida() + " arrows on their retreat.");
                                        break;
                                    case "ES":
                                        log.setText(log.getText() + "\nLos orcos han atacado a las arqueras. Las arqueras pierden " + arqueras.getCosteHuida() + " flechas en su huida.");
                                        break;
                                }
                            }
                        }
                    } else {
                        switch (Game.getLanguage()) {
                            case "EN":
                                log.setText(log.getText() + "\nThe orcs try to attack the archers again, but they have already retreated.");
                                break;
                            case "ES":
                                log.setText(log.getText() + "\nLos orcos intentan atacar a las arqueras de nuevo, pero ya han huido.");
                                break;
                        }
                    }
                    vecesNingunaCara++;
                }

                countHordePressed++;

                if (countHordePressed == carta.getNumeroOrcos()) {
                    imgHorde.removeEventHandler(MouseEvent.MOUSE_CLICKED, hordeHandler);
                    imgHorde.setVisible(false);

                    //ahora sí, comienza la batalla
                    //primero, el jugador debe decidir qué dos cartas de acción usar y cómo
                    elegirCarta();
                }
            };
            imgHorde.addEventHandler(MouseEvent.MOUSE_CLICKED, hordeHandler);
        });
        pause.play();
    }

    /**
     * Método que se ejecutará antes de cada batalla cuerpo a cuerpo. El jugador debe elegir una carta y, con ella, decidir si usarla para mover un caballero (decidiendo después de dónde a dónde moverlo), usarla para llamar a las arqueras según el tipo de carta, o descartarla directamente.
     *
     * @author Manuel Valdepeñas
     */
    public void elegirCarta() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished((e1) -> {
            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\n\nKnights and orcs in place. You can now use 2 of your action cards. " +
                            "Click on any of them to use it.");
                    break;
                case "ES":
                    log.setText(log.getText() + "\n\nGuerreros y orcos colocados. Ahora puedes usar 2 de tus cartas de acción. " +
                            "Pulsa cualquiera de ellas para usarla.");
                    break;
            }

            cardHandlerClicks.set(0);

            //cuando se pulsa una carta, se llama a la nueva interfaz y al nuevo controller
            cardHandler = MouseEvent -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("card_interface.fxml"));
                    final Stage dialog = new Stage();
                    switch (Game.getLanguage()) {
                        case "EN":
                            dialog.setTitle("Use Card");
                            break;
                        case "ES":
                            dialog.setTitle("Usar carta");
                            break;
                    }
                    Scene dialogScene = new Scene(fxmlLoader.load(), 468, 368);
                    dialog.setScene(dialogScene);
                    dialog.setResizable(false);
                    dialog.getIcons().add(new Image(new File("src/main/resources/img/archers_of_nand_icon.png").toURI().toString()));
                    dialog.show();

                    CardController controller = fxmlLoader.getController();

                    controller.setMainController(this);
                    controller.refreshTextsCard(cardsView.indexOf(MouseEvent.getSource()));
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            };

            for (ImageView img : cardsView) {
                img.addEventHandler(MouseEvent.MOUSE_CLICKED, cardHandler);
            }
        });
        pause.play();
    }

    /**
     * Método que abre el manual de ayuda en una ventana del navegador externa al pulsar el botón de ayuda del tablero
     */
    public void openHelp() {
        File file = null;
        switch (Game.getLanguage()) {
            case "EN":
                file = new File("src/main/resources/manuals/spanish.pdf"); //no encuentro el manual en inglés, es posible que ni exista
                break;
            case "ES":
                file = new File("src/main/resources/manuals/spanish.pdf");
                break;
        }
        MainApp.applicationInstance.getHostServices().showDocument(String.valueOf(file.toURI()));
    }

    /**
     * Método que pregunta si quieres salir del juego al pulsar el botón de salir del tablero
     */
    public void exitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        switch (Game.getLanguage()) {
            case "EN":
                alert.setTitle("Quit to desktop");
                alert.setHeaderText("Are you sure you want to exit the game?");
                alert.setContentText("I wouldn't leave if I were you. There's still orcs roaming about.");
                break;
            case "ES":
                alert.setTitle("Salir del juego");
                alert.setHeaderText("¿Seguro que quieres salir del juego?");
                alert.setContentText("Yo que tú no me iría tan pronto. Aún quedan orcos sueltos.");
                break;
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ((Stage) btnExit.getScene().getWindow()).close();
        }
    }

    /**
     * Método que se ejecuta después de la horda. El jugador selecciona un territorio y se añade un guerrero a ese territorio
     */
    public void enviarCaballeros() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished((e1) -> {
            switch (Game.getLanguage()) {
                case "EN":
                    log.setText(log.getText() + "\n\nNow, you'll need to send a knight. " +
                            "Click on the board on the territory you want to send the knight to.");
                    break;
                case "ES":
                    log.setText(log.getText() + "\n\nAhora, tendrás que enviar un guerrero. " +
                            "Haz clic sobre el tablero en el territorio al que quieres enviar al guerrero.");
                    break;
            }

            boardHandler = MouseEvent -> {
                Territorio territory = listaTerritorios.get(stackTerritoriesList.indexOf(MouseEvent.getSource()));

                colocarGuerreros(territory);
            };

            for (StackPane stack : stackTerritoriesList) {
                stack.addEventHandler(MouseEvent.MOUSE_CLICKED, boardHandler);
            }
        });
        pause.play();
    }
}