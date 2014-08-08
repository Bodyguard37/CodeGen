package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.Tina;
/**
 * Test de la clase GenerarCodigoJavaTest
 * @author Maria Florencia caro & Ignacio Furey
 * */
public class GenerarCodigoJavaTest {
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
	 * Unidad bajo prueba.
	 */
	private GenerarCodigoJava uut;

	/**
	 * Precondiciones.
	 * @throws java.lang.Exception .
	 */
	@Before
	public void setUp() throws Exception {
		this.hpn = new Tina();
		this.hpn.cargarRed(this.pathRed, this.pathProcesos);
		this.uut = new GenerarCodigoJava();
	}

	/**
	 * Prueba que el metodo generarCodigo() genere correctamente el arbol de
	 * directorios del proyecto generado.
	 */
	@Test
	public void testGenerarCodigo() {
		final File testFolder = new File(this.pathDir + "\\generador");
		testFolder.mkdirs();
		this.uut.generarCodigo(this.hpn, testFolder.getPath());
		assertTrue("", new File(testFolder.getPath() +
				"\\archivosConfiguracion").exists());
		assertTrue("", new File(testFolder.getPath() +
				"\\src").exists());
		assertTrue("", new File(testFolder.getPath() +
				"\\src\\main").exists());
		assertTrue("", new File(testFolder.getPath() +
				"\\src\\procesos").exists());
		assertTrue("", new File(testFolder.getPath() +
				"\\PPNV.jar").exists());
		deleteWithChildren(testFolder.getPath());
	}

	/**
	 * Comprueba que se hayan generado los archivos de configuracion luego
	 * de llamar el metodo generarArchivosConfiguracion().
	 */
	@Test
	public void testGenerarArchivosConfiguracion() {
		this.uut.generarArchivosConfiguracion(this.hpn, this.pathDir);
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\automaticos0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\config.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\cotas0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\incidencia0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\marcado0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\prioridades0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\prioridadesDistribuidas.txt").
				exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\relacion0.txt").exists());
		assertTrue("", new File(this.pathDir +
				"\\archivosConfiguracion\\sinInforme0.txt").exists());
		deleteWithChildren(this.pathDir + "\\archivosConfiguracion");
	}

	/**
	 * Comprueba los "catch" & "try" del metodo generarArchivoMatrix().
	 */
	@Test
	public void testGenerarArchivoMatriz() {
		final int[][] matrizPositiva = { {0, 1, }, {0, 1, } };
		final int[][] matrizNegativa = { {0, 1, }, {0, 1, } };
		try {	
			this.uut.generarArchivoMatriz(matrizPositiva,
					matrizNegativa,
					"AZ:\\pathInexistente");
			fail("No capturo java.io.IOException con path erroneo");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Comprueba los "catch" & "try" del metodo generarVectorVertical().
	 */
	@Test
	public void testGenerarArchivoVectorVertical() {
		final int[] valores = {0, 1, };
		try {	
			this.uut.generarArchivoVectorVertical(valores,
					"AZ:\\pathInexistente");
			fail("No capturo java.io.NullPointerException con path erroneo");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Comprueba los "catch" & "try" del metodo generarVectorCero().
	 */
	@Test
	public void testGenerarVectorCero() {
		try {	
			this.uut.generarVectorCero(0, "AZ:\\pathInexistente", " ");
			fail("No capturo java.io.NullPointerException con path erroneo");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Comprueba los "catch" & "try" del metodo generarMatrizIdentidad().
	 */
	@Test
	public void testGenerarMatrizIdentidad() {
		try {	
			this.uut.generarMatrizIdentidad(1, "AZ:\\pathInexistente");
			fail("No capturo java.io.NullPointerException con path erroneo");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Comprueba los "catch" & "try" del metodo generarArchivoConfig().
	 */
	@Test
	public void testGenerarArchivoConfig() {
		try {	
			this.uut.generarArchivoConfig("AZ:\\pathInexistente");
			fail("No capturo java.io.NullPointerException con path erroneo");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Prueba el metodo copiar().
	 */
	@Test
	public void testCopiar() {
		this.uut.copiar(this.pathDir + "testGeneradorCodigo.pnml",
				this.pathDir + "pruebaCopiado.pnml");
		final File copia = new File(this.pathDir + "pruebaCopiado.pnml");
		assertTrue("Copia de Archivo", copia.exists());
		copia.delete();
		try {	
			this.uut.copiar("AZ:\\pathInexistente", "AZ:\\otroPathInexistente");
		} catch (Exception e) {
			assertEquals(java.lang.NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Elimina un archivo o un directorio con todo su contenido.
	 * @param path path del archivo o directorio a borrar.
	 * @return true si el archivo no existe o fue eliminado exitosamente. false
	 * si ocurrio un error durante la eliminacion.
	 */
	private boolean deleteWithChildren(final String path) {
		final File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (!file.isDirectory()) {
			return file.delete();
		}
		return this.deleteChildren(file) && file.delete();
	}
	/**
	 * Elimina recursivamente el contenido de un directorio.
	 * @param dir File con el directorio.
	 * @return true en caso de exito. false caso contrario.
	 */
	private boolean deleteChildren(final File dir) {
		final File[] children = dir.listFiles();
		boolean childrenDeleted = true;
		for (int i = 0; children != null && i < children.length; i = i + 1) {
			final File child = children[i];
			if (child.isDirectory()) {
				childrenDeleted = this.deleteChildren(child) && childrenDeleted;
			}
			if (child.exists()) {
				childrenDeleted = child.delete() && childrenDeleted;
			}
		}
		return childrenDeleted;
	}
}
