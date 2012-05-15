/*
 * $Id:  TabManager.java 14:14:01 jakob $
 * $URL: TabManager.java $
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

import java.util.ArrayList;

import javax.swing.JTabbedPane;

import de.zbit.graph.gui.TranslatorSBMLgraphPanel;

/**
 * @author Jakob Matthes
 * @version $Rev$
 */
public class TabManager extends JTabbedPane {

  private static final long serialVersionUID = -905908829761611472L;
  private SBMLEditor editorInstance;
  private ArrayList<OpenedDocument> tabMap = new ArrayList<OpenedDocument>();
  

  /**
   * @param editorInstance
   */
  public TabManager(SBMLEditor editorInstance) {
    this.editorInstance = editorInstance;
  }
  
  /**
   * @return the editorInstance
   */
  public SBMLEditor getEditorInstance() {
    return editorInstance;
  }

  /**
   * @param doc
   */
  public void closeTab(OpenedDocument doc) {
    removeTabAt(tabMap.indexOf(doc));
  }

  /**
   * @param doc
   */
  public void addTab(OpenedDocument doc) {
    tabMap.add(doc);
    String title;
    if (doc.hasAssociatedFilepath()) {
      title = doc.getAssociatedFilepath();      
    }
    else {
      title = Resources.getString("UNSAVED_FILE");
    }
    TranslatorSBMLgraphPanel panel = new TranslatorSBMLgraphPanel(doc.getSbmlDocument(), false);
    addTab(title, panel);
    setSelectedComponent(panel);
  }

  /**
   * Return the currently opened document.
   * @return
   */
  public OpenedDocument getCurrentDocument() {
    TranslatorSBMLgraphPanel current = (TranslatorSBMLgraphPanel) getSelectedComponent();
    
    for (OpenedDocument opened : tabMap) {
      if (opened.getSbmlDocument() == current.getDocument()) {
        return opened;
      }
    }
    
    return null;
  }

  /**
   * Close the currently visible tab.
   */
  public void closeCurrentTab() {
    closeTab(getCurrentDocument());
  }
  
}
