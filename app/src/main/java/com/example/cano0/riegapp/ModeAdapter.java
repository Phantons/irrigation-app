package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;

/**
 * Created by cano0 on 17/03/2018.
 */

public class ModeAdapter extends ArrayAdapter<Mode> {

private final List<Mode> list;
private final Context context;
private final int nControlador;


public ModeAdapter(Context context, List<Mode> list, int nControlador) {
        super(context, R.layout.content_modes, list);
        this.context = context;
        this.list = list;
        this.nControlador = nControlador;
        }

static class ViewHolderMode {
    protected Button modeN;
    protected CheckBox activeN;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ModeAdapter.ViewHolderMode viewHolder;
        if (view == null) {
            LayoutInflater inflator =LayoutInflater.from(context);// Activity.getLayoutInflater();
            view =  inflator.inflate(R.layout.content_modes, null);// java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            viewHolder = new ModeAdapter.ViewHolderMode();
            viewHolder.modeN = (Button) view.findViewById(R.id.modoN);
            viewHolder.activeN = (CheckBox) view.findViewById(R.id.active_mode_checkBox);
            viewHolder.activeN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override                    //checkbox ,                activado:true-false
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Mode mode = (Mode) viewHolder.activeN.getTag();
                    mode.setActive(buttonView.isChecked());
                    UsuarioClass usuarioClass = new UsuarioClass();
                        usuarioClass.getControlador(nControlador).setActive_mode(mode);
                }
            });
            view.setTag(viewHolder);
            viewHolder.activeN.setTag(list.get(position));
        } else {
            viewHolder = (ModeAdapter.ViewHolderMode) view.getTag();
            ((ViewHolderMode) view.getTag()).activeN.setTag(list.get(position));
        }
        viewHolder.modeN.setText(list.get(position).getName());
        //rutina de atencion al boton
        viewHolder.modeN.setTag(R.integer.modeN_view,   convertView);
        viewHolder.modeN.setTag(R.integer.modeN_pos, position);
        //viewHolder.activeN.setChecked(list.get(position).getName() == UsuarioClass.getControladorList().get(nControlador).getActiveMode().getName(););
        viewHolder.modeN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //entra en el controlador seleccionado
                View tempview = (View) viewHolder.modeN.getTag(R.integer.modeN_view);
                Integer pos = (Integer) viewHolder.modeN.getTag(R.integer.modeN_pos);

                enterModeSelected(pos);
            }
        });
        return view;

    }


    /**
     * Método que edita o crea nuevos Modos
     * @param pos posicion del modo. Si es nuevo envío la posicion que sería
     */
    public void enterModeSelected(int pos) {
        Intent i = new Intent(context, ModeActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", pos);
        context.startActivity(i);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Mode getItem(int position) {
        return super.getItem(position);
    }

}
