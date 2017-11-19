package com.example.david.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

public class RegistrarProducto extends Fragment implements View.OnClickListener {
    EditText edtDescripcion, edtDescripcionAmpliada, edtPrecio, edtStock;
    TextView edtFamilias;
    ImageView imgProducto;
    Button btnRegistrar;
    RadioGroup rdgValida;
    RadioButton rbtSi, rbtNo;
    ArrayList<TablaBean> listaFamilias = new ArrayList<>();

    ProgressDialog prgDialogRegistrar;
    ProgressDialog prgDialogFamilias;

    Utilitarios utils = new Utilitarios();
    String rutaImagen = "";


    int idFamilia = 0;

    private void iniciarComponentes(View view) {
        edtDescripcion = view.findViewById(R.id.edtDescripcion);
        edtDescripcionAmpliada = view.findViewById(R.id.edtDescripcionAmpliada);
        imgProducto = view.findViewById(R.id.imgProducto);
        edtPrecio = view.findViewById(R.id.edtPrecio);
        edtStock = view.findViewById(R.id.edtStock);
        edtFamilias = view.findViewById(R.id.edtFamilias);
        btnRegistrar = view.findViewById(R.id.btnRegProducto);
        rdgValida = view.findViewById(R.id.rdgValida);
        rdgValida.check(R.id.rbtNo);
        rbtNo = view.findViewById(R.id.rbtNo);
        rbtSi = view.findViewById(R.id.rbtSi);

        prgDialogRegistrar = new ProgressDialog(getActivity());
        prgDialogRegistrar.setMessage("Registrando Producto");
        prgDialogRegistrar.setCancelable(false);

        edtStock.setEnabled(false);

        prgDialogFamilias = new ProgressDialog(getActivity());
        prgDialogFamilias.setMessage("Listando Familias...");
        prgDialogFamilias.setCancelable(false);
    }

    public void callWsFamilias() {
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

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TablaBean bean = new TablaBean();
                            bean.setID(jsonObject.optInt("id"));
                            bean.setDESCRIPCION(jsonObject.optString("descripcion"));
                            listaFamilias.add(bean);
                        }
                        prgDialogFamilias.dismiss();
                        dialogFamilias();
                    } else {
                        prgDialogFamilias.dismiss();
                        Utilitarios.validarCampos("Ocurrió un Error!!", "Array vacío.", getActivity());
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
                Utilitarios.validarCampos("Ocurrió un Error!!", error.getMessage(), getActivity());
            }
        });
    }

    public void dialogFamilias() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(" Seleccione Familia ");
        final List<String> lstDescripciones = new ArrayList<>();
        final List<Integer> lstCodigos = new ArrayList<>();
        for (int i = 0; i < listaFamilias.size(); i++) {
            lstDescripciones.add(listaFamilias.get(i).getDESCRIPCION());
            lstCodigos.add(listaFamilias.get(i).getID());
        }
        final CharSequence[] allCargos = lstDescripciones.toArray(new String[lstDescripciones.size()]);
        dialog.setItems(allCargos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String valueSelect = allCargos[i].toString();
                edtFamilias.setText(valueSelect);
                edtFamilias.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);

                for (TablaBean b : listaFamilias) {
                    if (b.getDESCRIPCION().equals(valueSelect)) {
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
        View view = inflater.inflate(R.layout.fragment_registrar_producto, container, false);
        iniciarComponentes(view);
        btnRegistrar.setOnClickListener(this);
        edtFamilias.setOnClickListener(this);
        imgProducto.setOnClickListener(this);
        rdgValida.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbtNo:
                        edtStock.setEnabled(false);
                        break;
                    case R.id.rbtSi:
                        edtStock.setEnabled(true);
                }
            }
        });
        ((NavigationActivity) getActivity()).setActionBarTitle("REGISTRAR PRODUCTO");
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
        if (view == btnRegistrar) {
            if (validarCampos(getActivity())) {
                grabarArticulo();
            }
        }
        if (view == imgProducto) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, utils.REQUEST_CODE_GALLERY);
        }
        if (view == edtFamilias) {
            callWsFamilias();
        }
    }

    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        final Bitmap map1 = ((BitmapDrawable) imgProducto.getDrawable()).getBitmap();
        Drawable foodIcon = getResources().getDrawable(R.drawable.food_icon);
        final Bitmap map2 = ((BitmapDrawable) foodIcon).getBitmap();
        if (edtDescripcion.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese descripción", ctx);
            valid = false;
        } else if (edtDescripcionAmpliada.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese descripción ampliada", ctx);
            valid = false;
        } else if (edtPrecio.getText().toString().equals("")) {
            Utilitarios.validarCampos("Atención!!", "Ingrese precioTot", ctx);
            valid = false;
        } else if (idFamilia == 0) {
            Utilitarios.validarCampos("Atención!!", "Seleccione un cargo", ctx);
            valid = false;
        } else if (map1.sameAs(map2)) {
            Utilitarios.validarCampos("Atención!!", "Seleccione una imagen", ctx);
            valid = false;
        } else if (edtStock.getText().toString().equals("") && rbtSi.isChecked()) {
            Utilitarios.validarCampos("Atención!!", "Ingrese stock", ctx);
            valid = false;
        }
        return valid;
    }

    private void registrarArticuloWS(RequestParams params) {
        prgDialogRegistrar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(utils.URL_SERVICE + "registroArticulo", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialogRegistrar.dismiss();
                if (statusCode == 201) {
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
                } else
                    Utilitarios.validarCampos("Error!!", "Error al registrar articulo.", getActivity());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialogRegistrar.dismiss();
                Utilitarios.validarCampos("Ocurrió un problema con el servidor", error.getMessage(), getActivity());
            }
        });
    }

    public String validaStock() {
        if (rbtSi.isChecked()) {
            return "SI";
        } else
            return "NO";
    }

    public void grabarArticulo() {
        RequestParams params = new RequestParams();
        params.put("descripcion", edtDescripcion.getText().toString());
        params.put("descAmpliada", edtDescripcionAmpliada.getText().toString());
        params.put("idFamilia", idFamilia);
        params.put("precioTot", Double.parseDouble(edtPrecio.getText().toString()));
        params.put("valStock", validaStock());
        params.put("stock", montoStock());
        params.put("imagen", idFamilia);
        params.put("usuRegistros", utils.nombre);
        registrarArticuloWS(params);

        Log.w(utils.APP_NAME, "Stock : " + montoStock());
        Log.w(utils.APP_NAME, "Valida : " + validaStock());
        Log.w(utils.APP_NAME, "Desc : " + edtDescripcion.getText().toString());
        Log.w(utils.APP_NAME, "ID Familia : " + idFamilia);
        Log.w(utils.APP_NAME, "Desc Ampliada : " + edtDescripcionAmpliada.getText().toString());
        Log.w(utils.APP_NAME, "precioTot : " + Double.parseDouble(edtPrecio.getText().toString()));
        Log.w(utils.APP_NAME, "Ruta Imagen : " + rutaImagen);
        Log.w(utils.APP_NAME, "Usu Registro : " + utils.nombre);

    }

    public int montoStock() {
        if (rbtNo.isChecked()) {
            return 0;
        } else
            return Integer.parseInt(edtStock.getText().toString());

    }
}