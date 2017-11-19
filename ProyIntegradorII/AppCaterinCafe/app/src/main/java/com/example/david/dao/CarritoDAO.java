package com.example.david.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.david.beans.DetalleVentasBean;
import com.example.david.beans.VentasBean;
import com.example.david.conexion.MiConexion;

import java.util.ArrayList;

public class CarritoDAO {
    Context context;

    public CarritoDAO(Context context){
        this.context = context;
    }

    public int agregarDetalle(DetalleVentasBean bean) {
        int result;
        try {
            MiConexion cn = new MiConexion(context, null, null, 1);
            SQLiteDatabase sql = cn.getWritableDatabase();
            sql.execSQL("insert into DETALLE_VENTA values (?,?,?,?,?,?,?)",
                    new Object[]{bean.getID_VENTAS(),bean.getID_ARTICULO(),bean.getRUTA_IMAGEN(),bean.getDESCRIPCION(), bean.getCANTIDAD(),bean.getPRECIO_UNITARIO(),bean.getPRECIO_TOTAL()});
            result = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    }

    public int actualizarDetalle(String idVenta, String idArticulo, int cantidad, double total) {
        int result;
        try {
            MiConexion cn = new MiConexion(context, null, null, 1);
            SQLiteDatabase sql = cn.getWritableDatabase();
            sql.execSQL("update DETALLE_VENTA set CANTIDAD= ?,PRECIO_TOTAL= ? where ID_VENTA = ? and ID_ARTICULO = ?;",
                    new String[]{String.valueOf(cantidad),String.valueOf(total),idVenta,idArticulo});
            result = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    }

    public int eliminarDetalle(String idVenta, String idArticulo) {
        int result;
        try {
            MiConexion cn = new MiConexion(context, null, null, 1);
            SQLiteDatabase sql = cn.getWritableDatabase();
            sql.execSQL("delete from DETALLE_VENTA where ID_VENTA = ? and ID_ARTICULO = ?;",
                    new String[]{idVenta,idArticulo});
            result = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    }

    public int registrarVenta(VentasBean bean) {
        int result;
        try {
            MiConexion cn = new MiConexion(context, null, null, 1);
            SQLiteDatabase sql = cn.getWritableDatabase();
            sql.execSQL("insert into VENTA values (?,?,?,?,?)",
                    new Object[]{bean.getID_VENTAS(),bean.getID_CLIENTE(),bean.getTOTAL_PAGAR(),bean.getID_TIPO_VENTA(),bean.getID_FORMA_PAGO()});
            result = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    }

    public ArrayList<DetalleVentasBean> miCarrito(String id) {
        ArrayList<DetalleVentasBean> lista = new ArrayList<>();
        MiConexion cn = new MiConexion(context, null, null, 1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from DETALLE_VENTA where ID_VENTA = ?", new String[]{id});
        DetalleVentasBean bean = null;
        while (cur.moveToNext()) {
            bean = new DetalleVentasBean();
            bean.setID_VENTAS(cur.getString(0));
            bean.setID_ARTICULO(cur.getString(1));
            bean.setRUTA_IMAGEN(cur.getString(2));
            bean.setDESCRIPCION(cur.getString(3));
            bean.setCANTIDAD(cur.getInt(4));
            bean.setPRECIO_UNITARIO(cur.getDouble(5));
            bean.setPRECIO_TOTAL(cur.getDouble(6));
            lista.add(bean);
        }
        return lista;
    }

    public DetalleVentasBean consultarDetalle(String idVenta, String idArticulo) {
        DetalleVentasBean bean = null;
        MiConexion cn = new MiConexion(context, null, null, 1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from DETALLE_VENTA where ID_VENTA = ? and ID_ARTICULO = ?", new String[]{idVenta,idArticulo});
        if (cur.moveToNext()) {
            bean = new DetalleVentasBean();
            bean.setID_VENTAS(cur.getString(0));
            bean.setID_ARTICULO(cur.getString(1));
            bean.setRUTA_IMAGEN(cur.getString(2));
            bean.setDESCRIPCION(cur.getString(3));
            bean.setCANTIDAD(cur.getInt(4));
            bean.setPRECIO_UNITARIO(cur.getDouble(5));
            bean.setPRECIO_TOTAL(cur.getDouble(6));
        }
        return bean;
    }


}
