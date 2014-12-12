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
 * This interface opens and close connection with the database. It also deletes
 * a database. These are the operations that are only accessible through
 * DBMSFactory class and not accessible to the algorithms via IDataAccessObject
 * interface.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
interface IDBMSConnection {
    /**
     * Connects with the database
     *
     * @param host
     *            Host name
     * @param port
     *            Port number
     * @return Returns an empty message if connection is made with the database,
     *         else returns the error message
     */
    public String connect(String host, Integer port);

    /**
     * Connects with the database using a authentication mechanism
     *
     * @param host
     *            Host name
     * @param port
     *            Port name
     * @param username
     *            User name
     * @param password
     *            Password
     * @param database
     *            Database name
     * @return Empty message if connected or error message
     */
    public String connect(String host, Integer port, String username, String password, String database);

    /**
     * Closes the connection
     */
    public void closeConnection();

    /**
     * Deletes a database
     *
     * @param database
     *            Database name
     */
    public void deleteDatabase(String database);
}
