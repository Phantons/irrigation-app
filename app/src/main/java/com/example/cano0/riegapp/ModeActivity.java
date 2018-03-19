package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;
import es.upm.etsit.irrigation.shared.Zone;

public class ModeActivity extends AppCompatActivity {


    private EditText nomMode;
    private FloatingActionButton saveButton;
    private UsuarioClass usuarioClass;
    private Controlador controlador;
    private Mode mode;
    private List<Zone> listZones;
    private int nControlador;
    private int nMode;

    private SaveAsyntask saveAsyntask = null;
    private Context context;
    private ArrayAdapter<Zone> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nomMode = (EditText) findViewById(R.id.mode_name);

        getModeSelected();
        setActivity();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newZone();
            }
        });

        saveButton = findViewById(R.id.saveMode);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMode();
            }
        });


    }

    /**
    * aMetodo que recoge el actual modo
     */
    private void getModeSelected() {
        nControlador = getIntent().getExtras().getInt("nControlador");
        nMode = getIntent().getExtras().getInt("nMode");
        usuarioClass = new UsuarioClass();
        controlador = usuarioClass.getControlador(nControlador);
        if ((nMode < controlador.getList_modo().size()) && (controlador.getList_modo().size() != 0)) {
            mode = controlador.getList_modo().get(nMode);
            listZones = mode.getZones();
        } else {
            mode = new Mode(nMode, "");
            listZones = new ArrayList<>();
            mode.setZones(listZones);
            controlador.addMode(mode);
            usuarioClass.setControladorN(controlador);
        }
    }

    private void setActivity() {
        nomMode.setText(mode.getName());
        addView();
    }

    private void addView() {
        try {
            ListView listaView = (ListView) findViewById(R.id.Zonas_list);
            adapter = new ZonaAdapter(ModeActivity.this, mode.getZones(), nControlador, nMode);
            listaView.setAdapter(adapter);
            listaView.refreshDrawableState();
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda el Modo configurado
     */
    private void saveMode() {
        mode.setName(nomMode.getText().toString());
        controlador.setMode(mode);
        usuarioClass.setControladorN(controlador);
        hideSoftKeyboard();
        /*
        saveAsyntask = new SaveAsyntask(controlador);
        saveAsyntask.execute((Void) null);
        */
        Toast.makeText(this, "Modo salvado", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ModesActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", nMode);
        startActivity(i);
    }



    private void newZone() {
        Intent i = new Intent(context, ZoneActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", nMode);
        i.putExtra("nZone", mode.getZones().size());
        context.startActivity(i);
        finish();
    }

    /**
     * esconde el teclado
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void onBackPressed() {
        if(mode.getName().isEmpty()) {
            usuarioClass.getControlador(nControlador).getList_modo().remove(nMode);
        }
        Intent i = new Intent(this, ModesActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", nMode);
        startActivity(i);
        super.onBackPressed();
        finish();
    }

    public class SaveAsyntask extends AsyncTask<Void, Void, Integer> {

        private Controlador controladorEnviado;

        private  SaveAsyntask(Controlador _controlador) {
            this.controladorEnviado = _controlador;
        }


        @Override
        protected Integer doInBackground(Void... params) {
            Comunicaciones comunicaciones = new Comunicaciones();
            return comunicaciones.sendOrUpdateController(controlador);
        }
        @Override
        protected void onPostExecute(final Integer success) {
            if(success == 1) {
                Toast.makeText(context, "Modo guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Fallo en la conexión al intentar guardar cambios", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
