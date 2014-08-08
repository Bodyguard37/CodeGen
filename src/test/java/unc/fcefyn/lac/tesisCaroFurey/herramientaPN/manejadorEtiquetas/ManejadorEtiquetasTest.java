package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorEtiquetas;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * test de la clase ManejadorEtiquetas.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ManejadorEtiquetasTest {

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorEtiquetas.ManejadorEtiquetas#ManejadorEtiquetas(java.lang.String)}.
	 */
	@Test
	public void testManejadorEtiquetas() {
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
			"tesisCaroFurey/PNMLTest.pnml";
		final ManejadorEtiquetas manejador = new ManejadorEtiquetas(path);
		final ArrayList<Etiqueta> etiquetas = manejador.getEtiquetas();
		final Etiqueta valorEsperado = new Etiqueta("t3", "D", "I");
		final Etiqueta valorLeido = etiquetas.get(2);
		assertEquals(valorEsperado.getIdTransicion(), valorLeido.getIdTransicion());
		assertEquals(valorEsperado.getPrimerValor(), valorLeido.getPrimerValor());
		assertEquals(valorEsperado.getSegundoValor(), valorLeido.getSegundoValor());
	}

}
