/**
 * 
 */
package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.GeneradorCodigo;
import unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.GenerarCodigoJava;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;

/**
 * Clase de prueba para la clase VentanaPrincipal.
 * @author Ignacio Furey.
 *
 */
public class VentanaPrincipalTest {
	/**
	 * Instancia de AbstractHerramientaPN necesaria para acceder a los datos
	 * de una red de Petri de ejemplo.
	 */
	private AbstractHerramientaPN hpn;
	/**
	 * Instancia de GeneradorCodigo.
	 */
	private GeneradorCodigo gc;
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
	 * Unidad bajo prueba.
	 */
	private VentanaPrincipal uut;
	/**
	 * caracter nueva linea.
	 */
	private final String nuevaLinea = "\n";

	/**
	 * Constructor de clase.
	 */
	public VentanaPrincipalTest() {
		
	}

	/**
	 * Precondiciones.
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		this.hpn = new Tina();
		this.gc = new GenerarCodigoJava();
		this.uut = new VentanaPrincipal(this.hpn, this.gc);
		this.uut.setActualPN(new File(this.pathRed));
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#VentanaPrincipal(unc.fcefyn.lac.tesisCaroFurey.
	 * herramientaPN.AbstractHerramientaPN, unc.fcefyn.lac.tesisCaroFurey.
	 * generacionCodigo.GeneradorCodigo)}.
	 */
	@Test
	public void testVentanaPrincipal() {
		
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#mostrarResultado(java.lang.String)}.
	 */
	@Test
	public void testMostrarResultado() {
		final String txt = new String("Texto a mostrar en el area de texto.");
		this.uut.limpiarResultados();
		this.uut.mostrarResultado(txt);
		assertEquals("Mostrar resultado", txt + this.nuevaLinea,
				this.uut.textArea.getText());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#limpiarResultados()}.
	 */
	@Test
	public void testLimpiarResultados() {
		this.uut.mostrarResultado("Algun texto");
		this.uut.limpiarResultados();
		assertEquals("Limpiar resultados", "", this.uut.textArea.getText());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#leerResultados()}.
	 */
	@Test
	public void testLeerResultados() {
		final String txt = new String("algun texto.");
		this.uut.limpiarResultados();
		this.uut.mostrarResultado(txt);
		assertEquals("Leer resultados", txt + this.nuevaLinea,
				this.uut.leerResultados());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#escribirResultadosenArchivo(java.io.File)}.
	 */
	@Test
	public void testEscribirResultadosenArchivo() {
		final String linea1 = new String("PrimeraLinea");
		final String linea2 = new String("SegundaLinea");
		this.uut.limpiarResultados();
		this.uut.mostrarResultado(linea1);
		this.uut.mostrarResultado(linea2);
		final File f = new File(this.pathDir + "pruebaGuardarResultados.txt");
		try {
			this.uut.escribirResultadosenArchivo(f);
		} catch (IOException ioe) {
			fail("IOException en escribirResultadosEnArchivo");
		}
		BufferedReader bf = null;

		try {
			final FileReader fr = new FileReader(f);
			bf = new BufferedReader(fr);
			assertEquals("renglones", linea1, bf.readLine());
			assertEquals("renglones", linea2, bf.readLine());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				bf.close();
				f.delete();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#setActualPN(java.io.File)}.
	 */
	@Test
	public void testSetActualPN() {
		//Prueba caso en que el File de la red sea null.
		this.uut.limpiarResultados();
		this.uut.setActualPN(null);
		assertEquals("setActualPN con File = null",
				"ERROR: archivo de red incorrecto" + this.nuevaLinea,
				this.uut.textArea.getText());
		//Prueba caso en que el File de la red sea correcto.
		this.uut.limpiarResultados();
		this.uut.setActualPN(new File(this.pathRed));
		assertNotEquals("setActualPN con File = null",
				"" + this.nuevaLinea,
				this.uut.textArea.getText());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#editarSimularPN()}.
	 * Prueba el metodo editarSimularPN en caso que el valor de actualPN sea
	 * erroneo.
	 */
	@Test
	public void testEditarSimularPN() {
		this.uut.limpiarResultados();
		this.uut.actualPN = new File("AZ:\\pathErroneo");
		this.uut.editarSimularPN();
		assertEquals("editarSimularPN path erroneo",
				"ERROR: Red de Petri no establecida o path incorrecto" +
						this.nuevaLinea,
				this.uut.textArea.getText());

		this.uut.limpiarResultados();
		this.uut.actualPN = null;
		this.uut.editarSimularPN();
		assertEquals("editarSimularPN path null",
				"ERROR: Red de Petri no establecida o path incorrecto" +
						this.nuevaLinea,
				this.uut.textArea.getText());
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#analisisAlcanzabilidad()}.
	 * Compara que el resultado tenga el primer y ultimo renglon esperados.
	 */
	@Test
	public void testAnalisisAlcanzabilidad() {
		final String primerRenglonEsperado = new String(
				"Tina version 3.1.0 -- 01/07/13 -- LAAS/CNRS");
		final String ultimoRenglonEsperado = new String(
				"ANALYSIS COMPLETED");
		this.uut.limpiarResultados();
		this.uut.analisisAlcanzabilidad();
		assertTrue("alcanzabilidad primer renglon",
				this.uut.textArea.getText().contains(primerRenglonEsperado));
		assertTrue("alcanzabilidad ultimo renglon",
				this.uut.textArea.getText().contains(ultimoRenglonEsperado));
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#analisisEstructural()}.
	 * Compara que el resultado tenga el primer y ultimo renglon esperados.
	 */
	@Test
	public void testAnalisisEstructural() {
		final String primerRenglonEsperado = new String(
				"Struct version 3.1.0 -- 01/07/13 -- LAAS/CNRS");
		final String ultimoRenglonEsperado = new String(
				"ANALYSIS COMPLETED");
		this.uut.limpiarResultados();
		this.uut.analisisEstructural();
		assertTrue("estructural primer renglon",
				this.uut.textArea.getText().contains(primerRenglonEsperado));
		assertTrue("estructural ultimo renglon",
				this.uut.textArea.getText().contains(ultimoRenglonEsperado));
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#matrizYMarcado()}.
	 * Compara que el resultado posea los "titulos" de matriz positiva, matriz
	 * negativa y marcado inicial.
	 */
	@Test
	public void testMatrizYMarcado() {
		final String positiva = new String("Matriz positiva:");
		final String negativa = new String("Matriz negativa:");
		final String marcado = new String("Marcado inicial:");
		this.uut.limpiarResultados();
		this.uut.matrizYMarcado();
		assertTrue("matriz y marcado, positiva",
				this.uut.textArea.getText().contains(positiva));
		assertTrue("matriz y marcado, negativa",
				this.uut.textArea.getText().contains(negativa));
		assertTrue("matriz y marcado, marcado",
				this.uut.textArea.getText().contains(marcado));
	}

	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.interfazGrafica.
	 * VentanaPrincipal#escribirCabeceraPNML(java.io.File)}.
	 */
	@Test
	public void testEscribirCabeceraPNML() {
		final File f1 = new File(this.pathDir + "\\cabeceraPNML.pnml");
		final File f2 = new File(this.pathDir + "\\cabeceraPNMLesperada.pnml");
		try {
			this.uut.escribirCabeceraPNML(f1);
		} catch (IOException ioe) {
			fail("crear archivo cabecera");
			ioe.printStackTrace();
		}
		//Compara el archivo generado con uno de prueba.
		//Como luego de instanciada uut no se modifica nada, el archivo de
		//prueba debería ser igual al recien generado.
		BufferedReader bf1 = null;
		BufferedReader bf2 = null;
		try {
			final FileReader fr1 = new FileReader(f1);
			final FileReader fr2 = new FileReader(f2);
	
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
				f1.delete();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}
}
