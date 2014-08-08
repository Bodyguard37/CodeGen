package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos;

import java.util.ArrayList;

/**
 * Clase que representa un proceso, resume toda la informacion de un proceso.
 *@author María Florencia Caro & Ignacio Furey 
 */
public class Proceso {
	/**
	 * id de Proceso.
	 */
	private String id;
	/**
	 * Transiciones del proceso.
	 */
	private ArrayList<TransicionProceso> transiciones;
	/**
	 * Variable booleana. Si es true es un hilo de ejecucion del sistema definido
	 * por el usuario.
	 */
	private boolean runnable;
	/**
	 * Define si un proceso es runnable o no.
	 * @return true si el proceso es runnable, false en caso contrario.
	 */
	public boolean isRunnable() {
		return this.runnable;
	}
	/**
	 * Setter.
	 * @param runnableNuevo .
	 */
	public void setRunnable(final boolean runnableNuevo) {
		this.runnable = runnableNuevo;
	}
	/**
	 * Constructor.
	 * @param idNuevo id del proceso.
	 * @param runnableNuevo informacion de runnable.
	 */
	public Proceso(final String idNuevo, final boolean runnableNuevo) {
		this.id = idNuevo;
		this.runnable = runnableNuevo;
		this.transiciones = new ArrayList<TransicionProceso>();
	}
	/**
	 * Setter.
	 * @return id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Getter.
	 * @return transicionesDisparo
	 */
	public ArrayList<TransicionProceso> getTransiciones() {
		return this.transiciones;
	}
	/**
	 * Agrega una transicion si tiene disparo y/o informe seteado. 
	 * @param transicion transicion para agregar.
	 */
	public void addTransicion(final TransicionProceso transicion) {
		if (transicion.getDisparo() || transicion.getInforme()) {
			this.transiciones.add(transicion);	
		}
	}
}
