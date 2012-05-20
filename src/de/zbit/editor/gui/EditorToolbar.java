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

import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.JToolBar;


/**
 * @author Jakob Matthes
 * @version $Rev$
 */
public class EditorToolbar extends JToolBar {

  private static final long serialVersionUID = 4238837776010510727L;

  /**
   * 
   * @param commandController
   */
  public EditorToolbar(SBMLEditor parent) {
	  // TODO: Create a very simple icon for each button, use Tooltips, remove the String Label.
    GUIFactory.addButton(this, "Unspecified",EventHandler.create(ActionListener.class, parent, "fileNew"));
    GUIFactory.addButton(this, "Simple Chemical");
    GUIFactory.addButton(this, "Macromolecule");
    GUIFactory.addButton(this, "Sink");
    GUIFactory.addButton(this, "Reaction");
    GUIFactory.addButton(this, "Catalysis");
    GUIFactory.addButton(this, "Inhibition");

//    String[] layoutArray = { "A", "B", "C" };
//    JComboBox layoutComboBox = new JComboBox(layoutArray);
//    layoutComboBox.setSelectedIndex(0);
//    layoutComboBox.addActionListener(toolbarListener);
//    add(layoutComboBox);
    
    GUIFactory.addButton(this, "open");
    GUIFactory.addButton(this, "open in new tab");
  }


}
