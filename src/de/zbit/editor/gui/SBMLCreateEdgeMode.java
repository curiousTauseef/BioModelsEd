/*
 * $Id:  SBMLCreateEdgeMode.java 21:02:39 Eugen Netz$
 * $URL: SBMLCreateEdgeMode.java$
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

import de.zbit.graph.sbgn.ReactionNodeRealizer;
import y.base.Edge;
import y.base.Node;
import y.view.Arrow;
import y.view.CreateEdgeMode;
import y.view.EdgeRealizer;
import y.view.Graph2D;


/**
 * @author Eugen Netz
 * @since 1.0
 * @version $Rev$
 */
public class SBMLCreateEdgeMode extends CreateEdgeMode {
  
  public void test(){
   
  }
  
  @Override
  public Edge createEdge(Graph2D graph, Node start, Node target, EdgeRealizer realizer) {
    if (graph.getRealizer(target) instanceof ReactionNodeRealizer) {
      Edge e = super.createEdge(graph, start, target, realizer);
      return e;
    }
    
    ReactionNodeRealizer nre = new ReactionNodeRealizer();
    Node reactionNode = graph.createNode(nre);

    Edge e1 = graph.createEdge(start, reactionNode);
    Edge e2 = graph.createEdge(reactionNode, target);;
    
    nre.setCenter((graph.getRealizer(start).getCenterX() + graph.getRealizer(target).getCenterX())/2d, (graph.getRealizer(start).getCenterY() + graph.getRealizer(target).getCenterY())/2d);
    ((EdgeRealizer)graph.getRealizer(e2)).setArrow(Arrow.DELTA);

    return e2;
  }
}
