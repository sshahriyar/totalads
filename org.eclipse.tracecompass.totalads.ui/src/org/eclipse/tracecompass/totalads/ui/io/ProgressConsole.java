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

package org.eclipse.tracecompass.totalads.ui.io;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutObserver;
import org.eclipse.tracecompass.totalads.ui.live.BackgroundLiveMonitor;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsole;

/**
 * Class to display output to the Eclipse console
 *
 * @author <p>
 *         Syed shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class ProgressConsole implements IAlgorithmOutObserver {

    private MessageConsoleStream fOutStream;
    private MessageConsole fConsole;

    /**
     *
     * Constructor
     *
     * @param consoleName
     *            Name of the console
     *
     */
    public ProgressConsole(String consoleName) {
        fConsole = findConsole(consoleName);
        fConsole.activate();
        fOutStream = fConsole.newMessageStream();

    }

    /**
     * Prints a message with a new line
     *
     * @param message
     *            Message as a String object
     */
    public void println(String message) {
        fOutStream.println(message);
    }

    /**
     * Prints a message
     *
     * @param message
     *            Message as a String object
     */
    public void print(String message) {
        fOutStream.print(message);
    }

    /**
     * Gets the console object
     *
     * @param name
     *            Name of the console
     * @return
     */
    private static MessageConsole findConsole(String name) {

        ConsolePlugin plugin = org.eclipse.ui.console.ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();

        for (int i = 0; i < existing.length; i++) {
            if (name.equals(existing[i].getName())) {
                return (org.eclipse.ui.console.MessageConsole) existing[i];
            }
        }

        // No console found, so create a new one
        org.eclipse.ui.console.MessageConsole console = new org.eclipse.ui.console.MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { console });
        return console;
    }

    /**
     * Closes the console streaming
     */
    public void closeConsole() {

        try {
            fOutStream.close();
        } catch (IOException ex) {
            Logger.getLogger(BackgroundLiveMonitor.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    /**
     * Clears the console
     */
    public void clearConsole() {
        fConsole.clearConsole();
    }

    /**
     * Implements a method of {@link IAlgorithmOutObserver}
     */
    @Override
    public void updateOutput(String message) {
        print(message);

    }

}
