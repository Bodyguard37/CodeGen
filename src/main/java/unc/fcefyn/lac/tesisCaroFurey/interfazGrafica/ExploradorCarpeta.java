package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * Abre una ventana de exploracion para buscar un path de Directorio.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ExploradorCarpeta {
	/**
	 * Objeto JFileChooser, usado para abrir explorador.
	 */
	private JFileChooser chooser;
	/**
	 * Class constructor. Crea una nueva instancia de JFileChooser.
	 * y la configura.
	 */
	public ExploradorCarpeta() {
		//Creamos selector de apertura
		this.chooser = new JFileChooser();
		//Establece el directorio actual del programa java
		//como directorio actual del explorador
		//chooser.setCurrentDirectory(new java.io.File("."));
		//Titulo que llevara la ventana
		this.chooser.setDialogTitle("Seleccionar directorio");
		//Elegiremos archivos del directorio
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	/**
	 * Retorna el File seleccionado en el explorador.
	 * @return archivo seleccionado.
	 */
	public File getPath() {
		File f = null;
		//analizamos la respuesta
		switch (this.chooser.showOpenDialog(null)) {
			case JFileChooser.APPROVE_OPTION:
				//seleccionó abrir, guarda el archivo
				//seleccionado en f
		 		f = this.chooser.getSelectedFile();
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
