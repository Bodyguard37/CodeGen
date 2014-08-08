package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;

/**
 * Test para clase generacionCodigo.GeneradorRecursosJava.java.
 * @author María Florencia Caro & Ignacio Furey
 */
public class GeneradorRecursosJavaTest {
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
			"testGeneradorCodigo_procesos_pruebaGenRecursos.xml";
	/**
	 * Instancia de Proceso de prueba.
	 */
	private Proceso p;
	/**
	 * Precondiciones.
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		this.hpn = new Tina();
		this.hpn.cargarRed(this.pathRed, this.pathProcesos);
		//Selecciona un proceso runnable para la prueba, de no existir ningun
		//proceso runnable el test falla.
		final Iterator<Proceso> it = this.hpn.getProcesos().iterator();
		boolean existeProcesoNoRunnable = false;
		while (it.hasNext() && !existeProcesoNoRunnable) {
			this.p = it.next();
			if (!this.p.isRunnable()) {
				existeProcesoNoRunnable = true;
			}
		}
		if (!existeProcesoNoRunnable) {
			fail("Debe existir un proceso no runnable en el archivo de red " +
					"para ejecutar el test.");
		}
		new GeneradorRecursosJava(this.p, this.hpn, this.pathDir);
	}
	/**
	 * Test method for {@link unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.
	 * GeneradorMainJava#GeneradorMainJava(java.lang.String, unc.fcefyn.lac.
	 * tesisCaroFurey.herramientaPN.AbstractHerramientaPN, java.lang.String)}.
	 * 
	 * Este metodo de test controla que el archivo fuente generado por
	 * generacionCodigo.GeneradorRecursosJava.java contenga ciertos puntos
	 * imopartantes. No controla el archivo en su totalidad.
	 */
	@Test
	public void testGeneradorRecursosJava() {
		boolean importProcesador = false;
		boolean interfazProceso = false;
		boolean metodoConstructor = false;
		boolean metodoConsultar = false;
		boolean metodoDisparar = false;
		int abreYCierraLlaves = 0;

		final File f = new File(this.pathDir + "\\" + this.p.getId() + ".java");
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
				//Controla la interfaz de la clase generada.
				if (!interfazProceso) {
					interfazProceso = linea.contains(
							"public class " +
							this.p.getId() + " {");
				}
				//Controla el metodo del constructor de la clase generada.
				if (!metodoConstructor) {
					metodoConstructor = linea.contains(
							"public " +
							this.p.getId() +
							"(ProcesadorPetri procesadorPN) {");
				}
				//Controla el metodo consultar de la clase.
				if (!metodoConsultar) {
					metodoConsultar = linea.contains(
							"private void consultar(int transicion) {");
				}
				//Controla el metodo disparar de la clase.
				if (!metodoDisparar) {
					metodoDisparar = linea.contains(
							"private void disparar(int transicion) {");
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
		assertTrue("Interfaz de proceso", interfazProceso);
		assertTrue("Metodo Constructor", metodoConstructor);
		assertTrue("Metodo Consultar", metodoConsultar);
		assertTrue("Metodo Disparar", metodoDisparar);
		assertEquals(0, abreYCierraLlaves);
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
