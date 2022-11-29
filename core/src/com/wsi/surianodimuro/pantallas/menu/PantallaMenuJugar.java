package com.wsi.surianodimuro.pantallas.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.pantallas.Pantalla;
import com.wsi.surianodimuro.pantallas.juego.PantallaOleadasUnJug;
import com.wsi.surianodimuro.redes.Servidor;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Imagen;
import com.wsi.surianodimuro.utilidades.Utiles;

public class PantallaMenuJugar extends Pantalla {

	private Imagen fondo;
	
	private Boton btnVolver;
	private Boton btnUnJug;
	private Boton btnMultiJug;
	private Boton[] botones;
	
	int numBotonSelecc = 1;
	
	@Override
	public void show() {
		
		super.show();
		
		fondo = new Imagen("wallpapers/8.png", 0, 0, ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA);
		btnVolver = new Boton("botones/btnSalir.png", "botones/btnSalirHover.png", 20, ConfigGraficos.ALTO_PANTALLA - 75 - 20, 75, 75);
		
		final float ANCHO_BOTON = 400;
		final float ALTO_BOTON = 100;
		final float ESPACIADO = 75;
		
		final float X = ConfigGraficos.ANCHO_PANTALLA / 2 - (ANCHO_BOTON * 2 + ESPACIADO) / 2; 
		final float Y = 130;
		
		btnUnJug = new Boton("botones/menu/btnUnJug.png", "botones/menu/btnUnJugHover.png", 0, 0, ANCHO_BOTON, ALTO_BOTON);
		btnUnJug.setPosicion(X, Y);
		
		btnMultiJug = new Boton("botones/menu/btnMultiJug.png", "botones/menu/btnMultiJugHover.png", 0, 0, ANCHO_BOTON, ALTO_BOTON);
		btnMultiJug.setPosicion(X + ESPACIADO + ANCHO_BOTON, Y);
		
		botones = new Boton[3];
		botones[0] = btnVolver;
		botones[1] = btnUnJug;
		botones[2] = btnMultiJug;
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		for (int i = 1; i < botones.length; i++) {
			if (i == numBotonSelecc) { botones[i].isHover = true; }
			else { botones[i].isHover = false; }
		}
		
		Utiles.batch.begin();
		
		fondo.renderizar();
		btnVolver.renderizar();
		btnUnJug.renderizar();
		btnMultiJug.renderizar();
		
		Utiles.batch.end();
		
		procesarEntrada();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		fondo.liberarMemoria();
		for (Boton boton : botones) {
			boton.liberarMemoria();
		}
	}

	
	@Override
	public void procesarEntrada() {

		super.procesarEntrada();
		
		if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			if (numBotonSelecc > 1) {
				numBotonSelecc--;
			}
		}
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			if (numBotonSelecc < botones.length - 1) {
				numBotonSelecc++;
			}
		}
		
		int i = 1;
		boolean seleccionado = false;
		do {
			if (
					(Gdx.input.getX() >= botones[i].getPosicion().x) &&
					(Gdx.input.getX() <= botones[i].getPosicion().x + botones[i].getDimensiones()[0]) &&
					(ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() >= botones[i].getPosicion().y) &&
					(ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() <= botones[i].getPosicion().y + botones[i].getDimensiones()[1])
			) {
				seleccionado = true;
				numBotonSelecc = i;
			}
		} while ((!seleccionado) && (++i < botones.length));
		
		if ((Gdx.input.isKeyJustPressed(Keys.ENTER)) || (seleccionado && Gdx.input.isButtonJustPressed(Buttons.LEFT))) {

			switch (numBotonSelecc) {
			case 1:
				Globales.cliente.offline = true;
				Globales.soundtrack.pause();
				Globales.juego.setScreen(new PantallaOleadasUnJug());
				break;
			case 2:
				if (Globales.cliente.offline) {
					Globales.cliente.offline = false;
				}
				if (!Globales.cliente.iniciado) {
					Globales.cliente.start();
				}
				if (!Servidor.iniciado) {
					Servidor.cargarServidor();
				}
				if ((Globales.cliente.iniciado) && (Servidor.iniciado)) {
					Globales.cliente.conectarseAlServidor();
				}
				
				if ((!Servidor.online) && (!Globales.cliente.conectadoAlServidor) && (Globales.cliente.iniciado) && (!Globales.cliente.offline)) {
					Globales.juego.setScreen(new PantallaEsperandoServidor());
				}
				break;
			default:
				break;
			}
		}

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
		if ((volver) && ((Gdx.input.isTouched())) || (Gdx.input.isKeyPressed(Keys.ESCAPE))) {
			Globales.juego.setScreen(new PantallaMenuOpciones());
		}
		
	}
}
