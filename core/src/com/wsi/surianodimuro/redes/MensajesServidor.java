package com.wsi.surianodimuro.redes;

public enum MensajesServidor {

	SOLICITUD_ACEPTADA("Conexion aceptada"),
	SOLICITUD_RECHAZADA("No puede conectarse"),
	
	DESCONECTAR_CLIENTE("Desconectar cliente"),
	CERRAR_SERVIDOR("Cerrar servidor"),
	
	MOVER_AGENTE_IZQUIERDA("Mover agente izquierda"),
	MOVER_AGENTE_DERECHA("Mover agente derecha"),
	SUBIR_AGENTE_POR_ASCENSOR("Subir agente por ascensor"),
	BAJAR_AGENTE_POR_ASCENSOR("Bajar agente por ascensor"),
	CAMBIAR_ARMA_AGENTE("Cambiar arma agente"),
	DISPARAR_PROYECTIL("Disparar proyectil agente"),
	DISPARAR_ULTIMATE("Disparar ultimate agente"),
	MANTENER_AGENTE_QUIETO("Mantener agente quieto"),
	PARAR_FUEGO_AGENTE("Parar fuego agente"),
	RESETEAR_ESTADOS_AGENTE("Resetear estados agente"),
	
	SPAWNEAR_INFECTADO("Spawnear infectado"),
	MOVER_INFECTADO_IZQUIERDA("Mover infectado izquierda"),
	MOVER_INFECTADO_DERECHA("Mover infectado derecha"),

	ESCAPE_MONSTRUO("Escape monstruo"),
	ESCAPE_NINIOS("Escape ninio"),
	
	AUMENTAR_VELOCIDAD_SPAWN("Aumentar velocidad spawn"),
	AUMENTAR_DURACION_OLEADA("Aumentar duracion oleada"),
	
	AUMENTAR_VIDA_AGENTE("Aumentar vida agente"),
	ACTUALIZAR_VIDA_AGENTE("Actualizar vida agente"),
	AUMENTAR_DISPARO("Aumentar disparo"),
	AUMENTAR_ALCANCE("Aumentar alcance"),
	AUMENTAR_RAPIDEZ("Aumentar rapidez"),
	
	ACTUALIZAR_CAJA_MENSAJES("Actualizar caja mensajes"),
	
	ACTUALIZAR_INDICADOR_OLEADA("Actualizar indicador oleada"),
	ACTUALIZAR_INDICADOR_GRITO("Actualizar indicador grito"),
	
	TERMINAR_JUEGO("Terminar juego"),
	EMPEZAR_JUEGO("Empezar juego");
	
	private String mensaje;
	
	private MensajesServidor(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
}
