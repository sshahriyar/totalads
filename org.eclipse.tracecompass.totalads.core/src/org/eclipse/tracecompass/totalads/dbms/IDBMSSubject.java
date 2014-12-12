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

import org.eclipse.tracecompass.totalads.dbms.IDBMSObserver;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;

/**
 * Subject interface used by {@link IDataAccessObject} to notify observers
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
interface IDBMSSubject {
    /**
     * Adds an observer of type {@link IDBMSObserver}
     *
     * @param observer
     *            Observer object
     */

    public void addObserver(IDBMSObserver observer);

    /**
     * Removes an observer of type {@link IDBMSObserver}
     *
     * @param observer
     *            Observer object
     */

    public void removeObserver(IDBMSObserver observer);

    /**
     * Notifies all observers of type {@link IDBMSObserver}
     */
    public void notifyObservers();

}
