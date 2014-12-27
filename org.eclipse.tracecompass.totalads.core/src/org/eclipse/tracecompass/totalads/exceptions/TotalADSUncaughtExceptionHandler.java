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

import org.apache.log4j.Logger;

/**
 * Class to log uncaught exception
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
public class TotalADSUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static Logger log = Logger.getLogger(TotalADSUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
      log.error("Uncaught exception in thread: " + t.getName(), ex); //$NON-NLS-1$
    }

  }
