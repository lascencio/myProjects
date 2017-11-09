package com.example.david.beans;

import java.io.Serializable;
import java.util.Date;

public class ClienteBean implements Serializable {
    private String ID_CLIENTE;
    private String NOMBRE;
    private String APELLIDO;
    private String ID_ESTADO;
    private String USUARIO;
    private String CORREO;
    private String CLAVE;
    private double SALDO;
    private int INDICA_AUTORIZA;
    private String FECHA_REGISTRO;
    private String FECHA_MODIFICA;
    private int ID_CARGO;

    public String getCORREO() {
        return CORREO;
    }

    public String getAPELLIDO() {
        return APELLIDO;
    }

    public void setAPELLIDO(String APELLIDO) {
        this.APELLIDO = APELLIDO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public int getID_CARGO() {
        return ID_CARGO;
    }

    public void setID_CARGO(int ID_CARGO) {
        this.ID_CARGO = ID_CARGO;
    }

    public String getID_CLIENTE() {
        return ID_CLIENTE;
    }

    public void setID_CLIENTE(String ID_CLIENTE) {
        this.ID_CLIENTE = ID_CLIENTE;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }


    public String getID_ESTADO() {
        return ID_ESTADO;
    }

    public void setID_ESTADO(String ID_ESTADO) {
        this.ID_ESTADO = ID_ESTADO;
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCLAVE() {
        return CLAVE;
    }

    public void setCLAVE(String CLAVE) {
        this.CLAVE = CLAVE;
    }

    public double getSALDO() {
        return SALDO;
    }

    public void setSALDO(double SALDO) {
        this.SALDO = SALDO;
    }

    public int getINDICA_AUTORIZA() {
        return INDICA_AUTORIZA;
    }

    public void setINDICA_AUTORIZA(int INDICA_AUTORIZA) {
        this.INDICA_AUTORIZA = INDICA_AUTORIZA;
    }

    public String getFECHA_REGISTRO() {
        return FECHA_REGISTRO;
    }

    public void setFECHA_REGISTRO(String FECHA_REGISTRO) {
        this.FECHA_REGISTRO = FECHA_REGISTRO;
    }

    public String getFECHA_MODIFICA() {
        return FECHA_MODIFICA;
    }

    public void setFECHA_MODIFICA(String FECHA_MODIFICA) {
        this.FECHA_MODIFICA = FECHA_MODIFICA;
    }
}
