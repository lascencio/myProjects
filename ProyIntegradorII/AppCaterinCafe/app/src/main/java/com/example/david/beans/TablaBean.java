package com.example.david.beans;

public class TablaBean {
    private int ID;
    private String DESCRIPCION;

    @Override
    public String toString() {
        return DESCRIPCION;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
