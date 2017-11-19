package com.example.david.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;


public class ActualizarProducto extends Fragment implements View.OnClickListener {

    EditText edtDescripcion, edtDescripcionAmpliada, edtPrecio, edtStock;
    TextView edtFamilias;
    RadioGroup rdgValida;
    RadioButton rbtSi, rbtNo;
    ArrayList<TablaBean> listaFamilias = new ArrayList<>();
    Button btnActualizar;
    ImageView imgProducto;

    String rutaImagen = "";
    int idFamilia = 0;

    Utilitarios utils = new Utilitarios();

    ProgressDialog prgDialogProducto;
    ProgressDialog prgDialogFamilias;
    ProgressDialog prgDialog;

    private void inicarComponentes(View view){
        edtDescripcion =  view.findViewById(R.id.edtDescripcion);
        edtDescripcionAmpliada =  view.findViewById(R.id.edtDescripcionAmpliada);
        edtPrecio = view.findViewById(R.id.edtPrecio);
        edtStock =  view.findViewById(R.id.edtStock);
        edtFamilias = view.findViewById(R.id.edtFamilias);
        btnActualizar = view.findViewById(R.id.btnActProducto);
        rdgValida = view.findViewById(R.id.rdgValida);
        rbtNo = view.findViewById(R.id.rbtNo);
        rbtSi = view.findViewById(R.id.rbtSi);
        imgProducto = view.findViewById(R.id.imgProducto);

        prgDialogProducto = new ProgressDialog(getActivity());
        prgDialogProducto.setMessage("Listando Producto...");
        prgDialogProducto.setCancelable(false);

        prgDialogFamilias = new ProgressDialog(getActivity());
        prgDialogFamilias.setMessage("Listando Familias...");
        prgDialogFamilias.setCancelable(false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Actualizando Producto...");
        prgDialog.setCancelable(false);

    }

    public void llenarArray() {
        listaFamilias.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaFamilias", new AsyncHttpResponseHandler() {
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
                            listaFamilias.add(bean);
                        }
                    } else {
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
                Toast.makeText(getActivity(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callWsFamilias(){
        prgDialogFamilias.show();
        listaFamilias.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaFamilias", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONArray jsonArray = null;
                try {
                    responseStr = new String(responseBody, "UTF-8");
                    jsonArray = new JSONArray(responseStr);

                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TablaBean bean = new TablaBean();
                            bean.setID(jsonObject.optInt("id"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            listaFamilias.add(bean);
                        }
                        prgDialogFamilias.dismiss();
                        dialogFamilias();
                    }else{
                        prgDialogFamilias.dismiss();
                        Utilitarios.validarCampos("Ocurrió un Error!!","Array vacío.",getActivity());
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogFamilias.dismiss();
                Utilitarios.validarCampos("Ocurrió un Error!!",error.getMessage(),getActivity());
            }
        });
    }

    public void dialogFamilias(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(" Seleccione Familia ");
        final List<String> lstDescripciones = new ArrayList<>();
        final List<Integer> lstCodigos = new ArrayList<>();
        for(int i=0;i<listaFamilias.size(); i++){
            lstDescripciones.add(listaFamilias.get(i).getDESCRIPCION());
            lstCodigos.add(listaFamilias.get(i).getID());
        }
        final CharSequence[] allCargos = lstDescripciones.toArray(new String[lstDescripciones.size()]);
        dialog.setItems(allCargos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String valueSelect = allCargos[i].toString();
                edtFamilias.setText(valueSelect);
                edtFamilias.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down,0);

                for(TablaBean b : listaFamilias){
                    if(b.getDESCRIPCION().equals(valueSelect)){
                        idFamilia = b.getID();
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
        View view = inflater.inflate(R.layout.fragment_actualizar_producto, container, false);
        inicarComponentes(view);
        llenarArray();
        consultarArticuloWS();
        btnActualizar.setOnClickListener(this);
        edtFamilias.setOnClickListener(this);
        imgProducto.setOnClickListener(this);

        ((NavigationActivity)getActivity()).setActionBarTitle("ACTUALIZAR PRODUCTO");
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == utils.REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, utils.REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getActivity(), "No tienes permisos para acceder a galería", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == utils.REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            rutaImagen = uri.toString();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProducto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if(view==btnActualizar){
            grabarArticulo();
        }
        if(view==imgProducto){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, utils.REQUEST_CODE_GALLERY);
        }
        if(view==edtFamilias){
            callWsFamilias();
        }
    }

    private String getTextoFamilia(int id) {
        String pos = "nada";
        idFamilia = id;
        Log.w("appcaterincafe", "Id JSON: " +id);
        for(TablaBean b : listaFamilias){
            Log.w("appcaterincafe","Id Familia: " +b.getID());
            Log.w("appcaterincafe","Desc: " +b.getDESCRIPCION());
            if(b.getID() == id){
                pos = b.getDESCRIPCION().toString();
                Log.w("appcaterincafe","Desc Fam Art: " +b.getDESCRIPCION());
            }
        }
        return pos;
    }

    public void setValidaStock(String indica){
        if(indica.equals("SI")){
            rdgValida.check(R.id.rbtSi);
        }else
            rdgValida.check(R.id.rbtNo);
    }

    private void consultarArticuloWS(){
        prgDialogProducto.show();
        RequestParams params = new RequestParams();
        params.put("id", utils.id_articulo);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "consultarArticulo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = null;
                JSONObject jsonObject = null;
                if(statusCode==202){
                    try {
                        responseStr = new String(responseBody, "UTF-8");
                        jsonObject = new JSONObject(responseStr);
                        edtDescripcion.setText(jsonObject.optString("descripcion"));
                        edtDescripcionAmpliada.setText(jsonObject.optString("descripcion_AMPLIADA"));
                        edtPrecio.setText(jsonObject.optDouble("precio_UNITARIO") + "");
                        edtFamilias.setText(getTextoFamilia(jsonObject.optInt("familia")));
                        imgProducto.setImageBitmap(utils.rutaAbitmap(jsonObject.optString("ruta_IMAGEN"),getActivity()));
                        setValidaStock(jsonObject.optString("valida_STOCK"));
                        edtStock.setText(jsonObject.optInt("stock")+"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    Utilitarios.validarCampos("Error!!","Error al mostrar datos dela articulo.",getActivity());
                prgDialogProducto.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogProducto.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor: ",error.getMessage(),getActivity());
            }
        });
    }

    private void actualizarArticuloWS(RequestParams params){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "actualizarArticulo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.dismiss();
                if(statusCode==201){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("ACTUALIZACIÓN EXITOSA!!");
                    builder.setMessage("Articulo actualizado correctamente.").setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.fragment_place, new ListaProductos()).commit();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }else
                    Utilitarios.validarCampos("Error!!","Error al actualizar articulo.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.dismiss();
                Utilitarios.validarCampos("Ocurrió un error en el servidor...: ",error.getMessage(),getActivity());
            }
        });
    }

    public String validaStock() {
        if (rbtSi.isChecked()) {
            return "SI";
        } else
            return "NO";
    }

    public void grabarArticulo(){
        RequestParams params = new RequestParams();
        params.put("id",utils.id_articulo);
        params.put("descripcion", edtDescripcion.getText().toString());
        params.put("descAmpliada", edtDescripcionAmpliada.getText().toString());
        params.put("idFamilia", idFamilia);
        params.put("precioTot", Double.parseDouble(edtPrecio.getText().toString()));
        params.put("valStock", validaStock());
        params.put("stock",Integer.parseInt(edtStock.getText().toString()));
        params.put("imagen",rutaImagen);
        params.put("usuRegistro",utils.nombre);
        actualizarArticuloWS(params);
    }


}
