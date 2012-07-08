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
package de.zbit.editor.control;

import java.util.List;

import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.ext.layout.BoundingBox;
import org.sbml.jsbml.ext.layout.CompartmentGlyph;
import org.sbml.jsbml.ext.layout.Dimensions;
import org.sbml.jsbml.ext.layout.GraphicalObject;
import org.sbml.jsbml.ext.layout.Layout;
import org.sbml.jsbml.ext.layout.Point;
import org.sbml.jsbml.ext.layout.ReactionGlyph;
import org.sbml.jsbml.ext.layout.SpeciesGlyph;
import org.sbml.jsbml.ext.layout.SpeciesReferenceGlyph;
import org.sbml.jsbml.ext.layout.SpeciesReferenceRole;
import org.sbml.jsbml.ext.layout.TextGlyph;

import de.zbit.editor.SBMLEditorConstants;

/**
 * @author Jakob Matthes
 * @version $Rev$
 */
public class SBMLFactory {

  /**
   * @param id
   * @param name
   * @param sboTerm
   * @param level
   * @param version
   * @return
   */
	
	//TODO Species only with Compartment
  public static Species createSpecies(String id, String name,
    int sboTerm, int level, int version) {
    Species s = new Species(id, name, level, version);
    s.setSBOTerm(sboTerm);
    return s;
  }


  
  
  /**
   * adds Species Glyph to layout defining a new Boundingbox  by x and y
   * @param layout
   * @param sGlyph
   * @param x
   * @param y
   * @param name
   * @return
   */
  public static SpeciesGlyph addSpeciesGlyphToLayout(Layout layout, SpeciesGlyph sGlyph, double x, double y, String name) {
    return addSpeciesGlyphToLayout(layout, sGlyph, x, y, SBMLEditorConstants.glyphDefaultDepth, 
      SBMLEditorConstants.glyphDefaultWidth, SBMLEditorConstants.glyphDefaultHeight, 
      SBMLEditorConstants.glyphDefaultDepth, name);
  }
  /**
   * adds Species Glyph to layout defining a new Boundingbox  by x and y
   * @param layout
   * @param sGlyph
   * @param x
   * @param y
   * @param z
   * @param width
   * @param height
   * @param depth
   * @param name
   * @return
   */
  public static SpeciesGlyph addSpeciesGlyphToLayout(Layout layout, SpeciesGlyph sGlyph, double x, double y, double z,
    double width, double height, double depth, String name) {
    sGlyph.setName(name);
    sGlyph.createBoundingBox(width, height, depth, x, y, z);
    layout.addSpeciesGlyph(sGlyph);
    return sGlyph;
  }
  
  public static TextGlyph createTextGlyph(String id, int level, int version, GraphicalObject graphicalObject, String text) {
    TextGlyph tg = new TextGlyph(id, level, version);
    tg.setGraphicalObject(graphicalObject);
    tg.setText(text);
    graphicalObject.putUserObject(SBMLEditorConstants.GRAPHOBJECT_TEXTGLYPH_KEY, tg);
    return tg;
  }
  
  public static Reaction createReaction(OpenedSBMLDocument selectedDoc, Species source, Species target, boolean reversible, int level, int version){
    String id = selectedDoc.nextGenericId(SBMLEditorConstants.genericReactionIdPrefix);
    Reaction reaction = new Reaction(id, level, version);
    SpeciesReference sourceRef = new SpeciesReference(source);
    SpeciesReference targetRef = new SpeciesReference(target);
    reaction.addReactant(sourceRef);
    reaction.addProduct(targetRef);
    reaction.setReversible(reversible);
    reaction.setSBOTerm(SBO.getStateTransition());
    return reaction;
  }
  
  public static ReactionGlyph createReactionGlyph(OpenedSBMLDocument selectedDoc, Reaction reaction, SpeciesGlyph source, SpeciesGlyph target, int level, int version) {
    String id = selectedDoc.nextGenericId(SBMLEditorConstants.genericGlyphIdPrefix);
    ReactionGlyph reactionGlyph = new ReactionGlyph(id, level, version);
    double x = source.getBoundingBox().getPosition().getX() + target.getBoundingBox().getPosition().getX() / 2d;
    double y = source.getBoundingBox().getPosition().getY() + target.getBoundingBox().getPosition().getY() / 2d;
    BoundingBox bb = new BoundingBox();
    bb.setLevel(source.getLevel());
    bb.setVersion(source.getVersion());
    bb.setDimensions(new Dimensions(10, 10, 0, source.getLevel(), source.getVersion()));
    bb.setPosition(new Point(x, y, 0, source.getLevel(), source.getVersion()));
    reactionGlyph.setBoundingBox(bb);
    
    id = selectedDoc.nextGenericId(SBMLEditorConstants.genericGlyphIdPrefix+ "Ref");
    SpeciesReferenceGlyph sourceRef = new SpeciesReferenceGlyph(id, level, version);
    sourceRef.setSpeciesGlyph(source.getId());
    sourceRef.setRole(SpeciesReferenceRole.SUBSTRATE);
    id = selectedDoc.nextGenericId(SBMLEditorConstants.genericGlyphIdPrefix+ "Ref");
    SpeciesReferenceGlyph targetRef = new SpeciesReferenceGlyph(id, level, version);
    targetRef.setSpeciesGlyph(target.getId());
    targetRef.setRole(SpeciesReferenceRole.PRODUCT);
    
    
    reactionGlyph.addSpeciesReferenceGlyph(sourceRef);
    reactionGlyph.addSpeciesReferenceGlyph(targetRef);
    reactionGlyph.setSBOTerm(SBO.getStateTransition());
    reactionGlyph.setReaction(reaction);
    
    return reactionGlyph;
  }
  
  /**
   * creates a CompartmentGlyph with BoundingBox
   * sets assoc. Compartment
   * @param id
   * @param level
   * @param version
   * @param compartment
   * @param x
   * @param y
   * @param width
   * @param height
   * @return
   */
  public static CompartmentGlyph createCompartmentGlyph(String id, int level, int version, 
    String compartment, Double x, Double y, Double width, Double height) {
    CompartmentGlyph cGlyph = new CompartmentGlyph(id, level, version);
    cGlyph.setCompartment(compartment);
    cGlyph.createBoundingBox(width, height, 0, x, y, 0);
    return cGlyph;
  }
  
  /**
   * Creates a ReactionGlyph with Boundingbox, 
   * adds SpeciesReferences, assoc. reaction and reaction type
   * TODO field ReactionGlyph.curve never used
   * @param id
   * @param level
   * @param version
   * @param listOfSpeciesReferenceGlyphs
   * @param x
   * @param y
   * @param width
   * @param height
   * @param reaction
   * @param reactionType
   * @return
   */
  public static ReactionGlyph createReactionGlyph(String id, int level, int version, 
    List<SpeciesReferenceGlyph> listOfSpeciesReferenceGlyphs, Double x, Double y, Double width, Double height,
    String reaction, int reactionType) {
    ReactionGlyph rGlyph = new ReactionGlyph(id, level, version);
    for(SpeciesReferenceGlyph glyph : listOfSpeciesReferenceGlyphs) {
      rGlyph.addSpeciesReferenceGlyph(glyph);
    }
    rGlyph.createBoundingBox(width, height, 0, x, y, 0);
    rGlyph.setReaction(reaction);
    rGlyph.setSBOTerm(reactionType);
    return rGlyph;
  }
  
  /**
   * creates a SpeciesReferenceGlyph with BoundingBox,
   * SpeciesReferenceRole, and SpeciesReference (assoc. SpeciesGlyph)
   * @param id
   * @param level
   * @param version
   * @param x
   * @param y
   * @param width
   * @param height
   * @param role
   * @param speciesReference
   * @return
   */
  public static SpeciesReferenceGlyph createSpeciesReferenceGlyph(String id, int level, int version,
    double x, double y, double width, double height, SpeciesReferenceRole role, String speciesReference) {
    SpeciesReferenceGlyph srGlyph = new SpeciesReferenceGlyph(id, level, version);
    
    srGlyph.createBoundingBox(width, height, 0, x, y, 0);
    srGlyph.setRole(role);
    srGlyph.setSpeciesReference(speciesReference);
    return srGlyph;
  }
  
  /**
   * creates a SpeciesGlyph with BoundingBox
   * sets assoc. Species
   * @param id
   * @param level
   * @param version
   * @param x
   * @param y
   * @param width
   * @param height
   * @param species
   * @return
   */
  public static SpeciesGlyph createSpeciesGlyph(String id, int level,
    int version, double x, double y, double width, double height, String species) {
    SpeciesGlyph sg = new SpeciesGlyph(id, level, version);
    sg.setSpecies(species);
    sg.createBoundingBox(width, height, 0, x, y, 0);
    return sg;
  }

  /**
   * creates a SpeciesGlyph without Boundingbox
   * sets species
   * @param id
   * @param level
   * @param version
   * @param speciesId
   * @return
   */
  public static SpeciesGlyph createSpeciesGlyph(String id,
    int level, int version, String speciesId) {
    SpeciesGlyph sg = new SpeciesGlyph(id, level, version);
    sg.setSpecies(speciesId);
    return sg;
  }
}
