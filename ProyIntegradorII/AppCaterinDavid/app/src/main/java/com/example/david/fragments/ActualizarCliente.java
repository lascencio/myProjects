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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.CargosBean;
import com.example.david.conexion.ConexionWs;
import com.example.david.dao.TablesDAO;
import com.example.david.utils.Session;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ActualizarCliente extends Fragment implements View.OnClickListener {
    EditText edtNombre, edtCorreo, edtSaldo, edtApellido;
    Spinner spnCargos;
    Button btnActualizar;
    RadioGroup rdgAutoriza;
    RadioButton rbtSi, rbtNo;
    ArrayList<CargosBean> cargos;
    private Session session;
    ProgressDialog prgDialog;
    ConexionWs cnws = new ConexionWs();
    int rbtSelected;

    private void cargarSpinners(Context ctx) {
        TablesDAO dao = new TablesDAO(ctx);
        cargos = dao.cargos();
        ArrayAdapter<CargosBean> adapterC = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, cargos);
        spnCargos.setAdapter(adapterC);
    }

    private void iniciarComponentes(View view){
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtSaldo = view.findViewById(R.id.edtSaldo);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtApellido = view.findViewById(R.id.edtApellido);
        spnCargos = view.findViewById(R.id.spnCargos);
        btnActualizar = view.findViewById(R.id.btnActualizar);
        rdgAutoriza = view.findViewById(R.id.rdgAutoriza);
        rbtNo = view.findViewById(R.id.rbtNo);
        rbtSi = view.findViewById(R.id.rbtSi);
        prgDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualizar_cliente, container, false);
        iniciarComponentes(view);
        cargarSpinners(getActivity());
        btnActualizar.setOnClickListener(this);
        ((NavigationActivity)getActivity()).setActionBarTitle("ACTUALIZAR PERFIL");
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==btnActualizar){
            if(validarCampos(getActivity())){
                grabarCliente(getActivity());
            }
        }
    }

    void grabarCliente(Context ctx){
        RequestParams params = new RequestParams();
        params.put("id", "C0000002");
        params.put("nombre", edtNombre.getText().toString());
        params.put("apellido",edtApellido.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        params.put("autoriza", validarCheckBoxAutoriza());
        params.put("rolid",((CargosBean)spnCargos.getItemAtPosition(spnCargos.getSelectedItemPosition())).getID_CARGO());
        actualizarClienteWS(params,ctx);
    }

    String validarCheckBoxAutoriza(){
        String estado = "";
        if(rbtSi.isChecked()){
            rbtSelected = 1;
            estado="SI";
        }else if(rbtNo.isChecked()){
            rbtSelected = 2;
            estado="NO";
        }
        return estado;
    }

    private int getPosicionCargos(int id) {
        int pos = -1;
        for (int i = 0; i < cargos.size(); i++) {
            CargosBean bean = cargos.get(i);
            if (bean.getID_CARGO() == id) {
                pos = i;
                break;
            }
        }
        return pos;
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

    private void actualizarClienteWS(RequestParams params, final Context ctx){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(cnws.urlservice + "actualizaCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    Toast.makeText(ctx,"Cliente actualizado.", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ctx,"Error al actualizar cliente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Toast.makeText(ctx, "Ocurrió un error con el servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void listarCliente(Context ctx){
//        session = new Session(ctx);
//        ClienteDAO dao = new ClienteDAO(ctx);
//        ClienteBean bean = dao.consultarCliente(session.getClienteUser(),session.getClaveWeb());
//        if(bean != null){
//            edtNomApe.setText(bean.getNOMBRE());
//            edtCorreo.setText(bean.getCORREO());
//            edtUsuario.setText(bean.getSALDO()+"");
//            spnNivelAccesso.setSelection(getPosicionCargos(bean.getID_NIVEL_ACCESO()));
//            ((RadioButton)rdgAutoriza.getChildAt(bean.getINDICA_AUTORIZA())).setChecked(true);
//        }
    }
}
