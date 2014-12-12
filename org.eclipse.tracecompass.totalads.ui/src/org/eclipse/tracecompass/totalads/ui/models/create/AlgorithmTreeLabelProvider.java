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

package org.eclipse.tracecompass.totalads.ui.models.create;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
/**
 * This class updates the contents of each cell in the tree on the algorithm selection page
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
public class AlgorithmTreeLabelProvider extends StyledCellLabelProvider {

	/**
	 * Constructor
	 */
	public AlgorithmTreeLabelProvider() {

	}

	/**
	 * Sets the style
	 * @param style Style number
	 */
	 public AlgorithmTreeLabelProvider(int style) {
		super(style);

	}

	//
	// This function is used to update the contents of the cells with only names of the algorithms
	//
	@Override
	 public void update(ViewerCell cell) {
	      Object element = cell.getElement();
	      StyledString text = new StyledString();
	      String algorithmName = ((IDetectionAlgorithm) element).getName();
	      text.append(algorithmName);
	      cell.setText(text.toString());
	      cell.setStyleRanges(text.getStyleRanges());
	      super.update(cell);

	    }
}
