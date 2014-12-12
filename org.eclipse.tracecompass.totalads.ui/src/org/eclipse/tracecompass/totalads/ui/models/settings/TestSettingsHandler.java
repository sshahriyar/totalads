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

package org.eclipse.tracecompass.totalads.ui.models.settings;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.models.settings.Messages;
import org.eclipse.tracecompass.totalads.ui.models.settings.TestSettingsDialog;
import org.eclipse.tracecompass.totalads.ui.models.settings.TestSettingsHandler;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements the settings command. It allows to edit settings for a
 * model selected in the {@link DataModelsView}.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@htomail.com
 *         </p>
 *
 */
public class TestSettingsHandler implements IHandler {
    private HashSet<String> fSelectedModels;
    private TestSettingsDialog fSettingsDialog;

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public TestSettingsHandler() {
        fSelectedModels = new HashSet<>();

        // / Registers a listener to Eclipse to get the list of models selected
        // (checked) by the user
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addSelectionListener(DataModelsView.VIEW_ID, new ISelectionListener() {

            @Override
            public void selectionChanged(IWorkbenchPart part, ISelection selection) {

                if (part instanceof DataModelsView) {
                    Object obj = ((org.eclipse.jface.viewers.StructuredSelection) selection).getFirstElement();
                    fSelectedModels = (HashSet<String>) obj;

                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core
     * .commands.IHandlerListener)
     */
    @Override
    public void addHandlerListener(IHandlerListener handlerListener) {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.commands.IHandler#dispose()
     */
    @Override
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String msgTitle="TotalADS"; //$NON-NLS-1$
        try {

            // Checking for the proper selection
            if (fSelectedModels.size() > 1) {
                MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),msgTitle , Messages.TestSettingsHandler_SelOneModel);
                return null;

            } else if (fSelectedModels.size() < 1) {
                MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),msgTitle ,
                        Messages.TestSettingsHandler_SelModel);

                return null;
            }

            IDataAccessObject connection = DBMSFactory.INSTANCE.getDataAccessObject();
            // Open the settings dialog
            if (connection.isConnected()) {
                String model = fSelectedModels.iterator().next(); // get the
                                                                  // only
                                                                  // selected
                                                                  // model
                IDetectionAlgorithm algorithm = AlgorithmUtility.getAlgorithmFromModelName(model);
                String[] settings = algorithm.getTestSettings(model, connection);

                fSettingsDialog = new TestSettingsDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                        , algorithm, model, settings);

                fSettingsDialog.create();
                fSettingsDialog.open();
            } else {
                MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),msgTitle ,
                        Messages.TestSettingsHandler_NoConn);
            }

        } catch (TotalADSGeneralException ex) {
            MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),msgTitle ,
                    ex.getMessage());
           // msgBoxErr.setMessage(ex.getMessage());
           // msgBoxErr.open();
        } catch (Exception ex) {
            MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),msgTitle ,
                    ex.getMessage());
          //  msgBoxErr.setMessage(ex.getMessage());
           // msgBoxErr.open();
            Logger.getLogger(TestSettingsHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // Check if connection still exists and all the views are notified
            // of the presence and absence of connection
            DBMSFactory.INSTANCE.verifyConnection();

        } finally {
            fSettingsDialog = null;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.commands.IHandler#isEnabled()
     */
    @Override
    public boolean isEnabled() {

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.commands.IHandler#isHandled()
     */
    @Override
    public boolean isHandled() {

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.
     * core.commands.IHandlerListener)
     */
    @Override
    public void removeHandlerListener(IHandlerListener handlerListener) {

    }

}
