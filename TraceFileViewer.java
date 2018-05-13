import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ?????????????
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;

public class TraceFileViewer extends JFrame {

	public TraceFileViewer() {
		super("Trace File Viewer");
    	setLayout(new FlowLayout());
    	setSize(1000,500);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	setupMenu();

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
		//fileOpenMenuBar.add(fileMenu);

		// File menu -> Open trace file
		JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
		fileMenuOpen.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser(".");
					// By default, only shows .txt files
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					int retValue = fileChooser.showOpenDialog(TraceFileViewer.this);

					if (retValue == JFileChooser.APPROVE_OPTION) {
						File f = fileChooser.getSelectedFile();
						// TODO import files properly

						traceFileMenu.setVisible(true);
						fileMenuClose.setEnabled(true);
					}
				}
			}
        );
		fileMenu.add(fileMenuOpen);

		// File menu -> Close trace file
		fileMenuClose.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO clear all trace file variables

					traceFileMenu.setVisible(false);
					fileMenuClose.setEnabled(false);
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
}