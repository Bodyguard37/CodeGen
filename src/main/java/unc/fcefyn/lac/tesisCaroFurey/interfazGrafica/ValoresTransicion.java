package unc.fcefyn.lac.tesisCaroFurey.interfazGrafica;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import unc.fcefyn.lac.tesisCaroFurey.herramientaPN.
			manejadorProcesos.TransicionProceso;

/**
 * Clase que contiene los components java swing de una transicion para la
 * ventana de definicion de procesos.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ValoresTransicion {
	/**
	 * id del hilo seleccionado en la ventana de definicion de procesos.
	 */
	private String idHiloActual;
	/**
	 * id del hilo que "capturo" la etiqueta de disparo de esta transicion.
	 */
	private String idHiloDisparo;
	/**
	 * id del hilo que "capturo" la etiqueta de informe de esta transicion.
	 */
	private String idHiloInforme;
	/**
	 * muestra el id de transicion.
	 */
	private JTextField idTransicion;
	/**
	 * captura la etiqueta de disparo para el hilo con el mismo id que
	 * idHiloActual.
	 */
	private JCheckBox disparo;
	/**
	 * Establece el orden dentro del hilo para el disparo de esta transicion.
	 */
	private JComboBox<String> ordenDisparo;
	/**
	 * permite al usuario definir un metodo para el disparo de la transicion.
	 */
	private JTextField metodoDisparo;
	/**
	 * captura la etiqueta de informe para el hilo con el mismo id que
	 * idHiloActual.
	 */
	private JCheckBox informe;
	/**
	 * Establece el orden dentro del hilo para el informe de esta transicion.
	 */
	private JComboBox<String> ordenInforme;
	/**
	 * permite al usuario definir un metodo para el informe de la transicion.
	 */
	private JTextField metodoInforme;

	/**
	 * constructor.
	 * @param posicionFila posicion en la fila del layout.
	 * @param panelTransiciones panel donde se colocan los componentes swing.
	 * @param totalTransicionesRed total de transiciones de la red.
	 * @param idTransicionActual id de la transicion.
	 */
	public ValoresTransicion(final int posicionFila,
							final JPanel panelTransiciones,
							final int totalTransicionesRed,
							final String idTransicionActual) {
		//inicializo variables.
		this.idHiloActual = new String("");
		this.idHiloDisparo = new String("");
		this.idHiloInforme = new String("");
		//Creo arreglo para definir los items de los JComboBox
		final int tamano;
		if (totalTransicionesRed < 13) {
			tamano = totalTransicionesRed * 4;			
		} else {
			tamano = (totalTransicionesRed * 2) + 26;
		}
		final String[] comboBoxItems = new String[tamano + 1];
		comboBoxItems[0] = "";
		for (int i = 1; i < (totalTransicionesRed * 2 + 1); i = i + 1) {
			comboBoxItems[i] = Integer.toString(i);
		}
		for (int i = 1; i < tamano - (totalTransicionesRed * 2) + 1; i = i + 1) {
			comboBoxItems[i + totalTransicionesRed * 2] =
					String.valueOf((char) ('A' + i - 1));
		}

		//Inicializo variables de java swing
		this.idTransicion = new JTextField();
		this.idTransicion.setText(idTransicionActual);
		this.idTransicion.setEditable(false);
		this.idTransicion.setColumns(10);
		final GridBagConstraints gbcIdTransicion = new GridBagConstraints();
		gbcIdTransicion.anchor = GridBagConstraints.NORTH;
		gbcIdTransicion.fill = GridBagConstraints.HORIZONTAL;
		gbcIdTransicion.insets = new Insets(0, 0, 0, 5);
		gbcIdTransicion.gridx = 0;
		gbcIdTransicion.gridy = posicionFila;
		panelTransiciones.add(this.idTransicion, gbcIdTransicion);

		this.disparo = new JCheckBox("");
		this.disparo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				if (disparo.isSelected()) {
					idHiloDisparo = idHiloActual;			
				} else {
					idHiloDisparo = "";
				}
			}
		});
		final GridBagConstraints gbcDisparo = new GridBagConstraints();
		gbcDisparo.anchor = GridBagConstraints.NORTH;
		gbcDisparo.insets = new Insets(0, 0, 0, 5);
		gbcDisparo.gridx = 1;
		gbcDisparo.gridy = posicionFila;
		panelTransiciones.add(this.disparo, gbcDisparo);

		this.ordenDisparo = new JComboBox<String>(comboBoxItems);
		this.ordenDisparo.setEditable(false);
		final GridBagConstraints gbcOrdenDisparo = new GridBagConstraints();
		gbcOrdenDisparo.anchor = GridBagConstraints.NORTH;
		gbcOrdenDisparo.fill = GridBagConstraints.NONE;
		gbcOrdenDisparo.insets = new Insets(0, 0, 0, 5);
		gbcOrdenDisparo.gridx = 2;
		gbcOrdenDisparo.gridy = posicionFila;
		panelTransiciones.add(this.ordenDisparo, gbcOrdenDisparo);

		this.metodoDisparo = new JTextField();
		this.metodoDisparo.setColumns(10);
		final GridBagConstraints gbcMetodoDisparo = new GridBagConstraints();
		gbcMetodoDisparo.anchor = GridBagConstraints.NORTH;
		gbcMetodoDisparo.fill = GridBagConstraints.HORIZONTAL;
		gbcMetodoDisparo.insets = new Insets(0, 0, 0, 5);
		gbcMetodoDisparo.gridx = 3;
		gbcMetodoDisparo.gridy = posicionFila;
		panelTransiciones.add(this.metodoDisparo, gbcMetodoDisparo);

		this.informe = new JCheckBox("");
		this.informe.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				if (informe.isSelected()) {
					idHiloInforme = idHiloActual;			
				} else {
					idHiloInforme = "";
				}
			}
		});
		final GridBagConstraints gbcInforme = new GridBagConstraints();
		gbcInforme.anchor = GridBagConstraints.NORTH;
		gbcInforme.insets = new Insets(0, 0, 0, 5);
		gbcInforme.gridx = 4;
		gbcInforme.gridy = posicionFila;
		panelTransiciones.add(this.informe, gbcInforme);

		this.ordenInforme = new JComboBox<String>(comboBoxItems);
		this.ordenInforme.setEditable(false);
		final GridBagConstraints gbcOrdenInforme = new GridBagConstraints();
		gbcOrdenInforme.anchor = GridBagConstraints.NORTH;
		gbcOrdenInforme.fill = GridBagConstraints.NONE;
		gbcOrdenInforme.insets = new Insets(0, 0, 0, 5);
		gbcOrdenInforme.gridx = 5;
		gbcOrdenInforme.gridy = posicionFila;
		panelTransiciones.add(this.ordenInforme, gbcOrdenInforme);

		this.metodoInforme = new JTextField();
		this.metodoInforme.setColumns(10);
		final GridBagConstraints gbcMetodoInforme = new GridBagConstraints();
		gbcMetodoInforme.anchor = GridBagConstraints.NORTH;
		gbcMetodoInforme.fill = GridBagConstraints.HORIZONTAL;
		gbcMetodoInforme.gridx = 6;
		gbcMetodoInforme.gridy = posicionFila;
		panelTransiciones.add(this.metodoInforme, gbcMetodoInforme);
	}
	/**
	 * Setter.
	 * @param estadoDisparo Estado del disparo en la transicion
	 */
	public final void setDisparo(final boolean estadoDisparo) {
		this.disparo.setSelected(estadoDisparo);
	}
	/**
	 * Setter.
	 * @param ordenDisparoNuevo ordenDisparo a setear
	 */
	public final void setOrdenDisparo(final String ordenDisparoNuevo) {
		this.ordenDisparo.setSelectedItem(ordenDisparoNuevo);
	}
	/**
	 * setter.
	 * @param metodoDisparoNuevo metodoDisparo a setear
	 */
	public final void setMetodoDisparo(final String metodoDisparoNuevo) {
		this.metodoDisparo.setText(metodoDisparoNuevo);
	}
	/**
	 * setter.
	 * @param estadoInforme el estado de informe
	 */
	public final void setInforme(final boolean estadoInforme) {
		this.informe.setSelected(estadoInforme);
	}
	/**
	 * Establece el orden de informe si exite.
	 * @param ordenInformeNuevo ordenInforme a setear
	 */
	public final void setOrdenInforme(final String ordenInformeNuevo) {
		this.ordenInforme.setSelectedItem(ordenInformeNuevo);
	}
	/**
	 * Establece el metodo de informe si ya existe.
	 * @param metodoInformeNuevo metodoInforme a setear
	 */
	public final void setMetodoInforme(final String metodoInformeNuevo) {
		this.metodoInforme.setText(metodoInformeNuevo);
	}
	/**
	 * setter.
	 * @param idHiloNuevo idHiloNuevo a setear
	 */
	public void setIdHiloActual(final String idHiloNuevo) {
		this.idHiloActual = idHiloNuevo;
		this.cambioDeHilo();
	}
	/**
	 * setter.
	 * @param idHiloDisparoNuevo idHiloDisparo a setear
	 */
	public void setIdHiloDisparo(final String idHiloDisparoNuevo) {
		this.idHiloDisparo = idHiloDisparoNuevo;
	}
	/**
	 * setter.
	 * @param idHiloInformeNuevo the idHiloInforme to set
	 */
	public void setIdHiloInforme(final String idHiloInformeNuevo) {
		this.idHiloInforme = idHiloInformeNuevo;
	}
	/**
	 * Getter.
	 * @return idHiloActual
	 */
	public String getIdHiloActual() {
		return this.idHiloActual;
	}
	/**
	 * Getter.
	 * @return idHiloDisparo
	 */
	public String getIdHiloDisparo() {
		return this.idHiloDisparo;
	}
	/**
	 * Getter.
	 * @return idHiloInforme
	 */
	public String getIdHiloInforme() {
		return this.idHiloInforme;
	}
	/**
	 * Getter.
	 * @return idTransicion
	 */
	public String getIdTransicion() {
		return this.idTransicion.getText();
	}
	/**
	 * Getter.
	 * @return disparo
	 */
	public JCheckBox getDisparo() {
		return this.disparo;
	}
	/**
	 * Getter.
	 * @return ordenDisparo
	 */
	public JComboBox<String> getOrdenDisparo() {
		return this.ordenDisparo;
	}
	/**
	 * Getter.
	 * @return metodoDisparo
	 */
	public JTextField getMetodoDisparo() {
		return this.metodoDisparo;
	}
	/**
	 * Getter.
	 * @return informe
	 */
	public JCheckBox getInforme() {
		return this.informe;
	}
	/**
	 * Getter.
	 * @return ordenInforme
	 */
	public JComboBox<String> getOrdenInforme() {
		return this.ordenInforme;
	}
	/**
	 * Getter.
	 * @return metodoInforme
	 */
	public JTextField getMetodoInforme() {
		return this.metodoInforme;
	}
	/**
	 * Establece la visibilidad y valor de los valores de la transicion en
	 * dependencia del hilo actual.
	 */
	private void cambioDeHilo() {
		boolean enable = false;
		boolean selected = false;

		if (this.idHiloDisparo.equals("")) {
			//Si valores de disparo no estan asignados
			enable = true;
			selected = false;
		} else {
			if (this.idHiloDisparo.equals(this.idHiloActual)) {
				//Si valores de disparo ya estan asignados al hilo nuevo
				enable = true;
				selected = true;
			} else {
				//Si valores de disparo ya estan asignados a otro hilo
				enable = false;
				selected = false;
			}
		}
		this.disparo.setEnabled(enable);
		this.disparo.setSelected(selected);
		this.ordenDisparo.setEnabled(enable);
		this.metodoDisparo.setEnabled(enable);
		if (this.idHiloInforme.equals("")) {
			//Si valores de disparo no estan asignados
			enable = true;
			selected = false;
		} else {
			if (this.idHiloInforme.equals(this.idHiloActual)) {
				//Si valores de disparo ya estan asignados al hilo nuevo
				enable = true;
				selected = true;
			} else {
				//Si valores de disparo ya estan asignados a otro hilo
				enable = false;
				selected = false;
			}
		}
		this.informe.setEnabled(enable);
		this.informe.setSelected(selected);
		this.ordenInforme.setEnabled(enable);
		this.metodoInforme.setEnabled(enable);
	}
	/**
	 * Libera el disparo y/o el informe de esta transicion si estos coinciden
	 * con el idHilo.
	 * @param idHilo id del hilo que debe liberar disparo y/o informe.
	 */
	public void removerHilo(final String idHilo) {
		if (this.idHiloActual.equals(idHilo)) {
			this.idHiloActual = "";
		}
		if (this.idHiloDisparo.equals(idHilo)) {
			this.idHiloDisparo = "";
		}
		if (this.idHiloInforme.equals(idHilo)) {
			this.idHiloInforme = "";
		}
		this.cambioDeHilo();
	}
	/**
	 * Genera una instancia de TransicionProceso segun los valores seteados
	 * para el hilo dado.
	 * @param idHilo hilo al cual se le desea generar la transicion.
	 * @return la instancia de TransicionProceso para ese hilo.
	 */
	public TransicionProceso generarTransicionHilo(final String idHilo) {
		final TransicionProceso tr = new TransicionProceso(
				this.idTransicion.getText());
		if (this.idHiloDisparo.equals(idHilo)) {
			tr.setDisparo(true);
			tr.setMetodoDisparo(this.metodoDisparo.getText());
			tr.setOrdenDisparo((String) this.ordenDisparo.getSelectedItem());
		}
		if (this.idHiloInforme.equals(idHilo)) {
			tr.setInforme(true);
			tr.setMetodoInforme(this.metodoInforme.getText());
			tr.setOrdenInforme((String) this.ordenInforme.getSelectedItem());
		}
		return tr;
	}
}
