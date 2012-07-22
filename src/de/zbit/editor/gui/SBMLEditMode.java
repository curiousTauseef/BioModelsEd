/*
 * $Id: SBMLEditMode.java 137 2012-07-14 17:43:39Z se-ss-12.netz$
 * $URL: https://cis.informatik.uni-tuebingen.de/svn/R4f8845abdec88/trunk/src/de/zbit/editor/gui/SBMLEditMode.java$
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
import java.util.List;

import org.sbml.jsbml.util.ValuePair;

import y.base.Node;
import y.base.NodeCursor;
import y.view.EditMode;
import y.view.HitInfo;
import de.zbit.editor.SBMLEditorConstants;
import de.zbit.editor.control.CommandController;

/**
 * This class accepts user input on the yFiles interface panel and decides the resulting actions.
 * 
 * @author Alexander Diamantikos
 * @author Jakob Matthes
 * @author Eugen Netz
 * @author Jan Rudolph
 * @version $Rev$
 */
public class SBMLEditMode extends EditMode  {

  //private ValuePair<Double, Double> lastPositionMouseClicked;
  //private ValuePair<Double, Double> oldNodePosition;
  //private Node node;
 
  /**
   * Constructor.
   * @param controller
   */
  public SBMLEditMode(CommandController controller) {
    super();
    this.allowBendCreation(false);
    this.allowNodeCreation(true);
    this.setCreateEdgeMode(new SBMLCreateEdgeMode());
    this.allowEdgeCreation(true);
    this.addPropertyChangeListener(controller);
  }
  
  /**
   * Fires a property change with the current mouse position, when the left mouse is pressed.
   */
  @Override
  public void mousePressedLeft(double x, double y) {
    
    HitInfo info = this.getGraph2D().getHitInfo(x, y);
    Object hit = info.getFirstHit();
    if (hit instanceof Node) {
      Node node = (Node) hit;
      
      firePropertyChange(SBMLEditorConstants.EditModeNodePressedLeft, null, node);      
      //oldNodePosition = new ValuePair<Double, Double>(nodeX, nodeY);      
    }
    else {
      ValuePair<Double, Double> newPositionMouseClicked = new ValuePair<Double, Double>(x, y);
      firePropertyChange(SBMLEditorConstants.EditModeMousePressedLeft, null, newPositionMouseClicked);
      //lastPositionMouseClicked = newPositionMouseClicked;
            
    }  
    
  }
  
  /**
   * Fires a property change with the current mouse position, when the pressed left mouse is released.
   */
  @Override
  public void mouseReleasedLeft(double x, double y) {
   
    List<Node> list = getSelectedNodes();      
    
    // Set list of nodes in CommandController
    firePropertyChange(SBMLEditorConstants.EditModeSelectionChanged, null, list);
    // Initiate updating of glyphs
    if (!list.isEmpty()) {
      firePropertyChange(SBMLEditorConstants.EditModeUpdateNodes, null, this.getGraph2D());
    }
    // Send mouse position
    ValuePair<Double, Double> positionMouseReleased = new ValuePair<Double, Double>(x, y);
    firePropertyChange(SBMLEditorConstants.EditModeMouseReleasedLeft, null, positionMouseReleased);  
  } 
  
  /**
   * Gets all nodes inside a multiple selection.
   * @return a list of the selected nodes
   */
  private List<Node> getSelectedNodes() {
    NodeCursor cursor = this.getGraph2D().selectedNodes();

    List<Node> list = new ArrayList<Node>();

    while(cursor.ok()) {
      Node current = cursor.node();
      list.add(current);
      cursor.next();
    }
    return list;
  }

  /**
   * Fires a property change with the current mouse position and the targeted node selection, when the right mouse is pressed.
   */
  @Override
  public void mousePressedRight(double x, double y) {
    HitInfo info = this.getGraph2D().getHitInfo(x, y);
    Object hit = info.getFirstHit();
    if (hit instanceof Node) {
      
      Node node = (Node) hit;
      
      unselectAll(this.getGraph2D());
      this.getGraph2D().setSelected(node, true);
      this.getGraph2D().updateViews();
      
      List<Node> list = this.getSelectedNodes();
      
      // Set list of nodes in CommandController
      firePropertyChange(SBMLEditorConstants.EditModeSelectionChanged, null, list);
      
      firePropertyChange(SBMLEditorConstants.EditModeNodePressedRight, null, node);      
      //oldNodePosition = new ValuePair<Double, Double>(nodeX, nodeY);      
    }
    else {

      
      ValuePair<Double, Double> newPositionMouseClicked = new ValuePair<Double, Double>(x, y);
      firePropertyChange(SBMLEditorConstants.EditModeMousePressedRight, null, newPositionMouseClicked);
      //lastPositionMouseClicked = newPositionMouseClicked;
    }
  }
  
  /**
   * Removes the node from the graph.
   * @param node
   */
  public void nodeDelete(Node node) {
    this.getGraph2D().removeNode(node);
  }
}
