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
package org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel;

/**
 * This class represents the fields of the settings collection for an Hmm in the
 * database.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
enum SettingsCollection {

    /**
     * The name of collection itself
     */
    COLLECTION_NAME("settings"), //$NON-NLS-1$
    /**
     * Key field name
     */
    KEY("_id"), //$NON-NLS-1$
    /**
     * Number of states
     */
    NUM_STATES("Number of States"), //$NON-NLS-1$
    /**
     * Number of symbols
     */
    NUM_SYMBOLS("Number of Unique Events"), //$NON-NLS-1$
    /**
     * Sequence length
     */
    NUMBER_OF_ITERATIONS("Number of Iterations"), //$NON-NLS-1$
    /**
     * Probability threshold
     */
    SEQ_LENGTH("Sequence Length"), //$NON-NLS-1$
    /**
     * Probability threshold
     */
    LOG_LIKELIHOOD("Log Likelihood"); //$NON-NLS-1$

    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName
     */
    private SettingsCollection(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the String Value of the FieldName
     */
    @Override
    public String toString() {
        return fieldName;
    }

}