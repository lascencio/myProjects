package com.example.david.appcaterincafe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.conexion.ConexionWs;
import com.example.david.utils.Session;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUser, edtPassword;
    TextView txtRegister;
    Button btnLogin;
    private Session session;
    ProgressDialog prgDialog;
    ConexionWs cnws = new ConexionWs();

    private void iniciarComponentes() {
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        prgDialog = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniciarComponentes();
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ALERTA!");
            builder.setMessage("¿Desea salir de la APP?").setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();;
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            if (validarCampos()) {
                RequestParams params = new RequestParams();
                params.put("user", edtUser.getText().toString());
                params.put("clave", edtPassword.getText().toString());
                loginWS(params, this);
            }
        }
        if (view == txtRegister) {
            startActivity(new Intent(LoginActivity.this, RegistrarClienteActivity.class));
        }
    }

    public boolean validarCampos() {
        boolean valid = true;
        if (edtUser.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese usuario",this);
            valid = false;
        }
        else if (edtPassword.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos","Ingrese contraseña",this);
            valid = false;
        }
        return valid;
    }


    private void loginWS(RequestParams params, final Context ctx){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(cnws.urlservice + "login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONObject jsonObject = null;
                if(statusCode==202){
                    try {
                        responseStr = new String(responseBody, "UTF-8");
                        jsonObject = new JSONObject(responseStr);
                        cnws.id = jsonObject.optString("id");
                        cnws.nombre = jsonObject.optString("nombre");
                        cnws.apellido = jsonObject.optString("apellido");
                        cnws.correo = jsonObject.optString("correo");
                        cnws.acceso_cargo = jsonObject.optInt("acceso_cargo");
                        cnws.estado = jsonObject.optInt("estado");
                        finish();
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(ctx,"Error loguear cliente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ctx, "Ocurrió un error con el servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}