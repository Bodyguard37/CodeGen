package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos;

/**
 * Esta clase esta encargada de guardar los datos de una transicion especifica
 * relacionada a un proceso.
 * @author María Florencia Caro & Ignacio Furey
 */

public class TransicionProceso {
	/**
	 * id de transicion.
	 */
	private String idTransicion;
	/**
	 *Es igual a true si este proceso controla el informe de esta transicion.
	 *false de lo contrario.
	 */
	private boolean informe;
	/**
	 *Es igual a true si este proceso controla el disparo de esta transicion.
	 *false de lo contrario.
	 */
	private boolean disparo;
	/**
	 *Almacena el metodo para el informe de esta transicion.
	 */
	private String metodoInforme;
	/**
	 *Almacena el metodo para el disparo de esta transicion.
	 */
	private String metodoDisparo;
	/**
	 *Almacena el orden de informe de esta transicion dentro del proceso.
	 */
	private String ordenInforme;
	/**
	 *Almacena el orden de disparo de esta transicion dentro del proceso.
	 */
	private String ordenDisparo;
	/**
	 * Constructor.
	 * @param idTransicionNuevo el id de esta transicion.
	 */
	public TransicionProceso(final String idTransicionNuevo) {
		this.informe = false;
		this.disparo = false;
		this.idTransicion = idTransicionNuevo;
	}
	/**
	 * Getter.
	 * @return id.
	 */
	public String getIdTransicion() {
		return this.idTransicion;
	}
	/**
	 * Getter.
	 * @return el informe.
	 */
	public boolean getInforme() {
		return this.informe;
	}
	/**
	 * Setter.
	 * @param idTransicionNuevo id de la transicion.
	 */
	public void setIdTransicion(final String idTransicionNuevo) {
		this.idTransicion = idTransicionNuevo;
	}
	/**
	 * Setter.
	 * @param informeNuevo informe de transicion.
	 */
	public void setInforme(final boolean informeNuevo) {
		this.informe = informeNuevo;
	}
	/**
	 * setter.
	 * @param disparoNuevo disparo de transicion.
	 */
	public void setDisparo(final boolean disparoNuevo) {
		this.disparo = disparoNuevo;
	}

	/**
	 * Setter.
	 * @param metodoInformeNuevo String con el metodo del informe de transicion.
	 */
	public void setMetodoInforme(final String metodoInformeNuevo) {
		this.metodoInforme = metodoInformeNuevo;
	}
	/**
	 * Setter.
	 * @param metodoDisparoNuevo String con el metodo del disparo de transicion.
	 */
	public void setMetodoDisparo(final String metodoDisparoNuevo) {
		this.metodoDisparo = metodoDisparoNuevo;
	}
	/**
	 * Setter.
	 * @param ordenInformeNuevo String con el orden del informe de transicion.
	 */
	public void setOrdenInforme(final String ordenInformeNuevo) {
		this.ordenInforme = ordenInformeNuevo;
	}
	/**
	 * Setter.
	 * @param ordenDisparoNuevo String con el orden del disparo de transicion.
	 */
	public void setOrdenDisparo(final String ordenDisparoNuevo) {
		this.ordenDisparo = ordenDisparoNuevo;
	}
	/**
	 * Retorna el estado del disparo de esta transicion para el proceso.
	 * @return disparo true si este proceso incluye el pedido de disparo de esta
	 * transicion.
	 */
	public boolean getDisparo() {
		return this.disparo;
	}
	/**
	 * Getter.
	 * @return metodoInforme El metodo de informe de la transicion.
	 */
	public String getMetodoInforme() {
		return this.metodoInforme;
	}
	/**
	 * Getter.
	 * @return metodoDisparo El metodo de disparo de la transicion.
	 */
	public String getMetodoDisparo() {
		return this.metodoDisparo;
	}
	/**
	 * Getter.
	 * @return ordenInforme El orden de informe de la transicion.
	 */
	public String getOrdenInforme() {
		return this.ordenInforme;
	}
	/**
	 * Getter.
	 * @return ordenDisparo El orden de disparo de la transicion.
	 */
	public String getOrdenDisparo() {
		return this.ordenDisparo;
	}
}
