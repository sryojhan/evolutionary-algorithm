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
import Common.Individuo;
import Common.InformacionAlgoritmo;

public class Mainframe extends JFrame {

	private JPanel window;

	private JTextField numGenTF;
	private JTextField genSizeTextField;
	private JTextField crossProbabilityTF;
	private JTextField mutationProbabilityTF;
	private JTextField tamTorneoTF;
	private JTextField solutionTF;
	private JTextField dimTF;
	private JTextField elitismTF;
	private JTextField toleranceTF;
	private JCheckBox showAll;
	
	private Plot2DPanel plot;

	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	DecimalFormat decimalFormat = (DecimalFormat) numberFormat;

	private final Color blue = new Color(104, 64, 255);
	private final Color green = new Color(104, 255, 104);
	private final Color red = new Color(255, 104, 104);

	private void replot() {
		plot.removeAllPlots();
		plot.resetMapData();

		if (solutionTF != null)
			solutionTF.setText("");

	}

	private void plot(double[] x, double[] y, Color c, String name) {

		plot.addLinePlot(name, c, x, y);
	}

	/**
	 * Create the frame.
	 */
	public Mainframe() {
		super("P1 PE - Yojhan Steven García Peña");
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

		
		//showAll.setText("Mostrar todos los individuos");
		
		JLabel toleranceLabel = new JLabel("Tolerancia");
		toleranceLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		toleranceTF = new JTextField();
		toleranceTF.setText("0.001");
		toleranceTF.setColumns(10);

		JLabel dimensionLabel = new JLabel("Dimension");
		dimensionLabel.setHorizontalAlignment(SwingConstants.LEFT);

		
		
		dimTF = new JTextField();
		dimTF.setText("2");

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
						"Truncamiento", "TorneoDeterministico", "TorneoProbabilistico", "Restos" }));

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
		crossTypeComboBox.setModel(
				new DefaultComboBoxModel(new String[] { "Cruce Monopunto", "Cruce Uniforme", "Cruce Aritmetico", "Cruce BLX - Alpha"

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
		mutationTypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Mutacion basica" }));
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
		problemPanel.setBorder(new TitledBorder("Problema"));
		problemPanel.setLayout(new GridLayout(0, 4, 1, 0));

		JLabel lblSelectProblema = new JLabel("Selecciona problema");
		lblSelectProblema.setHorizontalAlignment(SwingConstants.LEFT);
		problemPanel.add(lblSelectProblema);

		JComboBox problemTypeComboBox = new JComboBox();
		problemTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] { "Calibracion y prueba", "GrieWank", "Styblinski-tang", "Michalewicz", "Michalewicz (Reales)"

				}));
		problemPanel.add(problemTypeComboBox);

		// BUTTONS
		JButton executeButton = new JButton("Ejecutar");

		JLabel elitismLabel = new JLabel("% elitismo");
		elitismLabel.setHorizontalAlignment(SwingConstants.LEFT);
		elitismTF = new JTextField();
		elitismTF.setText("0");
		problemPanel.add(elitismLabel);

		problemPanel.add(elitismTF);


		
		
		JButton restartButton = new JButton("Reiniciar");
		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				replot();

			}

		});

		GroupLayout gl_westPanel = new GroupLayout(westSidePanel);
		gl_westPanel.setHorizontalGroup(gl_westPanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_westPanel
						.createSequentialGroup().addGap(10).addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)

								.addComponent(mutationPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)
								.addComponent(crossPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(selectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)

								.addComponent(numGenTF, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)
								.addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)

								.addComponent(genSizeTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)
								.addComponent(genSizeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)

								.addComponent(toleranceTF, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)
								.addComponent(toleranceLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)

								.addComponent(dimTF, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(dimensionLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
										Short.MAX_VALUE)

								.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(restartButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)

								.addComponent(problemPanel, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
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

						.addComponent(toleranceLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(toleranceTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)

						.addComponent(dimensionLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(dimTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)

						.addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(executeButton)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(restartButton).addGap(244)));
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

		GroupLayout gl_solutionPanel = new GroupLayout(solutionPanel);
		gl_solutionPanel.setHorizontalGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_solutionPanel.createSequentialGroup().addContainerGap().addComponent(solutionLabel)
						.addGap(18).addComponent(solutionTF, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
						.addContainerGap()));
		gl_solutionPanel
				.setVerticalGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_solutionPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_solutionPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(solutionTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(solutionLabel, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
								.addGap(11)));
		solutionPanel.setLayout(gl_solutionPanel);

		executeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int tamPoblacion = Integer.parseInt(genSizeTextField.getText().replace(".", ""));
				int nGeneraciones = Integer.parseInt(numGenTF.getText().replace(".", ""));
				int dim = Integer.parseInt(dimTF.getText());
				double elitism = Double.parseDouble(elitismTF.getText());
				double probCruce = Double.parseDouble(crossProbabilityTF.getText());
				double probMutacion = Double.parseDouble(mutationProbabilityTF.getText());
				double tolerance = Double.parseDouble(toleranceTF.getText());

				int crossingType = crossTypeComboBox.getSelectedIndex();
				int selectionType = selectionTypeComboBox.getSelectedIndex();
				int mutationType = mutationTypeComboBox.getSelectedIndex();
				int tamTorneo = Integer.parseInt(tamTorneoTF.getText());

				InformacionAlgoritmo info = null;

				switch (problemTypeComboBox.getSelectedIndex()) {
				case 0:

					info = new InformacionAlgoritmo(2, true);

					info.minimos[0] = -3.0;
					info.maximos[0] = 12.1;

					info.minimos[1] = 4.1;
					info.maximos[1] = 5.8;

					info.fitnessFunction = (input) -> {

						return 21.5 + input[0] * Math.sin(4 * Math.PI * input[0])
								+ input[1] * Math.sin(20 * Math.PI * input[1]);
					};

					break;

				case 1: {
					int d = 2;
					info = new InformacionAlgoritmo(d, false);

					for (int i = 0; i < d; i++) {
						info.minimos[i] = -600;
						info.maximos[i] = 600;

					}

					info.fitnessFunction = (input) -> {

						double sum = 0;
						double prod = 1;
						for (int i = 0; i < input.length; i++) {

							sum += Math.pow(input[i], 2) / 4000.0;
							prod *= Math.cos(input[i] / Math.sqrt(i + 1));
						}

						return sum - prod + 1;
					};

					break;

				}
				case 2: {

					int d = 2;
					info = new InformacionAlgoritmo(d, false);

					for (int i = 0; i < d; i++) {
						info.minimos[i] = -5;
						info.maximos[i] = 5;

					}

					info.fitnessFunction = (input) -> {

						double sum = 0;

						for (int i = 0; i < input.length; i++) {

							double xi = input[i];
							sum += Math.pow(xi, 4) - 16 * Math.pow(xi, 2) + 5 * xi;

						}

						sum *= 0.5f;

						return sum;
					};

					break;
				}

				case 3: {

					
					info = new InformacionAlgoritmo(dim, false);

					for (int i = 0; i < dim; i++) {
						info.minimos[i] = 0;
						info.maximos[i] = Math.PI;

					}

					info.fitnessFunction = (input) -> {

						
						double sum = 0;
						
						for(int i = 0; i < input.length; i++) {
							
							double x = input[i];
							sum += Math.sin(x) * Math.pow(Math.sin((i + 1) * x * x / Math.PI), 20);
							
						}
						
						return -sum;
					};

					break;
					
					
					
				}


				case 4: {

					
					info = new InformacionAlgoritmo(dim, false);

					for (int i = 0; i < dim; i++) {
						info.minimos[i] = 0;
						info.maximos[i] = Math.PI;

					}
					
					info.tipoGen = Individuo.Type.Real;
					info.fitnessFunction = (input) -> {

						
						double sum = 0;
						
						for(int i = 0; i < input.length; i++) {
							
							double x = input[i];
							sum += Math.sin(x) * Math.pow(Math.sin((i + 1) * x * x / Math.PI), 20);
							
						}
						
						return -sum;
					};

					break;
					
					
					
				}

				
				default:

					info = new InformacionAlgoritmo(2, true);

					info.minimos[0] = -3.0;
					info.maximos[0] = 12.1;

					info.minimos[1] = 4.1;
					info.maximos[1] = 5.8;

					info.fitnessFunction = (input) -> {

						return 21.5 + input[0] * Math.sin(4 * Math.PI * input[0])
								+ input[1] * Math.sin(20 * Math.PI * input[1]);
					};

					break;
				}

				solutionTF.setText("Cargando...");

				Algoritmo algoritmo = new Algoritmo(tamPoblacion, nGeneraciones, info);

				algoritmo.SetTolerance(tolerance);
				algoritmo.SetSelection(selectionType, tamTorneo);
				algoritmo.SetElitism(elitism * 0.01);
				algoritmo.SetMutation(mutationType, probMutacion * 0.01);
				algoritmo.SetCrossing(crossingType, probCruce * 0.01);

				double solution = algoritmo.run();

				String puntoSolucionStr = "";
				
				double[] puntoSolucion = algoritmo.PuntoSolucion();
				
				for(int i = 0; i < puntoSolucion.length; i++) {
					
					puntoSolucionStr += "x" + (i + 1) + "=" + puntoSolucion[i] + ", ";
				}
				
				
				puntoSolucionStr = puntoSolucionStr.substring(0, puntoSolucionStr.length() - 2);
				
				solutionTF.setText("Resultado: " + solution + " en " + puntoSolucionStr);

				//System.out.println(info.fitnessFunction.CalculateFitness(puntoSolucion));
				
				double[] generaciones = new double[nGeneraciones];

				for (int i = 0; i < nGeneraciones; i++) {

					generaciones[i] = i;
				}

				plot(generaciones, algoritmo.MejorFitness(), red, "Mejor de cada generacion");
				plot(generaciones, algoritmo.FitnessMedio(), green, "Media de cada generacion");
				plot(generaciones, algoritmo.MejorFitnessAbsoluto(), blue, "Mejor hasta el momento");

				boolean showAllFitness = showAll.isSelected();

				if (showAllFitness) {

					var all = algoritmo.allFitness;

					Random rnd = new Random();

					for (int i = 0; i < all.length; i++) {

						plot(generaciones, all[i], new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)),
								"Todas las generaciones");

					}

				}

			}

		});
		window = new JPanel();
		window.setLayout(new BorderLayout());

		this.setVisible(true);
	}
}
