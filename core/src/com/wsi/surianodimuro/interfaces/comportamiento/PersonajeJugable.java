package com.wsi.surianodimuro.interfaces.comportamiento;

public interface PersonajeJugable extends Ente {

	void usarAscensor(float x, float y);
	
	void dispararProyectil();
	void dispararUltimate();
	
	void actualizarSustoPuntos(int sustoPuntos);
	void sumarVida();
}
