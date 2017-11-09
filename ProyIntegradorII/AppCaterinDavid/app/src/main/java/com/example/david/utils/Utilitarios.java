package com.example.david.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utilitarios {
    public static String fechaActual(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaActual = new Date();
        String fechaFormateada = sdf.format(fechaActual);
        return fechaFormateada;
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

}
