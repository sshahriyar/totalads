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
package org.eclipse.tracecompass.internal.totalads.readers.textreaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
/**
 * This class reads a text file and returns each line as an fEvent.
 *
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
public class TextLineTraceReader implements ITraceTypeReader {
	//---------------------------------------------------------------------------
	//Inner class: Implements the iterator to iterate through the text file
	//---------------------------------------------------------------------------
	private class TextLineIterator implements ITraceIterator{
		private BufferedReader fBufferedReader;
		private String fEvent=""; //$NON-NLS-1$
		private Boolean fIsClose=false;

		/**
		 * Constructor
		 * @param file File object
		 * @throws FileNotFoundException An exception about file
		 */
		public TextLineIterator(File  file) throws FileNotFoundException{
			fBufferedReader= new BufferedReader(new FileReader(file));
		}

		/*
		 *Advances the iterator
		 */
		@Override
		public boolean advance() throws TotalADSReaderException  {
		   boolean isAdvance=false;
		   try {
				do {
				   	fEvent=fBufferedReader.readLine();

					 if (fEvent==null){
						  fBufferedReader.close();
						  fIsClose=true;
						  isAdvance=false;
					 }
					 else{
						 isAdvance=true;
						  fEvent=fEvent.trim();
					 }
				}while(fEvent!=null && fEvent.isEmpty());// if there are empty lines or there is no match on regex on a line, no need to send an fEvent.
										// keep looping till the end of file.


			} catch (IOException e) {

				throw new TotalADSReaderException(e.getMessage());
			}
		   return isAdvance;
		}

		/*
		 * Returns the Current fEvent
		 */
		@Override
		public String getCurrentEvent() {

			return fEvent;
		}

		/**
		 * Closes the iterator
		 * @throws TotalADSReaderException An exception about reading errors
		 */
		@Override
		public void close() throws TotalADSReaderException {
			try {
				if (!fIsClose) {
                    fBufferedReader.close();
                }
			} catch (IOException e) {

				throw new TotalADSReaderException(e.getMessage());
			}

		}


	}
	//--------------------------------------------------------------------------------
	// Inner class ends
	//--------------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public TextLineTraceReader() {

	}

	@Override
	public ITraceTypeReader createInstance(){
		return new TextLineTraceReader();
	}


	@Override
	public String getName() {

		return Messages.TextLineTraceReader_TextReaderName;
	}

	/**
    * Returns the acronym of the text reader
    */
    @Override
    public String getAcronym(){

    	return "TXT"; //$NON-NLS-1$
    }

    /**
     * Returns the trace iterator
     */
	@Override
	public ITraceIterator getTraceIterator(File file) throws TotalADSReaderException {

		if (file==null) {
            throw new TotalADSReaderException(Messages.TextLineTraceReader_NoNull);
        }

		try {

			TextLineIterator textLineIterator=new TextLineIterator(file);
			return textLineIterator;

		} catch (FileNotFoundException e) {
			throw new TotalADSReaderException(e.getMessage());
		}
	}

	/**
	 * Registers Itself with the Trace Type Reader
	 * @throws TotalADSGeneralException A general exception from TotalADS
	 */
	 public static void registerTraceTypeReader() throws TotalADSGeneralException{
	    	TraceTypeFactory trcTypFactory=TraceTypeFactory.getInstance();
	    	TextLineTraceReader textFileReader=new TextLineTraceReader();
	    	trcTypFactory.registerTraceReaderWithFactory(textFileReader.getName(), textFileReader);
	    }
}
