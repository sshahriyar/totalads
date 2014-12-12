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
 * This class represents the fields of the collection called hmm_model in a
 * NoSQL database.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
enum HmmModelCollection {

    /**
     * The name of collection itself
     */
    COLLECTION_NAME("hmm_model"), //$NON-NLS-1$
    /**
     * Key field name
     */
    KEY("_id"), //$NON-NLS-1$
    /**
     * DataModel
     */
    MODEL("model"), //$NON-NLS-1$
    /**
     * Initial state probabilities, found within the state document
     */
    INTITIALPROB("Pi"), //$NON-NLS-1$
    /**
     * Transition probabilities, found within the state document
     */
    TRANSITIONPROB("Aij"), //$NON-NLS-1$
    /**
     * Emission probabilities, found within the state document
     */
    EMISSIONPROB("Opdf"); //$NON-NLS-1$
    /**
     * Private field to hold the name
     */
    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName
     */
    private HmmModelCollection(String fieldName) {
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