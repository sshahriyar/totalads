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
package org.eclipse.tracecompass.totalads.dbms;

/**
 *
 * Interface to access a record (or a document) in a database
 *
 * @author Syed Shariyar Murtaza
 *
 */
public interface IDBRecord {
    /**
     * Returns an object corresponding to a field
     *
     * @param fieldName
     *            The name of the field in a record (document)
     *
     * @return An object corresponding to the field
     */
    public Object get(String fieldName);
}
