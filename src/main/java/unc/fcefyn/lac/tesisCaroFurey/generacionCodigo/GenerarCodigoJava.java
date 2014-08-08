package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.Iterator;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;
import unc.fcefyn.lac.tesisCaroFurey.resourcesManager.ResourcesManager;

/**
 * Clase Generacion de Codigo en Java. Se encarga de crear la estructura
 * de directorios del codigo generado, copiar la libreria y generar los archivos
 * de configuracion del procesador virtual.
 * Como su nombre lo indica, el codigo generado es en lenguage Java, y el resultado
 * obtenido es un ``proyecto'' con el codigo de todas las clases y
 * los recursos necesarios para su ejecucion.		  
 * @author María Florencia Caro & Ignacio Furey
 */

public class GenerarCodigoJava implements GeneradorCodigo {
	/**
	 * Nombre de archivo para la matriz de incidencia.
	 */
	private static final String INCIDENCIA = "incidencia";
	/**
	 * Nombre de archivo para el marcado inicial.
	 */
	private static final String MARCADO = "marcado";
	/**
	 * Nombre de archivo para para la matriz de relaciones.
	 */
	private static final String RELACION = "relacion";
	/**
	 * Nombre de archivo para las cotas de plazas.
	 */
	private static final String COTAS = "cotas";
	/**
	 * Nombre de archivo para los diparos automaticos.
	 */
	private static final String AUTOMATICOS = "automaticos";
	/**
	 * Nombre de archivo para la matriz de prioridades.
	 */
	private static final String PRIORIDADES = "prioridades";
	/**
	 * Nombre de archivo para la matriz de prioridades distribuidas.
	 */
	private static final String PRIORIDADESDISTRIBUIDAS =
			"prioridadesDistribuidas";
	/**
	 * Nombre de archivo para las transiciones sin informe.
	 */
	private static final String SININFORME = "sinInforme";

	/**
	 * Constructor.
	 */
	public GenerarCodigoJava() {
	}
	/**
	 * Genera codigo fuente en lenguaje Java.
	 * @param infoRed instancia de AbstractHerramientaPN uqe contenga
	 * los analisis de la red para generar codigo.
	 * @param pathArchivos path a donde guardar el codigo generado.
	 */
	public void generarCodigo(final AbstractHerramientaPN infoRed,
			final String pathArchivos) {
		//Genera archivos de configuracion de procesador de petri.
		generarArchivosConfiguracion(infoRed, pathArchivos);

		//Genera el codigo
		final String pathCodigo = pathArchivos + "\\src";
		final File folderCodigo = new File(pathCodigo);
		folderCodigo.mkdirs();
		final String pathMain =  pathArchivos + "\\src" + "\\main";
		final File folderMain = new File(pathMain);
		folderMain.mkdirs();

		//primero genera la clase Main
		new GeneradorMainJava(pathMain, infoRed,
				pathArchivos + "\\archivosConfiguracion\\config.txt");
		//luego se generan los hilos

		final String pathProcesos =  pathArchivos + "\\src" + "\\procesos";
		final File folderProcesos = new File(pathProcesos);
		folderProcesos.mkdirs();
		final Iterator<Proceso> it = infoRed.getProcesos().iterator();
		while (it.hasNext()) {
			final Proceso proc = it.next();
			if (proc.isRunnable()) {
				//Si el proceso es runnable, genera un hilo.
				new GeneradorHilosJava(proc, infoRed, pathProcesos);
			} else {
				//Si no es runnable, genera un recurso.
				new GeneradorRecursosJava(proc, infoRed, pathProcesos);
			}
		}
		//Se llama al metodo copiar para agregar en el directorio donde se
		//generará el codigo al procesador PPNV.jar
		URI jarUri;
		URI ppnvUri;
		try {
			jarUri = ResourcesManager.getJarURI();
			ppnvUri = ResourcesManager.getFile(jarUri, "ppnv/PPNV.jar");
			final String sourceFile = ppnvUri.getPath();
			final String destinationFile = pathArchivos + "\\PPNV.jar";
			copiar(sourceFile, destinationFile);
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Genera todos los archivos de configuracion para el procesador de petri.
	 * @param infoRed instancia de Herramienta red de petri con informacion
	 * de la red.
	 * @param pathArchivos path del proyecto.
	 */
	protected void generarArchivosConfiguracion(
			final AbstractHerramientaPN infoRed,
			final String pathArchivos) {
		final String pathConfig = pathArchivos + "\\archivosConfiguracion";
		final File folderConfig = new File(pathConfig);
		folderConfig.mkdirs();
		////Se genera la matriz de prioridades distribuidas con un vector nulo,
		//por que no existen transiciones distribuidas.
		generarVectorCero(1,
				pathConfig + "\\" + PRIORIDADESDISTRIBUIDAS + ".txt",
				"\n");
		//Matriz de incidencia
		generarArchivoMatriz(infoRed.getMatrizIncidenciaPositiva(),
				infoRed.getMatrizIncidenciaNegativa(),
				pathConfig + "\\" + INCIDENCIA + "0.txt");
		//Marcado Inicial
		generarArchivoVectorVertical(infoRed.getMarcadoInicial(),
				pathConfig + "\\" + MARCADO + "0.txt");
		//Se genera la matriz de relaciones con un vector nulo, de modo que
		//no existan relaciones.
		generarVectorCero(infoRed.getMatrizIncidenciaPositiva()[0].length,
				pathConfig + "\\" + RELACION + "0.txt",
				" ");
		//Se genera las cotas de plazas con un vector nulo, de modo que
		//no existan limitaciones de tokens en las plazas.
		generarVectorCero(infoRed.getMatrizIncidenciaPositiva().length,
				pathConfig + "\\" + COTAS + "0.txt",
				"\n");
		//Se genera el vector de disparos automaticos.
		generarArchivoVectorVertical(infoRed.getVectorDisparosAtomaticos(),
				pathConfig + "\\" + AUTOMATICOS + "0.txt");
		//Se genera el vector de disparos sin informe.
		generarArchivoVectorVertical(infoRed.getVectorDisparosSinInforme(),
				pathConfig + "\\" + SININFORME + "0.txt");
		//Se genera una matriz identidad de prioridades.
		generarMatrizIdentidad(infoRed.getMatrizIncidenciaPositiva()[0].length,
				pathConfig + "\\" + PRIORIDADES + "0.txt");
		//Se genera el archivo de configuracion con los nombres del resto de los
		//archivos.
		generarArchivoConfig(pathConfig + "\\config.txt");
	}
	/**
	 * Generador del archivo matriz de configuracion del procesador.
	 * @param matrizPositiva matriz de incidencia positiva
	 * @param matrizNegativa matriz de incidencia negativa
	 * @param filePath path donde crear el archivo
	 */
	protected void generarArchivoMatriz(final int[][] matrizPositiva,
		final int[][] matrizNegativa, final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
        	bw = new BufferedWriter(new FileWriter(matrizFile));
			for (int f = 0; f < matrizPositiva.length; f = f + 1) {
				for (int c = 0; c < matrizPositiva[0].length; c = c + 1) {
					final int valor = matrizPositiva[f][c] - matrizNegativa[f][c];
					bw.write(String.valueOf(valor) + " ");
				}
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Crea un archivo cuyo path sera filePath con un vector vertical con los
	 * valores definidos en valores.
	 * @param valores vector con los valores.
	 * @param filePath path del archivo a crear.
	 */
	protected void generarArchivoVectorVertical(final int[] valores, final String filePath) {
		File marcadoFile = null;
		BufferedWriter bw = null;
        try {
        	marcadoFile = new File(filePath);
        	marcadoFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(marcadoFile));

			for (int f = 0; f < valores.length; f = f + 1) {
				final int valor = valores[f];
				bw.write(String.valueOf(valor));
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Genera un vector con todos sus valores a cero.
	 * @param length Longitud
	 * @param filePath path del archivo a crear.
	 * @param separador separador entre valores. Normalmente espacio blanco o
	 * nueva linea.
	 */
	protected void generarVectorCero(final int length, final String filePath,
			final String separador) {
		File relacionesFile = null;
		BufferedWriter bw = null;
        try {
        	relacionesFile = new File(filePath);
        	relacionesFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(relacionesFile));
			for (int f = 0; f < length; f = f + 1) {
				bw.write('0' + separador);
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	/**
	 * Generar matriz Identidad.
	 * @param length Longitud
	 * @param filePath path del archivo a crear.
	 */
	protected void generarMatrizIdentidad(final int length, final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(matrizFile));
			for (int f = 0; f < length; f = f + 1) {
				for (int c = 0; c < length; c = c + 1) {
					final int valor;
					if (f == c) {
						valor = 1;
					} else {
						valor = 0;
					}
					bw.write(String.valueOf(valor) + " ");
				}
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	/**
	 * Generar archivo config.
	 * @param filePath Path al archivo
	 */
	protected void generarArchivoConfig(final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(matrizFile));

			bw.write("<" + INCIDENCIA + ">"  + INCIDENCIA + "X.txt" +
					"</" + INCIDENCIA + ">");
			bw.newLine();
			bw.write("<" +  MARCADO + ">"  +  MARCADO + "X.txt" +
					"</" +  MARCADO + ">");
			bw.newLine();
			bw.write("<" + COTAS + ">"  + COTAS + "X.txt" +
					"</" + COTAS + ">");
			bw.newLine();
			bw.write("<" + AUTOMATICOS + ">"  + AUTOMATICOS + "X.txt" +
					"</" + AUTOMATICOS + ">");
			bw.newLine();
			bw.write("<" + PRIORIDADES + ">"  + PRIORIDADES + "X.txt" +
					"</" + PRIORIDADES + ">");
			bw.newLine();
			bw.write("<" + PRIORIDADESDISTRIBUIDAS + ">" +
					PRIORIDADESDISTRIBUIDAS + ".txt" +
					"</" + PRIORIDADESDISTRIBUIDAS + ">");
			bw.newLine();
			bw.write("<" + SININFORME + ">"  + SININFORME + "X.txt" +
					"</" + SININFORME + ">");
			bw.newLine();
			bw.write("<" + RELACION + ">"  + RELACION + "X.txt" +
					"</" + RELACION + ">");
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Copia un archivo desde sourceFile a destinationFile. Es usado para
	 * copiar la libreria del procesador al proyecto generado junto con el
	 * codigo de los procesos.
	 * @param sourceFile path actual de donde se va a sacar el archivo
	 * @param destinationFile path que el usuario defina para la generacion de 
	 * codigo, donde se va a copiar el procesador
	 */

	protected void copiar(String sourceFile, String destinationFile) {
	
		final File destination = new File(destinationFile);
		try {
			destination.createNewFile();
		} catch (IOException e1) {
			System.out.println("Error creando el nuevo archivo :" + e1);
			e1.printStackTrace();
		}
		try {
			FileInputStream is;
			is = new FileInputStream(sourceFile);
			final FileOutputStream os = new FileOutputStream(destination);
		    final FileChannel srcChannel = is.getChannel();
		    final FileChannel dstChannel = os.getChannel();
		    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		    is.close();
		    os.close();
		} catch (FileNotFoundException e) {
			System.out.println("Specified file not found :" + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error while copying file :" + e);
			e.printStackTrace();
		}
	}		
}
