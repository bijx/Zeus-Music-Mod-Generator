/*
 * * * * * * * * * * * * * * * * * * * * *  *
 * A3 Zeus Music Mod Generator				*
 * by bijx									*
 * 											*
 * http://zeusmissiongen.com/				*
 * * * * * * * * * * * * * * * * * * * * *  *
 */
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import javax.swing.JList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import java.awt.Toolkit;

public class Main implements ActionListener {

	/* Project Setting & Variables */
	private static String projectName = "New Music Mod", authorName = "Your username", logoPath = "";
	private static boolean useDefaultLogo = true;

	// private String className = "MyMusicClass"; //Hard-coded for now, will change
	// in future to allow multiple custom music classes.

	private static ArrayList<Track> trackList = new ArrayList<>();
	private static DefaultListModel<String> trackNames = new DefaultListModel<String>();

	private static JList<String> list;

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
		frmZeusMusicMod.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/hyperspc/zmmg/zmmg.png")));
		frmZeusMusicMod.setTitle("Zeus Music Mod Generator");
		frmZeusMusicMod.setBounds(100, 100, 450, 500);
		frmZeusMusicMod.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmZeusMusicMod.getContentPane().setLayout(new BorderLayout(0, 0));

		frmZeusMusicMod.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frmZeusMusicMod, "Are you sure you want to exit?", "Confirm Exit",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					frmZeusMusicMod.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
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
		addCoverImageButton
				.setToolTipText("Include an image that will be visible in the Arma 3 Launcher. Must be .paa format.");
		addCoverImageButton.addActionListener(this);
		mnProjectSettings.add(addCoverImageButton);

		JMenuItem exportButton = new JMenuItem("Export...");
		exportButton.addActionListener(this);
		mnNewMenu.add(exportButton);

		JMenuItem exitButton = new JMenuItem("Exit");
		exitButton.addActionListener(this);
		mnNewMenu.add(exitButton);

		JMenu mnNewMenu_1 = new JMenu("Tools");
		menuBar.add(mnNewMenu_1);

		JMenuItem mp3OggConvertButton = new JMenuItem("MP3 to OGG");
		mp3OggConvertButton.addActionListener(this);

		JMenuItem songCountButton = new JMenuItem("Track Count");
		songCountButton.addActionListener(this);

		JMenuItem buildAddonButton = new JMenuItem("Build Addon...");
		buildAddonButton.addActionListener(this);
		mnNewMenu_1.add(buildAddonButton);
		mnNewMenu_1.add(songCountButton);

		JMenuItem removeAllTracksButton = new JMenuItem("Remove all Tracks");
		removeAllTracksButton.addActionListener(this);
		mnNewMenu_1.add(removeAllTracksButton);
		mnNewMenu_1.add(mp3OggConvertButton);

		JMenu mnNewMenu_2 = new JMenu("Help");
		menuBar.add(mnNewMenu_2);

		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.addActionListener(this);
		mnNewMenu_2.add(aboutButton);

		JPanel panel = new JPanel();
		frmZeusMusicMod.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		list = new JList<String>(trackNames);
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

	/**
	 * ActionListener method that reacts to various UI events such as menu or button
	 * clicks.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("General")) {
			showProjectSettings();
		} else if (e.getActionCommand().equals("Add Songs")) {
			addSong();
		} else if (e.getActionCommand().equals("Add Cover Image (.paa)")) {
			addCoverImage();
		} else if (e.getActionCommand().equals("Remove Song")) {
			removeSong();
		} else if (e.getActionCommand().equals("MP3 to OGG")) {
			JOptionPane.showMessageDialog(null, "This feature will be implemented in a future update.",
					"Feature Unavailable", JOptionPane.WARNING_MESSAGE);
		} else if (e.getActionCommand().equals("Exit")) {
			int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (n == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
		} else if (e.getActionCommand().equals("Track Count")) {
			JOptionPane.showMessageDialog(null,
					"There are currently " + trackList.size() + " tracks loaded in the list.", "Track Count",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getActionCommand().equals("Export...")) {
			exportDialog();
		} else if (e.getActionCommand().equals("Remove all Tracks")) {
			int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove all tracks?",
					"Remove All Tracks", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (n == JOptionPane.OK_OPTION) {
				trackList.clear();
				trackNames.clear();
			}
		} else if (e.getActionCommand().equals("Build Addon...")) {
			buildAddon();
		} else if (e.getActionCommand().equals("About")) {
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://github.com/bijx/Zeus-Music-Mod-Generator"));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frmZeusMusicMod,
						"Unable to open about webpage :( \nFind documentation on the GitHub project page: https://github.com/bijx/Zeus-Music-Mod-Generator",
						"About Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	

	/**
	 * Method that shows export settings dialog prior to full export.
	 */
	public static void exportDialog() {
		JCheckBox useTagsCheckbox = new JCheckBox("Append tags to track names");
		useTagsCheckbox.setSelected(true);
		JCheckBox defaultLogoCheckbox = new JCheckBox("Use default logo");
		defaultLogoCheckbox.setToolTipText(
				"If this option is selected, the standard logo will be used instead of the one provided in 'Logo Path'.");
		defaultLogoCheckbox.setSelected(useDefaultLogo);
		useTagsCheckbox
				.setToolTipText("If selected, tags will be exported in front of track names in game: [Tag] Track Name");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Mod will be structured in a folder with the name " + projectName
				+ " in the same directory as this program's executable."));
		panel.add(useTagsCheckbox);
		panel.add(defaultLogoCheckbox);

		Object[] options = { "Export", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, panel, "Export Settings", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (result == 0) {
			try {
				export(useTagsCheckbox.isSelected(), defaultLogoCheckbox.isSelected());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ActionListener that listens for double clicks on JList.
	 */
	Action displayAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JList list = (JList) e.getSource();
			if (list.getSelectedIndex() != -1) {
				Track track = trackList.get(list.getSelectedIndex());
				System.out.println(track.toString());
				JSlider db = new JSlider(JSlider.HORIZONTAL, -10, 5, track.getDecibels());
				db.setMajorTickSpacing(5);
				db.setMinorTickSpacing(1);
				db.setPaintTicks(true);
				db.setPaintLabels(true);
				db.setToolTipText(
						"Increase or decrease volume of track. Warning: Setting too loud can result in earrape.");

				JTextField trackName = new JTextField(track.getTrackName());
				trackName.setToolTipText("Name of the track that will appear in game.");
				JTextField tag = new JTextField(track.getTag());
				tag.setToolTipText("Will appear in front of the track name: [Tag] Track Name");
				JTextField durationField = new JTextField("" + track.getDuration());
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

				int result = JOptionPane.showConfirmDialog(null, panel, "Metadata Editor", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				track.setTrackName(trackName.getText());
				track.setTag(tag.getText());
				try {
					track.setDuration(Integer.parseInt(durationField.getText()));
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Invalid duration. Make sure it is an integer number (no decimals).", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				track.setDecibels(db.getValue());
				trackNames.set(list.getSelectedIndex(), track.getTrackName());
			}
		}
	};

	/**
	 * Method that removes currently selected song in the JList and updates the
	 * list's DefaultModel.
	 */
	public static void removeSong() {
		if (list.getSelectedIndex() != -1) {
			trackList.remove(list.getSelectedIndex());
			trackNames.remove(list.getSelectedIndex());
		}
	}

	/**
	 * Add a song or multiple songs to the list via a FileChooser dialog.
	 */
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

			for (int i = 0; i < files.length; i++) {
				Track newTrack = new Track(files[i].getAbsolutePath(),
						files[i].getName().substring(0, files[i].getName().length() - 4), null, "", 0, 0);

				// Get track duration
				try {
					AudioFile af = AudioFileIO.read(files[i]);
					AudioHeader ah = af.getAudioHeader();
					newTrack.setDuration(ah.getTrackLength());
				} catch (Exception ex) {
					System.out.println("There was an error reading duration data for " + newTrack.getTrackName());
				}

				trackList.add(newTrack);
				trackNames.addElement(newTrack.getTrackName());
			}
		} else {
			System.out.println("Open command cancelled by user.");
		}
	}

	/**
	 * Include a cover image in PAA format that is seen when the mod is selected in
	 * both the launcher and in game.
	 */
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
			JOptionPane.showMessageDialog(frmZeusMusicMod, "File " + file.getName()
					+ " has been added successfully. Path can be found in File>Project Settings>General.");

		} else {
			System.out.println("Open command cancelled by user.");
		}
	}

	/**
	 * Opens the project settings dialog which allows user to make changes to
	 * overall variables of the project.
	 */
	public static void showProjectSettings() {
		JTextField modName = new JTextField(projectName);
		modName.setToolTipText(
				"The name of the mod that will be visible on Steam, in the Arma 3 Launcher, and in game.");
		JTextField author = new JTextField(authorName);
		author.setToolTipText("The author of this mod, can be any arbitrary name. Visible in Arma 3 Launcher.");
		JTextField logoField = new JTextField(logoPath);
		JCheckBox defaultLogoCheckbox = new JCheckBox("Use Default Logo");
		defaultLogoCheckbox.setToolTipText(
				"If this option is selected, the standard logo will be used instead of the one provided in 'Logo Path'.");
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

		int result = JOptionPane.showConfirmDialog(null, panel, "Project Settings", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		projectName = modName.getText();
		authorName = author.getText();
		useDefaultLogo = defaultLogoCheckbox.isSelected();

	}

	/**
	 * Allows user to pack PBO file into mod folder structure after export is built
	 * using Arma 3 Tools Addon Builder.
	 * 
	 * @throws IOException
	 */
	public static void buildAddon() {
		try {
			// Get Addon PBO
			String pboPath = "";
			final JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PBO files", "pbo");
			fc.addChoosableFileFilter(filter);
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);

			int returnVal = fc.showOpenDialog(frmZeusMusicMod);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				pboPath = file.getAbsolutePath();
			} else {
				System.out.println("Open command cancelled by user.");
				return;
			}

			// Create main mod folder
			String projectNameRegex = "@" + projectName.replaceAll("\\W", "");
			File mainDir = new File(projectNameRegex);
			if (!mainDir.exists()) {
				mainDir.mkdirs();
			}

			// Copy image PAA to main folder
			File logo = (useDefaultLogo) ? getResourceAsFile("logo.paa") : new File(logoPath);
			Files.copy(logo.toPath(), (new File(mainDir.getAbsolutePath() + "\\logo.paa")).toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			// Copy default steamLogo.png to main folder
			File steamLogo = getResourceAsFile("steamLogo.png");
			Files.copy(steamLogo.toPath(), (new File(mainDir.getAbsolutePath() + "\\steamLogo.png")).toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			// Create mod.cpp
			Theme theme = new Theme();
			Chunk chunk = theme.makeChunk("config", "txt");
			chunk = theme.makeChunk("mod", "txt");

			chunk.set("modName", projectName);
			chunk.set("authorName", authorName.trim());

			String outfilePath = mainDir.getAbsolutePath() + "\\mod.cpp";
			File file = new File(outfilePath);
			FileWriter out = new FileWriter(file);

			outfilePath = mainDir.getAbsolutePath() + "\\mod.cpp";
			file = new File(outfilePath);
			out = new FileWriter(file);

			chunk.render(out);

			out.flush();
			out.close();

			// Create addons folder
			File addonDir = new File(projectNameRegex + "\\Addons");
			if (!addonDir.exists()) {
				addonDir.mkdirs();
			}

			// Add PBO to addons folder
			System.out.println(pboPath);
			File source = new File(pboPath);
			Files.copy(source.toPath(), (new File(addonDir.getAbsolutePath() + "\\MusicModPBO.pbo")).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			JOptionPane.showMessageDialog(frmZeusMusicMod, "Addon built successfully to folder: " + projectNameRegex);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frmZeusMusicMod, "There was an error building the addon.",
					"Addon Build Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Export method which combines all data collected during program use and
	 * formats the final file structure of the mod before it is given to the A3Tools
	 * Addon Builder.
	 * 
	 * @param useTags
	 * @param useDefaultLogo
	 * @throws IOException
	 */
	public static void export(boolean useTags, boolean useDefaultLogo) throws IOException {
		String projectNameRegex = projectName.replaceAll("\\W", "");

		// Create main folder
		File mainDir = new File(projectNameRegex); // Remove whitespace or tabs from project name
		if (!mainDir.exists()) {
			mainDir.mkdirs();
		}

		Theme theme = new Theme();

		// Create config.cpp
		Chunk chunk = theme.makeChunk("config", "txt");

		chunk.set("modNameNoSpaces", projectNameRegex);
		chunk.set("modName", projectName);
		chunk.set("authorName", authorName.trim()); // Removes trailing or leading whitespace too

		String outfilePath = mainDir.getAbsolutePath() + "\\config.cpp";
		File file = new File(outfilePath);
		FileWriter out = new FileWriter(file);

		chunk.render(out);

		out.flush();
		out.close();

		// Create mod.cpp
		chunk = theme.makeChunk("mod", "txt");

		chunk.set("modName", projectName);
		chunk.set("authorName", authorName.trim());

		outfilePath = mainDir.getAbsolutePath() + "\\mod.cpp";
		file = new File(outfilePath);
		out = new FileWriter(file);

		chunk.render(out);

		out.flush();
		out.close();

		// Create track folder
		File trackDir = new File(projectNameRegex + "/folderwithtracks");
		if (!trackDir.exists()) {
			trackDir.mkdirs();
		}

		// Copy all referenced tracks to destination track folder in main project folder
		for (int i = 0; i < trackList.size(); i++) {
			try {
				File source = new File(trackList.get(i).getPath());

				Files.copy(source.toPath(),
						(new File(trackDir.getAbsolutePath() + "/" + trackList.get(i).getTrackName() + ".ogg")
								.toPath()),
						StandardCopyOption.REPLACE_EXISTING); // Copy song from source to target directory. If the song
																// is already there, overwrite it.
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frmZeusMusicMod,
						"An error occurred while copying songs to target directory. Ensure songs in list still exist in original paths.",
						"Export Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}

		// Copy image PAA to main folder
		File logo = (useDefaultLogo) ? getResourceAsFile("logo.paa") : new File(logoPath);
		if (logo.getPath().equals(""))
			logo = getResourceAsFile("logo.paa");
		Files.copy(logo.toPath(), (new File(mainDir.getAbsolutePath() + "\\logo.paa")).toPath(),
				StandardCopyOption.REPLACE_EXISTING);

		// Copy default steamLogo.png to main folder
		File steamLogo = getResourceAsFile("steamLogo.png");
		Files.copy(steamLogo.toPath(), (new File(mainDir.getAbsolutePath() + "\\steamLogo.png")).toPath(),
				StandardCopyOption.REPLACE_EXISTING);

		// Generate the fileListWithMusicTracks.hpp file
		outfilePath = mainDir.getAbsolutePath() + "\\FileListWithMusicTracks.hpp";
		file = new File(outfilePath);
		out = new FileWriter(file);
		for (int i = 0; i < trackList.size(); i++) {
			String tag = (useTags) ? trackList.get(i).getTag() : ""; // If 'useTags' checkbox was ticked, use file tags.
			tag = (tag.equals("")) ? "" : "[" + tag + "] "; // If no tag was provided, don't add the square brackets.

			chunk = theme.makeChunk("FileListWithMusicTracks", "txt");
			chunk.set("trackClass", "Song" + i);
			chunk.set("trackName", tag + trackList.get(i).getTrackName());
			chunk.set("trackPath",
					projectNameRegex + "\\folderwithtracks\\" + trackList.get(i).getTrackName() + ".ogg");
			chunk.set("decibels", (trackList.get(i).getDecibels() >= 0) ? "+" + trackList.get(i).getDecibels()
					: trackList.get(i).getDecibels());
			chunk.set("duration", trackList.get(i).getDuration());

			chunk.render(out);

		}

		out.flush();
		out.close();
		JOptionPane.showMessageDialog(frmZeusMusicMod, "Export to " + projectNameRegex + " folder complete.",
				"Export Successful", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Method to retrieve resources packaged in JAR file.
	 * 
	 * @param resourcePath
	 * @return
	 */
	public static File getResourceAsFile(String resourcePath) {
		try {
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
			if (in == null) {
				return null;
			}

			File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
			tempFile.deleteOnExit();

			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				// copy stream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
			in.close();
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}