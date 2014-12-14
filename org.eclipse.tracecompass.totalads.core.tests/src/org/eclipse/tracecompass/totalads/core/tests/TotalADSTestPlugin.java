package org.eclipse.tracecompass.totalads.core.tests;



/*******************************************************************************
 * Copyright (c) 2014 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Matthew Khouzam - Initial API and implementation
 *******************************************************************************/



import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author Syed Shariyar Murtaza
 */
public class TotalADSTestPlugin implements BundleActivator {

    /**
     * The plug-in ID
     */
    public static final String PLUGIN_ID = "org.eclipse.tracecompass.totalads.core.tests";

    private static BundleContext fContext;

    /**
     * Gets the bundle of this plug-in.
     *
     * @return the oel.btf.core.tests bundle
     */
    public static Bundle getBundle() {
        if (fContext == null) {
            return null;
        }
        return fContext.getBundle();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        fContext = context;
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
