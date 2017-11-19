package com.example.david.conexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class MiConexion extends SQLiteOpenHelper {
    public MiConexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, "DB_CATERIN4", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String DETALLE_VENTA = "create table DETALLE_VENTA (ID_VENTA text, ID_ARTICULO text, RUTA_IMAGEN text, DESCRIPCION text, CANTIDAD integer, PRECIO_UNITARIO real, PRECIO_TOTAL real);";
        db.execSQL(DETALLE_VENTA);

        String VENTA = "create table VENTA (ID_VENTA text, ID_CLIENTE text, TOTAL_PAGAR real, ID_TIPO_VENTA integer, ID_FORMA_PAGO integer);";
        db.execSQL(VENTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
