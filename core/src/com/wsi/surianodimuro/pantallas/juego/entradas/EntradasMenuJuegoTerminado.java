package com.wsi.surianodimuro.pantallas.juego.entradas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.interfaces.ProcesamientoEntradas;
import com.wsi.surianodimuro.pantallas.juego.TiempoProcesos;
import com.wsi.surianodimuro.pantallas.menu.PantallaMenuOpciones;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Helpers;
import com.wsi.surianodimuro.utilidades.GuardarRecords;

public final class EntradasMenuJuegoTerminado implements ProcesamientoEntradas {

	@Override
	public void procesarEntrada() {

		float x = Gdx.input.getX();
		float y = ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY();

		Boton btnMenu = Globales.menuJuegoTerminado.getBtnMenu();
		Boton btnSalir = Globales.menuJuegoTerminado.getBtnSalir();

		boolean posicionadoEncima = false;

		if ((x > btnMenu.getRectangulo().getX())
				&& (x < btnMenu.getRectangulo().getX() + btnMenu.getRectangulo().getWidth())
				&& (y > btnMenu.getRectangulo().getY())
				&& (y < btnMenu.getRectangulo().getY() + btnMenu.getRectangulo().getHeight())) {
			Globales.menuJuegoTerminado.opc = 1;
			posicionadoEncima = true;
		} else if ((x > btnSalir.getRectangulo().getX())
				&& (x < btnSalir.getRectangulo().getX() + btnSalir.getRectangulo().getWidth())
				&& (y > btnSalir.getRectangulo().getY())
				&& (y < btnSalir.getRectangulo().getY() + btnSalir.getRectangulo().getHeight())) {
			Globales.menuJuegoTerminado.opc = 2;
			posicionadoEncima = true;
		}

		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			Globales.menuJuegoTerminado.opc = 1;
		} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			Globales.menuJuegoTerminado.opc = 2;
		}

		if ((posicionadoEncima) && (Gdx.input.isTouched()) || (Gdx.input.isKeyJustPressed(Keys.ENTER))) {

			Globales.menuJuegoTerminado.terminado = true;
			solicitarGuardado();

			if (Globales.menuJuegoTerminado.opc == 1) {
				
				if (Globales.sonidos.musicaFinalSonando) {
					Globales.sonidos.pausarMusicaFinal();
				}
				if (Globales.sonidos.alarmaSonando) {
					Globales.sonidos.pausarMusicaAlarma();
				}
				TiempoProcesos.resetearTiempos();
				
//				if (!InfoRed.conexionGlobalEstablecida) {
					Globales.soundtrack.play();
//				}

				if (Globales.cliente.conectadoAlServidor) {
					Globales.cliente.desconectarseDelServidor();
				}
				
				Globales.juego.setScreen(new PantallaMenuOpciones());
			
			} else {
				Globales.soundtrack.dispose();
				Helpers.cerrarJuego();
			}
		}
	}

	private void solicitarGuardado() {
		GuardarRecords.cargar();
		GuardarRecords.rcrd.aniadirRecord(Globales.datosPartida.puntajeTotal);
		GuardarRecords.guardar();
	}
}
