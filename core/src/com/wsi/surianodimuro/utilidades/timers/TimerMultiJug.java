package com.wsi.surianodimuro.utilidades.timers;

import com.badlogic.gdx.Gdx;
import com.wsi.surianodimuro.enumeradores.Mensajes;
import com.wsi.surianodimuro.pantallas.juego.TiempoProcesos;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.utilidades.Globales;

public class TimerMultiJug extends Thread {
	
	private Agente jugadorUno;
	private Agente jugadorDos;

	private float tiempoMuertoDisparoJugUno;
	private float tiempoMuertoInfeccionJugUno;

	private float tiempoMuertoDisparoJugDos;
	private float tiempoMuertoInfeccionJugDos;

	private float supervivencia;
	private float duracionOleada;
	private float cronometro;

	public float timerMusicaFinal;

	public TimerMultiJug() {

		cronometro = 0;
		supervivencia = 0;
		duracionOleada = 0;
		timerMusicaFinal = 0;

		tiempoMuertoDisparoJugUno = 0;
		tiempoMuertoDisparoJugDos = 0;
		tiempoMuertoInfeccionJugUno = 0;
		tiempoMuertoInfeccionJugDos = 0;
		
		if (Globales.cliente.numCliente == 1) {
			jugadorUno = Globales.jugadores.get(0);
			jugadorDos = Globales.jugadores.get(1);
		} else {
			jugadorUno = Globales.jugadores.get(1);
			jugadorDos = Globales.jugadores.get(0);
		}
	}

	@Override
	public void run() {

		super.run();
		
		if ((Globales.datosPartida.terminada) && (timerMusicaFinal < 5)) {
			timerMusicaFinal += Gdx.graphics.getDeltaTime();
		}

		if (!Globales.datosPartida.terminada) {

			contarTiempoSupervivencia();

			/* Tiempo muerto entre cada disparo del Agente */

			if (!jugadorUno.controlador.puedeDisparar) {
				tiempoMuertoDisparoJugUno += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoDisparoJugUno >= jugadorUno.getArmamento()[jugadorUno.armaEnUso]
						.getTiempoMuertoDisparo()) {
					tiempoMuertoDisparoJugUno = 0;
					jugadorUno.controlador.puedeDisparar = true;
				}
			}

			if (!jugadorDos.controlador.puedeDisparar) {
				tiempoMuertoDisparoJugDos += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoDisparoJugDos >= jugadorDos.getArmamento()[jugadorDos.armaEnUso]
						.getTiempoMuertoDisparo()) {
					tiempoMuertoDisparoJugDos = 0;
					jugadorDos.controlador.puedeDisparar = true;
				}
			}

			/* Tiempo de retardo para volver a infectarse */

			if (!jugadorUno.controlador.puedeInfectarse) {
				tiempoMuertoInfeccionJugUno += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoInfeccionJugUno >= TiempoProcesos.tpoRetardoInfecccion) {
					jugadorUno.controlador.puedeInfectarse = true;
					tiempoMuertoInfeccionJugUno = 0;
				}
			}

			if (!jugadorDos.controlador.puedeInfectarse) {
				tiempoMuertoInfeccionJugDos += Gdx.graphics.getDeltaTime();
				if (tiempoMuertoInfeccionJugDos >= TiempoProcesos.tpoRetardoInfecccion) {
					jugadorDos.controlador.puedeInfectarse = true;
					tiempoMuertoInfeccionJugDos = 0;
				}
			}

			/* (1) Tiempo de retardo para el inicio de cada oleada. */
			procesarRetardoInicioOleadas();

			/* (2) Tiempo transcurrido hasta el fin de la oleada. */
			procesarTpoTranscurridoOleadas();

			/*
			 * (3) Tiempo medio transcurrido luego de cada oleada antes del comienzo de la
			 * siguiente.
			 */
			procesarEntreTiempoOleadas();
		}
	}

	public void contarTiempoSupervivencia() {
		supervivencia += Gdx.graphics.getDeltaTime();
		Globales.datosPartida.tiempoSupervivencia = supervivencia;
	}

	public void procesarEntreTiempoOleadas() {

		if ((Globales.oleadaInfo.oleadaComenzada) && (!Globales.oleadaInfo.oleadaEnCurso)) {
//			System.out.println("--> Procesando Entre tiempo Oleadas");
			cronometro += Gdx.graphics.getDeltaTime();
			if (!Globales.sonidos.musicaEntreRondaSonando) {
				Globales.sonidos.sonarMusicaEntreRonda();
//				System.out.println("-> Musica entre ronda sonando.");
			}
			if (cronometro >= TiempoProcesos.tpoEntreOleadas) {
				Globales.oleadaInfo.numOleada += 1;
				Globales.oleadaInfo.mejoraEfectuada = false;
				Globales.oleadaInfo.oleadaComenzada = false;
				Globales.oleadaInfo.actualizarIndicador = true;
				Globales.oleadaInfo.dificultadAumentada = false;
				Globales.sonidos.terminarMusicaEntreRonda();
//				System.out.println("-> Musica entre ronda terminada.");
				cronometro = 0;
			}
			Globales.cajaMensajes.setTexto(Mensajes.FIN_OLEADA.getMensaje());
		}
	}

	public void procesarTpoTranscurridoOleadas() {

		
		if ((Globales.oleadaInfo.oleadaComenzada) && (Globales.oleadaInfo.oleadaEnCurso)) {
//			System.out.println("--> Procesando Tiempo transcurrido Inicio Oleadas");

			duracionOleada += Gdx.graphics.getDeltaTime();
			cronometro += Gdx.graphics.getDeltaTime();

			Globales.oleadaInfo.tiempoTranscurrido = duracionOleada;

			/* (A) Spawn de Entes */
			if ((cronometro >= TiempoProcesos.tpoRetardoSpawns) && (duracionOleada < TiempoProcesos.duracionOleada)) {
				cronometro = 0;
				Globales.cajaMensajes.setTexto(Mensajes.COMIENZO_OLEADA.getMensaje());
			}

			/* (B) Oleada Terminada */
			if ((duracionOleada >= TiempoProcesos.duracionOleada) && (Globales.oleadaInfo.libreDeEntes)) {
				Globales.oleadaInfo.oleadaEnCurso = false;
				duracionOleada = 0;
				cronometro = 0;
				Globales.sonidos.terminarMusicaDeFondo();
//				System.out.println("-> Musica de fondo terminada.");
			}
		}
	}

	public void procesarRetardoInicioOleadas() {

		if (!Globales.oleadaInfo.oleadaComenzada) {
//			System.out.println("--> Procesando retardo Inicio Oleadas");
			cronometro += Gdx.graphics.getDeltaTime();
			if (cronometro >= TiempoProcesos.tpoRetardoInicio) {
				Globales.oleadaInfo.oleadaComenzada = true;
				Globales.oleadaInfo.oleadaEnCurso = true;
				cronometro = 0;
			}
			if (!Globales.sonidos.musicaDeFondoSonando) {
				Globales.sonidos.sonarMusicaDeFondo();
//				System.out.println("-> Poniendo musica de fondo.");
			}			
			Globales.cajaMensajes.setTexto(Mensajes.PREVIA_OLEADA.getMensaje());
		}
	}
}
