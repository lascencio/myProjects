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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ActualizarCliente extends Fragment implements View.OnClickListener {
    EditText edtNombre, edtCorreo, edtSaldo, edtApellido;
    TextView edtCargos;
    Button btnActualizar;
    RadioGroup rdgAutoriza;
    RadioButton rbtSi, rbtNo;
    ArrayList<TablaBean> listaCargos = new ArrayList<>();
    int idCargo = 0;
    ProgressDialog prgDialogClientes;
    ProgressDialog prgDialogCargos;

    Utilitarios utils = new Utilitarios();

    private void iniciarComponentes(View view){
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtCargos = view.findViewById(R.id.edtCargos);
        edtSaldo = view.findViewById(R.id.edtSaldo);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtApellido = view.findViewById(R.id.edtApellido);
        btnActualizar = view.findViewById(R.id.btnActualizar);
        rdgAutoriza = view.findViewById(R.id.rdgAutoriza);
        rbtNo = view.findViewById(R.id.rbtNo);
        rbtSi = view.findViewById(R.id.rbtSi);

        prgDialogClientes = new ProgressDialog(getActivity());
        prgDialogClientes.setMessage("Actualizando Cliente...");
        prgDialogClientes.setCancelable(false);

        prgDialogCargos = new ProgressDialog(getActivity());
        prgDialogCargos.setMessage("Listando Cargos...");
        prgDialogCargos.setCancelable(false);

    }

    public void callWsCargos() {
        prgDialogCargos.show();
        listaCargos.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaCargos", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONArray jsonArray = null;
                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TablaBean bean = new TablaBean();
                            bean.setID(jsonObject.optInt("id"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            listaCargos.add(bean);
                        }
                        prgDialogCargos.dismiss();
                        dialogCargos();
                    } else {
                        prgDialogCargos.dismiss();
                        Toast.makeText(getActivity(), "Array Vacío.", Toast.LENGTH_SHORT).show();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogCargos.dismiss();
                Toast.makeText(getActivity(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void llenarArray() {
        prgDialogCargos.show();
        listaCargos.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaCargos", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONArray jsonArray = null;
                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TablaBean bean = new TablaBean();
                            bean.setID(jsonObject.optInt("id"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            listaCargos.add(bean);
                        }
                        prgDialogCargos.dismiss();
                    } else {
                        prgDialogCargos.dismiss();
                        Toast.makeText(getActivity(), "Array Vacío.", Toast.LENGTH_SHORT).show();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogCargos.dismiss();
                Toast.makeText(getActivity(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dialogCargos() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        dialog.setTitle(" Seleccione un Cargo ");
        final List<String> lstDescripciones = new ArrayList<>();
        final List<Integer> lstCodigos = new ArrayList<>();
        for (int i = 0; i < listaCargos.size(); i++) {
            lstDescripciones.add(listaCargos.get(i).getDESCRIPCION());
            lstCodigos.add(listaCargos.get(i).getID());
        }
        final CharSequence[] allCargos = lstDescripciones.toArray(new String[lstDescripciones.size()]);
        dialog.setItems(allCargos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String valueSelect = allCargos[i].toString();
                edtCargos.setText(valueSelect);
                edtCargos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
                for (TablaBean b : listaCargos) {
                    if (b.getDESCRIPCION().equals(valueSelect)) {
                        idCargo = b.getID();
                    }
                }
            }
        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(R.color.white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualizar_cliente, container, false);
        iniciarComponentes(view);
        btnActualizar.setOnClickListener(this);
        edtCargos.setOnClickListener(this);
        ((NavigationActivity)getActivity()).setActionBarTitle("ACTUALIZAR PERFIL");
        llenarArray();
        consultarClienteWS();
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==btnActualizar){
            if(validarCampos(getActivity())){
                if(edtCorreo.getText().toString().equals(utils.correo)){
                    grabarCliente();
                }else{
                    RequestParams params = new RequestParams();
                    params.put("email", edtCorreo.getText().toString().trim());
                    validarCorreo(params);
                }
            }
        }
        if(view == edtCargos){
            callWsCargos();
        }
    }

    private String getTextoCargo(int id) {
        String pos = "nada";
        idCargo = id;
        Log.w("appcaterincafe",id + "");
        for(TablaBean b : listaCargos){
            Log.w("appcaterincafe",b.getID() + "");
            Log.w("appcaterincafe",b.getDESCRIPCION() + "");
            if(b.getID() == id){
                pos = b.getDESCRIPCION().toString();
            }
        }
        return pos;
    }

    void grabarCliente(){
        RequestParams params = new RequestParams();
        params.put("id", utils.id_usuario);
        params.put("nombre", edtNombre.getText().toString());
        params.put("apellido",edtApellido.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        params.put("autoriza", indicaAutoriza());
        params.put("saldo", Double.parseDouble(edtSaldo.getText().toString()));
        params.put("rolid",idCargo);
        actualizarClienteWS(params);
    }

    public String indicaAutoriza() {
        if (rbtSi.isChecked()) {
            return "SI";
        } else
            return "NO";
    }

    public void chekRadio(String indica){
        if(indica.equals("SI")){
            rdgAutoriza.check(R.id.rbtSi);
        }else
            rdgAutoriza.check(R.id.rbtNo);
    }

    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        if (edtNombre.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese nombres",ctx);
            valid = false;
        }
        else if (edtApellido.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese apellidos",ctx);
            valid = false;
        }
        else if (edtCorreo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese correo electrónico",ctx);
            valid = false;
        }
        else if (edtSaldo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese saldo actual",ctx);
            valid = false;
        }
        return valid;
    }

    private void actualizarClienteWS(RequestParams params){
        prgDialogClientes.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "actualizaCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialogClientes.dismiss();
                if(statusCode==201){
                    Utilitarios.validarCampos("Actualización Exitosa!!","Cliente actualizado correctamente.",getActivity());
                }else
                    Utilitarios.validarCampos("Error!!","Error al actualizar cliente.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogClientes.dismiss();
                Utilitarios.validarCampos("Ocurrió un error en el servidor...: ",error.getMessage(),getActivity());
            }
        });
    }

    private void consultarClienteWS(){
        RequestParams params = new RequestParams();
        params.put("id", utils.id_usuario);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "consultarCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONObject jsonObject = null;
                if(statusCode==202){
                    try {
                        responseStr = new String(responseBody, "UTF-8");
                        jsonObject = new JSONObject(responseStr);
                        edtNombre.setText(jsonObject.optString("nombre"));
                        edtApellido.setText(jsonObject.optString("apellido"));
                        edtCorreo.setText(jsonObject.optString("correo"));
                        edtSaldo.setText(jsonObject.optDouble("saldo") +"");
                        edtCargos.setText(getTextoCargo(jsonObject.optInt("id_CARGO")));
                        chekRadio(jsonObject.optString("indica_AUTORIZA"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    Utilitarios.validarCampos("Error: ","Error al mostrar datos de cliente.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogClientes.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor: ",error.getMessage(),getActivity());
            }
        });
    }

    public void validarCorreo(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "validarCorreo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.w("appcaterincafe", "CODE A:" + statusCode);
                if (statusCode == 202) {
                    grabarCliente();
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
