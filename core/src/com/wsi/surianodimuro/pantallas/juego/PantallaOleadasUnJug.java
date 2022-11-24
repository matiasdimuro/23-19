package com.wsi.surianodimuro.pantallas.juego;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.wsi.surianodimuro.enumeradores.Ascensores;
import com.wsi.surianodimuro.enumeradores.DireccionesDisparo;
import com.wsi.surianodimuro.enumeradores.Infectados;
import com.wsi.surianodimuro.enumeradores.Mensajes;
import com.wsi.surianodimuro.enumeradores.Monstruos;
import com.wsi.surianodimuro.enumeradores.Ninios;
import com.wsi.surianodimuro.enumeradores.Proyectiles;
import com.wsi.surianodimuro.interfaces.InfectadosListables;
import com.wsi.surianodimuro.interfaces.ProcesosJugabilidad;
import com.wsi.surianodimuro.objetos.Ascensor;
import com.wsi.surianodimuro.objetos.PuertaSpawn;
import com.wsi.surianodimuro.pantallas.juego.hud.HudUnJug;
import com.wsi.surianodimuro.personajes.Infectado;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.personajes.agentes.AgenteUno;
import com.wsi.surianodimuro.personajes.agentes.armamento.proyectiles.Proyectil;
import com.wsi.surianodimuro.personajes.agentes.armamento.proyectiles.ProyectilDisparado;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Utiles;
import com.wsi.surianodimuro.utilidades.timers.TimerUnJug;

public final class PantallaOleadasUnJug extends PantallaOleadas implements ProcesosJugabilidad {

	private Agente jugadorUno;
	private HudUnJug hud;
	
	private TimerUnJug timer;
	
	public PantallaOleadasUnJug() {
		
		super();
		
		jugadorUno = new AgenteUno();
		Globales.jugadores.add(jugadorUno);
		
		Globales.mejorarEstadisticasListener = this;
		Globales.aumentarDificultadListener = this;
		Globales.actividadInfectadosListener = this;
		Globales.actividadProyectilesListener = this;
		Globales.movimientoAgenteListener = this;
	}

	@Override
	public void show() {
		
		super.show();
		
		jugadorUno.setPosicion(ConfigGraficos.ANCHO_MAPA - jugadorUno.getDimensiones()[0] - 10, mapa.getTienda().getPosicion().y);
		timer = new TimerUnJug();
		hud = new HudUnJug(mapa.getElemsHud());

		Globales.cajaMensajes = hud.getCajaMensajes();
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (!datosPartida.terminada) {
			
			timer.run();
			
			Utiles.batch.begin();
			hud.renderizar();
			jugadorUno.renderizar();
			Utiles.batch.end();
			
			mostrarIndicadores();
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
		
		if (!datosPartida.suspendida) {
			
			for (Infectado infectado : infectados) {
				if (infectado.controlador.mirandoIzquierda) {
					infectado.moverseIzquierda();
					infectado.caminarIzquierda();
				} else if (infectado.controlador.mirandoDerecha) {
					infectado.moverseDerecha();
					infectado.caminarDerecha();
				}
			}
			
			if ((oleadaInfo.oleadaEnCurso) && (infectados.size() > 0)) {
				if (jugadorUno.controlador.puedeInfectarse) {
					detectarInfecciones();
				}
				detectarEscapes();
				chequearVidaInfectados();
				chequearInfectadosEnMapa();
			}

			if (proyectilesDisparados.size() > 0) {
				procesarTrayectoriaProyectiles();
				chequearColisionProyectiles();
			}

			if ((proyectilesDisparados.size() > 0) && ((oleadaInfo.oleadaEnCurso) && (infectados.size() > 0))) {
				chequearProyectilesImpactados();
			}

			if (oleadaInfo.actualizarIndicador) {
				hud.getIndicadorOleada().actualizarDatos();
				oleadaInfo.actualizarIndicador = false;
			}

			if ((oleadaInfo.oleadaEnCurso) && (oleadaInfo.oleadaComenzada)) {
				hud.getIndicadorGrito().actualizarDatos();
			}

			if (!oleadaInfo.dificultadAumentada) {
				aumentarDificultad();
			}
			
			if (!oleadaInfo.mejoraEfectuada) {
				chequearAumentoEstadisticas();
			}

			if (!((datosPartida.escapesRestantesMonstruos > 0) && (datosPartida.escapesRestantesNinios > 0)
					&& (jugadorUno.vida > 0))) {
				datosPartida.terminada = true;
			}
		}
		
		else {
			Utiles.batch.begin();
			menuDeSuspension.renderizar();
			Utiles.batch.end();
		}
		
		if (!((datosPartida.escapesRestantesMonstruos > 0) && (datosPartida.escapesRestantesNinios > 0)
				&& (jugadorUno.vida > 0))) {
			datosPartida.terminada = true;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		hud.liberarMemoria();
		jugadorUno.liberarMemoria();
	}
	
	@Override
	public void procesarEntrada() {
		
		super.procesarEntrada();
		
		if (!datosPartida.terminada) {
			
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				datosPartida.suspendida = !datosPartida.suspendida;
				timer.alternarPausa();
			}
		}
	}
	
	@Override
	public boolean chequearColisiones() {

		int i = 0;
		boolean colisiona = false;

		do {
			RectangleMapObject obj = mapa.getColisiones()[i];
			if (jugadorUno.getRectangulo().overlaps(obj.getRectangle())) {
				colisiona = true;
			}
		} while ((!colisiona) && (++i < mapa.getColisiones().length));

		return colisiona;
	}

	@Override
	public Ascensor chequearUbicacionEnAscensor() {

		int i = 0;
		boolean colisiona = false;

		do {

			Ascensor ascensor = mapa.getAscensores()[i];
			Rectangle rectJug = jugadorUno.getRectangulo();

			// Xo del jugador entre Xo y Xf del ascensor (colisiona) ???
			if (((rectJug.getX() > ascensor.getRectangulo().getX())
					&& (rectJug.getX() < ascensor.getRectangulo().getX() + ascensor.getRectangulo().getWidth())
					&& (rectJug.getY() >= ascensor.getRectangulo().getY())
					&& (rectJug.getY() < ascensor.getRectangulo().getY() + ascensor.getRectangulo().getHeight()))
					// Jugador sobrepasa la mitad del ascensor ???
					&& (rectJug.getX() <= ascensor.getRectangulo().getX()
							+ ascensor.getRectangulo().getWidth() * 0.5f)) {
				colisiona = true;
			}

			// Xf del jugador entre Xo y Xf del ascensor (colisiona) ???
			else if (((rectJug.getX() + rectJug.getWidth() > ascensor.getRectangulo().getX())
					&& (rectJug.getX() + rectJug.getWidth() < ascensor.getRectangulo().getX()
							+ ascensor.getRectangulo().getWidth())
					&& (rectJug.getY() > ascensor.getRectangulo().getY())
					&& (rectJug.getY() < ascensor.getRectangulo().getY() + ascensor.getRectangulo().getHeight()))
					// Jugador sobrepasa la mitad del ascensor ???
					&& (rectJug.getX() + rectJug.getWidth() >= ascensor.getRectangulo().getX()
							+ ascensor.getRectangulo().getWidth() * 0.5f)) {
				colisiona = true;
			}

		} while ((!colisiona) && (++i < mapa.getAscensores().length));

		return (colisiona) ? mapa.getAscensores()[i] : null;
	}

	@Override
	public void procesarMovimientoVertical(Ascensor ascensorOrigen) {

		Ascensores tipoDestino = null;
		Ascensor ascensorDestino = null;

		if ((jugadorUno.controlador.arriba) && (ascensorOrigen.getArriba() != null)) {
			tipoDestino = ascensorOrigen.getArriba();
		} else if ((jugadorUno.controlador.abajo) && (ascensorOrigen.getAbajo() != null)) {
			tipoDestino = ascensorOrigen.getAbajo();
		}

		if (tipoDestino != null) {

			int i = 0;
			boolean encontrado = false;
			do {
				Ascensor asc = mapa.getAscensores()[i];
				if (tipoDestino.getNombre().equals(asc.getTipo().getNombre())) {
					encontrado = true;
					ascensorDestino = asc;
				}
			} while ((!encontrado) && (++i < mapa.getAscensores().length));

			float nuevaPosX = ascensorDestino.getPosicion().x + ascensorDestino.getDimensiones()[0] / 2
					- jugadorUno.getDimensiones()[0] / 2;
			float nuevaPosY = ascensorDestino.getPosicion().y;

			jugadorUno.usarAscensor(nuevaPosX, nuevaPosY);
			sonidos.sonarAscensor();
		}
	}

	@Override
	public void procesarTrayectoriaProyectiles() {

		for (ProyectilDisparado proyectilDisparado : proyectilesDisparados) {

			Proyectil proyectil = proyectilDisparado.proyectil;

			float caminoRecorrido = jugadorUno.getArmamento()[jugadorUno.armaEnUso].getVelocidadDisparo()
					* Gdx.graphics.getDeltaTime();
			float x = proyectil.getPosicion().x
					+ ((proyectilDisparado.getDireccion() == DireccionesDisparo.IZQUIERDA) ? -caminoRecorrido
							: caminoRecorrido);
			float y = proyectil.getPosicion().y;

			proyectil.setPosicion(x, y);
		}
	}

	@Override
	public void chequearColisionProyectiles() {

		ArrayList<ProyectilDisparado> proyectilesColisionados = new ArrayList<ProyectilDisparado>();

		for (ProyectilDisparado proyectilDisparado : proyectilesDisparados) {

			boolean colision = false;

			if (proyectilDisparado.proyectil.getTipo() != Proyectiles.ULTIMATE) {

				float alcance = jugadorUno.getArmamento()[jugadorUno.armaEnUso].getAlcance();
				float posAlcanceX = proyectilDisparado.posicionInicial.x
						+ ((proyectilDisparado.getDireccion() == DireccionesDisparo.IZQUIERDA) ? -alcance : alcance);

				if ((proyectilDisparado.getDireccion() == DireccionesDisparo.IZQUIERDA)
						&& (proyectilDisparado.proyectil.getPosicion().x <= posAlcanceX)) {
					colision = true;
				} else if ((proyectilDisparado.getDireccion() == DireccionesDisparo.DERECHA)
						&& (proyectilDisparado.proyectil.getPosicion().x >= posAlcanceX)) {
					colision = true;
				}
			}

			else {
				if ((proyectilDisparado.getDireccion() == DireccionesDisparo.IZQUIERDA)
						&& (proyectilDisparado.proyectil.getPosicion().x
								+ proyectilDisparado.proyectil.getDimensiones()[0] < 0)) {
					colision = true;
				} else if ((proyectilDisparado.getDireccion() == DireccionesDisparo.DERECHA)
						&& (proyectilDisparado.proyectil.getPosicion().x > ConfigGraficos.ANCHO_MAPA)) {
					colision = true;
				}
			}

			if (colision) {
				proyectilesColisionados.add(proyectilDisparado);
			}
		}

		for (ProyectilDisparado proyectilColisionado : proyectilesColisionados) {
			proyectilesDisparados.remove(proyectilColisionado);
		}
	}

	@Override
	public void chequearVidaInfectados() {

		for (Infectado infectado : infectados) {

			if ((!infectado.controlador.desinfectado) && (infectado.vida == 0)) {

				infectado.controlador.desinfectado = true;
				infectado.controlador.mirandoDerecha = false;
				infectado.controlador.mirandoIzquierda = true;

				String tipo = (infectado.getTipo() == Infectados.NINIO) ? "Ninio" : "Monstruo";
				String subPaquete = (tipo.equals("Ninio")) ? "ninios" : "monstruos";

				InfectadosListables infectadoListable = null;

				try {
					infectadoListable = (InfectadosListables) Class
							.forName("com.wsi.surianodimuro.personajes." + subPaquete + "." + tipo)
							.getDeclaredMethod("getTipo" + tipo).invoke(infectado);
					jugadorUno.actualizarSustoPuntos(infectadoListable.getSustoPuntos());
					datosPartida.puntajeTotal += infectadoListable.getSustoPuntos();
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void chequearProyectilesImpactados() {

		ArrayList<ProyectilDisparado> proyectilesDisparadosImpactados = new ArrayList<ProyectilDisparado>();

		for (Infectado infectado : infectados) {

			int i = 0;
			boolean impactado = false;

			do {

				ProyectilDisparado proyectilDisparado = proyectilesDisparados.get(i);

				if (infectado.getRectangulo().overlaps(proyectilDisparado.proyectil.getRectangulo())) {

					if (infectado.getDebilidad() == proyectilDisparado.proyectil.getTipo()) {
						Gdx.audio.newSound(Gdx.files.internal(proyectilDisparado.proyectil.getTipo().getRutaSonidoColision())).play();
						proyectilesDisparadosImpactados.add(proyectilDisparado);
						if (!infectado.controlador.desinfectado) {
							infectado.restarVida();;
						}
						impactado = true;
					} else if ((proyectilDisparado.proyectil.getTipo() == Proyectiles.ULTIMATE)
							&& (!infectado.controlador.desinfectado)) {
						infectado.vida = 0;
					}
				}

			} while ((!impactado) && (++i < proyectilesDisparados.size()));
		}

		for (ProyectilDisparado proyectilDisparadoImpactado : proyectilesDisparadosImpactados) {
			proyectilesDisparados.remove(proyectilDisparadoImpactado);
		}
	}

	@Override
	public void detectarEscapes() {

		Rectangle zonaEscape = mapa.getZonaEscape().getRectangle();
		for (Infectado infectado : infectados) {
			if ((infectado.getPosicion().x) > (zonaEscape.getX() + zonaEscape.getWidth())) {
				if (infectado.getDebilidad() == Proyectiles.PROYECTIL_DESINTOX) {
					sonidos.sonarEscapeMonstruo();
					datosPartida.escapesRestantesMonstruos -= 1;
					hud.getIndicadorEscMonstruos().actualizar();
					Globales.cajaMensajes.setTexto(Mensajes.ESCAPE_MONSTRUO.getMensaje());
				} else if (infectado.getDebilidad() == Proyectiles.PROYECTIL_BOO2000) {
					sonidos.sonarEscapeNinio();
					datosPartida.escapesRestantesNinios -= 1;
					hud.getIndicadorEscNinios().actualizar();
					Globales.cajaMensajes.setTexto(Mensajes.ESCAPE_NINIO.getMensaje());
				}
			}
		}
	}
	
	@Override
	public void detectarInfecciones() {

		int i = 0;
		boolean infeccion = false;

		do {
			Infectado infectado = infectados.get(i);
			if ((infectado.vida > 0) && (infectado.getRectangulo().overlaps(jugadorUno.getRectangulo()))) {
				infeccion = true;
				jugadorUno.restarVida();
				jugadorUno.controlador.puedeInfectarse = false;
				Globales.cajaMensajes.setTexto(Mensajes.INFECTADO.getMensaje());
				hud.getIndicadorVidasJugUno().actualizar();
				if (jugadorUno.vida > 0) {
					Gdx.audio.newSound(Gdx.files.internal("sonidos/oof.mp3")).play();
				}
			}
		} while ((!infeccion) && (++i < infectados.size()));
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
	public void aumentarVida() {
		jugadorUno.sumarVida();
		hud.getIndicadorVidasJugUno().actualizar();
	}

	@Override
	public void aumentarRapidez() {
		jugadorUno.incrementarVelocidad();
	}

	@Override
	public void aumentarAlcance() {
		jugadorUno.getArmamento()[0].aumentarAlcance();
		jugadorUno.getArmamento()[1].aumentarAlcance();
	}

	@Override
	public void aumentarVelDisparo() {
		jugadorUno.getArmamento()[0].aumentarVelocidadDisparo();
		jugadorUno.getArmamento()[1].aumentarVelocidadDisparo();
	}
	
	@Override
	public void spawnearInfectado() {

		int random = Utiles.rand.nextInt(Infectados.values().length);
		Infectado infectado = (random == 0) ? Ninios.retornarNinio(Utiles.rand.nextInt(Ninios.values().length))
				: Monstruos.retornarMonstruo(Utiles.rand.nextInt(Monstruos.values().length));

		int numPuerta = Utiles.rand.nextInt(mapa.getPuertasSpawn().length);
		float x = mapa.getPuertasSpawn()[numPuerta].getPosicion().x
				+ mapa.getPuertasSpawn()[numPuerta].getDimensiones()[0] / 2;
		float y = mapa.getPuertasSpawn()[numPuerta].getPosicion().y;

		if (oleadaInfo.aumentarVelocidadInfectados) {
			aumentarVelocidadInfectado(infectado);
		}

		infectado.setPosicion(x, y);
		infectados.add(infectado);
	}
	
	@Override
	public void chequearInfectadosEnMapa() {

		ArrayList<Infectado> infectadosFueraDeMapa = new ArrayList<Infectado>();

		Rectangle zonaEscape = mapa.getZonaEscape().getRectangle();
		PuertaSpawn puerta = mapa.getPuertasSpawn()[0];

		for (Infectado infectado : infectados) {
			float x = infectado.getPosicion().x;
			if (((infectado.controlador.mirandoDerecha) && (x >= zonaEscape.getX() + zonaEscape.getWidth()))
					|| ((infectado.controlador.mirandoIzquierda)
							&& (x + infectado.getDimensiones()[0] <= puerta.getPosicion().x))) {
				infectadosFueraDeMapa.add(infectado);
			}
		}

		for (Infectado infectadoFueraDeMapa : infectadosFueraDeMapa) {
			infectados.remove(infectadoFueraDeMapa);
		}
	}
}
