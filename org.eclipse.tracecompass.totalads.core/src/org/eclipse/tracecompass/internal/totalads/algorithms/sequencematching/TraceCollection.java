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
 * This class represents the fields of a trace collection in a NoSQL database.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
enum TraceCollection {

    /**
     * The name of collection itself
     */
    COLLECTION_NAME("trace_data"), //$NON-NLS-1$
    /**
     * name
     */
    KEY("_id"), //$NON-NLS-1$
    /**
     * Tree field
     */
    TREE("tree"); //$NON-NLS-1$

    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName
     */
    private TraceCollection(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the string value of the field's name
     */
    @Override
    public String toString() {
        return fieldName;
    }

}