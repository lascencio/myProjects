package pe.edu.cibertec.cateringdemo.appcaterincafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import pe.edu.cibertec.cateringdemo.Conexion.ConexionWs;
import pe.edu.cibertec.cateringdemo.R;
import pe.edu.cibertec.cateringdemo.appcaterincafe.fragments.fr_lista_clientes;
import pe.edu.cibertec.cateringdemo.appcaterincafe.fragments.fr_perfil;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static  NavigationView  navigationView ;
    ConexionWs ws = new ConexionWs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        try {
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.navigation_paciente);
            TextView nav_correo = (TextView)hView.findViewById(R.id.navigation_correo);
            nav_user.setText(ws.nombre_usr+" "+ws.apellido_usr);
            nav_correo.setText(ws.correo_usr);
        }catch (Exception e){
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);

        if(ws.perfil_usr ==1){
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(2).setVisible(true);
            navigationView.getMenu().getItem(3).setVisible(true);
            navigationView.getMenu().getItem(4).setVisible(true);
            navigationView.getMenu().getItem(4).setChecked(true);
            navigationView.getMenu().getItem(5).setVisible(false);
            navigationView.getMenu().getItem(6).setVisible(false);
            navigationView.getMenu().getItem(7).setVisible(false);
            navigationView.getMenu().getItem(8).setVisible(true);
            navigationView.getMenu().getItem(9).setVisible(true);
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new fr_perfil()).commit();
        }else if(ws.perfil_usr==100){
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor, new fr_lista_clientes()).commit();
            navigationView.getMenu().getItem(0).setVisible(true);
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(true);
            navigationView.getMenu().getItem(4).setVisible(true);
            navigationView.getMenu().getItem(5).setVisible(true);
            navigationView.getMenu().getItem(6).setVisible(true);
            navigationView.getMenu().getItem(7).setVisible(true);
            navigationView.getMenu().getItem(8).setVisible(true);
            navigationView.getMenu().getItem(9).setVisible(true);
        }else{
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
            navigationView.getMenu().getItem(6).setVisible(false);
            navigationView.getMenu().getItem(7).setVisible(false);
            navigationView.getMenu().getItem(8).setVisible(false);
            navigationView.getMenu().getItem(9).setVisible(false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            confirmacionSalir();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager=getSupportFragmentManager();

        if (id == R.id.misclientes) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new fr_lista_clientes()).commit();
        }else if (id == R.id.comprar) {

        }else if (id == R.id.miscompas) {

        } else if (id == R.id.misdeudas) {

        } else if (id == R.id.miperfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new fr_perfil()).commit();
        }else if (id == R.id.rproducto) {

        }else if (id == R.id.rusuario) {

        }else if (id == R.id.ppendientes) {

        }else if (id == R.id.rpedidos) {

        }else if (id == R.id.logout){
            finish();
            Intent intent = new Intent(MenuPrincipal.this, Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*------------------------------*/
    void confirmacionSalir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        builder.setMessage("¿Desea cerrar la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        //onBackPressed();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alerta");
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }
}
