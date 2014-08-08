package unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;

import java.io.IOException;
import java.nio.channels.FileChannel;

import org.junit.Test;
/**
 * Test de los metodos borrarProceso() y agregraProceso().
 * @author María Florencia Caro & Ignacio Furey  
 **/

public class TestBorrarProceso {
	/**
	 * Proceso que se borrara.
	 */
	private Proceso proceso0;

	@Test
	public final void testBorrarProceso() {
		final String sourceFile = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos_procesos.pnml";
		final String destinationFile = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos_procesosA.pnml";
		this.copiar(sourceFile, destinationFile);

		final ManejadorProcesos manejador = new ManejadorProcesos(destinationFile);
		this.proceso0 = manejador.procesos.get(0);	
		manejador.borrarProceso(this.proceso0, destinationFile);
		final ManejadorProcesos manejador1 = new ManejadorProcesos(destinationFile);
		
		assertEquals(manejador.getProcesos().size(), manejador1.getProcesos().size());
		for (int indice = 0; indice < manejador.getProcesos().size();
				indice = indice + 1) {
			assertEquals(manejador.getProcesos().get(indice).getId(), manejador1
					.getProcesos().get(indice).getId());
		}	
		new File(destinationFile).delete();
	}
	/**
	 * Probar metodo agregar proceso.
	 */
	@Test
	public final void testAgregarProceso() {
		
		final String sourceFile = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos_procesos.pnml";
		final String destinationFile = "./src/test/resources/unc/fcefyn/lac/" +
				"tesisCaroFurey/Ejemplo2hilos_procesosA.pnml";
		this.copiar(sourceFile, destinationFile);
	
		final Proceso procesoparaAgregar = new Proceso("procesoAgregado", true);
				
		final ManejadorProcesos manejador = new ManejadorProcesos(destinationFile);	

		final int sizeEsperadoProcesosDespuesAgregar = manejador.getProcesos().size() + 1;
		manejador.agregarProceso(procesoparaAgregar, destinationFile);
		
		assertEquals(sizeEsperadoProcesosDespuesAgregar, manejador.getProcesos().size());
		
		final Proceso ultimoProcesoManejadorDespuesAgregar = manejador.
				getProcesos().get(sizeEsperadoProcesosDespuesAgregar - 1);
		assertEquals(procesoparaAgregar.getId(), ultimoProcesoManejadorDespuesAgregar.getId());
		
		new File(destinationFile).delete();
				
	}
	/**
	 * Metodo que hace una copia del archivo de procesos que se va a modificar.
	 * @param sourceFile path del archivo de procesos original
	 * @param destinationFile path del arhivo copia que se generará
	 */
	public void copiar(final String sourceFile, final String destinationFile) {
		final File destination = new File(destinationFile);
		try {
			destination.createNewFile();
		} catch (IOException e1) {
			System.out.println("Error creando el nuevo archivo :" + e1);
			e1.printStackTrace();
		}
		try {
			FileInputStream is;
			is = new FileInputStream(sourceFile);
			final FileOutputStream os = new FileOutputStream(destination);
		    final FileChannel srcChannel = is.getChannel();
		    final FileChannel dstChannel = os.getChannel();
		    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		    is.close();
		    os.close();
		} catch (FileNotFoundException e) {
			System.out.println("Specified file not found :" + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error while copying file :" + e);
			e.printStackTrace();
		}
	}			
}
