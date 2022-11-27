package com.wsi.surianodimuro.enumeradores;

public enum Mensajes {

	FIN_OLEADA("\n\nLa oleada ha finalizado.\nRecarga fuerzas!"),
	PREVIA_OLEADA("\n\nUna nueva oleada esta\npor comenzar. Preparate!"),
	COMIENZO_OLEADA("\n\nVamos!\n\nDefiende Monstruopilis!"),
	
	INFECTADO("\n\nHas sido infectado!\nPierdes una vida"),
	ESCAPE_MONSTRUO("\n\nRayos! Se ha\n\nescapado un monstruo!"),
	ESCAPE_NINIO("\n\nRayos! Se ha\nescapado un niño!");
	
	private String mensaje;
	
	private Mensajes(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
}