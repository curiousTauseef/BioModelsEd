/*
 * $Id:  OpenedDocument.java 19:34:25 jakob $
 * $URL: OpenedDocument.java $
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

import org.sbml.jsbml.SBMLDocument;

/**
 * @author Jakob Matthes
 * @version $Rev$
 */
public class OpenedDocument {
  /**
   * SBML Document of the openedDocument
   */
  private SBMLDocument sbmlDocument;
  /**
   * associated filepath of the openedDocument, can be unset
   */
  private String associatedFilepath;
  
  public OpenedDocument(SBMLDocument sbmlDocument) {
    this.sbmlDocument = sbmlDocument;
  }
  
  public OpenedDocument(SBMLDocument sbmlDocument, String associatedFilepath) {
    this.sbmlDocument = sbmlDocument;
    this.associatedFilepath = associatedFilepath;
  }

  /**
   * @return the sbmlDocument
   */
  public SBMLDocument getSbmlDocument() {
    return sbmlDocument;
  }
  
  /**
   * @return the associatedFilepath
   */
  public String getAssociatedFilepath() {
    return associatedFilepath;
  }
  
  /**
   * @param associatedFilepath the associatedFilepath to set
   */
  public void setAssociatedFilepath(String associatedFilepath) {
    this.associatedFilepath = associatedFilepath;
  }
  
  /**
   * Check if filepath is set
   * @return
   */
  public boolean hasAssociatedFilepath() {
    return (getAssociatedFilepath() != null);
  }
  
}