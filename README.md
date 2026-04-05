# User API + PokeAPI Pokemon name consumer Challenge

You have to build a microservice that exposes a REST API with two different tables, users and states. 
Both tables should be open to creation, deletion, or update. Every request must only 
accept this `Content-type: application/json`. 

### Badges

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/3VQFRDsiSjeHHg9ASnahfn/W9HrkTVEbxjRxVupqE7yFe/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/circleci/3VQFRDsiSjeHHg9ASnahfn/W9HrkTVEbxjRxVupqE7yFe/tree/master)
[![Coverage Status](https://coveralls.io/repos/github/Rindoresu/API_Challenge_Project/badge.svg?branch=fixing_Coveralls)](https://coveralls.io/github/Rindoresu/API_Challenge_Project?branch=master)

### App Features

- Create new Users including their Pokemon IDs
- Get a Users list
- Get a User by User ID and their Pokemons by the Pokemon ID with their names taken from the PokeAPI.
- Update a User by ID
- Delete a User by ID

## Prerequisites
- Docker installed without SUDO permissions
- Docker compose installed without SUDO permissions
- Ports 8080 (APP) and 5432 (DB) must be unused locally, and open for inbound connections when accessed from other machines.


## How to Run the Application
On Linux/Mac terminal: 
```shell
chmod +x run-dev.sh
./run-dev.sh
```
On Windows PowerShell: 
```shell
wsl ./run-dev.sh
```

## Areas to Improve

- Test Data should be included in a Test Data file instead of the tests themselves.
- A generic Method that emulates the PokeAPI could be implemented to mock PokeAPI endpoints
- APP Error handling could give more information on the error displayed.
- Project lacks Seed migration file to include data on a new APP.
- ORM should be working on Migrations instead of Synchronize as is now.
- Deployment is missing
- Coveralls missing
- Circle CI missing
- Deployment missing

## Known Bugs

### Security issues

- Passwords are neither securely handled nor safely stored in the Database.
- Database connection credentials are not securely stored.

## Techs used

- Spring/SpringBoot
- Hibernate
- Liquibase
- Postgres
- JUnit/Mockito
- Maven
- Docker

## Decisions made

- Docker for portability.
- JUnit/Mockito as one of the most widely used testing frameworks, being the most tested, advanced and available in Java.
- As above, Hibernate is the most widely used ORM Java framework.

## URLs exposed

- [Swagger API URL](https://localhost:8080/swagger-ui) 

## application.properties should be defined based on the current environment

- src/main/resources/application.properties
- src/test/resources/application-test.properties