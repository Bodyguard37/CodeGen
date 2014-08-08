package unc.fcefyn.lac.tesisCaroFurey.herramientaPN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorEtiquetas.Etiqueta;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorEtiquetas.ManejadorEtiquetas;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorIntervalos.Intervalo;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorIntervalos.ManejadorIntervalos;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorMatrizyMarcado
	.ManejadorXMLMatrizYMarcado;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorMatrizyMarcado
	.MatrizIncidenciaMarcadoInicial;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorMatrizyMarcado.ProcesaXML;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.ManejadorProcesos;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;

/**
 * Clase abstracta para el manejo de redes de petri.
 * @author María Florencia Caro & Ignacio Furey
 */

public abstract class AbstractHerramientaPN {
	/**
	 * Formatos soportados por la herramienta.
	 */
	protected String[] formatosSoportados;
	/**
	 * Vectores de invariantes de plaza de la red de petri.
	 */
	protected ArrayList<ArrayList<String>> invariantesPlaza;

	/**
	 * Vectores de invariantes de transicion de la red de petri.
	 */
	protected ArrayList<ArrayList<String>> invariantesTransicion;
	/**
	 * Propiedad de vivacidad de la red de petri.
	 */
	protected Boolean vivacidad;
	/**
	 * Propiedad de acotamiento de la red de petri.
	 */
	protected Boolean acotada;
	/**
	 * Propiedad de reversibilidad de la red de petri.
	 */
	protected Boolean reversible;
	/**
	 * Propiedad de interbloqueo de la red de Petri.
	 */
	protected Boolean interbloqueo;
	/**
	 * Texto legible por personas con el analisis de alcanzabilidad.
	 */
	protected String analisisAlcanzabilidad;
	/**
	 * Texto legible por personas con el analisis Estructural.
	 */
	protected String analisisEstructural;
	/**
	 * Intancia con los datos de la matriz de incidencia y el marcado inicial.
	 */
	protected MatrizIncidenciaMarcadoInicial matrizIncidenciaMarcadoInicial;
	/**
	 * manejadorXMLMatrizYMarcado un manejador.
	 */
	private ManejadorXMLMatrizYMarcado manejadorXMLMatrizYMarcado;
	/**
	 * Un manejador de procesos.
	 */
	private ManejadorProcesos manejadorProcesos;
	/**
	 * Un manejador de etiquetas.
	 */
	private ManejadorEtiquetas manejadorEtiquetas;
	/**
	 * Un manejador de intervalos.
	 */
	private ManejadorIntervalos manejadorIntervalos;
	/**
	 * Getter.
	 * @return	formatosSoportados formatos de red de petri soportados por la
	 * herramienta.
	 */
	public final String[] getFormatosSoportados() {
		return this.formatosSoportados;
	}
	/**
	 * Getter.
	 * @return invariantesPlaza
	 */
	public final ArrayList<ArrayList<String>> getInvariantesPlaza() {
		return this.invariantesPlaza;
	}
	/**
	 * Getter.
	 * @return the invariantesTransicion
	 */
	public final ArrayList<ArrayList<String>> getInvariantesTransicion() {
		return this.invariantesTransicion;
	}
	/**
	 * Getter.
	 * @return vivacidad
	 */
	public final Boolean getVivacidad() {
		return this.vivacidad;
	}
	/**
	 * Getter.
	 * @return acotada
	 */
	public final Boolean getAcotada() {
		return this.acotada;
	}
	/**
	 * Getter.
	 * @return reversible
	 */
	public final Boolean getReversible() {
		return this.reversible;
	}
	/**
	 * Getter.
	 * @return interbloqueo
	 */
	public final Boolean getInterbloqueo() {
		return this.interbloqueo;
	}
	/**
	 * Getter.
	 * @return etiquetas
	 */
	public final ArrayList<Etiqueta> getEtiquetas() {
		return this.manejadorEtiquetas.getEtiquetas();
	}
	/**
	 * Getter.
	 * @return intervalos
	 */
	public final ArrayList<Intervalo> getIntervalos() {
		return this.manejadorIntervalos.getIntervalos();
	}
	/**
	 * Getter.
	 * @return Vector de disparos automaticos
	 */
	public final int[] getVectorDisparosAtomaticos() {
		//Se genera vector con tamaño igual a cantidad de transiciones.
		final int[] disparosAutomaticos = 
				new int[this.manejadorEtiquetas.getEtiquetas().size()];
		//Se genera un iterator con el array list de etiquetas.
		final Iterator<Etiqueta> etiquetasIt = 
				this.manejadorEtiquetas.getEtiquetas().iterator();
		System.out.println("Orden de transiciones");
		//Mientras haya otra etiqueta ==> hay otra transicion.
		while (etiquetasIt.hasNext()) {
			//salvo el valor de la proxima etiqueta.
			final Etiqueta actual = etiquetasIt.next();
			//Obtengo el valor con el idTransicion como key. Ese valor sera
			//el indice del vector
			final int indice = this.matrizIncidenciaMarcadoInicial.
					getTransiciones().get(actual.getIdTransicion()).intValue();
			/*System.out.println("id: " + actual.getIdTransicion()
					+ " colum: " + indice);*/
			//guardo el valor que represente la etiqueta de 
			//Disparo = 0/Automatico = 1 en la posicion del vector indicada
			//por indice.
			if (actual.getPrimerValor().equals("A")) {
				disparosAutomaticos[indice] = 1;
			} else {
				disparosAutomaticos[indice] = 0;
			}
		}
		return disparosAutomaticos;
	}
	/**
	 * Getter.
	 * @return Vector de disparos automatios.
	 */
	public final int[] getVectorDisparosSinInforme() {
		//Se genera vector con tamaño igual a cantidad de transiciones.
		final int[] disparosSinInforme = 
				new int[this.manejadorEtiquetas.getEtiquetas().size()];
		//Se genera un iterator con el array list de etiquetas.
		final Iterator<Etiqueta> etiquetasIt = 
				this.manejadorEtiquetas.getEtiquetas().iterator();
		//Mientras haya otra etiqueta ==> hay otra transicion.
		while (etiquetasIt.hasNext()) {
			//salvo el valor de la proxima etiqueta.
			final Etiqueta actual = etiquetasIt.next();
			//Obtengo el valor con el idTransicion como key. Ese valor sera
			//el indice del vector
			final int indice = this.matrizIncidenciaMarcadoInicial.
					getTransiciones().get(actual.getIdTransicion()).intValue();
			//guardo el valor que represente la etiqueta de 
			//Disparo = 0/Automatico = 1 en la posicion del vector indicada
			//por indice.
			if (actual.getSegundoValor().equals("N")) {
				disparosSinInforme[indice] = 1;
			} else {
				disparosSinInforme[indice] = 0;
			}
		}
		return disparosSinInforme;
	}
	/**
	 * Getter.
	 * @return Analisis de Alcanzabilidad en formato texto para lectura humana.
	 */
	public final String getAnalisisAlcanzabilidad() {
		return this.analisisAlcanzabilidad;
	}
	/**
	 * Getter.
	 * @return Analisis Estructural en formato texto para lectura humana
	 */
	public final String getAnalisisEstructural() {
		return this.analisisEstructural;
	}
	/**
	 * Abre la herramienta de edicion de Redes de Petri para crear una
	 * nueva Red de Petri.
	 * @param pathArchivo Path al archivo de la Red de Petri.
	 */
	public final void crearPN(final String pathArchivo) {
		this.editarPN(pathArchivo);
	}
	/**
	 * Es llamado luego de editar una PN. Llama a los metodos
	 * actualizarDatosRedPetri y generarMatrizIncidenciaYMarcado.
	 * @param pathARedPetri Path de la Red de Petri a cargar.
	 * @param pathAXMLProcesos Path al archivo de procesos.
	 */
	public final void cargarRed(final String pathARedPetri,
			final String pathAXMLProcesos) {
		final String nuevoPath = this.convertirAPNML(pathARedPetri);
		this.actualizarDatosRedPetri(nuevoPath);
		this.generarMatrizIncidenciaYMarcado(nuevoPath);

		this.manejadorXMLMatrizYMarcado = new ManejadorXMLMatrizYMarcado();
		this.manejadorProcesos = new ManejadorProcesos(pathAXMLProcesos);
		this.manejadorEtiquetas = new ManejadorEtiquetas(nuevoPath);
		this.manejadorIntervalos = new ManejadorIntervalos(nuevoPath);
	}
	/**
	 * Abre la herramienta de edicion de Redes de Petri para editar la red de
	 * petri indicada por path.
	 * @param path Path de la Red de Petri a editar
	 */
	public final void editarPN(final String path) {
		this.llamarEditor(path);
	}
	//INTERACCION CON MANEJADOR MATRIZ Y MARCADO
	/**
	 * Genera el objeto matrizIncidenciaMarcadoInicial.
	 * @param path Path de la Red de Petri a editar
	 */
	private void generarMatrizIncidenciaYMarcado(final String path) {
		final ManejadorXMLMatrizYMarcado manejador =
			new ManejadorXMLMatrizYMarcado();
		new ProcesaXML(manejador, path);
		this.matrizIncidenciaMarcadoInicial = manejador.getMatriz();
	}
	/**
	 * Getter.
	 * @return Matriz de Incidencia Positiva
	 */
	public final int[][] getMatrizIncidenciaPositiva() {
		return this.matrizIncidenciaMarcadoInicial.getMatrizPositiva();
	}
	/**
	 * Getter.
	 * @return Matriz de Incidencia Negativa
	 */
	public final int[][] getMatrizIncidenciaNegativa() {
		return this.matrizIncidenciaMarcadoInicial.getMatrizNegativa();
	}
	/**
	 * Getter.
	 * @return Marcado Inicial
	 */
	public final int[] getMarcadoInicial() {
		return this.matrizIncidenciaMarcadoInicial.getMarcado();
	}
	/**
	 * Getter.
	 * @return Marcado Transiciones
	 */
	public final HashMap<String, Integer[]> getPlazas() {
		return this.matrizIncidenciaMarcadoInicial.getPlazas();
	}
	/**
	 * Getter.
	 * @return Marcado Transiciones
	 */
	public final HashMap<String, Integer> getTransiciones() {
		return this.matrizIncidenciaMarcadoInicial.getTransiciones();
	}
	/**
	 * Getter.
	 * @return String con la descripcion de las matrices de incidencia y el
	 * marcado inicial de la red.
	 */
	public final String matrizIncidenciaMarcadoInicialComoString() {
		return this.matrizIncidenciaMarcadoInicial.matrizYMarcadoComoString();
	}
	//INTERCACCION CON MANEJADOR de PROCESOS
	/**
	 * Getter.
	 * @return los procesos
	 */
	public final ArrayList<Proceso> getProcesos() {
		return this.manejadorProcesos.getProcesos();
	}
	/**
	 * Agregar un proceso al archivo de procesos de la red y a la variable de
	 * procesos de HerramientaPN.
	 * @param proceso Proceso a agregar.
	 * @param pathAXMLProcesos path del archivo de procesos.
	 */
	public final void agregarProceso(final Proceso proceso,
			final String pathAXMLProcesos) {
		this.manejadorProcesos.agregarProceso(proceso, pathAXMLProcesos);
	}
	/**
	 * Remover un proceso.
	 * @param proceso Proceso a remover.
	 * @param xmlFilePath path del archivo 
	 * @param pathAXMLProcesos 
	 */
	public final void borrarProceso(final Proceso proceso,
			final String xmlFilePath, final String pathAXMLProcesos) {
		this.manejadorProcesos.borrarProceso(proceso, pathAXMLProcesos);
	}
	/**
	 * Actualizar un proceso.
	 * @param procesoViejo Proceso a actualizar.
	 * @param procesoNuevo Proceso que contiene los nuevos datos.
	 * @param pathAXMLProcesos path del archivo XML de procesos.
	 */
	public final void actualizarProceso(final Proceso procesoViejo,
		final Proceso procesoNuevo, final String pathAXMLProcesos) {
		this.manejadorProcesos.actualizarProceso(procesoViejo, procesoNuevo,
				pathAXMLProcesos);
	}
	//METODOS ABSTRACTOS
	/**
	 * Abre la herramienta de edicion de Redes de Petri para crear una nueva
	 * Red de Petri.
	 * Debe implementarse en la clase hija.
	 * @param path Path del archivo de la red de Petri.
	 */
	public abstract void llamarEditor(String path);
	/**
	 * Metodo el cual se deberan cargar los valores de todos los atributos de
	 * esta clase. Dichos atributos seran puestos a disposicion de cualquier
	 * clase mediante los correspondientes getters.
	 * Debe implementarse en la clase hija.
	 * @param path Path del archivo de la red de Petri.
	 */
	public abstract void actualizarDatosRedPetri(String path);
	/**
	 * Utiliza la herramienta de conversion de formatos de Red de Petri para
	 * convertir desde algun formato soportado al formato estandar PNML.
	 * Debe implementarse en la clase hija.
	 * @param archivo Path del archivo a convertir.
	 * @return Path al nuevo archivo.
	 */
	public abstract String convertirAPNML(String archivo);
	/**
	 * Utiliza la herramienta de conversion de formatos de Red de Petri para
	 * obtener una red dada en algun formato soportado en formato de texto NET.
	 * @param archivoFuente Red de Petri que se desea mostrar.
	 * @return Texto con la descripcion grafica de la Red de Petri.
	 */
	public abstract String mostrarEnFormatoNET(String archivoFuente);
	/**
	 * Reemplaza archivo de procesos.
	 * @param procesos procesos nuevos.
	 */
	public void reemplazarArchivoXML(final ArrayList<Proceso> procesos) {
		this.manejadorProcesos.reemplazarArchivoXML(procesos);
	}
}
