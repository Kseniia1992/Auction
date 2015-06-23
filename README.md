# Auction
The system consists of two subsystems: **gui** and **webService**.
Gui subsystem is used for representation. 
WebService subsystem is used for implementation of the system's logic.
# Installation
* JDK 1.6
* Apache Maven 2.2.1
* Apache-tomee-webprofile-1.7.1
* PostgreSQL 9.3.6

**Database settings:** name: auction, user: admin, password: admin. Tables will mapped automatically.

# Building
**mvn clean install**

#Deploying
Copy ./Auction/gui/target/auction.war to apache-tomee-webprofile-1.7.1/webapps

Run tomee: **apache-tomee-webprofile-1.7.1/bin/catalina.sh run**

**localhost:8080/auction**



