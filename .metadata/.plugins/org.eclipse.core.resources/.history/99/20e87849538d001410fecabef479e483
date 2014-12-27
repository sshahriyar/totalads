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

package org.eclipse.tracecompass.totalads.ui.models.dbconnect;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.tracecompass.totalads.ui.models.DataModelsView;
import org.eclipse.tracecompass.totalads.ui.models.dbconnect.DBConnectWizard;
import org.eclipse.tracecompass.totalads.ui.models.dbconnect.Messages;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements a handler to open the new connection wizard dialog when
 * an icon on the {@link DataModelsView} is pressed . Its object is executed by
 * Eclipse automatically whenever the create model icon is clicked. *
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class DBConnectionHandler implements IHandler {

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
        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                new DBConnectWizard());

        if (wizardDialog.open() == Window.OK) {

            MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    msgTitle, Messages.DBConnectionHandler_SuccessfulCon);

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
