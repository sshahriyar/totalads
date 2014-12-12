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

package org.eclipse.tracecompass.totalads.ui;



import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisView;
import org.eclipse.tracecompass.totalads.ui.live.LiveMonitorView;
import org.eclipse.tracecompass.totalads.ui.live.LiveResultsView;
import org.eclipse.tracecompass.totalads.ui.modeling.ModelingView;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.properties.PropertiesView;
import org.eclipse.tracecompass.totalads.ui.results.ResultsView;
import org.eclipse.tracecompass.internal.lttng2.control.ui.views.ControlView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
/**
 * A simple implementation of {@link IPerspectiveFactory} that is used by the workbench
 * to produce a custom perspective for the plugin
 *
 * @author  <p> Efraim J Lopez  efraimlopez@gmail.com </p>
 * 			<p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
public class TotalAdsPerspectiveFactory implements IPerspectiveFactory {

    /** Perspective ID */
    public static final String ID = "org.eclipse.tracecompass.totalads.ui.TotalAdsPerspective"; //$NON-NLS-1$


    private static final String PROJECT_VIEW_ID = IPageLayout.ID_PROJECT_EXPLORER;
    private static final String CONTROL_VIEW_ID = ControlView.ID;


    @Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		//Create right folders
        IFolderLayout topRightFolder = layout.createFolder(
                "topRightFolder", IPageLayout.RIGHT, 0.80f,IPageLayout.ID_EDITOR_AREA);  //$NON-NLS-1$
        topRightFolder.addView(DataModelsView.VIEW_ID);


        IFolderLayout bottomRightFolder = layout.createFolder(
                "bottomRightFolder", IPageLayout.BOTTOM, 0.50f,"topRightFolder");  //$NON-NLS-1$ //$NON-NLS-2$
        bottomRightFolder.addView(PropertiesView.VIEW_ID);
        //bottomRightFolder.addView(IPageLayout.ID_PROP_SHEET);

		// Create Left folders
        IFolderLayout topLeftFolder = layout.createFolder(
                "topLeftFolder", IPageLayout.LEFT, 0.20f, IPageLayout.ID_EDITOR_AREA);  //$NON-NLS-1$
        topLeftFolder.addView(PROJECT_VIEW_ID);


        IFolderLayout bottomLeftFolder = layout.createFolder(
                "bottomLeftFolder", IPageLayout.BOTTOM, 0.70f, "topLeftFolder");  //$NON-NLS-1$ //$NON-NLS-2$
        bottomLeftFolder.addView(CONTROL_VIEW_ID);

        // Create the center folders
        IFolderLayout centerTopFolder = layout.createFolder(
                "centerTopFolder", IPageLayout.TOP, 0.70f, IPageLayout.ID_EDITOR_AREA);  //$NON-NLS-1$
        centerTopFolder.addView(DiagnosisView.VIEW_ID);
        centerTopFolder.addView(ModelingView.VIEW_ID);
        centerTopFolder.addView(LiveMonitorView.VIEW_ID);

        IFolderLayout centerMiddleFolder = layout.createFolder(
                "centerMiddleFolder", IPageLayout.BOTTOM, 0.25f,"centerTopFolder");  //$NON-NLS-1$ //$NON-NLS-2$
        centerMiddleFolder.addView(ResultsView.VIEW_ID);
        centerMiddleFolder.addView(LiveResultsView.VIEW_ID);

        IFolderLayout centerBottomFolder = layout.createFolder(
                "centerBottomFolder", IPageLayout.BOTTOM, 0.70f,"centerMiddleFolder");  //$NON-NLS-1$ //$NON-NLS-2$
        centerBottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}




}
