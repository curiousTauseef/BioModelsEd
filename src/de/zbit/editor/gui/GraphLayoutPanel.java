/*
 * $Id$
 * $URL$
 * ---------------------------------------------------------------------
 * This file is part of SBML Editor.
 *
 * Copyright (C) 2012 by the University of Tuebingen, Germany.
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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.layout.Layout;

import y.view.Graph2D;
import de.zbit.graph.gui.TranslatorGraphLayerPanel;
import de.zbit.graph.io.Layout2GraphML;
import de.zbit.gui.GUITools;

/**
 * @author Andreas Dr&aum;ger
 * @since 1.0
 * @version $Rev$
 */
public class GraphLayoutPanel extends TranslatorGraphLayerPanel<Layout> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1321167449409691414L;

	/**
	 * The converter we used to generate the GraphML document
	 * from our {@link SBMLDocument}.
	 */
	private Layout2GraphML converter = null;
	private SBMLEditMode editMode;
	
	/**
	 * Maps a element id to a list of graphical objects associated
	 * with that element.
	 */
	//private Map<String, List<String>> idMap;
	
	/**
	 * Constructor.
	 * @param layout
	 */
	public GraphLayoutPanel(Layout layout, SBMLEditMode editMode) {
	  super(null, null, null, layout, false);
	  this.editMode = editMode;
		if (layout == null)
      System.out.print("Layout = null");
		
	  //this.idMap = new HashMap<String, List<String>>();
		try {
			createTabContent();
		} catch (Throwable e) {
			GUITools.showErrorMessage(null, e);
			fireActionEvent(new ActionEvent(this, JOptionPane.ERROR, COMMAND_TRANSLATION_DONE));
			return;
		}
	}
	
	/**
	 * Creates a graph from the given layout.
	 * @param layout
	 * @return the created graph
	 */
	@Override
	protected Graph2D createGraphFromDocument(Layout layout) {
		//TODO plug in layout director
		
		converter = new Layout2GraphML(editMode);
		return converter.createGraph(layout);
	}

	/**
	 * @return the converter
	 */
  public Layout2GraphML getConverter() {
    return this.converter;
  }

	
	//======================================================================================
	
	/* (non-Javadoc)
	 * @see de.zbit.graph.gui.TranslatorGraphLayerPanel#getOutputFileFilterForRealDocument()
	 */
	@Override
	protected List<FileFilter> getOutputFileFilterForRealDocument() {
		// TODO Auto-generated method stub
		return null;
	}
	

	/* (non-Javadoc)
	 * @see de.zbit.graph.gui.TranslatorGraphLayerPanel#writeRealDocumentToFileUnchecked(java.io.File, java.lang.String)
	 */
	@Override
	protected boolean writeRealDocumentToFileUnchecked(File file, String string)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
