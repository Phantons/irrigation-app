package com.example.cano0.riegapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsit.irrigation.shared.Mode;
/**
 * Created by cano0 on 17/03/2018.
 */

public class ModesSaved {
    private Mode active_mode;
    private List<Mode> list_mode;
    private SharedPreferences outputStream;


    public Mode getActive_mode(int nControlador) {
        String modeselectedS = "modeselected" + Integer.toString(nControlador);
        String modeSelected;
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(modeselectedS);
            jsonArray = jsonObject.getJSONArray("modeselected");
            active_mode = new Mode(jsonArray.getJSONObject(nControlador).getInt("ID" ), jsonArray.getJSONObject(nControlador).getString("name"));
        } catch (Exception e) {
        }
        return  active_mode;
    }

    public List<Mode> getList_mode(int nControlador) {
        String modeS = "mode" + Integer.toString(nControlador);
        String modeSelected;
        JSONObject jsonObject;
        JSONArray jsonArray;
        list_mode = new ArrayList<>();
        try {
            jsonObject = new JSONObject("modes");
            jsonArray = jsonObject.getJSONArray(modeS);
            for(int i = 0; i< jsonArray.length(); i++) {
                list_mode.add(new Mode(jsonArray.getJSONObject(nControlador).getInt("ID" ), jsonArray.getJSONObject(nControlador).getString("name")));
            }
        } catch (Exception e) {
        }
        return  list_mode;
    }
}
