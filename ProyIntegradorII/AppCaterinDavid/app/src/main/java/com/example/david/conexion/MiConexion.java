package com.example.david.conexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class MiConexion extends SQLiteOpenHelper {
    public MiConexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, "DB_CATERIN1", factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String des, String desAmp, int idfam, int idest, double pre, String valSto, int stock, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "insert into ARTICULO values (null,?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, des);
        statement.bindString(2, desAmp);
        statement.bindLong(3, idfam);
        statement.bindLong(4, idest);
        statement.bindDouble(5, pre);
        statement.bindString(6, valSto);
        statement.bindLong(7, stock);
        statement.bindBlob(8, image);

        statement.executeInsert();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CARGO = "create table CARGO (ID_CARGO integer primary key autoincrement, DESCRIPCION text);";
        db.execSQL(CARGO);

        String NIVEL_ACCESO = "create table NIVEL_ACCESO (ID_NIVEL_ACCESO integer primary key autoincrement, DESCRIPCION text);";
        db.execSQL(NIVEL_ACCESO);

        String FAMILIA = "create table FAMILIA (ID_FAMILIA integer primary key autoincrement, DESCRIPCION text);";
        db.execSQL(FAMILIA);

        String ARTICULO = "create table ARTICULO (ID_ARTICULO integer primary key autoincrement, DESCRIPCION text, DESCRIPCION_AMP," +
                " ID_FAMILIA integer, ID_ESTADO integer, PRECIO_UNITARIO real, VALIDA_STOCK text, STOCK integer, IMAGEN blob);";
        db.execSQL(ARTICULO);

        db.execSQL("insert into FAMILIA values (null, 'PRODUCTOS');");
        db.execSQL("insert into FAMILIA values (null, 'SUMINISTROS');");

        db.execSQL("insert into NIVEL_ACCESO values (null, 'TODO');");
        db.execSQL("insert into NIVEL_ACCESO values (null, 'MANTENIMIENTOS');");
        db.execSQL("insert into NIVEL_ACCESO values (null, 'CONSULTA');");

        db.execSQL("insert into CARGO values (null, 'EMPLEADO');");
        db.execSQL("insert into CARGO values (null, 'JEFE PROYECTOS');");
        db.execSQL("insert into CARGO values (null, 'RR.HH.');");
        db.execSQL("insert into CARGO values (null, 'ADMINISTRACION');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
