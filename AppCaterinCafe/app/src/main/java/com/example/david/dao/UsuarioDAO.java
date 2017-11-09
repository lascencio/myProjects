package com.example.david.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.david.beans.UsuarioBean;
import com.example.david.conexion.MiConnection;

public class UsuarioDAO {
    Context context;

    public UsuarioDAO(Context context) {
        this.context = context;
    }

    public UsuarioBean loguin(String user, String pwd) {
        UsuarioBean bean = null;
        MiConnection cn = new MiConnection(context,null,null,1);
        SQLiteDatabase sql = cn.getReadableDatabase();
        Cursor cur = sql.rawQuery("select * from USUARIO where NOMBRE = ? and CLAVE_WEB = ?", new String[]{user, pwd});
        if(cur.moveToNext()){
            bean = new UsuarioBean();
            bean.setID_USUARIO(cur.getString(0));
            bean.setNOMBRE(cur.getString(1));
            bean.setCLAVE_WEB(cur.getString(2));
            bean.setNIVEL_ACCESO(cur.getString(3));
        }
        return bean;
    }
}
