package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;

/**
 * Test para la clase interfazGrafica.DefinicionProcesos.
 * @author María Florencia Caro & Ignacio Furey.
 */
public class DefinicionProcesosTest {
	/**
	 * Instancia de AbstractHerramientaPN necesaria para acceder a los datos
	 * de una red de Petri de ejemplo.
	 */
	private AbstractHerramientaPN hpn;
	/**
	 * Path a directorio de trabajo.
	 */
	private final String pathDir = ".\\src\\test\\resources\\unc\\fcefyn\\" +
			"lac\\tesisCaroFurey\\testInterfazGrafica\\";
	/**
	 * Path a archivo de descripcion de la red.
	 */
	private final String pathRed =  this.pathDir + "testInterfazGrafica.pnml";
	/**
	 * Path a archivo de descripcion de procesos de la red.
	 */
	private final String pathProcesos = this.pathDir +
			"testInterfazGrafica_procesos.xml";
	/**
	 * Unidad bajo prueba.
	 */
	private DefinicionProcesos uut;

	/**
	 * Constructor de clase.
	 */
	public DefinicionProcesosTest() {		
	}
	/**
	 * Precondiciones.
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		this.hpn = new Tina();
		this.hpn.cargarRed(this.pathRed, this.pathProcesos);
		this.uut = new DefinicionProcesos(this.hpn);	
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#DefinicionProcesos(unc.fcefyn.lac.tesisCaroFurey.
	 * herramientaPN.AbstractHerramientaPN)}.
	 */
	@Test
	public void testDefinicionProcesos() {
		//Controla visibilidad del frame.
		assertTrue("Visibilidad de frame de DefinicionProcesos",
				this.uut.frmDefinicionProcesos.isVisible());
		//Controla que la cantidad de transiciones leida sea acorde a las de 
		//hpn.
		assertEquals("Cantidad de transiciones",
				this.hpn.getTransiciones().size(),
				this.uut.valoresTransiciones.size());
		//Controla que la cantidad de hilos leida sea acorde a las de 
		//hpn.
		assertEquals("Cantidad de hilos",
				this.hpn.getProcesos().size(),
				this.uut.hilosComboBox.getItemCount());
		assertEquals("Cantidad de hilos",
				this.hpn.getProcesos().size(),
				this.uut.definicionHilos.size());
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#setIdHiloActual()}.
	 * Prueba que, luego de establecer el idHiloActual, este correcto en todas
	 * las instancias de ValoresTransicion.
	 */
	@Test
	public void testSetIdHiloActual() {
		final String id = new String("nuevoHiloPrueba__inexistente++*$");
		this.uut.crearHilo(id, false);
		this.uut.hilosComboBox.setSelectedIndex(
				this.uut.hilosComboBox.getItemCount() - 1);
		this.uut.setIdHiloActual();
		final Iterator<ValoresTransicion> it =
				this.uut.valoresTransiciones.iterator();
		while (it.hasNext()) {
			assertEquals("SetIdHiloActual en todas las transiciones",
					id, it.next().getIdHiloActual());
		}
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#crearHilo(java.lang.String, boolean)}.
	 * y {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#removerHilo()}.
	 */
	@Test
	public void testCrearYRemoverHilo() {
		final String id = new String("nuevoHiloPrueba__inexistente++*$");
		this.uut.crearHilo(id, false);
		assertFalse("CrearHilo agregado en definicion hilos",
				this.uut.definicionHilos.get(id));
		this.uut.removerHilo(id);
		assertFalse("RemoverHilo en definicion hilos",
				this.uut.definicionHilos.containsKey(id));
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#idHiloExistente(java.lang.String)}.
	 */
	@Test
	public void testIdHiloExistente() {
		final String idInexistente = new String("nuevoHiloPrueba__inexistente++*$");
		final String idNuevo = new String("nuevoHiloPrueba__nuevo++*$");
		this.uut.crearHilo(idNuevo, false);
		assertTrue("prueba idHiloExistente true",
				this.uut.idHiloExistente(idNuevo));
		assertFalse("prueba idHiloExistente false",
				this.uut.idHiloExistente(idInexistente));
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * DefinicionProcesos#generarArchivoHilos()}.
	 */
	@Test
	public void testGenerarArchivoHilos() {
		this.uut.generarArchivoHilos();
		//Compara el archivo generado con uno de prueba.
		//Como luego de instanciada uut no se modifica nada, el archivo de
		//prueba debería ser igual al recien generado.
		BufferedReader bf1 = null;
		BufferedReader bf2 = null;
		try {
			final FileReader fr1 = new FileReader(this.pathProcesos);
			final FileReader fr2 = new FileReader(this.pathDir +
					"testInterfazGrafica_procesos_originalParaComparar.xml");
	
			bf1 = new BufferedReader(fr1);
			bf2 = new BufferedReader(fr2);

			String sCadena1 = bf1.readLine();
			String sCadena2 = bf2.readLine();

			while ((sCadena1 != null) && (sCadena2 != null)) {
				assertEquals("Compara Archivos", sCadena2, sCadena1);
				sCadena1 = bf1.readLine();
				sCadena2 = bf2.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				bf1.close();
				bf2.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}
}
