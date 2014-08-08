package unc.fcefyn.lac.tesisCaroFurey.herramientaPN;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import unc.fcefyn.lac.tesisCaroFurey.resourcesManager.ResourcesManager;

/**
 * Clase para ejecutar la herramienta Tina y obtener sus resultados.
 * @author María Florencia Caro & Ignacio Furey
 */

public class Tina extends AbstractHerramientaPN {

	/**
	 * URI para llamar al archivo binario ndrio.exe. Se setea en el constructor.
	 */
	private URI ndrioUri;
	/**
	 * URI para llamar al archivo binario struct.exe. Se setea en el constructor.
	 */
	private URI structUri;
	/**
	 * URI para llamar al archivo binario tina.exe. Se setea en el constructor.
	 */
	private URI tinaUri;
	/**
	 * URI para llamar al archivo binario nd.exe. Se setea en el constructor.
	 */
	private URI ndUri;
	/**
	 * Formatos soportados por defecto.
	 */
	private static final String[] DEFAULTFORMATOSSOPORTADOS = {"pnml", "xml",
		"ndr", "net", };

	/**
	 * Class constructor.
	 */
	public Tina() {
		//Establece los formatos soportados por Tina
		this.formatosSoportados = DEFAULTFORMATOSSOPORTADOS;
		//Establece el path por defecto de Tina
		URI jarUri;
		try {
			jarUri = ResourcesManager.getJarURI();
			this.structUri = ResourcesManager.getFile(jarUri, "tinaBinary/struct.exe");
			this.ndrioUri = ResourcesManager.getFile(jarUri, "tinaBinary/ndrio.exe");
			this.tinaUri = ResourcesManager.getFile(jarUri, "tinaBinary/tina.exe");
			this.ndUri = ResourcesManager.getFile(jarUri, "tinaBinary/nd.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/circo.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/dot.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/graphplace.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/kill.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/ktzio.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/libdl.dll");
			ResourcesManager.getFile(jarUri, "tinaBinary/libpolkag.dll");
			ResourcesManager.getFile(jarUri, "tinaBinary/libpolkai.dll");
			ResourcesManager.getFile(jarUri, "tinaBinary/libpolkal.dll");
			ResourcesManager.getFile(jarUri, "tinaBinary/ltl2ba.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/muse.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/neato.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/pathto.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/plan.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/play.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/plughelp.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/selt.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/sift.exe");
			ResourcesManager.getFile(jarUri, "tinaBinary/");
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
		}	
		
	}

	/**
	 * Abre la herramienta de edicion de redes de Petri para crear una nueva
	 * red de Petri.
	 * @param path path del archivo a abrir.
	 * @see unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN
	 * llamarEditor(java.lang.String)
	 */
	@Override
	public final void llamarEditor(final String path) {
		//Crea un arreglo de String con el path a ejecutar y los parametros,
		//luego ejecuta.
		//Convierte el file al tipo de archivo nativo de TINA.
		final String nuevoPath = convertirANDR(path);
		final String[] comandos = new String[]{this.ndUri.getPath(), nuevoPath};
		ejecutarPrograma(comandos);
		//Una vez que finaliza, lo convierte nuevamente a formato pnml.
		convertirAPNML(nuevoPath);
	}
	/**
	 * Obtiene informacion referiada al analisis estructural y de
	 * alcanzabilidad generado por TINA.
	 * @param path Path al archivo de la red que se desea analizar.
	 * @see unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN
	 * actualizarDatosRedPetri(java.lang.String)
	 */
	@Override
	public void actualizarDatosRedPetri(final String path) {
		this.realizarAnalisisAlcanzabilidadPN(path);
		this.realizarAnalisisEstructuralPN(path);
	}
	/**
	 * Convierte desde otros formatos de redes a formato PNML.
	 * @param archivo Path del archivo de red de petri a convertir
	 * @return Path al nuevo archivo.
	 * @see unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN#
	 * convertirAPNML(java.lang.String)
	 */
	@Override
	public String convertirAPNML(final String archivo) {
		String nuevoArchivo;
		nuevoArchivo = archivo.substring(0, archivo.lastIndexOf('.')) + ".pnml";
		final String[] comandos = new String[]{this.ndrioUri.getPath(), archivo,
			nuevoArchivo, };
		final Process p = ejecutarPrograma(comandos);
		System.out.println(leerSalidaPrograma(p));
		return nuevoArchivo;
	}
	/**
	 * Convierte desde otros formatos de redes a formato NDR. Se utiliza por 
	 * NDR es el formato nativo de TINA:
	 * @param archivo Path del archivo de red de petri a convertir
	 * @return Path al nuevo archivo.
	 */
	public String convertirANDR(final String archivo) {
		String nuevoArchivo;
		nuevoArchivo = archivo.substring(0, archivo.lastIndexOf('.')) + ".ndr";
		final String[] comandos = new String[]{this.ndrioUri.getPath(), archivo,
			nuevoArchivo, };
		final Process p = ejecutarPrograma(comandos);
		System.err.println(leerSalidaPrograma(p));
		return nuevoArchivo;		
	}
	/**
	 * Utiliza la herramienta de conversion de formatos de red de Petri para
	 * obtener una red dada en algun formato soportado en formato de texto NET.
	 * @param archivoFuente Red de Petri que se desea mostrar.
	 * @return Texto con la descripcion grafica de la Red de Petri.
	 * @see unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN
	 * mostrarEnFormatoNET(java.lang.String)
	 */
	@Override
	public String mostrarEnFormatoNET(final String archivoFuente) {
		//Crea un arreglo de String con el path a ejecutar y los parametros,
		//luego ejecuta.
		final String[] comandos = new String[]{this.ndrioUri.getPath(), archivoFuente,
			"-net", };
		final Process p = ejecutarPrograma(comandos);
		//Devuelve el resultado leido de la salida del proceso
		return leerSalidaPrograma(p);
	}
	/**
	 * Utiliza la herramienta de analisis estructural de Redes de Petri para
	 * obtener un analisis estructural de la Red de Petri dada.
	 * @param archivoFuente Red de Petri a analizar.
	 */
	private void realizarAnalisisEstructuralPN(final String archivoFuente) {
		//Crea un arreglo de String con el path a ejecutar y los parametros,
		//luego ejecuta.
		this.invariantesPlaza = new ArrayList<ArrayList<String>>();
		this.invariantesTransicion = new ArrayList<ArrayList<String>>();
		final String[] comandos = new String[]{this.structUri.getPath(), /*"-q",*/
			archivoFuente, };
		
		final Process p = ejecutarPrograma(comandos);
		final String resultadoEstructural = leerSalidaPrograma(p);
		this.analisisEstructural = resultadoEstructural;
		//INVARIANTES
		String renglon;
		//genera un beffedReader para poder leer renglon por renglon.
		final BufferedReader lector = new BufferedReader(
			new StringReader(this.analisisEstructural));
		try {
			//Obtiene la primera linea y sigue avanzado linea a linea hasta
			//encontrar el titulo de invariantes de plaza.
			renglon = lector.readLine();
			while (!(renglon.contains("P-SEMI-FLOWS") ||
				renglon.contains("P-FLOWS"))) {
				renglon = lector.readLine();
			}
			renglon = lector.readLine();
			//Continua leyendo todos los invariantes de plaza hasta encontrar el
			//titulo de invariantes de transicion.
			boolean noInvariante = false;
			while (!(renglon.contains("T-SEMI-FLOWS") ||
				renglon.contains("T-FLOWS"))) {
				//crea un arraylist para un nuevo invariante.
				final ArrayList<String> invariante = new ArrayList<String>();
				//separa el contenido del renglon por los espacios y agrega
				//cada palabra a un ArrayList
				if (renglon.equals("")) {
					if (noInvariante) {
						noInvariante = false;
					} else {
						noInvariante = true;
					}
				} else {
					if (!noInvariante) {
						invariante.addAll(Arrays.asList(renglon.split(" ")));
						//Agrega el invariante al ArrayList de invariantes y continua
						//con el proximo renglon.
						this.invariantesPlaza.add(invariante);
					}
				}
				renglon = lector.readLine();
			}
			renglon = lector.readLine();
			noInvariante = false;
			while (!(renglon.contains("ANALYSIS COMPLETED"))) {
				//crea un arraylist para un nuevo invariante.
				ArrayList<String> invariante = new ArrayList<String>();
				//separa el contenido del renglon por los espacios y agrega
				//cada palabra a un ArrayList
				if (renglon.equals("")) {
					if (noInvariante) {
						noInvariante = false;
					} else {
						noInvariante = true;
					}
				} else {
					if (!noInvariante) {
						invariante.addAll(Arrays.asList(renglon.split(" ")));
						//Agrega el invariante al ArrayList de invariantes y continua
						//con el proximo renglon.
						this.invariantesTransicion.add(invariante);
					}
				}
				renglon = lector.readLine();
			}
		} catch (IOException e) {
			this.invariantesTransicion.clear();
			this.invariantesPlaza.clear();
			e.printStackTrace();
		}
	}
	/**
	 * Utiliza la herramienta de analisis de alcanzabilidad de Redes de Petri
	 * para obtener un analisis de alcanzabilidad de la Red de Petri dada.
	 * @param archivoFuente Red de Petri a analizar.
	 */
	private void realizarAnalisisAlcanzabilidadPN(final String archivoFuente) {
		//Crea un arreglo de String con el path a ejecutar y los parametros
		final String[] comandos = new String[]{this.tinaUri.getPath(), "-q", archivoFuente};
		//Ejecuta
		final Process p = ejecutarPrograma(comandos);
		final String resultadoAlcanzabilidad = leerSalidaPrograma(p);
		this.analisisAlcanzabilidad = resultadoAlcanzabilidad;
		try {
			//ACOTAMIENTO
			if (resultadoAlcanzabilidad.contains("possibly bounded")) {
				this.acotada = null;			
			} else {
				if (resultadoAlcanzabilidad.contains("net unbounded")) {
					this.acotada = new Boolean(false);
				} else {
					this.acotada = new Boolean(true);
				}
			}
			//REVERSIBILIDAD
			if (resultadoAlcanzabilidad.contains("possibly reversible")) {
				this.reversible = null;
			} else {
				if (resultadoAlcanzabilidad.contains("not reversible")) {
					this.reversible = new Boolean(false);
				} else {
					this.reversible = new Boolean(true);
				}
			}
			//VIVACIDAD
			if (resultadoAlcanzabilidad.contains("possibly live")) {
				this.vivacidad = null;
			} else {
				if (resultadoAlcanzabilidad.contains("not live")) {
					this.vivacidad = new Boolean(false);
				} else {
					this.vivacidad = new Boolean(true);
				}
			}
			//INTERBLOQUEO
			//obtengo el indice del renglon donde esta esta propiedad.
			int index = resultadoAlcanzabilidad.lastIndexOf("dead classe(s),");
			if (index < 0) {
				index = resultadoAlcanzabilidad.lastIndexOf("dead marking(s),");
			}
			//Con este indice busco el numero que indica las clases con deadlock
			//Si es distinto de cero, la red posee interbloqueo.
			if (resultadoAlcanzabilidad.charAt(index - 2) != '0') {
				this.interbloqueo = new Boolean(true);
			} else {
				//Si es cero, existen dos opciones, o el numero de clases con
				//interbloqueo en multiplo de diez, o no hay interbloqueo.
				//Busco un digito mas atras, si es "endLine", significa que el numero
				//termino y por lo tanto la red no posee interbloqueo. De lo
				//contrario, la red posee interbloqueo.
				if (resultadoAlcanzabilidad.charAt(index - 3) == '\n') {
					this.interbloqueo = new Boolean(false);
				} else {
					this.interbloqueo = new Boolean(true);
				}
			}
		} catch (java.lang.StringIndexOutOfBoundsException
				| NullPointerException e) {
			this.acotada = new Boolean(false);
			this.reversible = new Boolean(false);
			this.vivacidad = new Boolean(false);
			this.interbloqueo = new Boolean(true);
		}
	}
	/**
	 * Ejecuta un programa segun el path y parametros pasados en comandos,
	 * captura las excepciones e indica en caso de error.
	 * @param comandos Arreglo de String con el path del ejecutable y los
	 * parametros a pasar.
	 * @return proceso ejecutado
	 */
	private Process ejecutarPrograma(final String[] comandos) {
		Process p = null;
		try	{
		   	//Prueba ejecutar
			p = Runtime.getRuntime().exec(comandos);
			p.waitFor();
		} catch (SecurityException e) {
			//Captura excepciones y reporta error.
			JOptionPane.showMessageDialog(null, "Permiso denegado", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null, "Error de I/O, probablemente " +
				"el" + " archivo no exista", "ERROR de I/O",
					JOptionPane.ERROR_MESSAGE);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "El path a ejecutar esta" +
				"vacio o path incorrecto", "ERROR de path",
				JOptionPane.ERROR_MESSAGE);
		}
		return p;
	}
	/**
	 * Lee el resultado de un Process ejecutado y lo devuelve en formato String.
	 * @param p proceso ejecutado.
	 * @return String con el resultado obtenido en STDOUT del programa ejecutado
	 */
	private String leerSalidaPrograma(final Process p) {
		String salida = new String("");
		//Si el proceso es null, se retorna un String vacio
		if (p == null) {
			return salida;
		}
		// Se obtiene el stream de salida del programa
        final InputStream is = p.getInputStream();
        //Se prepara un bufferedReader para poder leer la salida más comodamente
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
        	// Se lee la primera linea
        	String aux = br.readLine();
        	// Mientras se haya leido alguna linea
        	while (aux != null) {
        		//Se agrega la linea leida en salida
        		salida = salida + aux + "\n";
        		// y se lee la siguiente.
        		aux = br.readLine();
        	}
        } catch (IOException e) {
        	// Excepciones si hay algún problema al leer su salida.
        	e.printStackTrace();
        }
        return salida;
	}
}
