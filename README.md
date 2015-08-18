# Stock Exchange / Broker System

**Team 3:**

Gay Hazan (Lead)

Patrick Cristofaro 

Ross Smith

---

Want the best view of this file? View it on [GitHub](https://github.com/pcristo/mjc62311/tree/PM4)! 

## 1 - Preparing your system
This section will describe how to prepare your system to run the software.

### 1.1 - Environment
The software package has been tested successfully on Windows, Mac and Linux systems with both IntelliJ and Eclipse. However, 
for the purposes of this document it is assumed your system meets these requirements:

  * Linux-based desktop operating system
  * Eclipse Luna installed
  * Tomcat server installed (Version 7 recommended)

### 1.2 - Dependencies
To correctly run the software package, the following packages are required:

  * Oracle Java 8  
  * JUnit 4   
  * JSON, Camel, Hamcrest and Jackson JARS *(included)*
  * ant (if using build.xml to compile)
  * Tomcat server (Version 7 recommended)

### 1.3 - About
The application has a number of classes that must be run as separate servers to simulate the reliable distributed system.

Stock to purchase: (GOOG, AAPL, MSFT, YHOO)

Stock Type: (COMMON, CONVERTIBLE, PREFERRED)

### 1.4 - Configuring Eclipse
When you open the project in Eclipse for the first time, it is a good idea to make sure the build paths are correct, that all imports are correct, and that the applications custom settings match your system.

1. Right-click on the "Stock Exchange" project in Package Explorer and select Properties...
2. Under Java Build Path, select the Source tab.
3. Ensure that StockExchange/src and StockExchange/test_src are displayed
4. Open the Libraries tab.
5. Ensure "java-8-oracle" and "JUnit 4" are displayed. If they are not, click "Add Library" and follow the prompts 
to add them. **Note:** You must have Oracle Java 8 and JUnit 4 installed on your system for this application to function correctly.
6. Add all the JAR files from /resources/jar to the project build path.

The source code in Eclipse should now show no errors. You are ready to launch and test the software package.


## 2 - Launch procedure: Eclipse
The following sections will describe how to launch and test the software package.

### 2.1 - Update local settings (Config.json)
In the "src" folder you will find a file named Config.json. Open this file and update "projectHome" to point to the root folder of the Eclipse project. Be sure to end it with a slash symbol. For example:

>"projectHome": "/home/anyuser/mjc62311/",

Save the file.

### 2.2 - Prepare & Launch Tomcat

1.  In the Tomcat webapps directory create the following folders: 

		./project
		./project/WEB-INF
		./project/WEB-INF/classes
		./project/WEB-INF/lib
	
2. From the project source files, copy the contents of the './resources/jar' folder into lib
3. Copy the contents of the './out/' folder into classes
4. Copy web.xml from the root directory into WEB-INF
5. If wanting to view servlet debug, set "restDebugFile" in config.json to an output file **IMPORTANT:** Make sure the file exists and has permissions 777, or any calls to the RESTful service will fail!!

		chmod 777 /home/someFolder/DEBUG_BROKER_REST.log

6. Restart Tomcat

You are now ready to use the system. Refer to the documentation in ./resources/documentation for further details.

## Extra
### Javadoc
Full Javadoc has been generated for your convenience and is located at [this link](http://users.encs.concordia.ca/~patrickc/).

### Git Repository
The git repository is open to the public and can be reviewed at [GitHub](https://github.com/pcristo/mjc62311/tree/PM2b). Branch *"PM2b"* is the submission for the second project milestone.

### Change Log
Please see the file [changelog.txt](https://github.com/pcristo/mjc62311/blob/PM4/changelog.txt).

### Design Documentation
Further documenation is included with the source code

> ./resources/documentation
