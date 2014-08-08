package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import unc.fcefyn.lac.tesisCaroFurey.generacionCodigo.GeneradorCodigo;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.resourcesManager.ResourcesManager;
/**
 * Ventana principal de la aplicacion. Contiene todos las instancias de los
 * objetos necesarios.
 * @author María Florencia Caro & Ignacio Furey
 */
public class VentanaPrincipal {	
	/**
	 * Area de texto para mostrar resultados.
	 */
	protected JTextArea textArea;	
	/**
	 * Red actual con la que se esta trabajando. Es un archivo con la red
	 * que se desea usar para desarrollar el codigo.
	 */
	protected File actualPN;
	/**
	 * JFrame de la ventana principal.
	 */
	private JFrame frmProyctointegrador;
	/**
	 * Jbutton para crear una nueva PN.
	 */
	private JButton btnCrearPn;
	/**
	 * Instancia de la herramienta de analisis de redes de petri.
	 * Se utiliza para obtener diferentes analisis e informacion
	 * de la red de petri con la que se trabaja.
	 */
	private AbstractHerramientaPN herramientaRedPetri;
	/**
	 * Instancia de generacion de codigo. Dpendiendo de que subclase de
	 * GeneradorCodigo se instancie en esta variable sera el lenguaje y modelo
	 * de generacion de codigo que la herramienta implementara.
	 */
	private GeneradorCodigo generadorCodigo;
	/**
	 * JButton de definicion de hilos.
	 */
	private JButton btnDefinirProcesos;
	/**
	 * JButton de generacion de codigo.
	 */
	private JButton btnGenerarCodigo;
	/**
	 * JButton para obtener la matriz de incidencia y marcado inicial.
	 */
	private JButton btnMatriz;
	/**
	 * JButton para obtener analisis estructural: Invariantes de transicion y
	 * plaza.
	 */
	private JButton btnAnalisisEstructural;
	/**
	 * JButton para obtener analisis de alcanzabilidad: vivacidad, interbloqueo,
	 * deadlock, trampas, sifones, grafo de estados/clases, etc.
	 */
	private JButton btnAnalisisDeAlcanzabilidad;
	/**
	 * JButton para abrir una herramienta grafica de edicion/simulacion de PN.
	 */
	private JButton btnAbrirSimularPn;
	/**
	 * Class contructor.
	 * @param hpn Instancia de la herramienta de analisis de redes de petri
	 * a utilizar.
	 * @param gc Instancia de el generador de codigo.
	 */
	public VentanaPrincipal(final AbstractHerramientaPN hpn,
			final GeneradorCodigo gc) {
		//Se establece un LookAndFeel nativo al sistema operativo en uso.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println(ex);
        }
		this.herramientaRedPetri = hpn;
		this.generadorCodigo = gc;
        initialize();
		this.frmProyctointegrador.setVisible(true);
	}
	/**
	 * Inicializa los contenidos del frame.
	 */
	private void initialize() {
		this.frmProyctointegrador = new JFrame();
		this.frmProyctointegrador.setIconImage(Toolkit.getDefaultToolkit().

				getImage(VentanaPrincipal.class.getResource(
					"/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		this.frmProyctointegrador.setTitle("CodGenTPN");
		this.frmProyctointegrador.setBounds(100, 100, 558, 387);
		this.frmProyctointegrador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JMenuBar menuBar = new JMenuBar();
		this.frmProyctointegrador.setJMenuBar(menuBar);
		//Menú archivo
		final JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		//Menú Archivo -> abrir
		final JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final ExploradorArchivoAbrir explorar =
						new ExploradorArchivoAbrir();
				final java.io.File f = explorar.getFile();
				if (f != null) {
					setActualPN(f);
				}
			}
		});
		mnArchivo.add(mntmAbrir);

		//Menú Archivo -> guardar
		final JMenuItem mntmGuardarResultados =
				new JMenuItem("Guardar resultados");
		mntmGuardarResultados.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final ExploradorArchivoGuardar guardarResultados =
						new ExploradorArchivoGuardar("Guardar Resultados",
								"Archivo de Texto", ".txt");
				final java.io.File f = guardarResultados.getFile(".txt");
				try {
					f.createNewFile();
					escribirResultadosenArchivo(f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnArchivo.add(mntmGuardarResultados);
		//Menú Archivo -> salir
		final JMenuItem mntmSalir =	new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {	
				final int confirmarSalir = JOptionPane.showConfirmDialog(mntmAbrir,
						"¿Está seguro que desea salir?", "Salir",  JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == confirmarSalir) {
					System.exit(0);
				}
	
			}
		});
		mnArchivo.add(mntmSalir);

		final JMenu mnAcciones = new JMenu("Acciones");
		menuBar.add(mnAcciones);

		final JMenuItem mntmLimpiarResultados =
				new JMenuItem("Limpiar resultados");
		mntmLimpiarResultados.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				limpiarResultados();
			}
		});
		mnAcciones.add(mntmLimpiarResultados);

		final JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);
		
		final JMenuItem mntmAyuda = new JMenuItem("Manual de Uso");
		mntmAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {			
				URI jarUri;
				URI fileUri;
				try {
					jarUri = ResourcesManager.getJarURI();
					fileUri = ResourcesManager.getFile(jarUri, "manualDeUsuario/ManualDeUso.pdf");
					Desktop.getDesktop().open(new File(fileUri));
				} catch (URISyntaxException | IOException e1) {
					e1.printStackTrace();
				}	
			}
		});
		mnAyuda.add(mntmAyuda);
		
		final JMenuItem mntmAyuda1 = new JMenuItem("About Us ...");
		mntmAyuda1.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				JOptionPane.showMessageDialog(null, "CodGenTPN: Code Generator" +
						"for Petri nets and Time Petri nets\n" +
						"Version: Release 1.0\n" +
						"Build id: 2014-01-20\n" +
						"Author: M.Florencia Caro & Ignacio Furey\n" +
						"(c) Copyright CodGenTPN contributors and others." +
						"All rights reserved.",	"CodGenTPN About Us..",
						JOptionPane.INFORMATION_MESSAGE);				
			}
		});
		mnAyuda.add(mntmAyuda1);	
		

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.9E-324};
		this.frmProyctointegrador.getContentPane().setLayout(gridBagLayout);

		this.btnCrearPn = new JButton("Crear PN");
		this.btnCrearPn.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//Pide un nuevo archivo de red
				final ExploradorArchivoGuardar explorar =
						new ExploradorArchivoGuardar("Nueva red",
								"Petri Net Markup Language - PNML", "pnml");
				final java.io.File f = explorar.getFile(".pnml");
				try {
					f.createNewFile();
					escribirCabeceraPNML(f);
					herramientaRedPetri.crearPN(f.getAbsolutePath());
					setActualPN(f);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Permiso denegado",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					//ex.printStackTrace();
				}
			}
		});
		final GridBagConstraints gbcBtnCrearPn1 = new GridBagConstraints();
		gbcBtnCrearPn1.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCrearPn1.insets = new Insets(0, 0, 5, 0);
		gbcBtnCrearPn1.gridx = 1;
		gbcBtnCrearPn1.gridy = 0;
		this.frmProyctointegrador.getContentPane().add(this.btnCrearPn, gbcBtnCrearPn1);

		this.btnAbrirSimularPn = new JButton("Editar/Simular PN");
		this.btnAbrirSimularPn.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				editarSimularPN();
				setActualPN(actualPN);
			}
		});
		final GridBagConstraints gbcBtnAbrirSimularPn =
								new GridBagConstraints();
		gbcBtnAbrirSimularPn.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnAbrirSimularPn.insets = new Insets(0, 0, 5, 0);
		gbcBtnAbrirSimularPn.gridx = 1;
		gbcBtnAbrirSimularPn.gridy = 1;
		this.frmProyctointegrador.getContentPane().add(this.btnAbrirSimularPn,
				gbcBtnAbrirSimularPn);

		this.btnAnalisisDeAlcanzabilidad = new JButton(
				"Analisis de alcanzabilidad");
		this.btnAnalisisDeAlcanzabilidad.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				analisisAlcanzabilidad();
			}
		});
		final GridBagConstraints gbcBtnAnalisisDeAlcanzabilidad =
				new GridBagConstraints();
		gbcBtnAnalisisDeAlcanzabilidad.insets = new Insets(0, 0, 5, 0);
		gbcBtnAnalisisDeAlcanzabilidad.gridx = 1;
		gbcBtnAnalisisDeAlcanzabilidad.gridy = 2;
		this.frmProyctointegrador.getContentPane().
			add(this.btnAnalisisDeAlcanzabilidad, gbcBtnAnalisisDeAlcanzabilidad);

		this.btnAnalisisEstructural = new JButton("Analisis Estructural");
		this.btnAnalisisEstructural.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				analisisEstructural();
			}
		});
		final GridBagConstraints gbcBtnAnalisisEstructural =
				new GridBagConstraints();
		gbcBtnAnalisisEstructural.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnAnalisisEstructural.insets = new Insets(0, 0, 5, 0);
		gbcBtnAnalisisEstructural.gridx = 1;
		gbcBtnAnalisisEstructural.gridy = 3;
		this.frmProyctointegrador.getContentPane().add(this.btnAnalisisEstructural,
				gbcBtnAnalisisEstructural);

		this.btnMatriz = new JButton("Matriz y marcado");
		this.btnMatriz.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				matrizYMarcado();
			}
		});
		final GridBagConstraints gbcBtnMatriz = new GridBagConstraints();
		gbcBtnMatriz.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnMatriz.insets = new Insets(0, 0, 5, 0);
		gbcBtnMatriz.gridx = 1;
		gbcBtnMatriz.gridy = 4;
		this.frmProyctointegrador.getContentPane().add(this.btnMatriz,
				gbcBtnMatriz);

		final JScrollPane scrollPane = new JScrollPane();
		final GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.gridheight = 8;
		gbcScrollPane.insets = new Insets(0, 0, 0, 5);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		this.frmProyctointegrador.getContentPane().add(scrollPane, gbcScrollPane);

		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		scrollPane.setViewportView(this.textArea);
		this.frmProyctointegrador.getContentPane().setFocusTraversalPolicy(
				new FocusTraversalOnArray(new Component[]{this.textArea, this.btnCrearPn,
					this.btnAbrirSimularPn, this.btnAnalisisDeAlcanzabilidad,
					this.btnAnalisisEstructural, this.btnMatriz, scrollPane, }));
		this.frmProyctointegrador.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[]{this.frmProyctointegrador.getContentPane(),
					this.btnCrearPn, this.btnAbrirSimularPn,
					this.btnAnalisisDeAlcanzabilidad, this.btnAnalisisEstructural,
					this.btnMatriz, scrollPane, this.textArea, menuBar, mnArchivo,
					mntmAbrir, mnAcciones, mntmLimpiarResultados,
					mnAyuda, }));

		this.btnGenerarCodigo = new JButton("Generar C\u00F3digo");
		this.btnGenerarCodigo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final ExploradorCarpeta explorar = new ExploradorCarpeta();
				final java.io.File folderFile = explorar.getPath();
				generadorCodigo.generarCodigo(herramientaRedPetri,
						folderFile.getAbsolutePath());
				JOptionPane.showMessageDialog(null, "Podrá " +
						"encontrar su codigo fuente en: \n" +
						folderFile.getAbsolutePath(),
						"GenCod finalizo!", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		final GridBagConstraints gbcBtnNewButton = new GridBagConstraints();
		gbcBtnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnNewButton.insets = new Insets(0, 0, 5, 0);
		gbcBtnNewButton.gridx = 1;
		gbcBtnNewButton.gridy = 6;
		this.frmProyctointegrador.getContentPane().add(
				this.btnGenerarCodigo, gbcBtnNewButton);

		this.btnDefinirProcesos = new JButton("Definir Procesos");
		this.btnDefinirProcesos.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				new DefinicionProcesos(herramientaRedPetri);
			}
		});
		final GridBagConstraints gbcBtnDefinirProcesos = new GridBagConstraints();
		gbcBtnDefinirProcesos.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnDefinirProcesos.insets = new Insets(0, 0, 5, 0);
		gbcBtnDefinirProcesos.gridx = 1;
		gbcBtnDefinirProcesos.gridy = 5;
		this.frmProyctointegrador.getContentPane().add(this.btnDefinirProcesos,
				gbcBtnDefinirProcesos);

		setVisibilidadBotones(false);
	}
	/**
	 * Muestra un resultado dado por string en el area de texto de la ventana.
	 * @param string resultado a mostrar.
	 */
	public void mostrarResultado(final String string) {
		this.textArea.append(string + "\n");
	}
	/**
	 * Limpia el área de texto de la ventana.
	 */
	public void limpiarResultados() {
		this.textArea.setText("");
	}
	/**
	 * Lee el área de texto de la ventana.
	 * @return resultado
	 */
	public String leerResultados() {
		final String resultado = this.textArea.getText();
		return resultado;
	}
	/**
	 * Escribe lo que esta en el area de texto en un archivo de texto.
	 * @param f archivo
	 * @throws java.io.IOException
	 */
	public void escribirResultadosenArchivo(final File f)
			throws java.io.IOException {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(this.leerResultados());
			bw.close();
		} catch (java.io.IOException e) {
			throw new java.io.IOException();
		} finally {
			try {
				bw.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
        }
	}	
	/**
	 * Establece el valor del campo actualPN.
	 * Limpia la salida de resultados de ventanaPrincipal, luego muestra la
	 * nueva red abierta por medio de ventanaPrincipal en formato NET.
	 * @param pn un File con algun formato admitido de PN.
	 */
	public void setActualPN(final File pn) {
		//Limpia la ventana resultados
		this.limpiarResultados();
		if (pn != null) {
			//Establece el valor de actualPN.
			this.actualPN = pn;
			//Muestra la red en formato NET por ventanaPrincipal
			this.mostrarResultado(
					this.herramientaRedPetri.mostrarEnFormatoNET(pn.getPath()));
			final String red = pn.getAbsolutePath();
			final String procesos = pn.getAbsolutePath().
					substring(0, pn.getAbsolutePath().lastIndexOf('.')) +
					"_procesos.xml";
			this.herramientaRedPetri.cargarRed(red, procesos);
			this.setVisibilidadBotones(true);
		} else {
			this.mostrarResultado(
					"ERROR: archivo de red incorrecto");
		}
	}
	/**
	 * Abre la red dada en actualPN con el editor de Red de Petri de
	 * herramientaRedPetri.
	 */
	public void editarSimularPN() {
		if (this.actualPN != null && this.actualPN.canRead()) {
			this.herramientaRedPetri.editarPN(this.actualPN.getAbsolutePath());
		} else {
			this.mostrarResultado(
					"ERROR: Red de Petri no establecida o path incorrecto");
		}
	}
	/**
	 * Realiza un analisis de alcanzabilidad a la Red de Petri dada en actualPN
	 * con la herramienta de herramientaRedPetri.
	 */
	public void analisisAlcanzabilidad() {
		this.mostrarResultado(this.herramientaRedPetri.getAnalisisAlcanzabilidad());
	}
	/**
	 * Realiza un analisis estructural a la Red de Petri dada en actualPN con la
	 * herramienta de herramientaRedPetri.
	 */
	public void analisisEstructural() {
		this.mostrarResultado(this.herramientaRedPetri.getAnalisisEstructural());
	}
	/**
	 * Obtiene la matriz de incidencia y el marcado inicial de la red de Petri
	 * dada en actualPN con la herramienta y los muestra por la ventana de
	 * resultados.
	 */
	protected void matrizYMarcado() {
		this.mostrarResultado(this.herramientaRedPetri.
				matrizIncidenciaMarcadoInicialComoString());
	}
	/**
	 * Escribe una cabecera PNML en un archivo nuevo, de esta forma se evita
	 * que el archivo nuevo este vacio.
	 *@param file El archivo a ser escrito con la cabecera PNML.
	 *@throws IOException en caso que halla un error de apertura o escritura
	 *con el archivo.
	 */
	protected void escribirCabeceraPNML(final File file)
		throws java.io.IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
				"\n<pnml>" +
					"\n\t<net id=\"net1\">" +
						"\n\t\t<name>" +
							"\n\t\t\t<text>" + file.getName() + "</text>" +
						"\n\t\t</name>" +
						"\n\t\t<page id=\"page1-net1\">" +
						"\n\t\t</page>" +
					"\n\t</net>" +
				"\n</pnml>");
		} catch (java.io.IOException e) {
			throw new java.io.IOException();
		} finally {
			try {
				bw.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
        }
	}
	/**
	 * Establece la visisbilidad de los botones de la ventana principal.
	 * @param visibilidad true para habilitar los botones, false caso contrario.
	 */
	protected void setVisibilidadBotones(final boolean visibilidad) {
		this.btnDefinirProcesos.setEnabled(visibilidad);
		this.btnGenerarCodigo.setEnabled(visibilidad);
		this.btnMatriz.setEnabled(visibilidad);
		this.btnAnalisisEstructural.setEnabled(visibilidad);
		this.btnAnalisisDeAlcanzabilidad.setEnabled(visibilidad);
		this.btnAbrirSimularPn.setEnabled(visibilidad);
	}
}
