package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
  * Test de la clase ManejadorProcesos.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ManejadorProcesosTest {
	/**
	 * procesos lista de procesos.
	 */
	private ArrayList<Proceso> procesos;
	/**
	 * valorLeidoProceso0 .
	 */
	private Proceso valorLeidoProceso0;
	/**
	 * valorEsperadoProceso1 .
	 */
	private Proceso valorEsperadoProceso1;
	/**
	 * valorEsperadoProceso0 .
	 */
	private Proceso valorEsperadoProceso0;
	/**
	 * valorLeidoProceso1 .
	 */
	private Proceso valorLeidoProceso1;

	@Before
	public void setUp() {
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos_procesos.pnml";
		final ManejadorProcesos manejador = new ManejadorProcesos(path);
		this.procesos = manejador.getProcesos(); 
		
		this.valorEsperadoProceso0 = new Proceso("Produccion1", false);
		this.valorEsperadoProceso1 = new Proceso("Produccion2", false);
		this.valorLeidoProceso0 = this.procesos.get(0);	
		this.valorLeidoProceso1 = this.procesos.get(1);		
	}
	/**
	 * Verificar que son leidos los 2 procesos que existen.
	 */
	@Test
	public void testVerificarQueDosProcesosSonLeidos() {
		assertEquals(this.valorEsperadoProceso0.getId(), this.valorLeidoProceso0.getId());
		assertEquals(this.valorEsperadoProceso1.getId(), this.valorLeidoProceso1.getId());
	}
	/**
	 * Para verificar que tanto el proceso 0 como el 1 tengan transiciones, acierta
	 * cuando la condicion es falsa, es decir si no esta vacio acierta en este caso.
	 */
	@Test
	public void testVerificarQueArrayTransicionesProceso0NoEsVacio() {
		assertFalse(this.valorLeidoProceso0.getTransiciones().isEmpty());
	}
	/**
	 * Para verificar que transicionesProceso no es vacio.
	 */	
	@Test
	public void testVerificarQueArrayTransicionesProceso1NoEsVacio() {
		assertFalse(this.valorLeidoProceso1.getTransiciones().isEmpty());
	}
	/**
	 * Verificar valores de transiciones.
	 */	
	@Test
	public void testValoresTransicionesProceso0() {
		final TransicionProceso transicionEsperada0 = new TransicionProceso("t0");
		transicionEsperada0.setMetodoDisparo(null); //
		transicionEsperada0.setOrdenDisparo(null);
		transicionEsperada0.setMetodoInforme("Imetodot0");
		transicionEsperada0.setOrdenInforme("2");
		this.valorEsperadoProceso0.addTransicion(transicionEsperada0);
		
		final TransicionProceso transicionEsperada1 = new TransicionProceso("t1");
		transicionEsperada1.setMetodoDisparo("Dmetodot1");
		transicionEsperada1.setOrdenDisparo("3");
		transicionEsperada1.setMetodoInforme("Imetodot1");
		transicionEsperada1.setOrdenInforme("1");
		this.valorEsperadoProceso0.addTransicion(transicionEsperada1);
		
		final TransicionProceso transicionEsperada2 = new TransicionProceso("t2");
		transicionEsperada2.setMetodoDisparo("Dmetodot2");
		transicionEsperada2.setOrdenDisparo("5");
		transicionEsperada2.setMetodoInforme("Imetodot2");
		transicionEsperada2.setOrdenInforme("6");
		this.valorEsperadoProceso0.addTransicion(transicionEsperada2);
		
		final TransicionProceso transicionLeida0 = this.valorLeidoProceso0.getTransiciones().get(0);
		final TransicionProceso transicionLeida1 = this.valorLeidoProceso0.getTransiciones().get(1);
		final TransicionProceso transicionLeida2 = this.valorLeidoProceso0.getTransiciones().get(2);
		
		assertEquals(transicionEsperada0.getIdTransicion(), transicionLeida0.getIdTransicion());
		assertEquals(transicionEsperada0.getMetodoDisparo(), transicionLeida0.getMetodoDisparo());
		assertEquals(transicionEsperada0.getOrdenDisparo(), transicionLeida0.getOrdenDisparo());
		assertEquals(transicionEsperada0.getMetodoInforme(), transicionLeida0.getMetodoInforme());
		assertEquals(transicionEsperada0.getOrdenInforme(), transicionLeida0.getOrdenInforme());
		
		assertEquals(transicionEsperada1.getIdTransicion(), transicionLeida1.getIdTransicion());
		assertEquals(transicionEsperada1.getMetodoDisparo(), transicionLeida1.getMetodoDisparo());
		assertEquals(transicionEsperada1.getOrdenDisparo(), transicionLeida1.getOrdenDisparo());
		assertEquals(transicionEsperada1.getMetodoInforme(), transicionLeida1.getMetodoInforme());
		assertEquals(transicionEsperada1.getOrdenInforme(), transicionLeida1.getOrdenInforme());
		
		assertEquals(transicionEsperada2.getIdTransicion(), transicionLeida2.getIdTransicion());
		assertEquals(transicionEsperada2.getMetodoDisparo(), transicionLeida2.getMetodoDisparo());
		assertEquals(transicionEsperada2.getOrdenDisparo(), transicionLeida2.getOrdenDisparo());
		assertEquals(transicionEsperada2.getMetodoInforme(), transicionLeida2.getMetodoInforme());
		assertEquals(transicionEsperada2.getOrdenInforme(), transicionLeida2.getOrdenInforme());
	}
	/**
	 * Valores transiciones de un proceso.
	 */	
	@Test
	public void testValoresTransicionesProceso1() {		
		final TransicionProceso transicionEsperada0 = new TransicionProceso("tA");
		transicionEsperada0.setMetodoDisparo("DmetodotA");
		transicionEsperada0.setOrdenDisparo("3");
		transicionEsperada0.setMetodoInforme(null);
		transicionEsperada0.setOrdenInforme(null);
		this.valorEsperadoProceso1.addTransicion(transicionEsperada0);
		final TransicionProceso transicionLeida0 = this.valorLeidoProceso1.getTransiciones().get(0);
		
		assertEquals(transicionEsperada0.getIdTransicion(), transicionLeida0.getIdTransicion());
		assertEquals(transicionEsperada0.getMetodoDisparo(), transicionLeida0.getMetodoDisparo());
		assertEquals(transicionEsperada0.getOrdenDisparo(), transicionLeida0.getOrdenDisparo());
		assertEquals(transicionEsperada0.getMetodoInforme(), transicionLeida0.getMetodoInforme());
		assertEquals(transicionEsperada0.getOrdenInforme(), transicionLeida0.getOrdenInforme());

	}	
}
