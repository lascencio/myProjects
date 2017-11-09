package com.example.david.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.david.beans.ClienteBean;

public class Session {
    private SharedPreferences prefs;

    public Session(Context ctx) {

        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void setClienteSession(ClienteBean bean) {
        prefs.edit().putString("idCliente", bean.getID_CLIENTE() + "").commit();
        prefs.edit().putString("nombre", bean.getNOMBRE() + "").commit();
        prefs.edit().putString("apellido", bean.getAPELLIDO() + "").commit();
        prefs.edit().putString("correo", bean.getCORREO() + "").commit();
        prefs.edit().putString("saldo", bean.getSALDO() + "").commit();
        prefs.edit().putString("indica", bean.getINDICA_AUTORIZA() + "").commit();
    }

    public ClienteBean getClienteSession() {
        ClienteBean bean = new ClienteBean();
        bean.setID_CLIENTE(prefs.getString("idCliente",""));
        bean.setNOMBRE(prefs.getString("nombre",""));
        bean.setAPELLIDO(prefs.getString("apellido",""));
        bean.setCORREO(prefs.getString("correo",""));
        bean.setSALDO(Double.parseDouble(prefs.getString("saldo","")));
        return bean;
    }

    public int getIdCliente() {
        String idCliente = prefs.getString("idCliente", "");
        return Integer.parseInt(idCliente);
    }

    public void setIdUsuario(int idUsuario) {
        prefs.edit().putString("idUsuario", idUsuario + "").commit();
    }

    public int getIdUsuario() {
        String idUsuario = prefs.getString("idUsuario", "");
        return Integer.parseInt(idUsuario);
    }

    public void setCorreoUsuario(String correo) {

        prefs.edit().putString("correoUsuario", correo).commit();
    }

    public String getCorreoUsuario() {
        String correo = prefs.getString("correoUsuario", "");
        return correo;
    }

    public void setCorreoCliente(String correo) {

        prefs.edit().putString("correoCliente", correo).commit();
    }

    public String getCorreoCliente() {
        String correo = prefs.getString("correoCliente", "");
        return correo;
    }

    public void setUsuarioNombre(String usuario) {

        prefs.edit().putString("usuarioNombre", usuario).commit();
    }

    public String getUsuarioNombre() {
        String usuario = prefs.getString("usuarioNombre", "");
        return usuario;
    }

    public void setClienteNombre(String nombre) {

        prefs.edit().putString("clienteNombre", nombre).commit();
    }

    public String getClienteNombre() {
        String nombre = prefs.getString("clienteNombre", "");
        return nombre;
    }

    public void setUsuarioUser(String user) {

        prefs.edit().putString("usuarioUser", user).commit();
    }

    public String getUsuarioUser() {
        String user = prefs.getString("usuarioUser", "");
        return user;
    }

    public void setClienteUser(String user) {

        prefs.edit().putString("clienteUser", user).commit();
    }

    public String getClienteUser() {
        String user = prefs.getString("clienteUser", "");
        return user;
    }


    public void setClaveWeb(String claveWeb) {

        prefs.edit().putString("claveWeb", claveWeb).commit();
    }

    public String getClaveWeb() {
        String claveWeb = prefs.getString("claveWeb", "");
        return claveWeb;
    }

    public void setNivelAcceso(String nivelAcceso) {

        prefs.edit().putString("nivelAcceso", nivelAcceso).commit();
    }

    public String getNivelAcceso() {
        String nivelAcceso = prefs.getString("nivelAcceso", "");
        return nivelAcceso;
    }

}
