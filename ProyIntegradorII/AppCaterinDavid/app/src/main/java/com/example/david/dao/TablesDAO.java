package com.example.david.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.david.beans.ArticuloBean;
import com.example.david.beans.CargosBean;
import com.example.david.beans.FamiliaBean;
import com.example.david.beans.NivelesBean;
import com.example.david.conexion.MiConexion;

import java.util.ArrayList;

public class TablesDAO {
    Context context;

    public TablesDAO(Context context) {
        this.context = context;
    }

    public ArrayList<CargosBean> cargos(){
        ArrayList<CargosBean> lista = new ArrayList<CargosBean>();
        MiConexion cn = new MiConexion(context, null, null,1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from CARGO",null);
        CargosBean bean = null;
        while(cur.moveToNext()){
            bean = new CargosBean();
            bean.setID_CARGO(cur.getInt(0));
            bean.setDESCRIPCION(cur.getString(1));
            lista.add(bean);
        }
        return lista;
    }

    public ArrayList<NivelesBean> niveles(){
        ArrayList<NivelesBean> lista = new ArrayList<NivelesBean>();
        MiConexion cn = new MiConexion(context, null, null,1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from NIVEL_ACCESO",null);
        NivelesBean bean = null;
        while(cur.moveToNext()){
            bean = new NivelesBean();
            bean.setID_NIVEL_ACCESO(cur.getInt(0));
            bean.setDESCRIPCION(cur.getString(1));
            lista.add(bean);
        }
        return lista;
    }

    public ArrayList<FamiliaBean> familia(){
        ArrayList<FamiliaBean> lista = new ArrayList<FamiliaBean>();
        MiConexion cn = new MiConexion(context, null, null,1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from FAMILIA",null);
        FamiliaBean bean = null;
        while(cur.moveToNext()){
            bean = new FamiliaBean();
            bean.setID_FAMILIA(cur.getInt(0));
            bean.setDESCRIPCION(cur.getString(1));
            lista.add(bean);
        }
        return lista;
    }

    public ArrayList<ArticuloBean> articulos(){
        ArrayList<ArticuloBean> lista = new ArrayList<ArticuloBean>();
        MiConexion cn = new MiConexion(context, null, null,1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select ID_ARTICULO,DESCRIPCION,PRECIO_UNITARIO,IMAGEN from ARTICULO",null);
        ArticuloBean bean = null;
        while(cur.moveToNext()){
            bean = new ArticuloBean();
            bean.setID_ARTICULO(cur.getInt(0));
            bean.setDESCRIPCION(cur.getString(1));
            bean.setPRECIO_UNITARIO(cur.getDouble(2));
            bean.setRUTA_IMAGEN(cur.getBlob(3));
            lista.add(bean);
        }
        return lista;
    }
}
