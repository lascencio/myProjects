package com.example.david.beans;

public class FamiliaBean {

    private int ID_FAMILIA;
    private String DESCRIPCION;

    @Override
    public String toString() {
        return DESCRIPCION;
    }

    public int getID_FAMILIA() {
        return ID_FAMILIA;
    }

    public void setID_FAMILIA(int ID_FAMILIA) {
        this.ID_FAMILIA = ID_FAMILIA;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
