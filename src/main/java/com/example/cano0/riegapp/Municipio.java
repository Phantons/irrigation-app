package com.example.cano0.riegapp;

/**
 * Created by cano0 on 02/04/2018.
 */

public class Municipio {
    private String COD;
    private String NAME;

    public Municipio(String cod, String name) {
        this.COD = cod;
        this.NAME = name;
    }

    public String getCOD() {
        return this.COD;
    }

    public String getNAME() {
        return this.NAME;
    }
}
