package com.example.cano0.riegapp;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import es.upm.etsit.irrigation.shared.Controlador;


public class SocketHandler {
    private static String token;

	/**
	 * Hace log in en el servidor.
	 * @return 0 -> error en la transmisión
	 *         1 -> log in correcto
	 *		   2 -> log in incorrecto
	 *		   3 -> error desconocido
	 */
    public static int login(String user, String pass) {
		String[] respuestas = ServerConection.consultarAlServidor("/LogIn/" + user + "/" + pass + "/", 1);

		if (respuestas == null)
			return 0;

		String respuesta = respuestas[0];

        if(respuesta != null) {
            if(respuesta.substring(0, 10).equals("/Correcto/")){
				SocketHandler.token = respuesta.substring(10, respuesta.length()-1);
                return 1;
            }
            else if(respuesta.substring(0, 10).equals("/Incorrec/")){
                return 2;
            }
            else {
            	return 3;
            }
        }
        return 0;
    }
	
	/**
	 * Crea un nuevo usuario en el servidor.
	 * @return 0 -> error en la transmisión
	 *		   2 -> Usuario/contraseña no valido (debe tener entre 5 y 25 caracteres sin espacios)
	 *		   3 -> nombre de usuario ocupado
	 *		   4 -> error desconocido
	 */
    public static int addNewUser(String user, String pass) {
        String[] respuestas = ServerConection.consultarAlServidor("/NewUs/" + user + "/" + pass + "/", 1);

        if (respuestas == null)
        	return 0;

        String respuesta = respuestas[0];

        if(respuesta != null){
            if(respuesta.equals("/CreadoOk/")){
                return 1;
            }
            else if(respuesta.equals("/BadUsPas/")){
                return 2;
            }
			else if(respuesta.equals("/IdOcupad/")){
				return 3;
			}
			else{
				return 4;
			}
        }
        return 0;
    }
	
	/**
	 * Riega ahora mismo en la zona "port" durante un tiempo de "timeoutInSeconds" en segundos
	 * @return 0 -> error en la transmisión
	 *         1 -> enviado correctamente
	 *		   2 -> no se pudo realizar esa peticion
	 *		   3 -> token malo
	 *		   4 -> error desconocido
	 */
    public static int irrigateNow(int port, int timeoutInSeconds, String idControlador) {
		String[] respuestas = ServerConection.consultarAlServidor("/Regar/" + SocketHandler.token + "/" + port + "/" + timeoutInSeconds + "/" + idControlador + "/", 1);

		if (respuestas == null)
			return 0;

		String respuesta = respuestas[0];


		if(respuesta != null) {
            if(respuesta.equals("/True/")){
                return 1;
            }
            else if(respuesta.equals("/False/")){
                return 2;
            }
            else if(respuesta.equals("/TokenMal/")){
            	return 3;
            }
            else{
            	return 4;
            }
        }
        return 0;
    }
	
	/**
	 * Te devuelve un array de boolean que te dice si esta regando esa zona o no.
	 * Por ejemplo si quieres saber si esta regando la zona 0. Tendrias que usarlo asi:
	 *   Boolean[] data = SocketHandler.isWatering();
	 *   if (data[0] == true) ... 
	 *
	 * true es que esta regando, false que no esta regando, null que esa zona no esta activa (no hay ninguna electrovalvula
	 * conectada)
	 * @return el array de boolean.
	 */
    public static Boolean[] isWatering(String idControlador) {
        Boolean[] regando = new Boolean[32];
		String[] respuestas = ServerConection.consultarAlServidor("/GetRe/" + SocketHandler.token + "/" + idControlador + "/", 1);

		if (respuestas == null)
			return regando;

		String respuesta = respuestas[0];

		if(respuesta != null){
	        if(respuesta.substring(0, 10).equals("/AnswerRg/")){
		        String[] separados = respuesta.substring(10, respuesta.length()-1).split("/");
		        for(int j1 = 0; j1 < separados.length; j1++){
		        	if(separados[j1].equals("null") == false){
		            	regando[j1] = Boolean.parseBoolean(separados[j1]);
		        	}
		        	else{
		        		regando[j1] = null;
		        	}
		        }
		        return regando;
		    }
		    else if(respuesta.substring(0, 10).equals("/TokenMal/")){
		    	return new Boolean[0];
		    }
		}
		return null;
    }
	
	/**
	 *  Hace logout del servidor
	 */
    public static void logout() {
        ServerConection.consultarAlServidor("/LogOu/" + SocketHandler.token + "/", 0);
    }
	
	/**
	 * Envía un nuevo controlador o un controlador actualizado al servidor
	 * @return 0 -> error en la transmisión
	 *		   1 -> todo correcto
	 *		   2 -> token incorrecto
	 *		   3 -> error desconocido
	 */
    public static int sendOrUpdateController(Controlador controlador) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(bs);
			os.writeObject(controlador);
			os.close();
		} catch (Exception e) {

		}
		
		byte[] controladorSerializado = bs.toByteArray();
		Formatter formatter = new Formatter();
		for(byte b: controladorSerializado){
			formatter.format("%02x", b);
		}
    	String controladorString = formatter.toString();
		String[] respuestas = ServerConection.consultarAlServidor("/UpCon/" + SocketHandler.token + "/" + controladorString + "/" + controlador.getId() + "/", 1);

		if (respuestas == null)
			return 0;

		String respuesta = respuestas[0];

		if(respuesta != null){
    		if(respuesta.equals("/Correcto/")){
    			return 1;
    		}
    		else if(respuesta.equals("/TokenMal/")){
    			return 2;
    		}
    		else{
    			return 3;
    		}
    	}
    	return 0;
    }
	
	/**
	 * Coge todos los controladores del servidor para este usuario.
	 * @return toda la lista de controladores. null si ha habido un error.
	 */
    public static List<Controlador> getAllControllers() {
    	String peneEnviar = "/AllCo/" + SocketHandler.token + "/";
    	System.out.println(peneEnviar);
    	String[] respuestas = ServerConection.consultarAlServidor(peneEnviar, 1);

		if (respuestas == null) {
			System.out.println("cacaaaaaaaaaa");
			return null;
		}

		String respuesta = respuestas[0];

    	if(respuesta != null) {
    		if(respuesta.substring(0, 10).equals("/AnswerCo/")){
				String[] separados = new String[0];
    			if(respuesta.length() > 10){
    				separados = respuesta.substring(10, respuesta.length() - 1).split("/");
				}
    			List<Controlador> salida = new ArrayList<Controlador>();
				
    			for(int j1 = 0; j1 < separados.length; j1 = j1 + 1) {
    				try {
						byte[] controllerInBytes = hexStringToByteArray(separados[j1]);
						ByteArrayInputStream dataInputStream = new ByteArrayInputStream(controllerInBytes);
						ObjectInputStream is = new ObjectInputStream(dataInputStream);
						Controlador controlador = (Controlador) is.readObject();
						salida.add(controlador);
					} catch (Exception e) {
    					e.printStackTrace();
					}
				}
				
    			return salida;
    		}
    		else if(respuesta.substring(0, 10).equals("/TokenMal/")){
    			System.out.println("TOKEN MAAAAAL");
    			return null;
    		}
    	}
		System.out.println("finaaaaaaaaaaal");
    	return null;
    }
	public static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}
}