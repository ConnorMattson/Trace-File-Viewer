import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;

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

    private Graph graphPanel;
    private Boolean graphActive = false;

	/**
 	* Starts the Trace File Viewer window
 	*/
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
		*/

		// Default menuBar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// The file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		// Initialises the close trace file menu item early so it may be changed by
		// the open trace file menu item
		JMenuItem fileMenuClose = new JMenuItem("Close trace file");

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
							sourceList = new HashMap<Integer, ArrayList>();
							destinationList = new HashMap<Integer, ArrayList>();
							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();

								if (line.matches(".*192\\.168\\.0\\..*")) {
									String[] splitLine = line.split("\t");
									short packetSize = Short.parseShort(splitLine[7]);
									if (packetSize > 0) {
									
										int sourceIP = Packet.stringToIP(splitLine[2]);
										int destinationIP = Packet.stringToIP(splitLine[4]);
										Packet currentPacket = new Packet(sourceIP, destinationIP, packetSize, Float.parseFloat(splitLine[1]));

										// Updates source list
										if (sourceList.containsKey(sourceIP)) sourceList.get(sourceIP).add(currentPacket);
										else {
											ArrayList<Packet> thisIPArrayList = new ArrayList<Packet>();
											thisIPArrayList.add(currentPacket);
											sourceList.put(sourceIP, thisIPArrayList);
										}

										// Updates destination list
										if (destinationList.containsKey(destinationIP)) destinationList.get(destinationIP).add(currentPacket);
										else {
											ArrayList<Packet> thisIPArrayList = new ArrayList<Packet>();
											thisIPArrayList.add(currentPacket);
											destinationList.put(destinationIP, thisIPArrayList);
										}
									}
								}	
							}
						
						}
						
						catch (IOException exception) {
							System.out.printf("Error reading data, re-opening file chooser");
							actionPerformed(e);
						}
						
						graphActive = true;
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
					graphActive = false;
					
					graphPanel.clearData();
					graphPanel.validate();
					graphPanel.repaint();

					updateIPSelectionList();

					fileMenuClose.setEnabled(false);
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
						if (graphActive) updateIPSelectionList();
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
						if (graphActive) updateIPSelectionList();
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

		Set<Integer> ipSet;
		if (radioButtonSourceHosts.isSelected()) ipSet = sourceList.keySet();
		else ipSet = destinationList.keySet();
		List<Integer> ipList = new ArrayList<Integer>(ipSet);
		Collections.sort(ipList);

		// Because of the way I store IPs, anything with a suffix greater than .128
		// is negative (and when sorted is added to the front of the list).
		for (Integer ip : ipList) {
			if (ip >= 0)	ipSelectionComboBox.addItem(Packet.ipToString(ip));
		}
		for (Integer ip : ipList) {
			if (ip < 0)	ipSelectionComboBox.addItem(Packet.ipToString(ip));
		}
	}
}