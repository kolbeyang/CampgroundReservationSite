# E-Store: Letchworth Campgrounds E-store

An online E-store system built in Java 17 
  
## Team

- Michael Oldziej
- Kolbe Yang
- Sherry Robinson
- Lianna Pottgen 
- Troy Wolf


## Prerequisites

- Java 17 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
- Angular CLI
- npm


## How to run it

1. Clone the repository and go to the root directory.
2. Go to estore-api directory
3. Execute `mvn compile exec:java`
4. Go to E-store-Angular directory
5. Run 'npm install' and then 'npm start'
6. Open in your browser `http://localhost:8080/`
7. Have a command prompt ready to run cURL commands

## Known bugs and disclaimers
(It may be the case that your implementation is not perfect.)
-Reservations can't overlap, however their is no message to the users that their reservation was unable to be made.
-When the owner reserves a campsite it doesn't cancel the reservations with that associated campsite

Document any known bug or nuisance.
If any shortcomings, make clear what these are and where they are located.

## How to test it

Run 'mvn clean test' from the estore-api directory

### For Windows:

Create/Add an new Campsite
`curl -X POST http://localhost:8080/campsites -H Content-Type:application/json -d "{\"name\":\"INSERT NAME HERE\", \"rate\":INSERT RATE HERE}" -i`

-Get all the products in the inventory
`curl -X GET "localhost:8080/campsites" -i`

-Delete the product with an ID 
`curl -X DELETE "localhost:8080/campsites/INSERTIDNUMBERHERE" -i`

-Get a certain product with the ID
`curl -X GET "localhost:8080/campsites/INSERTIDNUMBERHERE" -i`

-Update a certain product with a new name or ID
`curl -X PUT http://localhost:8080/campsites -H Content-Type:application/json -d "{\"id\":INSERT ID NAME HERE,\"name\":\"INSERT NAME HERE\", \"rate\":INSERT RATE HERE}" -i`

-Search a certain product using a string
`curl -X GET "localhost:8080/campsites/?name=INSERT STRING HERE" -i`


The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program 

1. Tester, first obtain the Acceptance Test plan
2. IP address of target machine running the app
3. Execute ________
4. ...
5. ...

## License

MIT License

See LICENSE for details.
