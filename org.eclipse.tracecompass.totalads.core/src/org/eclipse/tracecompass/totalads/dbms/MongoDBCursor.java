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

import org.eclipse.tracecompass.totalads.dbms.IDBCursor;
import org.eclipse.tracecompass.totalads.dbms.IDBRecord;
import org.eclipse.tracecompass.totalads.dbms.MongoDBRecord;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * This class creates an iterator over the collection of documents of a MongoDB
 * by wrapping the MongoDB {@link DBCursor} class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class MongoDBCursor implements IDBCursor {
    private DBCursor fDbCursor;

    /**
     * Constructor to create an iterator over the documents' collection for a
     * query
     *
     * @param dbCursor
     *            Cursor on a collection
     */
    public MongoDBCursor(DBCursor dbCursor) {
        fDbCursor = dbCursor;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDBCursor#hasNext()
     */
    @Override
    public boolean hasNext() {

        return fDbCursor.hasNext();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDBCursor#next()
     */
    @Override
    public IDBRecord next() {
        DBObject document = fDbCursor.next();
        MongoDBRecord record = new MongoDBRecord(document);
        return record;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws TotalADSDBMSException {
        try {
            fDbCursor.close();
        } catch (Exception ex) {
            throw new TotalADSDBMSException(ex);
        }
    }

}
