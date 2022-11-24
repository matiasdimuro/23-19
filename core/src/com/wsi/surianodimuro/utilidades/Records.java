package com.wsi.surianodimuro.utilidades;

import java.io.Serializable;

public class Records implements Serializable {

	/* ID DEFAULT, generada de manera automatica */
	private static final long serialVersionUID = 1L;

	private int recordsMaximos = 10;

	private long[] records;
	
//	private long posibleRecord;

	public Records() {
		records = new long[recordsMaximos];
	}
	
	
	/* creo una tabla de records en blanco*/
	public void init() {
		for (int i = 0; i < recordsMaximos; i++) {
			records[i] = 0;
		}
	}
	
	/*geter para el array de records*/
	public long[] getRecords() { return records; }
	
//	/*geter y seter para el posible record a verificar*/
//	public long getPosibleRecord() { return posibleRecord; }
//	public void setPosibleRecord(long posibleRecord) {this.posibleRecord = posibleRecord;}
	
	/*para saber si entro al top 10*/
	public boolean isRecord(long posibleRecord) {
		return posibleRecord > records[recordsMaximos - 1];
	}
	
	/*Paso un posible record para verificarlo y a�adirlo*/
	public void aniadirRecord(long nuevoRecord) {
		if(isRecord(nuevoRecord)) {
			records[recordsMaximos - 1] = nuevoRecord;
			ordenarRecords();
		}
	}

	/* ordanamiento de la tabla de records*/
	private void ordenarRecords() {
		for (int i = 0; i < recordsMaximos; i++) {
			for (int j = 0; j < recordsMaximos - 1; j++) {
				long recordActual = records[j];
				long recordSiguiente = records[j+1];
//				System.out.println("Record Actual: Posicion - " + j + "Record - " + recordActual);
//				System.out.println("Record Siguiente: Posicion - " + (j+1) + "Record - " + recordSiguiente);
				if(recordSiguiente > recordActual) {
					records[j] = recordSiguiente;
					records[j+1] = recordActual;
				}
			}
		}
	}
	
}
