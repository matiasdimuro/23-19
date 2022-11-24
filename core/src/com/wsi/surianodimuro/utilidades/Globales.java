package com.wsi.surianodimuro.utilidades;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.wsi.surianodimuro.interfaces.ActividadInfectadosListener;
import com.wsi.surianodimuro.interfaces.ActividadProyectilesListener;
import com.wsi.surianodimuro.interfaces.AumentarDificultadListener;
import com.wsi.surianodimuro.interfaces.MejorarEstadisticasListener;
import com.wsi.surianodimuro.interfaces.MovimientoAgenteListener;
import com.wsi.surianodimuro.pantallas.juego.DatosPartida;
import com.wsi.surianodimuro.pantallas.juego.OleadaInfo;
import com.wsi.surianodimuro.pantallas.juego.Sonidos;
import com.wsi.surianodimuro.pantallas.juego.menus.MenuJuegoTerminado;
import com.wsi.surianodimuro.pantallas.juego.menus.MenuSuspension;
import com.wsi.surianodimuro.personajes.Infectado;
import com.wsi.surianodimuro.personajes.agentes.Agente;
import com.wsi.surianodimuro.personajes.agentes.armamento.proyectiles.ProyectilDisparado;
import com.wsi.surianodimuro.redes.Cliente;
import com.wsi.surianodimuro.redes.RedListener;

public abstract class Globales {

	public static Game juego;
	
	/* Modo de un jugador */
	public static MejorarEstadisticasListener mejorarEstadisticasListener;
	public static AumentarDificultadListener aumentarDificultadListener;
	public static ActividadInfectadosListener actividadInfectadosListener;
	public static ActividadProyectilesListener actividadProyectilesListener;
	public static MovimientoAgenteListener movimientoAgenteListener;
	
	public static Cliente cliente;
	public static RedListener redListener;
	
	public static ArrayList<Agente> jugadores;

	public static Sonidos sonidos;
	public static Music soundtrack;
	
	public static Texto cajaMensajes;
	public static OleadaInfo oleadaInfo;
	public static DatosPartida datosPartida;
	public static ArrayList<Infectado> infectados;
	public static ArrayList<ProyectilDisparado> proyectilesDisparados;
	
	public static MenuJuegoTerminado menuJuegoTerminado;
	public static MenuSuspension menuSuspension;
}
