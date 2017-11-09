package pe.edu.cibertec.cateringdemo.appcaterincafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import pe.edu.cibertec.cateringdemo.Conexion.ConexionWs;
import pe.edu.cibertec.cateringdemo.R;

public class Login extends AppCompatActivity {

    EditText edt_usuario,edt_clave;
    TextView txtRegister;
    Button btnIngresar;
    ConexionWs ws = new ConexionWs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarVariabler();
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_validacion();
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    void inicializarVariabler(){
        edt_usuario = (EditText)findViewById(R.id.edt_usuario);
        edt_clave   = (EditText)findViewById(R.id.edt_clave);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        txtRegister = (TextView)findViewById(R.id.txtRegister);
    }

    void call_validacion(){
        RequestParams params = new RequestParams();
        params.put("email",edt_usuario.getText().toString().trim());
        try {
            params.put("passwd",ws.getMD5(edt_clave.getText().toString().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        validarAcceso(params);
    }

    /*METODOS WS*/

    void validarAcceso(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ws.urlservice+"validarLoginAPP", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONObject jsonObject = null;

                if(statusCode==202){
                    try {
                        responseStr = new String(responseBody, "UTF-8");
                        jsonObject = new JSONObject(responseStr);
                        int cod_estado = jsonObject.optInt("estado");
                        if(cod_estado==1){
                            ws.cod_usr = jsonObject.optString("codigo");
                            ws.nombre_usr = jsonObject.optString("nombre");
                            ws.apellido_usr = jsonObject.optString("apellido");
                            ws.perfil_usr = jsonObject.optInt("acceso");
                            ws.correo_usr = jsonObject.optString("correo");
                        /**/
                            finish();
                            Intent intent = new Intent(Login.this, MenuPrincipal.class);
                            startActivity(intent);
                        }else{
                            AlertaValidacion("Alerta","Reivise su correo electrónico para verificar su cuenta.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("CODE : " +statusCode);
                Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     *
     */
    void AlertaValidacion(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(title);
        builder.setMessage(message).setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
