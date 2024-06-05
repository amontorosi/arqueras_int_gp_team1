package com.gp.arqueras_javafx;

/**
 * Clase para una moneda de color
 *
 * @author Manuel Valdepeñas
 * @version 1.0 18/02/2024
 */
public class Moneda {
    /**
     * Atributo que determina si sale cara (true, es decir, se cuenta el estandarte de ese tipo) o cruz (false, no se tiene en cuenta su tipo)
     */
    private boolean caraOCruz;
    /**
     * El tipo de la moneda (bosque, montaña o mar)
     */
    private String tipo;

    public Moneda(String tipo) {
        this.setCaraOCruz(false);
        this.setTipo(tipo);
    }

    public boolean isCaraOCruz() {
        return caraOCruz;
    }

    public void setCaraOCruz(boolean caraOCruz) {
        this.caraOCruz = caraOCruz;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo.equalsIgnoreCase("bosque") || tipo.equalsIgnoreCase("montaña") || tipo.equalsIgnoreCase("mar")) {
            this.tipo = tipo;
        } else {
            this.tipo = null;
        }
    }

    /**
     * Este método simulará el lanzar la moneda al aire. Se elegirá un número aleatorio entre 1 y 2: 1 es cara (true) y 2 es cruz (false)
     *
     * @author Manuel Valdepeñas
     */
    public boolean lanzarMoneda() {
        int random = (int) (Math.random() * (2 + 1 - 1) + 1);
        switch (random) {
            case 1:
                this.setCaraOCruz(true);
                return true;
            case 2:
                this.setCaraOCruz(false);
                return false;
        }
        return false;
    }
}
