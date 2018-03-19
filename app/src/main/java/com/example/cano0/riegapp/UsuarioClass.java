package com.example.cano0.riegapp;

import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;

/**
 * Created by cano0 on 17/03/2018.
 */

public class UsuarioClass {

    private static String nUser;
    private static List<Controlador> controlador_list;

    public UsuarioClass(String _nUser, List<Controlador> _controlador_list) {
        nUser = _nUser;
        if(controlador_list == null) { controlador_list = _controlador_list;}
        }

    public UsuarioClass() {

    }

    public static String getUser() {
        return nUser;
    }

    public static void setnUser(String _nUser) {
        nUser = _nUser;
    }

    public static void setControladorList(List<Controlador> _controlador_list) {
        controlador_list = _controlador_list;
    }

    public static void addControladorList(Controlador controlador) {
        controlador_list.add(controlador);
    }

    public static List<Controlador> getControladorList() {
        return controlador_list;
    }

    public static void setControladorN(Controlador controladorN) {
        for(int i = 0; i< controlador_list.size(); i++) {
            if (controlador_list.get(i).getId() == controladorN.getId()) {
                controlador_list.set(i, controladorN);
            }
        }
    }

    public Controlador getControlador(int n) {
        return controlador_list.get(n);
    }
}
