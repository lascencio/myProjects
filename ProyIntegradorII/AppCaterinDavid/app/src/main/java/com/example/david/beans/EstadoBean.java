package com.example.david.beans;

import java.io.Serializable;

/**
 * Created by David on 07/10/2017.
 */

public class EstadoBean implements Serializable {
    private int ID_ESTADO;
    private String DESCRIPCION;

    public int getID_ESTADO() {
        return ID_ESTADO;
    }

    public void setID_ESTADO(int ID_ESTADO) {
        this.ID_ESTADO = ID_ESTADO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
