package com.wsi.surianodimuro.pantallas.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class Sonidos implements Disposable {
	
	private Music musicaDeFondo;
	public boolean musicaDeFondoSonando = false;
	
	public void sonarMusicaDeFondo() {
		musicaDeFondo = Gdx.audio.newMusic(Gdx.files.internal("sonidos/scare-floor.mp3"));
		musicaDeFondo.play();
		musicaDeFondo.setVolume(0.5f);
		musicaDeFondoSonando = true;
	}
	
	public void terminarMusicaDeFondo() {
		musicaDeFondoSonando = false;
		musicaDeFondo.pause();
		musicaDeFondo.dispose();
		musicaDeFondo = null;
	}

	public void pausarMusicaDeFondo() {
		if (musicaDeFondoSonando) {
			musicaDeFondoSonando = false;
			musicaDeFondo.pause();			
		}
	}
	
	public void despausarMusicaDeFondo() {
		if (!musicaDeFondoSonando) {
			musicaDeFondoSonando = true;
			musicaDeFondo.play();
		}
	}
	
	
	
	private Music musicaEntreRonda;
	public boolean musicaEntreRondaSonando = false;
	
	public void sonarMusicaEntreRonda() {
		musicaEntreRonda = Gdx.audio.newMusic(Gdx.files.internal("sonidos/entreRonda.mp3"));
		musicaEntreRonda.play();
		musicaEntreRonda.setVolume(0.5f);
		musicaEntreRondaSonando = true;
	}
	
	public void terminarMusicaEntreRonda() {
		musicaEntreRondaSonando = false;
		musicaEntreRonda.pause();
		musicaEntreRonda.dispose();
		musicaEntreRonda = null;
	}
	
	public void pausarMusicaEntreRonda() {
		if (musicaEntreRondaSonando) {
			musicaEntreRondaSonando = false;
			musicaEntreRonda.pause();			
		}
	}
	
	public void despausarMusicaEntreRonda() {
		if (!musicaEntreRondaSonando) {
			musicaEntreRondaSonando = true;
			musicaEntreRonda.play();			
		}
	}
	
	
	
	private Music alarma;
	public boolean alarmaSonando = false;
	
	public void sonarAlarma() {
		alarma = Gdx.audio.newMusic(Gdx.files.internal("sonidos/alarm-sound.mp3"));
		alarma.play();
		alarmaSonando = true;
	}
	
	public void pausarMusicaAlarma() {
		alarmaSonando = false;
		alarma.pause();
		alarma.dispose();
		alarma = null;
	}
	
	
	
	private Music musicaFinal;
	public boolean musicaFinalSonando = false;
	
	public void sonarMusicaFinal() {
		musicaFinal = Gdx.audio.newMusic(Gdx.files.internal("sonidos/end-music.mp3"));
		musicaFinal.play();
		musicaFinal.setVolume(0.5f);
		musicaFinalSonando = true;
	}
	
	public void pausarMusicaFinal() {
		musicaFinalSonando = false;
		musicaFinal.pause();
		musicaFinal.dispose();
		musicaFinal = null;
	}

	private Sound boo2000;

	public void sonarCambioBoo2000() {
		boo2000 = Gdx.audio.newSound(Gdx.files.internal("sonidos/boo2000.mp3"));
		boo2000.play(0.25f);
	}

	
	
	private Sound desintox;

	public void sonarCambioDesintox() {
		desintox = Gdx.audio.newSound(Gdx.files.internal("sonidos/desintox.mp3"));
		desintox.play(0.25f);
	}

	
	
	private Sound ascensor;

	public void sonarAscensor() {
		ascensor = Gdx.audio.newSound(Gdx.files.internal("sonidos/ascensor.mp3"));
		ascensor.play();
	}

	
	
	private Sound escapeMonstruo;

	public void sonarEscapeMonstruo() {
		escapeMonstruo = Gdx.audio.newSound(Gdx.files.internal("sonidos/escapeMonstruo.mp3"));
		escapeMonstruo.play();
	}

	
	
	private Sound escapeNinio;

	public void sonarEscapeNinio() {
		escapeNinio = Gdx.audio.newSound(Gdx.files.internal("sonidos/escapeNinios.mp3"));
		escapeNinio.play();
	}

	
	
	@Override
	public void dispose() {
		if (musicaEntreRondaSonando) { musicaEntreRonda.dispose();	}
		if (musicaDeFondoSonando) { musicaDeFondo.dispose(); }
		if (musicaFinalSonando) { musicaFinal.dispose(); }
		if (alarmaSonando) { alarma.dispose(); }
		boo2000.dispose();
		desintox.dispose();
		ascensor.dispose();
		escapeNinio.dispose();
		escapeMonstruo.dispose();
	}
}
