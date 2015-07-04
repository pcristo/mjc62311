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
for the purposes of this document it is assumed your system is:

  * A Linux-based desktop operating system
  * Running Eclipse Luna

### 1.2 - Dependencies
To correctly run the software package, the following packages are required:

  * Oracle Java 8  
  * JUnit 4   
  * OpenORB 1.4 JAR *(included)*  
  * JSON JAR *(included)*
  * ant (if using build.xml to compile)


### 1.3 - About
The application has a FrontEnd class, which when ran will locally will start the servers and provide a menu to purchase
shares.  This is a simple UI with no checks:

Stock to purchase: (GOOG, AAPL, MSFT, YHOO)

Stock Type: (COMMON, CONVERTIBLE, PREFERRED)

The application is better to be run from the test suite which will perform numerous operations regarding purchasing and selling shares.
The test suite has an integration class which runs everything over Corba and is slow (due to constantly starting and stopping servers)
All other test classes test locally without a CORBA connection.

### 1.4 - Configuring Eclipse
When you open the project in Eclipse for the first time, it is a good idea to make sure the build paths are correct, that all imports are correct, and that the applications custom settings match your system.

1. Right-click on the "Stock Exchange" project in Package Explorer and select Properties...
2. Under Java Build Path, select the Source tab.
3. Ensure that StockExchange/src and StockExchange/test_src are displayed
4. Open the Libraries tab.
5. Ensure "java-8-oracle" and "JUnit 4" are displayed. If they are not, click "Add Library" and follow the prompts 
to add them. **Note:** You must have Oracle Java 8 and JUnit 4 installed on your system for this application to function correctly.
6. Add "openorb_orb-1.4.0.jar" if it is not already included. Click "Add JAR" and navigate to /resources/jar to find it.
7. Add "json-20141113.jar" if it is not already included. Click "Add JAR" and navigate to /resources/jar to find it.

The source code in Eclipse should now show no errors. You are ready to launch and test the software package.


## 2 - Launch procedure - Eclipse
The following sections will describe how to launch and test the software package.

### 2.1 - Update local settings (Config.json)
In the "src" folder you will find a file named Config.json. Open this file and update "projectHome" to point to the root folder of the Eclipse project. Be sure to end it with a slash symbol. For example:

>"projectHome": "/home/anyuser/mjc62311/",

Save the file.

### 2.2 - Launch the Naming Service
Assuming the CORBA naming service is going to run locally, open a terminal window and launch the CORBA naming service on the port from the previous section. **Note:** Some configurations may require you to run this command as root.

> $ tnameserv -ORBIntialPort 900

### 2.3 - Run JUnit Tests
You are now ready to run the unit tests found under the /test_src folder in Eclipse. Additional test details are available in the [Javadoc](http://users.encs.concordia.ca/~patrickc/).

To run all the tests in one shot, simply launch "test_src/TestSuite.java". When running the test suite, there is no need to manually set up all the servers as this will be done for you.

If you run tests individually, you will need to launch the servers manually by running "src/projectLauncher.java" first.

## 3 - Optional Launch Procedure - Command Line Option
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

---

### Demo Questions
#### What is narrow()? What does it do?
The narrow method is used to cast a CORBA object reference to its proper type.[1]

For example:

In the Exchange we need to keep a registry of all the businesses that register with us. The register business method calls a getBusinessIface method that maps a business to its Corba object reference, but we can’t have it as a Corba object reference we need to be able to access the business’s methods.

The following line of code is uses the narrow() method from the interface_businessHelper class to casts the Corba object reference to interface_business object, we can now use the iBusiness created object to access the methods from the business.

>interface_business iBusiness =
> (interface_business) interface_businessHelper.narrow(ncRef.resolve_str("business"+businessName.toUpperCase()));

#### Why use UDP for the logger? Why not RMI or CORBA?
UDP is much lighter and easier to implement than RMI or CORBA. Since we are just passing messages, it doesn't really make sense to add all the overhead of remote methods or remote objects.


References * :

[1] https://docs.oracle.com/javase/8/docs/technotes/guides/idl/tutorial/GSserver.html 