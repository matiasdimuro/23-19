package com.wsi.surianodimuro.pantallas.juego.hud;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.wsi.surianodimuro.pantallas.juego.hud.elementos.IndicadorVidas;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;

public final class HudMultiJug extends Hud {
	
	private IndicadorVidas indicadorVidasJugUno;
	private IndicadorVidas indicadorVidasJugDos;
	
	public HudMultiJug(MapObjects elemsHud) {
		
		super(elemsHud);
		
		int numAgenteUno = 0;
		int numAgenteDos = 0;
		
		if (Globales.cliente.numCliente == 1) {
			numAgenteUno = 0;
			numAgenteDos = 1;
		} else {
			numAgenteDos = 0;
			numAgenteUno = 1;
		}
		
		MapProperties propsIndVidasUno = elemsHud.get("emoji_pj1").getProperties();
		indicadorVidasJugUno = new IndicadorVidas(Float.parseFloat(propsIndVidasUno.get("x").toString()),
				ConfigGraficos.ALTO_MAPA - Float.parseFloat(propsIndVidasUno.get("y").toString()),
				Float.parseFloat(propsIndVidasUno.get("ancho").toString()),
				Float.parseFloat(propsIndVidasUno.get("alto").toString()), numAgenteUno);
		
		MapProperties propsIndVidasDos = elemsHud.get("emoji_pj2").getProperties();
		indicadorVidasJugDos = new IndicadorVidas(Float.parseFloat(propsIndVidasDos.get("x").toString()),
				ConfigGraficos.ALTO_MAPA - Float.parseFloat(propsIndVidasDos.get("y").toString()),
				Float.parseFloat(propsIndVidasDos.get("ancho").toString()),
				Float.parseFloat(propsIndVidasDos.get("alto").toString()), numAgenteDos);
	}
	
	public void renderizar() {
		super.renderizar();
		indicadorVidasJugUno.renderizar();
		indicadorVidasJugDos.renderizar();
	}

	public void liberarMemoria() {
		super.liberarMemoria();
		indicadorVidasJugUno.liberarMemoria();
		indicadorVidasJugDos.liberarMemoria();
	}

	public IndicadorVidas getIndicadorVidasJugUno() {
		return indicadorVidasJugUno;
	}
	
	public IndicadorVidas getIndicadorVidasJugDos() {
		return indicadorVidasJugDos;
	}
}
