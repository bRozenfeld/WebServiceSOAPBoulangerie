# Bakery Management
WebServiceSOAPBoulangerie

## Requirements
* java 11 
* maven

## Project Architecture
* PaymentsWS : web service for payment
* ProductsWS : web service to manage products and user commands
* UsersWS : web service to register, login, logout users
* Common : class common and required by the others module
* WarModule : module used to generate the deployment file

## Installation
docker run
-p 127.0.0.1:8080:8080
-v $(pwd)/target:/usr/local/tomcat/webapps/
--name tomcat
tomcat:9.0.12-jre10-slim

## Web Services available
* $deployment http://localhost:8080/Bakery-1.0/users
* $deployment http://localhost:8080/Bakery-1.0/products
* $deployment http://localhost:8080/Bakery-1.0/payments
