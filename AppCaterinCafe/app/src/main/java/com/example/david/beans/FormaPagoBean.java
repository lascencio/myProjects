package com.example.david.beans;

import java.io.Serializable;

/**
 * Created by David on 07/10/2017.
 */

public class FormaPagoBean implements Serializable {
    private int ID_FORMA_PAGO;
    private String DESCRIPCION;
    public int getID_FORMA_PAGO() {
        return ID_FORMA_PAGO;
    }

    public void setID_FORMA_PAGO(int ID_FORMA_PAGO) {
        this.ID_FORMA_PAGO = ID_FORMA_PAGO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
