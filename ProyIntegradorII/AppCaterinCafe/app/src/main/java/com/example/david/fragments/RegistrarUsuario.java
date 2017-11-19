package com.example.david.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.TablaBean;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RegistrarUsuario extends Fragment implements View.OnClickListener {
    EditText edtNombre, edtCorreo, edtUsuario, edtClave, edtApellido;
    TextView edtNiveles;
    Button btnRegistrar;
    ProgressDialog prgDialog;
    ProgressDialog prgDialogNiveles;
    Utilitarios utils = new Utilitarios();

    ArrayList<TablaBean> listaNiveles = new ArrayList<>();
    int idNivel = 0;

    private void iniciarComponentes(View view){
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtApellido = view.findViewById(R.id.edtApellido);
        edtUsuario = view.findViewById(R.id.edtUsuario);
        edtNiveles = view.findViewById(R.id.edtNiveles);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtClave = view.findViewById(R.id.edtClaveWeb);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Registrando Usuario");
        prgDialog.setCancelable(false);

        prgDialogNiveles = new ProgressDialog(getActivity());
        prgDialogNiveles.setMessage("Listando Niveles de Acceso...");
        prgDialogNiveles.setCancelable(false);
    }

    public void dialogNiveles(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(" Seleccione un Cargo ");
        final List<String> lstDescripciones = new ArrayList<>();
        final List<Integer> lstCodigos = new ArrayList<>();
        for(int i=0;i<listaNiveles.size(); i++){
            lstDescripciones.add(listaNiveles.get(i).getDESCRIPCION());
            lstCodigos.add(listaNiveles.get(i).getID());
        }
        final CharSequence[] allCargos = lstDescripciones.toArray(new String[lstDescripciones.size()]);
        dialog.setItems(allCargos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String valueSelect = allCargos[i].toString();
                edtNiveles.setText(valueSelect);
                edtNiveles.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down,0);

                for(TablaBean b : listaNiveles){
                    if(b.getDESCRIPCION().equals(valueSelect)){
                        idNivel = b.getID();
                    }
                }
            }
        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(R.color.white);
    }

    public void callWsNiveles(){
        prgDialogNiveles.show();
        listaNiveles.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaNiveles", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONArray jsonArray = null;
                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);

                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TablaBean bean = new TablaBean();
                            bean.setID(jsonObject.optInt("id"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            listaNiveles.add(bean);
                        }
                        prgDialogNiveles.dismiss();
                        dialogNiveles();
                    }else{
                        prgDialogNiveles.dismiss();
                        Toast.makeText(getActivity(),"Array Vacío.", Toast.LENGTH_SHORT).show();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogNiveles.dismiss();
                Toast.makeText(getActivity(),"Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_usuario, container, false);
        iniciarComponentes(view);
        btnRegistrar.setOnClickListener(this);
        edtNiveles.setOnClickListener(this);
        ((NavigationActivity)getActivity()).setActionBarTitle("REGISTRAR USUARIO");
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view== btnRegistrar){
            if(validarCampos(getActivity())){
                RequestParams params = new RequestParams();
                params.put("email", edtCorreo.getText().toString().trim());
                validarCorreo(params);
            }
        }
        if(view == edtNiveles){
            callWsNiveles();
        }
    }

    public void grabarUsuario(){
        RequestParams params = new RequestParams();
        params.put("nombre", edtNombre.getText().toString());
        params.put("apellido", edtApellido.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        params.put("usuario", edtUsuario.getText().toString());
        params.put("claveWeb", edtClave.getText().toString());
        params.put("nivelAcesso",idNivel);
        registrarUsuarioWS(params);
    }

    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        if (edtNombre.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!","Ingrese nombres",ctx);
            valid = false;
        }else if (edtApellido.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!","Ingrese apellido",ctx);
            valid = false;
        }
        else if (edtCorreo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!","Ingrese correo electrónico",ctx);
            valid = false;
        }
        else if (edtUsuario.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!","Ingrese usuario",ctx);
            valid = false;
        }
        else if (edtClave.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!","Ingrese clave",ctx);
            valid = false;
        }else if(idNivel==0){
            Utilitarios.validarCampos("Atención!!", "Seleccione un nivel", ctx);
            valid = false;
        }
        return valid;
    }

    private void registrarUsuarioWS(RequestParams params){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "registroUsuario", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    Utilitarios.validarCampos("Registro Exitoso!!","Cuenta enviada al correo del usuario creado.",getActivity());
                }else
                    Utilitarios.validarCampos("Error!!","Error al registrar usuario.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor",error.getMessage(),getActivity());
            }
        });
    }

    public void validarCorreo(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "validarCorreo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.w("appcaterincafe", "CODE A:" + statusCode);
                if(statusCode==202){
                    grabarUsuario();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.w("appcaterincafe", "CODE B:" + statusCode);
                Utilitarios.validarCampos("Atención", "El correo electrónico ya existe", getActivity());
            }
        });
    }
}
