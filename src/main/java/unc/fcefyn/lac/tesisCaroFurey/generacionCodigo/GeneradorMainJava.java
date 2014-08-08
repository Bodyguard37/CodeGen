package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;

/**
 * Clase que genera el codigo fuente del archivo Main del proyecto generado.
 * Solo se debe generar una nueva instancia con los parametros requeridos y
 * genera el archivo .java en el directorio indicado.
 * @author María Florencia Caro & Ignacio Furey
 */

public class GeneradorMainJava {
	/**
	 * Archivo Main del sistema.
	 */
	private File mainFile;
	/**
	 * Buffer de escritura para archivo fuente generado.
	 */
    private BufferedWriter bw;
    /**
     * Constructor. Obtiene información relevante de datos para generar el
     * código fuente de la clase Main del proyecto generado.
     * @param filePath path del directorio donde se generara el archivo Main.
     * @param datos instancia de AbstractHerramientaPN con los datos ya
     * cargados de la red de la cual se desea generar el codigo.
     * @param configFilePath path del archivo de configuracion del procesador
     * de petri con los datos de la red.
     */
	public GeneradorMainJava(final String filePath,
			final AbstractHerramientaPN datos,
			final String configFilePath) {

		final String mainFilePath = filePath.concat("\\Main.java");
        try {
        	this.mainFile = new File(mainFilePath);
        	this.mainFile.createNewFile();
        	this.bw = new BufferedWriter(new FileWriter(this.mainFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
        escribirCabecera(datos.getProcesos());
        escribirConstantes(datos.getTransiciones().size(), configFilePath);
        escribirMetodoMain();
        escribirConstructor(datos.getProcesos());
        try {
			//Se cierra la clase.
        	this.bw.write("}\n");
		    this.bw.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metodo que escribe la cabecera del archivo fuente Main del proyecto.
	 * Incluyendo la declaracion del package, clases importadas e interfaz de
	 * clase.
	 * @param procesos ArrayList con la lista completa de procesos definidos
	 * de la red.
	 */
	private void escribirCabecera(final ArrayList<Proceso> procesos) {
		try {
			//Se escribe el nombre package de la clase
			this.bw.write("package main;");
			this.bw.newLine();
			//Se importa el procesador que ejecuta la red.
			this.bw.write("import procesador.ProcesadorPetri;");
			this.bw.newLine();
			this.bw.write("import procesadorPNVirtual.ProcesadorPetriVirtual;");
			this.bw.newLine();
			final Iterator<Proceso> itp = procesos.iterator();
			while (itp.hasNext()) {
				final String idProceso = itp.next().getId();
				this.bw.write("import procesos." + idProceso + ";");
				this.bw.newLine();
			}
			//Se escribe la definicion de clase
			this.bw.write("\npublic class Main {");
			this.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Se encarga de escribir las contantes en el archivo main generado.
	 * @param cantidadTransiciones cantidad de transiciones del sistema
	 * @param configPath path 
	 */
	private void escribirConstantes(
		final int cantidadTransiciones, final String configPath) {
		try {
			final String[] arrayConfigPath = configPath.split("\\\\");
			String nuevoPath = new String("");
			for (int i = 0; i < arrayConfigPath.length; i = i + 1) {
				nuevoPath = nuevoPath + arrayConfigPath[i];
				if (i < (arrayConfigPath.length - 1)) {
					nuevoPath = nuevoPath + "\\\\";
				}
			}
			//Se escribe el path del archivo de configuracion y la 
			//constante con cantidad de transiciones.
			this.bw.write("\n\tprivate static final int CANTIDADTRANSICIONES = " +
				cantidadTransiciones + ";");
			this.bw.newLine();
			this.bw.write("\tprivate static final String CONFIGFILE = \" " +
					nuevoPath +
					"\";\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe el metodo main en el archivo "main" del codigo generado.
	 */
	private void escribirMetodoMain() {
		try {
			this.bw.write("\tpublic static void main(String[] args) {\n" +
					"\t\t\tjavax.swing.SwingUtilities.invokeLater(new Runnable(){\n" +
					"\t\t\t\tpublic void run(){\n" +
					"\t\t\t\t\tMain m = new Main();\n" +
            		"\t\t\t\t}\n\t\t});\n\t}\n");
			this.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Escribe el contructor en el archivo "main" del codigo generado.
	 * @param procesos procesos que estan definidos
	 */
	private void escribirConstructor(final ArrayList<Proceso> procesos) {
		try {
			this.bw.write("\tpublic Main() {\n" +
				"\t\t//En primer lugar se levanta el procesador.\n" +
				"\t\tProcesadorPetri procesador =" +
				"new ProcesadorPetriVirtual" +
				"(CONFIGFILE, CANTIDADTRANSICIONES);\n" +
				"\t\t//Luego se levantan los procesos.\n");
			final Iterator<Proceso> it = procesos.iterator();
			while (it.hasNext()) {
				final Proceso proceso = it.next();
				final String idProceso = proceso.getId();
				if (proceso.isRunnable()) {
					this.bw.write("\t\tnew Thread(new " + idProceso +
						"(procesador)).start();");
					this.bw.newLine();
				} else {
					this.bw.write("\t\t" + idProceso + " " +
							idProceso.toLowerCase() + "= new " +
							idProceso + "(procesador);");
					this.bw.newLine();
				}
			}
			this.bw.write("\t}");
			this.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
