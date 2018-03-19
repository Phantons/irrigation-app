package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Zone;

public class ControllerActivity extends AppCompatActivity {

    private Comunicaciones comunicaciones;
    private Toolbar toolbar;
    private TextView titulo;
    private TextView activeModeText;
    private FloatingActionButton newControlador;
    private Button regarYa;
    private RegarModeTask mAuthTask = null;

    private Context context;
    private Controlador controller;
    private int nControlador;
    private UsuarioClass usuarioClass;

    public ControllerActivity(Controlador controller) {
        this.controller = controller;
    }

    public ControllerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        context = getApplicationContext();
        usuarioClass = new UsuarioClass();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titulo = (TextView) findViewById(R.id.tituloController);
        activeModeText = (TextView) findViewById(R.id.active_mode_text);
        regarYa = (Button) findViewById(R.id.regarYa) ;
        regarYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRegarYa();
            }
        });

        getControlador();
        titulo.setText(controller.getTitulo());
        if(controller.getActiveMode() != null) {
            activeModeText.setText(controller.getActiveMode().getName());
        }

        newControlador = (FloatingActionButton) findViewById(R.id.newControlador);
        newControlador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editControlador();
            }
        });
    }


    private void editControlador() {
        Intent i = new Intent(ControllerActivity.this, ModesActivity.class);
        i.putExtra("nControlador", nControlador);
        startActivity(i);

    }
    private void getControlador() {
        nControlador = getIntent().getExtras().getInt("nControlador");
        controller = usuarioClass.getControlador(nControlador);
        titulo.setText(controller.getTitulo());
        if(controller.getActiveMode() != null) {
            activeModeText.setText(controller.getActiveMode().getName());
        } else {
            activeModeText.setText("ningun modo activado");
        }
    }

    private void setRegarYa() {
        //mAuthTask = new RegarModeTask(true, 10);
       // mAuthTask.execute((Void) null);
        Toast.makeText(this, "regar ya a implementar comunicaciones", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
        super.onBackPressed();
        finish();
    }


    public class RegarModeTask extends AsyncTask<Void, Void, Integer[]> {

        private boolean regarAhora;
        private int segundos;

        private  RegarModeTask(boolean regarAhora, int segundos) {
            this.regarAhora = regarAhora;
            this.segundos = segundos;
        }


        @Override
        protected Integer[] doInBackground(Void... params) {
            Integer[] results = new Integer[controller.getActiveMode().getZones().size()];
            if (results.length == 0) { return results; }
            if(regarAhora) {
                int i = 0;
                for(Zone zone : controller.getActiveMode().getZones()) {
                    results[i] = comunicaciones.irrigateNow(zone.getPinAddress(), segundos);
                    i++;
                }
            }
            return results;

        }
        @Override
        protected void onPostExecute(final Integer[] success) {
            if(success.length == 0) {
                Toast.makeText(context, "No ha configurado ninguna zona", Toast.LENGTH_SHORT).show();
            } else {
                for(int i = 0; i < success.length; i++) {
                    if(success[i] == 1) {
                        Toast.makeText(context, "Zona " + controller.getActiveMode().getZones().get(i).getName() + " regando durante 10m", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Zona " + controller.getActiveMode().getZones().get(i).getName() + " no pudo establecer comunicacion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {

        }
    }


}


