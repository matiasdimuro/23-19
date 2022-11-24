package com.wsi.surianodimuro.interfaces;

import com.wsi.surianodimuro.objetos.Ascensor;

public interface ProcesosJugabilidad extends ActividadInfectadosListener, ActividadProyectilesListener, MovimientoAgenteListener {
	
	/* MovimientoAgenteListener */
	
	@Override
	boolean chequearColisiones();

	@Override
	Ascensor chequearUbicacionEnAscensor();

	@Override
	void procesarMovimientoVertical(Ascensor ascensorOrigen);

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
