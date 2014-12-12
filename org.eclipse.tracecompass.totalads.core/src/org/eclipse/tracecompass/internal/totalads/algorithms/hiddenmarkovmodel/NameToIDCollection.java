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
 * This class represents the fields of the name to id collection for an HMM in a
 * NoSQL database. It stores the name of an event with its corresponding integer
 * number
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
enum NameToIDCollection {

    /**
     * The name of collection itself
     */
    COLLECTION_NAME("name_to_id"), //$NON-NLS-1$
    /**
     * Key field name
     */
    KEY("_id"), //$NON-NLS-1$
    /**
     * Mapper name, this field will contain the map of name to ids. It can be
     * converted to a hash map directly.
     */
    MAP("map"); //$NON-NLS-1$

    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName
     */
    private NameToIDCollection(String fieldName) {
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