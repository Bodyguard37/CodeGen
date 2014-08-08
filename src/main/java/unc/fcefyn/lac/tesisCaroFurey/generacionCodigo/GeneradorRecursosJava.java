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
 * Genera Clases de recursos de la red. Un recurso seria un proceso no runnable.
 * @author María Florencia Caro & Ignacio Furey
 */

public class GeneradorRecursosJava {
	/**
	 * recursoFile archivo de codigo de cada recurso.
	 */
	private File recursoFile;
    /**
     * Buffer para escritura del archivo.
     */
	private BufferedWriter bw;
	/**
	 * Class Constructor.
	 * @param proceso proceso al q hay q generar el codigo
	 * @param datos AbstractHerramientaPN con los datos de la red
	 * @param filePath path de archivos
	 */
	public GeneradorRecursosJava(final Proceso proceso, final AbstractHerramientaPN datos,
			final String filePath) {

		final String recursoFilePath = filePath.concat("\\" + proceso.getId() + ".java");

		try {
			this.recursoFile = new File(recursoFilePath);
			this.recursoFile.createNewFile();
			this.bw = new BufferedWriter(new FileWriter(this.recursoFile));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
        escribirCabecera(proceso);
        escribirConstantesTransicion(
        		datos.getTransiciones(), proceso.getTransiciones());
        escribirMetodosFijos(proceso.getId());
        escribirMetodosRecurso(proceso.getTransiciones(), datos.getEtiquetas());
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
	 * Se encarga de scribir la cabecera a los archivos de codigo de los recursos
	 * del sistema.
	 * @param proceso proceso al cual se le va a escribir el codigo.
	 */
	private void escribirCabecera(final Proceso proceso) {
		try {
			//Se escribe el nombre package de la clase
			this.bw.write("package procesos;\n\n");
			//Se importa el procesador que ejecuta la red.
			this.bw.write("import procesador.ProcesadorPetri;\n\n");
			//Se escribe la definicion de clase
			this.bw.write("public class " + proceso.getId() +
					" {");
			this.bw.newLine();
			//Se declara la variable del procesador.
			this.bw.write("\tprivate ProcesadorPetri procesador;");
			this.bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe por cada transicion del proceso el num de transicion como constante
	 * igualada al num de columna de la matriz que le corresponde.
	 * @param transicionesRed Transiciones del sistema
	 * @param transicionesProceso Transiciones del proceso
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
				//al número de columna de la matriz que le corresponde.
				this.bw.write("\tprivate static final int " +
						transicionActual.getIdTransicion() +
						" = " + columna + ";");
				this.bw.newLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 *  Escribe metodos fijos del archivo. Crea un metodo para el proceso
	 *  pasandole el procesadorPetri.
	 * @param idProceso id del proceso recurso para el que escribe el codigo.
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe los metodos que piden las transiciones o que consultan las
	 * transiciones ejecutadas según corresponda.
	 * @param transicionesProceso
	 * @param etiquetasRed
	 */
	private void escribirMetodosRecurso(
			final ArrayList<TransicionProceso> transicionesProceso,
			final ArrayList<Etiqueta> etiquetasRed) {
		//itera para todas las transiciones.
		final Iterator<TransicionProceso> it = transicionesProceso.iterator();
		while (it.hasNext()) {
			final TransicionProceso trActual = it.next();
			//Obtengo el id de transición.
			final String idTransicion = trActual.getIdTransicion();
			//Obtiene el índice de las etiquetas de esta transición en el
			//array de etiquetasRed.
			int indice;
			for (indice = 0; indice < etiquetasRed.size() &&
					!etiquetasRed.get(indice).getIdTransicion().
					equals(idTransicion); indice = indice + 1) { }
			//Controla y escribe Disparo de transicion.
			if (trActual.getDisparo()) {
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
						this.bw.write("\t//metodo de disparo de transicion " +
							idTransicion + ".\n");
						this.bw.write("\tpublic void " +
							trActual.getMetodoDisparo() +
							"() {\n");
						this.bw.write("\t\t//TODO realizar aqui las tareas " +
							"previas al disparo de la transicion " +
							idTransicion + ".");
						this.bw.write("\n\t\ttry {\n");
						this.bw.write("\t\t\tdisparar(" + idTransicion +
							");\n");
						this.bw.write("\t\t} catch (Exception e) {\n" +
							"\t\t\te.printStackTrace();\n" +
							"\t\t}\n");
						this.bw.write("\t\t//TODO realizar aqui las tareas " +
							"posteriores al disparo de la transicion " +
							idTransicion + ".\n\t}\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			//Controla y escribe Informe de transicion.
			if (trActual.getInforme()) {
				if (etiquetasRed.get(indice).getSegundoValor().equals("N")) {
					try {
						this.bw.write("\t\t\t\t//Transicion " + idTransicion +
								" sin informe.");
						this.bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						this.bw.write("\t//metodo de informe de transicion " +
							idTransicion + ".\n");
						this.bw.write("\tpublic void " +
							trActual.getMetodoInforme() +
							"() {\n");
						this.bw.write("\t\t//TODO realizar aqui las tareas " +
							"previas al informe de la transicion " +
							idTransicion + ".");
						this.bw.write("\n\t\ttry {\n");
						this.bw.write("\t\t\tconsultar(" + idTransicion +
								");\n");
						this.bw.write("\t\t} catch (Exception e) {\n" +
							"\t\t\te.printStackTrace();\n" +
							"\t\t}\n");
						this.bw.write("\t\t//TODO realizar aqui las tareas " +
							"posteriores al informe de la transicion " +
							idTransicion + ".\n\t}\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
