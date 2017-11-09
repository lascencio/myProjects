package com.example.david.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.david.adapters.MenuListAdapter;
import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.ArticuloBean;
import com.example.david.conexion.MiConexion;
import com.example.david.dao.TablesDAO;

import java.util.ArrayList;

public class CompraAhora extends Fragment {

    GridView gridView;
    ArrayList<ArticuloBean> articulos = new ArrayList<ArticuloBean>();
    MenuListAdapter adapter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compra_ahora, container, false);
        ((NavigationActivity)getActivity()).setActionBarTitle("COMPRA AHORA");
        gridView = view.findViewById(R.id.gridView);
        TablesDAO dao = new TablesDAO(getActivity());
        articulos = dao.articulos();
        adapter = new MenuListAdapter(getActivity(),R.layout.item_menus,articulos);
        gridView.setAdapter(adapter);

        //Cursor cursor = sqLiteHelper.getData("select * from ARTICULO");
//        articulos.clear();
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String desc = cursor.getString(1);
//            double pre = cursor.getDouble(2);
//            byte[] img = cursor.getBlob(3);
//            ArticuloBean bean = new ArticuloBean();
//            bean.setID_ARTICULO(id);
//            bean.setDESCRIPCION(desc);
//            bean.setPRECIO_UNITARIO(pre);
//            bean.setRUTA_IMAGEN(img);
//            articulos.add(bean);
//        }
//        adapter.notifyDataSetChanged();

        return view;

    }

}
