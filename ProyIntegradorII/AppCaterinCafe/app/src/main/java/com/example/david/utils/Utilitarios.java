package com.example.david.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilitarios {

    public static final String URL_SERVICE = "http://192.168.0.15:8080/SW_AppCateringDavid/";

    public static final String APP_NAME = "appcaterincafe";

    public static final int REQUEST_CODE_GALLERY = 999;

    public static Bitmap rutaAbitmap(String rutaImagen, Context context){
        Bitmap bitmap = null;
        Uri uri = Uri.parse(rutaImagen);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String fechaActual(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public Spanned negrita(String texto){
        return Html.fromHtml("<b>" + texto + "</b>");
    }

    public static void validarCampos(String titulo, String mensaje, Context ctx){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(titulo);
        builder.setMessage(mensaje).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    public static String id_usuario = "";
    public static String id_articulo = "";
    public static String id_venta = "";
    public static String nombre = "";
    public static String apellido = "";
    public static String correo = "";
    public static int acceso_cargo = 0;
    public static int estado = 0;
}
