package pe.edu.cibertec.cateringdemo.appcaterincafe.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import pe.edu.cibertec.cateringdemo.Conexion.ConexionWs;
import pe.edu.cibertec.cateringdemo.R;
import pe.edu.cibertec.cateringdemo.appcaterincafe.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_perfil extends Fragment {

    View v;
    EditText nombres,appelidos;
    Button btnGrabar;
    ConexionWs ws = new ConexionWs();
    ProgressDialog progressDialog;

    public fr_perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Mi Perfil");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_perfil, container, false);
        inicializarVariables();
        setDatosCliente();

        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.put("nombres", nombres.getText().toString().trim());
                params.put("apellidos", appelidos.getText().toString().trim());
                params.put("cod_usr", ws.cod_usr);
                WSModificarDatos(params);
            }
        });

        return v;
    }

    void inicializarVariables(){
        nombres = (EditText)v.findViewById(R.id.edt_nombres);
        appelidos = (EditText)v.findViewById(R.id.edt_apellidos);
        btnGrabar = (Button)v.findViewById(R.id.btnGrabar);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    void setDatosCliente(){
        nombres.setText(ws.nombre_usr);
        appelidos.setText(ws.apellido_usr);
    }

    void WSModificarDatos(RequestParams params){
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ws.urlservice+"modificarDatos", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                if(statusCode==200){
                    AlertaValidacion("Alerta","Se grabaron los datos con exito!");
                }else{
                    AlertaValidacion("Error", "No se pudo grabar los datos.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                AlertaValidacion("Error", "No se pudo grabar los datos.");
            }
        });
    }

    void AlertaValidacion(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
