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
package org.eclipse.tracecompass.internal.totalads.algorithms.ksm;

/**
 * This class represents the fields of a trace collection in the SB In the case
 * of trace collection the fields are the system call tree generated from the
 * trace
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
     * FS field
     */
    FS("FS"), //$NON-NLS-1$
    /**
     * KL field
     */
    KL("KL"), //$NON-NLS-1$

    /**
     * MM field
     */
    MM("MM"), //$NON-NLS-1$

    /**
     * AC field
     */
    AC("AC"), //$NON-NLS-1$

    /**
     * IPC field
     */
    IPC("IPC"), //$NON-NLS-1$
    /**
     * NT field
     */
    NT("NT"), //$NON-NLS-1$
    /**
     * SC field
     */
    SC("SC"), //$NON-NLS-1$
    /**
     * UN
     */
    UN("UN"); //$NON-NLS-1$

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
     * Returns the String Value of the FieldName
     */
    @Override
    public String toString() {
        return fieldName;
    }

}