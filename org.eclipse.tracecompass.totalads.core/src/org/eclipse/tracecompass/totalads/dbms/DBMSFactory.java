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

import org.eclipse.tracecompass.totalads.dbms.IDBMSConnection;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.dbms.Messages;
import org.eclipse.tracecompass.totalads.dbms.MongoDBMS;

/**
 * Initializes a singleton instance of the database management system and
 * provides utility functions
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *         <p>
 *         Efraim Lopez efraimlopez@gmail.com
 *         </p>
 *
 */
public enum DBMSFactory {
    /**
     * Represents a singleton instance of DBMS factory
     */
    INSTANCE;
    private boolean init = false;
    private MongoDBMS mongoDBMS;
    private IDataAccessObject daoRef;
    private IDBMSConnection connRef;
    private String fHost = ""; //$NON-NLS-1$
    private Integer fPort;
    private String fUserName = ""; //$NON-NLS-1$
    private String fDatabase = ""; //$NON-NLS-1$
    private String fPassword = ""; //$NON-NLS-1$

    /**
     * Initializes an object the database
     *
     * @return An object of type IDataAccessObject
     */
    public IDataAccessObject getDataAccessObject() {
        synchronized (this) {
            if (!init) {
                mongoDBMS = new MongoDBMS();
                daoRef = mongoDBMS;
                connRef = mongoDBMS;
                init = true;
            }
        }
        return daoRef;
    }

    /**
     * Closes the connection
     */
    public void closeConnection() {
        synchronized (this) {
            if (daoRef != null && daoRef.isConnected()) {
                connRef.closeConnection();

            }
        }
    }

    /**
     * Opens a connection
     *
     * @param host
     *            Host name
     * @param port
     *            Port number
     * @return An empty string on success, else an error message
     */
    public String openConnection(String host, Integer port) {
        String err = ""; //$NON-NLS-1$
        synchronized (this) {
            if (daoRef == null)
            {
                getDataAccessObject();// initialize it;
            }
            if (daoRef.isConnected()) {
                connRef.closeConnection();
            }
            err = connRef.connect(host, port);
            if (err.isEmpty()) {
                this.fHost = host;
                this.fPort = port;
            }

        }
        return err;
    }

    /**
     * Opens a connection
     *
     * @param host
     *            Host name
     * @param port
     *            Port number
     * @param userName
     *            User name
     * @param password
     *            Password
     * @param database
     *            Database name
     * @return An empty string on success, else an error message
     */
    public String openConnection(String host, Integer port, String userName, String password, String database) {
        String err = ""; //$NON-NLS-1$
        synchronized (this) {
            if (daoRef == null)
            {
                getDataAccessObject();// initialize it;
            }
            if (daoRef.isConnected()) {
                connRef.closeConnection();
            }
            err = connRef.connect(host, port, userName, password, database);
            if (err.isEmpty()) {
                this.fHost = host;
                this.fPort = port;
                this.fUserName = userName;
                this.fPassword = password;
                this.fDatabase = database;
            }
        }
        return err;
    }

    /**
     * Deletes a database
     *
     * @param database
     *            Database name
     * @return An empty string on success, else an error message
     */
    public String deleteDatabase(String database) {
        String err = ""; //$NON-NLS-1$
        synchronized (this) {
            if (daoRef != null && daoRef.isConnected()) {
                connRef.deleteDatabase(database);
            } else {
                err = Messages.DBMSFactory_NoConnection;
            }
        }
        return err;
    }

    /**
     * Reconnects to the database. Use it when the connection is lost and you
     * need to verify the connection
     *
     * @return Error message or an empty string if all goes well
     */
    public String verifyConnection() {
        String err = ""; //$NON-NLS-1$
        synchronized (this) {
            if (daoRef != null && daoRef.isConnected()) {
                connRef.closeConnection();
            }
            if (fHost.isEmpty()) {
                err = Messages.DBMSFactory_VerifyConnection;
            } else if (fUserName.isEmpty()) {
                err = connRef.connect(fHost, fPort);
            } else {
                err = connRef.connect(fHost, fPort, fUserName, fPassword, fDatabase);
            }
        }
        return err;
    }
}
