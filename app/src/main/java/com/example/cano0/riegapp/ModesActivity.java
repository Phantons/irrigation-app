package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;
import es.upm.etsit.irrigation.shared.Zone;

public class ModesActivity extends AppCompatActivity {

    private FloatingActionButton addModes;

    private UsuarioClass usuarioClass;
    private Mode modo;
    private Controlador controlador;
    private int nControlador;
    private Context context;
    private ArrayAdapter<Mode> adapter;
    private SaveAsyntask saveAsyntask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        context = getApplicationContext();
        getControlador();

        addModes = (FloatingActionButton) findViewById(R.id.fab);
        addModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModes();
            }
        });

        addView();

    }

    /**
     * Inicializacion del controlador en el que estamos
     */
    private void getControlador() {
        usuarioClass = new UsuarioClass();
        nControlador = getIntent().getExtras().getInt("nControlador");
        controlador = usuarioClass.getControlador(nControlador);
        //Toast.makeText(this, "controlador: " + controlador.getTitulo(), Toast.LENGTH_SHORT).show();

    }

    private void addView() {
        try {
            ListView listaView = (ListView) findViewById(R.id.modes_list);
            adapter = new ModeAdapter(ModesActivity.this, controlador.getList_modo(), nControlador);
            listaView.setAdapter(adapter);
            listaView.refreshDrawableState();
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void createModes() {
        usuarioClass.setControladorN(controlador);
        Intent i = new Intent(context, ModeActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", controlador.getList_modo().size());
        context.startActivity(i);
        finish();
    }


    public void onBackPressed() {
        saveAsyntask = new SaveAsyntask(controlador);
        saveAsyntask.execute((Void) null);
    }

    public class SaveAsyntask extends AsyncTask<Void, Void, Integer> {

        private Controlador controladorEnviado;

        private  SaveAsyntask(Controlador _controlador) {
            this.controladorEnviado = _controlador;
        }


        @Override
        protected Integer doInBackground(Void... params) {
            return SocketHandler.sendOrUpdateController(controlador);
        }
        @Override
        protected void onPostExecute(final Integer success) {
            if(success == 1) {
                UsuarioClass.setControladorN(controlador);
                Toast.makeText(context, "Controlador guardado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Fallo en la conexión al intentar guardar cambios", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(ModesActivity.this, ControllerActivity.class);
            i.putExtra("nControlador", nControlador);
            startActivity(i);
            finish();
        }

        @Override
        protected void onCancelled() {
        }
    }
}
