package com.example.david.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;



public class ListaClientes extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_clientes, container, false);
        ((NavigationActivity)getActivity()).setActionBarTitle("MENÚ PRINCIPAL");

        return view;

    }

}
