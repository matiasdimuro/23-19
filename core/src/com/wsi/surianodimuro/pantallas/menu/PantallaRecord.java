package com.wsi.surianodimuro.pantallas.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.wsi.surianodimuro.pantallas.Pantalla;
import com.wsi.surianodimuro.utilidades.Boton;
import com.wsi.surianodimuro.utilidades.ConfigGraficos;
import com.wsi.surianodimuro.utilidades.Globales;
import com.wsi.surianodimuro.utilidades.Texto;
import com.wsi.surianodimuro.utilidades.Utiles;
import com.wsi.surianodimuro.utilidades.GuardarRecords;

public class PantallaRecord extends Pantalla {

	private Boton btnVolver;

	private Texto tituloRecords;
	private String rcrd;
	
	private Stage recordStage;
	private Label rLabel;
	private Table recordsTable;
	private Skin skin;

	private long[] records;

	public void show() {
		super.show();
		btnVolver = new Boton("botones/btnSalir.png", "botones/btnSalirHover.png", 20,
				ConfigGraficos.ALTO_PANTALLA - 75 - 20, 75, 75);
		tituloRecords = new Texto("fuentes/8-BITWONDER.TTF", "RECORDS", 60, Color.WHITE);
		tituloRecords.setPosicion(((ConfigGraficos.ANCHO_PANTALLA / 2) - (tituloRecords.getDimensiones()[0] / 2)),
				ConfigGraficos.ALTO_PANTALLA - 30);
		/*Cargo mi .sav para mostrar los records*/
		GuardarRecords.cargar();
		/*Guardo los records en mi array*/
		records = GuardarRecords.rcrd.getRecords();
		
		/*creo una skin para los labels de mi tabla, la skin utiliza un .json para cargar los presets default que cree*/
		skin = new Skin(Gdx.files.internal("skin/skinStyle.json"));
		/*Creo mi tabla y le asigno limites*/
		recordsTable = new Table();
		recordsTable.setBounds(0, 0, ConfigGraficos.ANCHO_PANTALLA, ConfigGraficos.ALTO_PANTALLA-20);
		/*Un debug de la tabla por si fuera necesario*/
		//recordsTable.debug();
		
		/*Creo un stage para mostrar mi tabla*/
		recordStage = new Stage();
		
		/*cargo los records a un label y cargo ese label a mi tabla, haciendo una linea por cada label que cargo*/
		for (int i = 0; i < records.length; i++) {
			System.out.println(records[i]);
			rcrd = String.format("%s) %s ptos.", i+1, records[i]);
			rLabel = new Label(rcrd, skin);
			recordsTable.add(rLabel);
			recordsTable.row();
			recordsTable.center();
		}
		
		/*agrego la tabla a mi stage*/
		recordStage.addActor(recordsTable);
		
	}

	public void render(float delta) {

		super.render(delta);

		Utiles.batch.begin();

		tituloRecords.setTexto("RECORDS");
		tituloRecords.renderizar();
		
		/*Muestro la tabla*/
		recordStage.act(delta);
		recordStage.draw();
		
		btnVolver.renderizar();
		Utiles.batch.end();

		procesarEntrada();

	}

	public void procesarEntrada() {

		super.procesarEntrada();

		boolean volver = false;
		if ((Gdx.input.getX() >= btnVolver.getPosicion().x)
				&& (Gdx.input.getX() <= btnVolver.getPosicion().x + btnVolver.getDimensiones()[0])
				&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() >= btnVolver.getPosicion().y)
				&& (ConfigGraficos.ALTO_PANTALLA - Gdx.input.getY() <= btnVolver.getPosicion().y
						+ btnVolver.getDimensiones()[1])) {
			btnVolver.isHover = true;
			volver = true;
		} else {
			btnVolver.isHover = false;
		}
		if ((volver) && ((Gdx.input.isTouched())) || (Gdx.input.isKeyPressed(Keys.ESCAPE))) {
			Globales.juego.setScreen(new PantallaMenuOpciones());
		}

	}

	public void dispose() {
		super.dispose();
		btnVolver.liberarMemoria();
		tituloRecords.liberarMemoria();
		recordStage.dispose();
	}
}
