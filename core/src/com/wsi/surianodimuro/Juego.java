package com.wsi.surianodimuro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.wsi.surianodimuro.pantallas.menu.PantallaMenuOpciones;
import com.wsi.surianodimuro.redes.Cliente;
import com.wsi.surianodimuro.utilidades.Globales;

public class Juego extends Game {
	
	@Override
	public void create () {
		
		Globales.juego = this;
		
		Globales.soundtrack = Gdx.audio.newMusic(Gdx.files.internal("sonidos/main-theme.mp3"));
		Globales.soundtrack.setVolume(0.3f);
		Globales.soundtrack.play();
		
		Globales.cliente = new Cliente();
		
		setScreen(new PantallaMenuOpciones());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		Globales.soundtrack.dispose();
		Globales.cliente.dispose();
	}
}
