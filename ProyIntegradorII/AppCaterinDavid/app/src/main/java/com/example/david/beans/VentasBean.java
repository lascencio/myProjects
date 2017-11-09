package com.example.david.beans;

import java.io.Serializable;
import java.util.Date;

public class VentasBean implements Serializable {
    private int ID_VENTAS;
    private String EJERCICIO;
    private String MES;
    private Date FECHA;
    private String ID_CLIENTE;
    private double TOTAL_PAGAR;
    private int ID_TIPO_VENTA;
    private int ID_FORMA_PAGO;

    public int getID_VENTAS() {
        return ID_VENTAS;
    }

    public void setID_VENTAS(int ID_VENTAS) {
        this.ID_VENTAS = ID_VENTAS;
    }

    public String getEJERCICIO() {
        return EJERCICIO;
    }

    public void setEJERCICIO(String EJERCICIO) {
        this.EJERCICIO = EJERCICIO;
    }

    public String getMES() {
        return MES;
    }

    public void setMES(String MES) {
        this.MES = MES;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public String getID_CLIENTE() {
        return ID_CLIENTE;
    }

    public void setID_CLIENTE(String ID_CLIENTE) {
        this.ID_CLIENTE = ID_CLIENTE;
    }

    public double getTOTAL_PAGAR() {
        return TOTAL_PAGAR;
    }

    public void setTOTAL_PAGAR(double TOTAL_PAGAR) {
        this.TOTAL_PAGAR = TOTAL_PAGAR;
    }

    public int getID_TIPO_VENTA() {
        return ID_TIPO_VENTA;
    }

    public void setID_TIPO_VENTA(int ID_TIPO_VENTA) {
        this.ID_TIPO_VENTA = ID_TIPO_VENTA;
    }

    public int getID_FORMA_PAGO() {
        return ID_FORMA_PAGO;
    }

    public void setID_FORMA_PAGO(int ID_FORMA_PAGO) {
        this.ID_FORMA_PAGO = ID_FORMA_PAGO;
    }
}
