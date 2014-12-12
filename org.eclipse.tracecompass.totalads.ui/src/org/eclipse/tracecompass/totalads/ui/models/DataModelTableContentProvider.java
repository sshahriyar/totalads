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

package org.eclipse.tracecompass.totalads.ui.models;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.ui.models.Messages;

/**
 * It provides formatted contents to a table
 * @author <p>Efraim Lopez </p>
 *         <p> Syed Shariyar Murtaza justssahry@htomail.com </p>
 *
 */
class DataModelTableContentProvider	implements IStructuredContentProvider{


	private IDataAccessObject dao = null;
	public static final String EMPTY_VIEW_FIELD=Messages.DataModelTableContentProvider_NoConn;
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {


	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	    if(newInput != null) {
	    	dao = (IDataAccessObject) newInput;
	     }
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		dao = (IDataAccessObject) inputElement;

		if(dao.isConnected()) {
            return dao.getDatabaseList().toArray();
        }
		return new String[] {EMPTY_VIEW_FIELD};
	}


}
