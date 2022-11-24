package com.wsi.surianodimuro.pantallas.juego.menus;

import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;

public final class MenuSuspension extends MenuJuego {
	
	private Boton botonSalir;

	public MenuSuspension() {

		final float ANCHO_BOTON = 400;
		final float ALTO_BOTON = 100;

		botonSalir = new Boton("botones/menu/btnSalir.png", "botones/menu/btnSalirHover.png", 0, 0, ANCHO_BOTON, ALTO_BOTON);
		botonSalir.setPosicion(ConfigGraficos.ANCHO_MAPA / 2 - ANCHO_BOTON / 2, ConfigGraficos.ALTO_MAPA / 2 - ALTO_BOTON / 2);
	}

	@Override
	public void renderizar() {
		super.renderizar();
		botonSalir.renderizar();
	}

	@Override
	public void liberarMemoria() {
		super.liberarMemoria();
		botonSalir.liberarMemoria();
	}
	
	public Boton getBotonSalir() {
		return botonSalir;
	}	
}
