package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorEtiquetas.Etiqueta;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.
		TransicionProceso;

/**
 * Se encarga de escribir el archivo de código fuente de las clases de los procesos
 * ``runnable'' del codigo generado, segun los procesos ``runnable'' definidos
 * previamente por el  usuario en la herramienta.
 * @author María Florencia Caro & Ignacio Furey
 */

public class GeneradorHilosJava {
	/**
	 * hiloFile archivo de codigo de cada hilo.
	 */
	private File hiloFile;
	/**
	 * Buffer para escritura de archivo.
	 */
    private BufferedWriter bw;
    /**
     * En esta variable se iran generando los metodos de las transiciones que
     * contenga este hilo y se escribiran en el archivo al final.
     */
    private String metodosTransiciones;
	/**
	 * Class Constructor.
	 * @param proceso proceso del cual hay que generar el codigo.
	 * @param datos AbstractHerramientaPN con los datos de la red.
	 * @param filePath path de archivos.
	 */
	public GeneradorHilosJava(final Proceso proceso,
			final AbstractHerramientaPN datos,
			final String filePath) {

		this.metodosTransiciones = "";
		final String hiloFilePath =
				filePath.concat("\\" + proceso.getId() + ".java");

		try {
			this.hiloFile = new File(hiloFilePath);
			this.hiloFile.createNewFile();
			this.bw = new BufferedWriter(new FileWriter(this.hiloFile));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
        escribirCabecera(proceso);
        escribirConstantesTransicion(
        		datos.getTransiciones(), proceso.getTransiciones());
        escribirMetodosFijos(proceso.getId());
        escribirMetodoRun(proceso.getTransiciones(), datos.getEtiquetas());
		escribirMetodosTransiciones();
        try {
			//Se cierra la clase.
        	this.bw.write("}");
        	this.bw.newLine();
        	this.bw.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe la cabecera del hilo.
	 * @param proceso instancia de Proceso con los datos del hilo.
	 */
	private void escribirCabecera(final Proceso proceso) {
		try {
			//Se escribe el nombre package de la clase
			this.bw.write("package procesos;\n\n");
			//Se importa el procesador que ejecuta la red.
			this.bw.write("import procesador.ProcesadorPetri;\n\n");
			//Se escribe la definicion de clase
			this.bw.write("public class " + proceso.getId() +
					" implements Runnable {");
			this.bw.newLine();
			//Se declara la variable del procesador.
			this.bw.write("\tprivate ProcesadorPetri procesador;");
			this.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe en archivo las constantes con los datos de la transicion.
	 * @param transicionesRed datos de las trancisiones de la red.
	 * @param transicionesProceso datos de las transiciones del proceso.
	 */
	private void escribirConstantesTransicion(
		final HashMap<String, Integer> transicionesRed,
		final ArrayList<TransicionProceso> transicionesProceso) {

		final Iterator<TransicionProceso> it = transicionesProceso.iterator();
		while (it.hasNext()) {
			final TransicionProceso transicionActual = it.next();
			final int columna = transicionesRed.get(
					transicionActual.getIdTransicion()).intValue();
			try {
				//Se escribe el nombre de la transicion como constante igualada
				//al numero de columna de la matriz que le corresponde.
				this.bw.write("\tprivate static final int " +
						transicionActual.getIdTransicion() +
						" = " + columna + ";");
				this.bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Escribe metodos que no varian entre diferentes hilos de la red.
	 * @param idProceso id de proceso.
	 */
	private void escribirMetodosFijos(final String idProceso) {
		try {
			this.bw.newLine();
			this.bw.write("\tpublic " + idProceso +
					"(ProcesadorPetri procesadorPN) {");
			this.bw.newLine();
			this.bw.write("\t\tthis.procesador = procesadorPN;");
			this.bw.newLine();
			this.bw.write("\t}");
			this.bw.newLine();
			this.bw.write("\tprivate void consultar(int transicion) {\n" +
				"\t\twhile (!procesador.consultarDisparoTransicion(" +
				"\t\t\ttransicion));\n" +
				"\t}");
			this.bw.newLine();
			this.bw.write("\tprivate void disparar(int transicion) {\n" +
				"\t\tprocesador.encolar(0, transicion);\n" +
				"\t}");
			this.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe el metodo run para el hilo.
	 * @param transicionesProceso transiciones del proceso.
	 * @param etiquetasRed etiquetas de las transiciones de la red.
	 */
	private void escribirMetodoRun(
			final ArrayList<TransicionProceso> transicionesProceso,
			final ArrayList<Etiqueta> etiquetasRed) {
		try {
			//Escribe cabecera del metodo.
			this.bw.write("\tpublic void run() {\n");
			escrituraTransicionesExtraWhile(
					transicionesProceso, etiquetasRed);
			this.bw.write("\t\t//Continuamente ejecuta las transiciones.\n" +
					"\t\twhile (true) {");
			this.bw.newLine();
			escrituraTransicionesIntraWhile(
					transicionesProceso, etiquetasRed);
			this.bw.write("\t\t}\n\t}\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe los disparos e informes de las transiciones dentro del while del
	 * metodo run.
	 * @param transicionesProceso transiciones del proceso.
	 * @param etiquetasRed etiquetas de las transiciones de la red.
	 */
	private void escrituraTransicionesIntraWhile(
			final ArrayList<TransicionProceso> transicionesProceso,
			final ArrayList<Etiqueta> etiquetasRed) {
		//Revisa transicion por transicion para todos los ordenes posibles.
		for (int orden = 1;
				orden <= (transicionesProceso.size() * 2);
				orden = orden + 1) {
			//itera para todas las transiciones.
			final Iterator<TransicionProceso> it =
					transicionesProceso.iterator();
			while (it.hasNext()) {
				final TransicionProceso trActual = it.next();
				//Obtengo el id de transicion.
				final String idTransicion = trActual.getIdTransicion();
				//Obtiene el indice de las etiquetas de esta transicion en el
				//array de etiquetasRed.
				int indice;
				for (indice = 0; indice < etiquetasRed.size() &&
						!etiquetasRed.get(indice).getIdTransicion().
						equals(idTransicion); indice = indice + 1) { }
				//Controla y escribe Disparo de transicion.
				final String sOrden = Integer.toString(orden);
				if (trActual.getDisparo() && trActual.getOrdenDisparo().
						equals(sOrden)) {
					if (etiquetasRed.get(indice).getPrimerValor().equals("A")) {
						try {
							this.bw.write("\t\t\t\t//Transicion " + idTransicion +
									" con disparo Automatico.");
							this.bw.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							this.bw.write("\t\t\t//ejecucion disparo transicion " +
									idTransicion + "\n\t\t\ttry {");
							this.bw.newLine();
							this.bw.write("\t\t\t\tdisparar(" + idTransicion +
									");\n" +
									"\t\t\t\t" + trActual.getMetodoDisparo() +
									"();");
							this.bw.newLine();
							this.bw.write("\t\t\t} catch (Exception e) {\n" +
									"\t\t\t\te.printStackTrace();\n" +
									"\t\t\t}");
							this.bw.newLine();
							guardarMetodoTransicion(idTransicion,
									trActual.getMetodoDisparo(), "disparo");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				//Controla y escribe Informe de transicion.
				if (trActual.getInforme() && trActual.getOrdenInforme().
						equals(Integer.toString(orden))) {
					if (etiquetasRed.get(indice).
							getSegundoValor().equals("N")) {
						try {
							this.bw.write("\t\t\t\t//Transicion " + idTransicion +
									" sin Informe de disparo.");
							this.bw.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							this.bw.write("\t\t\t//ejecucion informe transicion " +
									idTransicion + "\n\t\t\ttry {");
							this.bw.newLine();
							this.bw.write("\t\t\t\tconsultar(" + idTransicion +
									");\n" +
									"\t\t\t\t" + trActual.getMetodoInforme() +
									"();");
							this.bw.newLine();
							this.bw.write("\t\t\t} catch (Exception e) {\n" +
									"\t\t\t\te.printStackTrace();\n" +
									"\t\t\t}");
							this.bw.newLine();
							guardarMetodoTransicion(idTransicion,
									trActual.getMetodoInforme(), "informe");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	/**
	 * Escribe los disparos e informes de las transiciones fuera del while del
	 * metodo run. Estas conformarian las precondiciones del hilo.
	 * @param transicionesProceso transiciones del proceso.
	 * @param etiquetasRed etiquetas de las transiciones de la red.
	 */
	private void escrituraTransicionesExtraWhile(
			final ArrayList<TransicionProceso> transicionesProceso,
			final ArrayList<Etiqueta> etiquetasRed) {
		//Revisa transicion por transicion para todos los ordenes posibles.
		for (int orden = 1;
				orden <= (transicionesProceso.size() * 2) &&
						orden <= 26; orden = orden + 1) {
			//itera para todas las transiciones.
			final Iterator<TransicionProceso> it =
					transicionesProceso.iterator();
			while (it.hasNext()) {
				final TransicionProceso trActual = it.next();
				//Obtengo el id de transicion.
				final String idTransicion = trActual.getIdTransicion();
				//Obtiene el indice de las etiquetas de esta transicion en el
				//array de etiquetasRed.
				int indice;
				for (indice = 0; indice < etiquetasRed.size() &&
						!etiquetasRed.get(indice).getIdTransicion().
						equals(idTransicion); indice = indice + 1) { }
				//Controla y escribe Disparo de transicion.
				final String sOrden = String.valueOf((char) ('A' + (orden - 1)));
				if (trActual.getDisparo() && trActual.getOrdenDisparo().
						equals(sOrden)) {
					if (etiquetasRed.get(indice).getPrimerValor().equals("A")) {
						try {
							this.bw.write("\t\t\t\t//Transicion " + idTransicion +
									" con disparo Automatico.");
							this.bw.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							this.bw.write("\t\t\t//ejecucion disparo transicion " +
									idTransicion + "\n\t\t\ttry {");
							this.bw.newLine();
							this.bw.write("\t\t\t\tdisparar(" + idTransicion +
									");\n" +
									"\t\t\t\t" + trActual.getMetodoDisparo() +
									"();");
							this.bw.newLine();
							this.bw.write("\t\t\t} catch (Exception e) {\n" +
									"\t\t\t\te.printStackTrace();\n" +
									"\t\t\t}");
							this.bw.newLine();
							guardarMetodoTransicion(idTransicion,
									trActual.getMetodoDisparo(), "disparo");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				//Controla y escribe Informe de transicion.
				if (trActual.getInforme() && trActual.getOrdenInforme().
						equals(sOrden)) {
					if (etiquetasRed.get(indice).
							getSegundoValor().equals("N")) {
						try {
							this.bw.write("\t\t\t\t//Transicion " + idTransicion +
									" sin Informe de disparo.");
							this.bw.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							this.bw.write("\t\t\t//ejecucion informe transicion " +
									idTransicion + "\n\t\t\ttry {");
							this.bw.newLine();
							this.bw.write("\t\t\t\tconsultar(" + idTransicion +
									");\n" +
									"\t\t\t\t" + trActual.getMetodoInforme() +
									"();");
							this.bw.newLine();
							this.bw.write("\t\t\t} catch (Exception e) {\n" +
									"\t\t\t\te.printStackTrace();\n" +
									"\t\t\t}");
							this.bw.newLine();
							guardarMetodoTransicion(idTransicion,
									trActual.getMetodoInforme(), "informe");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	/**
	 * Escribe los metodos de las transiciones que fueron guardados en la
	 * variable metodosTransiciones.
	 */
	private void escribirMetodosTransiciones() {
		try {
			this.bw.write(this.metodosTransiciones);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Guarda un nuevo metodo de una transicion en la variable
	 * metodosTransiciones.
	 * @param idTransicion id de la transicion.
	 * @param nombreMetodo nombre del metodo.
	 * @param infDis indica si el metodo a escribir es de informe o disparo.
	 */
	private void guardarMetodoTransicion(final String idTransicion,
			final String nombreMetodo, final String infDis) {

		this.metodosTransiciones = this.metodosTransiciones.concat(
				"\t//metodo de " + infDis + " de transicion " +
				idTransicion + ".\n");
		this.metodosTransiciones = this.metodosTransiciones.concat(
				"\tprivate void " + nombreMetodo + "() {\n");
		this.metodosTransiciones = this.metodosTransiciones.concat(
				"\t\t//TODO realizar aqui las tareas concernientes al " +
				infDis + " de la transicion " +	idTransicion + ".\n\t}\n");
	}
}
