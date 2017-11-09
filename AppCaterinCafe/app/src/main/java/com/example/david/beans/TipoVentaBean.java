package com.example.david.beans;

import java.io.Serializable;

/**
 * Created by David on 07/10/2017.
 */

public class TipoVentaBean implements Serializable {
    private int ID_TIPO_VENTA;
    private String DESCRIPCION;

    public int getID_TIPO_VENTA() {
        return ID_TIPO_VENTA;
    }

    public void setID_TIPO_VENTA(int ID_TIPO_VENTA) {
        this.ID_TIPO_VENTA = ID_TIPO_VENTA;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
