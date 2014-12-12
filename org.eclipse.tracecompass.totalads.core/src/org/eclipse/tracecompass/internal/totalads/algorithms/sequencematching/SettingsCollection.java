/*********************************************************************************************
 * Copyright (c) 2014-2015  Software Behaviour Analysis Lab, Concordia University, Montreal, Canada
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of Eclipse Public License v1.0 License which
 * accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Syed Shariyar Murtaza -- Initial design and implementation
 **********************************************************************************************/
package org.eclipse.tracecompass.internal.totalads.algorithms.sequencematching;
/**
 * This class represents the fields of the settings collection in a NoSQL database.
 *
 *  @author <p>Syed Shariyar Murtaza justsshary@hotmail.com</p>
 *
 */
enum SettingsCollection{
	/**
	 * The Key name in the collection
	 */
	KEY("_id"), //$NON-NLS-1$
	/**
	 * Max win field in the collection
	 */
	MAX_WIN("maxWIN"), //$NON-NLS-1$
	/**
	 * Max hamming distance field in the collection
	 */
	MAX_HAM_DIS("maxHamDis"), //$NON-NLS-1$
	/**
	 * Detailed Analysis field in the collection
	 */
	DETAILED_ANALYSIS("detailedAnalysis"), //$NON-NLS-1$
	/**
	 * The name of collection itself
	 */
	COLLECTION_NAME("settings"); //$NON-NLS-1$

	private String fieldName;
	/**
	 * Constructor
	 * @param fieldName
	 */
	private SettingsCollection(String fieldName){
		this.fieldName=fieldName;
	}
	/**
	 * Returns the String Value of the FieldName
	 */
	 @Override
	 public String toString() {
	      return fieldName;
	 }




}