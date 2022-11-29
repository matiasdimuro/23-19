package com.wsi.surianodimuro.pantallas.juego.entradas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.enumeradores.Armamentos;
import com.wsi.surianodimuro.interfaces.ProcesamientoEntradas;
import com.wsi.surianodimuro.objetos.Ascensor;
import com.wsi.surianodimuro.redes.InfoRed;
import com.wsi.surianodimuro.redes.MensajesCliente;
import com.wsi.surianodimuro.utilidades.Globales;

public final class EntradasComportamientoAgente implements ProcesamientoEntradas {

	@Override
	public void procesarEntrada() {

		int numAgente = Globales.cliente.numCliente - 1;

		/* Movimiento Horizontal */

		if (!((Gdx.input.isKeyPressed(Keys.LEFT)) && (Gdx.input.isKeyPressed(Keys.RIGHT)))) {

			if (Gdx.input.isKeyPressed(Keys.LEFT)) {

				Globales.jugadores.get(numAgente).controlador.caminando = true;
				Globales.jugadores.get(numAgente).controlador.mirandoIzquierda = true;
				Globales.jugadores.get(numAgente).controlador.mirandoDerecha = false;
				Globales.jugadores.get(numAgente).controlador.izquierda = true;

				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.CAMINAR_IZQUIERDA.getMensaje());
				} else {
					Globales.jugadores.get(numAgente).moverseIzquierda();
					if (Globales.movimientoAgenteListener.chequearColisiones()) {
						Globales.jugadores.get(numAgente).moverseDerecha();
					}
				}
			}

			else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {

				Globales.jugadores.get(numAgente).controlador.caminando = true;
				Globales.jugadores.get(numAgente).controlador.mirandoDerecha = true;
				Globales.jugadores.get(numAgente).controlador.mirandoIzquierda = false;
				Globales.jugadores.get(numAgente).controlador.derecha = true;

				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.CAMINAR_DERECHA.getMensaje());
				} else {
					Globales.jugadores.get(numAgente).moverseDerecha();
					if (Globales.movimientoAgenteListener.chequearColisiones()) {
						Globales.jugadores.get(numAgente).moverseIzquierda();
					}
				}
			}

			else {
				Globales.jugadores.get(numAgente).controlador.caminando = false;
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.MANTENERSE_QUIETO.getMensaje());
				}
			}
		}

		else {
			Globales.jugadores.get(numAgente).controlador.caminando = false;
			if (InfoRed.conexionGlobalEstablecida) {
				Globales.cliente.enviarMensaje(MensajesCliente.MANTENERSE_QUIETO.getMensaje());
			}
		}

		/* Movimiento Vertical */

		if (!((Gdx.input.isKeyPressed(Keys.UP)) && (Gdx.input.isKeyPressed(Keys.DOWN)))) {

			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				Globales.jugadores.get(numAgente).controlador.arriba = true;
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.SUBIR_ASCENSOR.getMensaje());
				}
			} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
				Globales.jugadores.get(numAgente).controlador.abajo = true;
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.BAJAR_ASCENSOR.getMensaje());
				}
			}

			if ((!InfoRed.conexionGlobalEstablecida) && ((Globales.jugadores.get(numAgente).controlador.arriba)
					|| (Globales.jugadores.get(numAgente).controlador.abajo))) {
				Ascensor ascensorOrigen = Globales.movimientoAgenteListener.chequearUbicacionEnAscensor();
				if (ascensorOrigen != null) {
					Globales.movimientoAgenteListener.procesarMovimientoVertical(ascensorOrigen);
				}
			}
		}

		/* Cambiar y utilizar armamento */

		if (Gdx.input.isKeyPressed(Keys.A)) {
			if (Globales.jugadores.get(numAgente).armaEnUso != 0) {
				Globales.jugadores.get(numAgente).armaEnUso = 0;
				Globales.sonidos.sonarCambioBoo2000();

				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.CAMBIAR_ARMA.getMensaje() + "#" + 0);
				}
			}
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			if (Globales.jugadores.get(numAgente).armaEnUso != 1) {
				Globales.jugadores.get(numAgente).armaEnUso = 1;
				Globales.sonidos.sonarCambioDesintox();

				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.CAMBIAR_ARMA.getMensaje() + "#" + 1);
				}
			}
		}

		if (Globales.jugadores.get(numAgente).controlador.puedeDisparar) {

			if (Gdx.input.isKeyPressed(Keys.C)) {
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.DISPARAR_PROYECTIL.getMensaje());
				} else {
					if (Globales.jugadores.get(numAgente).getArmamento()[Globales.jugadores.get(numAgente).armaEnUso]
							.getTipo() == Armamentos.DESINTOX) {
						Gdx.audio.newSound(Gdx.files.internal("sonidos/bloop.mp3")).play();
					} else {
						Gdx.audio.newSound(Gdx.files.internal("sonidos/waterDrop.mp3")).play();
					}
					Globales.jugadores.get(numAgente).dispararProyectil();
				}
				Globales.jugadores.get(numAgente).controlador.disparando = true;
				Globales.jugadores.get(numAgente).controlador.puedeDisparar = false;

			}

			else if ((Gdx.input.isKeyJustPressed(Keys.V))
					&& (Globales.jugadores.get(numAgente).getSustoPuntos() >= Globales.oleadaInfo.GRITOS_ULTIMATE)) {
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.DISPARAR_ULTIMATE.getMensaje());
				} else {
					Globales.jugadores.get(numAgente).dispararUltimate();
					Globales.jugadores.get(numAgente).actualizarSustoPuntos(-Globales.oleadaInfo.GRITOS_ULTIMATE);
				}
				Globales.jugadores.get(numAgente).controlador.disparando = true;
				Globales.jugadores.get(numAgente).controlador.puedeDisparar = false;
			}

			else {
				Globales.jugadores.get(numAgente).controlador.disparando = false;
				if (InfoRed.conexionGlobalEstablecida) {
					Globales.cliente.enviarMensaje(MensajesCliente.PARAR_FUEGO.getMensaje());
				}
			}
		}

		Globales.jugadores.get(numAgente).controlador.resetearEstados();
		if (InfoRed.conexionGlobalEstablecida) {
			Globales.cliente.enviarMensaje(MensajesCliente.RESETEAR_ESTADOS.getMensaje());
		}
	}
}
