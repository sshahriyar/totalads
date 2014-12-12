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

import org.eclipse.tracecompass.totalads.dbms.IDBRecord;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;

/**
 * An interface to iterator over the collection of records (or documents)
 *
 * @author Syed Shariyar Mutaza
 *
 */
public interface IDBCursor extends AutoCloseable {
    /**
     * Determines whether the next record (or document) exists or not
     *
     * @return True if there is a next record, else it is false
     */
    public boolean hasNext();

    /**
     * Returns the next record (or document)
     *
     * @return An object of type {@link IDBRecord}
     */
    public IDBRecord next();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws TotalADSDBMSException;
}
