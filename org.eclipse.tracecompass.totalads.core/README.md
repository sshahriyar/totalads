TotalADS is a framework to automatically diagnose anomalies from software systems in operations.
It	integrates different anomaly detection algorithms. It supports execution traces and logs in CTF, XML and text format. It supports live anomaly detection using trace streaming along with real time training and testing. It also supports variety of trace inspection views for forensic analysis, such as control flow of processes,resource usages and etc. It is developed in Java and provides extendible interfaces in Java to add new algorithms, trace formats, and views. Most importantly, it is available as an Eclipse Plugin.


Instructions to setup TotalADS and Tracecompass

1. First clone the source code:

        git clone https://github.com/sshahriyar/org.eclipse.tracecompass
        cd org.eclipse.tracecompass

  If you want to contribute to TotalADS, first fork the org.eclipse.tracecompass repository it into your account and clone it from there (just as shown above) instead of this repository.

2. Checkout the development, "develop", branch:

         git checkout develop

   There are two more branches master and staging. If you just want to compile and run, then use the master branch but to add new features you *must* only use the develop branch.

3. Install Eclipse from www.eclipse.org/downloads. You can start with any Eclipse pre-package, but I would suggest you download the Eclipse package named "Eclipse for RCP and RAP Developers". This package already contains the necessary plugins for Eclipse required by Tracecompass to compile. You can also download any other Eclipse distribution and install the necessary plugins as mentioned in step 4.

4. While following the instructions below in Step 4, you may have to install new Eclipse plugins/dependencies if you have not already downloaded "Eclipse for RCP and RAP Developers" package.You can install new dependencies in Eclipse either through "Help->Eclipse Marketplace" or "Help -> Install New Software". One of the options should work for installation of dependencies. Following instructions from 4.1 to 4.4 are taken from http://wiki.eclipse.org/Trace_Compass/Development_Environment_Setup.

	4.1 Import the Trace Compass projects into the workspace.

    Select "File -> Import..."

    Select "General -> Existing Projects into Workspace"

    Next to "Select root directory" click "Browse..."

    Navigate to the directory where you git cloned the project earlier. It should now list the available plugins.

    Make sure they are all checked and click  "Finish".

    The plugins should now be added to your workspace, and visible in the Package Explorer view on the left.

     You will probably get a bunch of build errors at this point. DON'T PANIC! This is because Trace Compass needs additional dependencies that might not be present in your current Eclipse installation. We will install those in the following section.

    4.2 Set the Target Platform.

     Eclipse offers the ability to set target platforms, which will download a specific environment to run your plugins, without having to "pollute" your Eclipse install. Trace Compass ships target definition files, which is the recommended way of installing its dependencies.

    4.3 To set the target platform for the Trace Compass plugins:

    Expand the "org.eclipse.tracecompass.target" project.

    Double-click the .target file that corresponds to your Eclipse version (tracecompass-e4.4.target, at the time of this writing).

    In the view that just opened, click Set as Target Platform on the top right.

     Eclipse will now download the required dependencies, which may take some time the first time.

     If you wish to switch target platforms in the future, you can come back to this plugin, or you can go to "Window -> Preferences -> Plug-in Development -> Target Platform". From that page, you can switch between any of the known targets, or your base Eclipse runtime.


    4.4 Build the documentation (optional)

    If you imported the *.help plugins (which contain the user and developer guides), you might notice warnings in these plugins because of missing files. It is because the documentation plugins need to be built using a separate component. If you do not care for the documentation, you can ignore those warnings, or even remove those plugins from your workspace.

    On the other hand, if you want to build or work on the documentation (which you should do if you add user-visible features!), here is how to build it.

    First you need to install the Mylyn-Wikitext Eclipse plugin:

    Go to "Help -> Install New Software..."
    Select "-- All Available Sites--"
    In the list of available plugins, select "Collaboration -> Mylyn Wikitext", and click Next/Finish to install it.
    Restart Eclipse

    You can now build the documentation plugins:

    Click on one of the *.help projects

    In the top menu, click "Project -> Build Project"

    After it is built, the warning should disappear, and the HTML files should be present in its doc/ sub-directory.

    Note that this builder does not run automatically ; Ant is not very smart at figuring out which files were changed, so it would end up constantly rebuilding the doc plugins for nothing. For this reason, if you modify the source (.mediawiki) files, you will have to rebuild the HTML manually, using the same method.

5. If you have not already installed it, install JDK 7 or higher for your operating system.

6. If JDK 7+ is not already in path of Eclipse, you may encounter errors. If not skip this step, else to add JDK 7+ to the path of Eclipse do the following:

	6.1. Go to Windows->Preference->Java->Installed JRES. Add JDK 7+ as an additional installed JRE.

    6.2. If you don't know where JDK 7+, then search in your "programs files" in Windows or look at the following path for Linux	  "/usr/lib/jvm/"

	6.3. Now right click on org.eclipse.tracecompass.totalads and select project properties

    Select Java Build Bath

    Select Libraries

    Select JRE System Library; (the one with the error status)

    If there is no error status just select the JRE System Library

    Click Edit and see if JRE is jdk 7+ or openjdk 7+; otherwise, click on Alternate JRE and select it.

7.  Install MongoDB

	Follow the instructions here on how to install MongoDB based on your OS: http://docs.mongodb.org/manual/installation/
	Simple installation instructions for a Linux system are as follows, they are also present on the above link:

	       curl -O http://downloads.mongodb.org/linux/mongodb-linux-x86_64-2.4.9.tgz

	       mkdir -p $HOME/mongodb

	       cp -R -n mongodb-linux-x86_64-2.4.9/ mongodb

	       mkdir -p data/db

	       Type gedit ~/.bashrc

	       And append the following ":your_path"/mongodb/bin" to PATH.

	       Replace your path with the actual path in the system and restart terminal

8. Start MongoDB. If you installed it directly on your OS from the URL in step 7, then it may have already been started or will start on a reboot automatically. An alternate way to start mongodb is as follows:
 	 cd mongodb
	 mongod --dbpath data/db/

9. Now right click on org.eclipse.tracecompass.totalads.core and run as Eclipse application. Once the new Eclipse instance is launched click on Windows->Perspective menu to open a new perspective TotalADS. The next time you can just select "Eclipse Application" from the Run (or Debug) icon in the toolbar.


10. You are all done setting up Tracecompass and TotalADS. User guide and a developer's guide for TotalADS are available at the following URLs:

        https://github.com/sshahriyar/org.eclipse.tracecompass/wiki/User-Guide
        https://github.com/sshahriyar/org.eclipse.tracecompass/wiki/Developer-Guide


