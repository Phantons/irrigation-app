package com.example.cano0.riegapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private List<Municipio> listaMunicipios = new ArrayList<>();
    private String codeMunicipio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_controlador);

        nomControlador = (EditText) findViewById(R.id.nomControlador);
        idControlador = (EditText) findViewById(R.id.idControlador);
        muniControlador = (AutoCompleteTextView) findViewById(R.id.munControlador);
        addControlador = (Button) findViewById(R.id.addController);

        muniControlador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
               hideSoftKeyboard();
               String selection = (String) parent.getItemAtPosition(position);
               for(Municipio municipio : listaMunicipios) {
                   if(municipio.getNAME() == selection) {
                       codeMunicipio = municipio.getCOD();
                   }
               }
               System.out.println("codigo: " + codeMunicipio);
           }
       });


        addControlador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controladorCreated();
            }
        });

        hideSoftKeyboard();
        getMunicipiosJson();
        municipioListener();

    }

    private void controladorCreated() {
        if(nomControlador.getText().length() == 0 || idControlador.getText().length() == 00|| muniControlador.getText().length() == 0) {
            //revisamos que no hay nada sin rellenar
            Toast.makeText(AddControlador.this, "introduzca todos los parámetros", Toast.LENGTH_SHORT);
        } else {
            hideSoftKeyboard();
            // Intent que envia los datos recopilados
            controlador = new Controlador(nomControlador.getText().toString(), idControlador.getText().toString(), true, codeMunicipio, null, new ArrayList<Mode>());
            addControllerTask = new AddControllerTask(controlador);
            addControllerTask.execute((Void) null);
        }
    }

    private void municipioListener() {
        // Arreglo con las regiones
        String[] regions = new String[listaMunicipios.size()];
        for(int i = 0; i < regions.length; i++) {
            regions[i] = listaMunicipios.get(i).getNAME();
        }
        // Le pasamos las regiones al adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        // finalmente le asignamos el adaptador a nuestro elemento
        muniControlador.setAdapter(adapter);
    }

    private void getMunicipiosJson() {
        try {
            InputStream ins = getResources().openRawResource(R.raw.codmunicipios);
            String todo = readTextFile(ins);
            JSONObject object = new JSONObject(todo);
            JSONArray array = object.getJSONArray("municipios");
            for (int i = 0; i < array.length(); i++) {
                listaMunicipios.add(new Municipio(i, array.getJSONObject(i)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard();
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
        finish();
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return outputStream.toString();
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

            UsuarioClass.addControladorList(controlador);
            int result = SocketHandler.sendOrUpdateController(newControlador);
            System.out.println(result);

            return result;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            addControlador = null;

            if (success == 1) {
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
