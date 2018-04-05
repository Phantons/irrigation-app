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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;

/**
 * Created by cano0 on 17/03/2018.
 */

public class ModeAdapter extends ArrayAdapter<Mode> {

private final List<Mode> list;
private final Context context;
private final int nControlador;
private int checked;




public ModeAdapter(Context context, List<Mode> list, int nControlador) {
        super(context, R.layout.content_modes, list);
        this.context = context;
        this.list = list;
        this.nControlador = nControlador;
        this.checked = getnActiveMode();
        }

static class ViewHolderMode {
    protected Button modeN;
    protected CheckBox activeN;
}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        UsuarioClass usuarioClass = new UsuarioClass();
        if (convertView == null) {
            LayoutInflater inflator =LayoutInflater.from(context);// Activity.getLayoutInflater();
            view =  inflator.inflate(R.layout.content_modes, null);// java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            final ViewHolderMode viewHolder = new ModeAdapter.ViewHolderMode();
            viewHolder.modeN = (Button) view.findViewById(R.id.modoN);
            viewHolder.activeN = (CheckBox) view.findViewById(R.id.active_mode_checkBox);

            viewHolder.activeN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (buttonView.isChecked()) {
                        UsuarioClass.getControladorList().get(nControlador).setActive_mode(list.get(position));
                        checked = ((Integer) buttonView.getTag());
                    }
                    else if(((Integer) buttonView.getTag()) == checked) {
                            UsuarioClass.getControladorList().get(nControlador).setActive_mode(null);
                            checked = -1;
                    }
                    System.out.println(" cambio por pulsar " + Integer.toString(checked));
                    for (int i = 0; i < list.size(); i++) {
                        if (checked == i) {
                            System.out.println("change " + Integer.toString(checked) + " to true");
                            UsuarioClass.getControladorList().get(nControlador).setActive_mode(list.get(i));
                        }
                        else if (checked != i) {
                            System.out.println("change " + Integer.toString(i) + " to false");
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            view.setTag(viewHolder);
            viewHolder.activeN.setTag(position);
        } else {
            view = convertView;
            ((ViewHolderMode)view.getTag()).activeN.setTag(position);
        }

        final ViewHolderMode viewHolder = (ViewHolderMode) view.getTag();
        viewHolder.modeN.setText(list.get(position).getName());
        //rutina de atencion al boton
        viewHolder.modeN.setTag(R.integer.modeN_view,   convertView);
        viewHolder.modeN.setTag(R.integer.modeN_pos, position);
        viewHolder.modeN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //entra en el controlador seleccionado
                View tempview = (View) viewHolder.modeN.getTag(R.integer.modeN_view);
                Integer pos = (Integer) viewHolder.modeN.getTag(R.integer.modeN_pos);
                enterModeSelected(pos);
            }
        });

        //rutina de cambio del checkbox del modo activo
        if(position == checked) {
            viewHolder.activeN.setChecked(true);
        } else {
            viewHolder.activeN.setChecked(false);
        }

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

    public int getnActiveMode() {
        for(Mode mode : UsuarioClass.getControladorList().get(nControlador).getList_modo()) {
            if(UsuarioClass.getControladorList().get(nControlador).getActiveMode().getID() == mode.getID()) {
                return mode.getID();
            }
        }
        return -1;
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
