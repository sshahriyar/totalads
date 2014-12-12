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
package org.eclipse.tracecompass.totalads.exceptions;

/**
 * This class defines custom IDataAccessObject exceptions that are thrown when a
 * user does not select the proper IDataAccessObject configurations
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class TotalADSDBMSException extends Exception {

    /**
     * Serializable id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public TotalADSDBMSException() {

    }

    /**
     * Creates an exception with a message
     *
     * @param message
     *            Message for the exception
     */
    public TotalADSDBMSException(String message) {
        super(message);

    }

    /**
     * Creates an exception with an object of type Throwable
     *
     * @param cause
     *            An object of type Throwable
     */
    public TotalADSDBMSException(Throwable cause) {
        super(cause);

    }

    /**
     * Creates an exception with a message and a Throwable object
     *
     * @param message
     *            Message for the exception
     * @param cause
     *            An object of type Throwable
     */
    public TotalADSDBMSException(String message, Throwable cause) {
        super(message, cause);

    }

}
