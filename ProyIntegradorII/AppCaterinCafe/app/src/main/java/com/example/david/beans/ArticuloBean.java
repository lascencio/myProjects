package com.example.david.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class ArticuloBean implements Serializable {
    private String ID_ARTICULO;
    private String DESCRIPCION;
    private String DESCRIPCION_AMPLIADA;
    private int ID_FAMILIA;
    private int ID_ESTADO;
    private double PRECIO_UNITARIO;
    private int STOCK;
    private String VALIDA_STOCK;
    private String USUARIO_REGISTRO;
    private Date FECHA_REGISTRO;
    private String USUARIO_MODIFICA;
    private Date FECHA_MODIFICA;
    private String RUTA_IMAGEN;

    public String getID_ARTICULO() {
        return ID_ARTICULO;
    }

    public void setID_ARTICULO(String ID_ARTICULO) {
        this.ID_ARTICULO = ID_ARTICULO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public String getDESCRIPCION_AMPLIADA() {
        return DESCRIPCION_AMPLIADA;
    }

    public void setDESCRIPCION_AMPLIADA(String DESCRIPCION_AMPLIADA) {
        this.DESCRIPCION_AMPLIADA = DESCRIPCION_AMPLIADA;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public int getID_FAMILIA() {
        return ID_FAMILIA;
    }

    public void setID_FAMILIA(int ID_FAMILIA) {
        this.ID_FAMILIA = ID_FAMILIA;
    }

    public int getID_ESTADO() {
        return ID_ESTADO;
    }

    public void setID_ESTADO(int ID_ESTADO) {
        this.ID_ESTADO = ID_ESTADO;
    }

    public double getPRECIO_UNITARIO() {
        return PRECIO_UNITARIO;
    }

    public void setPRECIO_UNITARIO(double PRECIO_UNITARIO) {
        this.PRECIO_UNITARIO = PRECIO_UNITARIO;
    }

    public int getSTOCK() {
        return STOCK;
    }

    public void setSTOCK(int STOCK) {
        this.STOCK = STOCK;
    }

    public String getVALIDA_STOCK() {
        return VALIDA_STOCK;
    }

    public void setVALIDA_STOCK(String VALIDA_STOCK) {
        this.VALIDA_STOCK = VALIDA_STOCK;
    }

    public String getUSUARIO_REGISTRO() {
        return USUARIO_REGISTRO;
    }

    public void setUSUARIO_REGISTRO(String USUARIO_REGISTRO) {
        this.USUARIO_REGISTRO = USUARIO_REGISTRO;
    }

    public Date getFECHA_REGISTRO() {
        return FECHA_REGISTRO;
    }

    public void setFECHA_REGISTRO(Date FECHA_REGISTRO) {
        this.FECHA_REGISTRO = FECHA_REGISTRO;
    }

    public String getUSUARIO_MODIFICA() {
        return USUARIO_MODIFICA;
    }

    public void setUSUARIO_MODIFICA(String USUARIO_MODIFICA) {
        this.USUARIO_MODIFICA = USUARIO_MODIFICA;
    }

    public Date getFECHA_MODIFICA() {
        return FECHA_MODIFICA;
    }

    public void setFECHA_MODIFICA(Date FECHA_MODIFICA) {
        this.FECHA_MODIFICA = FECHA_MODIFICA;
    }

    public String getRUTA_IMAGEN() {
        return RUTA_IMAGEN;
    }

    public void setRUTA_IMAGEN(String RUTA_IMAGEN) {
        this.RUTA_IMAGEN = RUTA_IMAGEN;
    }

    @Override
    public String toString() {
        return "ArticuloBean{" +
                "ID_ARTICULO=" + ID_ARTICULO +
                ", DESCRIPCION='" + DESCRIPCION + '\'' +
                ", DESCRIPCION_AMPLIADA='" + DESCRIPCION_AMPLIADA + '\'' +
                ", ID_FAMILIA=" + ID_FAMILIA +
                ", ID_ESTADO=" + ID_ESTADO +
                ", PRECIO_UNITARIO=" + PRECIO_UNITARIO +
                ", STOCK=" + STOCK +
                ", VALIDA_STOCK='" + VALIDA_STOCK + '\'' +
                ", USUARIO_REGISTRO='" + USUARIO_REGISTRO + '\'' +
                ", FECHA_REGISTRO=" + FECHA_REGISTRO +
                ", USUARIO_MODIFICA='" + USUARIO_MODIFICA + '\'' +
                ", FECHA_MODIFICA=" + FECHA_MODIFICA +
                ", RUTA_IMAGEN=" + RUTA_IMAGEN.toString() +
                '}';
    }
}
