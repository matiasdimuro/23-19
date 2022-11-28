package com.wsi.surianodimuro.pantallas.juego;

public abstract class TiempoProcesosUnJug extends TiempoProcesos {

	public static float tpoRetardoSpawns = 5;
	public static float duracionOleada = 30;	

	public static void resetearTiempos() {
		tpoRetardoInicio = 3;
		tpoEntreOleadas = 10;	
		tpoRetardoSpawns = 5;
		duracionOleada = 30;	
		tpoRetardoInfecccion = 2.5f;
	}
}