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

package org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel;

import java.util.Arrays;
import java.util.Random;

import org.apache.mahout.classifier.sequencelearning.hmm.HmmAlgorithms;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmModel;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmTrainer;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import org.eclipse.tracecompass.totalads.dbms.IDBCursor;
import org.eclipse.tracecompass.totalads.dbms.IDBRecord;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class implements the HMM algorithm using Apache Mahout library
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class HmmMahout {

    private HmmModel fHmm;

    /**
     * Initializes Hidden Markov Model with random initial probabilities
     *
     * @param numSymbols
     *            number of symbols
     * @param numStates
     *            Number of states
     *
     */
    public void initializeHMM(int numSymbols, int numStates) {

        fHmm = new HmmModel(numStates, numSymbols);

    }

    /**
     * Initializes HMM with the customized transition, emission and initial
     * probabilities rather than using Mahout's initialization. Specially this
     * function makes sure that initial probabilities are equal.
     *
     * @param numSymbols
     *            Number of Symbols
     * @param numStates
     *            Number of States
     */
    public void initializeHMMWithCustomizeInitialValues(int numSymbols, int numStates) {
        // Generating transition probabilities with random numbers
        Random random = new Random();
        double start = 0.0001;
        double end = 1.0000;
        DenseMatrix tansitionProbabilities = new DenseMatrix(numStates, numStates);

        // Measuring Transition Probabilities
        double[] rowSums = new double[numStates];
        Arrays.fill(rowSums, 0.0);

        for (int row = 0; row < numStates; row++) {
            for (int col = 0; col < numStates; col++) {
                tansitionProbabilities.set(row, col, getRandomRealNumber(start, end, random));
                rowSums[row] += tansitionProbabilities.get(row, col);
            }
        }

        for (int row = 0; row < numStates; row++) {
            for (int col = 0; col < numStates; col++) {
                tansitionProbabilities.set(row, col, (tansitionProbabilities.get(row, col) / rowSums[row]));
            }
        }

        // Assigning initial state probabilities Pi; i.e. probabilities at time
        // 1
        DenseVector initialProbabilities = new DenseVector(numStates);
        double initialProb = 1 / ((double) numStates);
        for (int idx = 0; idx < numStates; idx++) {
            initialProbabilities.set(idx, initialProb);
        }

        // Measuring Emission probabilities of each symbol
        DenseMatrix emissionProbabilities = new DenseMatrix(numStates, numSymbols);
        Arrays.fill(rowSums, 0.0);// Utilizing the same rowSums variable
        random = new Random();

        for (int row = 0; row < numStates; row++) {
            for (int col = 0; col < numSymbols; col++) {
                emissionProbabilities.set(row, col, getRandomRealNumber(start, end, random));
                rowSums[row] += emissionProbabilities.get(row, col);
            }
        }

        for (int row = 0; row < numStates; row++) {
            for (int col = 0; col < numSymbols; col++) {
                emissionProbabilities.set(row, col, emissionProbabilities.get(row, col) / rowSums[row]);
            }
        }

        fHmm = new HmmModel(tansitionProbabilities, emissionProbabilities, initialProbabilities);

    }

    /**
     * Returns a decimal random number within a decimal range
     *
     * @param start
     * @param end
     * @param random
     * @return
     */
    private static double getRandomRealNumber(double start, double end, Random random) {

        // get the range, casting to long to avoid overflow problems
        double range = end - start;
        // compute a fraction of the range, 0 <= frac < range
        double fraction = (range * random.nextDouble());
        double randomNumber = fraction + start;
        return randomNumber;
    }

    /**
     * Validates settings and saves them into the database after creating a new
     * database if required
     *
     * @param settings
     *            SettingsForm array
     * @param database
     *            Database name
     * @param connection
     *            IDataAccessObject object
     * @param isNewSettings
     *            True if settings are inserted first time, else false if
     *            existing fields are updated
     * @param isNewDB
     *            if new database has to be created
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     */
    public void verifySaveSettingsCreateDb(String[] settings, String database, IDataAccessObject connection, Boolean isNewSettings, Boolean isNewDB) throws TotalADSGeneralException, TotalADSDBMSException {


        JsonObject settingObject = new JsonObject();
        for (int i = 0; i < settings.length; i += 2) {

            if (SettingsCollection.NUM_STATES.toString().equalsIgnoreCase(settings[i])) {
                try {
                    Integer num_states = Integer.parseInt(settings[i + 1]);
                    settingObject.add(SettingsCollection.NUM_STATES.toString(), new JsonPrimitive(num_states));
                } catch (Exception ex) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectIntStates);
                }

            } else if (SettingsCollection.NUM_SYMBOLS.toString().equalsIgnoreCase(settings[i])) {
                try {
                    Integer num_symbols = Integer.parseInt(settings[i + 1]);
                    settingObject.add(SettingsCollection.NUM_SYMBOLS.toString(), new JsonPrimitive(num_symbols));
                } catch (Exception ex) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectIntSymbols);
                }
            } else if (SettingsCollection.SEQ_LENGTH.toString().equalsIgnoreCase(settings[i])) {
                try {
                    Integer seqLength = Integer.parseInt(settings[i + 1]);
                    settingObject.add(SettingsCollection.SEQ_LENGTH.toString(), new JsonPrimitive(seqLength));
                } catch (Exception ex) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectIntSeq);
                }
            } else if (SettingsCollection.LOG_LIKELIHOOD.toString().equalsIgnoreCase(settings[i])) {
                Double prob = null;
                try {
                    prob = Double.parseDouble(settings[i + 1]);
                } catch (Exception ex) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectDecForLog);
                }
                if (prob > 0.0) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectNegForLog);
                }
                settingObject.add(SettingsCollection.LOG_LIKELIHOOD.toString().toString(), new JsonPrimitive(prob));

            } else if (SettingsCollection.NUMBER_OF_ITERATIONS.toString().equalsIgnoreCase(settings[i])) {
                Integer it = null;
                try {
                    it = Integer.parseInt(settings[i + 1]);
                } catch (Exception ex) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectIntIteration);
                }
                if (it <= 0) {
                    throw new TotalADSGeneralException(Messages.HmmMahout_SelectIterations);
                }
                settingObject.add(SettingsCollection.NUMBER_OF_ITERATIONS.toString().toString(), new JsonPrimitive(it));

            } else if (SettingsCollection.KEY.toString().equalsIgnoreCase(settings[i])) {
                settingObject.add(SettingsCollection.KEY.toString(), new JsonPrimitive("hmm")); //$NON-NLS-1$
            }
        }

        // creating id for query searching
        JsonObject jsonKey = new JsonObject();
        jsonKey.addProperty(SettingsCollection.KEY.toString(), "hmm"); //$NON-NLS-1$

        if (isNewDB) {
            String[] collectionNames = { HmmModelCollection.COLLECTION_NAME.toString(), SettingsCollection.COLLECTION_NAME.toString()
                    , NameToIDCollection.COLLECTION_NAME.toString() };
            connection.createDatabase(database, collectionNames);
        }

        if (isNewSettings) {
            connection.insertOrUpdateUsingJSON(database, jsonKey, settingObject, SettingsCollection.COLLECTION_NAME.toString());
        } else {
            connection.updateFieldsInExistingDocUsingJSON(database, jsonKey, settingObject, SettingsCollection.COLLECTION_NAME.toString());
        }

    }

    /**
     * Loads settings from the database
     *
     * @param database
     *            Database or model name
     * @param dataAccessObject
     *            Data access object
     * @return Settings as an array of String
     * @throws TotalADSDBMSException
     *             DBMS Exception
     */
    public String[] loadSettings(String database, IDataAccessObject dataAccessObject) throws TotalADSDBMSException {
        String[] settings = null;
        try (IDBCursor cursor = dataAccessObject.selectAll(database,
                SettingsCollection.COLLECTION_NAME.toString())) {
            if (cursor.hasNext()) {
                settings = new String[10];

                IDBRecord dbObject = cursor.next();
                settings[0] = SettingsCollection.NUM_STATES.toString();
                settings[1] = dbObject.get(SettingsCollection.NUM_STATES.toString()).toString();
                settings[2] = SettingsCollection.NUMBER_OF_ITERATIONS.toString();
                settings[3] = dbObject.get(SettingsCollection.NUMBER_OF_ITERATIONS.toString()).toString();
                settings[4] = SettingsCollection.NUM_SYMBOLS.toString();
                settings[5] = dbObject.get(SettingsCollection.NUM_SYMBOLS.toString()).toString();
                settings[6] = SettingsCollection.LOG_LIKELIHOOD.toString();
                settings[7] = dbObject.get(SettingsCollection.LOG_LIKELIHOOD.toString()).toString();
                settings[8] = SettingsCollection.SEQ_LENGTH.toString();
                settings[9] = dbObject.get(SettingsCollection.SEQ_LENGTH.toString()).toString();
            }
        }
        return settings;
    }

    /**
     * Trains an HMM on a sequence using the BaumWelch algorithm
     *
     * @param numIterations
     *            Number of Iterations
     * @param observedSequence
     *            The sequence
     */
    public void learnUsingBaumWelch(Integer numIterations, Integer[] observedSequence) {

        int[] seq = new int[observedSequence.length];
        for (int i = 0; i < seq.length; i++) {
            seq[i] = observedSequence[i];
        }
        HmmTrainer.trainBaumWelch(fHmm, seq, 0.0001, numIterations, true);

    }

    /**
     * Trains an HMM on a sequence using the BaumWelch algorithm
     *
     * @param numIterations
     *            Number of iterations
     * @param observedSequence
     *            The sequence
     */
    public void learnUsingBaumWelch(Integer numIterations, int[] observedSequence) {

        HmmTrainer.trainBaumWelch(fHmm, observedSequence, 0.0001, numIterations, true);

    }

    /**
     * Returns the observation sequence's log likelihood based on a model
     *
     * @param sequence
     *            Integer array of sequences
     * @return Log Likelihood
     */
    public double observationLikelihood(int[] sequence) {

        Matrix m = HmmAlgorithms.forwardAlgorithm(fHmm, sequence, true);
        int lastCol = m.numCols() - 1;
        int numRows = m.numRows();
        double sum = 0.0;
        for (int i = 0; i < numRows; i++) {
            sum += m.getQuick(i, lastCol);
        }

        return sum;

    }

    /**
     * Update HMM based on an incremental version as described in
     * http://goanna.cs.rmit.edu.au/~jiankun/Sample_Publication/ICON04_Dau.pdf
     *
     * @param sequence
     *            The sequence
     * @param dataAccessObject
     *            Data access object
     * @param database
     *            Model name
     * @throws TotalADSDBMSException
     *             Validation exception
     */
    public void updatePreviousModel(Integer[] sequence, IDataAccessObject dataAccessObject, String database) throws TotalADSDBMSException {
        int[] seq = new int[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            seq[i] = sequence[i];
        }

        double prob = 1.0;
        Matrix transition = fHmm.getTransitionMatrix().divide(prob);
        Matrix emission = fHmm.getEmissionMatrix().divide(prob);
        Vector initial = fHmm.getInitialProbabilities().divide(prob);

        HmmMahout oldHMM = new HmmMahout();
        oldHMM.loadHmm(dataAccessObject, database);
        if (oldHMM.fHmm != null) {
            transition = oldHMM.fHmm.getTransitionMatrix().plus(transition);
            emission = oldHMM.fHmm.getEmissionMatrix().plus(emission);
            initial = oldHMM.fHmm.getInitialProbabilities().plus(initial);
        }

        HmmMahout newHMM = new HmmMahout();
        newHMM.fHmm = new HmmModel(transition, emission, initial);
        newHMM.saveHMM(database, dataAccessObject);
    }

    /**
     * Loads the model directly from a database
     *
     * @param dao
     *            Data access object
     * @param modelName
     *            Model (or database) name
     * @throws TotalADSDBMSException
     *             DBMS exception
     */
    public void loadHmm(IDataAccessObject dao, String modelName) throws TotalADSDBMSException {

        try (IDBCursor cursor = dao.selectAll(modelName,
                HmmModelCollection.COLLECTION_NAME.toString())) {
            if (cursor.hasNext()) {
                Gson gson = new Gson();
                if (cursor.hasNext()) {
                    IDBRecord dbObject = cursor.next();
                    Object emissionProb = dbObject.get(HmmModelCollection.EMISSIONPROB.toString());
                    Object transsitionProb = dbObject.get(HmmModelCollection.TRANSITIONPROB.toString());
                    Object initialProb = dbObject.get(HmmModelCollection.INTITIALPROB.toString());

                    DenseMatrix emissionMatrix = gson.fromJson(emissionProb.toString(), DenseMatrix.class);
                    DenseMatrix transitionMatrix = gson.fromJson(transsitionProb.toString(), DenseMatrix.class);
                    DenseVector initialProbVector = gson.fromJson(initialProb.toString(), DenseVector.class);

                    fHmm = new HmmModel(transitionMatrix, emissionMatrix, initialProbVector);
                }

            }
        }
    }

    /**
     * This functions saves the HmmJahmm model into the database
     *
     * @param database
     *            Model (or database) name
     * @param dao
     *            Data access object
     * @throws TotalADSDBMSException
     *             DBMS exception
     */
    public void saveHMM(String database, IDataAccessObject dao) throws TotalADSDBMSException {
        // / Inserting the states and probabilities
        // Creating states ids
        String key = "hmm"; //$NON-NLS-1$
        Gson gson = new Gson();

        DenseMatrix emissionMatrix = (DenseMatrix) fHmm.getEmissionMatrix();
        DenseMatrix transitionMatrix = (DenseMatrix) fHmm.getTransitionMatrix();
        Vector initialProb = fHmm.getInitialProbabilities();

        JsonObject hmmDoc = new JsonObject();
        hmmDoc.add(HmmModelCollection.KEY.toString(), new JsonPrimitive(key));
        hmmDoc.add(HmmModelCollection.EMISSIONPROB.toString(), gson.toJsonTree(emissionMatrix));
        hmmDoc.add(HmmModelCollection.TRANSITIONPROB.toString(), gson.toJsonTree(transitionMatrix));
        hmmDoc.add(HmmModelCollection.INTITIALPROB.toString(), gson.toJsonTree(initialProb));

        // Creating id for query searching
        JsonObject jsonTheKey = new JsonObject();
        jsonTheKey.addProperty(HmmModelCollection.KEY.toString(), key);

        dao.insertOrUpdateUsingJSON(database, jsonTheKey, hmmDoc, HmmModelCollection.COLLECTION_NAME.toString());

    }

    /**
     * Prints the model
     *
     * @return HMM model in the textual representation
     */
    @Override
    public String toString() {

        return Messages.HmmMahout_HmmModel + "\n" + //$NON-NLS-1$
                Messages.HmmMahout_HiddenStates + fHmm.getNrOfHiddenStates() + "\n" + //$NON-NLS-1$
                Messages.HmmMahout_ObservableEvents + fHmm.getNrOfOutputStates() + "\n" + //$NON-NLS-1$
                Messages.HmmMahout_EmisssionProbs + fHmm.getEmissionMatrix().toString() + "\n" + //$NON-NLS-1$
                Messages.HmmMahout_Transition + fHmm.getTransitionMatrix().toString() + "\n" + //$NON-NLS-1$
                Messages.HmmMahout_Initial + fHmm.getInitialProbabilities();

    }

}
