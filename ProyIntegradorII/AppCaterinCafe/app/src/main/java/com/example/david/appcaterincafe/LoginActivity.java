package com.example.david.appcaterincafe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUser, edtPassword;
    TextView txtRegister;
    Button btnLogin;
    ProgressDialog prgDialog;
    Utilitarios utils = new Utilitarios();

    private void iniciarComponentes() {
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Validando Ingreso");
        prgDialog.setCancelable(false);
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
                dialogInterface.cancel();
                ;
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
                loginWS(params);
            }
        }
        if (view == txtRegister) {
            startActivity(new Intent(LoginActivity.this, RegistrarClienteActivity.class));
        }
    }

    public boolean validarCampos() {
        boolean valid = true;
        if (edtUser.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese usuario", this);
            valid = false;
        } else if (edtPassword.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese contraseña", this);
            valid = false;
        }
        return valid;
    }


    private void loginWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONObject jsonObject = null;
                if (statusCode == 202) {
                    try {
                        idVenta();
                        responseStr = new String(responseBody, "UTF-8");
                        jsonObject = new JSONObject(responseStr);
                        utils.id_usuario = jsonObject.optString("id");
                        utils.nombre = jsonObject.optString("nombre");
                        utils.apellido = jsonObject.optString("apellido");
                        utils.correo = jsonObject.optString("correo");
                        utils.acceso_cargo = jsonObject.optInt("acceso_cargo");
                        utils.estado = jsonObject.optInt("estado");
                        finish();
                        prgDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Utilitarios.validarCampos("Error!", "Usuario o clave incorrectos.", LoginActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                if(error.getMessage().equals("Petición incorrecta")){
                    Utilitarios.validarCampos("Error Login!", "Usuario o clave incorrectos.", LoginActivity.this);
                }
                else
                    Utilitarios.validarCampos("Ocurrió un error con el servidor: : ", error.getMessage(), LoginActivity.this);
            }
        });
    }

    public void idVenta() {
        utils.id_venta = "";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/idVenta", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                try {
                    responseStr = new String(responseBody, "UTF-8");
                    utils.id_venta = responseStr;
                    Log.w(utils.APP_NAME, "ID VENTA:" + utils.id_venta);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}