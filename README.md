# CashDispenserApp
This is a cash dispensing application for use in an ATM or similar device. 

## For building and running the application you need:

**JDK 1.8**

**Maven 3**

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.zealousdev.cashdispenser.CashdispenserApplication class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:

*mvn spring-boot:run*

## Automated Build

The automated build for this project is achieved using maven. To build a jar file for this application:

Navigate to the folder with the pom.xml file and run the following command:

*mvn clean*

*mvn package*

## Automated Test Suite

The automated test suite is part of the build process

## Libraries

Since it is a spring boot application, all libraries come packaged as part of the application jar
