import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// ?????????????
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
//import java.util.regex.Pattern;


// for tests

import javax.swing.SwingWorker;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;

/** TODO
 * Returns an Image object that can then be painted on the screen. 
 * The url argument must specify an absolute {@link URL}. The name
 * argument is a specifier that is relative to the url argument. 
 * <p>
 * This method always returns immediately, whether or not the 
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives 
 * that draw the image will incrementally paint on the screen. 
 *
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 * @see         Image
 */
public class TraceFileViewer extends JFrame {
	private HashMap<Integer, ArrayList> sourceList;
	private HashMap<Integer, ArrayList> destinationList;

    private JPanel radioButtonPanel;
    private ButtonGroup radioButtons;
    private JRadioButton radioButtonSourceHosts;
    private JRadioButton radioButtonDestinationHosts;

    private JPanel comboBoxPanel;
    private JLabel addressToViewLabel;
    private JComboBox ipSelectionComboBox;
    private JLabel addressToFilterLabel;
    private JComboBox ipFilterComboBox;

    private JPanel graphDisplayOptionsPanel;
    private JLabel graphIntervalOptionsLabel;
    private JTextField graphIntervalOptionsField;
    private JLabel graphRangeStartLabel;
    private JLabel graphRangeFinishlabel;
    private JLabel graphXRangeLabel;
    private JTextField graphXRangeStartField;
    private JLabel graphXRangeDashLabel;
    private JTextField graphXRangeFinishField;
    private JLabel graphYRangeLabel;
    private JTextField graphYRangeStartField;
    private JLabel graphYRangeDashLabel;
    private JTextField graphYRangeFinishField;
    private JButton graphSettingsSet;
    private JButton graphSettingsClear;

    private Graph graphPanel;

	public TraceFileViewer() {
		super("Trace File Viewer");
		setLayout(new FlowLayout());
		setSize(1000,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupMenu();
		setupDataSelectionOptions();
		setupGraph();

		// this.addComponentListener(this);
		setVisible(true);
	}

	private void setupMenu() {
		/* 
		*  File
		*      Open trace file
		*      Close trace file (disabled when no trace file is open)
		*      Quit
		*  Trace file options Menu (only shown when a trace file is open)
		*      Export image
		*      Export IP data
		*      Find in explorer
		*  Preferences
		*      Color scheme
		*      Font
		*/

		// Default menuBar
		// Contains file menu and display options menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Early delcaration of components so they can be affected by functions in the file menu
		JMenu traceFileMenu = new JMenu("Trace File Options");
		JMenuItem fileMenuClose = new JMenuItem("Close trace file");

		// The file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		// File menu -> Open trace file
		JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
		fileMenuOpen.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser(".");
					// Only shows .txt files
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					int retValue = fileChooser.showOpenDialog(TraceFileViewer.this);

					if (retValue == JFileChooser.APPROVE_OPTION) {
						File filename = fileChooser.getSelectedFile();
						
						try (Scanner scanner = new Scanner(filename)) {
						
						}
						
						catch (IOException exception) {
							System.out.printf("Error reading data, re-opening file chooser");
							actionPerformed(e);
						}
						
						traceFileMenu.setVisible(true);
						fileMenuClose.setEnabled(true);
						comboBoxPanel.setVisible(true);
						updateIPSelectionList();
					}
				}
			}
		);
		fileMenu.add(fileMenuOpen);

		// File menu -> Close trace file
		fileMenuClose.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sourceList = new HashMap<Integer, ArrayList>();
					destinationList = new HashMap<Integer, ArrayList>();
					
					graphPanel.clearData();
					graphPanel.validate();
					graphPanel.repaint();

					updateIPSelectionList();

					traceFileMenu.setVisible(false);
					fileMenuClose.setEnabled(false);
					radioButtonDestinationHosts.setEnabled(false);
					radioButtonSourceHosts.setEnabled(false);
					comboBoxPanel.setVisible(false);
				}
			}
		);
		fileMenuClose.setEnabled(false);
		fileMenu.add(fileMenuClose);

		// File menu -> Quit
		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenuQuit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		fileMenu.add(fileMenuQuit);

		// The trace file options menu
		traceFileMenu.setMnemonic('T');
		menuBar.add(traceFileMenu);
		traceFileMenu.setVisible(false);

		// Trace file menu -> Export image
		JMenuItem traceFileMenuExportImage = new JMenuItem("Export image");
		traceFileMenuExportImage.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO export image of graph
				}
			}
		);
		traceFileMenu.add(traceFileMenuExportImage);

		// Trace file menu -> Export IP data
		JMenuItem traceFileMenuExportData = new JMenuItem("Export IP data");
		traceFileMenuExportData.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO export data of IP
				}
			}
		);
		traceFileMenu.add(traceFileMenuExportData);

		// Trace file menu -> Show in explorer
		JMenuItem traceFileMenuFindFile = new JMenuItem("Show in explorer");
		traceFileMenuFindFile.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO show in explorer
				}
			}
		);
		traceFileMenu.add(traceFileMenuFindFile);


		// Preferences menu
		JMenu preferencesMenu = new JMenu("Preferences");
		preferencesMenu.setMnemonic('P');
		menuBar.add(preferencesMenu);

		// Prefereces menu -> Color scheme
		JMenuItem preferencesMenuColorScheme = new JMenuItem("Color Scheme");
		preferencesMenuColorScheme.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO have color scheme selection menu
				}
			}
		);
		preferencesMenu.add(preferencesMenuColorScheme);

		JMenuItem preferencesMenuFont = new JMenuItem("Font");
		preferencesMenuFont.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO have font selection menu
				}
			}
		);
		preferencesMenu.add(preferencesMenuFont);
	}

	private void setupDataSelectionOptions() {
		// Source/Destination host radio buttons
		radioButtonPanel = new JPanel();
		radioButtonPanel.setPreferredSize(new Dimension(150, 100));
		radioButtonPanel.setLocation(0,0);
    	radioButtonPanel.setLayout(new GridBagLayout());

    	GridBagConstraints radioButtonLayout = new GridBagConstraints();
    	radioButtonLayout.gridx = 0;
    	radioButtonLayout.gridy = GridBagConstraints.RELATIVE;
    	radioButtonLayout.anchor = GridBagConstraints.WEST;

    	radioButtons = new ButtonGroup();

    	radioButtonSourceHosts = new JRadioButton("Source Hosts");
    	radioButtonSourceHosts.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateIPSelectionList();
					}
				}
	    );
    	radioButtonSourceHosts.setSelected(true);
        radioButtons.add(radioButtonSourceHosts);
        radioButtonPanel.add(radioButtonSourceHosts, radioButtonLayout);

    	radioButtonDestinationHosts = new JRadioButton("Destination Hosts");
    	radioButtonDestinationHosts.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateIPSelectionList();
					}
				}
	    );
        radioButtons.add(radioButtonDestinationHosts);
    	radioButtonPanel.add(radioButtonDestinationHosts, radioButtonLayout);

    	add(radioButtonPanel);

    	// Host selection combobox
    	comboBoxPanel = new JPanel();
		comboBoxPanel.setPreferredSize(new Dimension(350, 100));
		comboBoxPanel.setLocation(150,0);

		GridBagConstraints ipSelectionLayout = new GridBagConstraints();
    	ipSelectionLayout.gridx = GridBagConstraints.RELATIVE;
    	ipSelectionLayout.gridy = 0;

		addressToViewLabel = new JLabel("Address to view:");
		comboBoxPanel.add(addressToViewLabel, ipSelectionLayout);

    	ipSelectionComboBox = new JComboBox<String>();
		ipSelectionComboBox.setModel((MutableComboBoxModel<String>) ipSelectionComboBox.getModel());
		ipSelectionComboBox.setMaximumRowCount(8);
		ipSelectionComboBox.addItemListener(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent e) {
			    	if (e.getStateChange() == ItemEvent.SELECTED) {
			    		if (radioButtonSourceHosts.isSelected()) graphPanel.setData(sourceList.get(Packet.stringToIP((String)ipSelectionComboBox.getSelectedItem())));
						else graphPanel.setData(destinationList.get(Packet.stringToIP((String)ipSelectionComboBox.getSelectedItem())));

						graphPanel.validate();
						graphPanel.repaint();
			    	}
			    	return;
			    }
			}
		);
		ipSelectionComboBox.setMinimumSize(new Dimension(200,25));
		comboBoxPanel.add(ipSelectionComboBox, ipSelectionLayout);

		add(comboBoxPanel);
		comboBoxPanel.setVisible(false);

		// Any additional options
		// area available 500 - 1000

		/*
	    graphDisplayOptionsPanel = new JPanel();
	    graphDisplayOptionsPanel.setPreferredSize(new Dimension(500, 100));
		graphDisplayOptionsPanel.setLocation(500,0);
		graphDisplayOptionsPanel.setLayout(new GridBagLayout());

    	GridBagConstraints displayOptionsLayout = new GridBagConstraints();
    	displayOptionsLayout.gridx = GridBagConstraints.RELATIVE;
    	displayOptionsLayout.gridy = 0;

    	graphIntervalOptionsLabel = new JLabel("Interval size [s]:");
    	graphDisplayOptionsPanel.add(graphIntervalOptionsLabel, displayOptionsLayout);
    	
    	graphIntervalOptionsField = new JTextField(10);
    	displayOptionsLayout.gridwidth = 3;
    	graphDisplayOptionsPanel.add(graphIntervalOptionsField, displayOptionsLayout);

    	graphRangeStartLabel = new JLabel("Start");
    	displayOptionsLayout.gridx = 1;
    	displayOptionsLayout.gridwidth = 1;
    	displayOptionsLayout.gridy = 1;
    	graphDisplayOptionsPanel.add(graphRangeStartLabel, displayOptionsLayout);
    	graphRangeFinishlabel = new JLabel("Finish");
    	displayOptionsLayout.gridx = 3;
    	graphDisplayOptionsPanel.add(graphRangeFinishlabel, displayOptionsLayout);

    	graphXRangeLabel = new JLabel("Range of time [s]:");
    	displayOptionsLayout.gridx = 0;
    	displayOptionsLayout.gridy = 2;
    	graphDisplayOptionsPanel.add(graphXRangeLabel, displayOptionsLayout);
    	graphXRangeStartField = new JTextField(4);
    	displayOptionsLayout.gridx = GridBagConstraints.RELATIVE;
    	graphDisplayOptionsPanel.add(graphXRangeStartField, displayOptionsLayout);
    	graphXRangeDashLabel = new JLabel("-");
    	graphDisplayOptionsPanel.add(graphXRangeLabel, displayOptionsLayout);
    	graphXRangeFinishField = new JTextField(4);
    	graphDisplayOptionsPanel.add(graphXRangeFinishField, displayOptionsLayout);

    	graphYRangeLabel = new JLabel("Range of volume [bytes]:");
    	displayOptionsLayout.gridx = 0;
    	displayOptionsLayout.gridy = 3;
    	graphDisplayOptionsPanel.add(graphYRangeLabel, displayOptionsLayout);
    	graphYRangeStartField = new JTextField(4);
    	displayOptionsLayout.gridx = GridBagConstraints.RELATIVE;
    	graphDisplayOptionsPanel.add(graphYRangeStartField, displayOptionsLayout);
    	graphYRangeLabel = new JLabel("-");
    	graphDisplayOptionsPanel.add(graphYRangeLabel, displayOptionsLayout);
    	graphYRangeFinishField = new JTextField(4);
    	graphDisplayOptionsPanel.add(graphYRangeFinishField, displayOptionsLayout);


	    graphSettingsSet = new JButton("Set");
    	graphSettingsSet.addActionListener(
    		new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateGraph();
						// TODO
					}
				}
    	);
    	displayOptionsLayout.gridx = 0;
    	displayOptionsLayout.gridy = 4;
    	graphDisplayOptionsPanel.add(graphSettingsSet, displayOptionsLayout);

	    graphSettingsClear = new JButton("Clear");
    	graphSettingsClear.addActionListener(
    		new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateGraph();
						// TODO
					}
				}
    	);
	    displayOptionsLayout.gridx = GridBagConstraints.RELATIVE;
	    displayOptionsLayout.gridwidth = 3;
	    graphDisplayOptionsPanel.add(graphSettingsClear, displayOptionsLayout);


    	add(graphDisplayOptionsPanel);
    	*/
	}

	private void setupGraph() {
		graphPanel = new Graph();
		graphPanel.setPreferredSize(new Dimension(1000, 325));
		graphPanel.setLocation(0,100);
		add(graphPanel);
	}

	private void updateIPSelectionList() {
		// Sets up the selection box
		ipSelectionComboBox.removeAllItems();

	}

	private void updateGraph() {
		
	}
}