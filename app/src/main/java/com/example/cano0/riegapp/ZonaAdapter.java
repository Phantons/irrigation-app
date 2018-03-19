package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import es.upm.etsit.irrigation.shared.Zone;

/**
 * Created by cano0 on 17/03/2018.
 */

public class ZonaAdapter extends ArrayAdapter<Zone> {

    private final List<Zone> list;
    private final Context context;
    private final int nControlador;
    private final int nMode;


    public ZonaAdapter(Context context, List<Zone> list, int nControlador, int nMode) {
        super(context, R.layout.content_zones, list);
        this.context = context;
        this.list = list;
        this.nControlador = nControlador;
        this.nMode = nMode;
    }

    static class ViewHolderZona {
        protected Button zonaN;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ZonaAdapter.ViewHolderZona viewHolder;
        if (view == null) {
            LayoutInflater inflator =LayoutInflater.from(context);// Activity.getLayoutInflater();
            view =  inflator.inflate(R.layout.content_zones, null);// java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            viewHolder = new ZonaAdapter.ViewHolderZona();
            viewHolder.zonaN = (Button) view.findViewById(R.id.zonaN);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ZonaAdapter.ViewHolderZona) view.getTag();
        }
        viewHolder.zonaN.setText(list.get(position).getName());
        //rutina de atencion al boton
        viewHolder.zonaN.setTag(R.integer.zonaN_view,   convertView);
        viewHolder.zonaN.setTag(R.integer.zonaN_pos, position);
        viewHolder.zonaN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //entra en el controlador seleccionado
                View tempview = (View) viewHolder.zonaN.getTag(R.integer.zonaN_view);
                Integer pos = (Integer) viewHolder.zonaN.getTag(R.integer.zonaN_pos);
                enterZoneSelected(pos, context);
            }
        });

        return view;

    }

    public void enterZoneSelected(int pos, Context context) {
        Intent i = new Intent(context, ZoneActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", nMode);
        i.putExtra("nZone", pos);
        context.startActivity(i);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Zone getItem(int position) {
        return super.getItem(position);
    }

}
