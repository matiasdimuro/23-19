
package com.wsi.surianodimuro.pantallas.juego.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Imagen;
import com.wsi.surianodimuro.utilidades.Texto;

public class MenuJuegoTerminado extends MenuJuego {

	private Imagen contenedor;
	
	private Texto encabezado;
	
	private Texto resOleada;
	private Texto resSupervivencia;
	private Texto resPuntos;
	
	private Boton btnMenu;
	private Boton btnSalir;
	
	private Rectangle areaSuperior;
	private Rectangle areaSuperiorA;
	private Rectangle areaSuperiorB;
	
	private Rectangle areaInferior;
	
	public int opc;
	public boolean terminado;
	
	public MenuJuegoTerminado() {
		
		opc = 1;
		terminado = false;
		
		final int ANCHO_CONT = 700;
		final int ALTO_CONT = 900;
		
		float posX = ConfigGraficos.ANCHO_PANTALLA / 2 - ANCHO_CONT / 2;
		float posY = ConfigGraficos.ALTO_PANTALLA / 2 - ALTO_CONT / 2;
		
		contenedor = new Imagen("menus/juegoTerminadoMenuContainer.png", posX, posY, ANCHO_CONT, ALTO_CONT);
		contenedor.setPosicion(posX, posY);
		
		areaSuperior = new Rectangle(posX, (posY + ALTO_CONT / 2), ANCHO_CONT, (ALTO_CONT / 2));
		areaSuperiorA = new Rectangle(posX, (areaSuperior.getY() + areaSuperior.getHeight() * 0.60f), ANCHO_CONT, (areaSuperior.getHeight() * 0.40f));
		areaSuperiorB = new Rectangle(posX, areaSuperior.getY(), ANCHO_CONT, (areaSuperior.getHeight() * 0.60f));
		areaInferior = new Rectangle(posX, posY, ANCHO_CONT, (ALTO_CONT / 2));
		
		encabezado = new Texto("fuentes/8-BITWONDER.TTF", "JUEGO TERMINADO", 35, Color.WHITE);
		posX = areaSuperior.getX() + areaSuperior.getWidth() / 2 - encabezado.getDimensiones()[0] / 2;
		posY = areaSuperiorA.getY() + areaSuperiorA.getHeight() / 2 + encabezado.getDimensiones()[1] / 2;
		encabezado.setPosicion(posX, posY);
		
		float espacio = 0;
		final int ESPACIO_ENTRE_TEXTOS = 30;
		
		resOleada = new Texto("fuentes/PixelOperator-Bold.ttf", "Oleada alcanzada: " + Globales.oleadaInfo.numOleada, 30, Color.WHITE);
		posX = areaSuperior.getX() + areaSuperior.getWidth() / 2 - resOleada.getDimensiones()[0] / 2;
		espacio = (areaSuperiorB.getHeight() - resOleada.getDimensiones()[1] * 3 - ESPACIO_ENTRE_TEXTOS * 2) / 2;
		posY = areaSuperiorB.getY() + areaSuperiorB.getHeight() - espacio - resOleada.getDimensiones()[1] / 2;
		resOleada.setPosicion(posX, posY);
		
		resSupervivencia = new Texto("fuentes/PixelOperator-Bold.ttf", "Tpo Supervivencia: " + (int) Globales.datosPartida.tiempoSupervivencia + " seg.", 30, Color.WHITE);
		posX = areaSuperior.getX() + areaSuperior.getWidth() / 2 - resSupervivencia.getDimensiones()[0] / 2;
		posY = areaSuperiorB.getY() + areaSuperiorB.getHeight() - espacio - (resOleada.getDimensiones()[1] * 2) / 2 - ESPACIO_ENTRE_TEXTOS;
		resSupervivencia.setPosicion(posX, posY);
		
		resPuntos = new Texto("fuentes/PixelOperator-Bold.ttf", "Puntaje total: " + Globales.datosPartida.puntajeTotal, 30, Color.WHITE);
		posX = areaSuperior.getX() + areaSuperior.getWidth() / 2 - resPuntos.getDimensiones()[0] / 2;
		posY = areaSuperiorB.getY() + areaSuperiorB.getHeight() - espacio - (resOleada.getDimensiones()[1] * 3) / 2 - ESPACIO_ENTRE_TEXTOS * 2;
		resPuntos.setPosicion(posX, posY);
	
		final int ANCHO_BOTON = 400;
		final int ALTO_BOTON = 100;
		final int ESPACIO_ENTRE_BOTONES = 20;
		
		espacio = (areaInferior.getHeight() - ALTO_BOTON * 2 - ESPACIO_ENTRE_BOTONES) / 2;
		posY = areaInferior.getY() + areaInferior.getHeight() - espacio - ALTO_BOTON;
		posX = areaSuperior.getX() + areaSuperior.getWidth() / 2 - ANCHO_BOTON / 2; 
		
		btnMenu = new Boton("botones/menu/btnMenu.png", "botones/menu/btnMenuHover.png", posX, posY, ANCHO_BOTON, ALTO_BOTON);
		
		posY = areaInferior.getY() + areaInferior.getHeight() - espacio - ALTO_BOTON * 2 - ESPACIO_ENTRE_BOTONES;
		btnSalir = new Boton("botones/menu/btnSalir.png", "botones/menu/btnSalirHover.png", posX, posY, ANCHO_BOTON, ALTO_BOTON);		
	}

	@Override
	public void renderizar() {
		super.renderizar();
		contenedor.renderizar();
		if (opc == 1) { 
			btnMenu.isHover = true; 
			btnSalir.isHover = false; 
		}
		else { 
			btnMenu.isHover = false; 
			btnSalir.isHover = true; 
		}
		btnMenu.renderizar();			
		btnSalir.renderizar();
		encabezado.renderizar();
		resOleada.renderizar();
		resOleada.setTexto("Oleada alcanzada: " + Globales.oleadaInfo.numOleada);
		resPuntos.renderizar();
		resPuntos.setTexto("Puntaje total: " + Globales.datosPartida.puntajeTotal);
		resSupervivencia.renderizar();
		resSupervivencia.setTexto("Tpo Supervivencia: " + String.format("%.2f", Globales.datosPartida.tiempoSupervivencia / 60f) + " min.");
	}

	@Override
	public void liberarMemoria() {
		super.liberarMemoria();
		btnMenu.liberarMemoria();
		encabezado.liberarMemoria();
		contenedor.liberarMemoria();
		btnSalir.liberarMemoria();
		resOleada.liberarMemoria();
		resPuntos.liberarMemoria();
		resSupervivencia.liberarMemoria();
	}
	
	public Boton getBtnMenu() {
		return btnMenu;
	}
	
	public Boton getBtnSalir() {
		return btnSalir;
	}
}
