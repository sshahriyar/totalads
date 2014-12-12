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
 * This class represents the fields of the name to id collection in a NoSQL database. It
 * stores the name of the event with its corresponding integer numbers.
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
     * Mapper name, this field would contain a map of name to ids.
     */
    MAP("map"); //$NON-NLS-1$

    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName field's name
     */
    private NameToIDCollection(String fieldName) {
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