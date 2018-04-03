package com.example.cano0.riegapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import es.upm.etsit.irrigation.shared.Controlador;


public class DatosActivity extends AppCompatActivity {

    private FloatingActionButton create;

    ArrayAdapter<Controlador> adapter;
    private  ControllerActivity controllerActivity;
    private UsuarioClass usuarioClass;

    // private SharedPreferences outputStream;
    //private int numControladores = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controllerActivity = new ControllerActivity();
        usuarioClass = new UsuarioClass();
        if(getIntent().getExtras() != null) {
            usuarioClass = new UsuarioClass(getIntent().getExtras().getString("usuario"), new ArrayList<Controlador>());
        }
        //Botón añadir Controlador
        create = (FloatingActionButton) findViewById(R.id.fab);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createControlador();
            }
        });

        addView();
    }

    public void createControlador() {
        Intent i = new Intent(DatosActivity.this, AddControlador.class);
        startActivity(i);
        finish();
    }

    //Muestra todos los controladores para mostrar
    private void addView() {
        try {
            ListView listaView = (ListView) findViewById(R.id.Controllers_list);
            adapter = new ControladorAdapter(DatosActivity.this, usuarioClass.getControladorList());
            listaView.setAdapter(adapter);
            listaView.refreshDrawableState();
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    /*
    //recoge lista de controladores guardados en memoria
    public List<Controlador> getListaControllers(){
        try {
            outputStream = PreferenceManager.getDefaultSharedPreferences(this);
            numControladores = outputStream.getInt("controladores", 0);
            for(int i = (numControladores); i > 0; i--){
                Log.d("controlador nº ", Integer.toString(i));
                String tituloS = "titulo" + Integer.toString(i);
                String idS = "id" + Integer.toString(i);
                String tiemparS = "tiempar" + Integer.toString(i);
                String municipioS = "municipio" + Integer.toString(i);
                outputStream.getClass();
                controladorList.add(new Controlador(outputStream.getString(tituloS, ""), outputStream.getString(idS, ""), outputStream.getBoolean(tiemparS, false), outputStream.getString(municipioS, ""), null, null));
            }
        } catch (Exception e) {
            Toast.makeText(DatosActivity.this, "no se ha encontrado ningun archivo", Toast.LENGTH_SHORT).show();
        }
        return controladorList;
    }

    //guarda el nuevo controlador creado
    private void guardarViews() {

        try {
            //outputStream = getSharedPreferences("Views", Context.MODE_PRIVATE);
            outputStream = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor myEditor = outputStream.edit();
            numControladores = controladorList.size();
            myEditor.putString("titulo".concat(Integer.toString(controladorList.size())), controller.getTitulo());
            myEditor.putString("id".concat(Integer.toString(controladorList.size())), controller.getId());
            myEditor.putInt("controladores", numControladores);
            myEditor.putBoolean("tiempar".concat(Integer.toString(controladorList.size())), controller.getTiempar());
            myEditor.putString("municipio".concat(Integer.toString(controladorList.size())), controller.getMunicipio());
            myEditor.commit();
        } catch (Exception e) {
        }
    }
    */

}
