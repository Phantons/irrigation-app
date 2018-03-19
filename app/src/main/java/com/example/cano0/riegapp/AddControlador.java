package com.example.cano0.riegapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;

public class AddControlador extends AppCompatActivity {

    private Button addControlador;
    private EditText nomControlador;
    private EditText idControlador;
    private EditText muniControlador;
    private Controlador controlador;
    private UsuarioClass usuarioClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_controlador);

        nomControlador = (EditText) findViewById(R.id.nomControlador);
        idControlador = (EditText) findViewById(R.id.idControlador);
        muniControlador = (EditText) findViewById(R.id.munControlador);
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
            Intent i = new Intent(AddControlador.this, DatosActivity.class);
            controlador = new Controlador(nomControlador.getText().toString(), idControlador.getText().toString(), true, muniControlador.getText().toString(), null, new ArrayList<Mode>());
            usuarioClass.addControladorList(controlador);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DatosActivity.class);
        startActivity(i);
        super.onBackPressed();
        finish();
    }
}
