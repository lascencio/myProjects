package com.example.david.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.david.appcaterincafe.NavigationActivity;
import com.example.david.appcaterincafe.R;
import com.example.david.beans.FamiliaBean;
import com.example.david.conexion.MiConexion;
import com.example.david.dao.TablesDAO;
import com.example.david.utils.Utilitarios;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RegistrarProducto extends Fragment implements View.OnClickListener {
    EditText edtDescripcion, edtDescripcionAmpliada, edtPrecio, edtStock;
    ImageView imgProducto;
    Spinner spnFamilia;
    Button btnRegistrar;
    RadioGroup rdgValida;
    RadioButton rbtSi, rbtNo;
    ArrayList<FamiliaBean> familias;
    ProgressDialog prgDialog;
    public static MiConexion sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;

    private void cargarSpinners(Context ctx) {
        TablesDAO dao = new TablesDAO(ctx);
        familias = dao.familia();
        ArrayAdapter<FamiliaBean> adapterF = new ArrayAdapter<FamiliaBean>(ctx, android.R.layout.simple_list_item_1, familias);
        spnFamilia.setAdapter(adapterF);
    }

    private void iniciarComponentes(View view) {
        edtDescripcion = view.findViewById(R.id.edtDescripcion);
        edtDescripcionAmpliada = view.findViewById(R.id.edtDescripcionAmpliada);
        imgProducto = view.findViewById(R.id.imgProducto);
        edtPrecio = view.findViewById(R.id.edtPrecio);
        edtStock = view.findViewById(R.id.edtStock);
        spnFamilia = view.findViewById(R.id.spnFamilia);
        btnRegistrar = view.findViewById(R.id.btnRegProducto);
        rdgValida = view.findViewById(R.id.rdgValida);
        rdgValida.check(R.id.rbtSi);
        rbtNo = view.findViewById(R.id.rbtNo);
        rbtSi = view.findViewById(R.id.rbtSi);
        prgDialog = new ProgressDialog(getActivity());
//        if (!rbtSi.isChecked()) {
//            edtStock.setEnabled(false);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_producto, container, false);
        iniciarComponentes(view);
        cargarSpinners(getActivity());
        btnRegistrar.setOnClickListener(this);
        imgProducto.setOnClickListener(this);
        sqLiteHelper = new MiConexion(getActivity(), null, null, 1);
        ((NavigationActivity)getActivity()).setActionBarTitle("REGISTRAR PRODUCTO");
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getActivity(), "No tienes permisos para acceder a galería", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
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
                try {
                    int fam = ((FamiliaBean) spnFamilia.getItemAtPosition(spnFamilia.getSelectedItemPosition())).getID_FAMILIA();
                    sqLiteHelper.insertData(
                            edtDescripcion.getText().toString().trim(),
                            edtDescripcionAmpliada.getText().toString().trim(),
                            fam,
                            1,
                            Double.parseDouble(edtPrecio.getText().toString()),
                            validaStock(),
                            Integer.parseInt(edtStock.getText().toString()),
                            imageViewToByte(imgProducto)
                    );
                    Toast.makeText(getActivity(),"Producto registrado.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if (view == imgProducto) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        }
    }


    String validaStock() {
        String estado = "";
        if (rbtSi.isChecked()) {
            estado = "SI";
        } else if (rbtNo.isChecked()) {
            estado = "NO";
        }
        return estado;
    }


    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public boolean validarCampos(Context ctx) {
        boolean valid = true;
        if (edtDescripcion.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese descripción", ctx);
            valid = false;
        } else if (edtDescripcionAmpliada.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese descripción ampliada", ctx);
            valid = false;
        } else if (edtPrecio.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese precio", ctx);
            valid = false;
        } else if (edtStock.getText().toString().equals("")) {
            Utilitarios.validarCampos("Campos Incorrectos", "Ingrese stock", ctx);
            valid = false;
        }
        return valid;
    }
}