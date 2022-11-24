package com.wsi.surianodimuro.pantallas.juego.entradas;

import com.badlogic.gdx.Gdx;
import com.wsi.surianodimuro.interfaces.ProcesamientoEntradas;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Helpers;

public final class EntradasPausa implements ProcesamientoEntradas {

	@Override
	public void procesarEntrada() {

		float x = Gdx.input.getX() * ConfigGraficos.COCIENTE_ENTRE_ANCHOS;
		float y = Gdx.input.getY() * ConfigGraficos.COCIENTE_ENTRE_ALTOS;

		Boton btnSalir = Globales.menuSuspension.getBotonSalir();

		if ((x > btnSalir.getPosicion().x) && (x < btnSalir.getPosicion().x + btnSalir.getDimensiones()[0])
				&& (y > btnSalir.getPosicion().y)
				&& (y < btnSalir.getPosicion().y + btnSalir.getDimensiones()[1])) {
			btnSalir.isHover = true;
			if (Gdx.input.isTouched()) {
				Helpers.cerrarJuego();
			}
		}

		else {
			Globales.menuSuspension.getBotonSalir().isHover = false;
		}
	}
}
