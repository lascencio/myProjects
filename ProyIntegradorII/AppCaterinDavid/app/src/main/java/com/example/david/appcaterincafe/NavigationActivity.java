package com.example.david.appcaterincafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.david.conexion.ConexionWs;
import com.example.david.fragments.ActualizarCliente;
import com.example.david.fragments.CompraAhora;
import com.example.david.fragments.MisClientes;
import com.example.david.fragments.RegistrarProducto;
import com.example.david.fragments.RegistrarUsuario;


public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FragmentManager fm = getSupportFragmentManager();
    ConexionWs cnws = new ConexionWs();
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setNavigationViewListener();
        View header = ((NavigationView) findViewById(R.id.navigation)).getHeaderView(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ((TextView) header.findViewById(R.id.correo_usuario)).setText(cnws.correo.toString());
        ((TextView) header.findViewById(R.id.nombre_usuario)).setText(cnws.nombre.toString() + " " + cnws.apellido.toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        activateItems(cnws.acceso_cargo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buy_now:
                fm.beginTransaction().replace(R.id.fragment_place, new CompraAhora()).commit();
                break;
            case R.id.logout:
                startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                break;
            case R.id.user_profile:
                fm.beginTransaction().replace(R.id.fragment_place, new ActualizarCliente()).commit();
                break;
            case R.id.reg_product:
                fm.beginTransaction().replace(R.id.fragment_place, new RegistrarProducto()).commit();
                break;
            case R.id.reg_usu:
                fm.beginTransaction().replace(R.id.fragment_place, new RegistrarUsuario()).commit();
                break;
            case R.id.my_clients:
                fm.beginTransaction().replace(R.id.fragment_place, new MisClientes()).commit();
                break;
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setActionBarTitle(String tittle) {
        getSupportActionBar().setTitle(Html.fromHtml("<b>" + tittle + "</b>"));
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void activateItems(int perfil) {
        if (perfil >= 100) {
//            navigationView.getMenu().getItem(4).setVisible(true);
//            navigationView.getMenu().getItem(5).setVisible(true);
//            navigationView.getMenu().getItem(6).setVisible(true);
//            navigationView.getMenu().getItem(7).setVisible(true);
//            navigationView.getMenu().getItem(8).setVisible(true);
             navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
        }
//            navigationView.getMenu().getItem(1).setVisible(true);
//            navigationView.getMenu().getItem(2).setVisible(true);
//            navigationView.getMenu().getItem(3).setVisible(true);

    }


}