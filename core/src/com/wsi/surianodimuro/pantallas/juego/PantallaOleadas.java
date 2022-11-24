package com.wsi.surianodimuro.pantallas.juego;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wsi.surianodimuro.enumeradores.Infectados;
import com.wsi.surianodimuro.enumeradores.Monstruos;
import com.wsi.surianodimuro.enumeradores.Ninios;
import com.wsi.surianodimuro.interfaces.ProcesamientoEntradas;
import com.wsi.surianodimuro.interfaces.ProcesosJugabilidad;
import com.wsi.surianodimuro.objetos.Ascensor;
import com.wsi.surianodimuro.objetos.PuertaSpawn;
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

public abstract class PantallaOleadas extends Pantalla implements ProcesosJugabilidad {
	
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
		
		Globales.mejorarEstadisticasListener = this;
		Globales.aumentarDificultadListener = this;
		Globales.actividadInfectadosListener = this;
		Globales.actividadProyectilesListener = this;
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
		return false;
	}

	@Override
	public Ascensor chequearUbicacionEnAscensor() {
		return null;
	}

	@Override
	public void procesarMovimientoVertical(Ascensor ascensorOrigen) {
	}

	@Override
	public void chequearColisionProyectiles() {
	}

	@Override
	public void chequearProyectilesImpactados() {
	}

	@Override
	public void procesarTrayectoriaProyectiles() {
	}

	@Override
	public void detectarEscapes() {
	}

	@Override
	public void detectarInfecciones() {
	}

	@Override
	public void chequearVidaInfectados() {
	}
}
