/*
 * $Id$
 * $URL$
 * ---------------------------------------------------------------------
 * This file is part of BioModelsEd.
 *
 * Copyright (C) 20012-2013 by the University of Tuebingen, Germany.
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation. A copy of the license
 * agreement is provided in the file named "LICENSE.txt" included with
 * this software distribution and also available online as
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 * ---------------------------------------------------------------------
 */
package de.zbit.editor.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.sbml.jsbml.SBMLDocument;

import de.zbit.AppConf;
import de.zbit.editor.control.CommandController;
import de.zbit.editor.control.SBMLView;
import de.zbit.gui.BaseFrame;
import de.zbit.gui.GUIOptions;
import de.zbit.gui.GUITools;
import de.zbit.io.OpenedFile;
import de.zbit.io.filefilter.SBFileFilter;
import de.zbit.util.ResourceManager;
import de.zbit.util.prefs.SBPreferences;

/**
 * @author Jan Rudoplph
 * @version $Rev$
 */
public class BioModelsEdGUI extends BaseFrame implements SBMLView, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8556637237458666164L;
	private TabManager tabManager;
	private CommandController controller;
	private final static Logger logger = Logger.getLogger(BioModelsEdGUI.class.getName());
	//TODO rename package
	private final static ResourceBundle MESSAGES = ResourceManager.getBundle("de.zbit.editor.locales.Messages");
	
	/**
	 * Constructor
	 * @param appConf 
	 */
	public BioModelsEdGUI(AppConf appConf) {
		super(appConf);
	}

	@Override
	public boolean closeFile() {
		OpenedFile<SBMLDocument> file = tabManager.getCurrentFile();
		if (file.isChanged()) {
			switch (JOptionPane.showConfirmDialog(this, MESSAGES.getString("CONFIRM_SAVING"))) {
				case 0 : return file.isSetFile() ? controller.saveFile(file) : (saveFileAs() != null);
				case 2 : return false;
			}
		}
		tabManager.closeFile(file);
		return controller.closeFile(file);
	}

	@Override
	protected JToolBar createJToolBar() {
		tabManager = new TabManager(this);
		EditorToolBar toolBar = new EditorToolBar(this, tabManager);
		return toolBar;
	}

	@Override
	protected Component createMainComponent() {
		tabManager.setToolBar(this.getJToolBar());
		tabManager.setMenuBar(this.getJMenuBar());
		return tabManager;
	}

	@Override
	public URL getURLAboutMessage() {
		return getClass().getResource(MESSAGES.getString("ABOUT_FILE"));
	}

	@Override
	public URL getURLLicense() {
		return getClass().getResource(MESSAGES.getString("LICENSE_FILE"));
	}

	@Override
	public URL getURLOnlineHelp() {
		return getClass().getResource(MESSAGES.getString("ONLINE_HELP_FILE"));
	}

	@Override
	protected File[] openFile(File... files) {
		SBPreferences prefs = SBPreferences.getPreferencesFor(GUIOptions.class);
		if ((files == null) || (files.length == 0)) {
			files = GUITools.openFileDialog(
				this,
				prefs.get(GUIOptions.OPEN_DIR),
				false,
				true,
				JFileChooser.FILES_ONLY, 
				SBFileFilter.createSBMLFileFilter()
			);
		}
		return controller.openFile(files);
	}

	@Override
	public CommandController getController() {
		return this.controller;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.info("Property changed: " + evt.getPropertyName());
	} 

	@Override
	public File saveFileAs() {
		File file = GUITools.saveFileDialog(this);
		if (file != null) {
			if (controller.saveFileAs(file, tabManager.getCurrentFile())) {
				logger.info(MESSAGES.getString("SAVING_DONE_INFO"));
			}
		}
		return file;
	}

	/* (non-Javadoc)
	 * @see de.zbit.gui.BaseFrame#saveFile()
	 */
	@Override
	public File saveFile() {
		OpenedFile<SBMLDocument> currentFile = tabManager.getCurrentFile();
		controller.saveFile(currentFile);
		return currentFile.getFile();
	}

	/**
	 * @param controller
	 */
	public void setController(CommandController controller) {
		this.controller = controller;
	}

	/* (non-Javadoc)
	 * @see de.zbit.editor.control.SBMLView#addTab(de.zbit.io.OpenedFile, java.lang.String, boolean)
	 */
	@Override
	public boolean show(OpenedFile<SBMLDocument> file) {
		return tabManager.addTab(file, null);
	}

	/* (non-Javadoc)
	 * @see de.zbit.gui.BaseFrame#additionalEditMenuItems()
	 */
	@Override
	protected JMenuItem[] additionalEditMenuItems() {
		return new JMenuItem[] {
				GUITools.createJMenuItem(this, Command.UNDO),
				GUITools.createJMenuItem(this, Command.REDO),
				GUITools.createJMenuItem(this, Command.COPY),
				GUITools.createJMenuItem(this, Command.CUT),
				GUITools.createJMenuItem(this, Command.PASTE),
				GUITools.createJMenuItem(this, Command.DELETE),
				GUITools.createJMenuItem(this, Command.SELECT_ALL),
		};
	}

	/* (non-Javadoc)
	 * @see de.zbit.gui.BaseFrame#additionalFileMenuItems()
	 */
	@Override
	protected JMenuItem[] additionalFileMenuItems() {
		return new JMenuItem[] {
				GUITools.createJMenuItem(this, Command.NEW),
				//GUITools.createJMenuItem(this, Command.CLOSE),
		};
	}

	/* (non-Javadoc)
	 * @see de.zbit.gui.BaseFrame#showsSaveMenuEntry()
	 */
	@Override
	protected boolean showsSaveMenuEntry() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.zbit.gui.BaseFrame#additionalMenus()
	 */
	@Override
	protected JMenu[] additionalMenus() {
		return new JMenu[] {
			GUITools.createJMenu(
				MESSAGES.getString("LAYOUT_MENU"), 
				MESSAGES.getString("LAYOUT_MENU_TOOLTIP"), 
				GUITools.createJMenuItem(this, Command.NEW_LAYOUT),
				GUITools.createJMenuItem(this, Command.RENAME_LAYOUT),
				GUITools.createJMenuItem(this, Command.DELETE_LAYOUT),
				GUITools.createJMenuItem(this, Command.CLONE_LAYOUT),
				GUITools.createJMenuItem(this, Command.AUTOMATIC_LAYOUT))
		};
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		logger.info("action performed: " + evt.getActionCommand());
		switch (Command.valueOf(evt.getActionCommand())) {
			case NEW : controller.fileNew(); break;
			//case CLOSE : controller.fileClose(); break;
		}
	}

	/* (non-Javadoc)
	 * @see de.zbit.editor.control.SBMLView#fileSaved(de.zbit.io.OpenedFile)
	 */
	@Override
	public void fileSaved(OpenedFile<SBMLDocument> file) {
		this.tabManager.updateTitle(file);
	}
}
