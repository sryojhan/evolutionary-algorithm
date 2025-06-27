package View;

import javax.swing.JFrame;

import javax.swing.WindowConstants;
import org.math.plot.Plot2DPanel;
import org.math.plot.plots.LinePlot;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.Random;

import javax.swing.JToolBar;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.Label;
import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.SystemColor;
import Common.Algoritmo;
import Common.CalcularFitness;
import Common.Crossing;
import Common.Individuo;
import Common.InformacionAlgoritmo;
import Common.Mutation;
import Common.Nodo;
import Common.Valor;

public class Mainframe extends JFrame {

	private JPanel window;

	private JTextField numGenTF;
	private JTextField genSizeTextField;
	private JTextField crossProbabilityTF;
	private JTextField mutationProbabilityTF;
	private JTextField tamTorneoTF;
	private JTextField solutionTF;
	private JTextField recorridoTF;
	private JTextField elitismTF;
	private JCheckBox showAll;

	private JCheckBox escaladoLineal;
	private JCheckBox controlBloating;
	private JTextField tarpeian;

	private JTextField repeticiones;

	private Plot2DPanel plot;
	private Plot2DPanel plotGrafica;

	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private final Color blue = new Color(104, 64, 255);
	private final Color green = new Color(104, 255, 104);
	private final Color red = new Color(255, 104, 104);

	// Analisis de parametros

	private JPanel panelResultados;

	private JTextField cruceMinTF;
	private JTextField cruceMaxTF;

	private JTextField mutationMinTF;
	private JTextField mutationMaxTF;

	private JTextField pobMinTF;
	private JTextField pobMaxTF;

	private JLabel mejorDistancia;
	private JLabel mejorCruce;
	private JLabel mejorMutacion;
	private JLabel mejorPoblacion;

	public Mainframe instance;
	public boolean runningThread = false;

	private void replot() {
		plot.removeAllPlots();
		plot.resetMapData();

		plotGrafica.removeAllPlots();
		plot.resetMapData();

		if (solutionTF != null) {
			solutionTF.setText("...");
			recorridoTF.setText("...");
			mejorPoblacion.setText("");
			mejorCruce.setText("");
			mejorMutacion.setText("");
			mejorDistancia.setText("");
		}

	}

	private void plot(double[] x, double[] y, Color c, String name) {

		plot.addLinePlot(name, c, x, y);
	}

	private void plotGrafica(double[] x, double[] y, Color c, String name) {

		plotGrafica.addLinePlot(name, c, x, y);
	}

	/**
	 * Create the frame.
	 */
	public Mainframe() {

		super("P3 PE - Yojhan Steven García Peña");
		instance = this;
		this.setResizable(false);
		this.setMinimumSize(new Dimension(1920, 1080));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		JPanel westSidePanel = new JPanel();
		getContentPane().add(westSidePanel, BorderLayout.WEST);
		westSidePanel.setBorder(BorderFactory.createTitledBorder("Params"));

		JLabel genSizeLabel = new JLabel("Tam. de la población");
		genSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		;
		genSizeTextField = new JFormattedTextField(numberFormat);
		genSizeTextField.setText("100");

		JLabel numGenLabel = new JLabel("Nº de generaciones");
		numGenLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		numGenTF = new JTextField();
		numGenTF.setText("100");
		numGenTF.setColumns(10);

		// showAll.setText("Mostrar todos los individuos");

		// SELECTION PANEL

		JPanel selectionPanel = new JPanel();
		selectionPanel
				.setBorder(new TitledBorder(null, "Seleccion", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel selectionTL = new JLabel("Tipo de selección");
		selectionTL.setHorizontalAlignment(SwingConstants.LEFT);
		selectionPanel.add(selectionTL);

		JComboBox selectionTypeComboBox = new JComboBox();
		selectionTypeComboBox.setModel(
				new DefaultComboBoxModel(new String[] { "Proporcional/Ruleta", "MuestreoUniversalEstoclastico",
						"Truncamiento", "TorneoDeterministico", "TorneoProbabilistico", "Restos", "Ranking" }));

		selectionPanel.add(selectionTypeComboBox);

		JLabel tamTorneolbl = new JLabel("TamTorneo");
		selectionPanel.add(tamTorneolbl);
		tamTorneoTF = new JTextField();
		tamTorneoTF.setText("3");
		tamTorneoTF.setColumns(10);
		selectionPanel.add(tamTorneolbl);
		selectionPanel.add(tamTorneoTF);

		JLabel showAllLabel = new JLabel("Mostrar todos los individuos");
		showAllLabel.setHorizontalAlignment(SwingConstants.LEFT);
		showAll = new JCheckBox();

		selectionPanel.add(showAllLabel);

		selectionPanel.add(showAll);

		// CROSS PANEL

		JPanel crossPanel = new JPanel();
		crossPanel.setBorder(new TitledBorder("Cruce"));
		crossPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel crossTL = new JLabel("Tipo de cruce");
		crossTL.setHorizontalAlignment(SwingConstants.LEFT);
		crossPanel.add(crossTL);

		JComboBox crossTypeComboBox = new JComboBox();
		crossTypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Cruce de ramas"

		}));

		crossPanel.add(crossTypeComboBox);

		JLabel crossProbabilityLabel = new JLabel("% Cruce");
		crossPanel.add(crossProbabilityLabel);

		crossProbabilityTF = new JTextField();
		crossProbabilityTF.setText("60.0");
		crossPanel.add(crossProbabilityTF);
		crossProbabilityTF.setColumns(10);

		// MUTATION PANEL

		JPanel mutationPanel = new JPanel();
		mutationPanel.setBorder(new TitledBorder("Mutacion"));
		mutationPanel.setLayout(new GridLayout(0, 2, 3, 0));

		JLabel mutationTypeLabel = new JLabel("Tipo de mutacion");
		mutationTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mutationPanel.add(mutationTypeLabel);

		JComboBox mutationTypeComboBox = new JComboBox();
		mutationTypeComboBox
				.setModel(new DefaultComboBoxModel(new String[] { "Funcional", "Terminal", "Permutacion" }));
		mutationPanel.add(mutationTypeComboBox);

		JLabel mutationProbLabel = new JLabel("% Mutacion");
		mutationPanel.add(mutationProbLabel);

		mutationProbabilityTF = new JTextField();
		mutationProbabilityTF.setText("6.0");
		mutationProbabilityTF.setColumns(10);
		mutationPanel.add(mutationProbabilityTF);

		JLabel maximizeLabel = new JLabel("Maximizar:");
		mutationPanel.add(maximizeLabel);

		// PROBLEM PANEL

		JPanel problemPanel = new JPanel();
		problemPanel.setBorder(new TitledBorder("Adicional"));
		problemPanel.setLayout(new GridLayout(0, 4, 1, 0));

		// BUTTONS
		JButton executeButton = new JButton("Ejecutar");

		JLabel elitismLabel = new JLabel("% elitismo");
		elitismLabel.setHorizontalAlignment(SwingConstants.LEFT);
		elitismTF = new JTextField();
		elitismTF.setText("0");
		problemPanel.add(elitismLabel);

		problemPanel.add(elitismTF);

		JLabel escaladoLinealLabel = new JLabel("Escalado lineal");
		elitismLabel.setHorizontalAlignment(SwingConstants.LEFT);
		escaladoLineal = new JCheckBox();

		problemPanel.add(escaladoLinealLabel);
		problemPanel.add(escaladoLineal);

		JLabel controlBloatinglLabel = new JLabel("Control bloating");
		controlBloatinglLabel.setHorizontalAlignment(SwingConstants.LEFT);
		controlBloating = new JCheckBox();

		problemPanel.add(controlBloatinglLabel);
		problemPanel.add(controlBloating);

		JLabel tarpeianLabel = new JLabel("Tarpeian N constant");
		tarpeianLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tarpeian = new JTextField();
		tarpeian.setText("2");

		problemPanel.add(tarpeianLabel);
		problemPanel.add(tarpeian);

		JLabel repeticionesLabel = new JLabel("Numero repeticiones");
		repeticionesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		repeticiones = new JTextField();
		repeticiones.setText("1");

		problemPanel.add(repeticionesLabel);
		problemPanel.add(repeticiones);

		// tarpeian

		JButton restartButton = new JButton("Reiniciar");
		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				replot();

			}

		});

		JPanel analisisPanel = new JPanel();
		analisisPanel.setBorder(new TitledBorder("Anasis de parametros"));
		analisisPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel parametros = new JPanel();
		parametros.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel cruceLabel = new JLabel("Probabilidad de cruce(5 inc)");
		cruceLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cruceMinTF = new JTextField();
		cruceMinTF.setText("40");
		cruceMaxTF = new JTextField();
		cruceMaxTF.setText("60");

		JLabel mutationLabel = new JLabel("Probabilidad de mutacion (1 inc)");
		mutationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mutationMinTF = new JTextField();
		mutationMinTF.setText("3");
		mutationMaxTF = new JTextField();
		mutationMaxTF.setText("7");

		JLabel pobLabel = new JLabel("Tamaño de la poblacion (100 inc)");
		pobLabel.setHorizontalAlignment(SwingConstants.LEFT);
		pobMinTF = new JTextField();
		pobMinTF.setText("50");
		pobMaxTF = new JTextField();
		pobMaxTF.setText("450");

		JButton ejecutar = new JButton("Ejecutar");
		ejecutar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (instance.runningThread)
					return;

				instance.runningThread = true;
				panelResultados.setBorder(new TitledBorder("Resultados (en progreso...)"));
				int nGeneraciones = Integer.parseInt(numGenTF.getText().replace(".", ""));

				double elitism = Double.parseDouble(elitismTF.getText());

				int crossingType = crossTypeComboBox.getSelectedIndex();
				int selectionType = selectionTypeComboBox.getSelectedIndex();
				int mutationType = mutationTypeComboBox.getSelectedIndex();
				int tamTorneo = Integer.parseInt(tamTorneoTF.getText());

				Runnable myRunnable = new Runnable() {
					public void run() {

						instance.AnalisisParametros(nGeneraciones, elitism, selectionType, crossingType, mutationType,
								tamTorneo);
						instance.runningThread = false;
						panelResultados.setBorder(new TitledBorder("Resultados"));
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
				// instance.AnalisisParametros(nGeneraciones, elitism, selectionType,
				// crossingType, mutationType, tamTorneo);

			}
		});
		parametros.add(cruceLabel);
		parametros.add(cruceMinTF);
		parametros.add(cruceMaxTF);

		parametros.add(mutationLabel);
		parametros.add(mutationMinTF);
		parametros.add(mutationMaxTF);

		parametros.add(pobLabel);
		parametros.add(pobMinTF);
		parametros.add(pobMaxTF);

		analisisPanel.add(parametros);
		analisisPanel.add(ejecutar);

		panelResultados = new JPanel();
		panelResultados.setBorder(new TitledBorder("Resultados"));
		panelResultados.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel mejorDistanciaLabel = new JLabel("Mejor error");
		mejorDistancia = new JLabel("");

		JLabel mejorCruceLabel = new JLabel("Mejor probabilidad de cruce");
		mejorCruce = new JLabel("");

		JLabel mejorMutacionLabel = new JLabel("Mejor probabilidad de mutacion");
		mejorMutacion = new JLabel("");

		JLabel mejorPoblacionLabel = new JLabel("Mejor tamaño de poblacion");
		mejorPoblacion = new JLabel("");

		panelResultados.add(mejorDistanciaLabel);
		panelResultados.add(mejorDistancia);

		panelResultados.add(mejorCruceLabel);
		panelResultados.add(mejorCruce);

		panelResultados.add(mejorMutacionLabel);
		panelResultados.add(mejorMutacion);

		panelResultados.add(mejorPoblacionLabel);
		panelResultados.add(mejorPoblacion);

		GroupLayout gl_westPanel = new GroupLayout(westSidePanel);
		gl_westPanel.setHorizontalGroup(gl_westPanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_westPanel.createSequentialGroup().addGap(10).addGroup(gl_westPanel
						.createParallelGroup(Alignment.LEADING)

						.addComponent(mutationPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(crossPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(selectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
								Short.MAX_VALUE)

						.addComponent(numGenTF, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						// .addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
						// Short.MAX_VALUE)
						.addComponent(genSizeTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
								Short.MAX_VALUE)
						.addComponent(genSizeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)

						.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(restartButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)

						.addComponent(problemPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(analisisPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(panelResultados, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
						.addGap(10)));
		gl_westPanel.setVerticalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup().addGap(1)
						.addComponent(genSizeLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(genSizeTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)

						.addComponent(numGenLabel).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(numGenTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)

						.addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(executeButton)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(restartButton).addGap(244)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(analisisPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panelResultados,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

		gl_westPanel.setAutoCreateGaps(true);
		gl_westPanel.setAutoCreateContainerGaps(true);
		westSidePanel.setLayout(gl_westPanel);

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(new TitledBorder(null, "Plot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		plot = new Plot2DPanel();
		plot.plotCanvas.setAutoBounds(1);
		plot.plotCanvas.setAxisLabels(new String[] { "X", "Y" });
		plot.plotCanvas.setBackground(UIManager.getColor("Button.light"));
		centerPanel.add(plot);

		plotGrafica = new Plot2DPanel();
		plotGrafica.plotCanvas.setAutoBounds(1);
		plotGrafica.plotCanvas.setAxisLabels(new String[] { "X", "Y" });
		plotGrafica.plotCanvas.setBackground(UIManager.getColor("Button.light"));
		centerPanel.add(plotGrafica);
		replot();

		JPanel solutionPanel = new JPanel();
		centerPanel.add(solutionPanel);

		JLabel solutionLabel = new JLabel("Solution:");
		solutionLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		solutionLabel.setHorizontalAlignment(SwingConstants.LEFT);

		solutionTF = new JTextField();
		solutionTF.setText("solution here");
		solutionTF.setEditable(false);
		solutionTF.setColumns(10);

		recorridoTF = new JTextField();
		recorridoTF.setEditable(false);
		recorridoTF.setColumns(10);
		recorridoTF.setHorizontalAlignment(SwingConstants.CENTER);

		GroupLayout gl_solutionPanel = new GroupLayout(solutionPanel);
		gl_solutionPanel.setHorizontalGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_solutionPanel.createSequentialGroup().addContainerGap().addComponent(solutionLabel)
						.addGap(18).addComponent(recorridoTF, 100, 100, 200)
						.addComponent(solutionTF, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)

						.addContainerGap()));
		gl_solutionPanel
				.setVerticalGroup(
						gl_solutionPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING,
										gl_solutionPanel.createSequentialGroup().addContainerGap()
												.addGroup(gl_solutionPanel.createParallelGroup(Alignment.TRAILING)
														.addComponent(solutionTF, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(10)
														.addComponent(recorridoTF, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(solutionLabel, GroupLayout.DEFAULT_SIZE, 14,
																Short.MAX_VALUE)

												).addGap(11)));
		solutionPanel.setLayout(gl_solutionPanel);

		executeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int tamPoblacion = Integer.parseInt(genSizeTextField.getText().replace(".", ""));
				int nGeneraciones = Integer.parseInt(numGenTF.getText().replace(".", ""));

				double elitism = Double.parseDouble(elitismTF.getText());
				double probCruce = Double.parseDouble(crossProbabilityTF.getText());
				double probMutacion = Double.parseDouble(mutationProbabilityTF.getText());

				int crossingType = crossTypeComboBox.getSelectedIndex();
				int selectionType = selectionTypeComboBox.getSelectedIndex();
				int mutationType = mutationTypeComboBox.getSelectedIndex();
				int tamTorneo = Integer.parseInt(tamTorneoTF.getText());

				int tarpeianIdx = Integer.parseInt(tarpeian.getText().replace(".", ""));
				boolean bloating = controlBloating.isSelected();
				boolean escalado = escaladoLineal.isSelected();
				
				int n = Integer.parseInt(repeticiones.getText().replace(".", ""));
				

				Algoritmo algoritmo = EjecutarAlgoritmo(tamPoblacion, nGeneraciones, elitism, probCruce, probMutacion,
						selectionType, crossingType, mutationType, tamTorneo, escalado, bloating, tarpeianIdx, n);

				PlotAlgoritmo(algoritmo, showAll.isSelected(), false);

			}
		});
		window = new JPanel();
		window.setLayout(new BorderLayout());

		this.setVisible(true);
	}

	private Algoritmo EjecutarAlgoritmo(int tamPoblacion, int nGeneraciones, double elitism, double probCruce,
			double probMutacion, int selectionType, int crossingType, int mutationType, int tamTorneo,
			boolean escaladoLineal, boolean bloating, int bloatingIndex, int repeticiones) {

		InformacionAlgoritmo info = null;

		info = new InformacionAlgoritmo(false);

		info.fitnessFunction = (input) -> {

			return CalcularFitness.CalculateFitness(input);

		};
		double best = Double.MAX_VALUE;

		Algoritmo mejor = null;

		for (int i = 0; i < repeticiones; i++) {

			Algoritmo algoritmo = new Algoritmo(tamPoblacion, nGeneraciones, info);

			algoritmo.SetSelection(selectionType, tamTorneo);
			algoritmo.SetElitism(elitism * 0.01);
			algoritmo.SetMutation(mutationType, probMutacion * 0.01);
			algoritmo.SetCrossing(crossingType, probCruce * 0.01);

			algoritmo.SetEscaladoLineal(escaladoLineal);
			algoritmo.SetBloating(bloating, bloatingIndex);

			CalcularFitness.GenerarDataSet();

			double value = algoritmo.run();

			if (algoritmo == null || value < best) {

				mejor = algoritmo;
				best = value;
			}

		}

		return mejor;

	}

	private String EscribirEcuacion(Nodo n) {

		Valor v = n.GetValor();
		if (v.esFuncion()) {

			switch (v.GetFuncion()) {
			case add:
				return "(" + EscribirEcuacion(n.Izquierdo()) + " + " + EscribirEcuacion(n.Derecho()) + ")";
			case mult:
				return "(" + EscribirEcuacion(n.Izquierdo()) + " * " + EscribirEcuacion(n.Derecho()) + ")";
			case sub:
				return "(" + EscribirEcuacion(n.Izquierdo()) + " - " + EscribirEcuacion(n.Derecho()) + ")";
			}

		}

		switch (v.GetTerminal()) {
		case Cero:
			return "0";
		case Dos:
			return "2";
		case MenosDos:
			return "-2";
		case MenosUno:
			return "-1";
		case Uno:
			return "1";
		case x:
			return "x";

		}
		return "";
	}

	private void PlotAlgoritmo(Algoritmo algoritmo, boolean showAllFitness, boolean clearPrevious) {

		Nodo mejorIndividuo = algoritmo.mejor;

		String ecuacionSolucion = EscribirEcuacion(mejorIndividuo);

		recorridoTF.setText("" + decimalFormat.format(algoritmo.MejorResultado()));
		solutionTF.setText(ecuacionSolucion);

		if (clearPrevious) {
			plot.removeAllPlots();
			plot.resetMapData();

			plotGrafica.removeAllPlots();
			plotGrafica.resetMapData();

		}

		int nGeneraciones = algoritmo.FitnessMedio().length;

		double[] generaciones = new double[nGeneraciones];

		for (int i = 0; i < nGeneraciones; i++) {

			generaciones[i] = i;
		}

		plot(generaciones, algoritmo.MejorFitness().clone(), red, "Mejor de cada generacion");
		plot(generaciones, algoritmo.FitnessMedio().clone(), green, "Media de cada generacion");
		plot(generaciones, algoritmo.MejorFitnessAbsoluto().clone(), blue, "Mejor hasta el momento");

		int numeroPuntos = CalcularFitness.numeroPuntos;
		double[] ejeX = new double[numeroPuntos];
		double[] ejeYCalculado = new double[numeroPuntos];
		double[] ejeYReal = new double[numeroPuntos];
		double incremento = 2.0 / (numeroPuntos - 1);

		for (int i = 0; i < numeroPuntos; i++) {

			double x = -1 + i * incremento;

			ejeX[i] = x;
			ejeYReal[i] = CalcularFitness.EvaluateDataset(x);
			ejeYCalculado[i] = CalcularFitness.CalcularFitnessDeRama(mejorIndividuo, x);
		}

		plotGrafica(ejeX, ejeYReal, red, "Funcion original");
		plotGrafica(ejeX, ejeYCalculado, blue, "Funcion calculada");

		showAllFitness = showAll.isSelected();

		if (showAllFitness) {

			var all = algoritmo.allFitness;

			Random rnd = new Random();

			for (int i = 0; i < all.length; i++) {

				plot(generaciones, all[i], new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)),
						"Todas las generaciones");

			}

		}

	}

	public void AnalisisParametros(int nGeneraciones, double elitism, int selectionType, int crossingType,
			int mutationType, int tamTorneo) {

		int incPoblacion = 100;
		double incMutacion = 1;
		double incCruce = 5;

		int poblacionMin = Integer.parseInt(pobMinTF.getText().replace(".", ""));
		int poblacionMax = Integer.parseInt(pobMaxTF.getText().replace(".", ""));
		double mutacionMin = Integer.parseInt(mutationMinTF.getText());
		double mutacionMax = Integer.parseInt(mutationMaxTF.getText());
		double cruceMin = Integer.parseInt(cruceMinTF.getText());
		double cruceMax = Integer.parseInt(cruceMaxTF.getText());

		boolean escalado = escaladoLineal.isSelected();

		int tarpeianIdx = Integer.parseInt(tarpeian.getText().replace(".", ""));
		boolean bloating = controlBloating.isSelected();
		int n = Integer.parseInt(repeticiones.getText().replace(".", ""));
		
		Algoritmo mejor = null;

		for (int p = poblacionMin; p < poblacionMax; p += incPoblacion) {
			for (double m = mutacionMin; m < mutacionMax; m += incMutacion) {
				for (double c = cruceMin; c < cruceMax; c += incCruce) {

					Algoritmo algoritmo = EjecutarAlgoritmo(p, nGeneraciones, elitism, c, m, selectionType,
							crossingType, mutationType, tamTorneo, escalado, bloating, tarpeianIdx, n);

					double nuevoResultado = algoritmo.MejorResultado();

					if (mejor == null || nuevoResultado < mejor.MejorResultado()) {

						mejor = algoritmo;

						PlotAlgoritmo(mejor, showAll.isSelected(), true);

						try { // No entiendo como funciona exactamente pero solo he conseguido que se
								// actualicen los textos poniendo un sleep
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mejorPoblacion.setText("" + p);
						mejorCruce.setText("" + c + "%");
						mejorMutacion.setText("" + m + "%");

						mejorDistancia.setText("" + nuevoResultado);

					}

				}
			}
		}

	}

}
