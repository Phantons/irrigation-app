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

    private SocketHandler socketHandler;
    private Toolbar toolbar;
    private TextView titulo;
    private TextView activeModeText;
    private FloatingActionButton newControlador;
    private Button regarYa;
    private boolean regar;
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
        regar = (regarYa.getText().toString().toLowerCase() == "regar ya");

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
        finish();
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
        mAuthTask = new RegarModeTask(null, 10, regarYa, regar, controller, context);
        mAuthTask.execute((Void) null);
    }

    public void onBackPressed() {
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
        finish();
    }

}


