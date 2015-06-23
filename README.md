# Auction
The system consists of two subsystems: **gui** and **webService**.
Gui subsystem is used for representation. 
WebService subsystem is used for implementation of the system's logic.
# Installation
* JDK 1.6
* Apache Maven 2.2.1
* Apache-tomee-webprofile-1.7.0
* PostgreSQL 9.3.6

**Database settings:** name: auction, user: admin, password: admin. Tables will mapped automatically.

**TomEE settings:** 
* download and copy **postgresql-9.1-903.jdbc4.jar** to apache-tomee-webprofile-1.7.0/lib 
* add to apache-tomee-webprofile-1.7.0/conf/tomee.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<tomee>
<Resource id="Auction" type="DataSource">
    JdbcDriver  org.postgresql.Driver 
    JdbcUrl jdbc:postgresql://localhost:5432/auction 
    UserName    admin
    Password    admin 
</Resource>
</tomee>
```
# Building
**mvn clean install**

#Deploying
Copy ./Auction/gui/target/auction.war to apache-tomee-webprofile-1.7.1/webapps

Run tomee: **apache-tomee-webprofile-1.7.1/bin/catalina.sh run**

**localhost:8080/auction**



