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
	void restarVidaInfectado(int indiceInfectado, int nuevaVida, String rutaSonidoImpacto);
	void eliminarInfectado(int indiceInfectado);
	
	void actualizarPosProyectil(int indiceProyectil, float x, float y);
	void eliminarProyectil(int indiceProyectil);
	
	void actualizarEscape(String mensaje);
	
	void procesarInfeccionAgente(int numAgente);
	
	void actualizarCajaMensaje(String cajaMensaje);
	
	void actualizarNumOleada(int numOleada);
	void actualizarSustoPuntos(int sustoPuntos);
	void actualizarPuntosTotales(int puntos);
}
