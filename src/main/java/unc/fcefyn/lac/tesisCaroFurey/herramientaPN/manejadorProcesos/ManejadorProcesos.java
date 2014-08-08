package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Se encarga de escribir y leer el archivo XML de definicion de procesos para agregar,
 * actualizar, borrar y obtener informacion de los procesos del sistema definidos por
 * el usuario. Mientras realiza la lectura del archivo XML instancia un
 *  objeto de la clase Proceso, por cada proceso que lee, agregandole una instancia de
 *  la clase TransicionProceso por cada transicion que forma parte del mismo.
 *@author María Florencia Caro & Ignacio Furey
 */
public class ManejadorProcesos {
	/**
	 * Establece la cabecera del archivo de procesos xml vacio.
	 */
	private static final String CABECERAXML =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"" +
			" standalone=\"no\"?>" +
			"<definicionProcesos>" +
				"<net id=\"net1\">" +
					"<name>" +
						"<text>nombre de red</text>" +
					"</name>" +
				"</net>" +
			"</definicionProcesos>";

	/**
	 * Lista de Procesos.
	 */
	protected ArrayList<Proceso> procesos;
	/**
	 * Path del archivo.
	 */
	private String path;
	/**
	 * Le pasa el path del archivo a leer y lo lee.
	 * @param pathAXMLProcesos path al archivo xml a leer
	 */
	public ManejadorProcesos(final String pathAXMLProcesos) {
		this.path = pathAXMLProcesos;
		this.leerProcesosDesdeXML(pathAXMLProcesos);
	}
	/**
	 * Getter.
	 * @return procesos
	 */
	public ArrayList<Proceso> getProcesos() {
		return this.procesos;
	}
	/**
	 * Agregar un proceso al archivo de procesos de la red y a la variable de 
	 * procesos de HerramientaPN.
	 * @param proceso Proceso a agregar.
	 */
	public final void agregarProceso(final Proceso proceso) {
		this.agregarProceso(proceso, this.path);
	}
	/**
	 * Agregar un proceso al archivo de procesos de la red y a la variable de 
	 * procesos de HerramientaPN.
	 * @param proceso Proceso a agregar.
	 * @param pathAXMLProcesos path del archivo.
	 */
	public final void agregarProceso(final Proceso proceso, 
			final String pathAXMLProcesos) {
		final Document doc = abrirArchivoProcesosXML(pathAXMLProcesos);
		//Controla que no exista un proceso con el mismo id.
		if (existeProceso(doc, proceso.getId())) {
			JOptionPane.showMessageDialog(null,
					"Ya existe un proceso con ese id.",
					"ERROR", JOptionPane.ERROR_MESSAGE);
		} else {
			final NodeList red = doc.getElementsByTagName("net");
			//Se genera el nuevo elemento proceso
	        final Element procesoNuevo = doc.createElement("process");
	        procesoNuevo.setAttribute("id", proceso.getId());
	        procesoNuevo.setAttribute("runnable",
	        		Boolean.toString(proceso.isRunnable()));
	        //creo un iterator a partir de la lista de transicionesDisparo
	        //del proceso
	        final Iterator<TransicionProceso> it = proceso.getTransiciones().iterator();
	        //creo un iterator a partir de la lista de transiciones
	        //del proceso
	        while (it.hasNext()) {
	        	//creo un nuevo nodo transicion.
		    	final Element transicionNueva = doc.createElement("transition");
		    	//al nodo transicion le agrego texto con el id de transicion.
		    	final TransicionProceso auxTransicion = it.next();
		    	transicionNueva.setAttribute(
		    			"id", auxTransicion.getIdTransicion());
		    	//Creo los nodos disparo y informe.
		    	if (auxTransicion.getDisparo()) {
		    		final Element disparo = doc.createElement("disparo");

		    		final Element metodo = doc.createElement("metodo");
		    		final Text textoMetodo = doc.createTextNode(
			    			auxTransicion.getMetodoDisparo());
			    	metodo.appendChild(textoMetodo);
			    	disparo.appendChild(metodo);

			    	final Element orden = doc.createElement("orden");
			    	final Text textoOrden = doc.createTextNode(
			    			auxTransicion.getOrdenDisparo());
			    	orden.appendChild(textoOrden);
			    	disparo.appendChild(orden);
			    	transicionNueva.appendChild(disparo);
		    	}
		    	if (auxTransicion.getInforme()) {
		    		final Element informe = doc.createElement("informe");

		    		final Element metodo = doc.createElement("metodo");
		    		final Text textoMetodo = doc.createTextNode(
			    			auxTransicion.getMetodoInforme());
			    	metodo.appendChild(textoMetodo);
			    	informe.appendChild(metodo);

			    	final Element orden = doc.createElement("orden");
			    	final Text textoOrden = doc.createTextNode(
			    			auxTransicion.getOrdenInforme());
			    	orden.appendChild(textoOrden);
			    	informe.appendChild(orden);
			    	transicionNueva.appendChild(informe);
		    	}
		    	//Agrego el nodo de transicion al proceso nuevo.
		    	procesoNuevo.appendChild(transicionNueva);
	        }
	        try {
		        //Por ultimo agrego el proceso al nodo red del archivo.
		        red.item(0).appendChild(procesoNuevo);
	        } catch (Exception e) {
	        	JOptionPane.showMessageDialog(null, "Archivo xml de procesos " +
	        		"con formato erroneo o vacio. " +
	        		"No se encontro nodo <net>",
						"ERROR", JOptionPane.ERROR_MESSAGE);
	        }
	        //Guarda los cambios en el archivo.
	        guardarDatosArchivoProcesosXML(doc, pathAXMLProcesos);
			//	Agregar el proceso al atributo de procesos.
			this.procesos.add(proceso);
		}
	}
	/**
	 * Remover un proceso.
	 * @param proceso Proceso a remover.
	 * @param pathAXMLProcesos path del archivo que tiene el proceso que hay que
	 *  borrar.
	 */
	public final void borrarProceso(final Proceso proceso, 
			final String pathAXMLProcesos) {
		final Document doc = abrirArchivoProcesosXML(pathAXMLProcesos);
		final NodeList red = doc.getElementsByTagName("net");
		final NodeList nodosProcesos = doc.getElementsByTagName("process");
		for (int i = 0; i < nodosProcesos.getLength(); i = i + 1) {
			final Element proc = (Element) nodosProcesos.item(i);
			if (proc.getAttribute("id").equals(proceso.getId())) {
				try {
					red.item(0).removeChild(proc);
				} catch (NullPointerException e1) {
					System.err.println("Error borrando proceso en XML, no " +
						"hay nodo <net>.");
				}
			}
		}
		//Guarda los cambios en el archivo.
        guardarDatosArchivoProcesosXML(doc, pathAXMLProcesos);
		//	BORRAR el proceso del array de procesos
		this.procesos.remove(proceso);
	}
	/**
	 * Actualizar un proceso.
	 * @param procesoViejo Proceso a actualizar.
	 * @param procesoNuevo Proceso que contiene los nuevos datos.
	 * @param pathAXMLProcesos path al archivo xml
	 */
	public final void actualizarProceso(final Proceso procesoViejo,
		final Proceso procesoNuevo, final String pathAXMLProcesos) {

		this.borrarProceso(procesoViejo, pathAXMLProcesos);
		this.agregarProceso(procesoNuevo, pathAXMLProcesos);
	}
	/**
	 * Lee los procesos definidos en el archivo XML dado.
	 * @param pathAXMLProcesos path al archivo.
	 */
	private void leerProcesosDesdeXML(final String pathAXMLProcesos) {
		this.procesos = new ArrayList<Proceso>();
		final File xmlFile = new File(pathAXMLProcesos); 
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException | IOException e1) {
			crearArchivoProcesosXML(pathAXMLProcesos);
			try {
				doc = dBuilder.parse(xmlFile);
			} catch (SAXException | IOException e2) {
				e2.printStackTrace();
			}
		}

		doc.getDocumentElement().normalize();

		//Nodo "process"
		final NodeList procesosLeidos = doc.getElementsByTagName("process");

		for (int temp = 0; temp < procesosLeidos.getLength(); temp = temp + 1) {
			final Node process = procesosLeidos.item(temp);
			if (process.getNodeType() == Node.ELEMENT_NODE) {
				final Element eElement1 = (Element) process;
				final Proceso procesoActual = new 
						Proceso(eElement1.getAttribute("id"),
								Boolean.parseBoolean(
										eElement1.getAttribute("runnable")));
				//Nodo "transition"
				final NodeList transiciones = procesosLeidos.item(temp).getChildNodes();
				//recorro todas las transiciones del proceso.
				for (int temp1 = 0; temp1 < transiciones.getLength(); temp1 = temp1 + 1) {
					final Node transicion = transiciones.item(temp1);
					if (transicion.getNodeType() == Node.ELEMENT_NODE) {
						final Element eElement2 = (Element) transicion;
						//Nueva TransicionProceso con el id de transicion
						final TransicionProceso transicionActual =
								new TransicionProceso(
										eElement2.getAttribute("id"));
						//Busco el nodo disparo dentro de la transicion
						final NodeList nodosDisparo =
								eElement2.getElementsByTagName("disparo");
						try {
							final Element disparoTransicion =
									(Element) nodosDisparo.item(0);
							//busco el metodo de disparo.
							final NodeList metodo =
									disparoTransicion.
										getElementsByTagName("metodo");
							transicionActual.setDisparo(true);
							try {
								//Seteo el metodo en la TransicionActual
								transicionActual.setMetodoDisparo(
										metodo.item(0).getTextContent());
							} catch (NullPointerException e) {
								transicionActual.setMetodoDisparo("");
							}
							//Busco el orden de disparo.
							final NodeList orden =
									disparoTransicion.
										getElementsByTagName("orden");
							try {
								//Seteo el orden en la TransicionActual
								transicionActual.setOrdenDisparo(
									orden.item(0).getTextContent());
							} catch (NullPointerException e) {
								transicionActual.setOrdenDisparo("");
							}
						} catch (NullPointerException e) {
							transicionActual.setDisparo(false);
						}

						//Busco el nodo informe dentro de la transicion
						final NodeList nodosInforme =
								eElement2.getElementsByTagName("informe");
						try {
							final Element informeTransicion =
									(Element) nodosInforme.item(0);
							//busco el metodo de disparo.
							final NodeList metodo =
									informeTransicion.
										getElementsByTagName("metodo");
							transicionActual.setInforme(true);
							try {
								//Seteo el metodo en la TransicionActual
								transicionActual.setMetodoInforme(
										metodo.item(0).getTextContent());
							} catch (NullPointerException e) {
								transicionActual.setMetodoInforme("");
							}
							//busco el orden de Informe.
							final NodeList orden =
									informeTransicion.
										getElementsByTagName("orden");
							try {
								//Seteo el orden en la TransicionActual
								transicionActual.setOrdenInforme(
										orden.item(0).getTextContent());
							} catch (NullPointerException e) {
								transicionActual.setOrdenInforme("");
							}
						} catch (NullPointerException e) {
							transicionActual.setInforme(false);
						}
						procesoActual.addTransicion(transicionActual);
					}
				}
				this.procesos.add(procesoActual);
			}
		}
	}
	/**
	 * Abre un archivo XML para escritura o lectura.
	 * @param filePath El path del archivo que se desea abrir.
	 * @return instancia de Document con el para parsear el archivo.
	 */
	private Document abrirArchivoProcesosXML(final String filePath) {
        File xmlFile = null;
        final DocumentBuilderFactory dbFactory =
        		DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
        	xmlFile = new File(filePath);
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);

        } catch (java.io.FileNotFoundException fe) {
        	fe.printStackTrace(); 
        }
        catch (SAXException | ParserConfigurationException
        		| IOException e1) {
            e1.printStackTrace();
        }
        doc.getDocumentElement().normalize();
		return doc;
	}
	/**
	 * Crea un archivo XML para escritura o lectura, agregando los datos
	 * minimos de un archivo XML que describa procesos de una red de Petri.
	 * @param filePath El path del archivo que se desea abrir.
	 * @return instancia de Document con el para parsear el archivo.
	 */
	public static Document crearArchivoProcesosXML(final String filePath) {
        File xmlFile = null;
        final DocumentBuilderFactory dbFactory =
        		DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        BufferedWriter bw = null;
        try {
        	xmlFile = new File(filePath);
        	xmlFile.createNewFile();

			bw = new BufferedWriter(new FileWriter(xmlFile));
			bw.write(CABECERAXML);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
        try {
        	dBuilder = dbFactory.newDocumentBuilder();
        	doc = dBuilder.parse(xmlFile);
	        doc.getDocumentElement().normalize();
        } catch (SAXException | ParserConfigurationException
        		| IOException e2) {
        	e2.printStackTrace();
        }
		return doc;
	}
	/**
	 * Guarda los datos establecidos en una instancia Document en un archivo
	 * cuyo path sera xmlFilePAth.
	 * @param doc el Document con datos a guardar.
	 * @param xmlFilePath path del archivo.
	 */
	private void guardarDatosArchivoProcesosXML(final Document doc,
			final String xmlFilePath) {
		try {
			doc.getDocumentElement().normalize();
	        final TransformerFactory transformerFactory =
	        		TransformerFactory.newInstance();
	     // añadimos sangrado y la cabecera de XML
	        transformerFactory.setAttribute("indent-number", new Integer(3));
	        final Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(
	        		"{http://xml.apache.org/xslt}indent-amount", "2");
	        final DOMSource source = new DOMSource(doc);
	        final StreamResult result = new StreamResult(new File(xmlFilePath));
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(source, result);
	    } catch (TransformerException e1) {
	        e1.printStackTrace();
	    }
	}
	/**
	 * Controla en el archivo XML de procesos si ya existe definido un proceso
	 * con el id dado.
	 * @param doc instancia de Document con la que se trabaja.
	 * @param idProceso id del proceso que se desea buscar.
	 * @return true si el proceso existe, false de lo contrario.
	 */
	private boolean existeProceso(final Document doc, final String idProceso) {
		final NodeList nodosProcesos = doc.getElementsByTagName("process");
		for (int i = 0; i < nodosProcesos.getLength(); i = i + 1) {
			final Element proc = (Element) nodosProcesos.item(i);
			if (proc.getAttribute("id").equals(idProceso)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Reempaza el archivo XML por uno nuevo con los datos dado por
	 * procesos nuevos.
	 * @param procesosNuevos nuevos datos.
	 */
	public void reemplazarArchivoXML(final ArrayList<Proceso> procesosNuevos) {
		//piso el archivo viejo
		ManejadorProcesos.crearArchivoProcesosXML(this.path);
		//agrego los procesos al archivo.
		final Iterator<Proceso> procIt = procesosNuevos.iterator();
		while (procIt.hasNext()) {
			this.agregarProceso(procIt.next());
		}
		this.leerProcesosDesdeXML(this.path);
	}
}
