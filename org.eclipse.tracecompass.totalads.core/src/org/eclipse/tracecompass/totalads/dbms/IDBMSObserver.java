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
 * Observer interface to be implemented by those classes which wants update from
 * the IDataAccessObject
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
public interface IDBMSObserver {
    /**
     * Updates the observer
     */
    public void update();
    /**
     * Provides the information as text when updating
     *
     * public void update(String information);
     */

}
