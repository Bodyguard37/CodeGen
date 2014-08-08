package unc.fcefyn.lac.tesisCaroFurey.herramientaPN;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author María Florencia Caro & Igancio Furey
 * Test d ela clase Tina.
 */
public class testTina {
	/**
	 * Para probar el método actualizarDatorRedPetri.
	 */	
	@Test
	public void testActualizarDatosRedPetri() {
		AbstractHerramientaPN tina = new Tina();
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos.pnml";
		tina.actualizarDatosRedPetri(path);
		
		//Analisis Alcanzabilidad
		assertTrue("Acotada", tina.getAcotada());
		assertTrue("Reversible", tina.getReversible());
		assertTrue("Vivacidad", tina.getVivacidad());
		assertFalse("Interbloqueo", tina.getInterbloqueo());
		
		final ArrayList<ArrayList<String>> invariantePlazasE =
				new ArrayList<ArrayList<String>>();
		invariantePlazasE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"P1_E1", "R2", })));
		invariantePlazasE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"P1_E2", "P2_E2", "R1", })));
		invariantePlazasE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"M1", "P1_E2", "P1_E3", "P2_E2", "P2_E3", })));
		invariantePlazasE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"P2_E1", "R3", })));
		
		final ArrayList<ArrayList<String>> invarianteTransicionesE =
				new ArrayList<ArrayList<String>>();
		invarianteTransicionesE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"P1_PtaHno", "P1_SalidaHno", "P1_disponible", "t7", })));
		invarianteTransicionesE.add(new ArrayList<String>(Arrays.asList(
				new String[]{"t3", "t4", "t5", "t6" })));	
	
		//Analisis estructural
		assertEquals("Invariantes Plazas", invariantePlazasE, tina.getInvariantesPlaza());
		assertEquals("Invariantes Transiciones", invarianteTransicionesE,
				tina.getInvariantesTransicion());
		tina.actualizarDatosRedPetri(path);
	}	
	/**
	 * Metodo convertirANDR.
	 */
	@Test
	public void testConvertirANDR() {
		final Tina tina = new Tina();
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos.pnml";
				
		final String nuevoArchivoE = "./src/test/resources/unc/fcefyn/lac" +
				"/tesisCaroFurey/Ejemplo2hilos.ndr";
			
		assertEquals("ArchivoNuevoConvertido", nuevoArchivoE, tina.convertirANDR(path));
	}
	/**
	 * Metodo MostrarEnFormatoNET().
	 */
	@Test
	public void testMostrarEnFormatoNET() {
		final Tina tina = new Tina();
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos.pnml";
		tina.mostrarEnFormatoNET(path);
		
		final String nuevoArchivoE = "net Ejemplo2hilos\n" +
				"tr P1_PtaHno : {<Robot1,Maquina1>} P1_E2 -> P1_E3 R1 \n" +
				"tr P1_SalidaHno : {<Maquina1,N>} P1_E3 -> M1 \n" +
				"tr P1_disponible : {<A,Robot1>} M1 P1_E1 R1 -> P1_E2 R2 \n" + 
				"tr t3 : {<Robot1,Maquina1>} P2_E2 -> R1 P2_E3 \n" + 
				"tr t4 : {<A,Robot1>} M1 R1 P2_E1 -> P2_E2 R3 \n" + 
				"tr t5 : {<Maquina1,N>} P2_E3 -> M1 \n" +
				"tr t6 : {<A,N>} R3 -> P2_E1 \n" + 
				"tr t7 : {<A,N>} R2 -> P1_E1 \n" + 
				"pl M1 (1)\n" +
				"pl R1 (1)\n" +
				"pl R2 (1)\n" +
				"pl R3 (1)\n";
		assertEquals("ArchivoNuevoConvertido", nuevoArchivoE, tina.mostrarEnFormatoNET(path));
	}
}
			
		
		


