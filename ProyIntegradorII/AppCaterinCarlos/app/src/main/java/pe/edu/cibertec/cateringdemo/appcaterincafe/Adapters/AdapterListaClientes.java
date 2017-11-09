package pe.edu.cibertec.cateringdemo.appcaterincafe.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pe.edu.cibertec.cateringdemo.Beans.ListaClientesBE;
import pe.edu.cibertec.cateringdemo.R;

/**
 * Created by Carlos on 1/11/2017.
 */

public class AdapterListaClientes extends ArrayAdapter<ListaClientesBE>{

    public AdapterListaClientes(Context context, ArrayList<ListaClientesBE> listaClientes) {
        super(context, 0, listaClientes);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public ListaClientesBE getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ListaClientesBE lstClientes = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_adapter_mis_clientes, parent, false);
        }
        TextView cliente = (TextView)convertView.findViewById(R.id.tv_cliente);
        TextView codigoCliente = (TextView)convertView.findViewById(R.id.tv_codCliente);
        TextView correo = (TextView)convertView.findViewById(R.id.tv_correo);
        TextView tipoCliente = (TextView)convertView.findViewById(R.id.tv_tipoCliente);
        TextView autoriza = (TextView)convertView.findViewById(R.id.tv_autoriza);
        TextView estado = (TextView)convertView.findViewById(R.id.tv_estado);
        TextView fechaCreacion = (TextView)convertView.findViewById(R.id.tv_fechaCreacion);

        cliente.setText(lstClientes.getNombre()+" "+lstClientes.getApelldio());
        codigoCliente.setText(lstClientes.getId_cliente());
        correo.setText(lstClientes.getCorreo());
        tipoCliente.setText(lstClientes.getTipoCliente());
        autoriza.setText(lstClientes.getIndicaAutoriza());
        estado.setText(lstClientes.getEstado());
        fechaCreacion.setText(lstClientes.getFechaRegistro());


        return convertView;
    }
}
