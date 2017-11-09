package com.example.david.beans;

import java.io.Serializable;

/**
 * Created by David on 07/10/2017.
 */

public class DetalleVentasBean implements Serializable {
    private String ID_VENTAS;
    private String ID_ARTICULO;
    private int CANTIDAD;
    private double PRECIO_UNITARIO;
    private double PRECIO_TOTAL;
    private int ID_ESTADO;

    public String getID_VENTAS() {
        return ID_VENTAS;
    }

    public void setID_VENTAS(String ID_VENTAS) {
        this.ID_VENTAS = ID_VENTAS;
    }

    public String getID_ARTICULO() {
        return ID_ARTICULO;
    }

    public void setID_ARTICULO(String ID_ARTICULO) {
        this.ID_ARTICULO = ID_ARTICULO;
    }

    public int getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(int CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public double getPRECIO_UNITARIO() {
        return PRECIO_UNITARIO;
    }

    public void setPRECIO_UNITARIO(double PRECIO_UNITARIO) {
        this.PRECIO_UNITARIO = PRECIO_UNITARIO;
    }

    public double getPRECIO_TOTAL() {
        return PRECIO_TOTAL;
    }

    public void setPRECIO_TOTAL(double PRECIO_TOTAL) {
        this.PRECIO_TOTAL = PRECIO_TOTAL;
    }

    public int getID_ESTADO() {
        return ID_ESTADO;
    }

    public void setID_ESTADO(int ID_ESTADO) {
        this.ID_ESTADO = ID_ESTADO;
    }
}
