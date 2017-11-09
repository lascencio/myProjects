package com.example.david.appcaterincafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.david.beans.CargosBean;
import com.example.david.conexion.ConexionWs;
import com.example.david.dao.TablesDAO;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.security.MessageDigest;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RegistrarClienteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtClave, edtNombre, edtCorreo, edtApellido;
    Button btnRegistrar;
    RadioGroup rdgAutoriza;
    RadioButton rbtSi, rbtNo;
    Spinner spnCargos;
    ArrayList<CargosBean> cargos;
    int rbtSelected;
    ProgressDialog prgDialog;
    ConexionWs cnws = new ConexionWs();

    private void iniciarComponentes() {
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtClave = (EditText) findViewById(R.id.edtClave);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        spnCargos = (Spinner) findViewById(R.id.spnCargos);
        btnRegistrar = (Button) findViewById(R.id.btnRegister);
        rdgAutoriza = (RadioGroup) findViewById(R.id.rdgAutoriza);
        rbtNo = (RadioButton) findViewById(R.id.rbtNo);
        rbtSi = (RadioButton) findViewById(R.id.rbtSi);
        btnRegistrar.setOnClickListener(this);
        rdgAutoriza.check(R.id.rbtNo);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Registrando Usuario");
        prgDialog.setCancelable(false);
    }

    private void cargarSpinners() {
        TablesDAO dao = new TablesDAO(this);
        cargos = dao.cargos();
        ArrayAdapter<CargosBean> adapterC = new ArrayAdapter<CargosBean>(this, android.R.layout.simple_list_item_1, cargos);
        spnCargos.setAdapter(adapterC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);
        iniciarComponentes();
        cargarSpinners();
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegistrar) {
            if (validarCampos()) {
                grabarCliente();
                startActivity(new Intent(RegistrarClienteActivity.this,LoginActivity.class));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean validarCampos() {
        boolean valid = true;
        if (edtNombre.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese nombre",this);
            valid = false;
        }
        else if (edtApellido.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese apellido",this);
            valid = false;
        }
        else if (edtCorreo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese correo electrónico",this);
            valid = false;
        }
        else if (edtClave.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese clave",this);
            valid = false;
        }
        return valid;
    }


    void grabarCliente(){
        RequestParams params = new RequestParams();
        params.put("nombre", edtNombre.getText().toString());
        params.put("apellido",edtApellido.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        String claveMD5 = edtClave.getText().toString();
        try {
            params.put("clave",getMD5(claveMD5));
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("autoriza", validarCheckBoxAutoriza());
        params.put("rolid",((CargosBean)spnCargos.getItemAtPosition(spnCargos.getSelectedItemPosition())).getID_CARGO());
        registrararClienteWS(params);
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

    private void registrararClienteWS(RequestParams params){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(cnws.urlservice + "registroCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    Toast.makeText(getApplicationContext(),"Cliente creado.", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(),"Error al registrar cliente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ocurrió un error con el servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getMD5(String cadena)throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(cadena.getBytes());
        int size = b.length;
        StringBuilder h = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int u = b[i] & 255;
            if (u < 16)
            {
                h.append("0").append(Integer.toHexString(u));
            }
            else
            {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }
}
