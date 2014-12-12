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
package org.eclipse.tracecompass.totalads.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel.HiddenMarkovModel;
import org.eclipse.tracecompass.internal.totalads.algorithms.ksm.KernelStateModeling;
import org.eclipse.tracecompass.internal.totalads.algorithms.sequencematching.SequenceMatching;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.algorithms.Messages;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;

/**
 * This is an AlgorithmFactory class based on a factory pattern. It registers
 * all the algorithms (or techniques) with itself. Any algorithm required by
 * clients has to be accessed through this class
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmFactory {

    // Variables that keep track of algorithms in the class
    private static AlgorithmFactory fAlgorithmTypes = null;
    private HashMap<AlgorithmTypes, HashSet<String>> fAlgList = null;
    private HashMap<String, IDetectionAlgorithm> fAcronymModels = null;

    /**
     * Constructor to create an algorithms' factory
     */
    private AlgorithmFactory() {
        fAlgList = new HashMap<>();
        fAcronymModels = new HashMap<>();
    }

    /**
     * Creates a singleton instance of the AlgorithmFactory
     *
     * @return Instance of the AlgorithmFactory
     */
    public static AlgorithmFactory getInstance() {
        if (fAlgorithmTypes == null) {
            fAlgorithmTypes = new AlgorithmFactory();
        }
        return fAlgorithmTypes;
    }

    /**
     * Destroys the instance of factory if already exists' This code is
     * necessary because when Eclipse is running and TotalADS window is closed
     * and reopened, then the static object is not recreated on the creation of
     * new object of TotalADS. We need to destroy all the objects.
     */
    public static void destroyInstance() {
        if (fAlgorithmTypes != null) {
            fAlgorithmTypes = null;
        }
    }

    /**
     * Gets the list of algorithms by a type; e.g., anomaly, classification,
     * clustering, etc.
     *
     * @param algorithmTypes
     *            An instance of AlgorithmTypes mentioning the type of algorithm
     * @return An array of algorithms of a given type
     */
    public IDetectionAlgorithm[] getAlgorithms(AlgorithmTypes algorithmTypes) {
        HashSet<String> list = fAlgList.get(algorithmTypes);
        if (list == null) {
            return null;
        }

        IDetectionAlgorithm[] models = new IDetectionAlgorithm[list.size()];
        Iterator<String> it = list.iterator();
        int count = 0;
        while (it.hasNext()) {
            models[count++] = getAlgorithmByAcronym(it.next());
        }
        return models;
    }

    /**
     * This function registers an algorithm by using its acronym as a name
     *
     * @param name
     *            Acronym of the algorithm
     * @param detectionAlgorithm
     *            The algorithm to register
     * @throws TotalADSGeneralException
     *             Validation exception
     */
    private void registerAlgorithmWithAcronym(String key, IDetectionAlgorithm detectionAlgorithm) throws TotalADSGeneralException {

        if (key.isEmpty()) {
            throw new TotalADSGeneralException(Messages.AlgorithmFactory_EmptyField);
        } else if (key.contains("_")) { //$NON-NLS-1$
            throw new TotalADSGeneralException(Messages.AlgorithmFactory_UnderscoreMsg);
        } else {

            IDetectionAlgorithm model = fAcronymModels.get(key);
            if (model == null) {
                fAcronymModels.put(key, detectionAlgorithm);
            } else {
                throw new TotalADSGeneralException(Messages.AlgorithmFactory_DuplicateName);
            }
        }

    }

    /**
     * Registers an algorithm with this factory
     *
     * @param detectionAlgorithm
     *            The algorithm to register
     * @param algorithmType
     *            An instance of {@link AlgorithmTypes}
     * @throws TotalADSGeneralException
     *             An exception for incorrect parameters
     */
    public void registerModelWithFactory(AlgorithmTypes algorithmType, IDetectionAlgorithm detectionAlgorithm)
            throws TotalADSGeneralException {

        if (algorithmType == null || detectionAlgorithm == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmFactory_NullValues);
        }

        registerAlgorithmWithAcronym(detectionAlgorithm.getAcronym(), detectionAlgorithm);
        HashSet<String> list = fAlgList.get(algorithmType);

        if (list == null) {
            list = new HashSet<>();
        }

        list.add(detectionAlgorithm.getAcronym());

        fAlgList.put(algorithmType, list);

    }

    /**
     * Get an algorithm by acronym
     *
     * @param acronym
     *            Acronym (key) of the algorithm
     * @return an instance of the algorithm
     */
    public IDetectionAlgorithm getAlgorithmByAcronym(String acronym) {
        if (acronym == null) {
            return null;
        }

        IDetectionAlgorithm model = fAcronymModels.get(acronym);
        if (model == null) {
            return null;
        }
        return model.createInstance();
    }

    /**
     * Gets all algorithms to register with the factory. Currently all the algorithms are manually
     * initialized in this function but in future versions, this code would be replaced with
     * reflection.The class will register all algorithms derived from the interface IDetectionAlgorithms
     * automatically.
     *
     * @throws TotalADSGeneralException
     *             An exception for invalid registration
     */
    public void initialize() throws TotalADSGeneralException {

        // Reflections reflections = new
        // Reflections("org.eclipse.tracecompass.totalads.ui");
        // //java.util.Set<Class<? extends IDetectionAlgorithm>> modules =
        // reflections.getSubTypesOf
        // (org.eclipse.tracecompass.totalads.ui.IDetectionAlgorithms.class);

        KernelStateModeling.registerAlgorithm();
        SequenceMatching.registerAlgorithm();
        HiddenMarkovModel.registerAlgorithm();

    }

}
