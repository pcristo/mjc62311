# Stock Exchange / Broker System

**Team 3:**

Gay Hazan (Lead)

Patrick Cristofaro 

Ross Smith

---

Want the best best of this file? View it on [GitHub](https://github.com/pcristo/mjc62311/tree/PM2b)! 

## 1 - Preparing your system
This section will describe how to prepare your system to run the software.

### 1.1 - Environment
The software package has been tested successfully on Windows, Mac and Linux systems with both IntelliJ and Eclipse. However, 
for the purposes of this document it is assumed your system meets these requirements:

  * Linux-based desktop operating system
  * Eclipse Luna installed
  * Tomcat server installed

### 1.2 - Dependencies
To correctly run the software package, the following packages are required:

  * Oracle Java 8  
  * JUnit 4   
  * JSON, Camel, Hamcrest and Jackson JARS *(included)*
  * ant (if using build.xml to compile)
  * Tomcat server

### 1.3 - About
The application has a FrontEnd class, which when ran will locally will start the servers (except for the REST server 
used by the Broker) and provide a menu to purchase shares.  This is a simple UI with no checks:

Stock to purchase: (GOOG, AAPL, MSFT, YHOO)

Stock Type: (COMMON, CONVERTIBLE, PREFERRED)

It will be easier to run the application from the test suite which will perform numerous operations regarding purchasing and selling shares.

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
5. If wanting to view servlet debug, set "restDebugFile" in config.json to an output file
6. Restart Tomcat


### 2.3 - Run JUnit Tests
You are now ready to run the unit tests found under the /test_src folder in Eclipse. Additional test details are available in the [Javadoc](http://users.encs.concordia.ca/~patrickc/).

To run all the tests in one shot, simply launch "test_src/TestSuite.java". When running the test suite, there is no need to manually set up all the servers as this will be done for you, with the exception of the Tomcat server which you must start yourself.

## 3 - Launch Procedure: Command Line Using Apache Ant
We have provided a way to compile and run the software package using Apache Ant. The following procedure requires that Ant be installed.

1. Update config.json (specifically the first line (project home) to point to you working directory)
2. Navigate to project root and run ant compile_test (This will compile the source, the tests and create a jar of the source in dist/lib)
3. To run test suite: 3.1.  Navigate to test_out and run java org.junit.runner.JunitCore TestSuite (You will need to update your classpath to include resources/jar/* and dist/lib/*
4. To run front end: Navigate to out and run java FrontEnd.FrontEnd (You will need to update your classpath to include resources/jar/* and dist/lib/*

---

## Extra
### Javadoc
Full Javadoc has been generated for your convenience and is located at [this link](http://users.encs.concordia.ca/~patrickc/).

### Git Repository
The git repository is open to the public and can be reviewed at [GitHub](https://github.com/pcristo/mjc62311/tree/PM2b). Branch *"PM2b"* is the submission for the second project milestone.

### Design Documentation
Further documenation is included with the source code

> ./resources/documentation
