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
import com.example.david.beans.ArticuloBean;
import com.example.david.utils.Utilitarios;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private ArrayList<ArticuloBean> menus;
    Utilitarios utils;

    public MenuListAdapter(Context context, int layout, ArrayList<ArticuloBean> menus) {
        this.context = context;
        this.layout = layout;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imgMenu;
        TextView txtDesc, txtPrecio, txtTipo, txtId;
        String idArticulo;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder =new ViewHolder();
        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtId = row.findViewById(R.id.idArticulo);
            holder.txtDesc = row.findViewById(R.id.txtDesc);
            holder.txtPrecio = row.findViewById(R.id.txtPrecio);
            holder.imgMenu = row.findViewById(R.id.imgDetalle);
            holder.txtTipo = row.findViewById(R.id.txtTipo);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder)row.getTag();
        }
        ArticuloBean menu = menus.get(i);
        holder.txtId.setText(menu.getID_ARTICULO());
        holder.txtDesc.setText(menu.getDESCRIPCION());
        holder.txtPrecio.setText("Costo: S/." + menu.getPRECIO_UNITARIO()+"0");
        holder.txtTipo.setText("Tipo: Menú");
        Uri uri = Uri.parse(menu.getRUTA_IMAGEN());
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            holder.imgMenu.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.w(utils.APP_NAME,"Aquí está el error: " +e.getMessage());

        }
        return row;
    }
}
