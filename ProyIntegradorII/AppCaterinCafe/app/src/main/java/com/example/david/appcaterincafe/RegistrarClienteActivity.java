package com.example.david.appcaterincafe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistrarClienteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtClave, edtNombre, edtCorreo, edtApellido;
    TextView edtCargos;
    Button btnRegistrar;
    RadioGroup rdgAutoriza;
    RadioButton rbtSi, rbtNo;
    ProgressDialog prgDialog;
    ProgressDialog prgDialogCargos;
    Utilitarios utils = new Utilitarios();

    ArrayList<TablaBean> listaCargos = new ArrayList<>();
    int idCargo = 0;

    private void iniciarComponentes() {
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtClave = (EditText) findViewById(R.id.edtClave);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtCargos = (TextView) findViewById(R.id.edtCargos);
        btnRegistrar = (Button) findViewById(R.id.btnRegister);
        rdgAutoriza = (RadioGroup) findViewById(R.id.rdgAutoriza);
        rbtNo = (RadioButton) findViewById(R.id.rbtNo);
        rbtSi = (RadioButton) findViewById(R.id.rbtSi);

        btnRegistrar.setOnClickListener(this);
        edtCargos.setOnClickListener(this);

        rdgAutoriza.check(R.id.rbtNo);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Registrando Cliente");
        prgDialog.setCancelable(false);

        prgDialogCargos = new ProgressDialog(this);
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
                        Toast.makeText(getApplicationContext(), "Array Vacío.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dialogCargos() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(" Seleccione Cargo ");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);
        iniciarComponentes();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(utils.negrita("REGISTRATE AQUÍ"));
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegistrar) {
            if (validarCampos()) {
                RequestParams params = new RequestParams();
                params.put("email", edtCorreo.getText().toString().trim());
                validarCorreo(params);
            }

        }
        if (view == edtCargos) {
            callWsCargos();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validarCampos() {
        boolean valid = true;
        if (edtNombre.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese nombre", this);
            valid = false;
        } else if (edtApellido.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese apellido", this);
            valid = false;
        } else if (edtCorreo.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese correo electrónico", this);
            valid = false;
        } else if (edtClave.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese clave", this);
            valid = false;
        } else if (idCargo == 0) {
            Utilitarios.validarCampos("Atención!!", "Seleccione un cargo", this);
            valid = false;
        }
        return valid;

    }

    public void grabarCliente() {
        RequestParams params = new RequestParams();
        params.put("nombre", edtNombre.getText().toString());
        params.put("apellido", edtApellido.getText().toString());
        params.put("correo", edtCorreo.getText().toString());
        params.put("clave", edtClave.getText().toString());
        params.put("rolid", idCargo);
        params.put("autoriza", indicaAutoriza());
        registrarClienteWS(params);
    }

    public String indicaAutoriza() {
        if (rbtSi.isChecked()) {
            return "SI";
        } else
            return "NO";
    }

    private void registrarClienteWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "registroCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if (statusCode == 201) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarClienteActivity.this);
                    builder.setTitle("REGISTRO EXITOSO!");
                    builder.setMessage("Por favor verifique su correo electrónico.").setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(RegistrarClienteActivity.this, LoginActivity.class));
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                } else
                    Toast.makeText(getApplicationContext(), "Error al registrar cliente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ocurrió un error con el servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                Utilitarios.validarCampos("Atención", "El correo electrónico ya existe", RegistrarClienteActivity.this);
            }
        });
    }
}
