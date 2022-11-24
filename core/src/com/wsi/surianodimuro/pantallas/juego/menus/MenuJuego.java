package com.wsi.surianodimuro.pantallas.juego.menus;

import com.wsi.surianodimuro.interfaces.Renderizable;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Imagen;

public abstract class MenuJuego implements Renderizable {

	private Imagen background;
	
	public MenuJuego() {
		background = new Imagen("backgrounds/black.png", 0, 0, ConfigGraficos.ANCHO_MAPA, ConfigGraficos.ALTO_MAPA);
		background.cambiarAlpha(.5f);
	}

	@Override
	public void renderizar() {
		background.renderizar();
	}

	@Override
	public void liberarMemoria() {
		background.liberarMemoria();
	}
}
