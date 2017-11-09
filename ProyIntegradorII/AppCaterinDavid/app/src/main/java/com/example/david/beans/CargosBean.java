package com.example.david.beans;

public class CargosBean {
    private int ID_CARGO;
    private String DESCRIPCION;

    @Override
    public String toString() {
        return DESCRIPCION;
    }

    public int getID_CARGO() {
        return ID_CARGO;
    }

    public void setID_CARGO(int ID_CARGO) {
        this.ID_CARGO = ID_CARGO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
