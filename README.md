# Stock Exchange / Broker System

**Team 3:**

Gay Hazan (Lead)

Patrick Cristofaro 

Ross Smith

~~Sai Sun~~

---

## 1 - Preparing your system
This section will describe how to prepare your system to run the software.

### 1.1 - Dependencies
To correctly run the software package, the following packages are required:
  * Oracle Java 8
  * JUnit 4 
  * OpenORB 1.4 JAR *(included)*
  * JSON JAR *(included)*

### 1.2 - Environment
The software package has been tested successfully on Windows, Mac and Linux systems with both IntelliJ and Eclipse. However, 
for the purposes of this document it is assumed your system is:
  * A Linux-based desktop operating system
  * Running Eclipse Luna

#### 1.2.1 - Configuring Eclipse
When you open the project in Eclipse for the first time, it is a good idea to make sure the build paths are correct, 
that all imports are correct, and that the applications custom settings match your system.

1. Right-click on the "Stock Exchange" project in Package Explorer and select Properties...
2. Under Java Build Path, select the Source tab.
3. Ensure that StockExchange/src and StockExchange/test_src are displayed
4. Open the Libraries tab.
5. Ensure "java-8-oracle" and "JUnit 4" are displayed. If they are not, click "Add Library" and follow the prompts 
to add them. **Note:** You must have Oracle Java 8 and JUnit 4 installed on your system for this application to function correctly.
6. Add "openorb_orb-1.4.0.jar" if it is not already included. Click "Add JAR" and navigate to /resources/jar to find it.
7. Add "json-20141113.jar" if it is not already included. Click "Add JAR" and navigate to /resources/jar to find it.

The source code in Eclipse should now show no errors. You are ready to launch and test the software package.


## 2 - Launch procedure
The following sections will describe how to launch and test the software package.

### 2.1 - Update local settings (Config.json)
In the "src" folder you will find a file named Config.json. Open this file and update "projectHome" to point to the root folder of the Eclipse project. Be sure to end it with a slash symbol. For example:

```JSON
"projectHome": "/home/anyuser/mjc62311/",
```

Save the file, then scroll to the field named *namingServicePort* and take note of the value (typically 9999).  This is the port to use for the naming service. Confirm that the *namingServiceAddr* value is *localhost*, and if it is not, change it.

### 2.2 - Launch the Naming Service
Assuming the CORBA naming service is going to run locally, open a terminal window and launch the CORBA naming service on the port from the previous section. **Note:** Some configurations may require you to run this command as root.

```
$ orbd -ORBIntialPort 9999 - ORBInitialHost localhost
```

### 2.3 - Run JUnit Tests
You are now ready to run the unit tests found under the /test_src folder in Eclipse. Complete descriptions of each test are available in the [Javadoc](http://users.encs.concordia.ca/~patrickc/), however to get a quick feel for the application, run </i>IntegrationTest</i> inside the default package and observe the console output.

---
## Extra
### Javadoc
Full Javadoc has been generated for your convenience and is located at [this link](http://users.encs.concordia.ca/~patrickc/).
### Logging
Remote log file can be viewed [here](http://166.78.186.20/resources/log/displayLog.html). If you experience difficulty connecting to the logging server, you can disable all remote logging in the Config.json file by changing the following parameter:
> "loggingDisabled": "true"

### Git Repository
The git repository is open to the public and can be reviewed at [GitHub](https://github.com/pcristo/mjc62311/tree/PM2b). Branch *"PM2b"* is the submission for the second project milestone.

### Design Documentation
Further documenation is included with the source code

```HTTP
./resources/documentation
```


