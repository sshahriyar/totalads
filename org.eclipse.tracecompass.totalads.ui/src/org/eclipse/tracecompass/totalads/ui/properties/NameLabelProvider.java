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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.tracecompass.totalads.ui.properties.NameVal;
import org.eclipse.swt.graphics.Image;

/**
 * This class contains a mechanism to correctly display the contents of each
 * cell in the table. It actually formats the object into a displayable form.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class NameLabelProvider extends ColumnLabelProvider {
    /*
     * Constructor
     */
    public NameLabelProvider() {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        NameVal keyVal = (NameVal) element;
        return keyVal.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.viewers.ColumnLabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        return null;
    }

}
