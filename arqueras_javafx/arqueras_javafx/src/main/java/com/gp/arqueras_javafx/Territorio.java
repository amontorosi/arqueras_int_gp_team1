package com.gp.arqueras_javafx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

/**
 * Clase para cada territorio
 *
 * @author Manuel Valdepeñas
 * @version 1.0 18/02/2024
 */
public class Territorio {
    /**
     * Nombre del territorio
     */
    private String nombre;
    /**
     * Comprobación de si el territorio concreto tiene el estandarte de bosque (true) o no (false)
     */
    private boolean bosque;
    /**
     * Comprobación de si el territorio concreto tiene el estandarte de montaña (true) o no (false)
     */
    private boolean montanna;
    /**
     * Comprobación de si el territorio concreto tiene el estandarte de mar (true) o no (false)
     */
    private boolean mar;
    /**
     * Comprobación de si el territorio ha sido destruido (true) o no (false)
     */
    private final BooleanProperty destruido = new SimpleBooleanProperty();
    /**
     * Representación del total de fichas de orco en el territorio concreto
     */
    private final IntegerProperty cantidadOrcos = new SimpleIntegerProperty();
    /**
     * Representación del total de fichas de guerreros en el territorio concreto
     */
    private final IntegerProperty cantidadGuerreros = new SimpleIntegerProperty();
    /**
     * Lista con los territorios adyacentes a la instancia, importante para saber por dónde se podrán desplazar los guerreros
     */
    private ArrayList<Territorio> territoriosAdyacentes;

    public Territorio(String nombre, boolean bosque, boolean montanna, boolean mar, ArrayList<Territorio> territoriosAdyacentes) {
        this.setNombre(nombre);
        this.setBosque(bosque);
        this.setMontanna(montanna);
        this.setMar(mar);
        this.setDestruido(false);
        this.setCantidadOrcos(0);
        this.setCantidadGuerreros(0);
        this.setTerritoriosAdyacentes(territoriosAdyacentes);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isBosque() {
        return bosque;
    }

    public void setBosque(boolean bosque) {
        this.bosque = bosque;
    }

    public boolean isMontanna() {
        return montanna;
    }

    public void setMontanna(boolean montanna) {
        this.montanna = montanna;
    }

    public boolean isMar() {
        return mar;
    }

    public void setMar(boolean mar) {
        this.mar = mar;
    }

    public int getCantidadOrcos() {
        return cantidadOrcos.get();
    }

    public IntegerProperty cantidadOrcosProperty() {
        return cantidadOrcos;
    }

    public void setCantidadOrcos(int cantidadOrcos) {
        this.cantidadOrcos.set(cantidadOrcos);
    }

    public int getCantidadGuerreros() {
        return cantidadGuerreros.get();
    }

    public IntegerProperty cantidadGuerrerosProperty() {
        return cantidadGuerreros;
    }

    public void setCantidadGuerreros(int cantidadGuerreros) {
        this.cantidadGuerreros.set(cantidadGuerreros);
    }

    public boolean isDestruido() {
        return destruido.get();
    }

    public BooleanProperty destruidoProperty() {
        return destruido;
    }

    public void setDestruido(boolean destruido) {
        this.destruido.set(destruido);
    }

    public ArrayList<Territorio> getTerritoriosAdyacentes() {
        return territoriosAdyacentes;
    }

    public void setTerritoriosAdyacentes(ArrayList<Territorio> territoriosAdyacentes) {
        this.territoriosAdyacentes = territoriosAdyacentes;
    }

    /**
     * Método que se ejecutará después de que las arqueras disparen sus flechas. Los orcos y los guerreros lucharán cuerpo a cuerpo en el/los territorio(s) donde hayan disparado las arqueras. Si el territorio tiene 3 o más orcos, es destruido
     *
     * @param controller El Controller del tablero principal, para poder acceder al log de la interfaz
     * @author Manuel Valdepeñas
     */
    public void pelea(MainController controller) {
        int caballerosAux = this.getCantidadGuerreros();
        int orcosAux = this.getCantidadOrcos();
        this.setCantidadGuerreros((caballerosAux - orcosAux * 2));
        this.setCantidadOrcos((orcosAux - caballerosAux / 2));

        if (this.getCantidadOrcos() < 0) {
            this.setCantidadOrcos(0);
        }

        if (this.getCantidadGuerreros() < 0) {
            this.setCantidadGuerreros(0);
        }

        System.out.println("Se ha producido una batalla cuerpo a cuerpo en " + this.getNombre() + ". En " + this.getNombre() + " hay " + this.getCantidadGuerreros() + " guerreros y " + this.getCantidadOrcos() + " orcos.");
        switch (Game.getLanguage()) {
            case "EN":
                controller.log.setText(controller.log.getText() + "\nA melee battle has taken place in " + this.getNombre() + " between " + caballerosAux + " knights and " + orcosAux + " orcs.");
                break;
            case "ES":
                controller.log.setText(controller.log.getText() + "\nSe ha producido una batalla cuerpo a cuerpo en " + this.getNombre() + " entre " + caballerosAux + " guerreros y " + orcosAux + " orcos.");
                break;
        }
    }

    @Override
    public String toString() {
        return "\tTerritorio: " + this.getNombre() + "\n\t\tBosque: " + this.isBosque() + "\n\t\tMontaña: " + this.isMontanna() + "\n\t\tMar: " + this.isMar() + "\n\tN.º de caballeros: " + this.getCantidadGuerreros() + "\n\tN.º de orcos: " + this.getCantidadOrcos() + "\n";
    }
}
