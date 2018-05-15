package com.example.cano0.riegapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import es.upm.etsit.irrigation.util.Time;

/**
 * Created by cano0 on 18/03/2018.
 */

public class TimeAdapter extends ArrayAdapter<Time> {

    private final List<Time> list;
    private final Context context;
    private final int nControlador;
    private final int nModo;
    private final int nZona;

    public TimeAdapter(Context context, List<Time> list, int nControlador, int nModo, int nZona) {
        super(context, R.layout.hours_day_view, list);
        this.context = context;
        this.list = list;
        this.nControlador = nControlador;
        this.nModo = nModo;
        this.nZona = nZona;
    }

    static class ViewHolderTime {
        protected TextView initialText;
        protected TextView finalText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflator =LayoutInflater.from(context);// Activity.getLayoutInflater();
            view =  inflator.inflate(R.layout.hours_day_view, null);// java.lang.UnsupportedOperationException: addView(View, LayoutParams) is not supported in AdapterView
            final ViewHolderTime viewHolder = new TimeAdapter.ViewHolderTime();
            viewHolder.initialText = (TextView) view.findViewById(R.id.inicioText);
            viewHolder.finalText = (TextView) view.findViewById(R.id.finalText);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        //recojo las horas modificadas y las añado
        ViewHolderTime viewHolderTime = (ViewHolderTime) view.getTag();
        //recojo las horas modificadas y las añado
        getTime(position, viewHolderTime);
        return view;
    }


    /**
     * método que añade nuevo intervalo
     */
    private void getTime(int position, ViewHolderTime viewHolderTime) {

        //recojo las horas modificadas y las añado
        int initialhour = list.get(position).getStart().getHour();
        int initialMinute = list.get(position).getStart().getMinute();
        int finalHour = initialhour + (int) ((list.get(position).getTimeout() / 60 + initialMinute)/ 60);
        int finalMinute =  (((int)(list.get(position).getTimeout() / 60 )+ initialMinute) % 60);
        String[] tiempoString = new String[4];
        tiempoString = passTimeToString(initialMinute, initialhour, finalMinute, finalHour);

        String initialTextString = tiempoString[1] + ":" + tiempoString[0];
        String finalTextString = tiempoString[3] + ":" + tiempoString[2];
        viewHolderTime.initialText.setText(initialTextString);
        viewHolderTime.finalText.setText(finalTextString);
    }

    private String[] passTimeToString(int imin, int ihour, int fmin, int fhour) {
        String[] result = new String[4];
        int[] num = {imin, ihour, fmin, fhour};
        for(int i = 0; i < num.length; i++) {
            if(num[i] <10) {
                result[i] = "0".concat(Integer.toString(num[i]));
            } else result[i] = Integer.toString(num[i]);
        }
        return result;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Time getItem(int position) {
        return super.getItem(position);
    }

}
