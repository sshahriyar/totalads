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

package org.eclipse.tracecompass.totalads.ui.models.delete;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.totalads.ui.models.delete.DeleteModelHandler;
import org.eclipse.tracecompass.totalads.ui.models.delete.Messages;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements the delete command for an icon on the
 * {@link DataModelsView}). Its object is executed by Eclipse automatically
 * whenever the delete icon is clicked.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class DeleteModelHandler implements IHandler {
    private HashSet<String> fSelectedModels;

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public DeleteModelHandler() {
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
     * This function deletes a model from the database (non-Javadoc)
     *
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        String msgTitle="TotalADS"; //$NON-NLS-1$
      //  MessageBox msgBoxYesNo = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        //        SWT.ICON_INFORMATION | SWT.YES | SWT.NO);

        //msgBoxYesNo.setText(msgTitle);

        try {

            // checking if a model is selected
            if (fSelectedModels.size() <= 0) {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        msgTitle,Messages.DeleteModelHandler_SelModel);
                 return null;
            }
            //
            // Get confirmation
            //
            Iterator<String> it = fSelectedModels.iterator();
            String message = Messages.DeleteModelHandler_WantToDel;
            int count = 1;
            while (it.hasNext()) {
                if (count == 1 && fSelectedModels.size() <= 2) {
                    message += it.next() + " "; //$NON-NLS-1$
                } else if (count == 1 && fSelectedModels.size() > 2) {
                    message += it.next() + ", "; //$NON-NLS-1$
                } else if (count > 1 && count < fSelectedModels.size()) {
                    message += it.next() + ", "; //$NON-NLS-1$
                } else if (count > 1 && count == fSelectedModels.size()) {
                    message += Messages.DeleteModelHandler_And + it.next();
                }

                count++;
            }
            message += "?"; //$NON-NLS-1$
            MessageDialog msgBoxYesNo= new MessageDialog(Display.getDefault().getActiveShell(),
                    msgTitle, null, message, MessageDialog.QUESTION, new String[]{
                     IDialogConstants.NO_LABEL, IDialogConstants.YES_LABEL,}, 0);


            if (msgBoxYesNo.open() == 1) {//YES
                // Delete models now
                it = fSelectedModels.iterator();
                while (it.hasNext()) {
                    String err = DBMSFactory.INSTANCE.deleteDatabase(it.next());
                    if (!err.isEmpty()) { // if the database is not connected
                        MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                msgTitle,err);
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        msgTitle,ex.getMessage());
            } else {
                MessageDialog.openError(org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        msgTitle,Messages.DeleteModelHandler_ErrDel);
            }

            Logger.getLogger(DeleteModelHandler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // Check if connection still exists and all the views are notified
            // of the presence and absence of connection
            DBMSFactory.INSTANCE.verifyConnection();
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
