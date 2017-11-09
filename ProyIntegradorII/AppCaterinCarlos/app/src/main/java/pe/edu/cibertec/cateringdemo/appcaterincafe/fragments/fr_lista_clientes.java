package pe.edu.cibertec.cateringdemo.appcaterincafe.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pe.edu.cibertec.cateringdemo.Beans.ListaClientesBE;
import pe.edu.cibertec.cateringdemo.Conexion.ConexionWs;
import pe.edu.cibertec.cateringdemo.R;
import pe.edu.cibertec.cateringdemo.appcaterincafe.Adapters.AdapterListaClientes;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_lista_clientes extends Fragment {

    View v;
    ConexionWs ws = new ConexionWs();
    ProgressDialog progressDialog;

    public fr_lista_clientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Mis Clientes");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_lista_clientes, container, false);
        inicializarVariables();
        onDestroy();
        listarClientes();
        return v;
    }

    void inicializarVariables(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void listarClientes(){
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ws.urlservice+"listaClientes", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONArray jsonArray = null;

                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);
                    //System.out.println(jsonArray);

                    if(jsonArray.length()>0){
                        if(getActivity()!=null){
                            progressDialog.dismiss();
                            ArrayList<ListaClientesBE> array_clientes = new ArrayList<>();
                            AdapterListaClientes adapterAtenciones = new AdapterListaClientes(getActivity(), array_clientes);
                            ListView listView = (ListView) v.findViewById(R.id.lista_clientes);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ListaClientesBE lstCli = new ListaClientesBE();
                                lstCli.setId_cliente(jsonObject.optString("id_cliente"));
                                lstCli.setNombre(jsonObject.optString("nombre"));
                                lstCli.setApelldio(jsonObject.optString("apelldio"));
                                lstCli.setCorreo(jsonObject.optString("correo"));
                                lstCli.setTipoCliente(jsonObject.optString("tipoCliente"));
                                lstCli.setIndicaAutoriza(jsonObject.optString("indicaAutoriza"));
                                lstCli.setEstado(jsonObject.optString("estado"));
                                lstCli.setFechaRegistro(jsonObject.optString("fechaRegistro"));

                                adapterAtenciones.add(lstCli);
                            }
                            listView.setAdapter(adapterAtenciones);
                        }
                    }else{
                        progressDialog.dismiss();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                if(getActivity()!=null){
                    Toast.makeText(getContext(), "Ocurrio un error al cargar los Clientes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

}
