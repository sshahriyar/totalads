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

package org.eclipse.tracecompass.totalads.ui.properties;

/**
 * A class representing name-value pairs of the settings array received from the
 * algorithms. It then displays the settings in the TableViewer
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
class NameVal {
    public String name;
    public String val;

    /**
     * Creates the NameVal object
     *
     * @param key
     *            name of the setting
     * @param val
     *            Value of the field
     */
    public NameVal(String key, String val) {
        this.name = key;
        this.val = val;
    }

    /**
     * Gets the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value
     *
     * @return value
     */
    public String getVal() {
        return val;
    }

}