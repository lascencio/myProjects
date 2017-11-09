package com.example.david.beans;

import java.io.Serializable;

/**
 * Created by David on 07/10/2017.
 */

public class UsuarioBean implements Serializable {
    private String ID_USUARIO;
    private String NOMBRE;
    private String CLAVE_WEB;
    private String NIVEL_ACCESO;

    public String getID_USUARIO() {
        return ID_USUARIO;
    }

    public void setID_USUARIO(String ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getCLAVE_WEB() {
        return CLAVE_WEB;
    }

    public void setCLAVE_WEB(String CLAVE_WEB) {
        this.CLAVE_WEB = CLAVE_WEB;
    }

    public String getNIVEL_ACCESO() {
        return NIVEL_ACCESO;
    }

    public void setNIVEL_ACCESO(String NIVEL_ACCESO) {
        this.NIVEL_ACCESO = NIVEL_ACCESO;
    }
}
