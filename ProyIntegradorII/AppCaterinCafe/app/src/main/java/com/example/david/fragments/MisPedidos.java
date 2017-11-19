package com.example.david.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.adapters.PedidosListAdapter;
import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.DetalleVentasBean;
import com.example.david.beans.TablaBean;
import com.example.david.beans.VentasBean;
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
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MisPedidos extends Fragment {
    ImageButton imgBtnConfirmar;
    ImageView imgArticulo;
    TextView edtFormaPagos;
    EditText edtDesc, edtPre, edtCant, edtTot, edtTotPag, edtCantTot, edtUsuCons, edtFechComp;
    Button btnAgregar, btnConfirmar;
    ListView lstPedidos;
    PedidosListAdapter adapter;
    ArrayList<DetalleVentasBean> listaDetalles = new ArrayList<>();
    ArrayList<TablaBean> listaFormaPagos = new ArrayList<>();
    Utilitarios utils = new Utilitarios();

    int cantidadTot = 0;
    double precioTot = 0.0;
    int idFormaPago = 0;

    ProgressDialog prgDialogTipos;
    ProgressDialog prgDialogRegVentas;


    private void iniciarComponentes(View view) {
        lstPedidos = view.findViewById(R.id.lstPedidos);
        imgBtnConfirmar = view.findViewById(R.id.imgBtnConfirmar);

        prgDialogTipos = new ProgressDialog(getActivity());
        prgDialogTipos.setMessage("Listando Tipos de Pago...");
        prgDialogTipos.setCancelable(false);
    }

    private void iniciarComponentesCarrito(Dialog dialog) {
        edtDesc = dialog.findViewById(R.id.edtDesc);
        edtPre = dialog.findViewById(R.id.edtPreUnit);
        edtCant = dialog.findViewById(R.id.edtCantidad);
        edtTot = dialog.findViewById(R.id.edtTotal);
        btnAgregar = dialog.findViewById(R.id.btnAgregar);
        imgArticulo = dialog.findViewById(R.id.imgArticulo);
        btnAgregar.setText("ACTUALIZAR PEDIDO");
    }

    private void iniciarComponentesCompra(Dialog dialog) {
        btnConfirmar = dialog.findViewById(R.id.btnConfirmar);
        edtCantTot = dialog.findViewById(R.id.edtCantTot);
        edtTotPag = dialog.findViewById(R.id.edtTotPag);
        edtFormaPagos = dialog.findViewById(R.id.edtFormaPagos);
        edtUsuCons = dialog.findViewById(R.id.edtUsuCons);
        edtFechComp = dialog.findViewById(R.id.edtFechComp);
        btnConfirmar = dialog.findViewById(R.id.btnConfirmar);

        prgDialogRegVentas = new ProgressDialog(getActivity());
        prgDialogRegVentas.setMessage("Listando Tipos de Pago...");
        prgDialogRegVentas.setCancelable(false);

        edtFechComp.setText(utils.fechaActual());
        edtUsuCons.setText(utils.nombre + " " + utils.apellido);

        for(DetalleVentasBean b : listaDetalles){
            cantidadTot = cantidadTot + b.getCANTIDAD();
        }
        for(DetalleVentasBean b : listaDetalles){
            precioTot = precioTot + b.getPRECIO_TOTAL();
        }
        edtCantTot.setText(cantidadTot +"");
        edtTotPag.setText(precioTot +"");
    }

    public void setAdapter() {
        listaDetalles.clear();
        CarritoDAO dao = new CarritoDAO(getActivity());
        listaDetalles = dao.miCarrito(utils.id_venta);
        adapter = new PedidosListAdapter(getActivity(), R.layout.listview_pedidos, listaDetalles);
        lstPedidos.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_pedidos, container, false);
        iniciarComponentes(view);
        setAdapter();
        elegirOpciones();
        ((NavigationActivity) getActivity()).setActionBarTitle("MIS PEDIDOS");
        imgBtnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarVenta(getActivity());
            }
        });
        return view;
    }

    public void elegirOpciones() {

        lstPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence[] items = {"Actualizar Detalle", "Eliminar Detalle"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final String idVenta = ((TextView) view.findViewById(R.id.idVent)).getText().toString();
                final String idArticulo = ((TextView) view.findViewById(R.id.idArt)).getText().toString();
                dialog.setTitle("Elige una opción");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            eliminarDetalle(idVenta, idArticulo);
                            setAdapter();
                        }
                        if (i == 0) {
                            actualizarDetalle(getActivity(), idVenta,idArticulo);
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    public void eliminarDetalle(String idVenta, String idArticulo) {
        CarritoDAO dao = new CarritoDAO(getActivity());
        int resultado = dao.eliminarDetalle(idVenta, idArticulo);
        if (resultado == 0) {
            utils.validarCampos("Error!", "Detalle no eliminado.", getActivity());
        }
    }

    public void actualizar(String idVenta, String idArticulo) {
        CarritoDAO dao = new CarritoDAO(getActivity());
        int resultado = dao.actualizarDetalle(idVenta, idArticulo, Integer.parseInt(edtCant.getText().toString()), Double.parseDouble(edtTot.getText().toString()));
        if (resultado == 0) {
            utils.validarCampos("Error!", "Detalle no actualizado.", getActivity());
        }
    }

    public void calcularTotal() {
        int cant;
        if (edtCant.getText().toString().equals("")) {
            cant = 0;
        } else
            cant = Integer.parseInt(edtCant.getText().toString());
        double pre = Double.parseDouble(edtPre.getText().toString());
        String resultado = (cant * pre) + "";
        edtTot.setText(resultado);
    }

    private void consultarDetalle(String idVenta, String idArticulo, Dialog dialog) {
        CarritoDAO dao = new CarritoDAO(dialog.getContext());
        DetalleVentasBean b = dao.consultarDetalle(idVenta, idArticulo);
        edtDesc.setText(b.getDESCRIPCION());
        edtPre.setText(b.getPRECIO_UNITARIO() + "");
        imgArticulo.setImageBitmap(utils.rutaAbitmap(b.getRUTA_IMAGEN(), getActivity()));
        edtCant.setText(b.getCANTIDAD() + "");
        edtTot.setText(b.getPRECIO_TOTAL() + "");
    }

    private void actualizarDetalle(Activity activity, final String idVenta, final String idArticulo) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_agregar_pedido);
        dialog.setTitle("ACTUALIZAR PEDIDO");

        iniciarComponentesCarrito(dialog);
        consultarDetalle(idVenta, idArticulo, dialog);

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

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar(idVenta,idArticulo);
                dialog.dismiss();
                setAdapter();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.5);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    private void confirmarVenta(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_confirmar_pedidos);
        dialog.setTitle("CONFIRMAR COMPRA");

        iniciarComponentesCompra(dialog);

        edtFormaPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callWsFormaPagos();
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabarVenta();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1.5);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }


    public void callWsFormaPagos(){
        prgDialogTipos.show();
        listaFormaPagos.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(utils.URL_SERVICE + "/listaFormaPAg", new AsyncHttpResponseHandler() {
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
                            listaFormaPagos.add(bean);
                        }
                        prgDialogTipos.dismiss();
                        dialogFormas();
                    }else{
                        prgDialogTipos.dismiss();
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
                prgDialogTipos.dismiss();
                Utilitarios.validarCampos("Ocurrió un Error!!",error.getMessage(),getActivity());
            }
        });
    }

    public void dialogFormas(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(" Seleccione Tipo de Pago ");
        final List<String> lstDescripciones = new ArrayList<>();
        final List<Integer> lstCodigos = new ArrayList<>();
        for(int i = 0; i< listaFormaPagos.size(); i++){
            lstDescripciones.add(listaFormaPagos.get(i).getDESCRIPCION());
            lstCodigos.add(listaFormaPagos.get(i).getID());
        }
        final CharSequence[] allCargos = lstDescripciones.toArray(new String[lstDescripciones.size()]);
        dialog.setItems(allCargos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String valueSelect = allCargos[i].toString();
                edtFormaPagos.setText(valueSelect);
                edtFormaPagos.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down,0);

                for(TablaBean b : listaFormaPagos){
                    if(b.getDESCRIPCION().equals(valueSelect)){
                        idFormaPago = b.getID();
                    }
                }
                Log.w(utils.APP_NAME, "Forma de Pago: " + idFormaPago);
            }
        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(R.color.white);
    }

    private void registrarDetallesWS(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "registrarDetalle", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==201){
                    Log.w(utils.APP_NAME, "DETALLE REGISTADO");
                }else
                    Utilitarios.validarCampos("Error!!","Error al registrar articulo.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utilitarios.validarCampos("Ocurrió un problema con el servidor",error.getMessage(),getActivity());
            }
        });
    }

    private void registrarVentaWS(RequestParams params){
        prgDialogRegVentas.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "registrarVenta", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialogRegVentas.dismiss();
                if(statusCode==201){
                    grabarDetalle();
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
                    idVenta();
                    Log.w(utils.APP_NAME, "Nuevo Id ventas: " + utils.id_venta);
                }else
                    Utilitarios.validarCampos("Error!!","Error al registrar articulo.",getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogRegVentas.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor",error.getMessage(),getActivity());
            }
        });
    }

    public void grabarVenta(){
        RequestParams params = new RequestParams();
        params.put("idVenta", utils.id_venta);
        params.put("idCliente", utils.id_usuario);
        params.put("total", Double.parseDouble(edtTotPag.getText().toString()));
        int tipoVenta = 0;
        if(idFormaPago==1){
            tipoVenta = 1;
        }else if(idFormaPago==2){
            tipoVenta = 2;
        }else
            tipoVenta = 3;
        params.put("idTipo",tipoVenta);
        params.put("idPago",idFormaPago);
        params.put("usuRegistro",utils.correo);
        registrarVentaWS(params);
    }

    public void grabarDetalle(){
        RequestParams params = new RequestParams();
        CarritoDAO dao = new CarritoDAO(getActivity());
        for(DetalleVentasBean b : dao.miCarrito(utils.id_venta)){
            params.put("idVenta",utils.id_venta);
            params.put("idArticulo",b.getID_ARTICULO());
            params.put("cantidad",b.getCANTIDAD());
            params.put("preUnitario",b.getPRECIO_UNITARIO());
            params.put("preTotal",b.getPRECIO_TOTAL());
            params.put("usuRegistro",utils.correo);

            registrarDetallesWS(params);
        }
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
                Toast.makeText(getActivity(), "Ocurrió un error... :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
