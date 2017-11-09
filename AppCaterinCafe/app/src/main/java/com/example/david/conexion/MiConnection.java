package com.example.david.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 07/10/2017.
 */

public class MiConnection extends SQLiteOpenHelper {
    public MiConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, "CATERIN_CAFE", factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String ARTICULO = "create table ARTICULO (ID_ARTICULO text primary key, DESCRIPCION text, FAMILIA text, ID_ESTADO int, PRECIO_UNITARIO real," +
                "STOCK integer, VALIDA_STOCK text, USUARIO_REGISTRO text, FECHA_REGISTRO text,USUARIO_MODIFICA text, FECHA_MODIFICA text, RUTA_IMAGEN text);";
        db.execSQL(ARTICULO);
        String CLIENTE = "create table CLIENTE (ID_CLIENTE text primary key, NOMBRE text, APELLIDO text, ID_ESTADO int, USUARIO text," +
                "CLAVE integer, SALDO real, INDICA_AUTORIZA text, FECHA_REGISTRO text,FECHA_MODIFICA text);";
        db.execSQL(CLIENTE);
        String USUARIO = "create table USUARIO (ID_USUARIO text primary key, NOMBRE text, CLAVE_WEB text, NIVEL_ACCESO text);";
        db.execSQL(USUARIO);

        db.execSQL("insert into USUARIO values ('user12', 'david01', 'term2', 'MANT');");
        db.execSQL("insert into USUARIO values ('user13', 'accel01', 'iron4', 'CONS');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
