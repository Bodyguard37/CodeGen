package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorIntervalos;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * @author María Florencia Caro & Ignacio Furey
  */
public class ManejadorIntervalosTest {
	/**
	 * Test Verifica que se lean bien los valores de intervalo del archivo pnml
	 * de la red.
	 */
	@Test
	public void testManejadorIntervalos() {
		final String path = "./src/test/resources/unc/fcefyn/lac/" +
			"tesisCaroFurey/PNMLTest.pnml";
		// Verifico los valores del intervalo de tiempo de la primera transicion
		final ManejadorIntervalos manejador = new ManejadorIntervalos(path);
		final ArrayList<Intervalo> intervalos = manejador.getIntervalos();
		final Intervalo valorEsperado = new Intervalo("t1", "4", "9");
		final Intervalo valorLeido = intervalos.get(0);
		assertEquals(valorEsperado.getIdTransicion(),
				valorLeido.getIdTransicion());
		assertEquals(valorEsperado.getPrimerValor(),
				valorLeido.getPrimerValor());
		assertEquals(valorEsperado.getSegundoValor(),
				valorLeido.getSegundoValor());
		// Verifico los valores del intervalo de tiempo de la segunda transicion
		final Intervalo valorEsperado1 = new Intervalo("t2", "0", "2");
		final Intervalo valorLeido1 = intervalos.get(1);
		assertEquals(valorEsperado.getIdTransicion(),
				valorLeido.getIdTransicion());
		assertEquals(valorEsperado1.getPrimerValor(),
				valorLeido1.getPrimerValor());
		assertEquals(valorEsperado1.getSegundoValor(),
				valorLeido1.getSegundoValor());
	}
}
