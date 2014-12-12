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
/**
 * This is an independent class that is used to extract system calls and their mappings to modules (states) from the source code.
 * Follow these instructions in Ubuntu to extract the mappings of from kernel's source code:
 * 1. Install ctags
 * 		> sudo apt-get install ctags
 * 2.  Get the Ubuntu (or any other Linux distribution) from the version control
 * 		> git clone git://kernel.ubuntu.com/ubuntu-DISTRIBUTION_NAME.git
 * 			e.g., > git clone git://kernel.ubuntu.com/ubuntu-precise.git
 * 3. cd ubuntu-DISTRIBUTION_NAME and type the following
 * 		> ctags --fields=afmikKlnsStz --c-kinds=+pc -R
 *    This generates a tag file
 * 4. Find the system call table or system calls list in the source code
 * 		older versions
 * 		a.  For 32bit: arch/x86/kernel/include/unistd_32.h    or arch/x86/kernel/syscall_table_32.S
 * 		b.  For 64 bit:	arch/x86/kernel/include/unistd_64.h
 * 		latest versions
 *      a.  For 32 bit: arch/x86/syscalls/syscall_32.tbl
 *      b.  For 64 bit: arch/x86/syscalls.syscall_64.tbl
 * 5. You need to clean these tables such that there is only one system call per line in the file. If you see the names starting
 * 	  with NR_ or stubx_32 then replace them with sys_ or compat_sys. You can figure this out easily by looking at the tables
 * 	  files I mentioned above. Files ending with .S or .tbl have all the correct names. Extract the columns  starting with sys or
 *    compat_ if there are multiple columns in a file. Once files are cleaned pass them to the following shell script
 * 6. Copy the following shell script in a new file and write the correct paths in it of cleaned system_call table, the tags file,
 *    and the output file for mapping. You need to repeat the process for each of the 32 bit and 64 bit system call table.
 * 7.  For other  architectures you need to again repeat the whole process from sytem calls list to the shell script.
 *
 #!/bin/sh
 SYSTEM_CALL_LIST="/home/umroot/experiments/syscallmappings/syscall_x86_u14.04_3.13_64.txt"
 OUTPUTFILE="/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u14.04_3.13.csv"
 TAGS_FILE="/home/umroot/Downloads/ubuntu-source/ubuntu-trusty/tags"

 cat  $SYSTEM_CALL_LIST| while read line
 do
 prefix=`echo $line|grep -o 'compat_'`
 line=`echo $line| sed 's/sys_//'`
 line=`echo $line| sed 's/stub_//'`
 line=`echo $line| sed 's/ptregs_//'`
 line=`echo $line| sed 's/compat_//'`

 if [[ -z $prefix ]]; then
 module=`grep "SYSCALL_DEFINE[0-9]*($line[,)]" $TAGS_FILE| sed 's/\s\+/,/g'| cut -d, -f2`

 else
 module=`grep "COMPAT_SYSCALL_DEFINE[0-9]*($line[,)]" $TAGS_FILE| sed 's/\s\+/,/g'| cut -d, -f2`
 if [[ -z $module ]]; then # if COMPAT is not found then search for SYSCALL_DEFINE
 module=`grep "SYSCALL_DEFINE[0-9]*($line[,)]" $TAGS_FILE| sed 's/\s\+/,/g'| cut -d, -f2`
 fi
 fi

 prefix=`echo $prefix"sys_"`
 echo "$prefix$line,$module"
 `echo "$prefix$line,$module" >> $OUTPUTFILE`
 done

 8. Once all the mapping files are generated pass them on to this class and run it. It will create a combine list
 grouped by each module and if there are any duplicates, it will show them. You can use this list to put in
 Kernel state Modeling class

 */

package org.eclipse.tracecompass.internal.totalads.algorithms.ksm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.tracecompass.internal.totalads.algorithms.ksm.KSMSyscallStateMapper;

/**
 * This is an independent class that is used to extract system calls and their
 * mappings to modules (states) from the source code's mapping files generated
 * using the instructions written in the comments of this class.
 *
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
class KSMSyscallStateMapper {
    private BufferedReader buffReader;
    private HashMap<String, HashSet<String>> list;

    /**
     * Constructor
     */
    public KSMSyscallStateMapper() {

        list = new HashMap<>();

    }

    /**
     * Extracts states and system calls
     *
     * @param path
     *            Path
     * @throws IOException
     *             I/O exception
     */
    public void extractStatesAndCalls(String path) throws IOException {
        int uniquecounter = 0;
        String line;
        System.out.println("\n ****************Processing new file************"); //$NON-NLS-1$
        buffReader = new BufferedReader(new FileReader(new File(path)));
        while ((line = buffReader.readLine()) != null) {
            String[] elements = line.split(","); //$NON-NLS-1$
            if (elements.length == 2) {

                elements[1] = elements[1].replaceAll("/.*", ""); //$NON-NLS-1$ //$NON-NLS-2$
                // System.out.print("\""+elements[0]+"\", ");
                HashSet<String> states = list.get(elements[0]);
                if (states == null) {
                    uniquecounter++;
                    states = new HashSet<>();
                }

                states.add(elements[1]);
                list.put(elements[0], states);
            }
            else {
                if (!elements[0].equalsIgnoreCase("sys_ni_syscall") && !elements[0].equalsIgnoreCase("sys_") && //$NON-NLS-1$ //$NON-NLS-2$
                        (elements[0].startsWith("sys") || elements[0].startsWith("compat"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    System.out.println(elements[0] + ", not found"); //$NON-NLS-1$
                    HashSet<String> states = list.get(elements[0]);
                    if (states == null) { // if syscall is not already there
                                          // with another state only then add
                                          // not found state
                        states = new HashSet<>();
                        states.add("not found"); //$NON-NLS-1$
                        list.put(elements[0], states);
                    }

                }

            }
        }
        System.out.println("\n******** Unique  Counter " + uniquecounter + " " + list.size()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * prints states
     */
    public void printStatesAndCalls() {
        System.out.println("\n ****************Print Duplicate States for Calls************"); //$NON-NLS-1$
        for (Map.Entry<String, HashSet<String>> values : list.entrySet()) {
            if (values.getValue().size() > 1) {
                System.out.println("\n" + values.getKey()); //$NON-NLS-1$
                Iterator<String> it = values.getValue().iterator();
                while (it.hasNext()) {
                    System.out.print(it.next() + ", "); //$NON-NLS-1$
                }
            }

        }
        System.out.println("\n ****************Printing Single Sates and Calls************"); //$NON-NLS-1$
        printSingleStatesAndCalls("arch"); //$NON-NLS-1$
        printSingleStatesAndCalls("kernel"); //$NON-NLS-1$
        printSingleStatesAndCalls("mm"); //$NON-NLS-1$
        printSingleStatesAndCalls("not found"); //$NON-NLS-1$
        printSingleStatesAndCalls("net"); //$NON-NLS-1$
        printSingleStatesAndCalls("fs"); //$NON-NLS-1$
        printSingleStatesAndCalls("ipc"); //$NON-NLS-1$
        printSingleStatesAndCalls("security"); //$NON-NLS-1$
    }

    /**
     * Prints all the system calls in a state
     *
     * @param prevState
     */
    private void printSingleStatesAndCalls(String prevState) {

        System.out.print("\n" + prevState + "\n "); //$NON-NLS-1$ //$NON-NLS-2$

        for (Map.Entry<String, HashSet<String>> values : list.entrySet()) {

            // if (values.getValue().size() ==1){

            Iterator<String> it = values.getValue().iterator();
            while (it.hasNext()) {
                String currenState = it.next();
                if (currenState.equals(prevState)) {
                    System.out.print("\"" + values.getKey() + "\", "); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            // }

        }

    }

    /**
     * Main to run the code
     *
     * @param args
     *            Arguments
     */
    public static void main(String args[]) {
        try {
            KSMSyscallStateMapper temp = new KSMSyscallStateMapper();
            // temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/nametocomponent_32_u10.04_2.6.32.csv");
            // temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/nametocomponent_64_u10.04_2.6.32.csv");
            // 1% conflict with above n all below
            // ---
            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u10.10_2.6.35.csv"); //$NON-NLS-1$
            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u10.10_2.6.35.csv"); //$NON-NLS-1$
            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u12.04_3.2.csv"); //$NON-NLS-1$
            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u12.04_3.2.csv"); //$NON-NLS-1$

            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u12.10_3.5.csv"); //$NON-NLS-1$
            temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u12.10_3.5.csv"); //$NON-NLS-1$

            /*
             * temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u13.04_3.8.0.csv"
             * ); temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u13.04_3.8.0.csv"
             * );
             *
             * temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u13.10_3.11.csv"
             * ); temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u13.10_3.11.csv"
             * ); /*temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_32_u14.04_3.13.csv"
             * ); temp.extractStatesAndCalls(
             * "/home/umroot/experiments/syscallmappings/script-mapping/nametocomponent_64_u14.04_3.13.csv"
             * );
             */

            // temp.extractStatesAndCalls("/home/umroot/experiments/syscallmappings/nametocomponent_m32r_u10.04_2.6.32.csv");
            //

            temp.printStatesAndCalls();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
