package unc.fcefyn.lac.tesisCaroFurey.generacionCodigo;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;

/**
 * Generador de codigo.
 * @author María Florencia Caro & Ignacio Furey
 */

public interface GeneradorCodigo {

	/**
	 * Este metodo deberia ser implementado para generar el codigo fuente
	 * en un lenguaje determinado para que se comunique con alguna
	 * implementacion de un "procesador de Petri".
	 * @param infoRed instancia de AbstractHerramientaPN que contenga
	 * los analisis de la red para generar coigo.
	 * @param pathArchivosConfig path a donde guardar el codigo generado.
	 */
	void generarCodigo(AbstractHerramientaPN infoRed,
			String pathArchivosConfig);
}
