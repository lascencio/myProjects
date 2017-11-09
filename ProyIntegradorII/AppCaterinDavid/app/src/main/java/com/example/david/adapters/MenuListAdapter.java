package com.example.david.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.appcaterincafe.R;
import com.example.david.beans.ArticuloBean;

import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter{
    private Context context;
    private int layout;
    private ArrayList<ArticuloBean> menus;

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
        TextView txtDesc, txtPrecio;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder =new ViewHolder();
        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtDesc = (TextView)row.findViewById(R.id.txtDesc);
            holder.txtPrecio = (TextView)row.findViewById(R.id.txtPrecio);
            holder.imgMenu = (ImageView) row.findViewById(R.id.imgMenu);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder)row.getTag();
        }
        ArticuloBean menu = menus.get(i);

        holder.txtDesc.setText(menu.getDESCRIPCION());
        holder.txtPrecio.setText(menu.getPRECIO_UNITARIO()+"");
        byte[]menuImage = menu.getRUTA_IMAGEN();
        Bitmap bitmap = BitmapFactory.decodeByteArray(menuImage,0,menuImage.length);
        holder.imgMenu.setImageBitmap(bitmap);

        return row;
    }
}
