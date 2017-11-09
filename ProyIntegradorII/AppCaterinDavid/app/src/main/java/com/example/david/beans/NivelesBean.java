package com.example.david.beans;

public class NivelesBean {

    private int ID_NIVEL_ACCESO;
    private String DESCRIPCION;

    public int getID_NIVEL_ACCESO() {
        return ID_NIVEL_ACCESO;
    }

    @Override
    public String toString() {
        return DESCRIPCION;
    }

    public void setID_NIVEL_ACCESO(int ID_NIVEL_ACCESO) {
        this.ID_NIVEL_ACCESO = ID_NIVEL_ACCESO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }
}
