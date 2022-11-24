package com.wsi.surianodimuro.interfaces;

public interface ProcesosJugabilidad extends ActividadInfectadosListener, ActividadProyectilesListener {

	/* ActividadProyectilesListener */
	
	@Override
	void chequearColisionProyectiles();

	@Override
	void chequearProyectilesImpactados();

	@Override
	void procesarTrayectoriaProyectiles();

	/* ActividadInfectadosListener */
	
	@Override
	void detectarEscapes();

	@Override
	void spawnearInfectado();

	@Override
	void detectarInfecciones();

	@Override
	void chequearVidaInfectados();

	@Override
	void chequearInfectadosEnMapa();
}
