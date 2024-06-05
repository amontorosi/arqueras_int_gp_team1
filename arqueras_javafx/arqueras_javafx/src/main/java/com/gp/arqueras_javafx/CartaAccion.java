package com.gp.arqueras_javafx;

/**
 * Clase para cada carta de acción
 *
 * @author Manuel Valdepeñas
 * @version 1.0 18/02/2024
 */
public class CartaAccion {
    /**
     * El tipo de acción de la carta concreta que se vaya a instanciar
     */
    private String tipo;
    /**
     * Todos los tipos de cartas de acción posibles
     */
    public static final String[] tipos = {"COUNT", "NOT", "OR", "AND", "LIKE", "XOR", "Bengala"};

    public CartaAccion(String tipo) {
        this.setTipo(tipo);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
