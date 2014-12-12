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
import org.eclipse.tracecompass.totalads.dbms.IDBMSSubject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;

import java.util.List;

import com.google.gson.JsonObject;

/**
 * Database Management System (IDataAccessObject) interface. This interface
 * performs the required manipulation required by clients (e.g., algorithms).
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public interface IDataAccessObject extends IDBMSSubject {

    /**
     * Determines the connected state
     *
     * @return true or false
     */
    public Boolean isConnected();

    /**
     * Get database list
     *
     * @return The list of database
     */
    public List<String> getDatabaseList();

    /**
     * Checks the existence of the database
     *
     * @param database
     *            Database name
     * @return True if database exist, else false
     */
    public boolean datbaseExists(String database);

    /**
     * Creates a database and all collections in it
     *
     * @param dataBase
     *            Database name
     * @param collectionNames
     *            Array of collection names
     * @throws TotalADSDBMSException
     *             DBMS related exception
     */
    public void createDatabase(String dataBase, String[] collectionNames) throws TotalADSDBMSException;

    /**
     * Creates a unique index on a collection (table) in ascending order
     *
     * @param dataBase
     *            Database name
     * @param collection
     *            Collection name
     * @param fields
     *            An array of field names on which to create unique indexes.
     */
    public void createAscendingUniquesIndexes(String dataBase, String collection, String[] fields);

    /**
     * Creates a unique index on a collection (table) in descending order
     *
     * @param dataBase
     *            Database name
     * @param collection
     *            Collection name
     * @param fields
     *            An array of field names on which to create unique indexes.
     */
    public void createDescendingUniquesIndexes(String dataBase, String collection, String[] fields);

    /**
     * Inserts an object in the form of JSON representation into the database.
     * Any kind of complex data structure can be converted to JSON using Gson
     * library and passed to this function
     *
     * @param database
     *            Database name
     * @param jsonObject
     *            JSON Object
     * @param collection
     *            Collection name in which to insert
     * @throws TotalADSDBMSException
     *             DBMS related exception
     */
    public void insertUsingJSON(String database, JsonObject jsonObject, String collection) throws TotalADSDBMSException;

    /**
     * Inserts or updates (if already exists) an object in the form of JSON
     * representation into the database. Any kind of complex data structure can
     * be converted to JSON using gson library and passed to this function. This
     * function replaces the entire document with a new document of a matching
     * name. E.g., If a document {"_id": 1, a:4, b:6} is updated with {b:8} then
     * the new document would be {"_id":1, b:8}
     *
     * @param database
     *            Database name
     * @param keytoSearch
     *            The indexed field and its value as a JSON object which is to
     *            be searched
     * @param jsonObjectToUpdate
     *            The data structure as a JSON object which is to be updated
     * @param collection
     *            Name of the collection (table)
     * @throws TotalADSDBMSException
     *             DBMS related exception
     */
    public void insertOrUpdateUsingJSON(String database, JsonObject keytoSearch,
            JsonObject jsonObjectToUpdate, String collection) throws TotalADSDBMSException;

    /**
     * Updates fields in an existing document.
     *
     * @param database
     *            Database name
     * @param keytoSearch
     *            Key to search
     * @param jsonObjectToUpdate
     *            The JSON object to update
     * @param collection
     *            The name of collection
     * @throws TotalADSDBMSException
     *             DBMS exception
     */
    public void updateFieldsInExistingDocUsingJSON(String database, JsonObject keytoSearch, JsonObject jsonObjectToUpdate, String collection)
            throws TotalADSDBMSException;

    /**
     * Selects a max value from a collection (table)
     *
     * @param key
     *            Field name whose maximum should be return. Use an index name
     *            otherwise the results will be slow
     * @param database
     *            Database name
     * @param collection
     *            Collection name
     * @return Maximum value as a string
     */

    public String selectMax(String key, String database, String collection);

    /**
     * Returns a set of documents based on a name search
     *
     * @param key
     *            Field name in the document of a collection. Should be an
     *            indexed name for faster processing
     * @param operator
     *            Comparison operators if any. Leave it empty if exact match is
     *            needed
     * @param value
     *            Double value of the field
     * @param database
     *            Database name
     * @param collection
     *            Collection name in the database
     * @return A DBCursor object which you can iterate through
     */
    public IDBCursor select(String key, String operator, Double value, String database, String collection);

    /**
     * Returns a set of documents based on a name search
     *
     * @param key
     *            Field name in the document of a collection. Should be an
     *            indexed name for faster processing.
     * @param operator
     *            Comparison operators if any. Leave it empty if exact match is
     *            needed
     * @param value
     *            String value of the field
     * @param database
     *            Database name
     * @param collection
     *            Collection name in the database
     * @return A DBCursor object which you can iterate through
     */
    public IDBCursor select(String key, String operator, String value, String database, String collection);

    /**
     * Selects all the documents as a IDBCursor Object which can be used to
     * iterate through them
     *
     * @param database
     *            Database name
     * @param collection
     *            Collection name
     * @return IDBCursor object
     */
    public IDBCursor selectAll(String database, String collection);

}
