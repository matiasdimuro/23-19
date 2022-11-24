package com.wsi.surianodimuro.pantallas.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.wsi.surianodimuro.pantallas.Pantalla;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Helpers;
import com.wsi.surianodimuro.utilidades.Imagen;
import com.wsi.surianodimuro.utilidades.Utiles;

public class PantallaMenuOpciones extends Pantalla {

	private Imagen fondo;

	private Boton btnJugar;
	private Boton btnControles;
	private Boton btnAcercaDe;
	private Boton btnSalir;
	private Boton btnRecord;

	Boton[] botones;

	int numBotonSelecc = 0;

	private String[] rutasWallapers = { "wallpapers/2.png", "wallpapers/3.png", "wallpapers/4.png", "wallpapers/5.png",
			"wallpapers/6.png", "wallpapers/7.png", "wallpapers/9.png" };

	@Override
	public void show() {

		super.show();

		fondo = new Imagen(rutasWallapers[Utiles.rand.nextInt(rutasWallapers.length)], 0, 0,
				ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA);

		btnRecord = new Boton("botones/btnRecord.png", "botones/btnRecordHover.png", 1820,
				ConfigGraficos.ALTO_PANTALLA - 75 - 20, 75, 75);

		final float ANCHO_BOTON = 300;
		final float ALTO_BOTON = 75;
		final float ESPACIADO = 30;

		final int CANT_BOTONES = 4;
		final int CANT_FILAS = 2;

		final float ANCHO_TOTAL = ANCHO_BOTON * CANT_BOTONES + ESPACIADO * (CANT_BOTONES / CANT_FILAS) - 1;

		float x = ConfigGraficos.ANCHO_PANTALLA / 2 - ANCHO_TOTAL / 2;
		float y = 100;

		btnJugar = new Boton("botones/menu/btnJugar.png", "botones/menu/btnJugarHover.png", 0, 0, ANCHO_BOTON,
				ALTO_BOTON);
		btnJugar.setPosicion(x, y);

		btnControles = new Boton("botones/menu/btnControles.png", "botones/menu/btnControlesHover.png", 0, 0,
				ANCHO_BOTON, ALTO_BOTON);
		btnControles.setPosicion(x + ANCHO_BOTON + ESPACIADO, y);

		btnAcercaDe = new Boton("botones/menu/btnAcercaDe.png", "botones/menu/btnAcercaDeHover.png", 0, 0, ANCHO_BOTON,
				ALTO_BOTON);
		btnAcercaDe.setPosicion(x + ANCHO_BOTON * 2 + ESPACIADO * 2, y);

		btnSalir = new Boton("botones/menu/btnSalir.png", "botones/menu/btnSalirHover.png", 0, 0, ANCHO_BOTON,
				ALTO_BOTON);
		btnSalir.setPosicion(x + ANCHO_BOTON * 3 + ESPACIADO * 3, y);

		botones = new Boton[CANT_BOTONES];
		botones[0] = btnJugar;
		botones[1] = btnControles;
		botones[2] = btnAcercaDe;
		botones[3] = btnSalir;

	}

	@Override
	public void render(float delta) {

		super.render(delta);
		Utiles.batch.begin();

		for (int i = 0; i < botones.length; i++) {
			if (i == numBotonSelecc) {
				botones[i].isHover = true;
			} else {
				botones[i].isHover = false;
			}
		}

		fondo.renderizar();
		btnJugar.renderizar();
		btnControles.renderizar();
		btnAcercaDe.renderizar();
		btnSalir.renderizar();
		btnRecord.renderizar();

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
		btnRecord.liberarMemoria();
	}

	@Override
	public void procesarEntrada() {

		super.procesarEntrada();

		if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			if (numBotonSelecc > 0) {
				numBotonSelecc--;
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			if (numBotonSelecc < botones.length - 1) {
				numBotonSelecc++;
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			if (numBotonSelecc == 2) {
				numBotonSelecc = 0;
			} else if (numBotonSelecc == 3) {
				numBotonSelecc = 1;
			}
		} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			if (numBotonSelecc == 0) {
				numBotonSelecc = 2;
			} else if (numBotonSelecc == 1) {
				numBotonSelecc = 3;
			}
		}

		int i = 0;
		boolean seleccionado = false;
		do {
			if ((Gdx.input.getX() >= botones[i].getPosicion().x)
					&& (Gdx.input.getX() <= botones[i].getPosicion().x + botones[i].getDimensiones()[0])
					&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() >= botones[i].getPosicion().y)
					&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() <= botones[i].getPosicion().y
							+ botones[i].getDimensiones()[1])) {
				seleccionado = true;
				numBotonSelecc = i;
			}
		} while ((!seleccionado) && (++i < botones.length));

		if ((Gdx.input.isKeyJustPressed(Keys.ENTER)) || (seleccionado && Gdx.input.isTouched())) {
			btnJugar.botonPlay();
			switch (numBotonSelecc) {
			case 0:
				Globales.juego.setScreen(new PantallaMenuJugar());
				break;
			case 1:
				Globales.juego.setScreen(new PantallaMenuControles());
				break;
			case 2:
				Globales.juego.setScreen(new PantallaMenuAcercaDe());
				break;
			case 3:
				Helpers.cerrarJuego();
				break;

			default:
				break;
			}
		}

		boolean record = false;
		if ((Gdx.input.getX() >= btnRecord.getPosicion().x)
				&& (Gdx.input.getX() <= btnRecord.getPosicion().x + btnRecord.getDimensiones()[0])
				&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() >= btnRecord.getPosicion().y)
				&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() <= btnRecord.getPosicion().y
						+ btnRecord.getDimensiones()[1])) {
			btnRecord.isHover = true;
			record = true;
		} else {
			btnRecord.isHover = false;
		}
		if ((record) && ((Gdx.input.isTouched()))) {
			Globales.juego.setScreen(new PantallaRecord());
		}
	}
}
