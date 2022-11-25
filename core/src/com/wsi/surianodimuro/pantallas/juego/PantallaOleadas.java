package com.wsi.surianodimuro.pantallas.juego;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wsi.surianodimuro.enumeradores.Ascensores;
import com.wsi.surianodimuro.interfaces.AumentarDificultadListener;
import com.wsi.surianodimuro.interfaces.MejorarEstadisticasListener;
import com.wsi.surianodimuro.interfaces.MovimientoAgenteListener;
import com.wsi.surianodimuro.interfaces.ProcesamientoEntradas;
import com.wsi.surianodimuro.objetos.Ascensor;
import com.wsi.surianodimuro.pantallas.Pantalla;
import com.wsi.surianodimuro.pantallas.juego.entradas.EntradasComportamientoAgente;
import com.wsi.surianodimuro.pantallas.juego.entradas.EntradasMenuJuegoTerminado;
import com.wsi.surianodimuro.pantallas.juego.entradas.EntradasPausa;
import com.wsi.surianodimuro.pantallas.juego.menus.MenuJuegoTerminado;
import com.wsi.surianodimuro.pantallas.juego.menus.MenuSuspension;
import com.wsi.surianodimuro.personajes.Infectado;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.personajes.agentes.armamento.proyectiles.ProyectilDisparado;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Utiles;

public abstract class PantallaOleadas extends Pantalla implements MejorarEstadisticasListener, AumentarDificultadListener, MovimientoAgenteListener {
	
	protected OleadaInfo oleadaInfo;
	protected DatosPartida datosPartida;

	protected Mapa mapa;
	
	protected ArrayList<Infectado> infectados;
	protected ArrayList<ProyectilDisparado> proyectilesDisparados;

	protected MenuSuspension menuDeSuspension;
	protected MenuJuegoTerminado menuJuegoTerminado;
	
	private ProcesamientoEntradas entradasPausa = new EntradasPausa();
	private ProcesamientoEntradas entradasAgente = new EntradasComportamientoAgente();
	private ProcesamientoEntradas entradasJuegoTerminado = new EntradasMenuJuegoTerminado();
	
	protected Sonidos sonidos;

	public PantallaOleadas() {

		sonidos = new Sonidos();
		oleadaInfo = new OleadaInfo();
		datosPartida = new DatosPartida();
		infectados = new ArrayList<Infectado>();
		proyectilesDisparados = new ArrayList<ProyectilDisparado>();

		Globales.jugadores = new ArrayList<Agente>();
		
		Globales.sonidos = sonidos;
		Globales.oleadaInfo = oleadaInfo;
		Globales.datosPartida = datosPartida;

		Globales.infectados = infectados;
		Globales.proyectilesDisparados = proyectilesDisparados;
		
		Globales.movimientoAgenteListener = this;
	}

	@Override
	public void show() {

		super.show();

		cam = new OrthographicCamera(ConfigGraficos.ANCHO_MAPA, ConfigGraficos.ALTO_MAPA);
		cam.setToOrtho(false, ConfigGraficos.ANCHO_MAPA, ConfigGraficos.ALTO_MAPA);

		mapa = new Mapa(cam);
		cam.update();

		viewport = new FitViewport(ConfigGraficos.ANCHO_MAPA, ConfigGraficos.ALTO_MAPA, cam);
		cam.update();

		menuDeSuspension = new MenuSuspension();
		menuJuegoTerminado = new MenuJuegoTerminado();
		
		Globales.menuJuegoTerminado = menuJuegoTerminado;
		Globales.menuSuspension = menuDeSuspension;
	}

	@Override
	public void render(float delta) {

		super.render(delta);
		
		if (!datosPartida.terminada) {
			
			Utiles.batch.begin();
			
			mapa.renderizar();

			if (oleadaInfo.oleadaEnCurso) {
				oleadaInfo.libreDeEntes = (infectados.size() > 0) ? false : true;
				if (!oleadaInfo.libreDeEntes) {
					for (Infectado infectado : infectados) {
						infectado.renderizar();
					}
				}
			}

			for (ProyectilDisparado proyectilDisparado : proyectilesDisparados) {
				proyectilDisparado.proyectil.renderizar();
			}

			Utiles.batch.end();
			
			procesarEntrada();
		}

		else {
			if (sonidos.musicaDeFondoSonando) {
				sonidos.terminarMusicaDeFondo();
			}
			if (sonidos.musicaEntreRondaSonando) {
				sonidos.terminarMusicaEntreRonda();				
			}
			
			if (!sonidos.alarmaSonando) {
				sonidos.sonarAlarma();
			}
			
			cam = new OrthographicCamera(ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA);
			cam.setToOrtho(false, ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA);
			
			viewport = new FitViewport(ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA, cam);
			cam.update();			
			
			Utiles.batch.begin();
			menuJuegoTerminado.renderizar();
			Utiles.batch.end();
			
			procesarEntrada();
		}
	}


	@Override
	public void dispose() {

		super.dispose();
		
		mapa.liberarMemoria();

		for (ProyectilDisparado proyectilDisparado : proyectilesDisparados) {
			proyectilDisparado.proyectil.liberarMemoria();
		}

		for (Infectado infectado : infectados) {
			infectado.liberarMemoria();
		}

		menuDeSuspension.liberarMemoria();
		menuJuegoTerminado.liberarMemoria();
		
		sonidos.dispose(); 
	}

	@Override
	public void procesarEntrada() {

		super.procesarEntrada();
		
		if (!datosPartida.terminada) {
			
			if (!datosPartida.suspendida) {
				entradasAgente.procesarEntrada();
			}
			else if (datosPartida.suspendida) {
				entradasPausa.procesarEntrada();
			}
		}
		
		else {
			entradasJuegoTerminado.procesarEntrada();
		}
	}

	

	@Override
	public void aumentarDuracionOleada() {
		TiempoProcesos.duracionOleada += 10;
	}

	@Override
	public void aumentarVelocidadSpawn() {
		if (TiempoProcesos.tpoRetardoSpawns >= 1) {
			TiempoProcesos.tpoRetardoSpawns -= .5f;
		}
	}

	@Override
	public void aumentarVelocidadInfectado(Infectado infectado) {
		for (int i = 0; i < oleadaInfo.aumentoDeVelocidad; i++) {
			infectado.incrementarVelocidad();
		}
	}

	@Override
	public void aumentarDificultad() {

		oleadaInfo.aumentarSpawns = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_AUMENTO_SPAWNS == 0);
		oleadaInfo.aumentarDuracionOleada = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_AUMENTO_DURACION == 0);
		oleadaInfo.aumentarVelocidadInfectados = (oleadaInfo.numOleada
				% oleadaInfo.INTERVALO_OLEADAS_AUMENTO_VELOCIDAD == 0);

		if (oleadaInfo.aumentarVelocidadInfectados) {
			oleadaInfo.aumentoDeVelocidad = oleadaInfo.numOleada / oleadaInfo.INTERVALO_OLEADAS_AUMENTO_VELOCIDAD;
		}

		if (oleadaInfo.aumentarSpawns) {
			aumentarVelocidadSpawn();
			oleadaInfo.dificultadAumentada = true;
		}
		if (oleadaInfo.aumentarDuracionOleada) {
			aumentarDuracionOleada();
			oleadaInfo.dificultadAumentada = true;
		}
	}

	@Override
	public void chequearAumentoEstadisticas() {
		
		oleadaInfo.mejoraRapidez = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_MEJORA_RAPIDEZ == 0);
		oleadaInfo.mejoraVelDisparo = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_MEJORA_VEL_DISP == 0);
		oleadaInfo.mejoraAlcance = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_MEJORA_ALCANCE == 0);
		oleadaInfo.mejoraVida = (oleadaInfo.numOleada % oleadaInfo.INTERVALO_OLEADAS_MEJORA_VIDA == 0);

		if (oleadaInfo.mejoraRapidez) {
			aumentarRapidez();
			oleadaInfo.mejoraEfectuada = true;
		}
		if (oleadaInfo.mejoraVelDisparo) {
			aumentarVelDisparo();
			oleadaInfo.mejoraEfectuada = true;
		}
		if (oleadaInfo.mejoraAlcance) {
			aumentarAlcance();
			oleadaInfo.mejoraEfectuada = true;
		}
		if (oleadaInfo.mejoraVida) {
			aumentarVida();
			oleadaInfo.mejoraEfectuada = true;
		}
	}

	@Override
	public void aumentarVida() {
	}

	@Override
	public void aumentarRapidez() {
	}

	@Override
	public void aumentarAlcance() {
	}

	@Override
	public void aumentarVelDisparo() {
	}
	
	@Override
	public boolean chequearColisiones() {

		int i = 0;
		boolean colisiona = false;

		do {
			RectangleMapObject obj = mapa.getColisiones()[i];
			if (Globales.jugadores.get(Globales.cliente.numCliente - 1).getRectangulo().overlaps(obj.getRectangle())) {
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
			Rectangle rectJug = Globales.jugadores.get(Globales.cliente.numCliente - 1).getRectangulo();

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

		if ((Globales.jugadores.get(Globales.cliente.numCliente - 1).controlador.arriba) && (ascensorOrigen.getArriba() != null)) {
			tipoDestino = ascensorOrigen.getArriba();
		} else if ((Globales.jugadores.get(Globales.cliente.numCliente - 1).controlador.abajo) && (ascensorOrigen.getAbajo() != null)) {
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
					- Globales.jugadores.get(Globales.cliente.numCliente - 1).getDimensiones()[0] / 2;
			float nuevaPosY = ascensorDestino.getPosicion().y;

			Globales.jugadores.get(Globales.cliente.numCliente - 1).usarAscensor(nuevaPosX, nuevaPosY);
			sonidos.sonarAscensor();
		}
	}
}
