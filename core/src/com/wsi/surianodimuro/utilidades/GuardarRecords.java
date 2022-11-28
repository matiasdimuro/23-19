package com.wsi.surianodimuro.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;

public class GuardarRecords {
	
	public static Records rcrd;
	
	public static void guardar() {	
		try {
			/*Guardo un archivo de records*/
			ObjectOutputStream guardar = new ObjectOutputStream( 
					new FileOutputStream("records.sav"));
//			System.out.println("Guardado realizado...");
			guardar.writeObject(rcrd);
			guardar.close();
		} catch (Exception e) {
			/*No sabia exactamente que errores se podian llegar a generar*/
			/*pero un 90% de los tutoriales que vi recomiendan que ponga un try catch generico por si acaso*/
			e.printStackTrace();
			Gdx.app.exit();
		}
	};
	
	public static void cargar() {
		try {
			/*En caso de no existir un archivo de records lo creo*/
			if(!existeGuardado()) {
				run();
				/*Como el archivo fue recien creado no hay nada que guardar*/
				return;
			}
			/*Si existe un records, lo cargo al programa*/
			ObjectInputStream cargar = new ObjectInputStream(
					new FileInputStream("records.sav"));
			rcrd = (Records) cargar.readObject();
//			System.out.println("Carga realizada...");
			cargar.close();
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	};
	
	private static boolean existeGuardado() {
		File grdr = new File("records.sav");
		return grdr.exists();
	}
	
	public static void run() {
		rcrd = new Records();
		rcrd.init();
		guardar();
	}
	
}
