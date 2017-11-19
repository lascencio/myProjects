package com.example.david.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.adapters.MenuListAdapter;
import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.ArticuloBean;
import com.example.david.beans.DetalleVentasBean;
import com.example.david.dao.CarritoDAO;
import com.example.david.utils.Utilitarios;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListaProductos extends Fragment {
    GridView gridView;
    ImageView imgArticulo;
    EditText edtDesc, edtPre, edtCant, edtTot;
    Button btnAgregar;
    TextView txtArticulo;
    ArrayList<ArticuloBean> listaArticulos = new ArrayList<>();
    MenuListAdapter adapter = null;
    Utilitarios utils = new Utilitarios();

    ProgressDialog prgDialogProductos;
    ProgressDialog prgDialogEliminar;

    String ruta_imagen = "";

    private void iniciarComponentes(View view){
        gridView = view.findViewById(R.id.gridView);
        prgDialogProductos = new ProgressDialog(getActivity());
        prgDialogProductos.setMessage("Listando Producto(s)...");
        prgDialogProductos.setCancelable(false);

        prgDialogEliminar = new ProgressDialog(getActivity());
        prgDialogEliminar.setMessage("Eliminando Producto...");
        prgDialogEliminar.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_productos, container, false);
        ((NavigationActivity) getActivity()).setActionBarTitle("COMPRA AHORA");
        iniciarComponentes(view);
        listarArticulosWS();
        return view;
    }

    public void elegirOpciones(){
        adapter = new MenuListAdapter(getActivity(), R.layout.item_productos, listaArticulos);
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                utils.id_articulo = "";
                CharSequence[] items = {"Actualizar Producto", "Eliminar Producto"};
                CharSequence[] items2 = {"Agregar Producto"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final String id_art = ((TextView)view.findViewById(R.id.idArticulo)).getText().toString();
                utils.id_articulo = id_art;
                dialog.setTitle("Elige una opción");
                if(utils.acceso_cargo >=100){
                    dialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int item) {
                            if (item == 0) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.fragment_place, new ActualizarProducto()).commit();

                            } else
                                eliminarArticuloWS(id_art);
                        }
                    });
                }else if(utils.acceso_cargo<100){
                    dialog.setItems(items2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int item) {
                            if (item == 0) {
                                agregarPedido(getActivity(),id_art);
                            }
                        }
                    });
                }
                dialog.show();
                return true;
            }
        });
    }

    public void listarArticulosWS() {
        prgDialogProductos.show();
        listaArticulos.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaArticulos", new AsyncHttpResponseHandler() {
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
                            ArticuloBean bean = new ArticuloBean();
                            bean.setID_ARTICULO(jsonObject.optString("id_ARTICULO"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            bean.setDESCRIPCION_AMPLIADA(jsonObject.optString("descripcion_AMPLIADA"));
                            bean.setPRECIO_UNITARIO(jsonObject.optDouble("precio_UNITARIO"));
                            bean.setVALIDA_STOCK(jsonObject.optString("valida_STOCK"));
                            bean.setSTOCK(jsonObject.optInt("stock"));
                            bean.setID_FAMILIA(jsonObject.optInt("familia"));
                            bean.setID_ESTADO(jsonObject.optInt("id_ESTADO"));
                            bean.setRUTA_IMAGEN(jsonObject.optString("ruta_IMAGEN"));
                            listaArticulos.add(bean);
                        }
                        elegirOpciones();
                        prgDialogProductos.dismiss();
                    } else {
                        prgDialogProductos.dismiss();
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
                prgDialogProductos.dismiss();
                Utilitarios.validarCampos("Ocurrió un Error!!",error.getMessage(),getActivity());
            }
        });
    }

    private void agregarPedido(Activity activity, String id_art) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_agregar_pedido);
        dialog.setTitle("AGREGAR PEDIDO");

        iniciarComponentesCarrito(dialog);
        consultarArticuloWS(id_art);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos(dialog.getContext())){
                    llenarCarrito(dialog);
                }
            }
        });
        edtCant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    calcularTotal();
                    if (!event.isShiftPressed()) {
                        calcularTotal();
                    }
                }
                return false;
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.5);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    private void iniciarComponentesCarrito(Dialog dialog){
        edtDesc = dialog.findViewById(R.id.edtDesc);
        edtPre = dialog.findViewById(R.id.edtPreUnit);
        edtCant = dialog.findViewById(R.id.edtCantidad);
        edtTot = dialog.findViewById(R.id.edtTotal);
        btnAgregar = dialog.findViewById(R.id.btnAgregar);
        txtArticulo = dialog.findViewById(R.id.txtArticulo);
        imgArticulo = dialog.findViewById(R.id.imgArticulo);
    }

    private void consultarArticuloWS(String id_art){
        RequestParams params = new RequestParams();
        params.put("id", id_art);
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
                        edtDesc.setText(jsonObject.optString("descripcion"));
                        edtPre.setText(jsonObject.optDouble("precio_UNITARIO") + "");
                        txtArticulo.setText(articulo(jsonObject.optInt("familia")));
                        imgArticulo.setImageBitmap(utils.rutaAbitmap(jsonObject.optString("ruta_IMAGEN"),getActivity()));
                        ruta_imagen = jsonObject.optString("ruta_IMAGEN");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    Utilitarios.validarCampos("Error: ","Error al mostrar datos de cliente.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utilitarios.validarCampos("Ocurrió un problema con el servidor: ",error.getMessage(),getActivity());
            }
        });
    }

    public String articulo(int id){
        if(id==1){
            return "Producto";
        }else
            return "Suministro";
    }

    public void llenarCarrito(final Dialog dialog){
        CarritoDAO dao = new CarritoDAO(dialog.getContext());
        DetalleVentasBean bean = new DetalleVentasBean();
        bean.setID_VENTAS(utils.id_venta);
        bean.setID_ARTICULO(utils.id_articulo);
        bean.setPRECIO_UNITARIO(Double.parseDouble(edtPre.getText().toString()));
        bean.setCANTIDAD(Integer.parseInt(edtCant.getText().toString()));
        bean.setPRECIO_TOTAL(Double.parseDouble(edtTot.getText().toString()));
        bean.setRUTA_IMAGEN(ruta_imagen);
        bean.setDESCRIPCION(edtDesc.getText().toString());
        int res = dao.agregarDetalle(bean);
        if (res>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("PRODUCTO AGREGADO!!");
            builder.setMessage("Verefica tu lista de pedidos.").setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        }else
            utils.validarCampos("Error!!!", "Producto no agregado...",getActivity());
    }

    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        if (edtCant.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingresa cantidadTot", ctx);
            valid = false;
        }
        return valid;
    }

    public void calcularTotal(){
        int cant;
        if(edtCant.getText().toString().equals("")){
            cant = 0;
        }else
            cant = Integer.parseInt(edtCant.getText().toString());
        double pre = Double.parseDouble(edtPre.getText().toString());
        String resultado = (cant*pre)+"";
        edtTot.setText(resultado);
    }

    private void eliminarArticuloWS(String idArticulo){
        RequestParams params = new RequestParams();
        params.put("id", idArticulo);
        prgDialogEliminar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "eliminarArticulo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialogEliminar.dismiss();
                if(statusCode==201){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("REGISTRO EXITOSO!");
                    builder.setMessage("Articulo registrado correctamente.").setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
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
                    Utilitarios.validarCampos("Error!!","Error al registrar articulo.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogEliminar.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor",error.getMessage(),getActivity());
            }
        });
    }

}
