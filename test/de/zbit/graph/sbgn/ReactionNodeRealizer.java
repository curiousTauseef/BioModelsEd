/*
 * $Id: ReactionNodeRealizer.java 946 2012-05-21 12:55:54Z schwarzkopf $
 * $URL: https://rarepos.cs.uni-tuebingen.de/svn-path/SysBio/trunk/src/de/zbit/graph/sbgn/ReactionNodeRealizer.java $
 * ---------------------------------------------------------------------
 * This file is part of KEGGtranslator, a program to convert KGML files
 * from the KEGG database into various other formats, e.g., SBML, GML,
 * GraphML, and many more. Please visit the project homepage at
 * <http://www.cogsys.cs.uni-tuebingen.de/software/KEGGtranslator> to
 * obtain the latest version of KEGGtranslator.
 *
 * Copyright (C) 2011 by the University of Tuebingen, Germany.
 *
 * KEGGtranslator is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation. A copy of the license
 * agreement is provided in the file named "LICENSE.txt" included with
 * this software distribution and also available online as
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 * ---------------------------------------------------------------------
 */
package de.zbit.graph.sbgn;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Set;

import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.geom.YPoint;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.NodeRealizer;
import y.view.ShapeNodeRealizer;
import de.zbit.math.MathUtils;

/**
 * Creates a small node that should be used as reaction node
 * to visualize e.g. SBML reactions.
 * @author Clemens Wrzodek
 * @version $Rev: 946 $
 */
public class ReactionNodeRealizer extends ShapeNodeRealizer {
  
    /**
     * line width of reaction node
     */
    private float lineWidth;
    
  public ReactionNodeRealizer() {
    super(ShapeNodeRealizer.RECT);
    setHeight(10);
    setWidth(getHeight()*2);
  }
  
  public ReactionNodeRealizer(NodeRealizer nr) {
    super(nr);
    // If the given node realizer is of this type, then apply copy semantics. 
    if (nr instanceof ReactionNodeRealizer) {
      ReactionNodeRealizer fnr = (ReactionNodeRealizer) nr;
      // TODO: Copy the values of custom attributes. 
    }
  }
  
  public NodeRealizer createCopy(NodeRealizer nr) {
    return new ReactionNodeRealizer(nr);
  }
  
    /**
     * sets line width of {@link ReactionNodeRealizer}. If not settet, line
     * width will be 1.
     * 
     * @param lineWidth
     */
  public void setLineWidth(float lineWidth){
      this.lineWidth = lineWidth;
  }
  
  /* (non-Javadoc)
   * @see y.view.ShapeNodeRealizer#paintShapeBorder(java.awt.Graphics2D)
   */
  @Override
  protected void paintShapeBorder(Graphics2D gfx) {
    int extendBesidesBorder=0;
    gfx.setColor(Color.BLACK);
    //line width
    gfx.setStroke(new BasicStroke(lineWidth > 0 ? lineWidth : 1));
    int x = (int) getX(); int y = (int) getY();
    double min = Math.min(getWidth(), getHeight());
    double offsetX = (getWidth()-min)/2.0;
    double offsetY = (getHeight()-min)/2.0;
    
    // Draw the reaction node rectangle
    gfx.drawRect((int)offsetX+x, (int)offsetY+y, (int)min, (int)min);
    
    if (isHorizontal()) {
      int halfHeight = (int)(getHeight()/2.0);
      
      // Draw the small reaction lines on both sides, where substrates
      // and products should dock.
      gfx.drawLine(0+x-extendBesidesBorder, halfHeight+y, (int)offsetX+x, halfHeight+y);
      gfx.drawLine((int)(offsetX+min)+x, halfHeight+y, (int)getWidth()+x+extendBesidesBorder, halfHeight+y);
      
    } else {
      // Rotate node by 90°
      int halfWidth = (int)(getWidth()/2.0);
      
      // Draw the small reaction lines on both sides, where substrates
      // and products should dock.
      gfx.drawLine(halfWidth+x, 0+y-extendBesidesBorder, halfWidth+x, (int)offsetY+y);
      gfx.drawLine(halfWidth+x, (int)(offsetY+min)+y, halfWidth+x, (int)getHeight()+y+extendBesidesBorder);
      
    }
  }
  
  /* (non-Javadoc)
   * @see y.view.ShapeNodeRealizer#paintFilledShape(java.awt.Graphics2D)
   */
  @Override
  protected void paintFilledShape(Graphics2D gfx) {
    // Do NOT fill it.
  }
  
  /**
   * True if the node is painted in a horizontal layout.
   * Use {@link #rotateNode()} to change the rotation.
   */
  public boolean isHorizontal() {
    return getWidth()>=getHeight();
  }
  
  /**
   * Rotates the node by 90°.
   */
  public void rotateNode() {
    double width = getWidth();
    double height = getHeight();
    setWidth(height);
    setHeight(width);
  }
  
  /**
   * Configures the orientation of this node and the docking point
   * for all adjacent edges to match the given parameters.
   * <p>Still, you should make sure that all reatants are on one side
   * of the node and all products on the other one!
   */
  public void fixLayout(Set<Node> reactants, Set<Node> products, Set<Node> modifier) {
    Node no = getNode();
    Graph2D graph = (Graph2D) no.getGraph();
    
    // Determine location of adjacent nodes
    Integer[] cases = new Integer[4];
    double[] meanDiff = new double[2];
    Arrays.fill(cases, 0);
    Arrays.fill(meanDiff, 0); // [0] = X, [1] = Y
//    int reactantsLeft=0;
//    int reactantsAbove=1;
//    int productsLeft=2;
//    int productsAbove=3;
    
    for (EdgeCursor ec = no.edges(); ec.ok(); ec.next()) {
      Edge v = ec.edge();
      Node other = v.opposite(no);
      NodeRealizer nr = graph.getRealizer(other);
      
      double distanceX = Math.abs(getCenterX() - nr.getCenterX());
      double distanceY = Math.abs(getCenterY() - nr.getCenterY());
      if (reactants.contains(other)) {
        meanDiff[0]+= distanceX;
        meanDiff[1]+= distanceY;
        
        // Count cases, but require at least 5 pixels difference
        if (distanceX>5 && nr.getCenterX()<getCenterX()) cases[0]++;
        if (distanceY>5 && nr.getCenterY()<getCenterY()) cases[1]++;
      } else if (products.contains(other)) {
        meanDiff[0]+= distanceX;
        meanDiff[1]+= distanceY;
        
        // Count cases, but require at least 5 pixels difference        
        if (distanceX>5 && nr.getCenterX()<getCenterX()) cases[2]++;
        if (distanceY>5 && nr.getCenterY()<getCenterY()) cases[3]++;
      }
    }
    
    // Determine orientation of this node
    double max = MathUtils.max(Arrays.asList(cases));
    boolean horizontal = isHorizontal();
    if (cases[0]==max && cases[1]==max ||
        cases[2]==max && cases[3]==max) {
      // If same number is above and left, let the distance decide the orientation.
      if (meanDiff[0]>meanDiff[1]) { // X-Distance larger
        if (!horizontal) rotateNode();
      } else { // Y-Distance larger
        if (horizontal) rotateNode();
      }
    }
    else if (cases[0]==max && !horizontal) rotateNode();
    else if (cases[1]==max && horizontal) rotateNode();
    else if (cases[2]==max && !horizontal) rotateNode();
    else if (cases[3]==max && horizontal) rotateNode();
    
    // Dock all edges to the correct side
    if (isHorizontal()) {
      if (cases[0]>cases[2]) {
        // Reactants are left of this node
        setEdgesToDockOnLeftSideOfNode(reactants);
        setEdgesToDockOnRightSideOfNode(products);
      } else {
        setEdgesToDockOnLeftSideOfNode(products);
        setEdgesToDockOnRightSideOfNode(reactants);
      }
    } else {
      if (cases[1]>cases[3]) {
        // Reactants are above this node;
        setEdgesToDockOnUpperSideOfNode(reactants);
        setEdgesToDockOnLowerSideOfNode(products);
      } else {
        setEdgesToDockOnUpperSideOfNode(products);
        setEdgesToDockOnLowerSideOfNode(reactants);
      }
    }
    // TODO: reaction modifiers dock on square
    
  }

  /**
   * @param adjacentNodes
   */
  private void setEdgesToDockOnLowerSideOfNode(Set<Node> adjacentNodes) {
    YPoint dockToPoint = new YPoint(0,getHeight()/2);
    letAllEdgesDockToThisPointOnThisNode(adjacentNodes, dockToPoint);
  }

  /**
   * @param adjacentNodes
   */
  private void setEdgesToDockOnUpperSideOfNode(Set<Node> adjacentNodes) {
    YPoint dockToPoint = new YPoint(0,getHeight()/2*-1);
    letAllEdgesDockToThisPointOnThisNode(adjacentNodes, dockToPoint);
  }

  /**
   * @param adjacentNodes
   */
  private void setEdgesToDockOnRightSideOfNode(Set<Node> adjacentNodes) {
    YPoint dockToPoint = new YPoint(getWidth()/2,0);
    letAllEdgesDockToThisPointOnThisNode(adjacentNodes, dockToPoint);
  }

  /**
   * @param adjacentNodes
   */
  private void setEdgesToDockOnLeftSideOfNode(Set<Node> adjacentNodes) {
    YPoint dockToPoint = new YPoint(getWidth()/2*-1,0);
    letAllEdgesDockToThisPointOnThisNode(adjacentNodes, dockToPoint);
  }

  /**
   * @param adjacentNodes
   * @param dockToPoint
   */
  protected void letAllEdgesDockToThisPointOnThisNode(Set<Node> adjacentNodes,
    YPoint dockToPoint) {
    Node no = getNode();
    Graph2D graph = (Graph2D) no.getGraph();
    
    for (EdgeCursor ec = no.edges(); ec.ok(); ec.next()) {
      Edge v = ec.edge();
      Node other = v.opposite(no);
      EdgeRealizer er = graph.getRealizer(v);
      boolean isSource = v.source().equals(no);
      
      if (adjacentNodes.contains(other)) {
        if (isSource) {
          er.setSourcePoint(dockToPoint);
        } else {
          er.setTargetPoint(dockToPoint);
        }
      }
    }
  }
  
}