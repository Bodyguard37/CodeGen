package unc.fcefyn.lac.tesisCaroFurey.main;

import java.awt.EventQueue;
import unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.GenerarCodigoJava;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;
import unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.VentanaPrincipal;

/**
 * Clase principal del proyecto, inicializa todas las instancias de una clase.
 * @author María Florencia Caro & Ignacio Furey
 */
public final class Main {	
	/**
	 * Class constructor. Inicializa las instancias de las variables globales
	 * de clase.
	 */
	private Main() {
		//Crea una nueva instancia de VentanaPrincipal,
		//pasa como parametro las instancias de los objetos
		new VentanaPrincipal(new Tina(), new GenerarCodigoJava());
	}	
	/**
	 * Metodo inicializador.
	 * @param args Argumentos de inicio
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				try {
					//Crea una instancia de Main
					final Main program = new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
