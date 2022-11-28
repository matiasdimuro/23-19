package com.wsi.surianodimuro.enumeradores;

public enum Mensajes {

	FIN_OLEADA("\nLa oleada ha finalizado.\nRecarga fuerzas!"),
	PREVIA_OLEADA("\nUna nueva oleada esta\npor comenzar. Preparate!"),
	COMIENZO_OLEADA("\nVamos!\nDefiende Monstruopilis!"),
	
	INFECTADO("\nHas sido infectado!\nPierdes una vida"),
	ESCAPE_MONSTRUO("\nRayos! Se ha\n\nescapado un monstruo!"),
	ESCAPE_NINIO("\nRayos! Se ha\nescapado un niño!");
	
	private String mensaje;
	
	private Mensajes(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
}