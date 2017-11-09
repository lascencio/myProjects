package com.example.david.appcaterincafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.beans.UsuarioBean;
import com.example.david.dao.UsuarioDAO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUser, edtPassword;
    TextView txtRegister;
    Button btnLogin;

    private void iniciarComponentes(){
        edtUser = (EditText)findViewById(R.id.edtUser);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        txtRegister = (TextView)findViewById(R.id.txtRegister);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniciarComponentes();
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            String user = edtUser.getText().toString();
            String pass = edtPassword.getText().toString();
            UsuarioDAO dao = new UsuarioDAO(this);
            UsuarioBean bean = dao.loguin(user, pass);
            if(bean != null){
                Intent intent = new Intent(this,NavigationActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
                Toast.makeText(this,"Bienvenido " + bean.getNOMBRE(),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Usuario Incorrecto",Toast.LENGTH_SHORT).show();
            }
        }
        if(view == txtRegister){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
    }
}