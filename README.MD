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

## Web Services available
* $deployment address$/bakery/users
* $deployment address$/bakery/products
* $deployment address$/bakery/payments
