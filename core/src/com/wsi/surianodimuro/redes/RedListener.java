package com.wsi.surianodimuro.redes;

public interface RedListener {

	void moverAgenteIzquierda(int numCliente);
	void moverAgenteDerecha(int numCliente);
	
	void subirAgentePorAscensor(int numCliente, float nuevaPosX, float nuevaPosY);
	void bajarAgentePorAscensor(int numCliente, float nuevaPosX, float nuevaPosY);
	
	void cambiarArmaAgente(int numCliente, int numArmaNueva);
	void dispararProyectil(int numCliente);
	void dispararUltimate(int numCliente);
	
	void mantenerAgenteQuieto(int numCliente);
	void pararFuegoAgente(int numCliente);
	
	void resetearEstadosAgente(int numCliente);

	void procesarSpawnInfectado(String tipoInfectado, int numInfectado, float x, float y);
	void moverInfectadoIzquierda(int indice);
	void moverInfectadoDerecha(int indice);
	
//	void actualizarEscape(String mensaje);
	
	void aumentarVidaAgente();
//	void actualizarVidaAgente();
	
	void actualizarCajaMensaje(String cajaMensaje);
	
	void actualizarDisparo();
	void actualizarAlcance();
	void actualizarRapidez();
	
	void aumentarVelocidadSpawnRed();
	void aumentarDuracionOleadaRed();
	void actualizarNumOleada(String numOleada);
	void actualizarSustoPuntos(String sustoPuntos);

}
