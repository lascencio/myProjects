package pe.edu.cibertec.cateringdemo.appcaterincafe;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import pe.edu.cibertec.cateringdemo.Beans.CargoBE;
import pe.edu.cibertec.cateringdemo.Conexion.ConexionWs;
import pe.edu.cibertec.cateringdemo.R;
import pe.edu.cibertec.cateringdemo.utils.validarEmail;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtClave, edtNombre,edtApellido,edtCorreo;
    TextView edtCargo;
    Button btnRegistrar;
    RadioGroup rdgAutoriza;
    RadioButton rbtSi, rbtNo;
    Spinner spnCargos;
    int rbtSelected;
    ProgressDialog prgDialog;
    ProgressDialog prgDialogCargos;
    ConexionWs ws = new ConexionWs();
    validarEmail validaMail = new validarEmail();

    ArrayList<CargoBE> listaCargos= new ArrayList<>();
    int idCargo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iniciarComponentes();

    }

    @Override
    public void onClick(View view) {
        if(view == btnRegistrar){
            CallgrabarCliente();
        }if(view == edtCargo){
            CallWSCargos();
        }

    }

    private void iniciarComponentes(){
        edtClave = (EditText) findViewById(R.id.edtClave);
        edtNombre = (EditText) findViewById(R.id.edtNomApe);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtCargo = (TextView) findViewById(R.id.edtCargos) ;
        btnRegistrar = (Button) findViewById(R.id.btnRegister);
        rdgAutoriza = (RadioGroup) findViewById(R.id.rdgAutoriza);
        rbtNo = (RadioButton) findViewById(R.id.rbtNo);
        rbtSi = (RadioButton) findViewById(R.id.rbtSi);
        btnRegistrar.setOnClickListener(this);
        edtCargo.setOnClickListener(this);
        rdgAutoriza.check(R.id.rbtNo);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Registrando Usuario");
        prgDialog.setCancelable(false);

        prgDialogCargos = new ProgressDialog(this);
        prgDialogCargos.setMessage("Listando Cargos...");
        prgDialogCargos.setCancelable(false);
    }

    void CallgrabarCliente(){
        if(edtNombre.getText().length()<1){
            AlertaValidacion("Atención", "Ingrese su nombre.");
        }else if(edtApellido.getText().length()<1){
            AlertaValidacion("Atención", "Ingrese sus Apellidos.");
        }else if(edtCorreo.getText().length()<1){
            AlertaValidacion("Atención", "Ingrese su correo electrónico.");
        }else if(validaMail.validateEmail(edtCorreo.getText().toString().trim())==false){
            AlertaValidacion("Atención", "Ingrese un correo electrónico Válido.");
        }else if(edtClave.getText().length()<1){
            AlertaValidacion("Atención", "Ingrese una contraseña.");
        }else if(idCargo==0){
            AlertaValidacion("Atención", "Seleccione un Cargo");
        }else{
            //grabarCliente();
            RequestParams params = new RequestParams();
            params.put("email",edtCorreo.getText().toString().trim());
            verificarCorreo(params);
        }
    }

    /**
     * Metodo para leer las cajas y enviarlo al WS
     */
    void grabarCliente(){
        RequestParams params = new RequestParams();
        params.put("nombre",edtNombre.getText().toString().trim());
        params.put("apellido",edtApellido.getText().toString().trim());
        params.put("correo", edtCorreo.getText().toString().trim());
        String claveMD5 = edtClave.getText().toString().trim();
        try {
            params.put("clave", ws.getMD5(claveMD5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("id_cargo",idCargo);
        params.put("autoriza",validarCheckBoxAutoriza());
        crearUsuarioWS(params);
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

    /**
     * Alert para validar campos
     * @param title
     * @param message
     */
    void AlertaValidacion(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(message).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    void dialogCargos(){
        android.app.AlertDialog.Builder dialog= new android.app.AlertDialog.Builder(this);
        dialog.setTitle(" Seleccione un Cargo ");
        final List<String> lstPacientes = new ArrayList<>();
        final List<Integer> lstCod_paci = new ArrayList<>();
        for(int i=0; i<listaCargos.size(); i++){
            lstPacientes.add(listaCargos.get(i).getStr_cargo());
            lstCod_paci.add(listaCargos.get(i).getId_cargo());
        }
        final CharSequence[] allPacientes = lstPacientes.toArray(new String[lstPacientes.size()]);
        dialog.setItems(allPacientes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String valueSelect = allPacientes[which].toString();
                edtCargo.setText(valueSelect);
                edtCargo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.downico2, 0);

                for (CargoBE pac : listaCargos){
                    if(pac.getStr_cargo().equals(valueSelect)){
                        idCargo=pac.getId_cargo();
                        //System.out.println(pac.getId_cargo());
                    }
                }

            }
        });
        android.app.AlertDialog dialog2 = dialog.create();
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(R.color.white);

    }


    /**
     * Metodo para guardar Cliente con WS
     * @param params
     */
    private void crearUsuarioWS(RequestParams params){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ws.urlservice+"registroCliente", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    //Toast.makeText(getApplicationContext(), "Registro con Exito!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Registro con Exito!");
                    builder.setMessage("Por favor revise su correo electrónico para verificar su cuenta.").setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo guardar el registro!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ocurrio un error con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    public void CallWSCargos(){
        prgDialogCargos.show();
        listaCargos.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ws.urlservice+"/listaCargos", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String responseStr = null;
                JSONArray jsonArray = null;

                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);

                    if(jsonArray.length()>0){

                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CargoBE objCargo = new CargoBE();
                            objCargo.setId_cargo(jsonObject.optInt("id_cargo"));
                            objCargo.setStr_cargo(jsonObject.optString("str_cargo"));
                            objCargo.setId_estado(jsonObject.optInt("id_estado"));
                            listaCargos.add(objCargo);
                            System.out.println(listaCargos);
                        }
                        prgDialogCargos.dismiss();
                        dialogCargos();


                    }else{
                        prgDialogCargos.dismiss();
                        Toast.makeText(getApplicationContext(), "Array Vacío.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogCargos.dismiss();
                Toast.makeText(getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void verificarCorreo(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ws.urlservice+"validarDatos", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("CODE A:" + statusCode);
                if(statusCode==202){
                    grabarCliente();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(getApplicationContext(), "Ocurrió un error con el Servidor.", Toast.LENGTH_SHORT).show();
                System.out.println("CODE B:" + statusCode);
                AlertaValidacion("Atención","El correo electrónico ya está registrado.");
            }
        });
    }



}
