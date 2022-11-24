package com.wsi.surianodimuro.utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wsi.surianodimuro.interfaces.Dimensionable;

public class Boton implements Dimensionable {

	private Imagen estiloDefault;
	private Imagen estiloHover;
	
	private Vector2 posicion;
	private float[] dimensiones;
	public boolean isHover;
	
	private Sound boton;
	
	/**
	 * Elemento de dos sprites para los estados del boton "default" y "hover"
	 * @param rutaDefault
	 * @param rutaHover
	 * @param x
	 * @param y
	 * @param ancho
	 * @param alto
	 */
	
	public Boton(String rutaDefault, String rutaHover, float x, float y, float ancho, float alto) {
	
		estiloDefault = new Imagen(rutaDefault, x, y, ancho, alto);
		estiloHover = new Imagen(rutaHover, x, y, ancho, alto);
		
		isHover = false;
		
		posicion = new Vector2();
		posicion.x = estiloDefault.getPosicion().x;
		posicion.y = estiloDefault.getPosicion().y;
		
		dimensiones = new float[2];
		dimensiones[0] = ancho;
		dimensiones[1] = alto;
	}

	@Override
	public void renderizar() {
		
		if (!isHover) estiloDefault.renderizar(); 
		else estiloHover.renderizar();
	}

	@Override
	public void liberarMemoria() {
		
		estiloDefault.liberarMemoria();
		estiloHover.liberarMemoria();
		boton.dispose();
	}

	@Override
	public float[] getDimensiones() {
		
		return dimensiones;
	}

	@Override
	public Vector2 getPosicion() {
		
		return posicion; 
	}

	@Override
	public Rectangle getRectangulo() {
		
		return new Rectangle(posicion.x, posicion.y, dimensiones[0], dimensiones[1]);
	}

	@Override
	public void setPosicion(float x, float y) {
		
		posicion.x = x;
		posicion.y = y;
		
		estiloDefault.setPosicion(x, y);
		estiloHover.setPosicion(x, y);
	}
	
	public void botonPlay() {
		boton = Gdx.audio.newSound(Gdx.files.internal("sonidos/tocarBoton.mp3"));
		boton.play();
	}

}
