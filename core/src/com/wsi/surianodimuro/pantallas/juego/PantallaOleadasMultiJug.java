package com.wsi.surianodimuro.pantallas.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.enumeradores.Infectados;
import com.wsi.surianodimuro.enumeradores.Mensajes;
import com.wsi.surianodimuro.enumeradores.Monstruos;
import com.wsi.surianodimuro.enumeradores.Ninios;
import com.wsi.surianodimuro.pantallas.juego.hud.HudMultiJug;
import com.wsi.surianodimuro.pantallas.menu.PantallaJugadorDesconectado;
import com.wsi.surianodimuro.personajes.Infectado;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.personajes.agentes.AgenteDos;
import com.wsi.surianodimuro.personajes.agentes.AgenteUno;
import com.wsi.surianodimuro.personajes.agentes.armamento.proyectiles.ProyectilDisparado;
import com.wsi.surianodimuro.redes.InfoRed;
import com.wsi.surianodimuro.redes.MensajesServidor;
import com.wsi.surianodimuro.redes.RedListener;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Utiles;
import com.wsi.surianodimuro.utilidades.timers.TimerMultiJug;

public final class PantallaOleadasMultiJug extends PantallaOleadas implements RedListener {

	private Agente jugadorUno;
	private Agente jugadorDos;
	private HudMultiJug hud;

	private TimerMultiJug timer;

	public PantallaOleadasMultiJug() {

		super();

		if (Globales.cliente.numCliente == 1) {
			jugadorUno = new AgenteUno();
			jugadorDos = new AgenteDos();
			Globales.jugadores.add(jugadorUno);
			Globales.jugadores.add(jugadorDos);
		} else {
			jugadorUno = new AgenteDos();
			jugadorDos = new AgenteUno();
			Globales.jugadores.add(jugadorDos);
			Globales.jugadores.add(jugadorUno);
		}

		Globales.redListener = this;
	}

	@Override
	public void show() {

		super.show();

		jugadorUno.setPosicion(ConfigGraficos.ANCHO_MAPA - jugadorUno.getDimensiones()[0] - 10,
				mapa.getTienda().getPosicion().y);
		jugadorDos.setPosicion(ConfigGraficos.ANCHO_MAPA - jugadorUno.getDimensiones()[0] - 10,
				mapa.getTienda().getPosicion().y);
		timer = new TimerMultiJug();
		hud = new HudMultiJug(mapa.getElemsHud());

		Globales.cajaMensajes = hud.getCajaMensajes();
	}

	@Override
	public void render(float delta) {

		super.render(delta);

		if (!InfoRed.conexionGlobalEstablecida) {
			
			TiempoProcesos.resetearTiempos();
			
			if (Globales.sonidos.musicaDeFondoSonando) {
				Globales.sonidos.terminarMusicaDeFondo();
			}
			else if (Globales.sonidos.musicaEntreRondaSonando) {
				Globales.sonidos.terminarMusicaEntreRonda();
			}
			
			if (!InfoRed.companieroConectado) {
				System.out.println("-> Companiero Desconectado!!!!");
				Globales.juego.setScreen(new PantallaJugadorDesconectado());
			}
			Globales.soundtrack.play();
		}

		else {
			if (!datosPartida.terminada) {

				timer.run();

				Utiles.batch.begin();

				hud.renderizar();

				if (oleadaInfo.oleadaEnCurso) {
					oleadaInfo.libreDeEntes = (infectados.size() > 0) ? false : true;
					if (!oleadaInfo.libreDeEntes) {
						for (Infectado infectado : infectados) {
							infectado.renderizar();
						}
					}
				}

				jugadorUno.renderizar();
				jugadorDos.renderizar();
				
				for (ProyectilDisparado proyectilDisparado : proyectilesDisparados) {
					proyectilDisparado.proyectil.renderizar();
				}

				Utiles.batch.end();
				mostrarIndicadores();

				if (datosPartida.suspendida) {
					Utiles.batch.begin();
					menuDeSuspension.renderizar();
					Utiles.batch.end();
				}
			}

			else {
				timer.timerMusicaFinal += Gdx.graphics.getDeltaTime();
				if (timer.timerMusicaFinal > 5) {
					if (sonidos.alarmaSonando) {
						sonidos.pausarMusicaAlarma();
					}
					if ((!sonidos.musicaFinalSonando) && (!menuJuegoTerminado.terminado)) {
						sonidos.sonarMusicaFinal();
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		hud.liberarMemoria();
		jugadorUno.liberarMemoria();
		jugadorDos.liberarMemoria();
	}

	@Override
	public void procesarEntrada() {

		super.procesarEntrada();

		if (!datosPartida.terminada) {
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				datosPartida.suspendida = !datosPartida.suspendida;
			}
		}
	}

	private void mostrarIndicadores() {

		for (Infectado infectado : infectados) {
			infectado.getIndicadorVida().renderizar();
		}

		if (oleadaInfo.oleadaEnCurso) {
			hud.getIndicadorOleada().mostrarIndicador();
		}

		hud.getIndicadorGrito().mostrarIndicador();
	}

	
	
	
	
	@Override
	public void moverAgenteIzquierda(int numAgente) {

		Globales.jugadores.get(numAgente).controlador.caminando = true;
		Globales.jugadores.get(numAgente).controlador.mirandoIzquierda = true;
		Globales.jugadores.get(numAgente).controlador.mirandoDerecha = false;
		Globales.jugadores.get(numAgente).controlador.izquierda = true;
		Globales.jugadores.get(numAgente).moverseIzquierda();
	}

	@Override
	public void moverAgenteDerecha(int numAgente) {

		Globales.jugadores.get(numAgente).controlador.caminando = true;
		Globales.jugadores.get(numAgente).controlador.mirandoDerecha = true;
		Globales.jugadores.get(numAgente).controlador.mirandoIzquierda = false;
		Globales.jugadores.get(numAgente).controlador.derecha = true;
		Globales.jugadores.get(numAgente).moverseDerecha();
	}

	@Override
	public void subirAgentePorAscensor(int numAgente, float nuevaPosX, float nuevaPosY) {

		Globales.jugadores.get(numAgente).controlador.arriba = true;
		Globales.jugadores.get(numAgente).usarAscensor(nuevaPosX, nuevaPosY);
		sonidos.sonarAscensor();
	}

	@Override
	public void bajarAgentePorAscensor(int numAgente, float nuevaPosX, float nuevaPosY) {

		Globales.jugadores.get(numAgente).controlador.abajo = true;
		Globales.jugadores.get(numAgente).usarAscensor(nuevaPosX, nuevaPosY);
		 sonidos.sonarAscensor();
	}

	@Override
	public void cambiarArmaAgente(int numAgente, int numArmaNueva) {

		Globales.jugadores.get(numAgente).armaEnUso = numArmaNueva;
	}

	@Override
	public void dispararProyectil(int numAgente) {

		Globales.jugadores.get(numAgente).controlador.disparando = true;
		Globales.jugadores.get(numAgente).dispararProyectil();
		Globales.jugadores.get(numAgente).controlador.puedeDisparar = false;
		
//		if (numAgente == Globales.cliente.numCliente - 1) {
			Gdx.audio.newSound(Gdx.files.internal(Globales.jugadores.get(numAgente).getArmamento()[Globales.jugadores.get(numAgente).armaEnUso].getTipoProyectil().getRutaSonidoDisparo())).play();
//		}
	}

	@Override
	public void dispararUltimate(int numAgente) {

		Globales.jugadores.get(numAgente).controlador.disparando = true;
		Globales.jugadores.get(numAgente).dispararUltimate();
		Globales.jugadores.get(numAgente).actualizarSustoPuntos(-Globales.oleadaInfo.GRITOS_ULTIMATE);
		Globales.jugadores.get(numAgente).controlador.puedeDisparar = false;
	}

	@Override
	public void mantenerAgenteQuieto(int numAgente) {
		Globales.jugadores.get(numAgente).controlador.caminando = false;
	}

	@Override
	public void pararFuegoAgente(int numAgente) {
		Globales.jugadores.get(numAgente).controlador.disparando = false;
	}

	@Override
	public void resetearEstadosAgente(int numAgente) {
		Globales.jugadores.get(numAgente).controlador.resetearEstados();
	}

	
	
	
	
	@Override
	public void procesarSpawnInfectado(String tipoInfectado, int numInfectado, float x, float y) {

		Infectado nuevoInfectado = null;

		if (tipoInfectado.equals(Infectados.MONSTRUO.toString())) {
			nuevoInfectado = Monstruos.retornarMonstruo(numInfectado);
		}

		else if (tipoInfectado.equals(Infectados.NINIO.toString())) {
			nuevoInfectado = Ninios.retornarNinio(numInfectado);
		}

		nuevoInfectado.setPosicion(x, y);
		infectados.add(nuevoInfectado);
	}

	@Override
	public void moverInfectadoIzquierda(int indice) {
		if ((infectados.size() > 0) && (indice < infectados.size())) {
			infectados.get(indice).controlador.mirandoIzquierda = true;
			infectados.get(indice).controlador.mirandoDerecha = false;
			infectados.get(indice).moverseIzquierda();
			infectados.get(indice).caminarIzquierda();
		}
	}

	@Override
	public void moverInfectadoDerecha(int indice) {
		if ((infectados.size() > 0) && (indice < infectados.size())) {
			infectados.get(indice).controlador.mirandoDerecha = true;
			infectados.get(indice).controlador.mirandoIzquierda = false;
			infectados.get(indice).moverseDerecha();
			infectados.get(indice).caminarDerecha();
		}
	}
	
	@Override
	public void restarVidaInfectado(int indiceInfectado, int nuevaVida, String rutaSonidoImpacto) {
		if ((infectados.size() > 0) && (indiceInfectado < infectados.size())) {
			infectados.get(indiceInfectado).vida = nuevaVida;
			Gdx.audio.newSound(Gdx.files.internal(rutaSonidoImpacto)).play();
		}
	}
	
	@Override
	public void eliminarInfectado(int indiceInfectado) {
		if ((infectados.size() > 0) && (indiceInfectado < infectados.size())) {
			infectados.remove(indiceInfectado);
		}
	}
	
	
	
	
	
	@Override
	public void actualizarPosProyectil(int indiceProyectil, float x, float y) {
		if ((proyectilesDisparados.size() > 0) && (indiceProyectil < proyectilesDisparados.size())) {
			proyectilesDisparados.get(indiceProyectil).proyectil.setPosicion(x, y);
		}
	}
	
	@Override
	public void eliminarProyectil(int indiceProyectil) {
		if (proyectilesDisparados.size() > 0) {
			proyectilesDisparados.remove(indiceProyectil);
		}
	}

	

	@Override
	public void actualizarDisparo() {
		for (int i = 0; i < jugadorUno.getArmamento().length; i++) {
			jugadorUno.getArmamento()[i].aumentarVelocidadDisparo();
		}
		for (int i = 0; i < jugadorDos.getArmamento().length; i++) {
			jugadorDos.getArmamento()[i].aumentarVelocidadDisparo();
		}
	}

	@Override
	public void actualizarAlcance() {
		jugadorUno.getArmamento()[0].aumentarAlcance();
		jugadorUno.getArmamento()[1].aumentarAlcance();
		jugadorDos.getArmamento()[0].aumentarAlcance();
		jugadorDos.getArmamento()[1].aumentarAlcance();
	}

	@Override
	public void actualizarRapidez() {
		jugadorUno.incrementarVelocidad();
		jugadorDos.incrementarVelocidad();
	}

	@Override
	public void aumentarVidaAgente() {
		jugadorUno.sumarVida();
		jugadorDos.sumarVida();
	}

	@Override
	public void procesarInfeccionAgente(int numAgente) {
		// TODO
//		System.out.println("-> Agente " + numAgente + " infectado.");
//		System.out.println("-> Agente Uno: " + jugadorUno.vida);
//		System.out.println("-> Agente Dos: " + jugadorDos.vida);
		
		Globales.jugadores.get(numAgente).restarVida();
		Globales.jugadores.get(numAgente).controlador.puedeInfectarse = false;
		
		if (numAgente == 0) {
			if (Globales.cliente.numCliente == 1) {
				hud.getIndicadorVidasJugUno().actualizar();
			}
			else if (Globales.cliente.numCliente == 2) {
				hud.getIndicadorVidasJugDos().actualizar();
			}
		}
		
		else if (numAgente == 1) {
			if (Globales.cliente.numCliente == 1) {
				hud.getIndicadorVidasJugDos().actualizar();
			}
			else if (Globales.cliente.numCliente == 2) {
				hud.getIndicadorVidasJugUno().actualizar();
			}
		}
		
		Gdx.audio.newSound(Gdx.files.internal("sonidos/oof.mp3")).play();
	}
	
	
	
	@Override
	public void actualizarEscape(String mensaje) {
		if (mensaje.equals(MensajesServidor.ESCAPE_MONSTRUO.getMensaje())) {
			datosPartida.escapesRestantesMonstruos -= 1;
			hud.getIndicadorEscMonstruos().actualizar(); 
			actualizarCajaMensaje(Mensajes.ESCAPE_MONSTRUO.getMensaje());
		} else {
			datosPartida.escapesRestantesNinios -= 1;
			hud.getIndicadorEscNinios().actualizar();
			actualizarCajaMensaje(Mensajes.ESCAPE_NINIO.getMensaje());
		}
	}
	
	@Override
	public void actualizarCajaMensaje(String cajaMensaje) {
		Globales.cajaMensajes.setTexto(cajaMensaje);		
	}

	@Override
	public void actualizarNumOleada(int numOleada) {
		Globales.oleadaInfo.numOleada = numOleada;
		hud.getIndicadorOleada().actualizarDatos();
	}

	@Override
	public void aumentarVelocidadSpawnRed() {
		aumentarVelocidadSpawn();
	}

	@Override
	public void aumentarDuracionOleadaRed() {
		aumentarDuracionOleada();
	}

	// TODO: Implementar desde server
	@Override
	public void actualizarSustoPuntos(/*String*/int sustoPuntos) {
//		hud.getIndicadorGrito().newActualizarDatos(sustoPuntos);	
		Globales.jugadores.get(0).sustoPuntos = sustoPuntos;
		hud.getIndicadorGrito().actualizarDatos();
	}
}
