TotalADS is a framework to automatically diagnose anomalies from software systems in operations. It integrates different anomaly detection algorithms. It supports execution traces and logs in CTF, XML and text format. It supports live anomaly detection using trace streaming along with real time training and testing. It also supports variety of trace inspection views for forensic analysis, such as control flow of processes,resource usages and etc. It is developed in Java and provides extendible interfaces in Java to add new algorithms, trace formats, and views. Most importantly, it is available as an Eclipse Plugin.


MANUALLY COMPILING AND INSTALLING TOTALADS

Download the source code by cloning it into your hard disk:

$ git clone https://github.com/sshahriyar/totalads

Change your directory to totalads and install maven if you do not already have it.

To build the plugins manually using Maven, simply run the following command from
the totalads directory:

$ mvn clean install

The default command will compile and run the unit tests. Running the tests can
take some time, to skip them you can append "-Dmaven.test.skip=true" to the
'mvn' command:

$ mvn clean install -Dmaven.test.skip=true


The RCP is not built by default, to build it you need to add "-Pbuild-rcp" to
the 'mvn' command:

$ mvn clean install -Pbuild-rcp -Dmaven.test.skip=true

This will build the RCP for all supported architectures. The resulting archives
will be placed in the directory totalads/org.eclipse.tracecompass.totalads.rcp.product/target/products

Run the executable file from the above directory based on your OS, and to use TotalADS don't forget to install MongoDB. (MongoDB installation instructions are explained below in Step 7 and user's guide link is present below in Step 9).

SETTING UP DEVELOPMENT ENVIRONMENT IN ECLIPSE TO DEVELOP TOTALADS


1. First clone the source code:

git clone https://github.com/sshahriyar/totalads
cd org.eclipse.tracecompass
If you want to contribute to TotalADS, first fork the totalads repository it into your account and clone it from there (just as shown above) instead of this repository.

2. Checkout the development, "develop", branch:

 git checkout develop
There are two more branches master and staging. If you just want to compile and run, then use the master branch but to add new features you must only use the develop branch.

3. Download the Eclipse package named "Eclipse for RCP and RAP Developers"  from the Eclipse's website. This package already contains the necessary plugins for Eclipse required by TotalADS to compile. You can also download any other Eclipse distribution and install the necessary plugins as mentioned in step 4.

4. (Following instructions (4.1 to 4.4) are modified from http://wiki.eclipse.org/Trace_Compass/Development_Environment_Setup.)

4.1 Import the TotalADS projects into the workspace.

Select "File -> Import..."

Select "General -> Existing Projects into Workspace"

Next to "Select root directory" click "Browse..."

Navigate to the directory where you git cloned the project earlier. It should now list the available plugins.

Make sure they are all checked and click "Finish".

The plugins should now be added to your workspace, and visible in the Package Explorer view on the left.

You will probably get a bunch of build errors at this point. DON'T PANIC! This is because TotalADS needs additional dependencies that might not be present in your current Eclipse installation. We will install those in the following section.

4.2 Set the Target Platform.

Eclipse offers the ability to set target platforms, which will download a specific environment to run your plugins, without having to "pollute" your Eclipse install. TotalADS ships target definition files, which is the recommended way of installing its dependencies.

4.3 To set the target platform for the TotalADS plugins:

Expand the "org.eclipse.tracecompass.totalads.target" project.

Double-click the .target file totalads-e4.4.target.

In the view that just opened, click Set as Target Platform on the top right.

Eclipse will now download the required dependencies, which may take some time the first time.


4.4 Build the documentation (optional)

If you imported the *.help plugins (which contain the user and developer guides), you might notice warnings in these plugins because of missing files. It is because the documentation plugins need to be built using a separate component. If you do not care for the documentation, you can ignore those warnings, or even remove those plugins from your workspace.

On the other hand, if you want to build or work on the documentation, here is how to build it.

Click on one of the *.help projects

In the top menu, click "Project -> Build Project"

After it is built, the warning should disappear, and the HTML files should be present in its doc/ sub-directory.

Note that this builder does not run automatically; Ant is not very smart at figuring out which files were changed, so it would end up constantly rebuilding the doc plugins for nothing. For this reason, if you modify the source (.mediawiki) files, you will have to rebuild the HTML manually, using the same method.

5. If you have not already installed it, install JDK 7 or higher for your operating system.

6. If JDK 7+ is not already in path of Eclipse, you may encounter errors. If not skip this step, else to add JDK 7+ to the path of Eclipse do the following:

6.1. Go to Windows->Preference->Java->Installed JRES. Add JDK 7+ as an additional installed JRE.

6.2. If you don't know where JDK 7+, then search in your "programs files" in Windows or look at the following path for Linux "/usr/lib/jvm/"

6.3. Now right click on org.eclipse.tracecompass.totalads.core and select project properties

Select Java Build Bath

Select Libraries

Select JRE System Library; (the one with the error status)

If there is no error status just select the JRE System Library

Click Edit and see if JRE is jdk 7+ or openjdk 7+; otherwise, click on Alternate JRE and select it.

7. Install MongoDB

Follow the instructions here on how to install MongoDB based on your OS: http://docs.mongodb.org/manual/installation/. Simple installation instructions for a Linux system are as follows:

   curl -O http://downloads.mongodb.org/linux/mongodb-linux-x86_64-2.4.9.tgz

   mkdir -p $HOME/mongodb

   cp -R -n mongodb-linux-x86_64-2.4.9/ mongodb

   mkdir -p data/db

   Type gedit ~/.bashrc

   And append the following ":your_path"/mongodb/bin" to PATH.

   Replace your path with the actual path to mongodb folder in your system and restart terminal
Start MongoDB. If you installed it directly on your OS from the URL in step 7, then it may have already been started or will start on a reboot automatically (make sure that journaling is enabled, otherwise you may encounter error with TotalADS). An alternate way to start mongodb is as follows: cd mongodb mongod --dbpath data/db/

8. Now right click on org.eclipse.tracecompass.totalads.core and run as Eclipse application. Once the new Eclipse instance is launched, you can open different perspectives from Windows->Perspective menu. The next time you can just select "Eclipse Application" from the Run (or Debug) icon in the toolbar.

9. You are all done setting up TotalADS. User guide and a developer's guide for TotalADS are available at the following URLs:

https://github.com/sshahriyar/totalads/wiki/User-Guide
https://github.com/sshahriyar/totalads/wiki/Developer-Guide




