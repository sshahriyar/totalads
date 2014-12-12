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

import com.mongodb.DBObject;

/**
 * This class wraps a document object from MongoDB
 * @author Syed Shariyar Murtaza
 *
 */
class MongoDBRecord implements IDBRecord {

    private DBObject fDbObject;

    /**
     * Constructor to create a mongo document object for TotalADS
     *
     * @param dbObject
     *            An object representing the document
     */
    public MongoDBRecord(DBObject dbObject) {
        fDbObject = dbObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBRecord#get(java.lang.String)
     */
    @Override
    public Object get(String fieldName) {
        return fDbObject.get(fieldName);
    }

}
