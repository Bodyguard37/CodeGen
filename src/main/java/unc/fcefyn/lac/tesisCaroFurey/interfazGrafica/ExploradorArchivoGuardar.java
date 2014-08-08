package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Abre una ventana de exploracion para crear un nuevo archivo de Red de Petri.
 * @author María Florencia Caro & Ignacio Fuey
 */
public class ExploradorArchivoGuardar {
	/**
	 * Objeto JFileChooser, usado para abrir explorador.
	 */
	private JFileChooser chooser;
	/**
	 * Class constructor. Crea una nueva instancia de JFileChooser
	 * y la configura.
	 * @param titulo Titulo de la ventana de exploracion
	 * @param formatoDescripcion Descripcion del formato del archivo
	 * @param formatoExtension Formato del archivo
	 */
	public ExploradorArchivoGuardar(final String titulo,
			final String formatoDescripcion, final String formatoExtension) {
		//Creamos selector de apertura
		this.chooser = new JFileChooser();

		//Titulo que llevara la ventana
		this.chooser.setDialogTitle(titulo);
		//Elegiremos archivos del directorio
		//chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		//Agregamos un filtros de extensiones de Redes de Petri, unicamente PNML
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						formatoDescripcion, formatoExtension));
		this.chooser.setAcceptAllFileFilterUsed(false);
	}
	/**
	 * Retorna el File seleccionado en el explorador.
	 * @param extension TODO
	 * @return archivo seleccionado.
	 */
	public File getFile(final String extension) {
		File f = null;
		//analizamos la respuesta
		switch (this.chooser.showSaveDialog(null)) {
			case JFileChooser.APPROVE_OPTION:
				//seleccionó guardar, guarda el archivo
				//seleccionado en f
		 		final File aux = this.chooser.getSelectedFile();
		 		f = new File(aux.getAbsolutePath() + extension);
		 		aux.delete();
		 		break;
			case JFileChooser.CANCEL_OPTION:
				//selecciono cancelar o cerro la
				//ventana, setea f en null
				f = null;
				break;

			case JFileChooser.ERROR_OPTION:
				//Llega aqui si sucede un error, setea f en null
				f = null;
				break;
			default:
				break;
		}
		return f;
	}
}
