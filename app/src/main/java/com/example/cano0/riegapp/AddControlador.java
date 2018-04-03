package com.example.cano0.riegapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;

public class AddControlador extends AppCompatActivity {

    private AddControllerTask addControllerTask = null;
    private Button addControlador;
    private EditText nomControlador;
    private EditText idControlador;
    private AutoCompleteTextView muniControlador;
    private Controlador controlador;
    private UsuarioClass usuarioClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_controlador);

        nomControlador = (EditText) findViewById(R.id.nomControlador);
        idControlador = (EditText) findViewById(R.id.idControlador);
        muniControlador = (AutoCompleteTextView) findViewById(R.id.munControlador);
        addControlador = (Button) findViewById(R.id.addController);

        usuarioClass = new UsuarioClass();

        addControlador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controladorCreated();
            }
        });

    }

    private void controladorCreated() {
        if(nomControlador.getText().length() == 0 || idControlador.getText().length() == 00|| muniControlador.getText().length() == 0) {
            //revisamos que no hay nada sin rellenar
            Toast.makeText(AddControlador.this, "introduzca todos los parámetros", Toast.LENGTH_SHORT);
        } else {
            // Intent que envia los datos recopilados
            controlador = new Controlador(nomControlador.getText().toString(), idControlador.getText().toString(), true, muniControlador.getText().toString(), null, new ArrayList<Mode>());
            addControllerTask = new AddControllerTask(controlador);
            addControllerTask.execute((Void) null);
        }
    }

    private void municipioListener() {
        // Arreglo con las regiones
        String[] regions = getResources().getStringArray(R.array.region_array);
        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento
        muniControlador.setAdapter(adapter);
    }
    /*
    private void getMunicipiosJson() {
        InputStream mjson = getResources().openRawResource()
        try {
            JSONObject jsonObj = new JSONObject();
        } catch {

        }
    }
    */

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
        finish();
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AddControllerTask extends AsyncTask<Void, Void, Integer> {

        private final Controlador newControlador;

        AddControllerTask(Controlador newControlador) {
            this.newControlador = newControlador;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int result = SocketHandler.sendOrUpdateController(newControlador);
            System.out.println(result);

            return result;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            addControlador = null;

            if (success == 1) {
                usuarioClass.addControladorList(controlador);
                addControllerTask = new AddControllerTask(controlador);
                Toast.makeText(AddControlador.this, "controlador guardado ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddControlador.this, DatosActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(AddControlador.this, "Conexion fallida, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            addControlador = null;
        }
    }
}
