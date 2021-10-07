package com.hyperspc.zmmg;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

public class Main implements ActionListener{

	/* Project Setting & Variables */
	private static String projectName = "New Music Mod", authorName = "Your username", mainCategory = projectName, logoPath = "";
	private static boolean useDefaultLogo = true;
	
	private String className = "MyMusicClass"; // Hard-coded for now, will change in future to allow multiple custom music classes.
	
	private static ArrayList<Track> trackList = new ArrayList<>();
	private static DefaultListModel<String> trackNames = new DefaultListModel();
	
	private static JList list;
	
	private static JFrame frmZeusMusicMod;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmZeusMusicMod.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmZeusMusicMod = new JFrame();
		frmZeusMusicMod.setTitle("Zeus Music Mod Generator");
		frmZeusMusicMod.setBounds(100, 100, 450, 500);
		frmZeusMusicMod.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmZeusMusicMod.getContentPane().setLayout(new BorderLayout(0, 0));
		
		 try {
	            // Set System L&F
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }catch (Exception e) {
			System.out.println("System look and feel not found.");
		} 
		
		JMenuBar menuBar = new JMenuBar();
		frmZeusMusicMod.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenu mnProjectSettings = new JMenu("Project Settings");
		mnNewMenu.add(mnProjectSettings);
		
		JMenuItem generalSettingsButton = new JMenuItem("General");
		generalSettingsButton.addActionListener(this);
		mnProjectSettings.add(generalSettingsButton);
		
		JMenuItem addCoverImageButton = new JMenuItem("Add Cover Image (.paa)");
		addCoverImageButton.setToolTipText("Include an image that will be visible in the Arma 3 Launcher. Must be .paa format.");
		addCoverImageButton.addActionListener(this);
		mnProjectSettings.add(addCoverImageButton);
		
		JMenuItem exportButton = new JMenuItem("Export...");
		mnNewMenu.add(exportButton);
		
		JMenuItem exitButton = new JMenuItem("Exit");
		mnNewMenu.add(exitButton);
		
		JMenu mnNewMenu_1 = new JMenu("Tools");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("MP3 to OGG");
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		JMenu mnNewMenu_2 = new JMenu("Help");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("About");
		mnNewMenu_2.add(mntmNewMenuItem_4);
		
		JPanel panel = new JPanel();
		frmZeusMusicMod.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		list = new JList(trackNames);
		ListAction la = new ListAction(list, displayAction);
		JScrollPane scrollPane = new JScrollPane(list);
		panel.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		frmZeusMusicMod.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnAddSong = new JButton("Add Songs");
		btnAddSong.setToolTipText("Select multiple songs to add to the list.");
		btnAddSong.addActionListener(this);
		panel_1.add(btnAddSong);
		
		JButton btnRemoveSong = new JButton("Remove Song");
		btnRemoveSong.setToolTipText("Remove selected song from list.");
		btnRemoveSong.addActionListener(this);
		panel_1.add(btnRemoveSong);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("General")) {
			showProjectSettings();
		}else if(e.getActionCommand().equals("Add Songs")) {
			addSong();
		}else if(e.getActionCommand().equals("Add Cover Image (.paa)")) {
			addCoverImage();
		}else if(e.getActionCommand().equals("Remove Song")) {
			removeSong();
		}
	}
	
	Action displayAction = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e)
	    {
	        JList list = (JList)e.getSource();
	        if(list.getSelectedIndex() !=-1) {
	        	Track track = trackList.get(list.getSelectedIndex());
	        	System.out.println(track.toString());
		        JSlider db = new JSlider(JSlider.HORIZONTAL, -10, 5, track.getDecibels());
		        db.setMajorTickSpacing(5);
		        db.setMinorTickSpacing(1);
		        db.setPaintTicks(true);
		        db.setPaintLabels(true);
		        db.setToolTipText("Increase or decrease volume of track. Warning: Setting too loud can result in earrape.");
		        
		        JTextField trackName = new JTextField(track.getTrackName());
		        trackName.setToolTipText("Name of the track that will appear in game.");
		        JTextField tag = new JTextField(track.getTag());
		        tag.setToolTipText("Will appear in front of the track name: [Tag] Track Name");
		        JTextField durationField = new JTextField(""+track.getDuration());
		        durationField.setToolTipText("Track duration in seconds. Will appear next to track name in game.");
		        JPanel panel = new JPanel(new GridLayout(0, 1));
		        panel.add(new JLabel("Track Name:"));
		        panel.add(trackName);
		        panel.add(new JLabel("Tag:"));
		        panel.add(tag);
		        panel.add(new JLabel("Duration (seconds):"));
		        panel.add(durationField);
		        panel.add(new JLabel("Decibels (dB):"));
		        panel.add(db);
		        
		        int result = JOptionPane.showConfirmDialog(null, panel, "Metadata Editor",
		            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
		        
		        track.setTrackName(trackName.getText());
		        track.setTag(tag.getText());
		        try {
		        	track.setDuration(Integer.parseInt(durationField.getText()));
		        }catch(NumberFormatException ex) {
		        	JOptionPane.showMessageDialog(null,"Invalid duration. Make sure it is an integer number (no decimals).","Invalid Input",JOptionPane.ERROR_MESSAGE);
		        }
		        track.setDecibels(db.getValue());
		        
	        }
	    }
	};
	
	
	public static void removeSong() {
		if(list.getSelectedIndex()!=-1) {
			trackList.remove(list.getSelectedIndex());
			trackNames.remove(list.getSelectedIndex());
		}
	}
	
	public static void addSong() {
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("OGG files", "ogg");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
		int returnVal = fc.showOpenDialog(frmZeusMusicMod);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            
            for(int i=0;i<files.length;i++) {
            	Track newTrack = new Track(files[i].getAbsolutePath(),files[i].getName().substring(0,files[i].getName().length()-4),null,null,0,0);
            	trackList.add(newTrack);
            	trackNames.addElement(newTrack.getTrackName());
            }
        } else {
            System.out.println("Open command cancelled by user.");
        }
	}
	
	public static void addCoverImage() {
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PAA files", "paa");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
		int returnVal = fc.showOpenDialog(frmZeusMusicMod);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            System.out.println("PAA file: " + file.getAbsolutePath());
            
            logoPath = file.getAbsolutePath();
            useDefaultLogo = false;
            JOptionPane.showMessageDialog(frmZeusMusicMod,"File " + file.getName() + " has been added successfully. Path can be found in File>Project Settings>General.");
            
        } else {
            System.out.println("Open command cancelled by user.");
        }
	}
	
	public static void showProjectSettings() {
		JTextField modName = new JTextField(projectName);
		modName.setToolTipText("The name of the mod that will be visible on Steam, in the Arma 3 Launcher, and in game.");
        JTextField author = new JTextField(authorName);
        author.setToolTipText("The author of this mod, can be any arbitrary name. Visible in Arma 3 Launcher.");
        JTextField logoField = new JTextField(logoPath);
        JCheckBox defaultLogoCheckbox = new JCheckBox("Use Default Logo");
        defaultLogoCheckbox.setToolTipText("If this option is selected, the standard logo will be used instead of the one provided in 'Logo Path'.");
        defaultLogoCheckbox.setSelected(useDefaultLogo);
        logoField.setEditable(false);
        logoField.setToolTipText("To change logo path, go to File>Project Settings>Add Cover Image (.paa).");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Mod Name:"));
        panel.add(modName);
        panel.add(new JLabel("Author:"));
        panel.add(author);
        panel.add(new JLabel("Logo Path:"));
        panel.add(logoField);
        panel.add(defaultLogoCheckbox);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Project Settings",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        projectName = modName.getText();
        authorName = author.getText();
        useDefaultLogo = defaultLogoCheckbox.isSelected();
        
	}

}
