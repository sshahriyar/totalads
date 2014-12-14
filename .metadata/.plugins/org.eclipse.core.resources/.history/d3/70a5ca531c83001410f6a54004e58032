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
package org.eclipse.tracecompass.totalads.core;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.tracecompass.totalads.core.Activator;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator for the plugin, called by Eclipse
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        init();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
        deInitialize();
    }

    /**
     *
     * Initializes TotalADS
     *
     */

    private static void init() {
        try {

            AlgorithmFactory algFactory = AlgorithmFactory.getInstance();
            algFactory.initialize();

            TraceTypeFactory trcTypeFactory = TraceTypeFactory.getInstance();
            trcTypeFactory.initialize();

            // Initialize the logger
            Handler handler = null;
            //handler = new java.util.logging.SocketHandler("localhost", 5000); //$NON-NLS-1$
            handler = new java.util.logging.FileHandler(getCurrentPath() + "totaladslog.xml"); //$NON-NLS-1$
            Logger.getLogger("").addHandler(handler); //$NON-NLS-1$

        } catch (Exception ex) { // capture all the exceptions here

            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the current directory of the application
     *
     * @return Path
     * @throws Exception
     */
    private static String getCurrentPath() {
        String applicationDir = Activator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return applicationDir + File.separator;
    }

    /**
     * DeInitializes TotalADS
     */
    private static void deInitialize() {
        DBMSFactory.INSTANCE.closeConnection();
        // This code deinitializes the Factory instance. It was necessary
        // because
        // if TotalADS plugin is reopened in running Eclipse, the static objects
        // are not
        // deinitialized on previous close of the plugin.
        AlgorithmFactory.destroyInstance();
        TraceTypeFactory.destroyInstance();
    }

}
