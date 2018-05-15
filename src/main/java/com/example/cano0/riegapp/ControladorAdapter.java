package com.example.cano0.riegapp;

/**
 * Created by cano0 on 10/03/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;

public class ControladorAdapter extends ArrayAdapter<Controlador> {

    private final List<Controlador> list;
    private final Context context;


    public ControladorAdapter(Context context, List<Controlador> list) {
        super(context, R.layout.content_data, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected Button controladorN;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflator =LayoutInflater.from(context);// Activity.getLayoutInflater();
            view =  inflator.inflate(R.layout.content_data, null);// java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.controladorN = (Button) view.findViewById(R.id.controladorN);
            view.setTag(viewHolder);
        } else {
        }
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.controladorN.setText(list.get(position).getTitulo());
        //rutina de atencion al boton
        viewHolder.controladorN.setTag(R.integer.controladorN_view,   convertView);
        viewHolder.controladorN.setTag(R.integer.controladorN_pos, position);
        viewHolder.controladorN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //entra en el controlador seleccionado
                View tempview = (View) viewHolder.controladorN.getTag(R.integer.controladorN_view);
                Integer pos = (Integer) viewHolder.controladorN.getTag(R.integer.controladorN_pos);

                enterControladorSelected(pos);
            }
        });
        return view;

    }

    //entra en el controlador seleccionador
    protected void enterControladorSelected(int posControlador) {
        Intent i = new Intent(context, ControllerActivity.class);
        i.putExtra("nControlador", posControlador);
        context.startActivity(i);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Controlador getItem(int position) {
        return super.getItem(position);
    }

}
