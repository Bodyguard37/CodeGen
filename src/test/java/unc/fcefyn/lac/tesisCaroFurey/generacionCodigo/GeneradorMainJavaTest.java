package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;

/**
 * Test para clase generacionCodigo.GeneradorMainJava.java.
 * @author María Florencia Caro & Ignacio Furey
 */
public class GeneradorMainJavaTest {
	/**
	 * Instancia de AbstractHerramientaPN necesaria para acceder a los datos
	 * de una red de Petri de ejemplo.
	 */
	private AbstractHerramientaPN hpn;
	/**
	 * Path a directorio de trabajo.
	 */
	private final String pathDir = ".\\src\\test\\resources\\unc\\fcefyn\\" +
			"lac\\tesisCaroFurey\\testGeneradorCodigo\\";
	/**
	 * Path a archivo de descripcion de la red.
	 */
	private final String pathRed =  this.pathDir + "testGeneradorCodigo.pnml";
	/**
	 * Path a archivo de descripcion de procesos de la red.
	 */
	private final String pathProcesos = this.pathDir +
			"testGeneradorCodigo_procesos.xml";

	/**
	 * Precondiciones.
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		this.hpn = new Tina();
		this.hpn.cargarRed(this.pathRed, this.pathProcesos);
		new GeneradorMainJava(
				this.pathDir,
				this.hpn,
				"Path\\Archivos\\Config");
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.
	 * GeneradorMainJava#GeneradorMainJava(java.lang.String, unc.fcefyn.lac.
	 * tesisCaroFurey.herramientaPN.AbstractHerramientaPN, java.lang.String)}.
	 * Este metodo de test controla que el archivo fuente generado por
	 * generacionCodigo.GeneradorMainJava.java contenga ciertos puntos
	 * imopartantes. No controla el archivo en su totalidad.
	 */
	@Test
	public void testGeneradorMainJava() {
		boolean importProcesador = false;
		boolean pathConfigFile = false;
		boolean instanciaMain = false;
		boolean interfazMain = false;
		int abreYCierraLlaves = 0;
		int cantidadTransiciones = 0;

		final File f = new File(this.pathDir + "\\Main.java");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
			String linea;
			while (br.ready()) {
				linea = br.readLine();
				//Controla si se importa correctamente la clase ProcesadorPetri.
				if (!importProcesador) {
					importProcesador = linea.contains(
							"import procesador.ProcesadorPetri;");
				}
				//Controla si se define correctamente la constante con el path
				//al archivo de configuracion.
				if (!pathConfigFile) {
					pathConfigFile = linea.contains(
							"private static final String CONFIGFILE = " +
							"\" Path");
				}
				//Controla si se genera una instancia de Main.
				if (!instanciaMain) {
					instanciaMain = linea.contains(
							"Main m = new Main();");
				}
				//Controla la definicion correcta de la interfas de Main.
				if (!interfazMain) {
					interfazMain = linea.contains(
							"public Main() {");
				}
				//Obtiene la cantidad de transiciones definida en el archivo
				//fuente.
				if (linea.contains(
						"private static final int CANTIDADTRANSICIONES = ")) {
					cantidadTransiciones = Integer.parseInt(
							linea.substring(linea.lastIndexOf('=') + 2,
									linea.lastIndexOf(';')));
				}
				//Controla llaves
				abreYCierraLlaves = abreYCierraLlaves + controlLlaves(linea);
			}
			br.close();
			f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue("Import ProcesadorPetri", importProcesador);
		assertTrue("Path configFile", pathConfigFile);
		assertTrue("Instancia Main", instanciaMain);
		assertTrue("Interfas Main", interfazMain);
		assertEquals(0, abreYCierraLlaves);
		assertEquals(this.hpn.getTransiciones().size(), cantidadTransiciones);
	}
	/**
	 * Controla que haya las misma cantidad de '{' que de '}',
	 * se supone un correcto estilo de escritura en donde en un
	 * mismo renglon solo ocurre un '{' o un '}'.
	 * @param linea linea a controlar.
	 * @return sumatoria de llaves para la linea.
	 */
	private int controlLlaves(final String linea) {
		int count = 0;
		if (linea.contains("{")) {
			count = count + 1;
		}
		if (linea.contains("}")) {
			count = count - 1;
		}
		return count;
	}
}
