package com.wsi.surianodimuro.utilidades.timers;

import com.badlogic.gdx.Gdx;
import com.wsi.surianodimuro.pantallas.juego.TiempoProcesosUnJug;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.utilidades.Globales;

public class TimerUnJug extends Timer {

	private Agente jugadorUno;
	
	private float tiempoMuertoDisparo;
	private float tiempoMuertoInfeccion;
	
	private boolean pausa = false;

	public TimerUnJug() {
		tiempoMuertoDisparo = 0;
		tiempoMuertoInfeccion = 0;
		jugadorUno = Globales.jugadores.get(0);
	}

	@Override
	public void run() {

		super.run();
		
		if ((!pausa) && (!Globales.datosPartida.terminada)) {
			
			contarTiempoSupervivencia();
			
			/* Tiempo muerto entre cada disparo del Agente */
			
			if (!jugadorUno.controlador.puedeDisparar) {
				tiempoMuertoDisparo += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoDisparo >= jugadorUno.getArmamento()[jugadorUno.armaEnUso].getTiempoMuertoDisparo()) {
					tiempoMuertoDisparo = 0;
					jugadorUno.controlador.puedeDisparar = true;
				}
			}
			
			/* Tiempo de retardo para volver a infectarse */
			
			if (!jugadorUno.controlador.puedeInfectarse) {
				tiempoMuertoInfeccion += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoInfeccion >= TiempoProcesosUnJug.tpoRetardoInfecccion) {
					jugadorUno.controlador.puedeInfectarse = true;
					tiempoMuertoInfeccion = 0;
				}
			}
			
			/* (1) Tiempo de retardo para el inicio de cada oleada. */
			procesarRetardoInicioOleadas();
			
			/* (2) Tiempo transcurrido hasta el fin de la oleada. */
			procesarTpoTranscurridoOleadas();
			
			/* (3) Tiempo medio transcurrido luego de cada oleada antes del comienzo de la siguiente. */
			procesarEntreTiempoOleadas();
		}
	}
	
	public void alternarPausa() {
		
		pausa = !pausa;
		
		if (pausa) {
			if ((!Globales.oleadaInfo.oleadaComenzada) || (Globales.oleadaInfo.oleadaEnCurso)) {
				if (Globales.sonidos.musicaDeFondoSonando) {
					Globales.sonidos.pausarMusicaDeFondo();
				}
			}
			
			if ((Globales.oleadaInfo.oleadaComenzada) && (!Globales.oleadaInfo.oleadaEnCurso)) {
				if (Globales.sonidos.musicaEntreRondaSonando) {
					Globales.sonidos.pausarMusicaEntreRonda();
				}
			}
		}
		
		else {
			if ((!Globales.oleadaInfo.oleadaComenzada) || (Globales.oleadaInfo.oleadaEnCurso)) {
				if (!Globales.sonidos.musicaDeFondoSonando) {
					Globales.sonidos.despausarMusicaDeFondo();
				}
			}
			
			if ((Globales.oleadaInfo.oleadaComenzada) && (!Globales.oleadaInfo.oleadaEnCurso)) {
				if (!Globales.sonidos.musicaEntreRondaSonando) {
					Globales.sonidos.despausarMusicaEntreRonda();
				}
			}
		}
	}
}
