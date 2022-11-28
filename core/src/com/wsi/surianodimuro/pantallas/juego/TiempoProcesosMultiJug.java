package com.wsi.surianodimuro.pantallas.juego;

public abstract class TiempoProcesosMultiJug extends TiempoProcesos {
		
	public static float tpoRetardoSpawns = 3.5f;
	public static float duracionOleada = 45;	
	
	public static void resetearTiempos() {
		
		tpoRetardoInfecccion = 2.5f;
		tpoEntreOleadas = 10;
		tpoRetardoInicio = 3;
		tpoRetardoSpawns = 3.5f;
		duracionOleada = 45;	
	}
}
