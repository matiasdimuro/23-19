package com.wsi.surianodimuro.utilidades.timers;

import com.badlogic.gdx.Gdx;
import com.wsi.surianodimuro.enumeradores.Mensajes;
import com.wsi.surianodimuro.pantallas.juego.TiempoProcesosUnJug;
import com.wsi.surianodimuro.utilidades.Globales;

public abstract class Timer extends Thread {

	private float supervivencia;
	private float duracionOleada;
	private float cronometro;
	
	public float timerMusicaFinal;
	
	public Timer() {
		cronometro = 0;
		supervivencia = 0;
		duracionOleada = 0;
		timerMusicaFinal = 0;
	}
	
	@Override
	public void run() {

		super.run();
	
		if ((Globales.datosPartida.terminada) && (timerMusicaFinal < 5)) {
			timerMusicaFinal += Gdx.graphics.getDeltaTime();
		}
	}

	public void contarTiempoSupervivencia() {
		supervivencia += Gdx.graphics.getDeltaTime();
		Globales.datosPartida.tiempoSupervivencia = supervivencia;
	}

	public void procesarEntreTiempoOleadas() {
		
		if ((Globales.oleadaInfo.oleadaComenzada) && (!Globales.oleadaInfo.oleadaEnCurso)) {
			cronometro += Gdx.graphics.getDeltaTime();
			if (!Globales.sonidos.musicaEntreRondaSonando) {
				Globales.sonidos.sonarMusicaEntreRonda();
			}
			if (cronometro >= TiempoProcesosUnJug.tpoEntreOleadas) {
				Globales.oleadaInfo.numOleada += 1;
				Globales.oleadaInfo.mejoraEfectuada = false;
				Globales.oleadaInfo.oleadaComenzada = false;
				Globales.oleadaInfo.actualizarIndicador = true;
				Globales.oleadaInfo.dificultadAumentada = false;
				Globales.sonidos.terminarMusicaEntreRonda();
				cronometro = 0;
			}
			Globales.cajaMensajes.setTexto(Mensajes.FIN_OLEADA.getMensaje());
		}
	}

	public void procesarTpoTranscurridoOleadas() {

		if ((Globales.oleadaInfo.oleadaComenzada) && (Globales.oleadaInfo.oleadaEnCurso)) {
			
			duracionOleada += Gdx.graphics.getDeltaTime();
			cronometro += Gdx.graphics.getDeltaTime();
			
			Globales.oleadaInfo.tiempoTranscurrido = duracionOleada;
			
			/* (A) Spawn de Entes */
			if ((cronometro >= TiempoProcesosUnJug.tpoRetardoSpawns) && (duracionOleada < TiempoProcesosUnJug.duracionOleada)) {
				cronometro = 0;
				Globales.actividadInfectadosListener.spawnearInfectado();
				Globales.cajaMensajes.setTexto(Mensajes.COMIENZO_OLEADA.getMensaje());
			}
			
			/* (B) Oleada Terminada */
			if ((duracionOleada >= TiempoProcesosUnJug.duracionOleada) && (Globales.oleadaInfo.libreDeEntes)) {
				Globales.oleadaInfo.oleadaEnCurso = false;
				duracionOleada = 0;
				cronometro = 0;
				Globales.sonidos.terminarMusicaDeFondo();
			}
		}
	}

	public void procesarRetardoInicioOleadas() {
		
		if (!Globales.oleadaInfo.oleadaComenzada) {
			cronometro += Gdx.graphics.getDeltaTime();
			if (cronometro >= TiempoProcesosUnJug.tpoRetardoInicio) {
				Globales.oleadaInfo.oleadaComenzada = true;
				Globales.oleadaInfo.oleadaEnCurso = true;
				cronometro = 0;
			}
			if (!Globales.sonidos.musicaDeFondoSonando) {
				Globales.sonidos.sonarMusicaDeFondo();
			}			
			Globales.cajaMensajes.setTexto(Mensajes.PREVIA_OLEADA.getMensaje());
		}
	}
}
