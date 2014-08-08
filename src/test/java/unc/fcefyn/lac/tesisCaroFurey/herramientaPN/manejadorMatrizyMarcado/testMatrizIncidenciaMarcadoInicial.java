package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorMatrizyMarcado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author María Florencia Caro & Ignacio Furey
 */
public class testMatrizIncidenciaMarcadoInicial {

	/**
	 * Test .
	 */
	@Test
	public void testMatrizYMarcadoComoString() {
		final MatrizIncidenciaMarcadoInicial matriz = new MatrizIncidenciaMarcadoInicial();

		matriz.agregarPlaza("P1", 0);
		final Plaza p2 = new Plaza();
		p2.setId("P2");
		p2.agregarElementoAMatriz(matriz);

		matriz.agregarTransicion("T1");
		final Transicion t2 = new Transicion();
		t2.setId("T2");
		t2.agregarElementoAMatriz(matriz);

		matriz.agregarArco("A1", "P1", "T1", "1");
		matriz.agregarArco("A2", "P1", "T2", "2");

		final Arco a3 = new Arco();
		a3.setId("A3");
		a3.setSourceTarget("T1", "P2");
		a3.setValorElemento(Integer.parseInt(String.valueOf("3")));
		a3.agregarElementoAMatriz(matriz);

		final Arco a4 = new Arco();
		a4.setId("A4");
		a4.setSourceTarget("T2", "P2");
		a4.setValorElemento(Integer.parseInt(String.valueOf("4")));
		a4.agregarElementoAMatriz(matriz);

		matriz.crearMatriz();

		final String valorLeido = matriz.matrizYMarcadoComoString();
		final String valorEsperado = 
				"Matriz positiva:\n" +
				"0	0	\n" +
				"3	4	\n" +
				"\n" +
				"Matriz negativa:\n" +
				"1	2	\n" +
				"0	0	\n" +
				"\n" +
				"Marcado inicial:\n" +
				"0\n" +
				"0\n";

		assertEquals(valorEsperado, valorLeido);
	}
	@Test
	public void testSetSourceTargetEnPlazasTiraExcepcion() {
		final Plaza plaza = new Plaza();
		try {
			plaza.setSourceTarget("A", "B");
			fail();
		} catch (RuntimeException e) {
			final String valorEsperado = "Una plaza no deberia tener " +
				"campos source ni target";
			assertEquals(valorEsperado, e.getMessage());
		}
	}
	@Test
	public void testSetSourceTargetEnTransicionTiraExcepcion() {
		final Transicion transicion = new Transicion();
		try {
			transicion.setSourceTarget("A", "B");
			fail();
		} catch (RuntimeException e) {
			final String valorEsperado = "Una transicion no deberia tener " +
				"campos source ni target";
			assertEquals(valorEsperado, e.getMessage());
		}
	}
}
