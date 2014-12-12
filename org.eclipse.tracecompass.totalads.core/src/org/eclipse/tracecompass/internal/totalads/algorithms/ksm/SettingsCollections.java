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
 * This class represents the fields of a settings collection in the MongoDB.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
enum SettingsCollections {

    /**
     * The name of collection itself
     */
    COLLECTION_NAME("settings"), //$NON-NLS-1$
    /**
     * The Key name in the collection
     */
    KEY("_id"), //$NON-NLS-1$
    /**
     * Alpha field in the collection
     */
    ALPHA("alpha"), //$NON-NLS-1$
    /**
     * Versions of the kernels
     */
    KernelVersions("kernel_versions"), //$NON-NLS-1$
    /**
     * Update_time field in the collection
     */
    UPDATE_TIME("update_time"); //$NON-NLS-1$

    private String fieldName;

    /**
     * Constructor
     *
     * @param fieldName
     */
    private SettingsCollections(String fieldName) {
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