package com.example.david.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.appcaterincafe.R;
import com.example.david.beans.DetalleVentasBean;
import com.example.david.utils.Utilitarios;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PedidosListAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private ArrayList<DetalleVentasBean> detalles;
    Utilitarios utils;

    public PedidosListAdapter(Context context, int layout, ArrayList<DetalleVentasBean> detalles) {
        this.context = context;
        this.layout = layout;
        this.detalles = detalles;
    }

    @Override
    public int getCount() {
        return detalles.size();
    }

    @Override
    public Object getItem(int i) {
        return detalles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imgDet;
        TextView txtDesc, txtPreTot, txtPreUni, txtCant, idArt, idVent;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder =new ViewHolder();
        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtCant = row.findViewById(R.id.txtCant);
            holder.txtDesc = row.findViewById(R.id.txtDesc);
            holder.txtPreTot = row.findViewById(R.id.txtPreTot);
            holder.imgDet = row.findViewById(R.id.imgDetalle);
            holder.txtPreUni = row.findViewById(R.id.txtPreUni);
            holder.idArt = row.findViewById(R.id.idArt);
            holder.idVent = row.findViewById(R.id.idVent);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder)row.getTag();
        }
        DetalleVentasBean menu = detalles.get(i);
        holder.txtCant.setText("Cant: " + menu.getCANTIDAD());
        holder.txtDesc.setText("Plato: " +menu.getDESCRIPCION());
        holder.txtPreTot.setText("Costo Total: S/." + menu.getPRECIO_TOTAL()+"0");
        holder.txtPreUni.setText("Precio: S/." + menu.getPRECIO_UNITARIO()+"0");
        holder.idVent.setText(menu.getID_VENTAS());
        holder.idArt.setText(menu.getID_ARTICULO());
        Uri uri = Uri.parse(menu.getRUTA_IMAGEN());
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.imgDet.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.w(utils.APP_NAME,"Aquí está el error: " +e.getMessage());
        }
        return row;
    }
}
