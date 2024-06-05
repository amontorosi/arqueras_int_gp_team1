package com.gp.arqueras_javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

/**
 * Clase para la carta que representa a las arqueras
 *
 * @author Manuel Valdepeñas
 * @version 1.0 18/02/2024
 */
public class CartaArquera {
    /**
     * Atributo que representa el total de las fichas de flechas
     */
    private final IntegerProperty cantidadFlechas = new SimpleIntegerProperty();
    /**
     * Atributo que representa las flechas que perderán las arqueras al huir
     */
    private int costeHuida;

    public CartaArquera(int cantidadFlechas, int costeHuida) {
        this.setCantidadFlechas(cantidadFlechas);
        this.setCosteHuida(costeHuida);
    }

    public int getCantidadFlechas() {
        return cantidadFlechas.get();
    }

    public void setCantidadFlechas(int cantidadFlechas) {
        if (cantidadFlechas < 0) {
            cantidadFlechas = 0;
        }
        this.cantidadFlechas.set(cantidadFlechas);
    }

    public int getCosteHuida() {
        return costeHuida;
    }

    public void setCosteHuida(int costeHuida) {
        this.costeHuida = costeHuida;
    }

    public IntegerProperty flechasProperty() {
        return cantidadFlechas;
    }

    /**
     * Método que comprueba si las arqueras tienen suficientes flechas para disparar a los territorios indicados
     *
     * @param listaTerritorios La lista de territorios a los que dispararían las arqueras
     * @return True si tienen flechas suficientes para disparar, false en caso contrario
     * @author Manuel Valdepeñas
     */
    public boolean puedenDisparar(ArrayList<Territorio> listaTerritorios) {
        int totalFlechas = 0;
        boolean puedenDisparar;
        for (Territorio terr : listaTerritorios) {
            if (terr.getCantidadOrcos() > 0 && terr.getCantidadGuerreros() == 0) {
                totalFlechas += 1;
            } else if (terr.getCantidadOrcos() == 0 && terr.getCantidadGuerreros() == 0) {
                totalFlechas += 2;
            } else if (terr.getCantidadOrcos() == 0 && terr.getCantidadGuerreros() > 0) {
                totalFlechas += 3;
            } else if (terr.getCantidadOrcos() > 0 && terr.getCantidadGuerreros() > 0) {
                totalFlechas += 4;
            }
        }

        if (this.getCantidadFlechas() < totalFlechas) {
            puedenDisparar = false;
        } else {
            puedenDisparar = true;
        }

        return puedenDisparar;
    }

    /**
     * Método que decidirá cuántos orcos/guerreros caerán y cuántas flechas se dispararán en función de las características del territorio en ese momento.
     *
     * @param territorio El territorio al que se dispararán las flechas
     * @param controller El controller del tablero principal, para poder modificar elementos de la interfaz
     * @author Manuel Valdepeñas
     */
    public void dispararFlechas(Territorio territorio, MainController controller) {
        //1 orco, 0 guerreros
        if (territorio.getCantidadOrcos() > 0 && territorio.getCantidadGuerreros() == 0) {
            territorio.setCantidadOrcos(territorio.getCantidadOrcos() - 1);
            this.setCantidadFlechas(this.getCantidadFlechas() - 1);
            System.out.println("Se ha disparado 1 flecha y ha caído 1 orco. " + territorio.getNombre() + " tiene " + territorio.getCantidadOrcos() + " orcos y " + territorio.getCantidadGuerreros() + " guerreros.");
            switch (Game.getLanguage()) {
                case "EN":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": 1 arrow fired, 1 orc down.");
                    break;
                case "ES":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": Se ha disparado 1 flecha y ha caído 1 orco.");
                    break;
            }

            //0 orcos, 0 guerreros
        } else if (territorio.getCantidadGuerreros() == 0 && territorio.getCantidadOrcos() == 0) {
            this.setCantidadFlechas(this.getCantidadFlechas() - 2);
            System.out.println("Se han disparado 2 flechas. " + territorio.getNombre() + " tiene " + territorio.getCantidadOrcos() + " orcos y " + territorio.getCantidadGuerreros() + " guerreros.");
            switch (Game.getLanguage()) {
                case "EN":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": 2 arrows fired.");
                    break;
                case "ES":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": Se han disparado 2 flechas.");
                    break;
            }

            //1 guerrero, 0 orcos
        } else if (territorio.getCantidadGuerreros() > 0 && territorio.getCantidadOrcos() == 0) {
            territorio.setCantidadGuerreros(territorio.getCantidadGuerreros() - 1);
            this.setCantidadFlechas(this.getCantidadFlechas() - 3);
            System.out.println("Se han disparado 3 flechas y ha caído 1 guerrero. " + territorio.getNombre() + " tiene " + territorio.getCantidadOrcos() + " orcos y " + territorio.getCantidadGuerreros() + " guerreros.");
            switch (Game.getLanguage()) {
                case "EN":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": 3 arrows fired, 1 knight down.");
                    break;
                case "ES":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": Se han disparado 3 flechas y ha caído 1 guerrero.");
                    break;
            }

            //1 orco, 1 guerrero
        } else if (territorio.getCantidadOrcos() > 0 && territorio.getCantidadOrcos() > 0) {
            territorio.setCantidadOrcos(territorio.getCantidadOrcos() - 1);
            territorio.setCantidadGuerreros(territorio.getCantidadGuerreros() - 1);
            this.setCantidadFlechas(this.getCantidadFlechas() - 4);
            System.out.println("Se han disparado 4 flechas y han caído 1 orco y 1 guerrero. " + territorio.getNombre() + " tiene " + territorio.getCantidadOrcos() + " orcos y " + territorio.getCantidadGuerreros() + " guerreros.");
            switch (Game.getLanguage()) {
                case "EN":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": 4 arrows fired, 1 orc and 1 knight down.");
                    break;
                case "ES":
                    controller.log.setText(controller.log.getText() + "\n\n" + territorio.getNombre() + ": Se han disparado 4 flechas y han caído 1 orco y 1 guerrero.");
                    break;
            }

        }
    }
}
