package com.wsi.surianodimuro.pantallas.juego.hud.elementos;

import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Utiles;

public final class InventarioArmamento extends Inventario {

	public InventarioArmamento(float x, float y, float ANCHO, float ALTO) {

		super("hud/inventarioArmamento.png", x, y, ANCHO, ALTO, Globales.jugadores.get(0).getArmamento().length);
		elementos[0] = Globales.jugadores.get(0).getArmamento()[0];
		elementos[1] = Globales.jugadores.get(0).getArmamento()[1];
	}
	
	@Override
	public void renderizar() {
		
		super.renderizar();
		
		int numAgente = Globales.cliente.numCliente - 1;
		seleccion.setPosition(elementos[Globales.jugadores.get(numAgente).armaEnUso].getPosicion().x, elementos[Globales.jugadores.get(numAgente).armaEnUso].getPosicion().y - 7);
		seleccion.draw(Utiles.batch);
	}
}
