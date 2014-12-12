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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.models.create.CreateModelWizard;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements a create model command handler for the icon on the
 * {@link DataModelsView}). Its object is executed by Eclipse automatically
 * whenever the create model icon is clicked.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class CreateModelHandler implements IHandler {
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
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();

        if (dao.isConnected()) {
            WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    new CreateModelWizard());
            wizardDialog.open();
        } else {
            String msgTitle="TotalADS";             //$NON-NLS-1$
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    msgTitle , Messages.CreateModelHandler_NoConn);

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
