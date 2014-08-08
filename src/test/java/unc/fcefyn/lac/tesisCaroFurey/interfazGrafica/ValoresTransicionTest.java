package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

/**
 * Test para la clase ValoresTransicion.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ValoresTransicionTest {
	/**
	 * Objeto de prueba.
	 */
	private ValoresTransicion ut;
	/**
	 * id para hilo disparo de prueba.
	 */
	private final String idDeDisparo = "idDisparo";
	/**
	 * id para hilo informe de prueba.
	 */
	private final String idDeInforme = "idInforme";	
	/**
	 * Constructor.
	 */
	public ValoresTransicionTest() {		
	}
	/**
	 * precondiciones de test.
	 */
	@Before
	public void setUp() {
		this.ut = new ValoresTransicion(0, new JPanel(), 10, "idTransicion");
		this.ut.setIdHiloDisparo(this.idDeDisparo);
		this.ut.setIdHiloInforme(this.idDeInforme);
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * 		ValoresTransicion#setIdHiloActual(java.lang.String)}.
	 */
	@Test
	public void testSetIdHiloActualEqualIdHiloDisparo() {
		//Prueba estableciendo hiloActual = hiloDisparo.
		this.ut.setIdHiloActual(this.idDeDisparo);
		//Prueba que los campos de "Disparo" se establezcan como habilitados.
		assertTrue("Prueba jCheckBoxDisparo selected",
				this.ut.getDisparo().isSelected());
		assertTrue("Prueba jCheckBoxDisparo enable",
				this.ut.getDisparo().isEnabled());
		assertTrue("Prueba jComboBoxDisparo enable",
				this.ut.getOrdenDisparo().isEnabled());
		assertTrue("Prueba jTextFieldDisparo enable",
				this.ut.getMetodoDisparo().isEnabled());

		//Prueba que los campos de "Informe" se establezcan como de.
		assertFalse("Prueba jCheckBoxInforme not selected",
				this.ut.getInforme().isSelected());
		assertFalse("Prueba jCheckBoxInforme not enable",
				this.ut.getInforme().isEnabled());
		assertFalse("Prueba jComboBoxInforme not enable",
				this.ut.getOrdenInforme().isEnabled());
		assertFalse("Prueba jTextFieldInforme not enable",
				this.ut.getMetodoInforme().isEnabled());
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * 		ValoresTransicion#setIdHiloActual(java.lang.String)}.
	 */
	@Test
	public void testSetIdHiloActualEqualIdHiloInforme() {
		//Prueba estableciendo hiloActual = hiloInforme.
		this.ut.setIdHiloActual(this.idDeInforme);
		//Prueba que los campos de "Informe" se establezcan como habilitados.
		assertTrue("Prueba jCheckBoxInforme selected",
				this.ut.getInforme().isSelected());
		assertTrue("Prueba jCheckBoxInforme enable",
				this.ut.getInforme().isEnabled());
		assertTrue("Prueba jComboBoxInforme enable",
				this.ut.getOrdenInforme().isEnabled());
		assertTrue("Prueba jTextFieldInforme enable",
				this.ut.getMetodoInforme().isEnabled());

		//Prueba que los campos de "Disparo" se establezcan como de.
		assertFalse("Prueba jCheckBoxDisparo not selected",
				this.ut.getDisparo().isSelected());
		assertFalse("Prueba jCheckBoxDisparo not enable",
				this.ut.getDisparo().isEnabled());
		assertFalse("Prueba jComboBoxDisparo not enable",
				this.ut.getOrdenDisparo().isEnabled());
		assertFalse("Prueba jTextFieldDisparo not enable",
				this.ut.getMetodoDisparo().isEnabled());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * 		ValoresTransicion#removerHilo(java.lang.String)}.
	 */
	@Test
	public void testRemoverHilo() {
		this.ut.removerHilo(this.idDeDisparo);
		this.ut.removerHilo(this.idDeInforme);
		assertEquals("Disparo vacio", "", this.ut.getIdHiloDisparo());
		assertEquals("Informe vacio", "", this.ut.getIdHiloInforme());
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * 		ValoresTransicion#generarTransicionHilo(java.lang.String)}.
	 */
	@Test
	public void testGenerarTransicionHilo() {
		assertTrue("Prueba disparo transicionProceso",
				this.ut.generarTransicionHilo(this.idDeDisparo).getDisparo());
		assertFalse("Prueba informe transicionProceso",
				this.ut.generarTransicionHilo(this.idDeDisparo).getInforme());
	}

}
