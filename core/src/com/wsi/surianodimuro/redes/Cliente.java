package com.wsi.surianodimuro.redes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.wsi.surianodimuro.pantallas.juego.PantallaOleadasMultiJug;
import com.wsi.surianodimuro.pantallas.menu.PantallaEsperandoJugador;
import com.wsi.surianodimuro.pantallas.menu.PantallaServidorDesconectado;
import com.wsi.surianodimuro.utilidades.Globales;

public class Cliente extends Thread implements Disposable {

	private DatagramSocket socket;

	public int numCliente = 1; // Por default, 1.

	public boolean fin = false;
	public boolean offline = true;
	public boolean iniciado = false;
	public boolean conectadoAlServidor = false;

	public Cliente() {
		System.out.println("Cliente creado.");
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		iniciado = true;
		offline = false;

		while (!fin) {
//			System.out.println(".");	// NO BORRAR ESTA LINEA - DESCOMENTAR ESTA LINEA
			if (!offline) {
				byte[] datos = new byte[1024];
				DatagramPacket datagrama = new DatagramPacket(datos, datos.length);
				try {
//					System.out.println("- Escuchando mensajes ...");
					socket.receive(datagrama);
					procesarMensaje(datagrama);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public void enviarMensaje(String msg) {
		byte[] datos = msg.getBytes();
		DatagramPacket datagrama = new DatagramPacket(datos, datos.length, Servidor.IP_SERVER, Servidor.PUERTO_SERVER);
		try {
			socket.send(datagrama);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void conectarseAlServidor() {
		System.out.println("-> Probando conexion al servidor ...");
		enviarMensaje(MensajesCliente.SOLICITAR_CONEXION.getMensaje());
	}

	public void desconectarseDelServidor() {
		System.out.println("-> Desconectandose del servidor ...");
		enviarMensaje(MensajesCliente.DESCONECTARSE_DEL_SERVIDOR.getMensaje());
		reiniciarCliente();
	}

	public void procesarMensaje(DatagramPacket datagrama) {

		String mensaje = new String(datagrama.getData()).trim();
//		System.out.println("- Mensaje: " + mensaje);

		String[] mensajeParametrizado = mensaje.split("#");

		if (mensajeParametrizado[0].equals(MensajesServidor.EMPEZAR_JUEGO.getMensaje())) {
			
			System.out.println("*** Ha comenzado el juego! ***");
			InfoRed.conexionGlobalEstablecida = true;
			InfoRed.companieroConectado = true;
			
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					Globales.juego.setScreen(new PantallaOleadasMultiJug());
					Globales.soundtrack.pause();
				}
			});
		}

		else if (mensajeParametrizado[0].equals(MensajesServidor.TERMINAR_JUEGO.getMensaje())) {
			System.out.println("*** Juego terminado! ***");
			Globales.datosPartida.terminada = true;
		}

		else if (mensajeParametrizado[0].equals(MensajesServidor.CERRAR_SERVIDOR.getMensaje())) {
			
			System.out.println("=> Oops, se ha cerrado el servidor");
			reiniciarCliente();
			Servidor.cerrar();
			offline = true;
			
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					Globales.juego.setScreen(new PantallaServidorDesconectado());
				}
			});
		}

		else if (mensajeParametrizado[0].equals(MensajesServidor.SOLICITUD_ACEPTADA.getMensaje())) {
			
			System.out.println("-> El cliente se ha conectado al servidor.");
			conectadoAlServidor = true;
			
			Servidor.iniciar();
			Servidor.IP_SERVER = datagrama.getAddress();
			
			numCliente = Integer.parseInt(mensajeParametrizado[1]);
			Globales.cliente.numCliente = Integer.parseInt(mensajeParametrizado[1]);
			
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					System.out.println("- Esperando Jugador ...");
					Globales.juego.setScreen(new PantallaEsperandoJugador());
				}
			});
		}
		
		else if (mensajeParametrizado[0].equals(MensajesServidor.SOLICITUD_RECHAZADA.getMensaje())) {
			System.out.println("-> El servidor se encuentra lleno.");
		}

		else if (mensajeParametrizado[0].equals(MensajesServidor.DESCONECTAR_CLIENTE.getMensaje())) {
			System.out.println("=> Has sido desconectado");
			InfoRed.conexionGlobalEstablecida = false;
			InfoRed.companieroConectado = false;
			reiniciarCliente();
		}

		else if ((InfoRed.conexionGlobalEstablecida) && (!Globales.datosPartida.terminada)) {

			if (mensajeParametrizado[0].equals(MensajesServidor.MOVER_AGENTE_IZQUIERDA.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.moverAgenteIzquierda(numAgente);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.MOVER_AGENTE_DERECHA.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.moverAgenteDerecha(numAgente);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.SUBIR_AGENTE_POR_ASCENSOR.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				float nuevaPosX = Float.parseFloat(mensajeParametrizado[2]);
				float nuevaPosY = Float.parseFloat(mensajeParametrizado[3]);
				Globales.redListener.subirAgentePorAscensor(numAgente, nuevaPosX, nuevaPosY);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.BAJAR_AGENTE_POR_ASCENSOR.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				float nuevaPosX = Float.parseFloat(mensajeParametrizado[2]);
				float nuevaPosY = Float.parseFloat(mensajeParametrizado[3]);
				Globales.redListener.bajarAgentePorAscensor(numAgente, nuevaPosX, nuevaPosY);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.CAMBIAR_ARMA_AGENTE.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				int numArmaNueva = Integer.parseInt(mensajeParametrizado[2]);
				Globales.redListener.cambiarArmaAgente(numAgente, numArmaNueva);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.DISPARAR_PROYECTIL.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Globales.redListener.dispararProyectil(numAgente);
					}
				});
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.DISPARAR_ULTIMATE.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Globales.redListener.dispararUltimate(numAgente);
					}
				});
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.MANTENER_AGENTE_QUIETO.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.mantenerAgenteQuieto(numAgente);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.PARAR_FUEGO_AGENTE.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.pararFuegoAgente(numAgente);
			}

			else if (mensajeParametrizado[0].equals(MensajesServidor.RESETEAR_ESTADOS_AGENTE.getMensaje())) {
				final int numAgente = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.resetearEstadosAgente(numAgente);
			}

			/* CONTROLAR DE ACA PARA ABAJO */
			
			else if (mensajeParametrizado[0].equals(MensajesServidor.SPAWNEAR_INFECTADO.getMensaje())) {
				
				final String tipoInfectado = mensajeParametrizado[1];
				final int numInfectado = Integer.parseInt(mensajeParametrizado[2]);
				final float nuevaPosX = Float.parseFloat(mensajeParametrizado[3]);
				final float nuevaPosY = Float.parseFloat(mensajeParametrizado[4]);
				
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Globales.redListener.procesarSpawnInfectado(tipoInfectado, numInfectado, nuevaPosX, nuevaPosY);
					}
				});
			}

			// TODO ProcesarMovimiento
			else if (mensajeParametrizado[0].equals(MensajesServidor.MOVER_INFECTADO_IZQUIERDA.getMensaje())) {
				int indiceInfectado = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.moverInfectadoIzquierda(indiceInfectado);
			}

			// TODO ProcesarMovimiento
			else if (mensajeParametrizado[0].equals(MensajesServidor.MOVER_INFECTADO_DERECHA.getMensaje())) {
				int indiceInfectado = Integer.parseInt(mensajeParametrizado[1]);
				Globales.redListener.moverInfectadoDerecha(indiceInfectado);
			}

//			else if (mensajeParametrizado[0].equals(MensajesServidor.ESCAPE_MONSTRUO.getMensaje())
//					|| (mensajeParametrizado[0].equals(MensajesServidor.ESCAPE_NINIOS.getMensaje()))) {
//				final String tipoEscape = mensajeParametrizado[0];
//				Gdx.app.postRunnable(new Runnable() {
//					public void run() {
//						Globales.redListener.actualizarEscape(tipoEscape);
//					}
//				});
//			}

//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_RAPIDEZ.getMensaje())) {
//				Globales.redListener.actualizarRapidez();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_ALCANCE.getMensaje())) {
//				Globales.redListener.actualizarAlcance();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_DISPARO.getMensaje())) {
//				Globales.redListener.actualizarDisparo();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_VIDA_AGENTE.getMensaje())) {
//				Gdx.app.postRunnable(new Runnable() {
//					public void run() {
//						Globales.redListener.aumentarVidaAgente();
//					}
//				});
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.ACTUALIZAR_VIDA_AGENTE.getMensaje())) {
//				Globales.redListener.actualizarVidaAgente();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.ACTUALIZAR_CAJA_MENSAJES.getMensaje())) {
//				final String cajaMensaje = mensajeParametrizado[1];
//				Globales.redListener.actualizarCajaMensaje(cajaMensaje);
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.ACTUALIZAR_INDICADOR_GRITO.getMensaje())) {
//				final String sustoPuntos = mensajeParametrizado[1];
//				Globales.redListener.actualizarSustoPuntos(sustoPuntos);
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_VELOCIDAD_SPAWN.getMensaje())) {
//				Globales.redListener.aumentarVelocidadSpawnRed();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.AUMENTAR_DURACION_OLEADA.getMensaje())) {
//				Globales.redListener.aumentarDuracionOleadaRed();
//			}
//
//			else if (mensajeParametrizado[0].equals(MensajesServidor.ACTUALIZAR_INDICADOR_OLEADA.getMensaje())) {
//				final String numOleada = mensajeParametrizado[1];
//				Globales.redListener.actualizarNumOleada(numOleada);
//			}

		}
	}

	public void reiniciarCliente() {

		Servidor.iniciado = false;
		conectadoAlServidor = false;
		numCliente = 1;
	}

	@Override
	public void dispose() {
		this.interrupt();
		fin = true;
		if (conectadoAlServidor) {
			System.out.println("-> Cerrando juego y cliente!");
			desconectarseDelServidor();
		}
		System.exit(0);
	}
}
