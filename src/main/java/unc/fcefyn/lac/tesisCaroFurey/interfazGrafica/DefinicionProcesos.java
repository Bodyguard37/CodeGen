package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.AbstractHerramientaPN;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.Proceso;
import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.manejadorProcesos.TransicionProceso;

/**
 * Ventana usada para la creacion y definicion de los procesos (nombre,
 * transiciones que lo componen, y si es runnable o no) .
 * @author María Florencia Caro & Ignacio Furey
 */
public class DefinicionProcesos {
	/**
	 * JFrame de la ventana.
	 */
	protected JFrame frmDefinicionProcesos;
	/**
	 * JPanel para contener las transiciones.
	 */
	protected JPanel panelTransiciones;
	/**
	 * ArrayList con una instancia de ValoresTransiciones por cada transicion
	 * de la red.
	 */
	protected ArrayList<ValoresTransicion> valoresTransiciones;
	/**
	 * JTextField usado para capturar el numbre del nuevo hilo cuando se agrega
	 * un hilo nuevo, o el nombre del hilo a remover cuando se elimina un hilo.
	 */
	protected JTextField nombreNuevoHilo;
	/**
	 * Combo box de hilos.
	 */
	protected JComboBox<String> hilosComboBox;
	/**
	 * Herramienta red de petri con los datos.
	 */
	protected AbstractHerramientaPN hpn;
	/**
	 * Contiene si un hilo es runnable o no segun su id.
	 */
	protected HashMap<String, Boolean> definicionHilos;
	/**
	 * indica si un hilo es runnable o no.
	 */
	protected JCheckBox chckbxRunnable;
	/**
	 * Create the application.
	 * @param datos la instancia de HerramientaPN que contiene los datos de la 
	 * red.
	 */
	public DefinicionProcesos(final AbstractHerramientaPN datos) {
		this.hpn = datos;
		initialize(datos.getProcesos(), datos.getTransiciones());
		this.frmDefinicionProcesos.setVisible(true);
	}
	/**
	 * Inicializa el contenido de los frames.
	 * @param transiciones transiciones de la red
	 * @param procesos procesosya definidos
	 */
	private void initialize(final ArrayList<Proceso> procesos,
			final HashMap<String, Integer> transiciones) {
		//inicializo variables.
		this.definicionHilos = new HashMap<String, Boolean>();
		this.valoresTransiciones = new ArrayList<ValoresTransicion>();
		this.frmDefinicionProcesos = new JFrame();
		this.frmDefinicionProcesos.setTitle("Definicion Procesos");
		this.frmDefinicionProcesos.setBounds(100, 100, 733, 460);
		this.frmDefinicionProcesos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JScrollPane scrollPaneTransiciones = new JScrollPane();

		final JPanel panelButtons = new JPanel();

		this.hilosComboBox = new JComboBox<String>();
		this.hilosComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setIdHiloActual();
			}
		});

		this.chckbxRunnable = new JCheckBox("Runnable");
		this.chckbxRunnable.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				definicionHilos.put((String) hilosComboBox.getSelectedItem(),
						Boolean.valueOf(chckbxRunnable.isSelected()));
			}
		});
		final GroupLayout groupLayout = new GroupLayout(
				this.frmDefinicionProcesos.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelButtons,
								GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(
									Alignment.LEADING)
								.addComponent(this.hilosComboBox,
										GroupLayout.PREFERRED_SIZE, 128,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(this.chckbxRunnable))
							.addGap(5)
							.addComponent(scrollPaneTransiciones,
									GroupLayout.DEFAULT_SIZE, 564,
									Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneTransiciones,
								GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.hilosComboBox,
									GroupLayout.PREFERRED_SIZE, 28,
									GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(this.chckbxRunnable)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 33,
							GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		final ActionListener crearHiloActionlistener = new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				crearHilo(nombreNuevoHilo.getText(), Boolean.FALSE);
				nombreNuevoHilo.setText("");
			}
		};
		final JButton btnCrearHilo = new JButton("Crear Proceso");
		btnCrearHilo.addActionListener(crearHiloActionlistener);

		this.nombreNuevoHilo = new JTextField();
		this.nombreNuevoHilo.addActionListener(crearHiloActionlistener);
		panelButtons.add(this.nombreNuevoHilo);
		this.nombreNuevoHilo.setColumns(20);
		panelButtons.add(btnCrearHilo);

		final JButton btnEliminarHilo = new JButton("Eliminar Proceso");
		btnEliminarHilo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				removerHilo((String) hilosComboBox.getSelectedItem());
			}
		});
		panelButtons.add(btnEliminarHilo);

		final JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				generarArchivoHilos();
				frmDefinicionProcesos.setVisible(false);
			}
		});
		panelButtons.add(btnAceptar);
		final JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				frmDefinicionProcesos.setVisible(false);
			}
		});
		panelButtons.add(btnCancelar);

		final Iterator<Proceso> procIt = procesos.iterator();
		while (procIt.hasNext()) {
			final Proceso proc = procIt.next();
			crearHilo(proc.getId(), proc.isRunnable());
		}
		this.panelTransiciones = new JPanel();
		scrollPaneTransiciones.setViewportView(this.panelTransiciones);
		final GridBagLayout gblPanelTransiciones = new GridBagLayout();
		gblPanelTransiciones.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gblPanelTransiciones.rowHeights = new int[]{0, 0, 0, 0};
		gblPanelTransiciones.columnWeights = new double[]
		{1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gblPanelTransiciones.rowWeights = new double[]
		{0.0, 0.0, 1.0, Double.MIN_VALUE};
		this.panelTransiciones.setLayout(gblPanelTransiciones);

		final JLabel labelIdTransicion = new JLabel("IdTrancision");
		final GridBagConstraints gbcLabelIdTransicion = new GridBagConstraints();
		gbcLabelIdTransicion.insets = new Insets(0, 0, 5, 5);
		gbcLabelIdTransicion.gridx = 0;
		gbcLabelIdTransicion.gridy = 0;
		this.panelTransiciones.add(labelIdTransicion, gbcLabelIdTransicion);

		final JLabel labelDisparo = new JLabel("DISPARO");
		final GridBagConstraints gbcLabelDisparo = new GridBagConstraints();
		gbcLabelDisparo.insets = new Insets(0, 0, 5, 5);
		gbcLabelDisparo.gridx = 1;
		gbcLabelDisparo.gridy = 0;
		this.panelTransiciones.add(labelDisparo, gbcLabelDisparo);

		final JLabel labelSecuenciaDisparo = new JLabel("# Secuencia");
		final GridBagConstraints gbcLabelSecuenciaDisparo =
				new GridBagConstraints();
		gbcLabelSecuenciaDisparo.insets = new Insets(0, 0, 5, 5);
		gbcLabelSecuenciaDisparo.gridx = 2;
		gbcLabelSecuenciaDisparo.gridy = 0;
		this.panelTransiciones.add(labelSecuenciaDisparo, gbcLabelSecuenciaDisparo);

		final JLabel labelMetodoDisparo = new JLabel("Metodo()");
		final GridBagConstraints gbcLabelMetodoDisparo =
				new GridBagConstraints();
		gbcLabelMetodoDisparo.insets = new Insets(0, 0, 5, 5);
		gbcLabelMetodoDisparo.gridx = 3;
		gbcLabelMetodoDisparo.gridy = 0;
		this.panelTransiciones.add(labelMetodoDisparo, gbcLabelMetodoDisparo);

		final JLabel labelInforme = new JLabel("INFORME");
		final GridBagConstraints gbcLabelInforme = new GridBagConstraints();
		gbcLabelInforme.insets = new Insets(0, 0, 5, 5);
		gbcLabelInforme.gridx = 4;
		gbcLabelInforme.gridy = 0;
		this.panelTransiciones.add(labelInforme, gbcLabelInforme);

		final JLabel labelSecuenciaInforme = new JLabel("# Secuencia");
		final GridBagConstraints gbcLabelSecuenciaInforme =
				new GridBagConstraints();
		gbcLabelSecuenciaInforme.insets = new Insets(0, 0, 5, 5);
		gbcLabelSecuenciaInforme.gridx = 5;
		gbcLabelSecuenciaInforme.gridy = 0;
		this.panelTransiciones.add(labelSecuenciaInforme, gbcLabelSecuenciaInforme);

		final JLabel labelMetodoInforme = new JLabel("Metodo()");
		final GridBagConstraints gbcLabelMetodoInforme = new GridBagConstraints();
		gbcLabelMetodoInforme.insets = new Insets(0, 0, 5, 0);
		gbcLabelMetodoInforme.gridx = 6;
		gbcLabelMetodoInforme.gridy = 0;
		this.panelTransiciones.add(labelMetodoInforme, gbcLabelMetodoInforme);

		final Iterator<String> transIt = transiciones.keySet().iterator();
		int contadorTransiciones = 1;
		while (transIt.hasNext()) {
			this.valoresTransiciones.add(new ValoresTransicion(
					contadorTransiciones, this.panelTransiciones,
					transiciones.size(),
					transIt.next()));
			contadorTransiciones = contadorTransiciones + 1;
		}
		this.restaurarValoresTransiciones(procesos);
		this.setIdHiloActual();
		this.frmDefinicionProcesos.getContentPane().setLayout(groupLayout);
	}
	/**
	 * Establece un cambio de hilo para todas las instancias de
	 * ValoresTransicion dentro del ArrayList valoresTransiciones.
	 */
	protected void setIdHiloActual() {
		this.chckbxRunnable.setEnabled(true);
		final String idHiloNuevo = (String) this.hilosComboBox.getSelectedItem();
		final Iterator<ValoresTransicion> it2 = this.valoresTransiciones.iterator();
		while (it2.hasNext()) {
			it2.next().setIdHiloActual(idHiloNuevo);
		}
		try {
			this.chckbxRunnable.setSelected(
					this.definicionHilos.get(idHiloNuevo).booleanValue());
		} catch (NullPointerException npe) {
			this.chckbxRunnable.setSelected(false);
			this.chckbxRunnable.setEnabled(false);
		}
	}

	/**
	 * Agrega un nuevo hilo. Controla que no se repitan los id de hilo antes
	 * de agregar.
	 * @param idNuevoHilo id del hilo a agregar.
	 * @param runnable indica si el hilo nuevo es runnable.
	 */
	protected void crearHilo(final String idNuevoHilo, final boolean runnable) {
		//controlo que no exista un hilo con ese id, y que el id nuevo no este
		//vacio.
		if (idNuevoHilo.equals("")) {
			JOptionPane.showMessageDialog(null, "Id de hilo vacio, " +
					"debe colocar un id para el hilo nuevo",
					"ERROR", JOptionPane.ERROR_MESSAGE);
		} else {
			if (idHiloExistente(idNuevoHilo)) {
				JOptionPane.showMessageDialog(null, "Ya existe un hilo con " +
						"ese id, el id de hilo debe ser único",
						"ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				this.definicionHilos.put(idNuevoHilo, runnable);
				this.hilosComboBox.addItem(idNuevoHilo);
			}
		}
	}
	/**
	 * Remueve el hilo cuyo id sea igual a idHilo si es que existe en la lista
	 * de hilos.
	 * @param idHilo .
	 */
	protected void removerHilo(final String idHilo) {
		this.hilosComboBox.removeItem(idHilo);
		this.definicionHilos.remove(idHilo);
		final Iterator<ValoresTransicion> it =
				this.valoresTransiciones.iterator();
		while (it.hasNext()) {
			it.next().removerHilo(idHilo);
		}
	}
	/**
	 * Controla si ya existe en seleccionHilos un hilo con id igual a idHilo.
	 * @param idHilo id de hilo a controlar.
	 * @return la posicion en seleccionHilos del hilo con id igual a idHilo o
	 * -1 si no existe un hilo con ese id.
	 */
	protected boolean idHiloExistente(final String idHilo) {
		for (int i = 0; i < this.hilosComboBox.getItemCount(); i = i + 1) {
			if (this.hilosComboBox.getItemAt(i).equals(idHilo)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Recopila la informacion definida en la ventana y llama al metodo para
	 * guardarla en el archivo xml.
	 */
	protected void generarArchivoHilos() {
		final ArrayList<Proceso> procesosDefinidos = new ArrayList<Proceso>();
		//Recorro todos los hilos
		for (int i = 0; i < this.hilosComboBox.getItemCount(); i = i + 1) {
			//Creo un hilo
			final String hilo = this.hilosComboBox.getItemAt(i);
			final Proceso proceso = new Proceso(hilo,
					this.definicionHilos.get(hilo).booleanValue());
			final Iterator<ValoresTransicion> trIt =
					this.valoresTransiciones.iterator();
			while (trIt.hasNext()) {
				proceso.addTransicion(trIt.next().generarTransicionHilo(hilo));
			}
			procesosDefinidos.add(proceso);
		}
		this.hpn.reemplazarArchivoXML(procesosDefinidos);
	}
	/**
	 * Establece valores de los procesos ya definidos.
	 * @param procesos procesos
	 */
	protected void restaurarValoresTransiciones(
			final ArrayList<Proceso> procesos) {
		//Iterator para cada uno de los procesos.
		final Iterator<Proceso> procIt = procesos.iterator();
		while (procIt.hasNext()) {
			final Proceso actual = procIt.next();
			//Iterator para cada una de las TransicionProceso.
			final Iterator<TransicionProceso> transicionesProcActual =
					actual.getTransiciones().iterator();
			while (transicionesProcActual.hasNext()) {
				final TransicionProceso trActual =
						transicionesProcActual.next();
				//Iterator para cada una de los valoresTransicion.
				final Iterator<ValoresTransicion> transicionesGraficas =
						this.valoresTransiciones.iterator();
				while (transicionesGraficas.hasNext()) {
					final ValoresTransicion trGraf =
							transicionesGraficas.next();
					if (trActual.getIdTransicion().
							equals(trGraf.getIdTransicion())) {
						//Define los valores para el disparo e informe
						//de la transicion.
						if (trActual.getDisparo()) {
							trGraf.setDisparo(trActual.getDisparo());
							trGraf.setMetodoDisparo(
									trActual.getMetodoDisparo());
							trGraf.setOrdenDisparo(trActual.getOrdenDisparo());
							trGraf.setIdHiloDisparo(actual.getId());
						}
						if (trActual.getInforme()) {
							trGraf.setInforme(trActual.getInforme());
							trGraf.setMetodoInforme(
									trActual.getMetodoInforme());
							trGraf.setOrdenInforme(trActual.getOrdenInforme());
							trGraf.setIdHiloInforme(actual.getId());
						}
					}
				}
			}
		}
	}
}
