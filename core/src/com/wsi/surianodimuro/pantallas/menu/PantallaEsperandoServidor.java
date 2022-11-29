package com.wsi.surianodimuro.pantallas.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.pantallas.Pantalla;
import com.wsi.surianodimuro.pantallas.juego.PantallaOleadasMultiJug;
import com.wsi.surianodimuro.redes.InfoRed;
import com.wsi.surianodimuro.redes.Servidor;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Imagen;
import com.wsi.surianodimuro.utilidades.Utiles;
	

public class PantallaEsperandoServidor extends Pantalla {
	
	private Imagen fondo;
	private Boton btnVolver;
	
	
	public void show() {
		super.show();
		fondo = new Imagen("backgrounds/esperando-servidor.jpeg", 0, 0, ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA);
		btnVolver = new Boton("botones/btnSalir.png", "botones/btnSalirHover.png", 20, ConfigGraficos.ALTO_PANTALLA - 75 - 20, 75, 75);
	}
	
	public void render(float delta) {
		
		super.render(delta);
		
		Utiles.batch.begin();
		fondo.renderizar();
		btnVolver.renderizar();
		Utiles.batch.end();
		
		if (!Servidor.online) {
			Globales.juego.setScreen(new PantallaServidorDesconectado());
		}

		if (InfoRed.conexionGlobalEstablecida) {
			Globales.juego.setScreen(new PantallaOleadasMultiJug());
			Globales.soundtrack.pause();
		}

		procesarEntrada();
	}
	
	public void dispose() {
		super.dispose();
		fondo.liberarMemoria();
		btnVolver.liberarMemoria();
	}
	
	public void procesarEntrada() {

		super.procesarEntrada();

		boolean volver = false;
		if (
				(Gdx.input.getX() >= btnVolver.getPosicion().x) &&
				(Gdx.input.getX() <= btnVolver.getPosicion().x + btnVolver.getDimensiones()[0]) &&
				(ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() >= btnVolver.getPosicion().y) &&
				(ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() <= btnVolver.getPosicion().y + btnVolver.getDimensiones()[1])
		) {
			btnVolver.isHover = true;
			volver = true;
		}
		else {
			btnVolver.isHover = false;
		}
		if ((volver) && ((Gdx.input.isButtonJustPressed(Buttons.LEFT))) || (Gdx.input.isKeyPressed(Keys.ESCAPE))) {
			Globales.juego.setScreen(new PantallaMenuJugar());
		}
	}

}
