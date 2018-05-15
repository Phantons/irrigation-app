package com.example.cano0.riegapp;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cano0 on 02/04/2018.
 */

public class Municipio {
    private String COD;
    private String NAME;
    private int n;

    public Municipio(int n,String cod, String name) {
        this.n =  n;
        this.COD = cod;
        this.NAME = name;
    }

    public Municipio(int n, JSONObject jsonObject) {
        try {
            this.n = n;
            this.COD = jsonObject.getString("COD");
            this.NAME = jsonObject.getString("NAME");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public String getCOD() {
        return this.COD;
    }

    public String getNAME() {
        return this.NAME;
    }
}
