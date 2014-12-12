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

package org.eclipse.tracecompass.totalads.ui.modeling;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.ui.modeling.BackgroundModeling;
import org.eclipse.tracecompass.totalads.ui.modeling.Messages;
import org.eclipse.tracecompass.totalads.ui.modeling.Modeling;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.totalads.ui.io.ProgressConsole;
import org.eclipse.ui.PlatformUI;

/**
 * This class builds a model by executing itself as a thread in the background. It is
 * instantiated and executed from the {@link Modeling} class.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */

public class BackgroundModeling implements Runnable {
    private String fTrainingTraces;
    private String fValidationTraces;
    private ITraceTypeReader fTraceReader;
    private HashSet<String> fModelsList;
    private Button fBtnModelStart;
    private Button fBtnModelStop;

    /**
     * Constructor
     *
     * @param trainingTraces
     *            Training traces
     * @param validationTraces
     *            Validation trace folder
     * @param traceReader
     *            Trace reader selected by the user
     * @param models
     *            Models
     * @param btnBuild
     *            Evaluation Button
     * @param btnStop
     *            Stop Button
     */
    public BackgroundModeling(String trainingTraces,
            String validationTraces, ITraceTypeReader traceReader, HashSet<String> models, Button btnBuild
            , Button btnStop) {
        this.fTrainingTraces = trainingTraces;
        this.fValidationTraces = validationTraces;
        this.fTraceReader = traceReader;
        this.fModelsList = models;
        this.fBtnModelStart = btnBuild;
        this.fBtnModelStop = btnStop;
    }

    /**
     * Implementation of the thread
     */
    @Override
    public void run() {
        String msg = null;

        try {

            ProgressConsole console = new ProgressConsole(Messages.BackgroundModeling_Modeling);
            console.clearConsole();

            AlgorithmOutStream outStream = new AlgorithmOutStream();
            outStream.addObserver(console);

            String[] models = new String[fModelsList.size()];
            models = fModelsList.toArray(models);

            AlgorithmUtility.trainAndValidateModels(fTrainingTraces, fValidationTraces, fTraceReader,
                    models, outStream);

        } catch (TotalADSGeneralException ex) {// handle UI exceptions here
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundModeling_UIErr;
            } else {
                msg = ex.getMessage();
            }
        } catch (TotalADSDBMSException ex) {// handle IDataAccessObject
                                            // exceptions here
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundModeling_DBErr;
            } else {
                msg = ex.getMessage();
            }
            Logger.getLogger(BackgroundModeling.class.getName()).log(Level.WARNING, msg, ex);

        } catch (TotalADSReaderException ex) {// handle Reader exceptions here
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundModeling_ReaderErr;
            } else {
                msg = ex.getMessage();
            }
            Logger.getLogger(BackgroundModeling.class.getName()).log(Level.WARNING, msg, ex);

        } catch (Exception ex) { // handle all other exceptions here and log
                                 // them too.
                                 // UI exceptions are simply notifications--no
                                 // need to log them

            if (ex.getMessage() == null) {
                msg = Messages.BackgroundModeling_SevereErr;
            } else {
                msg = ex.getMessage();
            }
            // ex.printStackTrace();
            Logger.getLogger(BackgroundModeling.class.getName()).log(Level.SEVERE, msg, ex);
            // An exception could be thrown due to unavailability of the db,
            // make sure that the connection is not lost
            DBMSFactory.INSTANCE.verifyConnection();
            // We don't have to worry about exceptions here as the above
            // function handles all the exceptions
            // and just returns a message. This function also initializes
            // connection info to correct value
            // We cannot write above function under ConnectinException block
            // because such exception is never thrown
            // and Eclipse starts throwing errors
        } finally {
            final String exception = msg;

            // Executing GUI elements on a main thread from another thread
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    String msgTitle="TotalADS"; //$NON-NLS-1$
                    if (exception != null) { // if there has been any exception
                                             // then show its message

                        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                                , msgTitle, exception);


                    } else {
                        MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                                , msgTitle,Messages.BackgroundModeling_ModelingFinished);

                    }

                    fBtnModelStart.setEnabled(true);
                    fBtnModelStop.setEnabled(false);

                }
            });

        }
    }
}
