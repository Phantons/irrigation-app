package com.example.cano0.riegapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;
import es.upm.etsit.irrigation.shared.Mode;
import es.upm.etsit.irrigation.shared.Schedule;
import es.upm.etsit.irrigation.shared.Zone;
import es.upm.etsit.irrigation.util.DayOfWeek;
import es.upm.etsit.irrigation.util.Time;
import es.upm.etsit.irrigation.util.LocalTime;

public class ZoneActivity extends AppCompatActivity {

    private SaveZoneAsyntask saveZoneAsyntask = null;
    private UsuarioClass usuarioClass;
    private Controlador controlador;
    private int nControlador;
    private Mode mode;
    private int nMode;
    private Zone zone;
    private int nZone;
    private boolean[] days;
    private boolean tiempar = false;

    private EditText initialHourN;
    private EditText initialMinuteN;
    private EditText finalHourN;
    private EditText finalMinuteN;
    private EditText nomZone;
    private EditText portZone;

    private ArrayAdapter<Time> adapter;
    //array con las horas de riego. Intervalo de 15 minutos
    private List<Time> timeIrrigate;
    private Schedule schedule;
    private DayOfWeek dayOfWeek;
    private FloatingActionButton addInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialHourN = (EditText) findViewById(R.id.initialhour);
        initialMinuteN = (EditText) findViewById(R.id.initialminute);
        finalHourN = (EditText) findViewById(R.id.finalhour);
        finalMinuteN = (EditText) findViewById(R.id.finalminute);
        nomZone = (EditText) findViewById(R.id.nomZone);
        portZone = (EditText) findViewById(R.id.portZone);

        FloatingActionButton saveZone = (FloatingActionButton) findViewById(R.id.fab);
        saveZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveZone();
            }
        });

        addInterval = findViewById(R.id.addInterval);
        addInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newInterval();
            }
        });

        getZoneSelected();
        configureDayTable();

    }

    /**
     * Halla la zona en la que se encuentra
     */
    private void getZoneSelected() {
        nControlador = getIntent().getExtras().getInt("nControlador");
        nMode = getIntent().getExtras().getInt("nMode");
        nZone = getIntent().getExtras().getInt("nZone");
        usuarioClass = new UsuarioClass();
        controlador = usuarioClass.getControlador(nControlador);
        mode = controlador.getList_modo().get(nMode);
        if((nZone < mode.getZones().size()) && (mode.getZones().size() != 0)) {
            zone = mode.getZones().get(nZone);
            schedule = zone.getSchedule();
            timeIrrigate = schedule.getIrrigationCycles();
            days = schedule.getDays();
            nomZone.setText(zone.getName());
            portZone.setText(Integer.toString(zone.getPinAddress()));
        } else {
            zone = new Zone("", 0);
            timeIrrigate = new ArrayList<>();
            days = new boolean[7];
            schedule = new Schedule(days, timeIrrigate);
            mode.addZone(zone);
            controlador.setMode(mode);
            usuarioClass.setControladorN(controlador);
        }
    }

    /**
     * Configura las horas de la tabla donde seleccionar el riego
     */
    private void configureDayTable() {
        try {
            ListView listaView = (ListView) findViewById(R.id.listTimes);
            adapter = new TimeAdapter(ZoneActivity.this, timeIrrigate, nControlador, nMode, nZone);
            listaView.setAdapter(adapter);
            listaView.refreshDrawableState();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "error adapter", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * método que crea Intervalo de Riego
     * a partir de lo recogido al darle al botón de crear
     */
    private void newInterval() {
        hideSoftKeyboard();
        if(initialHourN.getText().toString() != "" && initialMinuteN.getText().toString() != "" && finalHourN.getText().toString() !="" && finalHourN.getText().toString() != "") {
            int initialHour = Integer.parseInt(initialHourN.getText().toString());
            int initialMinute = Integer.parseInt(initialMinuteN.getText().toString());
            int finalHour = Integer.parseInt(finalHourN.getText().toString());
            int finalMinute = Integer.parseInt(finalMinuteN.getText().toString());
            if(initialHour > 24 || finalHour > 24 || initialMinute > 59 || finalMinute > 59) {
                Toast.makeText(this, "Introduzca unos tiempoos válidos", Toast.LENGTH_SHORT).show();
                return;
            }
            int interval = (finalHour - initialHour) * 3600 + (finalMinute - initialMinute) * 60;
            if (interval > 0) {
                LocalTime localTime = LocalTime.of(initialHour, initialMinute);
                Time time = new Time(localTime, interval);
                timeIrrigate.add(time);
                adapter.notifyDataSetChanged();
                configureDayTable();
            } else {
                Toast.makeText(this, "introduzca un tiempo final mayor que el inicial", Toast.LENGTH_SHORT).show();
            }
            initialHourN.getText().clear();
            initialMinuteN.getText().clear();
            finalHourN.getText().clear();
            finalMinuteN.getText().clear();
        }
    }


    /**
     * Guarda La Zona configurada
     */
    private void saveZone() {
        if(nomZone.getText().toString().isEmpty() || portZone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Introduzca todos los parámetros", Toast.LENGTH_SHORT).show();
            return;
        }
        schedule = new Schedule(days, timeIrrigate);
        zone = new Zone(nomZone.getText().toString(), Integer.parseInt(portZone.getText().toString()));
        zone.setSchedule(schedule);
        zone.setShouldTakeWeather(tiempar);
        mode.setZone(nZone, zone);
        controlador.setMode(mode);
        saveZoneAsyntask = new SaveZoneAsyntask(controlador);
        saveZoneAsyntask.execute((Void) null);
    }

    /**
     * metodo que recoge los dias en los que se riega
     * @param view
     */
    public void onCheckDayClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.check_take_weather:
                tiempar =checked;
                break;
            case R.id.day1:
                days[0] = checked;
                break;
            case R.id.day2:
                days[1] = checked;
                break;
            case R.id.day3:
                days[2] = checked;
                break;
            case R.id.day4:
                days[3] = checked;
                break;
            case R.id.day5:
                days[4] = checked;
                break;
            case R.id.day6:
                days[5] = checked;
                break;
            case R.id.day7:
                days[6] = checked;
                break;
        }
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
        if(zone.getName().isEmpty()) {
            usuarioClass.getControlador(nControlador).getList_modo().get(nMode).getZones().remove(nZone);
        }
        Intent i = new Intent(this, ModeActivity.class);
        i.putExtra("nControlador", nControlador);
        i.putExtra("nMode", nMode);
        startActivity(i);
        finish();
    }
    public class SaveZoneAsyntask extends AsyncTask<Void, Void, Integer> {

        private Controlador controladorEnviado;

        private  SaveZoneAsyntask(Controlador _controlador) {
            this.controladorEnviado = _controlador;
        }


        @Override
        protected Integer doInBackground(Void... params) {
            return SocketHandler.sendOrUpdateController(controladorEnviado);
        }

        @Override
        protected void onPostExecute(final Integer success) {
            if(success == 1) {
                usuarioClass.setControladorN(controlador);
                Toast.makeText(ZoneActivity.this, Integer.toString(nControlador) + " Modo: " + Integer.toString(nMode), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ZoneActivity.this, "Fallo en la conexión al intentar guardar cambios", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(ZoneActivity.this, ModeActivity.class);
            i.putExtra("nControlador", nControlador);
            i.putExtra("nMode", nMode);
            startActivity(i);
            finish();
        }

        @Override
        protected void onCancelled() {
        }
    }
}
