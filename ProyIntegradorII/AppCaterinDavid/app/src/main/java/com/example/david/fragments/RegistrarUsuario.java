package com.example.david.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.NivelesBean;
import com.example.david.conexion.ConexionWs;
import com.example.david.dao.TablesDAO;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RegistrarUsuario extends Fragment implements View.OnClickListener {
    EditText edtNombre, edtCorreo, edtUsuario, edtClave;
    Spinner spnNivelAccesso;
    Button btnRegistrar;
    ArrayList<NivelesBean> niveles;
    ProgressDialog prgDialog;
    ConexionWs cnws = new ConexionWs();

    private void cargarSpinners(Context ctx) {
        TablesDAO dao = new TablesDAO(ctx);
        niveles = dao.niveles();
        ArrayAdapter<NivelesBean> adapterN = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, niveles);
        spnNivelAccesso.setAdapter(adapterN);
    }

    private void iniciarComponentes(View view){
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtUsuario = view.findViewById(R.id.edtUsuario);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtClave = view.findViewById(R.id.edtClaveWeb);
        spnNivelAccesso = view.findViewById(R.id.spnNivelAcceso);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        prgDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_usario, container, false);
        iniciarComponentes(view);
        cargarSpinners(getActivity());
        btnRegistrar.setOnClickListener(this);
        ((NavigationActivity)getActivity()).setActionBarTitle("REGISTRAR USUARIO");
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view== btnRegistrar){
            if(validarCampos(getActivity())){
                grabarUsuario(getActivity());
            }
        }
    }

    void grabarUsuario(Context ctx){
        RequestParams params = new RequestParams();
        params.put("nombre", edtNombre.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        params.put("usuario", edtUsuario.getText().toString());
        params.put("claveWeb", edtClave.getText().toString());
        params.put("nivelAcesso",((NivelesBean) spnNivelAccesso.getItemAtPosition(spnNivelAccesso.getSelectedItemPosition())).getID_NIVEL_ACCESO());
        registrarUsuarioWS(params,ctx);
    }

    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        if (edtNombre.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese nombres",ctx);
            valid = false;
        }
        else if (edtCorreo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese correo electrónico",ctx);
            valid = false;
        }
        else if (edtUsuario.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese usuario",ctx);
            valid = false;
        }
        else if (edtClave.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese clave",ctx);
            valid = false;
        }
        return valid;
    }

    private void registrarUsuarioWS(RequestParams params, final Context ctx){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(cnws.urlservice + "registroUsuario", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    Toast.makeText(ctx,"Usuario registrado.", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ctx,"Error al registrar usuario.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Toast.makeText(ctx, "Ocurrió un error con el servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
