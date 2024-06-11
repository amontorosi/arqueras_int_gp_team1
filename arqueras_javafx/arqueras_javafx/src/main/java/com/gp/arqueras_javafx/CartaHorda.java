package com.gp.arqueras_javafx;

/**
 * Clase para cada carta de horda
 *
 * @author Manuel Valdepeñas
 * @version 1.0 18/02/2024
 */
public class CartaHorda {
    /**
     * Cuántos orcos van a entrar en el mapa cuando se saque esta carta
     */
    private int numeroOrcos;

    public CartaHorda(int numeroOrcos) {
        this.setNumeroOrcos(numeroOrcos);
    }

    public int getNumeroOrcos() {
        return numeroOrcos;
    }

    public void setNumeroOrcos(int numeroOrcos) {
        this.numeroOrcos = numeroOrcos;
    }

    /**
     * Método que representa la entrada de un orco en una zona del mapa
     *
     * @param territorio El territorio al que va a ir un solo orco
     * @param controller El controlador de la interfaz principal, para poder acceder al log
     * @author Manuel Valdepeñas
     */
    public void horda(Territorio territorio, MainController controller) {
        territorio.setCantidadOrcos(territorio.getCantidadOrcos() + 1);
        System.out.println("Un orco enviado a " + territorio.getNombre() + ". Orcos en " + territorio.getNombre() + ": " + territorio.getCantidadOrcos());
        switch (Game.getLanguage()) {
            case "EN":
                controller.log.setText(controller.log.getText() + "\nOne orc sent to " + territorio.getNombre() + ".");
                break;
            case "ES":
                controller.log.setText(controller.log.getText() + "\nUn orco enviado a " + territorio.getNombre() + ".");
                break;
        }
    }
}
