
TotalADS is a framework to automatically diagnose anomalies from software systems in operations.
It	integrates different anomaly detection algorithms. It supports execution traces and logs in CTF, XML and text format. It supports live anomaly detection using trace streaming along with real time training and testing. It also supports variety of trace inspection views for forensic analysis, such as control flow of processes,resource usages and etc. It is developed in Java and provides extendible interfaces in Java to add new algorithms, trace formats, and views. Most importantly, it is available as an Eclipse Plugin.

Instructions to Compile TotalADS Using Maven

	Download the source code by cloning it into your hard disk:

	$ git clone https://github.com/sshahriyar/totalads

	Change your directory to totalads and install maven if you do not already have it.

	To build the plugins manually using Maven, simply run the following command from the totalads directory:

	$ mvn clean install

	The default command will compile and run the unit tests. Running the tests can take some time, to skip them you can append "-Dmaven.test.skip=true" to the 'mvn' command:

	$ mvn clean install -Dmaven.test.skip=true

	The RCP is not built by default, to build it you need to add "-Pbuild-rcp" to the 'mvn' command:

	$ mvn clean install -Pbuild-rcp -Dmaven.test.skip=true

	This will build the RCP for all supported architectures. The resulting archives will be placed in the directory totalads/org.eclipse.tracecompass.totalads.rcp.product/target/products

	Run the executable file from the above directory based on your OS, and to use TotalADS don't forget to install MongoDB (see below).

Instructions to setup TotalADS Using Eclipse

1. First clone the source code:

        git clone https://github.com/sshahriyar/totalads
        
        cd totalads

  If you want to contribute to TotalADS, first fork the "totalads" repository into your account and clone it from there,  instead of this repository. Later on, when your code is ready on Github, then send me pull requests over here. 

2. Checkout the development, "develop", branch:

         git checkout develop

   There are two more branches master and staging. If you just want to compile and run, then use the master branch but to add new features you *must* only use the develop branch.

3. Install Eclipse from www.eclipse.org/downloads. You can start with any Eclipse pre-package, but I would suggest you download the Eclipse package named "Eclipse for RCP and RAP Developers" and download Luna distribution. At the time of writing, the link for this distribution was: http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/lunasr2. This package already contains the necessary plugins for Eclipse required by TotalADS to compile. You can also download any other Eclipse distribution and install the necessary plugins  which would be needed in Step 4. You can install new dependencies in Eclipse either through "Help->Eclipse Marketplace" or "Help -> Install New Software". The first time you run it, it will ask for a workspace directory. You can use the default location. You will then be greeted by a welcome screen, which you can dismiss by clicking the Workbench arrow in the upper-right corner. 

4.  Following instructions from 4.1 to 4.4 are adopted with modifications from http://wiki.eclipse.org/Trace_Compass/Development_Environment_Setup.

    4.1 Import the TotalADS projects into the workspace.

    Select "File -> Import..."

    Select "General -> Existing Projects into Workspace"

    Next to "Select root directory" click "Browse..."

    Navigate to the directory where you git cloned the project earlier. It should now list the available plugins.

    Make sure they are all checked and click  "Finish".

    The plugins should now be added to your workspace, and visible in the Package Explorer view on the left.

     You will probably get a bunch of build errors at this point. DON'T PANIC! This is because TotalADS needs additional dependencies that might not be present in your current Eclipse installation. We will install those in the following section.

    4.2 Set the Target Platform.

     Eclipse offers the ability to set target platforms, which will download a specific environment to run your plugins, without having to "pollute" your Eclipse install. TotalADS also ships target definition files, which is the recommended way of installing its dependencies.

    4.3 To set the target platform for the TotalADS plugins:

    Expand the "org.eclipse.tracecompass.totalads.target" project.

    Double-click the .target file that corresponds to your Eclipse version (totalads-e4.4.target, at the time of this writing).

    In the view that just opened, click Set as Target Platform on the top right.

     Eclipse will now download the required dependencies, which may take some time the first time.

     
    4.4 Build the documentation (optional)

    If you imported the *.help plugins (which contain the user and developer guides), you might notice warnings in these plugins because of missing files. It is because the documentation plugins need to be built using a separate component. If you do not care for the documentation, you can ignore those warnings, or even remove those plugins from your workspace.

    On the other hand, if you want to build or work on the documentation (which you should do if you add user-visible features!), here is how to build it.

    First you need to install the Mylyn-Wikitext Eclipse plugin, if you are not using the RCP and RAP Eclipse package:

    Go to "Help -> Install New Software..."
    Select "-- All Available Sites--"
    In the list of available plugins, select "Collaboration -> Mylyn Wikitext", and click Next/Finish to install it.
    Restart Eclipse

    You can now build the documentation plugins:

    Click on one of the *.help projects

    In the top menu, click "Project -> Build Project"

    After it is built, the warning should disappear, and the HTML files should be present in its doc/ sub-directory.

    Note that this builder does not run automatically. Ant is not very smart at figuring out which files were changed, so it would end up constantly rebuilding the doc plugins for nothing. For this reason, if you modify the source (.mediawiki) files, you will have to rebuild the HTML manually, using the same method.

5. If you have not already installed it, install JDK 7 or higher for your operating system.

6. If JDK 7+ is not already in path of Eclipse, you may encounter errors. If not skip this step, else to add JDK 7+ to the path of Eclipse do the following:

    6.1. Go to Windows->Preference->Java->Installed JRES. Add JDK 7+ as an additional installed JRE.
    6.2. If you don't know where JDK 7+, then search in your "programs files" in Windows or look at the following path for Linux: "/usr/lib/jvm/".

    6.3. Now right click on org.eclipse.tracecompass.totalads.core and select project properties.

    Select Java Build Bath.

    Select Libraries.

    Select JRE System Library (the one with the error status).

    If there is no error status just select the JRE System Library

    Click Edit and see if JRE is one of JDK 7+ or OpenJDK 7+. If not then click on Alternate JRE and select it.

7.  Install MongoDB

	Follow the instructions here on how to install MongoDB based on your OS: http://docs.mongodb.org/manual/installation/
	Or  an alternate simple installation instructions for a Linux system are as follows, they are also present on the above link.

	       cd ~/
               curl -O http://downloads.mongodb.org/linux/mongodb-linux-x86_64-2.4.9.tgz

	        mkdir  mongodb

	       cp -R -n mongodb-linux-x86_64-2.4.9/ mongodb
               
               cd mongodb
               
               tar zxvf mongodb-linux-x86_64-2.4.9.tgz
	
	       mkdir -p data/db

	       Type gedit ~/.bashrc

	       Append the following ":your_path"/mongodb/bin" to PATH.

	       Replace your path with the actual path in the system and restart terminal

8. If you installed it directly on your OS from the URL in step 7, then it may have already been started or will start on a reboot automatically. An alternate way to start mongodb is as follows:
 	 cd ~/mongodb
	 mongod --dbpath data/db/

9. Now right click on org.eclipse.tracecompass.totalads.core and run as Eclipse application. Once the new Eclipse instance is launched click on Windows->Perspective menu to open a new perspective TotalADS. The next time you can just select "Eclipse Application" from the Run (or Debug) icon in the toolbar.


10. You are all done setting up TotalADS. Following are some important links:

     User Guide: https://github.com/sshahriyar/totalads/wiki/1.-Overview

     Developer Guide: https://github.com/sshahriyar/totalads/wiki/5.-Developer-Guide:-Adding-New-Algorithms-and-Trace-Types
  
     Binary Executables: https://github.com/sshahriyar/totalads/releases

     



