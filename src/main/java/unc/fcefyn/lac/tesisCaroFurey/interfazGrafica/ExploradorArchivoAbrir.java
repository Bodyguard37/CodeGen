package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Abre una ventana de exploracion para buscar una archivo de Red de Petri.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ExploradorArchivoAbrir {
	/**
	 * Objeto JFileChooser, usado para abrir explorador.
	 */
	private JFileChooser chooser;
	/**
	 * Class constructor. Crea una nueva instancia de JFileChooser
	 * y la configura.
	 */
	public ExploradorArchivoAbrir() {
		//Creamos selector de apertura
		this.chooser = new JFileChooser();
		//Establece el directorio actual del programa java
		//como directorio actual del explorador
		//chooser.setCurrentDirectory(new java.io.File("."));
		//Titulo que llevara la ventana
		this.chooser.setDialogTitle("Abrir");
		//Elegiremos archivos del directorio
		this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//Agregamos un filtros de extensiones de Redes de Petri
		//DEBERIAN SETEARSE SEGUN FORMATOS SOPORTADOS
		//DE HERRAMIENTAPETRINET
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						"Petri Net Markup Language - PNML", "pnml"));
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						"Petri Net Markup Language - XML", "xml"));
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						"NDR", "ndr"));
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						"NET", "net"));
		this.chooser.setFileFilter(
				new FileNameExtensionFilter(
						"Petri Net", "pnml", "xml", "ndr", "net"));
		this.chooser.setAcceptAllFileFilterUsed(false);
	}
	/**
	 * Retorna el File seleccionado en el explorador.
	 * @return archivo seleccionado.
	 */
	public File getFile() {
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
