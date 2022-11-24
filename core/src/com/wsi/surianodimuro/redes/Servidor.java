package com.wsi.surianodimuro.redes;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Servidor {

	public static InetAddress IP_SERVER;
	public static final int PUERTO_SERVER = 9001;
	
	public static boolean iniciado = false;
	public static boolean online = false;
	
	public static void cargarServidor() {
		try {
			IP_SERVER = InetAddress.getByName("255.255.255.255");
			iniciado = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void cerrar() {
		InfoRed.conexionGlobalEstablecida = false;
		Servidor.iniciado = false;
		Servidor.online = false;
	}
	
	public static void iniciar() {
		Servidor.iniciado = true;
		Servidor.online = true;
		InfoRed.companieroConectado = false;
	}
}
