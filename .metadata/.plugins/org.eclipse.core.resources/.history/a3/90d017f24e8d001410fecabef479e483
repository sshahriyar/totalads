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
import org.eclipse.tracecompass.totalads.dbms.IDBMSConnection;
import org.eclipse.tracecompass.totalads.dbms.IDBMSObserver;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.dbms.Messages;
import org.eclipse.tracecompass.totalads.dbms.MongoDBCursor;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.osgi.util.NLS;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 * This class connects with MongoDB, and performs the manipulations required in
 * a program with MongoDB.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class MongoDBMS implements IDataAccessObject, IDBMSConnection {

    private MongoClient mongoClient;
    private Boolean isConnected = false;
    private ArrayList<IDBMSObserver> observers;

    /**
     * Constructor
     */
    public MongoDBMS() {
        observers = new ArrayList<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSConnection#connect(java
     * .lang.String, java.lang.Integer)
     */
    @Override
    public String connect(String host, Integer port) {
        String message = ""; //$NON-NLS-1$
        synchronized (this) {
            try {

                mongoClient = new MongoClient(host, port);
                mongoClient.setWriteConcern(WriteConcern.JOURNALED);

                mongoClient.getDatabaseNames(); // if this doesn't work then
                                                // there is no running DB.
                                                // Unfortunately,mongoClient
                                                // doesn't tell whether there is
                                                // a DB or not
            } catch (UnknownHostException ex) {
                isConnected = false;
                message = ex.getMessage();
                notifyObservers();
                return message;
            } catch (Exception ex) { // Just capture an exception and don't let
                                     // the system crash when db is not there
                isConnected = false;
                message = Messages.MongoDBMS_NoConn;
                notifyObservers();
                return message;
            }

            isConnected = true;// if it reaches here then it is connected
            notifyObservers();
        }
        return message;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSConnection#connect(java
     * .lang.String, java.lang.Integer, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String connect(String host, Integer port, String username, String password, String database) {

        String message = ""; //$NON-NLS-1$
        synchronized (this) {
            try {

                mongoClient = new MongoClient(host, port);
                mongoClient.setWriteConcern(WriteConcern.JOURNALED);

                DB db = mongoClient.getDB(database);

                if (db.authenticate(username, password.toCharArray()) == false) {
                    isConnected = false;
                    message = NLS.bind(Messages.MongoDBMS_AuthFailed, username, database);
                }
                else {
                    isConnected = true;// if it reaches here then everything is
                                       // fine

                }

            } catch (UnknownHostException ex) {
                isConnected = false;
                message = ex.getMessage();

            } catch (Exception ex) { // Just capture an exception and don't let
                                     // the system crash when db is not there
                isConnected = false;
                message = "Unable to connect to MongoDB"; //$NON-NLS-1$

            } finally {

                notifyObservers();

            }
        }
        return message;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#isConnected()
     */
    @Override
    public Boolean isConnected() {
        return isConnected;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#getDatabaseList
     * ()
     */
    @Override
    public List<String> getDatabaseList() {
        synchronized (this) {
            List<String> dbList = mongoClient.getDatabaseNames();
            dbList.remove("admin"); //$NON-NLS-1$
            dbList.remove("local"); //$NON-NLS-1$
            return dbList;
        }
    }

    /*
     * @Override(non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#datbaseExists
     * (java.lang.String)
     */
    @Override
    public boolean datbaseExists(String database) {
        List<String> databaseNames = getDatabaseList();

        for (int j = 0; j < databaseNames.size(); j++) {
            if (databaseNames.get(j).equals(database)) {
                return true;
            }
        }
        return false;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#createDatabase
     * (java.lang.String, java.lang.String[])
     */
    @Override
    public void createDatabase(String dataBase, String[] collectionNames) throws TotalADSDBMSException {
        synchronized (this) {
            if (datbaseExists(dataBase)) {
                if (isConnected == false) {// This code snippet is a check for
                                           // the breakage of connection during
                                           // the execution
                    isConnected = true; // if reconnection occurs during
                                        // execution it will notify all
                                        // observers
                    notifyObservers();
                }
                throw new TotalADSDBMSException(Messages.MongoDBMS_DBExists);

            }

            DB db = mongoClient.getDB(dataBase);
            DBObject options = com.mongodb.BasicDBObjectBuilder.start().add("capped", false).get(); //$NON-NLS-1$

            for (int j = 0; j < collectionNames.length; j++) {
                db.createCollection(collectionNames[j], options);
            }

            isConnected = true; // If this code is executed after the breakage
                                // of connection, make sure isConnected is true

            notifyObservers();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#
     * createAscendingUniquesIndexes(java.lang.String, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void createAscendingUniquesIndexes(String dataBase, String collection, String[] fields) {

        DBCollection coll = mongoClient.getDB(dataBase).getCollection(collection);
        DBObject fieldsDB = new BasicDBObject();
        for (int j = 0; j < fields.length; j++) {
            fieldsDB.put(fields[j], 1);
        }

        coll.ensureIndex(fieldsDB, new BasicDBObject("unique", true)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#
     * createDescendingUniquesIndexes(java.lang.String, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void createDescendingUniquesIndexes(String dataBase, String collection, String[] fields) {

        DBCollection coll = mongoClient.getDB(dataBase).getCollection(collection);
        DBObject fieldsDB = new BasicDBObject();
        for (int j = 0; j < fields.length; j++) {
            fieldsDB.put(fields[j], -1);
        }

        coll.ensureIndex(fieldsDB, new BasicDBObject("unique", true)); //$NON-NLS-1$

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSConnection#closeConnection
     * ()
     */
    @Override
    public void closeConnection() {
        synchronized (this) {
            isConnected = false;
            mongoClient.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSConnection#deleteDatabase
     * (java.lang.String)
     */
    @Override
    public void deleteDatabase(String database) {
        synchronized (this) {
            mongoClient.dropDatabase(database);
            notifyObservers();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#insertUsingJSON
     * (java.lang.String, com.google.gson.JsonObject, java.lang.String)
     */
    @Override
    public void insertUsingJSON(String database, JsonObject jsonObject, String collection) throws TotalADSDBMSException {
        try {
            DB db = mongoClient.getDB(database);
            DBCollection coll = db.getCollection(collection);
            BasicDBObject obj = (BasicDBObject) JSON.parse(jsonObject.toString());
            coll.insert(obj);
        } catch (Exception ex) { // If there is any exception here, cast it as
                                 // IDataAccessObject exception
            throw new TotalADSDBMSException(ex.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#
     * insertOrUpdateUsingJSON(java.lang.String, com.google.gson.JsonObject,
     * com.google.gson.JsonObject, java.lang.String)
     */
    @Override
    public void insertOrUpdateUsingJSON(String database, JsonObject keytoSearch,
            JsonObject jsonObjectToUpdate, String collection) throws TotalADSDBMSException {
        DB db = mongoClient.getDB(database);
        DBCollection coll = db.getCollection(collection);

        BasicDBObject docToUpdate = (BasicDBObject) JSON.parse(jsonObjectToUpdate.toString());

        BasicDBObject keyToSearch = (BasicDBObject) JSON.parse(keytoSearch.toString());

        WriteResult writeRes = coll.update(keyToSearch, docToUpdate, true, false);

        CommandResult cmdResult = writeRes.getLastError();
        if (!cmdResult.ok()) {
            throw new TotalADSDBMSException("Error : " + cmdResult.getErrorMessage()); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#
     * updateFieldsInExistingDocUsingJSON(java.lang.String,
     * com.google.gson.JsonObject, com.google.gson.JsonObject, java.lang.String)
     */
    @Override
    public void updateFieldsInExistingDocUsingJSON(String database, JsonObject keytoSearch, JsonObject jsonObjectToUpdate, String collection)
            throws TotalADSDBMSException {

        DB db = mongoClient.getDB(database);
        DBCollection col = db.getCollection(collection);

        BasicDBObject query = (BasicDBObject) JSON.parse(keytoSearch.toString());
        // new BasicDBObject();
        // query.put("name", "MongoDB");

        BasicDBObject newDocument = (BasicDBObject) JSON.parse(jsonObjectToUpdate.toString());
        // newDocument.put("name", "MongoDB-updated");

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", newDocument); //$NON-NLS-1$

        col.update(query, updateObj);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#selectMax(
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String selectMax(String key, String database, String collection) {
        String maxVal = ""; //$NON-NLS-1$
        DB db = mongoClient.getDB(database);
        DBCollection coll = db.getCollection(collection);
        BasicDBObject query = new BasicDBObject(key, -1);
        try (DBCursor curs = coll.find().sort(query).limit(1)) {
            if (curs.hasNext()) {
                maxVal = curs.next().get(key).toString();
            }

        } catch (Exception ex) {

        }

        return maxVal;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#select(java
     * .lang.String, java.lang.String, java.lang.Double, java.lang.String,
     * java.lang.String)
     */
    @SuppressWarnings("resource")
    @Override
    public IDBCursor select(String key, String operator, Double value, String database, String collection) {
        DBCursor cursor;
        BasicDBObject query;

        DB db = mongoClient.getDB(database);
        DBCollection coll = db.getCollection(collection);

        if (operator == null || operator.isEmpty()) {
            query = new BasicDBObject(key, value);
        } else {
            query = new BasicDBObject(key, new BasicDBObject(operator, value));
        }

        cursor = coll.find(query);

        // MongoDBCursor is AutoCloseable, we need not to worry about it here
        return new MongoDBCursor(cursor);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#select(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @SuppressWarnings("resource")
    @Override
    public IDBCursor select(String key, String operator, String value, String database, String collection) {
        DBCursor cursor;
        BasicDBObject query;

        DB db = mongoClient.getDB(database);
        DBCollection coll = db.getCollection(collection);

        if (operator == null || operator.isEmpty()) {
            query = new BasicDBObject(key, value);
        } else {
            query = new BasicDBObject(key, new BasicDBObject(operator, value));
        }

        cursor = coll.find(query);

        // MongoDBCursor is AutoCloseable, we need not to worry about it here
        return new MongoDBCursor(cursor);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject#selectAll(
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("resource")
    @Override
    public IDBCursor selectAll(String database, String collection) {

        DBCursor cursor;

        DB db = mongoClient.getDB(database);
        DBCollection coll = db.getCollection(collection);

        cursor = coll.find();
        // MongoDBCursor is AutoCloseable, we need not to worry about it here
        return new MongoDBCursor(cursor);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSSubject#addObserver(org
     * .eclipse.tracecompass.totalads.dbms.IDBMSObserver)
     */
    @Override
    public void addObserver(IDBMSObserver observer) {

        observers.add(observer);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSSubject#removeObserver(
     * org.eclipse.tracecompass.totalads.dbms.IDBMSObserver)
     */
    @Override
    public void removeObserver(IDBMSObserver observer) {
        observers.remove(observer);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.dbms.IDBMSSubject#notifyObservers()
     */
    @Override
    public void notifyObservers() {
        for (IDBMSObserver ob : observers) {
            ob.update();
        }
    }

}
