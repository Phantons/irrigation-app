package com.example.cano0.riegapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Zone;


public class RegarModeTask extends AsyncTask<Void, Void, Integer[]> {

    private Zone zoneParameter;
    private int minutos;
    private Button regarButtonTask;
    private boolean regarTask;
    private Controlador controladorTask;
    private Context contextTask;

    public  RegarModeTask(Zone zoneParameter, int minutos, Button regarButtonTask, boolean regar, Controlador controllerTask, Context contextTask) {
        this.zoneParameter = zoneParameter;
        this.minutos = regar? minutos : -1;
        this.regarButtonTask = regarButtonTask;
        this.regarTask = regar;
        this.controladorTask = controllerTask;
        this.contextTask = contextTask;
    }


    @Override
    protected Integer[] doInBackground(Void... params) {
        Integer[] results;
        if(controladorTask.getActiveMode() == null) {
            results = new Integer[0];
        }else if(controladorTask.getActiveMode().getZones() == null) {
            results = new Integer[0];
        } else {
            results = new Integer[controladorTask.getActiveMode().getZones().size()];
        }
        if (results.length == 0) { return results; }
        if(zoneParameter == null) {
            int i = 0;
            for(Zone zone : controladorTask.getActiveMode().getZones()) {
                results[i] = SocketHandler.irrigateNow(zone.getPinAddress(), minutos, controladorTask.getId());
                i++;
            }
        } else {
            results[0] = SocketHandler.irrigateNow(zoneParameter.getPinAddress(), minutos, controladorTask.getId());
        }
        return results;

    }
    @Override
    protected void onPostExecute(final Integer[] success) {
        if(success.length == 0) {
            Toast.makeText(contextTask, "No ha configurado ninguna zona o el puerto introducido no es válido", Toast.LENGTH_SHORT).show();
        } else {
            for(int i = 0; i < success.length; i++) {
                if(success[i] == 1) {
                    if(regarTask) {
                        Toast.makeText(contextTask, "Zona " + controladorTask.getActiveMode().getZones().get(i).getName() + " regando durante 10m", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contextTask, "Zona " + controladorTask.getActiveMode().getZones().get(i).getName() + " ha dejado de regar", Toast.LENGTH_SHORT).show();
                    }
                    regarButtonTask.setText(regarTask? "Regar Ya" : "Dejar de Regar");
                } else {
                    Toast.makeText(contextTask, "Zona " + controladorTask.getActiveMode().getZones().get(i).getName() + " no pudo establecer comunicacion", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCancelled() {

    }
}
